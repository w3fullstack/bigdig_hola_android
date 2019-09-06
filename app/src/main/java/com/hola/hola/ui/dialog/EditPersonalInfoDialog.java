package com.hola.hola.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.manager.AccountManager;
import com.hola.hola.model.body.EditPersonalInfoBody;
import com.hola.hola.model.UserContact;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class EditPersonalInfoDialog extends AlertDialog {

    public interface Callback { }

    @BindView(R.id.tilEmail) TextInputLayout tilEmail;
    @BindView(R.id.tilEtEmail) TextInputEditText tilEtEmail;
    @BindView(R.id.tilFirstName) TextInputLayout tilFirstName;
    @BindView(R.id.tilEtFirstName) TextInputEditText tilEtFirstName;
    @BindView(R.id.tilLastName) TextInputLayout tilLastName;
    @BindView(R.id.tilEtLastName) TextInputEditText tilEtLastName;

    public EditPersonalInfoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        setContentView(R.layout.dialog_edit_personal_info);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        UserContact user = AccountManager.currentUser;
        tilEtFirstName.setText(user.getName());
        tilEtLastName.setText(user.getLastName());
        tilEtEmail.setText(user.getEmail().toString());
    }


    @OnClick(R.id.btnSubmit) void submit(){
        String firstName = tilEtFirstName.getText().toString();
        String lastName = tilEtLastName.getText().toString();
        String email = tilEtEmail.getText().toString();
        HolaRESTClient.get().updateUserData(HolaRESTClient.getCsrfToken(), new EditPersonalInfoBody(firstName, lastName, email))
                .enqueue(new retrofit2.Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.code() == 200){
                            Toast.makeText(getContext(), "Personal info successfuly updated", Toast.LENGTH_SHORT).show();
                            AccountManager.setCurrentUserFromJson(response.body());
                            dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }
}
