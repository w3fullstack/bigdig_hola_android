package com.hola.hola.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hola.hola.R;
import com.hola.hola.REST.HolaApi;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.manager.SecretChatManager;
import com.hola.hola.model.AllChats;
import com.hola.hola.model.Chat;
import com.hola.hola.ui.HolaApp;
import com.hola.hola.ui.activities.MainActivity;
import com.hola.hola.ui.adapter.MyChatAdapter;
import com.hola.hola.ui.dialog.SecretChatPasswordDialog;
import com.hola.hola.ui.view.MyChatsScreen;

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

public class FragmentMyChat extends Fragment
        implements MyChatsScreen, SwipeRefreshLayout.OnRefreshListener {

    private static final long TIMER_DELAY_MS = 5000;
    CompositeDisposable disposableBag = new CompositeDisposable();

    private RecyclerView mRecyclerView;
    private MyChatAdapter myChatAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Chat> chatModelList = new ArrayList<>();
    private TextView textViewEmpty;
    private FloatingActionButton fabAdd;
    private MenuItem menuTranslateEnabled;

    private String systemLanguage = Locale.getDefault().getLanguage();
    private Integer translateEnabled = HolaApi.TRANSLATE_ENABLED;

    SecretChatManager secretChatManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle(R.string.my_chats_title);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_my_chats, container, false);
        textViewEmpty = rootView.findViewById(R.id.empty);

        if (chatModelList.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
        }

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myChatAdapter = new MyChatAdapter(this);
        mRecyclerView.setAdapter(myChatAdapter);
        myChatAdapter.setList(chatModelList);

        swipeRefreshLayout = rootView.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        fabAdd = rootView.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            startChat();
        });
        getChats(false);

        disposableBag.add(
                Observable.interval(TIMER_DELAY_MS, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(next -> getChats(false), err -> {
                        }, () -> {
                        })
        );

        secretChatManager = new SecretChatManager(getContext());
        return rootView;
    }

    private void startChat() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateSmall(MainActivity.FRAGMENT_NEW_CHAT);
        }
    }

    private void getChats(boolean showProgress) {
        if (showProgress) {
            swipeRefreshLayout.setRefreshing(true);
        }
        HolaRESTClient.get()
                .getChatList(HolaRESTClient.getCsrfToken(), systemLanguage, translateEnabled)
                .enqueue(new Callback<AllChats>() {
                    @Override
                    public void onResponse(Call<AllChats> call, Response<AllChats> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (response.body() != null) {
                            updateChat(response.body().chats);
                        }
                    }

                    @Override
                    public void onFailure(Call<AllChats> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void updateChat(List<Chat> chatModels) {
        if (chatModels.size() == 0)
            textViewEmpty.setVisibility(View.VISIBLE);
        else
            textViewEmpty.setVisibility(View.GONE);
//        DiffUtil.calculateDiff(new MyDiffCallback(chatModels, chatModelList)).dispatchUpdatesTo(myChatAdapter);
        chatModelList.clear();
        chatModelList.addAll(chatModels);
        myChatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_chats, menu);
        menuTranslateEnabled = menu.findItem(R.id.translateEnabled);
        if(!HolaApp.isPremium()) {
            menuTranslateEnabled.setVisible(false);
            menuTranslateEnabled.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.translateEnabled:
                if (translateEnabled.equals(HolaApi.TRANSLATE_ENABLED)) {
                    translateEnabled = HolaApi.TRANSLATE_DISABLED;
                    menuTranslateEnabled.setChecked(false);
                } else {
                    translateEnabled = HolaApi.TRANSLATE_ENABLED;
                    menuTranslateEnabled.setChecked(true);
                }
                getChats(true);
                break;
        }
        return false;
    }

    @Override
    public void onItemSelected(int chatId) {
//        if (secretChatManager.isChatSecret(chatId)) {
//            checkPassword(chatId);
//        } else {
//            ChatManager.currentChatId = chatId;
//            ((MainActivity) getActivity()).navigateSmall(MainActivity.FRAGMENT_TICKET_CHAT);
//        }
        ((MainActivity)getActivity()).openChat(chatId);
    }

    @Override
    public void onItemSwiped(int chatId) {
        if(secretChatManager.isChatSecret(chatId)){
            new SecretChatPasswordDialog(getContext(), SecretChatPasswordDialog.MODE_DELETE, chatId, new SecretChatPasswordDialog.Callback() {
                @Override
                public void onSuccess() {
                    getChats(false);
                }

                @Override
                public void onRetry() {

                }
            }).show();
        } else {
            new SecretChatPasswordDialog(getContext(), SecretChatPasswordDialog.MODE_SET, chatId, new SecretChatPasswordDialog.Callback() {
                @Override
                public void onSuccess() {
                    getChats(false);
                }

                @Override
                public void onRetry() {

                }
            }).show();
        }
    }

//    private void checkPassword(int chatId) {
//        new SecretChatPasswordDialog(getContext(), SecretChatPasswordDialog.MODE_CHECK, chatId, new SecretChatPasswordDialog.Callback() {
//            @Override
//            public void onSuccess() {
//                ChatManager.currentChatId = chatId;
//                if (isAdded()) {
//                    ((MainActivity) getActivity()).navigateSmall(MainActivity.FRAGMENT_TICKET_CHAT);
//                }
//            }
//
//            @Override
//            public void onRetry() {
//                checkPassword(chatId);
//            }
//        }).show();
//    }

    @Override
    public SecretChatManager getSecretChatManager() {
        return secretChatManager;
    }

    @Override
    public void refresh() {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getChats(true);
    }

    @Override
    public void onDestroyView() {
        disposableBag.clear();
        super.onDestroyView();
    }
}