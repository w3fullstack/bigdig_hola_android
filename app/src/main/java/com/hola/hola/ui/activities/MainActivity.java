package com.hola.hola.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hola.hola.REST.HolaApi;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.manager.ChatManager;
import com.hola.hola.manager.SecretChatManager;
import com.hola.hola.model.Chat;
import com.hola.hola.model.UserContact;
import com.hola.hola.ui.HolaApp;
import com.hola.hola.ui.dialog.SecretChatPasswordDialog;
import com.hola.hola.ui.fragments.FragmentBugReport;
import com.hola.hola.ui.fragments.FragmentContacts;
import com.hola.hola.ui.fragments.FragmentNewSecretChat;
import com.hola.hola.util.files.FileUploader;
import com.hola.hola.R;
import com.hola.hola.manager.AccountManager;
import com.hola.hola.manager.LoginManager;
import com.hola.hola.ui.fragments.FragmentMyChat;
import com.hola.hola.ui.fragments.FragmentNewChat;
import com.hola.hola.ui.fragments.FragmentSettings;
import com.hola.hola.ui.fragments.FragmentTicketChat;
import com.hola.hola.ui.fragments.pin.FragmentPinCode;
import com.hola.hola.ui.fragments.pin.PinCodeContract;
import com.hola.hola.util.ActivityWindowUtils;
import com.hola.hola.util.files.FileChooser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PinCodeContract.ResultListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawer;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.toolbar_image) CircleImageView toolbarImage;
    @BindView(R.id.nvView) NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private CircleImageView ivHeaderPhoto;
    private TextView tvProfileName;
    private TextView tvProfilePhone;
    private TextView tvProfilePremium;

    public static final int FRAGMENT_MY_CHATS = 21;
    public static final int FRAGMENT_TICKET_CHAT = 23;
    public static final int FRAGMENT_SETTINGS = 26;
    public static final int FRAGMENT_NEW_CHAT = 27;
    public static final int FRAGMENT_NEW_SECRET_CHAT = 28;
    public static final int FRAGMENT_REPORT_BUG = 29;

    public static final int FRAGMENT_CHANGE_PIN = 5;
    public static final int FRAGMENT_CONTACTS = 30;

    private static final String STATE_OPEN_CHAT = "STATE_OPEN_CHAT";


    private FileChooser fileChooser;
    private CompositeDisposable disposableBag = new CompositeDisposable();


    SecretChatManager secretChatManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getUserData();

        setNavigationDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Log.d(TAG, "---- On backstack changed ----");
            int sz = getSupportFragmentManager().getBackStackEntryCount();
            for (int i = 0; i < sz; ++i) {
                Log.d(TAG, "BackStackEntry: " + getSupportFragmentManager().getBackStackEntryAt(i).getName());
            }
            Log.d(TAG, "---- End on backstack changed ----");
            ActivityWindowUtils.hideKeyboard(MainActivity.this);
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                displayToolbarButton(true);
            }
            setToolbarImage("");
        });

        checkPermissions();
        fileChooser = new FileChooser(this);
        secretChatManager = new SecretChatManager(this);

        Log.d(TAG, "---- On Create ----");
        navigateSmall(FRAGMENT_MY_CHATS);
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_OPEN_CHAT)) {
            ChatManager.currentChatId = savedInstanceState.getInt(STATE_OPEN_CHAT);
            navigateSmall(FRAGMENT_TICKET_CHAT);
            Log.d(TAG, "Navigating to saved state chat");
        }
        Log.d(TAG, "---- End On Create ----");

        disposableBag.add(
                AccountManager.currentUserSubject.subscribe(user -> {
                    if (tvProfileName != null) {
                        tvProfileName.setText(user.getFullName());
                        tvProfilePhone.setText(user.getPhoneNumber());
                        Picasso.get().load(HolaRESTClient.getEndpoint() + user.getAvatar())
                                .placeholder(R.drawable.ic_account_circle_black_48dp)
                                .into(ivHeaderPhoto);
                    }
                })
        );
    }

    private void getUserData() {
        HolaRESTClient.get().getUserData(HolaRESTClient.getCsrfToken())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d(TAG, "getUserData->onResponse code=" + response.code());
                        if (response.code() == 200) {
                            AccountManager.setCurrentUserFromJson(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e(TAG, "getUserData failed: " + t.getMessage(), t);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int sz = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "---- On Resume -----   BackStackEntryCount=" + sz);
        for (int i = 0; i < sz; ++i) {
            Log.d(TAG, "[onResume] BackStackEntry: " + getSupportFragmentManager().getBackStackEntryAt(i).getName());
        }

        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); ++i) {
            Log.d(TAG, "[onResume] Fragment: " + getSupportFragmentManager().getFragments().get(i).getClass().getSimpleName());
        }
        Log.d(TAG, "---- End On Resume ----");
        getUserData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList.size() > 0 &&
                fragmentList.get(fragmentList.size() - 1) instanceof FragmentTicketChat) {
            outState.putInt(STATE_OPEN_CHAT, ChatManager.currentChatId);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void setNavigationDrawer() {
        nvDrawer.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header_main);
        ivHeaderPhoto = headerLayout.findViewById(R.id.profileImage);
        tvProfileName = headerLayout.findViewById(R.id.profileName);
        tvProfilePhone = headerLayout.findViewById(R.id.profilePhone);
        tvProfilePremium = headerLayout.findViewById(R.id.profilePremium);
        tvProfilePremium.setVisibility(HolaApp.isPremium() ? View.VISIBLE : View.GONE);
//        tvProfileName.setText("User name");
//        tvProfilePhone.setText("User phone");
        UserContact user = AccountManager.currentUser;
        if (user != null) {
            tvProfileName.setText(user.getFullName());
            tvProfilePhone.setText(user.getPhoneNumber());
            Picasso.get().load(user.getAvatar())
                    .placeholder(R.drawable.ic_account_circle_black_48dp)
                    .into(ivHeaderPhoto);
        }
        drawerToggle.syncState();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_new_chat:
                navigateSmall(FRAGMENT_NEW_CHAT);
                break;
            case R.id.nav_new_secret_chat:
                navigateSmall(FRAGMENT_NEW_SECRET_CHAT);
                break;
//            case R.id.nav_saved_messages:
//                Log.d("nav_logout", "nav_logout");
//                break;
//            case R.id.nav_local_storage:
//                Log.d("image_view_share", "image_view_share");
//                break;
//            case R.id.nav_cloud_storage:
//                Log.d("image_view_share", "image_view_share");
//                break;
//            case R.id.nav_invite_friends:
//                Log.d("image_view_share", "image_view_share");
//                break;
//            case R.id.nav_calls:
//                Log.d("image_view_share", "image_view_share");
//                break;
//            case R.id.nav_camera:
//                Log.d("image_view_share", "image_view_share");
//                break;
            case R.id.nav_contacts:
                Log.d("image_view_share", "image_view_share");
                navigateSmall(FRAGMENT_CONTACTS);
                break;
            case R.id.nav_settings:
                navigateSmall(FRAGMENT_SETTINGS);
                break;
            case R.id.nav_logout:
                askLogout();
                break;
        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private void askLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Do you really want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    new LoginManager(getApplicationContext(), null).clearSession();
                    AccountManager.currentAccountId = -1;
                    Intent i = new Intent(MainActivity.this, SplashScreenActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();
                })
                .setNegativeButton("No", null)
                .create().show();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void navigateSmall(int id) {
        Fragment fragment = null;
        String name;
        setToolbarTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            displayToolbarButton(true);
        }
        switch (id) {
            case FRAGMENT_MY_CHATS:
                fragment = new FragmentMyChat();
                displayToolbarButton(true);
                setToolbarTitle(R.string.my_chats_title);
                break;
            case FRAGMENT_NEW_CHAT:
                fragment = new FragmentNewChat();
                displayToolbarButton(false);
                setToolbarTitle(R.string.new_chat_title);
                break;
            case FRAGMENT_NEW_SECRET_CHAT:
                fragment = new FragmentNewSecretChat();
                displayToolbarButton(false);
                setToolbarTitle(R.string.new_secret_chat_title);
                break;
            case FRAGMENT_TICKET_CHAT:
                fragment = new FragmentTicketChat();
                displayToolbarButton(false);
                break;
            case FRAGMENT_SETTINGS:
                fragment = new FragmentSettings();
                displayToolbarButton(false);
                setToolbarTitle(R.string.settings_title);
                break;
            case FRAGMENT_REPORT_BUG:
                fragment = new FragmentBugReport();
                displayToolbarButton(false);
                setToolbarTitle(R.string.title_fragment_bugreport);
                break;
            case FRAGMENT_CONTACTS:
                fragment = new FragmentContacts();
                displayToolbarButton(false);
                setToolbarTitle(R.string.menu_contacts);
                break;
        }
        if (fragment != null) {
            name = fragment.getClass().getSimpleName();

            boolean popped = getSupportFragmentManager().popBackStackImmediate(name, 0);
            if (!popped && getSupportFragmentManager().findFragmentByTag(name) == null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment fullscreenFragment =
                        getSupportFragmentManager().findFragmentById(R.id.small_container);
                if (fullscreenFragment != null && !fullscreenFragment.isHidden()) {
                    transaction.hide(fullscreenFragment);
                }
                transaction.replace(R.id.small_container, fragment, name).addToBackStack(name).commit();
            }
        }
    }

    public void displayToolbarButton(boolean drawerIndicator) {
        if (drawerIndicator) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(null);
        } else {
            drawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(v -> onBackPressed());
        }
    }

    public void navigateFullscreen(int id) {
        Fragment fragment = null;
        String name = "fullscreen";
        setToolbarTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        switch (id) {
            case FRAGMENT_CHANGE_PIN:
                fragment = FragmentPinCode.newInstance(PinCodeContract.State.CHANGE_PIN, this);
                break;
        }
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fullscreenFragment =
                    getSupportFragmentManager().findFragmentById(R.id.small_container);
            if (fullscreenFragment != null && !fullscreenFragment.isHidden()) {
                transaction.hide(fullscreenFragment);
            }
            transaction.replace(R.id.small_container, fragment).addToBackStack(name).commit();
        }
    }

    public void setToolbarImage(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            toolbarImage.setVisibility(View.GONE);
        } else {
            toolbarImage.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_account_circle_black_48dp)
                    .into(toolbarImage);
        }
    }

    public void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    public void setToolbarTitle(int resId) {
        toolbarTitle.setText(resId);
    }

    private void popFullscreenFragment() {
        getSupportFragmentManager().popBackStackImmediate("fullscreen", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportActionBar().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fileChooser.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPinCodeSet() {
        popFullscreenFragment();
        Toast.makeText(this, "PIN code changed successfully.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPinCodeChecked(boolean success) {
        popFullscreenFragment();
    }

    @Override
    public void onPinCancelled() {
        popFullscreenFragment();

    }

    public void uploadImage(FileUploader.DocumentUploadListener listener) {
        checkPermissions();
        fileChooser.startChooseImage((uri) -> onFileChosen(listener, uri));
    }

    public void uploadFile(final FileUploader.DocumentUploadListener listener) {
        checkPermissions();
        fileChooser.startChooseFile((uri) -> onFileChosen(listener, uri));
    }

    public void uploadChatPicture(int chatId, final FileUploader.UploadChatPicListener listener) {
        checkPermissions();
        fileChooser.startChooseImage((uri) -> {
            disposableBag.add(
                    FileUploader.uploadAvatar(getApplicationContext(), HolaRESTClient.getEndpoint() + HolaApi.METHOD_UPLOAD_CHAT_PIC + chatId, uri, Chat.class)
                            .subscribe(
                                    listener::onSuccess,
                                    err -> listener.onFailure(err.getLocalizedMessage())));
        });
    }


    public void changeUserAvatar() {
        checkPermissions();
        fileChooser.startChooseImage(uri -> {
            disposableBag.add(
                    FileUploader.uploadAvatar(getApplicationContext(), HolaRESTClient.getEndpoint() + "/app/user/ava", uri, UserContact.class)
                            .subscribe(userContact -> AccountManager.setCurrentUser(userContact)));
        });
    }

    private void onFileChosen(FileUploader.DocumentUploadListener listener, Uri uri) {
        try {
            disposableBag.add(FileUploader.upload(getApplicationContext(), uri)
                    .subscribe(
                            listener::onSuccess,
                            e -> listener.onFailure(e.getLocalizedMessage())
                    )
            );
            listener.onDocumentUploadStarted("TODO");
        } catch (Throwable t) {
            Log.e("MainActivity", "Error: " + t.toString());
            t.printStackTrace();
        }
    }

    private void checkPermissions() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(DialogOnAnyDeniedMultiplePermissionsListener.Builder
                        .withContext(this)
                        .withTitle("Read & write storage permission")
                        .withMessage("Both read and write to storage permissions are needed to download and upload file attachments")
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_hola)
                        .build())
                .check();
    }

    public void openChat(int chatId) {
        if (secretChatManager.isChatSecret(chatId)) {
            new SecretChatPasswordDialog(this, SecretChatPasswordDialog.MODE_CHECK, chatId, new SecretChatPasswordDialog.Callback() {
                @Override
                public void onSuccess() {
                    ChatManager.currentChatId = chatId;
                    navigateSmall(FRAGMENT_TICKET_CHAT);
                }

                @Override
                public void onRetry() {

                }
            }).show();
        } else {
            ChatManager.currentChatId = chatId;
            navigateSmall(FRAGMENT_TICKET_CHAT);
        }
    }

}
