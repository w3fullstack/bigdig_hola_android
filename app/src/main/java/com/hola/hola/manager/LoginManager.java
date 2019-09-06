package com.hola.hola.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.centrifuge.CentrifugeModel;
import com.hola.hola.model.body.LoginBody;
import com.hola.hola.model.LoginResponse;
import com.hola.hola.ui.HolaApp;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginManager {
    private static final String PREFS_NAME = "LoginManager";
    private static final String PREF_USER_PHONE = "phone";
    private static final String PREF_VALID_BEFORE = "timestamp";
    private static final String PREF_PASSWORD = "password";

    private static final long SESSION_LIFETIME = 1000 * 60 * 60 * 2L; // 2 hours
    private static final String TAG = LoginManager.class.getSimpleName();


    private SharedPreferences preferences;
    private LoginResultListener loginResultListener;

    public LoginManager(Context ctx, LoginResultListener loginResultListener){
        preferences = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.loginResultListener = loginResultListener;
        if(preferences.contains(PREF_VALID_BEFORE)){
            long timestamp = preferences.getLong(PREF_VALID_BEFORE, Long.MAX_VALUE);
            if(System.currentTimeMillis() >= timestamp){
                preferences.edit()
//                        .remove(PREF_USER_ID)
                        .remove(PREF_VALID_BEFORE)
                        .remove(PREF_PASSWORD)
                        .remove(PREF_USER_PHONE)
                        .apply();
            }
        }
    }

    public boolean isSessionActive(){
        return preferences.contains(PREF_VALID_BEFORE) && preferences.getLong(PREF_VALID_BEFORE, Long.MAX_VALUE) > System.currentTimeMillis();
    }

    public LoginBody getSessionLoginBody(){
        String phone = preferences.getString(PREF_USER_PHONE, null);
        String pass = preferences.getString(PREF_PASSWORD, null);
        if(phone != null && pass != null){
            return new LoginBody(phone, pass);
        } else {
            return null;
        }
    }

    public void requestLogin(LoginBody login, boolean firstTime){
        Log.d(TAG, "requestLogin: loginBody=" + login.toString());

        HolaRESTClient.get()
                .login(HolaRESTClient.getCsrfToken(), Locale.getDefault().getLanguage(), login)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Log.d(TAG, "requestLogin -> onResponse: code=" + response.code());
                        if (response.code() == 200) {
                            AccountManager.currentAccountId = response.body().getUserId();
                            AccountManager.setCentrifugeModel(new CentrifugeModel(response.body().getUserId(),
                                    response.body().getCentifugeToken(), response.body().getTimestamp()));
                            loginResultListener.onLoginResult(true, login);
                        } else {
                            if(response.code() == 419 && firstTime){
                                HolaApp.requestCsrfUpdate(() -> requestLogin(login, false));
                            } else {
                                loginResultListener.onLoginResult(false, login);
                            }
                        }
                    }

                    @Override public void onFailure(Call<LoginResponse> call, Throwable t) {
//                        showError();
                        Log.d(TAG, "requestLogin -> onFailure");

                        loginResultListener.onLoginResult(false, login);
                    }
                });
    }

    public void saveSession(LoginBody body) {
        preferences.edit()
                .putString(PREF_USER_PHONE, body.getPhone())
                .putString(PREF_PASSWORD, body.getPassword())
                .putLong(PREF_VALID_BEFORE, System.currentTimeMillis() + SESSION_LIFETIME)
                .apply();
    }

    public void clearSession(){
        preferences.edit()
                .remove(PREF_VALID_BEFORE)
                .remove(PREF_PASSWORD)
                .remove(PREF_USER_PHONE)
                .apply();
    }


    public interface LoginResultListener{
        void onLoginResult(boolean success, LoginBody loginBody);
    }
}
