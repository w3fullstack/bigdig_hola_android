package com.hola.hola.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hola.hola.R;
import com.hola.hola.manager.SecretChatManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecretChatPasswordDialog extends AlertDialog {
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvMessage) TextView tvMessage;

    SecretChatManager chatManager;
    Callback callback;
    int mode = 0;
    int chatId;
    String message = null;
    String title = "Enter password";

    public static final int MODE_CHECK = 0;
    public static final int MODE_SET = 1;
    public static final int MODE_DELETE = 2;

    public SecretChatPasswordDialog(Context context, int mode, int chatId, Callback callback) {
        super(context);
        chatManager = new SecretChatManager(context);
        this.callback = callback;
        this.mode = mode;
        this.chatId = chatId;
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        setContentView(R.layout.alert_dialog_password_prompt);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if(message == null){
            switch (mode){
                case MODE_CHECK:
                    message = "Chat is secret, you need password to access it";
                    break;
                case MODE_DELETE:
                    message = null;
                    break;
                case MODE_SET:
                    message = "Assign a password to chat";
                    break;
            }
        }
        if (message == null) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(message);
        }
        tvTitle.setText(title);

    }

    public SecretChatPasswordDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public SecretChatPasswordDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    @OnClick(R.id.btn_ok)
    void ok() {
        String p = etPassword.getText().toString();
        switch (mode) {
            case MODE_CHECK:
                if (chatManager.check(chatId, p)) {
                    callback.onSuccess();
                    dismiss();
                } else {
                    String message = "The password you have entered is incorrect." + " \n" + "Please try again";
                    AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                    b.setTitle("Error").setMessage(message)
                            .setPositiveButton("Cancel", null)
                            .setNegativeButton("Retry", (dialog1, which1) -> callback.onRetry());
                    b.create().show();
                    this.dismiss();
                }
                break;
            case MODE_SET:
                chatManager.put(chatId, p);
                callback.onSuccess();
                dismiss();
                break;
            case MODE_DELETE:
                if (chatManager.check(chatId, p)) {
                    chatManager.remove(chatId);
                    callback.onSuccess();
                    dismiss();
                } else {
                    String message = "The password you have entered is incorrect." + " \n" + "Please try again";
                    AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                    b.setTitle("Error").setMessage(message)
                            .setPositiveButton("Cancel", null)
                            .setNegativeButton("Retry", (dialog1, which1) -> callback.onRetry());
                    b.create().show();
                    this.dismiss();
                }
                break;
        }
    }

    @OnClick(R.id.btn_cancel)
    void onCancel() {
        dismiss();
    }

    public interface Callback {
        void onSuccess();

        void onRetry();
    }
}
