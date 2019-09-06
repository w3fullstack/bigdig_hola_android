package com.hola.hola.util.files;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import static android.app.Activity.RESULT_OK;

public class FileChooser {
    private final static int REQUEST_CODE_CHOOSE_FILE = 123;
    private final static int REQUEST_CODE_CHOOSE_IMAGE = 124;
    private final static String TAG = FileChooser.class.getSimpleName();


    private OnFileChosenListener listener;
    private Activity activity;

    public FileChooser(Activity activity){
        this.activity = activity;
    }

    public void startChooseFile(OnFileChosenListener listener){
        this.listener = listener;
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        chooseFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent = Intent.createChooser(chooseFile, "Choose a file");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivityForResult(intent, REQUEST_CODE_CHOOSE_FILE);
    }

    public void startChooseImage(OnFileChosenListener listener){
        this.listener = listener;
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        pickIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivityForResult(chooserIntent, REQUEST_CODE_CHOOSE_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != RESULT_OK) return;
        if(requestCode == REQUEST_CODE_CHOOSE_FILE ||
                requestCode == REQUEST_CODE_CHOOSE_IMAGE){
            Uri fileUri = data.getData();
            if(listener != null){
                listener.onFileChosen(fileUri);
            }
            Log.d(TAG, "onFileChosen: " +  fileUri.getPath());
        }
    }

    public interface OnFileChosenListener{
        void onFileChosen(Uri fileUri);
    }
}
