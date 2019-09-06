package com.hola.hola.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import com.hola.hola.R;
import com.hola.hola.REST.HolaApi;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.manager.AccountManager;
import com.hola.hola.manager.ChatManager;
import com.hola.hola.model.body.ChangeAdminBody;
import com.hola.hola.model.Chat;
import com.hola.hola.model.body.DeleteUserBody;
import com.hola.hola.model.DocumentUploadResponse;
import com.hola.hola.model.Message;
import com.hola.hola.model.body.MessageBody;
import com.hola.hola.model.MessagesResponse;
import com.hola.hola.model.Other;
import com.hola.hola.ui.HolaApp;
import com.hola.hola.ui.activities.MainActivity;
import com.hola.hola.ui.adapter.MessageAdapter;
import com.hola.hola.ui.dialog.SelectUserDialog;
import com.hola.hola.util.ActivityWindowUtils;
import com.hola.hola.util.files.FileUploader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTicketChat extends Fragment {

    private static final long TIMER_DELAY_MS = 2000;
    private static final String TAG = FragmentTicketChat.class.getSimpleName();


    Unbinder unbinder;
    CompositeDisposable disposableBag = new CompositeDisposable();

    List<Message> messages;

    @BindView(R.id.rv_messages)
    RecyclerView rvMessages;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.empty_holder)
    FrameLayout emptyHolder;

    @BindView(R.id.iv_send)
    ImageView btnSend;
    @BindView(R.id.iv_attach_image)
    ImageView btnAttachImage;
    @BindView(R.id.holder_keyboard)
    LinearLayout holderKeyboard;

    @BindView(R.id.tv_count_uploading_documents)
    TextView tvCountUploading;

    @BindView(R.id.holder_currently_uploading)
    LinearLayout holderCurrentlyUploading;

    @BindView(R.id.tv_count_uploaded_documents)
    TextView tvCountUploaded;

    private MenuItem menuTranslateEnabled;
    private MenuItem menuChangeAdmin;
    private MenuItem menuDeleteUser;
    private MenuItem menuChangePicture;

    private String systemLanguage = Locale.getDefault().getLanguage();
    private Integer translateEnabled = HolaApi.TRANSLATE_ENABLED;

    private List<DocumentUploadResponse> documentsToSend = new ArrayList<>();
    private int uploadingDocuments = 0;

    private boolean btnSendStateVisible = true;

    private Chat currentChat;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ticket_chat, container, false);
        unbinder = ButterKnife.bind(this, v);
        rvMessages.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        messages = new ArrayList<>();
        rvMessages.setAdapter(new MessageAdapter(messages));
        getChatMessages();

        disposableBag.add(
                Observable
                        .interval(TIMER_DELAY_MS, TIMER_DELAY_MS, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(next -> getChatMessages(), err -> {
                        }, () -> {
                        })
        );

        ActivityWindowUtils.setAdjustResizeWindow(getActivity());
        rvMessages.addOnLayoutChangeListener((v1, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) ->
                        ActivityWindowUtils.setAdjustResizeWindow(getActivity())
        );
        emptyHolder.addOnLayoutChangeListener((v12, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) ->
                ActivityWindowUtils.setAdjustResizeWindow(getActivity())
        );

        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                updateView();
            }
        });

        Handler h = new Handler();
        h.post(this::updateView);
        return v;
    }

    private void updateView(){
        if(!isAdded() || etText == null) return;
        updateButtonSendVisibility();
        updateCurrentlyUploadingView();
        updateMenu();
    }

    private void updateButtonSendVisibility() {
        boolean visible = etText.getText().length() > 0 || documentsToSend.size() > 0;
        if(!visible && btnSendStateVisible){
            float w = btnSend.getWidth();
            btnSend.animate().translationX(w*1.5f).alpha(0);
            btnAttachImage.animate().translationX(w);
            btnSendStateVisible = false;
        } else if(visible && !btnSendStateVisible){
            btnSend.animate().translationX(0).alpha(1);
            btnAttachImage.animate().translationX(0);
            btnSendStateVisible = true;
        }
    }

    private void updateCurrentlyUploadingView() {
        if(uploadingDocuments > 0) {
            holderCurrentlyUploading.setVisibility(View.VISIBLE);
            tvCountUploading.setText("Uploading " + uploadingDocuments + " document(s)");
        } else {
            holderCurrentlyUploading.setVisibility(View.GONE);
        }

        if (documentsToSend.size() > 0) {
            tvCountUploaded.setVisibility(View.VISIBLE);
            tvCountUploaded.setText("Uploaded " + documentsToSend.size() + " document(s)");
        } else {
            tvCountUploaded.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityWindowUtils.setAdjustResizeWindow(getActivity());
    }

    private void getChatMessages() {
        Log.d("FragmentTicketChat", "GetChatMessages: lang=" + systemLanguage);
        HolaRESTClient.get()
                .getChatMessages(HolaRESTClient.getCsrfToken(), ChatManager.currentChatId, systemLanguage, translateEnabled)
                .enqueue(new Callback<MessagesResponse>() {
                    @Override
                    public void onResponse(Call<MessagesResponse> call, Response<MessagesResponse> response) {
                        if (response.code() == 200 && response.body() != null) {
                            updateMessages(response.body().getMessages());
                            currentChat = response.body().getChat();
                            updateMenu();
                            updateChatImage();
                            if(getActivity() instanceof MainActivity
                                    && currentChat != null
                                    && currentChat.getOthers() != null) {
                                ((MainActivity) getActivity()).setToolbarTitle(currentChat.getTitle());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessagesResponse> call, Throwable t) {

                    }
                });
    }

    private void updateChatImage() {
        if(isAdded()) {
            String imgUrl = HolaRESTClient.getEndpoint();
            if (currentChat.getOthers().size() == 1) {
                imgUrl += currentChat.getOthers().get(0).getAvatar();
            } else {
                imgUrl += currentChat.getPicture();
            }
            ((MainActivity) getActivity()).setToolbarImage(imgUrl);
        }
    }


    private void updateMessages(List<Message> messages) {
        boolean shouldScroll = messages.size() > this.messages.size();
        this.messages.clear();
        this.messages.addAll(messages);
        if (rvMessages != null) {
            shouldScroll = shouldScroll || emptyHolder.getVisibility() == View.VISIBLE;
            rvMessages.getAdapter().notifyDataSetChanged();
            if (this.messages.size() == 0) {
                rvMessages.setVisibility(View.GONE);
                emptyHolder.setVisibility(View.VISIBLE);
            } else {
                rvMessages.setVisibility(View.VISIBLE);
                emptyHolder.setVisibility(View.GONE);
            }

            if(shouldScroll) {
                rvMessages.scrollToPosition(messages.size() - 1);
            }
        }

    }


    @OnClick(R.id.iv_send)
    void send() {
        if(uploadingDocuments > 0){
            Toast.makeText(getContext(), "Please wait until all documents are uploaded", Toast.LENGTH_SHORT).show();
        } else {
            MessageBody body = new MessageBody(etText.getText().toString(), documentsToSend);
            HolaRESTClient.get()
                    .sendMessage(HolaRESTClient.getCsrfToken(), ChatManager.currentChatId,
                            body, systemLanguage)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            documentsToSend.clear();

                            getChatMessages();
                            updateView();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            getChatMessages();
                            updateView();
                        }
                    });
            etText.setText("");
        }
    }

    private FileUploader.DocumentUploadListener documentUploadListener = new FileUploader.DocumentUploadListener() {

        @Override
        public void onDocumentUploadStarted(String fileName) {
            uploadingDocuments++;
            updateView();
        }

        @Override
        public void onSuccess(DocumentUploadResponse response) {
            uploadingDocuments--;
            documentsToSend.add(response);
            updateView();
        }

        @Override
        public void onFailure(String reason) {
            uploadingDocuments--;

            if (isAdded()) {
                Toast.makeText(getContext(), "Could not upload file: " + reason, Toast.LENGTH_SHORT).show();
                updateView();
            }
        }
    };

    @OnClick(R.id.iv_attach_file) void attachFile(){
        ((MainActivity)getActivity()).uploadFile(documentUploadListener);
    }

    @OnClick(R.id.iv_attach_image) void attachImage(){
        ((MainActivity)getActivity()).uploadImage(documentUploadListener);
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_single_chat, menu);
        menuTranslateEnabled = menu.findItem(R.id.translateEnabled);
        menuChangeAdmin = menu.findItem(R.id.menu_item_change_admin);
        menuDeleteUser = menu.findItem(R.id.menu_item_delete_user);
        menuChangePicture = menu.findItem(R.id.menu_item_change_picture);

        menuTranslateEnabled.setVisible(HolaApp.isPremium());
    }

    private void updateMenu() {
        if(currentChat != null) {
            boolean isAdmin = currentChat.adminId == AccountManager.currentAccountId;
            menuChangeAdmin.setVisible(isAdmin);
            menuDeleteUser.setVisible(isAdmin);
            menuChangePicture.setVisible(isAdmin);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.translateEnabled:
                if(translateEnabled.equals(HolaApi.TRANSLATE_ENABLED)){
                    translateEnabled = HolaApi.TRANSLATE_DISABLED;
                    menuTranslateEnabled.setChecked(false);
                } else {
                    translateEnabled = HolaApi.TRANSLATE_ENABLED;
                    menuTranslateEnabled.setChecked(true);
                }
                getChatMessages();
                break;
            case R.id.menu_item_change_admin:
                changeAdmin();
                break;
            case R.id.menu_item_delete_user:
                deleteUser();
                break;
            case R.id.menu_item_change_picture:
                changePicture();
                break;
        }
        return false;
    }

    private void changePicture() {
        ((MainActivity)getActivity()).uploadChatPicture(currentChat.getId(), new FileUploader.UploadChatPicListener() {
            @Override
            public void onSuccess(Chat newChat) {
                currentChat = newChat;
                updateView();
                Toast.makeText(getContext(), "Chat picture is successfully uploaded", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(String reason) {
                Toast.makeText(getContext(), "Error: " + reason, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteUser() {
        new SelectUserDialog<Other>(getContext(), currentChat.getOthers(), user -> {
            HolaRESTClient.get()
                    .chatDeleteUser(HolaRESTClient.getCsrfToken(), currentChat.getId(), new DeleteUserBody(user.getId()))
            .enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code() == 200){
                        Toast.makeText(getContext(), user.getFullName() + " is deleted from chat.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Could not delete user from chat.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DeleteUser->OnResponse->code=" + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Could not delete user from chat.", Toast.LENGTH_SHORT).show();
                }
            });
        }).setTitle("Delete user").show();
    }

    private void changeAdmin() {
        new SelectUserDialog<Other>(getContext(), currentChat.getOthers(), user -> {
            HolaRESTClient.get()
                    .chatChangeAdmin(HolaRESTClient.getCsrfToken(), currentChat.getId(), new ChangeAdminBody(user.getId()))
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.code() == 200){
                                Toast.makeText(getContext(), user.getFullName() + " is now admin.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Could not change chat admin.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getContext(), "Could not change chat admin.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }).setTitle("Select chat admin")
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        disposableBag.clear();
    }
}
