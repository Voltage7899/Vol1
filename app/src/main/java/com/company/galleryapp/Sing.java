package com.company.galleryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class Sing extends AppCompatActivity {

    private Button loginButton;
    private EditText phone, pass;
    private TextView adminlink, clientlink;
    private String parentDataBaseName = "User";
    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing);
        database= FirebaseDatabase.getInstance().getReference();

        init();
        sing();
    }

    private void sing() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_field=phone.getText().toString();
                String pass_field = pass.getText().toString();

                if(TextUtils.isEmpty(phone_field)&&TextUtils.isEmpty(pass_field)){
                    Toast.makeText(Sing.this, "Введите все данные", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(parentDataBaseName=="User"){
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("User").child(phone_field).exists()){//Проверяем,есть ли пользователь с таким номером,введенным из поля телефона
                                    //если есть,то устанавливаем полученные данные в объект типа юзер
                                    User userCurrentData=snapshot.child("User").child(phone_field).getValue(User.class);
                                    //Потом устанавливаем данные в глобальную переменную пользователя
                                    CurrentUser.currentUser=userCurrentData;
                                    //Логи для просмотра,какие данные приходят
                                    Log.d(TAG,"Переменная имени  "+CurrentUser.currentUser.name);
                                    Log.d(TAG,"Переменная телефона "+CurrentUser.currentUser.phone);
                                    Log.d(TAG,"Переменная пароля "+CurrentUser.currentUser.pass);
                                    //Проверка на то,правильно ли введены поля
                                    if(userCurrentData.phone.equals(phone_field) && userCurrentData.pass.equals(pass_field)){
                                        //если все окей,то переносит на страницу для юзеров со списком товаров
                                        Toast.makeText(Sing.this, "Вы вошли как Юзер", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(Sing.this,ImageRecycler_User.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(Sing.this, "Wrong Data", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    if (parentDataBaseName=="Admin"){

                        if(TextUtils.isEmpty(phone_field)&&TextUtils.isEmpty(pass_field)){
                            Toast.makeText(Sing.this, "Введите все данные", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            database.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.child("Admin").child(phone_field).exists()){

                                        User userCurrentData=snapshot.child("Admin").child(phone_field).getValue(User.class);
                                        CurrentUser.currentUser=userCurrentData;

                                        Log.d(TAG,"Переменная имени  "+CurrentUser.currentUser.name);
                                        Log.d(TAG,"Переменная телефона "+CurrentUser.currentUser.phone);
                                        Log.d(TAG,"Переменная пароля "+CurrentUser.currentUser.pass);

                                        if(userCurrentData.phone.equals(phone_field) && userCurrentData.pass.equals(pass_field)){
                                            Toast.makeText(Sing.this, "Вы вошли как админ", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(Sing.this,ImageRecycler.class);
                                            startActivity(intent);

                                        }
                                        else {
                                            Toast.makeText(Sing.this, "Wrong Data", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                }
            }
        });
    }

    public void init() {
        loginButton = findViewById(R.id.button);
        phone = findViewById(R.id.Phone);
        pass = findViewById(R.id.Pass);

        adminlink = findViewById(R.id.adminlink);
        clientlink = findViewById(R.id.userlink);

        clientlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminlink.setVisibility(View.VISIBLE);
                clientlink.setVisibility(View.INVISIBLE);
                loginButton.setText("Вход");
                parentDataBaseName = "User";
            }
        });

        adminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminlink.setVisibility(View.INVISIBLE);
                clientlink.setVisibility(View.VISIBLE);
                loginButton.setText("Вход для админа");
                parentDataBaseName = "Admin";
            }
        });
    }
}