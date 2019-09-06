package com.hola.hola.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.model.PostSendCode;
import com.hola.hola.model.Success;
import com.hola.hola.ui.activities.SplashScreenActivity;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentRegisterConfirmPhone extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.et_code)
    EditText etCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_confirm_code, container, false);
        unbinder = ButterKnife.bind(this, v);
        etCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    confirmRegistration();
                return false;
            }
        });
        return v;
    }

    @OnClick(R.id.bt_confirm_registration)
    void confirmRegistration() {
        if (TextUtils.isEmpty(etCode.getText())) {
            etCode.setError(getString(R.string.field_required));
        } else {
//            ((SplashScreenActivity) getActivity()).navigate(SplashScreenActivity.MODE_NEW_PIN, null);
            String phoneNumber = getArguments().getString(SplashScreenActivity.ARG_PHONE_NUMBER);
            int code = Integer.valueOf(etCode.getText().toString());
            HolaRESTClient.get()
                    .confirmPhone(HolaRESTClient.getCsrfToken(), new PostSendCode(phoneNumber, code))
                    .enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            if(response.isSuccessful()) {
                                toast(getString(R.string.phone_number_confirmed));
                                ((SplashScreenActivity)getActivity()).navigate(SplashScreenActivity.FRAGMENT_FINISH_REGISTER, null, getArguments());
                            } else {
                                try {
                                    JSONObject errJson = new JSONObject(response.errorBody().string());
                                    String err = errJson.getString("error");
                                    toast(err);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                    toast(getString(R.string.could_not_verify_phone));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            toast(getString(R.string.could_not_verify_phone));
                        }
                    });
        }
    }

    private void toast(String message) {
        if(isAdded()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
