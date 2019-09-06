package com.hola.hola.util;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.hola.hola.model.Chat;

import java.util.List;

public class MyDiffCallback extends DiffUtil.Callback{

    List<Chat> oldChats;
    List<Chat> newChats;

    public MyDiffCallback(List<Chat> newChats, List<Chat> oldChats) {
        this.newChats = newChats;
        this.oldChats = oldChats;
    }

    @Override
    public int getOldListSize() {
        return oldChats.size();
    }

    @Override
    public int getNewListSize() {
        return newChats.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldChats.get(oldItemPosition).getDialogId().intValue() == newChats.get(newItemPosition).getDialogId().intValue();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldChats.get(oldItemPosition).equals(newChats.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}