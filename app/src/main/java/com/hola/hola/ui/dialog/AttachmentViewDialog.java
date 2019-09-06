package com.hola.hola.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.hola.hola.model.Document;
import com.hola.hola.util.files.FileUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttachmentViewDialog extends AlertDialog {
    private Document document;

    @BindView(R.id.tv_attachment_name)
    TextView attachmentName;
    @BindView(R.id.tv_sent_by)
    TextView sentBy;
    @BindView(R.id.iv_attachment_image)
    ImageView attachmentImage;

    @BindView(R.id.holder_attachment_image_placeholder)
    LinearLayout holderFileImagePlaceholder;
    @BindView(R.id.tv_file_attachment_name)
    TextView filePlaceholderAttachmentName;

    Activity owner;

    public AttachmentViewDialog(Document document, @NonNull Context context) {
        super(context);
        this.document = document;
        if(context instanceof Activity) owner = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog_attachment);
        ButterKnife.bind(this);

        attachmentName.setText("Attachment: " + document.getOriginalName());
        sentBy.setText("Sent by: " + document.getUser().getFullName());
        if (FileUtils.isViewableImage(document.getOriginalName())) {
            holderFileImagePlaceholder.setVisibility(View.GONE);
            attachmentImage.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(HolaRESTClient.getEndpoint() + "/document/get/" + document.getId())
                    .placeholder(R.drawable.ic_file_placeholder)
                    .into(attachmentImage);
        } else {
            attachmentImage.setVisibility(View.GONE);
            holderFileImagePlaceholder.setVisibility(View.VISIBLE);
            filePlaceholderAttachmentName.setText(document.getOriginalName());
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        owner = getOwnerActivity();
    }

    @OnClick(R.id.btn_download)
    void downloadAttachment() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            checkPermissions(new BasePermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    FileUtils.downloadFile(getContext(), HolaRESTClient.getDocumentUrl(document.getId()), document.getOriginalName(), "/Hola");
                }
            });
        } else {
            FileUtils.downloadFile(getContext(), HolaRESTClient.getDocumentUrl(document.getId()), document.getOriginalName(), "/Hola");
        }
    }

    @OnClick(R.id.btn_close)
    void close() {
        this.cancel();
    }

    private void checkPermissions(BasePermissionListener listener) {
        DialogOnDeniedPermissionListener dlgListener = DialogOnDeniedPermissionListener.Builder
                .withContext(getContext())
                .withTitle("Write storage permission")
                .withMessage("Write external storage permission is needed to download files")
                .withButtonText(android.R.string.ok)
                .withIcon(R.mipmap.ic_hola)
                .build();
        CompositePermissionListener composite = new CompositePermissionListener(listener, dlgListener);
        Dexter.withActivity(owner)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(composite)
                .onSameThread()
                .check();
    }
}
