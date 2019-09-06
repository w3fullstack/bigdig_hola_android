package com.hola.hola.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.model.body.ChangePasswordBody;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordDialog extends AlertDialog {
    @BindView(R.id.constraintLayout) ConstraintLayout constraintLayout;
    @BindView(R.id.til_old_pass) TextInputLayout tilOldPass;
    @BindView(R.id.til_new_pass) TextInputLayout tilNewPass;
    @BindView(R.id.til_confirm_pass) TextInputLayout tilConfirmPass;
    @BindView(R.id.et_old_pass) TextInputEditText etOld;
    @BindView(R.id.et_new_pass) TextInputEditText etNew;
    @BindView(R.id.et_confirm_pass) TextInputEditText etConfirm;
    @BindView(R.id.nestedScrollView)
    NestedScrollView scrollView;
    public ChangePasswordDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        setContentView(R.layout.dialog_change_password);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        scrollView.addOnLayoutChangeListener((v1, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        });
    }

    @OnClick(R.id.btnSubmit) void onSubmit(){
        String oldPass = etOld.getText().toString();
        String newPass = etNew.getText().toString();
        String confirm = etConfirm.getText().toString();

        tilOldPass.setErrorEnabled(false);
        tilNewPass.setErrorEnabled(false);
        tilConfirmPass.setErrorEnabled(false);
        HolaRESTClient.get().changePassword(HolaRESTClient.getCsrfToken(), new ChangePasswordBody(oldPass, newPass, confirm))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            Toast.makeText(getContext(), "Password successfully changed", Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            JsonParser parser = new JsonParser();
                            JsonObject errJson = null;
                            try {
                                String errorString = response.errorBody().string();
                                Log.d("ChangePassword", "ResponseErrorBody: " + errorString);
                                errJson = parser.parse(errorString).getAsJsonObject();

                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            Log.d("ChangePassword", "ResponseCode: " + response.code());
                            Log.d("ChangePassword", "ResponseBody: " + response.body());
                            if(errJson != null) {
                                Toast.makeText(getContext(), "Error: " + errJson.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                JsonObject errors = errJson.getAsJsonObject("errors");
                                Set<Map.Entry<String, JsonElement>> entries = errors.entrySet();
                                for (Map.Entry<String, JsonElement> entry : entries) {
                                    String key = entry.getKey();
                                    String val = entry.getValue().getAsJsonArray().get(0).getAsString();
                                    switch (key) {
                                        case "old_password":
                                            tilOldPass.setErrorEnabled(true);
                                            tilOldPass.setError(val);
                                            break;
                                        case "new_password":
                                            tilNewPass.setErrorEnabled(true);
                                            tilNewPass.setError(val);
                                            break;
                                        case "new_password_confirm":
                                            tilConfirmPass.setErrorEnabled(true);
                                            tilConfirmPass.setError(val);
                                            break;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("ChangePassword", "FAILURE: " + t.getMessage(), t);
                    }
                });
    }
}
