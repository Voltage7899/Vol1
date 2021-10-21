package com.company.galleryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImageRecycler extends AppCompatActivity {
    //Переменные обновляемого списка и адаптера
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    //Ссылки на базу и хранилище
    private DatabaseReference database;
    private FirebaseStorage firebaseStorage;
    //Переменная списка и кнопки
    private List<ImageUpload> imageUploadList;
    private Button add_new;



    //Переменная загрузочного кружочка
    private ProgressBar progressBarCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_recycler);
//Привязка переменных
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBarCircle=findViewById(R.id.progressBarCircle);
        add_new=findViewById(R.id.Add_admin);
        //Установка слушателя на кнопку,чтобы перейти на добавление картинки
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ImageRecycler.this,MainActivity.class);
                startActivity(intent);
            }
        });
//Получение ссылок на бд и хранилище
        database= FirebaseDatabase.getInstance().getReference("upload");
        firebaseStorage=FirebaseStorage.getInstance();
//создание адаптера и более гибкого массива
        imageUploadList=new ArrayList<>();
        imageAdapter=new ImageAdapter(ImageRecycler.this,imageUploadList);
        //Установка адаптера
        recyclerView.setAdapter(imageAdapter);
        //Уустановка слушателей для на каждый элемент списка,которые мы прописали в адаптере
        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ImageRecycler.this, "NormalClick"+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(int position) {
                //Код удаления выбранного элемента из спискаЭ,а именно из бд и хранилища

                ImageUpload selectedItem=imageUploadList.get(position);
                String selectedKey=selectedItem.getKey();

                StorageReference imageRef=firebaseStorage.getReferenceFromUrl(selectedItem.getImageUri());
                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.child(selectedKey).removeValue();
                        Toast.makeText(ImageRecycler.this, "DeleteItem "+position, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        //Получение данных из базы данных и добавление в массив
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                imageUploadList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ImageUpload imageUpload=dataSnapshot.getValue(ImageUpload.class);
                    imageUpload.setKey(dataSnapshot.getKey());
                    imageUploadList.add(imageUpload);
                }
                //обноваление списка
                imageAdapter.notifyDataSetChanged();



//Скрытие прогресс кружочка
                progressBarCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Если что то пошла не так,то выведет сообщение
                Toast.makeText(ImageRecycler.this, "Упс", Toast.LENGTH_SHORT).show();
                progressBarCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
}