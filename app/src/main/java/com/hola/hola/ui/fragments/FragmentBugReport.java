package com.hola.hola.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.model.body.BugReportBody;
import com.hola.hola.ui.activities.MainActivity;
import com.hola.hola.util.ActivityWindowUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBugReport extends Fragment {
    @BindView(R.id.til_email) TextInputLayout tilEmail;
    @BindView(R.id.til_et_email) TextInputEditText tilEtEmail;
    @BindView(R.id.et_bug_text) EditText etBugText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bugreport, container, false);
        ButterKnife.bind(this, v);
        ActivityWindowUtils.setAdjustResizeWindow(getActivity());
        return v;
    }

    @OnClick(R.id.buttonSend) void send(){
        String email = tilEtEmail.getText().toString();
        String text = etBugText.getText().toString();
        if(!isEmailValid(email)){
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Email is not valid");
            return;
        } else {
            tilEmail.setErrorEnabled(false);
        }

        HolaRESTClient.get().sendBugReport(HolaRESTClient.getCsrfToken(), new BugReportBody(email, text))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            Toast.makeText(getContext(), "Report successfuly sent", Toast.LENGTH_SHORT).show();
                            ((MainActivity)getActivity()).onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("FragmentBugReport", "Failure: " + t.getMessage(), t);
                    }
                });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
