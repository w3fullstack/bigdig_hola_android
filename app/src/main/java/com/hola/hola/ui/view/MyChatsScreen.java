package com.hola.hola.ui.view;

import com.hola.hola.manager.SecretChatManager;

public interface MyChatsScreen {
    void onItemSelected(int chatId);
    void refresh();
    void onItemSwiped(int chatId);
    SecretChatManager getSecretChatManager();
}
