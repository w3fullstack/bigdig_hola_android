package com.hola.hola.util.files;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hola.hola.REST.BooleanDeserializer;
import com.hola.hola.REST.HolaApi;
import com.hola.hola.model.Chat;
import com.hola.hola.util.FixedMultipartBuilder;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.model.DocumentUploadResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploader {
    private static final MediaType MEDIA_TYPE_FILE = MediaType.parse("file/*");

    private FileUploader() {
    }

    public static Single<DocumentUploadResponse> upload(Context context, Uri uri) {
        String p = FileUtils.getRealPathFromUri(context, uri);
        if(p == null)  p = FileUtils.getPath(uri, context);
        final String realPath = p;
        final String originalName = Uri.parse(realPath).getLastPathSegment();
        return Single.<DocumentUploadResponse>create(emitter -> {
            OkHttpClient client = HolaRESTClient.getOkHttpClient();

             FixedMultipartBuilder builder = new FixedMultipartBuilder()
                    .addFormDataPart("file", originalName,
                            RequestBody.create(MEDIA_TYPE_FILE, getFileFromUri(context, uri)))
                    .type(MultipartBody.FORM);

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("X-CSRF-TOKEN", HolaRESTClient.getCsrfToken())
                    .url(HolaRESTClient.getEndpoint() + HolaApi.METHOD_UPLOAD_DOCUMENT)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    if (response.code() == 413) {
                        throw new IOException("Payload too large.");
                    }
                    throw new IOException("Unexpected code " + response);
                }
                String responseString = response.body().string();
                Log.d("FileUploaderService", responseString);

                DocumentUploadResponse res = getGson().fromJson(responseString, DocumentUploadResponse.class);
                if (res.getOriginalName() == null) {
                    res.setOriginalName(originalName);
                }
                emitter.onSuccess(res);
            } catch (Throwable t) {
                Log.e("FileUploaderService", "Error: " + t.getMessage(), t);
                emitter.onError(t);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static <T> Single<T> uploadAvatar(Context context, String endpoint, Uri uri, Class<T> resType) {
        String p = FileUtils.getRealPathFromUri(context, uri);
        if(p == null)  p = FileUtils.getPath(uri, context);
        final String realPath = p;
        final String originalName = Uri.parse(realPath).getLastPathSegment();
        return Single.<T>create(emitter -> {
            OkHttpClient client = HolaRESTClient.getOkHttpClient();

            FixedMultipartBuilder builder = new FixedMultipartBuilder()
                    .addFormDataPart("ava", originalName,
                            RequestBody.create(MEDIA_TYPE_FILE, getFileFromUri(context, uri)))
                    .type(MultipartBody.FORM);

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("X-CSRF-TOKEN", HolaRESTClient.getCsrfToken())
                    .url(endpoint)
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    if (response.code() == 413) {
                        throw new IOException("Payload too large.");
                    }
                    throw new IOException("Unexpected code " + response);
                }
                String responseString = response.body().string();
                Log.d("FileUploaderService", responseString);

                if(resType == String.class){
                    emitter.onSuccess((T)responseString);
                } else {
                    T res = getGson().fromJson(responseString, resType);
                    emitter.onSuccess(res);
                }
            } catch (Throwable t) {
                Log.e("FileUploaderService", "Error: " + t.getMessage(), t);
                emitter.onError(t);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    private static Gson getGson() {
        BooleanDeserializer deserializer = new BooleanDeserializer();

        return new GsonBuilder()
                .registerTypeAdapter(Boolean.class, deserializer)
                .registerTypeAdapter(boolean.class, deserializer)
                .setLenient().create();
    }

    private static File getFileFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            File cache = context.getCacheDir();
            String fname = uri.getLastPathSegment();
            String[] fnameArr = fname.split("\\.");
            File tempFile = File.createTempFile(fnameArr[0], fnameArr[fnameArr.length - 1], cache);
            OutputStream output = new FileOutputStream(tempFile);
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } finally {
                output.close();
            }
            return tempFile;
        } finally {
            if(inputStream != null)
                inputStream.close();
        }
    }

    public interface DocumentUploadListener {
        void onDocumentUploadStarted(String fileName);

        void onSuccess(DocumentUploadResponse response);

        void onFailure(String reason);
    }

    public interface UploadChatPicListener{
        void onSuccess(Chat newChat);
        void onFailure(String reason);
    }
}
