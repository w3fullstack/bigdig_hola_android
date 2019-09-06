package com.hola.hola.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.hola.hola.R;
import com.hola.hola.model.body.RegisterBody;
import com.hola.hola.repository.AuthRepository;
import com.hola.hola.repository.AuthRepositoryImpl;
import com.hola.hola.ui.activities.SplashScreenActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//todo:logic
public class FragmentFinishRegister extends Fragment implements AuthRepositoryImpl.IRegisterResponse {

  Unbinder unbinder;

  @BindView(R.id.nestedScrollView)
  NestedScrollView scrollView;

  @BindView(R.id.til_first_name)
  TextInputLayout tilFirstName;
  @BindView(R.id.et_first_name)
  TextInputEditText etFirstName;

  @BindView(R.id.til_last_name)
  TextInputLayout tilLastName;
  @BindView(R.id.et_last_name)
  TextInputEditText etLastName;

  @BindView(R.id.til_password)
  TextInputLayout tilPassword;
  @BindView(R.id.et_password)
  TextInputEditText etPassword;

  @BindView(R.id.til_password_confirm)
  TextInputLayout tilPasswordConfirm;
  @BindView(R.id.et_password_confirm)
  TextInputEditText etPasswordConfirm;

  @BindView(R.id.til_email)
  TextInputLayout tilEmail;
  @BindView(R.id.et_email)
  TextInputEditText etEmail;

//  @BindView(R.id.et_email)
//  EditText etEmail;
//
//  @BindView(R.id.et_phone)
//  EditText etPhone;
//  @BindView(R.id.et_password)
//  EditText etPassword;
//  @BindView(R.id.et_password_confirm)
//  EditText etPasswordConfirm;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_register_finish, container, false);
    unbinder = ButterKnife.bind(this, v);



    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    scrollView.addOnLayoutChangeListener((v1, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
      getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    });
//    if(getArguments() != null && getArguments().containsKey(SplashScreenActivity.ARG_PHONE_NUMBER)){
//      etPhone.setText(getArguments().getString(SplashScreenActivity.ARG_PHONE_NUMBER));
//      etPhone.setEnabled(false);
//    }

      etPassword.setTransformationMethod(new PasswordTransformationMethod());
      etPasswordConfirm.setTransformationMethod(new PasswordTransformationMethod());

      //todo:add register button visible when keyboard appears
    etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          register();
        }
        return false;
      }
    });
    return v;
  }

  @OnClick(R.id.bt_register)
  void register() {

    boolean correct = verifyNotEmpty(tilFirstName, etFirstName);
    correct &= verifyNotEmpty(tilLastName, etLastName);
    correct &= verifyNotEmpty(tilPassword, etPassword);
    correct &= verifyNotEmpty(tilEmail, etEmail);
    if(!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())){
      tilPasswordConfirm.setErrorEnabled(true);
      tilPasswordConfirm.setError(getString(R.string.passwords_do_not_match));
      correct = false;
    } else {
      tilPasswordConfirm.setErrorEnabled(false);
      correct &= verifyNotEmpty(tilPasswordConfirm, etPasswordConfirm);
    }


//    if (!TextUtils.isEmpty(etEmail.getText()) &&
//        !TextUtils.isEmpty(etNickname.getText()) &&
//        !TextUtils.isEmpty(etPhone.getText()) &&
//        !TextUtils.isEmpty(etPassword.getText()) &&
//        !TextUtils.isEmpty(etPasswordConfirm.getText())) {
    if(correct){
      AuthRepository authRepository = new AuthRepositoryImpl(this);
      RegisterBody
          register =
          new RegisterBody(getArguments().getString(SplashScreenActivity.ARG_PHONE_NUMBER), 555, etFirstName.getText().toString(),
              etLastName.getText().toString(), etPassword.getText().toString(),
              etPasswordConfirm.getText().toString(), etEmail.getText().toString());
      authRepository.registerUser(register);
      //((SplashScreenActivity) getActivity()).navigate(SplashScreenActivity.FRAGMENT_CONFIRM_REGISTRATION, null);
    } else {
//      showError();
    }
  }

  private boolean verifyNotEmpty(TextInputLayout inputLayout, TextInputEditText editText) {
    if(TextUtils.isEmpty(editText.getText())){
      inputLayout.setErrorEnabled(true);
      inputLayout.setError(getString(R.string.field_required));
      return false;
    } else {
      inputLayout.setErrorEnabled(false);
      return true;
    }
  }

  private void showError(String message) {
    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT)
        .show();
  }

  @Override
  public void onDestroy() {
    unbinder.unbind();
    super.onDestroy();
  }

  @Override public void finishRegister(boolean status) {
    if(status && getFragmentManager() != null){
      if(getActivity() != null)
      {
        Toast.makeText(getActivity(), "Registration successful.", Toast.LENGTH_SHORT).show();
      }
      int rootFragmentId = getFragmentManager().getBackStackEntryAt(0).getId();
      getFragmentManager().popBackStackImmediate(rootFragmentId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    } else {
//      showError();
    }
  }

  @Override
  public void serverError(String message) {
    showError(message);
  }
}
