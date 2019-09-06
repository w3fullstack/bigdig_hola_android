package com.hola.hola.ui.fragments.pin;

import com.hola.hola.manager.PinCodeManager;

public class PinCodePresenter implements PinCodeContract.Presenter {
    private PinCodeContract.View view;
    private PinCodeContract.ResultListener resultListener;
    private PinCodeManager manager;

    private PinCodeContract.State currentState;
    private String lastPin = "";

    public PinCodePresenter(PinCodeContract.View view, PinCodeContract.ResultListener resultListener, PinCodeManager pinManager, PinCodeContract.State initState) {
        this.view = view;
        this.manager = pinManager;
        this.currentState = initState;
        this.resultListener = resultListener;
        view.displayState(currentState);
    }

    @Override
    public void onPinEntered(String pincode) {
        switch (currentState) {
            case CHECK:
                if (manager.checkPinCode(pincode)) {
                    resultListener.onPinCodeChecked(true);
                } else {
                    view.toast("Wrong pin code. Try again");
                }
                break;
            case CHANGE_PIN:
                if (manager.checkPinCode(pincode)) {
                    currentState = PinCodeContract.State.CHANGE_NEW;
                } else {
                    view.toast("Wrong pin code. Try again");
                }
                break;
            case CHANGE_NEW:
                lastPin = pincode;
                currentState = PinCodeContract.State.CHANGE_CONFIRM;
                break;
            case CHANGE_CONFIRM:
                if (pincode.equals(lastPin)) {
                    manager.setPinCode(pincode);
                    resultListener.onPinCodeSet();
                } else {
                    view.toast("Pins do not match");
                    lastPin = "";
                    currentState = PinCodeContract.State.CHANGE_NEW;
                }
                break;
            case NEWPIN_FIRST_TIME:
                lastPin = pincode;
                currentState = PinCodeContract.State.NEWPIN_FIRST_TIME_CONFIRM;
                break;
            case NEWPIN_FIRST_TIME_CONFIRM:
                if (pincode.equals(lastPin)) {
                    manager.setPinCode(pincode);
                    resultListener.onPinCodeSet();
                } else {
                    view.toast("Pins do not match");
                    lastPin = "";
                    currentState = PinCodeContract.State.NEWPIN_FIRST_TIME;
                }
                break;

        }
        view.displayState(currentState);
    }

    @Override
    public void bottomButtonPressed() {
        resultListener.onPinCancelled();
    }

    @Override
    public void backPressed() {
        resultListener.onPinCancelled();
    }
}
