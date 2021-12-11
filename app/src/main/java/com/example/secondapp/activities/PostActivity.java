package com.example.secondapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondapp.R;
import com.example.secondapp.models.Post;
import com.example.secondapp.providers.AuthProviders;
import com.example.secondapp.providers.ImageProvider;
import com.example.secondapp.providers.PostProvider;
import com.example.secondapp.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class PostActivity extends AppCompatActivity {

    ImageView pImageViewPost1;
    File pImageFile;
    Button pBtnPublicar;
    ImageProvider pImageProvider;
    PostProvider pPostProvider;
    AuthProviders pAuthProviders;

    private final int gallery_REQUEST_CODE = 1;


    TextInputEditText pITextTitle;
    TextInputEditText pITextDescription;
    TextView pTextCategory;

    ImageView pImgC1;
    ImageView pImgC2;
    ImageView pImgC3;
    ImageView pImgC4;

    String pCategoryString;
    String pTitle;
    String pDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        pImageViewPost1 = findViewById(R.id.imageviewPost1);
        pBtnPublicar = findViewById(R.id.btnPublicar);
        pImageProvider = new ImageProvider();
        pPostProvider =  new PostProvider();
        pAuthProviders = new AuthProviders();


        pITextTitle=findViewById(R.id.post_title);
        pITextDescription=findViewById(R.id.post_description);
        pTextCategory=findViewById(R.id.textViewCategory);

        pImgC1=findViewById(R.id.imageViewC1);
        pImgC2=findViewById(R.id.imageViewC2);
        pImgC3=findViewById(R.id.imageViewC3);
        pImgC4=findViewById(R.id.imageViewC4);

        pCategoryString="";

        pBtnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPost();
            }
        });

        pImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });



        //Eventos de ImageViews
        pImgC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pCategoryString = "C1";
                pTextCategory.setText(pCategoryString);
            }
        });
        pImgC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pCategoryString = "C2";
                pTextCategory.setText(pCategoryString);
            }
        });
        pImgC3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pCategoryString = "C3";
                pTextCategory.setText(pCategoryString);
            }
        });
        pImgC4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pCategoryString = "C4";
                pTextCategory.setText(pCategoryString);
            }
        });


    } // end onCreate

    private void clickPost() {
        pTitle = pITextTitle.getText().toString();
        pDescription  = pITextDescription.getText().toString();
        String category = pTextCategory.getText().toString();

        if(!pTitle.isEmpty() && !pDescription.isEmpty() && !pCategoryString.isEmpty()){
                if(pImageFile !=null){
                    saveImage();
                }else{
                    Toast.makeText(PostActivity.this, "Seleccioné una imagen", Toast.LENGTH_SHORT).show();
                }
        }else{
            Toast.makeText(PostActivity.this, "No puedes dejar campos en blanco", Toast.LENGTH_SHORT).show();
        }

    }

    //Uso de image Provider
    private void saveImage() {
        pImageProvider.save(PostActivity.this,pImageFile)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            pImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Post post =  new Post();
                                    post.setImage1(url);
                                    post.setCategory(pCategoryString);
                                    post.setTitle(pTitle);
                                    post.setDescription(pDescription);
                                    post.setIdUser(pAuthProviders.getUid());

                                    pPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> tasksave) {
                                            if(tasksave.isSuccessful()){
                                                Toast.makeText(PostActivity.this, "La informacion de la imagen se alamacenó correctamente", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(PostActivity.this, "La informacion de la imagen no se pudo almacenar", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                }
                            });
                            Toast.makeText(PostActivity.this, "La imagen se alamacenó correctamente", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(PostActivity.this, "Error, la imagen no se pudo alamacenar", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }



    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, gallery_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == gallery_REQUEST_CODE && resultCode == RESULT_OK){
            try {
                pImageFile = FileUtil.from(this, data.getData());
                pImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(pImageFile.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR", "onActivityResult: "+e.getMessage());
                Toast.makeText(this, "Se ha producido un error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}