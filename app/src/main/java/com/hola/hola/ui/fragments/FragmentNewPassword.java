
package com.hola.hola.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


import com.hola.hola.R;
import com.hola.hola.ui.activities.SplashScreenActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentNewPassword extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_repeat_password)
    EditText etRepeatPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_password, container, false);
        unbinder = ButterKnife.bind(this, v);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    savePassword();
                return false;
            }
        });
        return v;
    }

    @OnClick(R.id.bt_save_password)
    void savePassword() {
        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError(getString(R.string.field_required));
        } else if (TextUtils.isEmpty(etRepeatPassword.getText())) {
            etRepeatPassword.setError(getString(R.string.field_required));
        } else {
            getFragmentManager().popBackStackImmediate(SplashScreenActivity.TAG_RESTORE_PASSWORD,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
