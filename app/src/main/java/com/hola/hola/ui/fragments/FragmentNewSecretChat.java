package com.hola.hola.ui.fragments;

import com.hola.hola.R;
import com.hola.hola.manager.ChatManager;
import com.hola.hola.manager.SecretChatManager;
import com.hola.hola.ui.activities.MainActivity;
import com.hola.hola.ui.dialog.SecretChatPasswordDialog;

public class FragmentNewSecretChat extends FragmentNewChat {
    public FragmentNewSecretChat() {
    }

    @Override
    protected void openChat(int chatId) {
        SecretChatManager manager = new SecretChatManager(getContext());
        if(manager.isChatSecret(chatId)){
            super.openChat(chatId);
        } else {
            new SecretChatPasswordDialog(getContext(), SecretChatPasswordDialog.MODE_SET, chatId, new SecretChatPasswordDialog.Callback() {
                @Override
                public void onSuccess() {
                    ChatManager.currentChatId = chatId;
                    ((MainActivity)getActivity()).navigateSmall(MainActivity.FRAGMENT_TICKET_CHAT);
                }

                @Override
                public void onRetry() {

                }
            }).show();
        }
    }

    @Override
    public int getTitleRes() {
        return R.string.new_secret_chat_title;
    }

    @Override
    public int getBottomButtonTextRes() {
        return R.string.create_secret_chat;
    }
}
