package com.hola.hola.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.model.BasicUser;
import com.hola.hola.model.User;
import com.hola.hola.model.UserContact;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter<U extends BasicUser> extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    public interface OnItemSelectedListener{
        void onItemSelected(BasicUser userContact);
    }

    List<U> list;
    OnItemSelectedListener listener;

    public UserListAdapter(List<U> list, OnItemSelectedListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        return new ViewHolder<U>(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        U model = list.get(position);
        holder.bind(model);
        holder.itemView.setOnClickListener(v -> {
            listener.onItemSelected(model);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder<T extends BasicUser> extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_user_name) TextView userName;
        @BindView(R.id.iv_user_avatar) CircleImageView userAvatar;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(T userContact) {
            userName.setText(userContact.getFullName());
            Picasso.get().load(HolaRESTClient.getEndpoint() + userContact.getAvatar())
                    .placeholder(R.drawable.ic_account_circle_black_48dp)
                    .into(userAvatar);
        }
    }
}
