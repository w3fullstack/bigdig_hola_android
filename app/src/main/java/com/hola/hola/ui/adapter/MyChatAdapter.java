package com.hola.hola.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.manager.SecretChatManager;
import com.hola.hola.model.Chat;
import com.hola.hola.model.Other;
import com.hola.hola.ui.view.MyChatsScreen;
import com.hola.hola.util.DateFormatUtil;
import com.hola.hola.util.MySwipeRevealLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyChatAdapter extends RecyclerView.Adapter<MyChatAdapter.ViewHolder> {
    private List<Chat> chatModelList;
    private MyChatsScreen myChatsScreen;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public MyChatAdapter(final MyChatsScreen myChatsScreen) {
        chatModelList = new ArrayList<>();
        this.myChatsScreen = myChatsScreen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyChatAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Chat chat = chatModelList.get(position);
        viewBinderHelper.bind(holder.swipeRevealLayout, chat.getId().toString());
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public void setList(List<Chat> list) {
        this.chatModelList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView profileName;
        TextView message;
        TextView profileTimeLastMessage;
        ImageView imgChatStatus;

        FrameLayout root;
        FrameLayout swipeRoot;
        RelativeLayout dialogContainer;
        MySwipeRevealLayout swipeRevealLayout;
        TextView swipeButtonText;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_reveal_layout);
            profileImage = itemView.findViewById(R.id.profileImage);
            profileName = itemView.findViewById(R.id.profileName);
            message = itemView.findViewById(R.id.message);
            profileTimeLastMessage = itemView.findViewById(R.id.profileTimeLastMessage);
            imgChatStatus = itemView.findViewById(R.id.iv_chat_status);
            swipeButtonText = itemView.findViewById(R.id.tv_swipe_button_text);
            root = itemView.findViewById(R.id.rootLayout);
            swipeRoot = itemView.findViewById(R.id.swipe_root_layout);
            dialogContainer = itemView.findViewById(R.id.dialogContainer);
        }

        void bind(Chat chat) {
//            int recipientId = chat.getOthers().get(0).id;//chat.getUsers().get(1);
//            User recipient = null;
//            for(User u : chat.getUsers()){
//                if(u.id == recipientId) {
//                    recipient = u;
//                    break;
//                }
//            }
//            if(chat.getOthers().size() == 1) {
//                profileName.setText(recipient.getFullName());
//            } else {
//                StringBuilder sb = new StringBuilder();
//                for(int i = 0; i < chat.getOthers().size() -1; ++i){
//                    String name = chat.getOthers().get(i).getName();
//                    sb.append(name).append(", ");
//                }
//                sb.append(chat.getOthers().get(chat.getOthers().size() -1).getFullName());
//                profileName.setText(sb.toString());
//            }
            Other recipient = chat.getOthers().get(0);
            profileName.setText(chat.getTitle());
            String lastMessage = "";
            if(chat.getLastMessage() != null) {
                lastMessage = chat.getLastMessage().getText();
            }
            if (TextUtils.isEmpty(lastMessage))
                message.setVisibility(View.GONE);
            else {
                message.setVisibility(View.VISIBLE);
                message.setText(lastMessage);
            }
            String timeString = DateFormatUtil.formatLastMessageDate(chat.getLastMessageDate());
            profileTimeLastMessage.setText(timeString);


            String pictureUrl = chat.getPicture();
            if(TextUtils.isEmpty(pictureUrl))
                pictureUrl = recipient.getAvatar();
            Picasso.get().load(HolaRESTClient.getEndpoint() + pictureUrl)
                    .placeholder(R.drawable.ic_account_circle_black_48dp)
                    .into(profileImage);

            Log.d("IMAGES", "Loading avatar: " + recipient.avatar);


            SecretChatManager manager = myChatsScreen.getSecretChatManager();
            imgChatStatus.setVisibility(manager.isChatSecret(chat.getDialogId()) ? View.VISIBLE : View.INVISIBLE);
            swipeButtonText.setText(manager.isChatSecret(chat.getDialogId()) ? "Delete PIN" : "Set PIN");
            swipeRevealLayout.setSwipeListener(new SwipeRevealLayout.SimpleSwipeListener(){
                @Override
                public void onOpened(SwipeRevealLayout view) {
                    swipeRevealLayout.postDelayed(() -> swipeRevealLayout.close(true), 1000);
                    myChatsScreen.onItemSwiped(chat.getDialogId());
//                    view.close(true);
                    swipeButtonText.setText(myChatsScreen.getSecretChatManager().isChatSecret(chat.getDialogId())
                            ? "Delete PIN" : "Set PIN");
                }

            });

            root.setOnClickListener(v -> {
                myChatsScreen.onItemSelected(chat.getDialogId());
            });

//            ViewGroup.LayoutParams params = swipeRoot.getLayoutParams();
//            params.height = dialogContainer.getMeasuredHeight();//.getLayoutParams().height;
//            swipeRoot.setLayoutParams(params);
//            swipeRoot.invalidate();

        }
    }


}
