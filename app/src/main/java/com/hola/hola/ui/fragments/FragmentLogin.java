package com.hola.hola.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import com.hola.hola.R;
import com.hola.hola.manager.LoginManager;
import com.hola.hola.model.body.LoginBody;
import com.hola.hola.ui.activities.SplashScreenActivity;

//todo:logic
public class FragmentLogin extends Fragment {

    private static final String TAG = FragmentLogin.class.getSimpleName();
    Unbinder unbinder;

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_remember_me)
    CheckBox cbRememberMe;

    private LoginManager.LoginResultListener loginResultListener;
    private LoginManager loginManager;
    private boolean firstTimeShown = true; // auto-login when first time shown only

    private ProgressDialog progressDialog;

    public static FragmentLogin newInstance(LoginManager.LoginResultListener loginResultListener) {
        FragmentLogin f = new FragmentLogin();
        f.loginResultListener = loginResultListener;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, v);
        loginManager = new LoginManager(getContext().getApplicationContext(), this::onLoginResult);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                }
                return false;
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in..");

        if(loginManager.isSessionActive()){
            LoginBody body = loginManager.getSessionLoginBody();
            if(body != null){
                etEmail.setText(body.getPhone());
                etPassword.setText(body.getPassword());
                cbRememberMe.setChecked(true);
                if(firstTimeShown) {
                    firstTimeShown = false;
                    requestLogin(body);
                }
            }
        }
        return v;
    }



    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @OnClick(R.id.bt_enter)
    void login() {
        boolean isAllCorrect = true;
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError(getString(R.string.field_required));
            isAllCorrect = false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError(getString(R.string.field_required));
            isAllCorrect = false;
        }
        if (isAllCorrect) {
            LoginBody loginBody =
                    new LoginBody(etEmail.getText().toString(), etPassword.getText().toString());
            requestLogin(loginBody);
        } else {
            showError();
        }
    }

    private void requestLogin(LoginBody login) {
        progressDialog.show();
        loginManager.requestLogin(login, true);
    }

    private void onLoginResult(boolean success, LoginBody loginBody) {
        progressDialog.hide();
        if (success) {
            if(cbRememberMe.isChecked()){
                loginManager.saveSession(loginBody);
            }
            loginResultListener.onLoginResult(true, loginBody);
        } else {
            showError();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: rq=" +requestCode + "; result=" + resultCode );
        if (requestCode == SplashScreenActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                ((SplashScreenActivity) getActivity()).openMainActivity();
            }
        }
    }

    private void showError() {
        if (isAdded()) {
            Toast.makeText(getContext(), getString(R.string.error_wrong_login_data), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @OnClick(R.id.tv_forget_password)
    void forgetPassword() {
        ((SplashScreenActivity) getActivity()).navigate(SplashScreenActivity.FRAGMENT_RESTORE_PASSWORD,
                null, null);
    }

    @OnClick(R.id.tv_register)
    void register() {
        ((SplashScreenActivity) getActivity()).navigate(SplashScreenActivity.FRAGMENT_START_REGISTER, null, null);
    }

    @Override
    public void onDestroyView() {
        progressDialog.dismiss();
        progressDialog = null;
        super.onDestroyView();
    }
}
