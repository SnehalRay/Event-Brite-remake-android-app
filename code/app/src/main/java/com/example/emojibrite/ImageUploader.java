package com.example.emojibrite;

import static androidx.test.InstrumentationRegistry.getContext;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

/**
 * Class for the ImageUploader object
 * This class is used to upload images to Firebase Storage
 */
public class ImageUploader {

    private final FirebaseStorage storage;
    private final String storagePath;
    private AlertDialog loadingDialog;

    public ImageUploader(Context context, String storagePath) {
        this.storage = FirebaseStorage.getInstance();
        this.storagePath = storagePath;
        createLoadingDialog(context);
    }

    private void createLoadingDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.loading); // Ensure you have a layout `loading.xml` with a ProgressBar
        builder.setCancelable(false);
        loadingDialog = builder.create();
    }

    private void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }


    public interface UploadCallback {
        void onUploadSuccess(Uri downloadUri);
        void onUploadFailure(Exception exception);
    }

    public void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            // Run on UI thread
            new Handler(Looper.getMainLooper()).post(() -> loadingDialog.dismiss());
        }
    }


    public void uploadImage(Uri imageUri, UploadCallback callback) {
        showLoadingDialog();  // Show dialog on UI thread

        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(storagePath + "/" + UUID.randomUUID().toString() + ".jpg");

        imageRef.putFile(imageUri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();  // Propagate error if task fails
            }
            return imageRef.getDownloadUrl();  // Continue with getting the download URL
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadedUri = task.getResult();
                callback.onUploadSuccess(downloadedUri);
            } else {
                callback.onUploadFailure(task.getException());
            }
            hideLoadingDialog();  // Dismiss dialog on UI thread
        });
    }


}

