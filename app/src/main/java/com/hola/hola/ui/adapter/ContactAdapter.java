package com.hola.hola.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hola.hola.R;
import com.hola.hola.model.Contact;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    List<Contact> contactsList;
    Context context;

    public ContactAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactsList.get(position);

        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhoneNumber());
        if(contact.getImageUri() != null)
            Picasso.get().load(contact.getImageUri()).into(holder.avatar);
        else
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        if (contactsList == null)
            return 0;
        else
            return contactsList.size();
    }

    public void setContactList(List<Contact> contactList){
        this.contactsList = contactList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.circleImageViewAvatar)
        CircleImageView avatar;

        @BindView(R.id.textViewName)
        TextView name;

        @BindView(R.id.textViewPhone)
        TextView phone;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
