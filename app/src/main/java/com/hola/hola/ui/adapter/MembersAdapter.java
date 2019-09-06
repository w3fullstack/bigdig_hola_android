package com.hola.hola.ui.adapter;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.model.UserContact;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {
    public interface OnItemSelectedListener {
        void onItemSelected(UserContact userContact);
    }

    List<UserContact> list;
    OnItemSelectedListener listener;

    public MembersAdapter(List<UserContact> users, OnItemSelectedListener listener) {
        this.list = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profileName) TextView profileName;
        @BindView(R.id.profileImage) CircleImageView profileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(UserContact userContact) {
            if (!TextUtils.isEmpty(userContact.getName())) {
                profileName.setText(userContact.getName());
            } else if (!TextUtils.isEmpty(userContact.getLastName())) {
                profileName.setText(userContact.getLastName());
            } else {
                profileName.setText("NULL");
            }
//            profileImage.getLayoutParams().width = itemView.getMeasuredHeight();
            profileImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
            if (!TextUtils.isEmpty(userContact.getAvatar())) {
                Picasso.get()
                        .load(HolaRESTClient.getEndpoint() + userContact.getAvatar())
                        .error(R.drawable.ic_account_circle_black_24dp)
                        .placeholder(R.drawable.ic_account_circle_black_24dp)
                        .into(profileImage);
            }

//            profileImage.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//                @Override
//                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                    profileImage.getLayoutParams().width = itemView.getMeasuredHeight();
//                }
//            });
        }
    }
}
