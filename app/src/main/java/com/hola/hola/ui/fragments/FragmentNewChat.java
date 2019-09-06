package com.hola.hola.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.hola.hola.R;
import com.hola.hola.REST.BooleanDeserializer;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.manager.AccountManager;
import com.hola.hola.model.ChatAddInfo;
import com.hola.hola.model.body.GroupChatBody;
import com.hola.hola.model.UserContact;
import com.hola.hola.ui.activities.MainActivity;
import com.hola.hola.ui.adapter.MembersAdapter;
import com.hola.hola.ui.adapter.NewChatAdapter;
import com.hola.hola.ui.view.NewChatScreen;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewChat extends Fragment implements NewChatScreen, SwipeRefreshLayout.OnRefreshListener {

    private static final long TIMER_DELAY_MS = 2000;
    private static final String TAG = FragmentNewChat.class.getCanonicalName();
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerViewMembers) RecyclerView recyclerViewMembers;
    @BindView(R.id.btn_create_chat) Button bottomButton;

    private NewChatAdapter adapter;
    private MembersAdapter membersAdapter;

    private String filter = "";

    List<UserContact> userList = new ArrayList<>();
    List<UserContact> addedUserList = new ArrayList<>();

    CompositeDisposable disposableBag = new CompositeDisposable();

    public FragmentNewChat() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle(getTitleRes());
        }
    }

    public int getTitleRes(){
        return R.string.new_chat_title;
    }

    public int getBottomButtonTextRes(){
        return R.string.create_chat;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_chat, container, false);
        ButterKnife.bind(this, rootView);

        setHasOptionsMenu(true);

        bottomButton.setText(getBottomButtonTextRes());

        refreshLayout.setOnRefreshListener(this);

        adapter = new NewChatAdapter(this);
        adapter.setUserList(userList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager membersLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        membersAdapter = new MembersAdapter(addedUserList, this::onAddedItemSelected);
        recyclerViewMembers.setLayoutManager(membersLayoutManager);
        recyclerViewMembers.setAdapter(membersAdapter);

//        disposableBag.add(
//                Observable.interval(TIMER_DELAY_MS, TimeUnit.MILLISECONDS)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(next -> getUserList(filter, false),
//                                err -> {
//                                }, () -> {
//                                }));

        getUserList(filter, true);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_new_chat, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchItem = menu.findItem(R.id.search_users);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter = query;
                getUserList(query, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter = newText;
                getUserList(newText, false);
                return false;
            }
        });
    }


    private void getUserList(String filter, boolean showProgress) {
        if (showProgress) {
            refreshLayout.setRefreshing(true);
        }
        HolaRESTClient
                .get()
                .getUsersByFilter(HolaRESTClient.getCsrfToken(), filter)
                .enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        refreshLayout.setRefreshing(false);
                        Type listType = new TypeToken<List<UserContact>>() {
                        }.getType();
                        BooleanDeserializer deserializer = new BooleanDeserializer();
                        List<UserContact> newList = new GsonBuilder()
                                .registerTypeAdapter(Boolean.class, deserializer)
                                .registerTypeAdapter(boolean.class, deserializer)
                                .create()
                                .fromJson(response.body(), listType);
                        Log.d(TAG, "Got response list: " + newList);
                        updateList(newList);
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                        Log.d(TAG, "Failure: " + t.getMessage());
                        t.printStackTrace();
                    }
                });
    }

    private void updateList(List<UserContact> list) {
        userList.clear();
        userList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        getUserList(filter, true);
    }

    @Override
    public void onItemSelected(UserContact userContact) {
        if (userContact.getId() == AccountManager.currentAccountId) {
            return;
        }
        if (!containsId(addedUserList, userContact.getId())) {
            addedUserList.add(userContact);
            membersAdapter.notifyDataSetChanged();
            recyclerViewMembers.post(() -> {
                recyclerViewMembers.smoothScrollToPosition(addedUserList.size() - 1);
            });
        }
//        Toast.makeText(getContext(), "Item selected: " + userId, Toast.LENGTH_LONG).show();
    }

    public void onAddedItemSelected(UserContact userContact) {
        //TODO: remove from addedMembersList
        int toRemove = -1;
        for(int i = 0; i < addedUserList.size(); ++i){
            if(addedUserList.get(i).getId().equals(userContact.getId())){
                toRemove = i;
                break;
            }
        }
        addedUserList.remove(toRemove);
        membersAdapter.notifyDataSetChanged();
    }

    protected void createChatWith(int userId) {
        HolaRESTClient.get().startChat(HolaRESTClient.getCsrfToken(), userId).enqueue(new Callback<ChatAddInfo>() {
            @Override
            public void onResponse(Call<ChatAddInfo> call, Response<ChatAddInfo> response) {
                if (response.isSuccessful()) {
                    int chatId = response.body().getId();
                    openChat(chatId);
                } else {
                    Log.d(TAG, "Start Chat unsuccessful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ChatAddInfo> call, Throwable t) {
                Log.d(TAG, "Start Chat failure: " + t.getMessage(), t);

            }
        });
    }

    protected void openChat(int chatId) {
        ((MainActivity) getActivity()).openChat(chatId);
    }

    protected void createGroupChatWith(List<Integer> userIds) {
        HolaRESTClient.get().startGroupChat(HolaRESTClient.getCsrfToken(), new GroupChatBody(userIds))
                .enqueue(new Callback<ChatAddInfo>() {
                    @Override
                    public void onResponse(Call<ChatAddInfo> call, Response<ChatAddInfo> response) {
                        if (response.isSuccessful()) {
                            addedUserList.clear();
                            membersAdapter.notifyDataSetChanged();
                            int chatId = response.body().getId();
                            openChat(chatId);
                        } else {
                            Log.d(TAG, "Start Chat unsuccessful: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatAddInfo> call, Throwable t) {
                        Log.d(TAG, "Start Chat failure: " + t.getMessage(), t);
                    }
                });
    }

    @OnClick(R.id.btn_create_chat)
    void createChat() {
        if (addedUserList.size() == 1) {
            createChatWith(addedUserList.get(0).getId());
        } else {
            List<Integer> userIds = new ArrayList<>();
//            userIds.add(AccountManager.currentAccountId);
            for (UserContact userContact : addedUserList) {
                userIds.add(userContact.getId());
            }
            createGroupChatWith(userIds);
        }
    }

    @Override
    public void onDestroyView() {
        disposableBag.clear();
        super.onDestroyView();
    }

    protected boolean containsId(List<UserContact> list, int userId) {
        for (UserContact userContact : list) {
            if (userContact.getId() == userId)
                return true;
        }
        return false;
    }
}
