package com.hola.hola.ui.fragments.pin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hola.hola.R;
import com.hola.hola.manager.PinCodeManager;
import com.hola.hola.util.ActivityWindowUtils;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.BiConsumer;

public class FragmentPinCode extends Fragment implements PinCodeContract.View {
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.button5)
    Button button5;
    @BindView(R.id.button6)
    Button button6;
    @BindView(R.id.button7)
    Button button7;
    @BindView(R.id.button8)
    Button button8;
    @BindView(R.id.button9)
    Button button9;
    @BindView(R.id.button0)
    Button button0;

    @BindView(R.id.buttonBackspace)
    Button backspace;

    @BindView(R.id.text_input_edit)
    TextInputEditText editText;

    @BindView(R.id.tvTitle)
    TextView title;
    @BindView(R.id.tvBottomText)
    TextView bottomText;
    @BindView(R.id.tvBottomState)
    TextView bottomState;

    private String pincodeString = "";
    private static final int PIN_LENGTH = 4;


    PinCodeContract.Presenter presenter;
    PinCodeContract.State initState;
    PinCodeContract.ResultListener resultListener;
    public static Fragment newInstance(PinCodeContract.State initState, PinCodeContract.ResultListener resultListener) {
        FragmentPinCode f = new FragmentPinCode();
        f.initState = initState;
        f.resultListener = resultListener;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pin_code, container, false);
        ButterKnife.bind(this, v);
        ActivityWindowUtils.setAdjustResizeWindow(getActivity());
        ActivityWindowUtils.hideKeyboard(getActivity());
        applyToAll((button, index) -> {
            button.setOnClickListener(view -> {
                buttonPressed(index);
            });
        });

//        PinCodeContract.State state = manager.isPinCodeSet() ? PinCodeContract.State.NEWPIN_FIRST_TIME : PinCodeContract.State.CHECK;
        presenter = new PinCodePresenter(this, resultListener, new PinCodeManager(getContext()), initState);

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    presenter.backPressed();
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    private void buttonPressed(Integer index) {
        if(pincodeString.length() < PIN_LENGTH) {
            editText.getText().append("*");
            pincodeString += index.toString();
        }
        if(pincodeString.length() == PIN_LENGTH){
            presenter.onPinEntered(pincodeString);
            pincodeString = "";
            editText.setText("");
        }
    }

    private void applyToAll(BiConsumer<Button, Integer> fn){
        try {
            fn.accept(button1, 1);
            fn.accept(button2, 2);
            fn.accept(button3, 3);
            fn.accept(button4, 4);
            fn.accept(button5, 5);
            fn.accept(button6, 6);
            fn.accept(button7, 7);
            fn.accept(button8, 8);
            fn.accept(button9, 9);
            fn.accept(button0, 0);
        } catch (Throwable t){
            t.printStackTrace();
        }
    }

    @OnClick(R.id.buttonBackspace) void backspace(){
        if(pincodeString.length() > 0) {
            pincodeString = pincodeString.substring(0, pincodeString.length() - 1);
            String stars = editText.getText().toString();
            stars = stars.substring(0, stars.length() - 1);
            editText.setText(stars);
        }
    }

    @OnClick(R.id.tvBottomText) void bottomTextClicked(){
        presenter.bottomButtonPressed();
    }


    @Override
    public void displayState(PinCodeContract.State state) {
        if(state == null) return;
        switch (state){
            case CHECK:
                bottomText.setText(R.string.forgot_pincode);
                title.setText(R.string.title_pin_check);
                bottomState.setVisibility(View.INVISIBLE);
                break;
            case NEWPIN_FIRST_TIME:
                bottomText.setText(R.string.pincode_cancel_add);
                bottomState.setVisibility(View.VISIBLE);
                bottomState.setText("1/2");
                title.setText(R.string.pincode_enter_new);
                break;
            case NEWPIN_FIRST_TIME_CONFIRM:
                bottomText.setText(R.string.pincode_cancel_add);
                bottomState.setVisibility(View.VISIBLE);
                bottomState.setText("2/2");
                title.setText(R.string.pincode_enter_repeat);
                break;
            case CHANGE_PIN:
                bottomText.setText(R.string.pincode_cancel_change);
                title.setText(R.string.pincode_enter_current);
                bottomState.setVisibility(View.VISIBLE);
                bottomState.setText("1/3");
                break;
            case CHANGE_NEW:
                bottomText.setText(R.string.pincode_cancel_change);
                title.setText(R.string.pincode_enter_new);
                bottomState.setVisibility(View.VISIBLE);
                bottomState.setText("2/3");
                break;
            case CHANGE_CONFIRM:
                bottomText.setText(R.string.pincode_cancel_change);
                title.setText(R.string.pincode_enter_repeat);
                bottomState.setVisibility(View.VISIBLE);
                bottomState.setText("3/3");
                break;

        }
    }

    @Override
    public void toast(String message) {
        if(isAdded()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
