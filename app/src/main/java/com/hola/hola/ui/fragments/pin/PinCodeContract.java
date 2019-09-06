package com.hola.hola.ui.fragments.pin;

public interface PinCodeContract {
    enum State{
        CHECK, // just check pin code
        CHANGE_PIN, CHANGE_NEW, CHANGE_CONFIRM, //  Set new pincode
        NEWPIN_FIRST_TIME, NEWPIN_FIRST_TIME_CONFIRM // Set pincode for the first time
    }
    interface View{
        void displayState(State state);
        void toast(String message);
    }
    interface ResultListener {
        void onPinCodeSet();
        void onPinCodeChecked(boolean success);
        void onPinCancelled();
    }
    interface Presenter{
        void onPinEntered(String pincode);
        void bottomButtonPressed();
        void backPressed();
    }
}
