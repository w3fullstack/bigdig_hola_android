package com.hola.hola.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.manager.SecretChatManager;
import com.hola.hola.model.UserContact;
import com.hola.hola.ui.view.NewChatScreen;
import com.hola.hola.util.DateFormatUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewChatAdapter extends RecyclerView.Adapter<NewChatAdapter.ViewHolder> {
    private NewChatScreen newChatScreen;
    private List<UserContact> userList;

    public NewChatAdapter(NewChatScreen screen) {
        this.newChatScreen = screen;
    }

    public void setUserList(List<UserContact> list) {
        this.userList = list;
    }

    @NonNull
    @Override
    public NewChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_auto_complete_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewChatAdapter.ViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profileImage)
        CircleImageView profileImage;
        @BindView(R.id.profileName)
        TextView profileName;
        @BindView(R.id.lastSeen)
        TextView lastSeen;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(ViewHolder.this, itemView);
        }

        public void bind(UserContact user) {
            itemView.setOnClickListener((view) -> newChatScreen.onItemSelected(user));
            if (user.getName() != null && user.getLastName() != null) {
                String fullName = user.getName() + " " + user.getLastName();
                profileName.setText(fullName);
            } else if (user.getName() != null) {
                profileName.setText(user.getName());
            } else if (user.getLastName() != null) {
                profileName.setText(user.getLastName());
            } else {
                profileName.setText("NULL");
            }
            String lastSeenString = DateFormatUtil.formatLastSeen(user.getUpdatedAt());
            lastSeen.setText(lastSeenString);

            Picasso.get().load(HolaRESTClient.getEndpoint() + user.avatar).placeholder(R.drawable.ic_account_circle_black_48dp).into(profileImage);
        }
    }
}
