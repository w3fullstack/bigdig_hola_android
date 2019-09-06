package com.hola.hola.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.hola.hola.model.Chat;

public class SecretChatManager {
    private static final String PREF_NAME = "SecretChatManager";
    private static final String PREF_PREFIX = "CHAT_";
    private SharedPreferences preferences;
    public SecretChatManager(Context ctx){
        preferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    public void put(int chatId, String password) {
        preferences.edit().putString(PREF_PREFIX + chatId, password).commit();
    }

    public boolean check(int chatId, String password){
        return preferences.contains(PREF_PREFIX + chatId)
                && preferences.getString(PREF_PREFIX + chatId, "").equals(password);
    }

    public void remove(int chatId){
        preferences.edit().remove(PREF_PREFIX + chatId).commit();
    }

    public boolean isChatSecret(int chatId){
        return preferences.contains(PREF_PREFIX + chatId);
    }
}
