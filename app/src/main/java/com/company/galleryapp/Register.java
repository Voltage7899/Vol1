package com.company.galleryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText name_reg,phone_reg,pass_reg;
    private Button register;
    //Переменная типа ссылки базы данных
    private DatabaseReference database;

    //Название создаваемой таблицы
    private String TABLE_NAME="User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Получаем ссылку на таблицу базы данных
        database= FirebaseDatabase.getInstance().getReference(TABLE_NAME);
        //Привязываем поля к переменным
        name_reg=findViewById(R.id.name_reg);
        phone_reg=findViewById(R.id.phone_reg);
        pass_reg=findViewById(R.id.pass_reg);
        //Привязываем кнопку
        register=findViewById(R.id.reg_reg);
        //Задаем слушателя
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //В переменные типа стринг пихаем введенные данные из полей
                String name = name_reg.getText().toString();
                String phone=phone_reg.getText().toString();
                String pass = pass_reg.getText().toString();

                //Создаем разметку данных через ХэшМэп
                HashMap<String,Object> userDataMap=new HashMap<>();
                userDataMap.put("name",name);
                userDataMap.put("phone",phone);
                userDataMap.put("pass",pass);

                //Устанавливаем слушателя на изменение данных в базе данных
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Как только мы считали данные с базы данных,нам приходят данные в качестве снэпшота
                        if(!(snapshot.child(phone).exists())){//Проверяем,есть ли введенный телефон уже в базе данных,если нет,то добавляем хэшмэп в бд

                            database.child(phone).updateChildren(userDataMap);
                            Toast.makeText(Register.this, "Вы зарегестрированы", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else{
                            Toast.makeText(Register.this, "Пользователь с такими данными уже есть", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}