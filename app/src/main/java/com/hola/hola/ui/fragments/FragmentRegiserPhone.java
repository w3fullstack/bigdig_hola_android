package com.hola.hola.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.model.body.PostPhoneBody;
import com.hola.hola.model.Success;
import com.hola.hola.ui.activities.SplashScreenActivity;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRegiserPhone extends Fragment {
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.bt_send_code) Button btSendCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_send_code, container, false);
        ButterKnife.bind(this, v);
        etPhone.requestFocus();
        return v;
    }

    @OnClick(R.id.bt_send_code) void sendCode(){
        final String phoneNumber = etPhone.getText().toString();
        if(TextUtils.isEmpty(phoneNumber)){
            etPhone.setError(getString(R.string.field_required));
            return;
        }
        HolaRESTClient.get()
                .sendConfirmationCode(HolaRESTClient.getCsrfToken(), new PostPhoneBody(phoneNumber))
                .enqueue(new Callback<Success>() {
                    @Override
                    public void onResponse(Call<Success> call, Response<Success> response) {
                        if(response.isSuccessful()){
                            if(isAdded()) {
                                showToast(getString(R.string.register_code_sent_to_phone));
                                ((SplashScreenActivity) getActivity()).navigate(SplashScreenActivity.FRAGMENT_REGISTER_CONFIRM_CODE,
                                        null, makeArgsBundle(phoneNumber));
                            }
                        } else {
                            try {
                                JSONObject errJson = new JSONObject(response.errorBody().string());
                                String errMessage = errJson.getString("error");
                                showToast(errMessage);
                                Log.d("REGISTER_PHONE", response.errorBody().string());
                            } catch (Throwable e) {
                                Log.d("REGISTER_PHONE", e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Success> call, Throwable t) {
                        if(isAdded()){

                        }
                    }
                });
    }

    private void showToast(String message) {
        if(isAdded()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private Bundle makeArgsBundle(String phoneNumber){
        Bundle bundle = new Bundle();
        bundle.putString(SplashScreenActivity.ARG_PHONE_NUMBER, phoneNumber);
        return bundle;
    }
}
