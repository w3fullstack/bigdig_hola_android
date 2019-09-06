package com.hola.hola.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;


import com.hola.hola.R;
import com.hola.hola.manager.LoginManager;
import com.hola.hola.manager.PinCodeManager;
import com.hola.hola.model.body.LoginBody;
import com.hola.hola.ui.HolaApp;
import com.hola.hola.ui.fragments.pin.FragmentPinCode;
import com.hola.hola.ui.fragments.FragmentRegisterConfirmPhone;
import com.hola.hola.ui.fragments.FragmentLogin;
import com.hola.hola.ui.fragments.FragmentNewPassword;
import com.hola.hola.ui.fragments.FragmentRegiserPhone;
import com.hola.hola.ui.fragments.FragmentFinishRegister;
import com.hola.hola.ui.fragments.FragmentRestorePassword;
import com.hola.hola.ui.fragments.FragmentRestorePasswordConfirmation;
import com.hola.hola.ui.fragments.pin.PinCodeContract;

public class SplashScreenActivity extends AppCompatActivity implements PinCodeContract.ResultListener, LoginManager.LoginResultListener {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    public static final int FRAGMENT_LOGIN = 0;
    public static final int FRAGMENT_FINISH_REGISTER = 1;
    public static final int FRAGMENT_START_REGISTER = 10;
    public static final int FRAGMENT_REGISTER_CONFIRM_CODE = 11;
    public static final int FRAGMENT_PIN_CHECK = 2;
    public static final int FRAGMENT_PIN_NEW = 3;
    public static final int REQUEST_CODE = 4;
    public static final int FRAGMENT_CONFIRM_REGISTRATION = 5;
    public static final int FRAGMENT_RESTORE_PASSWORD = 6;
    public static final int FRAGMENT_RESTORE_PASSWORD_CONFIRMATION = 7;
    public static final int FRAGMENT_NEW_PASSWORD = 8;
    public static final String TAG_FINISH_REGISTER = "tag register";
    public static final String TAG_START_REGISTER = "tag start register";
    public static final String TAG_REGISTER_CONFIRM_CODE = "tag confirm code";
    public static final String TAG_RESTORE_PASSWORD = "tag restore password";

    public static final String ARG_PHONE_NUMBER = "PHONE_NUMBER";

    private LoginManager loginManager;
    private PinCodeManager pinCodeManager;

    private boolean fragmentInitFailed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        loginManager = new LoginManager(getApplicationContext(), this);
        pinCodeManager = new PinCodeManager(getApplicationContext());

        scheduleFragmentInit();

        HolaApp.requestCsrfUpdate(null);
    }

    private void scheduleFragmentInit() {
        Handler h = new Handler();
        h.postDelayed(() -> {
            try {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.holder_fragments, FragmentLogin.newInstance(SplashScreenActivity.this))
                        .commit();
                fragmentInitFailed = false;
            } catch(IllegalStateException e){
                fragmentInitFailed = true;
                Log.e(TAG, e.toString());
                e.fillInStackTrace();
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fragmentInitFailed){
            scheduleFragmentInit();
        }
    }

    public void navigate(int fragmentId, Fragment targetFragment, Bundle args) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = null;
        String tag = null;
        switch (fragmentId) {
            case FRAGMENT_LOGIN:
                fragment = new FragmentLogin();
                break;
            case FRAGMENT_START_REGISTER:
                tag = TAG_START_REGISTER;
                fragment = new FragmentRegiserPhone();
                break;
            case FRAGMENT_REGISTER_CONFIRM_CODE:
                tag = TAG_REGISTER_CONFIRM_CODE;
                fragment = new FragmentRegisterConfirmPhone();
                break;
            case FRAGMENT_FINISH_REGISTER:
                tag = TAG_FINISH_REGISTER;
                fragment = new FragmentFinishRegister();
                break;
            case FRAGMENT_PIN_CHECK:
                fragment = FragmentPinCode.newInstance(PinCodeContract.State.CHECK, this);
                break;
            case FRAGMENT_PIN_NEW:
                fragment = FragmentPinCode.newInstance(PinCodeContract.State.NEWPIN_FIRST_TIME, this);
                break;
            case FRAGMENT_CONFIRM_REGISTRATION:
                fragment = new FragmentRegisterConfirmPhone();
                break;
            case FRAGMENT_RESTORE_PASSWORD:
                tag = TAG_RESTORE_PASSWORD;
                fragment = new FragmentRestorePassword();
                break;
            case FRAGMENT_RESTORE_PASSWORD_CONFIRMATION:
                fragment = new FragmentRestorePasswordConfirmation();
                break;
            case FRAGMENT_NEW_PASSWORD:
                fragment = new FragmentNewPassword();
                break;
        }
        if (fragment != null) {
            if (args != null) {
                fragment.setArguments(args);
            }
            if (targetFragment != null) {
                fragment.setTargetFragment(targetFragment, REQUEST_CODE);
            }
            fm.beginTransaction()
                    .replace(R.id.holder_fragments, fragment)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    public void openMainActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginResult(boolean success, LoginBody body) {
        if (success) {
            if(HolaApp.isPremium()) { // If premium, check pin code, or ask to set pin code
                if (pinCodeManager.isPinCodeSet()) {
                    navigate(FRAGMENT_PIN_CHECK, null, null);
                } else {
                    AlertDialog dlg = new AlertDialog.Builder(this)
                            .setTitle("Do you want to set PIN code?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                navigate(FRAGMENT_PIN_NEW, null, null);
                            }).setNegativeButton("No", ((dialog, which) -> {
                                openMainActivity();
                            })).show();
                }
            } else { // If free, just open main activity
                openMainActivity();
            }
        }
    }

    @Override
    public void onPinCodeSet() {
        openMainActivity();
        Log.d(TAG, "onPinCodeSet");
    }

    @Override
    public void onPinCodeChecked(boolean success) {
        Log.d(TAG, "onPinCodeChecked: success=" + success);
        if (success) {
            openMainActivity();
        } else {
            //todo
        }
    }

    @Override
    public void onPinCancelled() {
        Log.d(TAG, "onPinCancelled");
    }
}
