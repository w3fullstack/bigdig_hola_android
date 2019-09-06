package com.hola.hola.ui.fragments;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hola.hola.R;
import com.hola.hola.model.Contact;
import com.hola.hola.ui.adapter.ContactAdapter;
import com.hola.hola.util.ListItemDecoration;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.schedulers.Schedulers;

public class FragmentContacts extends Fragment {

    private Unbinder unbinder;
    private RxPermissions rxPermissions;
    private Cursor cursor;
    private final String TAG = "mLog";
     private CompositeDisposable compositeDisposable;
    private ContactAdapter contactAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView rvContacts;

    public static FragmentContacts newInstance() {
        return new FragmentContacts();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_chat, container, false);
        unbinder = ButterKnife.bind(this, v);
        init();
        return v;
    }

    public void init() {
        compositeDisposable = new CompositeDisposable();
        rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_CONTACTS)
                .subscribe(granted -> {
                    if (granted) {
                        displayContactsList();
                    } else {
                        showMessage(R.string.allow_permission_for_contacts_in_settings);
                    }
                });

        contactAdapter = new ContactAdapter(getContext());

        rvContacts.setNestedScrollingEnabled(false);
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContacts.setAdapter(contactAdapter);
        rvContacts.addItemDecoration(new ListItemDecoration(5));
    }

    public void showMessage(int resId) {
        new AlertDialog.Builder(getContext())
                .setMessage(getString(resId))
                .setPositiveButton(R.string.OK, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    public void displayContactsList() {
        cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        compositeDisposable.add(Flowable.just(1)
                .map(cursor1 -> {
                    String name, phonenumber;
                    List<Contact> contactList = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String imageUriString = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                        Uri imageUri = null;
                        if (!TextUtils.isEmpty(imageUriString))
                            imageUri = Uri.parse(imageUriString);

                        contactList.add(new Contact(name, phonenumber, imageUri));
                    }
                    return contactList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> cursor.close())
                .subscribe(contacts -> {
                  //  for (Contact contact : contacts)
                    //    Log.d(TAG, contact.toString());
                    contactAdapter.setContactList(contacts);
                }, throwable -> {
                    Log.e(TAG, "error while fetching contacts");
                    throwable.printStackTrace();
                })
        );
    }

    @Override
    public void onDestroy() {
        if (unbinder != null)
            unbinder.unbind();
        compositeDisposable.clear();
        super.onDestroy();
    }
}
