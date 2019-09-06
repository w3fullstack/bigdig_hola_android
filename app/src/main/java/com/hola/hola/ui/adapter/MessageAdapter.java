package com.hola.hola.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.manager.AccountManager;
import com.hola.hola.model.Document;
import com.hola.hola.model.Message;
import com.hola.hola.ui.dialog.AttachmentViewDialog;
import com.hola.hola.util.files.FileUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final int TYPE_SERVER = 0;
    private final int TYPE_CLIENT = 1;

    List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutFile;
        if (viewType == TYPE_SERVER)
            layoutFile = R.layout.listitem_message_server;
        else
            layoutFile = R.layout.listitem_message_client;
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutFile, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messages.get(position), getItemViewType(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getFromId() == AccountManager.currentAccountId ? TYPE_CLIENT : TYPE_SERVER;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.messageText)
        TextView messageText;
        @BindView(R.id.messageTime)
        TextView messageTime;
        @BindView(R.id.messageUserAvatar)
        CircleImageView messageAvatar;
        @BindView(R.id.documents_holder)
        LinearLayout documentsHolder;
//        @BindView(R.id.iv_document_image)
//        ImageView imageView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            imageView.setImageBitmap(null);
//            imageView.setVisibility(View.GONE);
        }

        void bind(Message message, int viewType) {
            if(TextUtils.isEmpty(message.getText())){
                messageText.setVisibility(View.GONE);
            } else {
                messageText.setVisibility(View.VISIBLE);
                messageText.setText(message.getText());
            }
            messageTime.setText(message.getCreatedAt());
            documentsHolder.removeAllViews();

            if (message.getDocuments() != null) {
                for (Document d : message.getDocuments()) {
                    View attachmentView = LayoutInflater.from(itemView.getContext()).inflate(getItemAttachmentLayout(viewType), (ViewGroup)itemView, false);
                    TextView tvName = attachmentView.findViewById(R.id.tv_attachment_name);
                    ImageView image = attachmentView.findViewById(R.id.iv_attachment_icon);
                    tvName.setText(d.getOriginalName());

                    image.setImageDrawable(itemView.getContext().getResources().getDrawable(
                            getAttachmentIconDrawable(d.getOriginalName(), viewType))
                    );
                    attachmentView.setOnClickListener((v) -> {
                        new AttachmentViewDialog(d, itemView.getContext()).show();
                    });
                    documentsHolder.addView(attachmentView);
                }
            }

            Picasso.get().load(HolaRESTClient.getEndpoint() + message.getFrom().avatar).placeholder(R.drawable.ic_account_circle_black_48dp).into(messageAvatar);
        }

        private int getItemAttachmentLayout(int viewType){
            if(viewType == TYPE_SERVER){
                return R.layout.item_attachment_server;
            } else {
                return R.layout.item_attachment;
            }
        }

        private int getAttachmentIconDrawable(String fileName, int viewType){
            if(FileUtils.isViewableImage(fileName)){
                return (viewType == TYPE_SERVER) ? R.drawable.ic_attach_image : R.drawable.ic_attach_image_white;
            } else {
                return (viewType == TYPE_SERVER) ? R.drawable.ic_attach_file : R.drawable.ic_attach_file_white;
            }
        }
    }
}
