package com.hola.hola.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.ByteString;

/**
 * Fluent API to build <a href="http://www.ietf.org/rfc/rfc2387.txt">RFC
 * 2387</a>-compliant request bodies.
 */
public final class FixedMultipartBuilder {

    private static final byte[] COLONSPACE = { ':', ' ' };
    private static final byte[] CRLF = { '\r', '\n' };
    private static final byte[] DASHDASH = { '-', '-' };

    private final ByteString boundary;
    private MediaType type = MultipartBody.MIXED;

    // Parallel lists of nullable headers and non-null bodies.
    private final List<Headers> partHeaders = new ArrayList<>();
    private final List<RequestBody> partBodies = new ArrayList<>();

    /** Creates a new multipart builder that uses a random boundary token. */
    public FixedMultipartBuilder() {
        this(UUID.randomUUID().toString());
    }

    /**
     * Creates a new multipart builder that uses {@code boundary} to separate
     * parts. Prefer the no-argument constructor to defend against injection
     * attacks.
     */
    public FixedMultipartBuilder(String boundary) {
        this.boundary = ByteString.encodeUtf8(boundary);
    }

    /**
     * Set the MIME type. Expected values for {@code type} are
     * {@link okhttp3.MultipartBody#MIXED} (the default),
     * {@link okhttp3.MultipartBody#ALTERNATIVE},
     * {@link okhttp3.MultipartBody#DIGEST},
     * {@link okhttp3.MultipartBody#PARALLEL} and
     * {@link okhttp3.MultipartBody#FORM}.
     */
    public FixedMultipartBuilder type(MediaType type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        if (!type.type().equals("multipart")) {
            throw new IllegalArgumentException("multipart != " + type);
        }
        this.type = type;
        return this;
    }

    /** Add a part to the body. */
    public FixedMultipartBuilder addPart(RequestBody body) {
        return addPart(null, body);
    }

    /** Add a part to the body. */
    public FixedMultipartBuilder addPart(Headers headers, RequestBody body) {
        if (body == null) {
            throw new NullPointerException("body == null");
        }
        if (headers != null && headers.get("Content-Type") != null) {
            throw new IllegalArgumentException("Unexpected header: Content-Type");
        }
        if (headers != null && headers.get("Content-Length") != null) {
            throw new IllegalArgumentException("Unexpected header: Content-Length");
        }

        partHeaders.add(headers);
        partBodies.add(body);
        return this;
    }

    /**
     * Appends a quoted-string to a StringBuilder.
     *
     * <p>RFC 2388 is rather vague about how one should escape special characters
     * in form-data parameters, and as it turns out Firefox and Chrome actually
     * do rather different things, and both say in their comments that they're
     * not really sure what the right approach is. We go with Chrome's behavior
     * (which also experimentally seems to match what IE does), but if you
     * actually want to have a good chance of things working, please avoid
     * double-quotes, newlines, percent signs, and the like in your field names.
     */
    private static StringBuilder appendQuotedString(StringBuilder target, String key) {
        target.append('"');
        for (int i = 0, len = key.length(); i < len; i++) {
            char ch = key.charAt(i);
            switch (ch) {
                case '\n':
                    target.append("%0A");
                    break;
                case '\r':
                    target.append("%0D");
                    break;
                case '"':
                    target.append("%22");
                    break;
                default:
                    target.append(ch);
                    break;
            }
        }
        target.append('"');
        return target;
    }

    /** Add a form data part to the body. */
    public FixedMultipartBuilder addFormDataPart(String name, String value) {
        return addFormDataPart(name, null, RequestBody.create(null, value));
    }

    /** Add a form data part to the body. */
    public FixedMultipartBuilder addFormDataPart(String name, String filename, RequestBody value) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        StringBuilder disposition = new StringBuilder("form-data; name=");
        appendQuotedString(disposition, name);

        if (filename != null) {
            disposition.append("; filename=");
            appendQuotedString(disposition, filename);
        }

        return addPart(Headers.of("Content-Disposition", disposition.toString()), value);
    }

    /** Assemble the specified parts into a request body. */
    public RequestBody build() {
        if (partHeaders.isEmpty()) {
            throw new IllegalStateException("Multipart body must have at least one part.");
        }
        return new MultipartRequestBody(type, boundary, partHeaders, partBodies);
    }

    private static final class MultipartRequestBody extends RequestBody {
        private final ByteString boundary;
        private final MediaType contentType;
        private final List<Headers> partHeaders;
        private final List<RequestBody> partBodies;

        public MultipartRequestBody(MediaType type, ByteString boundary, List<Headers> partHeaders,
                                    List<RequestBody> partBodies) {
            if (type == null) throw new NullPointerException("type == null");

            this.boundary = boundary;
            this.contentType = MediaType.parse(type + "; boundary=" + boundary.utf8());
            this.partHeaders = Util.immutableList(partHeaders);
            this.partBodies = Util.immutableList(partBodies);
        }

        @Override public MediaType contentType() {
            return contentType;
        }

        private long contentLengthForPart(Headers headers, RequestBody body) throws IOException {
            // Check if the body has an contentLength != -1, otherwise cancel!
            long bodyContentLength = body.contentLength();
            if(bodyContentLength < 0L) {
                return -1L;
            }

            long length = 0;

            length += DASHDASH.length + boundary.size() + CRLF.length;

            if (headers != null) {
                for (int h = 0, headerCount = headers.size(); h < headerCount; h++) {
                    length += headers.name(h).getBytes().length
                            + COLONSPACE.length
                            + headers.value(h).getBytes().length
                            + CRLF.length;
                }
            }

            MediaType contentType = body.contentType();
            if (contentType != null) {
                length += "Content-Type: ".getBytes().length
                        + contentType.toString().getBytes().length
                        + CRLF.length;
            }

            length += CRLF.length;
            length += bodyContentLength;
            length += CRLF.length;

            return length;
        }

        @Override public long contentLength() throws IOException {
            long length = 0;

            for (int p = 0, partCount = partHeaders.size(); p < partCount; p++) {
                long contentPartLength = contentLengthForPart(partHeaders.get(p), partBodies.get(p));

                if(contentPartLength < 0) {
                    // Too bad, can't get contentPartLength!
                    return -1L;
                }

                length += contentPartLength;
            }

            length += DASHDASH.length + boundary.size() + DASHDASH.length + CRLF.length;

            return length;
        }

        @Override public void writeTo(BufferedSink sink) throws IOException {
            for (int p = 0, partCount = partHeaders.size(); p < partCount; p++) {
                Headers headers = partHeaders.get(p);
                RequestBody body = partBodies.get(p);

                sink.write(DASHDASH);
                sink.write(boundary);
                sink.write(CRLF);

                if (headers != null) {
                    for (int h = 0, headerCount = headers.size(); h < headerCount; h++) {
                        sink.writeUtf8(headers.name(h))
                                .write(COLONSPACE)
                                .writeUtf8(headers.value(h))
                                .write(CRLF);
                    }
                }

                MediaType contentType = body.contentType();
                if (contentType != null) {
                    sink.writeUtf8("Content-Type: ")
                            .writeUtf8(contentType.toString())
                            .write(CRLF);
                }

                // Skipping the Content-Length for individual parts

                sink.write(CRLF);
                partBodies.get(p).writeTo(sink);
                sink.write(CRLF);
            }

            sink.write(DASHDASH);
            sink.write(boundary);
            sink.write(DASHDASH);
            sink.write(CRLF);
        }
    }
}
