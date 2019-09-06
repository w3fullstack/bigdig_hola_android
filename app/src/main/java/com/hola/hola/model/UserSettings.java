package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

public class UserSettings {
    @SerializedName("multi") public Multi multi;
    @SerializedName("account") public Account account;
    @SerializedName("language") public Language language;
    @SerializedName("mediaDoc") public MediaDoc mediaDoc;
    @SerializedName("mediaText") public MediaText mediaText;
    @SerializedName("mediaPhoto") public MediaPhoto mediaPhoto;
    @SerializedName("appearance") public Appearance appearance;
    @SerializedName("notifications") public Notifications notifications;

    public UserSettings(Multi multi, Account account, Language language, MediaDoc mediaDoc, MediaText mediaText, MediaPhoto mediaPhoto, Appearance appearance, Notifications notifications) {
        this.multi = multi;
        this.account = account;
        this.language = language;
        this.mediaDoc = mediaDoc;
        this.mediaText = mediaText;
        this.mediaPhoto = mediaPhoto;
        this.appearance = appearance;
        this.notifications = notifications;
    }



    public static class Multi{
        @SerializedName("qrScan") public Boolean qrScan;
        @SerializedName("retentionTime") public String retentionTime;

        public Multi(Boolean qrScan, String retentionTime) {
            this.qrScan = qrScan;
            this.retentionTime = retentionTime;
        }

        public Boolean getQrScan() {
            return qrScan;
        }

        public void setQrScan(Boolean qrScan) {
            this.qrScan = qrScan;
        }

        public String getRetentionTime() {
            return retentionTime;
        }

        public void setRetentionTime(String retentionTime) {
            this.retentionTime = retentionTime;
        }
    }

    public static class Account{
        @SerializedName("publicKey") public Boolean publicKey;

        public Account(Boolean publicKey) {
            this.publicKey = publicKey;
        }

        public Boolean getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(Boolean publicKey) {
            this.publicKey = publicKey;
        }
    }

    public static class Language{
        @SerializedName("language") public String language;

        public Language(String language) {
            this.language = language;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }

    public static class MediaDoc{
        @SerializedName("destroy") public String destroy;
        @SerializedName("autoSave") public Boolean autoSave;
        @SerializedName("autoDownload") public Boolean autoDownload;

        public MediaDoc(String destroy, Boolean autoSave, Boolean autoDownload) {
            this.destroy = destroy;
            this.autoSave = autoSave;
            this.autoDownload = autoDownload;
        }

        public String getDestroy() {
            return destroy;
        }

        public void setDestroy(String destroy) {
            this.destroy = destroy;
        }

        public Boolean getAutoSave() {
            return autoSave;
        }

        public void setAutoSave(Boolean autoSave) {
            this.autoSave = autoSave;
        }

        public Boolean getAutoDownload() {
            return autoDownload;
        }

        public void setAutoDownload(Boolean autoDownload) {
            this.autoDownload = autoDownload;
        }
    }

    public static class MediaText{
        @SerializedName("destroy") public String destroy;
        @SerializedName("autoSave") public Boolean autoSave;
        @SerializedName("autoDownload") public Boolean autoDownload;

        public MediaText(String destroy, Boolean autoSave, Boolean autoDownload) {
            this.destroy = destroy;
            this.autoSave = autoSave;
            this.autoDownload = autoDownload;
        }

        public String getDestroy() {
            return destroy;
        }

        public void setDestroy(String destroy) {
            this.destroy = destroy;
        }

        public Boolean getAutoSave() {
            return autoSave;
        }

        public void setAutoSave(Boolean autoSave) {
            this.autoSave = autoSave;
        }

        public Boolean getAutoDownload() {
            return autoDownload;
        }

        public void setAutoDownload(Boolean autoDownload) {
            this.autoDownload = autoDownload;
        }
    }

    public static class MediaPhoto{
        @SerializedName("destroy") public String destroy;
        @SerializedName("autoSave") public Boolean autoSave;
        @SerializedName("autoDownload") public Boolean autoDownload;

        public MediaPhoto(String destroy, Boolean autoSave, Boolean autoDownload) {
            this.destroy = destroy;
            this.autoSave = autoSave;
            this.autoDownload = autoDownload;
        }

        public String getDestroy() {
            return destroy;
        }

        public void setDestroy(String destroy) {
            this.destroy = destroy;
        }

        public Boolean getAutoSave() {
            return autoSave;
        }

        public void setAutoSave(Boolean autoSave) {
            this.autoSave = autoSave;
        }

        public Boolean getAutoDownload() {
            return autoDownload;
        }

        public void setAutoDownload(Boolean autoDownload) {
            this.autoDownload = autoDownload;
        }
    }

    public static class Appearance {
        @SerializedName("theme") public String theme;
        @SerializedName("chatBg") public String chatBackground;
        @SerializedName("textSize") public String textSize;

        public Appearance(String theme, String chatBackground, String textSize) {
            this.theme = theme;
            this.chatBackground = chatBackground;
            this.textSize = textSize;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getChatBackground() {
            return chatBackground;
        }

        public void setChatBackground(String chatBackground) {
            this.chatBackground = chatBackground;
        }

        public String getTextSize() {
            return textSize;
        }

        public void setTextSize(String textSize) {
            this.textSize = textSize;
        }
    }

    public static class Notifications {
        @SerializedName("desktop") public Boolean desktop;
        @SerializedName("previews") public Boolean previews;
        @SerializedName("chatNotifications") public String chatNotifications;
        @SerializedName("groupNotifications") public String groupNotifications;
        @SerializedName("notificationSounds") public String notificationSounds;

        public Notifications(Boolean desktop, Boolean previews, String chatNotifications, String groupNotifications, String notificationSounds) {
            this.desktop = desktop;
            this.previews = previews;
            this.chatNotifications = chatNotifications;
            this.groupNotifications = groupNotifications;
            this.notificationSounds = notificationSounds;
        }

        public Boolean getDesktop() {
            return desktop;
        }

        public void setDesktop(Boolean desktop) {
            this.desktop = desktop;
        }

        public Boolean getPreviews() {
            return previews;
        }

        public void setPreviews(Boolean previews) {
            this.previews = previews;
        }

        public String getChatNotifications() {
            return chatNotifications;
        }

        public void setChatNotifications(String chatNotifications) {
            this.chatNotifications = chatNotifications;
        }

        public String getGroupNotifications() {
            return groupNotifications;
        }

        public void setGroupNotifications(String groupNotifications) {
            this.groupNotifications = groupNotifications;
        }

        public String getNotificationSounds() {
            return notificationSounds;
        }

        public void setNotificationSounds(String notificationSounds) {
            this.notificationSounds = notificationSounds;
        }
    }


    public Multi getMulti() {
        return multi;
    }

    public void setMulti(Multi multi) {
        this.multi = multi;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public MediaDoc getMediaDoc() {
        return mediaDoc;
    }

    public void setMediaDoc(MediaDoc mediaDoc) {
        this.mediaDoc = mediaDoc;
    }

    public MediaText getMediaText() {
        return mediaText;
    }

    public void setMediaText(MediaText mediaText) {
        this.mediaText = mediaText;
    }

    public MediaPhoto getMediaPhoto() {
        return mediaPhoto;
    }

    public void setMediaPhoto(MediaPhoto mediaPhoto) {
        this.mediaPhoto = mediaPhoto;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }
}
