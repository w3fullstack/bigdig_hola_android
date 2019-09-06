package com.hola.hola.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PinCodeManager {
    private static final String PREF_NAME = "PinCodeManager";
    private static final String PREF_PIN = "PINCODE";
    private SharedPreferences preferences;
    public PinCodeManager(Context ctx){
        preferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isPinCodeSet(){
        return preferences.contains(PREF_PIN);
    }

    public boolean checkPinCode(String pin){
        return pin.equals(preferences.getString(PREF_PIN, null));
    }

    public void setPinCode(String pin){
        preferences.edit().putString(PREF_PIN, pin).apply();
    }

    public void unsetPinCode(){
        preferences.edit().remove(PREF_PIN).apply();
    }

}
