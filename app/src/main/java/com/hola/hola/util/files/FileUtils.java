package com.hola.hola.util.files;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;

import java.io.File;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();


    public static String getRealPathFromUri(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
//                return cursor.getString(column_index);
            }
        } catch (Throwable t) {
            Log.e(TAG, "Could not resolve real path from uri: " + t.getMessage(), t);
            return contentUri.getPath();
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public static boolean isViewableImage(String name) {
        String suffix = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
        if (suffix.length() == 0)
            return false;
        if (suffix.equals("svg"))
            // don't support svg preview
            return false;

        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (mime == null)
            return false;
        return mime.contains("image");
    }

    public static long downloadFile(Context context, String url, String fileName, String directory) {
        final DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (mgr == null) return -1;

        final Uri downloadUri = Uri.parse(url);
        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        Log.d(TAG, "Extension: " + extension);
        Log.d(TAG, "MimeType: " + MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));

        final DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setTitle(context.getString(R.string.download_manager_title_format, fileName))
//                .setDescription(url)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setMimeType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));
        List<Cookie> cookies = HolaRESTClient.getOkHttpClient().cookieJar().loadForRequest(HttpUrl.parse(url));
        for (Cookie cookie : cookies) {
            request.addRequestHeader("Cookie", cookie.toString());
        }
        File dir;
        if (isExternalStorageWritable()) {
            dir = new File(Environment.getExternalStorageDirectory() + directory);
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e(TAG, "Error: Could not make dir");
                throw new IllegalStateException("Could not make dir: " + dir.getAbsolutePath());
            }
            request.setDestinationInExternalPublicDir(directory, fileName);

        } else {
            dir = new File(Environment.getRootDirectory() + directory);
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e(TAG, "Error: Could not make dir");
                throw new IllegalStateException("Could not make dir: " + dir.getAbsolutePath());
            }
            request.setDestinationUri(Uri.parse(dir.getAbsolutePath() + fileName));
        }

        return mgr.enqueue(request);
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Uri uri, Context context) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            //Whether the Uri authority is ExternalStorageProvider.
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //Goole photos uri
                if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
                    return DocumentFile.fromSingleUri(context, uri).getName();

//                    return uri.getLastPathSegment();
                }

                    //Goole doc uri
                else if ("com.google.android.apps.docs.storage".equals(uri.getAuthority())) {
                    return DocumentFile.fromSingleUri(context, uri).getName();
//                    return uri.getLastPathSegment();
                }

                return getRealPathFromUri(context, uri);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            } else {
                return uri.getPath();
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

}
