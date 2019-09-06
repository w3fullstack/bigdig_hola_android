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


import com.hola.hola.R;
import com.hola.hola.ui.activities.SplashScreenActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class FragmentRestorePasswordConfirmation extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.et_data)
    EditText etData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restore_password_confirmation, container, false);
        unbinder = ButterKnife.bind(this, v);
        etData.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    restorePassword();
                return false;
            }
        });
        return v;
    }

    @OnClick(R.id.bt_restore_password)
    void restorePassword() {
        if (TextUtils.isEmpty(etData.getText())) {
            etData.setError(getString(R.string.field_required));
        } else {
            ((SplashScreenActivity) getActivity()).navigate(SplashScreenActivity.FRAGMENT_NEW_PASSWORD,
                    null, null);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
