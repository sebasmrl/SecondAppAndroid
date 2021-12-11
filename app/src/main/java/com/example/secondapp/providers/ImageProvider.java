package com.example.secondapp.providers;

import android.content.Context;

import com.example.secondapp.utils.CompressorBitmapImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class ImageProvider {
    StorageReference imgStorage;

    public ImageProvider() {
        imgStorage = FirebaseStorage.getInstance().getReference();
    }


    public UploadTask save(Context context, File file){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(),500,500);
        StorageReference storage = imgStorage.child(new Date()+"jpg");
        UploadTask task = storage.putBytes(imageByte);
        imgStorage = storage;
        return task;
    }

    public StorageReference getStorage(){
        return imgStorage;
    }
}
