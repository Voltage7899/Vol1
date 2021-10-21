package com.company.galleryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {



    private Button button_choose_image;
    private Button button_upload;
    private TextView show_show_all_images;
    private EditText enter_name_image;
    private ImageView image_load;
    private ProgressBar progressBar;

    private Uri imageUri;

    private StorageReference storage;
    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_choose_image=findViewById(R.id.choose_image);
        button_upload=findViewById(R.id.upload);
        show_show_all_images=findViewById(R.id.text_show_all_images);
        enter_name_image=findViewById(R.id.edit_image_name);
        image_load=findViewById(R.id.image_load);
        progressBar=findViewById(R.id.progress_bar);

        storage= FirebaseStorage.getInstance().getReference("upload");
        database= FirebaseDatabase.getInstance().getReference("upload");

        button_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri!=null){
                    storage.child(System.currentTimeMillis()+"."+getFileExtension(imageUri))
                            .putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler =new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            },5000);
                            Toast.makeText(MainActivity.this, "Загружено", Toast.LENGTH_SHORT).show();
//                            ImageUpload image=new ImageUpload(enter_name_image.getText().toString(),taskSnapshot.getUploadSessionUri().toString());
//                            String imageUploadId=database.push().getKey();
//                            database.child(imageUploadId).setValue(image);

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                            ImageUpload upload = new ImageUpload(enter_name_image.getText().toString().trim(),downloadUrl.toString());

                            String uploadId = database.push().getKey();
                            database.child(uploadId).setValue(upload);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MainActivity.this, "Упс", Toast.LENGTH_SHORT).show();

                        }

                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                            double progress=(100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int)progress);

                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Не загрузилось", Toast.LENGTH_SHORT).show();
                }


            }
        });
        show_show_all_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ImageRecycler.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data.getData()!=null){
            imageUri=data.getData();

            Picasso.get().load(imageUri).into(image_load);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimne=MimeTypeMap.getSingleton();
        return  mimne.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    

}