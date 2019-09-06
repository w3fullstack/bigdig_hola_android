package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

public class DocumentUploadResponse {
    String filename;
    boolean temp;
    @SerializedName("original_name")
    Object originalName;
    @SerializedName("download_url")
    String downloadUrl;

    public DocumentUploadResponse(String filename, boolean temp, Object originalName, String downloadUrl) {
        this.filename = filename;
        this.temp = temp;
        this.originalName = originalName;
        this.downloadUrl = downloadUrl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public Object getOriginalName() {
        return originalName;
    }

    public void setOriginalName(Object originalName) {
        this.originalName = originalName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
