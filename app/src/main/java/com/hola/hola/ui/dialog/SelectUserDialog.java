package com.hola.hola.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hola.hola.R;
import com.hola.hola.model.BasicUser;
import com.hola.hola.model.User;
import com.hola.hola.model.UserContact;
import com.hola.hola.ui.adapter.UserListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectUserDialog<U extends BasicUser> extends AlertDialog {
    public interface OnUserSelectedListener<T extends BasicUser>{
        void onUserSelected(T user);
    }

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.tvTitle) TextView tvTitle;

    UserListAdapter adapter;
    List<U> users;
    OnUserSelectedListener listener;
    String title = "Select user";
    public SelectUserDialog(Context context, List<U> users, OnUserSelectedListener<U> listener) {
        super(context);
        this.users = users;
        this.listener = listener;
    }

    public SelectUserDialog<U> setTitle(String title){
        this.title = title;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog_select_user);
        ButterKnife.bind(this);
        tvTitle.setText(title);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new UserListAdapter<U>(users, userContact -> {
            if(listener != null){
                listener.onUserSelected(userContact);
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
