package com.hola.hola.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.hola.hola.R;
import com.hola.hola.ui.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;


public class FragmentSettings extends Fragment {
    @BindView(R.id.settingsRootLayout) LinearLayout rootLayout;

    @BindView(R.id.profileImage)    ImageView profileImage;
    @BindView(R.id.profileName)    TextView profileName;
    @BindView(R.id.profilePhone)    TextView profilePhone;
    @BindView(R.id.profileEmail)    TextView profileEmail;
    @Nullable
    @BindView(R.id.profilePremium) TextView profilePremium;

    // Show information block
    @Nullable @BindView(R.id.profileShowInformation) LinearLayout profileShowInformation;
    @Nullable @BindView(R.id.rlLastSeen)    RelativeLayout rlLastSeen;
    @Nullable @BindView(R.id.switchLastSeen)    Switch switchLastSeen;
    @Nullable @BindView(R.id.rlUserOnline)    RelativeLayout rlUserOnline;
    @Nullable @BindView(R.id.switchUserOnline)    Switch switchUserOnline;
    @Nullable @BindView(R.id.rlViewedMessages)    RelativeLayout rlViewedMessages;
    @Nullable @BindView(R.id.switchViewedMessages)    Switch switchViewedMessages;

    // Notifications block
    @BindView(R.id.rlPushNotifications)    RelativeLayout rlPushNotifications;
    @BindView(R.id.switchPushNotifications)    Switch switchPushNotifications;
    @BindView(R.id.rlShowPreviews)    RelativeLayout rlShowPreviews;
    @BindView(R.id.switchShowPreviews)    Switch switchShowPreviews;
    @BindView(R.id.rlChatNotifications)    RelativeLayout rlChatNotifications;
    @BindView(R.id.tvChatNotifications)    TextView tvChatNotifications;
    @BindView(R.id.rlGroupNotifications)    RelativeLayout rlGroupNotifications;
    @BindView(R.id.tvGroupNotifications)    TextView tvGroupNotifications;
    @BindView(R.id.rlSelectNotificationsSound)    RelativeLayout rlSelectNotificationsSound;
    @BindView(R.id.tvSelectNotificationsSound)    TextView tvSelectNotificationsSound;
    @BindView(R.id.rlResetNotifications)    RelativeLayout rlResetNotifications;

    // Multi-device block
    @BindView(R.id.rlSetPassphrase) RelativeLayout rlSetPassphrase;
    @BindView(R.id.rlQrCodeScan) RelativeLayout rlQrCodeScan;
    @BindView(R.id.switchQrCodeScan) Switch switchQrCodeScan;
    @BindView(R.id.rlRetentionTime) RelativeLayout rlRetentionTime;
    @BindView(R.id.tvRetentionTime) TextView tvRetentionTime;

    // Media / Photo
    @BindView(R.id.rlPhotoAutoDownloadAndDisplay) RelativeLayout rlPhotoAutoDownloadAndDisplay;
    @BindView(R.id.switchPhotoAutoDownloadAndDisplay) Switch switchPhotoAutoDownloadAndDisplay;
    @BindView(R.id.rlPhotoAutoSave) RelativeLayout rlPhotoAutoSave;
    @BindView(R.id.switchPhotoAutoSave) Switch switchPhotoAutoSave;
    @BindView(R.id.rlPhotoSetPin) RelativeLayout rlPhotoSetPin;
    @BindView(R.id.rlPhotoDestroy) RelativeLayout rlPhotoDestroy;
    @BindView(R.id.tvPhotoDestroy) TextView tvPhotoDestroy;

    // Media / Text
    @BindView(R.id.rlTextAutoDownloadAndDisplay) RelativeLayout rlTextAutoDownloadAndDisplay;
    @BindView(R.id.switchTextAutoDownloadAndDisplay) Switch switchTextAutoDownloadAndDisplay;
    @BindView(R.id.rlTextAutoSave) RelativeLayout rlTextAutoSave;
    @BindView(R.id.switchTextAutoSave) Switch switchTextAutoSave;
    @BindView(R.id.rlTextSetPin) RelativeLayout rlTextSetPin;
    @BindView(R.id.rlTextDestroy) RelativeLayout rlTextDestroy;
    @BindView(R.id.tvTextDestroy) TextView tvTextDestroy;

    // Media / Documents
    @BindView(R.id.rlDocsAutoDownloadAndDisplay) RelativeLayout rlDocsAutoDownloadAndDisplay;
    @BindView(R.id.switchDocsAutoDownloadAndDisplay) Switch switchDocsAutoDownloadAndDisplay;
    @BindView(R.id.rlDocsAutoSave) RelativeLayout rlDocsAutoSave;
    @BindView(R.id.switchDocsAutoSave) Switch switchDocsAutoSave;
    @BindView(R.id.rlDocsSetPin) RelativeLayout rlDocsSetPin;
    @BindView(R.id.rlDocsDestroy) RelativeLayout rlDocsDestroy;
    @BindView(R.id.tvDocsDestroy) TextView tvDocsDestroy;

    // Media storage
    @BindView(R.id.rlClearAllNow) RelativeLayout rlClearAllNow;

    // Screen lock
    @Nullable @BindView(R.id.rlLockScreen) RelativeLayout rlLockScreen;
    @Nullable @BindView(R.id.switchLockScreen) Switch switchLockScreen;
    @Nullable @BindView(R.id.rlLockScreenChangePin) RelativeLayout rlLockScreenChangePin;
    @Nullable @BindView(R.id.rlAllowBriefExitEntry) RelativeLayout rlAllowBriefExitEntry;
    @Nullable @BindView(R.id.switchAllowBriefExitEntry) Switch switchAllowBriefExitEntry;
    @Nullable @BindView(R.id.rlBriefExitEntryTime) RelativeLayout rlBriefExitEntryTime;
    @Nullable @BindView(R.id.tvBriefExitEntryTime) TextView tvBriefExitEntryTime;

    // Account
    @BindView(R.id.rlPublicKey) RelativeLayout rlPublicKey;
    @BindView(R.id.switchPublicKey) Switch switchPublicKey;

    // Appearance
    @BindView(R.id.rlChatBackground) RelativeLayout rlChatBackground;
    @BindView(R.id.tvChatBackground) TextView tvChatBackground;
    @BindView(R.id.rlTheme) RelativeLayout rlTheme;
    @BindView(R.id.tvTheme) TextView tvTheme;
    @BindView(R.id.rlTextSize) RelativeLayout rlTextSize;
    @BindView(R.id.tvTextSize) TextView tvTextSize;

    // Language
    @BindView(R.id.rlSetLanguage) RelativeLayout rlSetLanguage;
    @BindView(R.id.tvSetLanguageCurrent) TextView tvSetLanguageCurrent;

    // Help
    @BindView(R.id.rlReportBug) RelativeLayout rlReportBug;
    @BindView(R.id.rlContactSupport) RelativeLayout rlContactSupport;

    // About
    @BindView(R.id.rlAboutVersion) RelativeLayout rlAboutVersion;
    @BindView(R.id.rlAboutPolicy) RelativeLayout rlAboutPolicy;


    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, v);
        tvChatNotifications.setSelected(true);
        tvGroupNotifications.setSelected(true);
        tvSelectNotificationsSound.setSelected(true);

        v.findViewById(R.id.tv_last_seen_title).setSelected(true);
        setSelectedAllTextViews();

        return v;
    }

    private void setSelectedAllTextViews() {
        for(int i = 0; i < rootLayout.getChildCount(); ++i){
            View child = rootLayout.getChildAt(i);
            if(child instanceof LinearLayout){
                LinearLayout blockLayout = (LinearLayout) child;
                for(int j = 0; j < blockLayout.getChildCount(); ++j){
                    View blockChild = blockLayout.getChildAt(j);
                    if(blockChild instanceof RelativeLayout){
                        RelativeLayout paramLayout = (RelativeLayout) blockChild;
                        Log.d("SETTINGS", "Processing relativeLayout: " + ((TextView)paramLayout.getChildAt(0)).getText());
                        for(int k = 0; k < paramLayout.getChildCount(); ++k){
                            View paramChild = paramLayout.getChildAt(k);
                            if(paramChild instanceof TextView){
                                paramChild.setSelected(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @OnClick(R.id.rlLastSeen) void rlLastSeen() {

    }

    @Override
    public void onResume() {
        super.onResume();
//    ActivityUtil.setTitle(getActivity(),getString(R.string.settings));
        if(getActivity() instanceof MainActivity){
            ((MainActivity)getActivity()).setToolbarTitle(R.string.settings_title);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Optional
    @OnClick(R.id.rlLockScreenChangePin) void changePin(){
        ((MainActivity)getActivity()).navigateFullscreen(MainActivity.FRAGMENT_CHANGE_PIN);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
