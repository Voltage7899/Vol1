package com.company.galleryapp;

import android.content.Context;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    //Адаптер для ресайлера

    //Переменные  для списка,контекста и слушателя
    private Context context;
    private List<ImageUpload> imageUploadList;
    private OnItemClickListener listener;

    //Конструктор
    public ImageAdapter(Context context,List<ImageUpload> imageUploads){
        this.context=context;
        this.imageUploadList=imageUploads;
    }
    //Установка образа для держателя элемента списка
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_element,parent,false);
        return new ImageViewHolder(view);
    }
    //установка данных в поля образа
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageUpload imageUpload=imageUploadList.get(position);
        holder.name_image_field.setText(imageUpload.getName());
        Picasso.get().load(imageUpload.getImageUri()).into(holder.imageView_field);
    }
    //Чисто для размера списка
    @Override
    public int getItemCount() {
        return imageUploadList.size();
    }

    //Класс обертка для элемента списка
    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        //Поля которые мы привяжем для элемента списка
        public TextView name_image_field;
        public ImageView imageView_field;
        //Констуктор в которым мы привязываем поля и устанавливаем слушатель
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            name_image_field=itemView.findViewById(R.id.name_element);
            imageView_field=itemView.findViewById(R.id.image_element);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }
        //Функция для того,чтобы слушатель наш работал при нажатие на элемент списка
        @Override
        public void onClick(View v) {
           if(listener!=null){
               int position=getAdapterPosition();
               if(position!=RecyclerView.NO_POSITION){
                   listener.onItemClick(position);
               }
           }
        }
        //Фукнция ддя создания меню при нажатии на элемент списка
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select");
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete");

            delete.setOnMenuItemClickListener(this);

        }
        //Функция для того,чтобы слушатель меню работал при нажатии на элемент списка
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listener!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            listener.onDelete(position);
                            return true;
                    }
                }

            }
            return false;
        }
    }
    //Наш интерфейс с переопределяемыми потом методами
    public interface OnItemClickListener{
        void onItemClick(int position);

        void onDelete(int position);
    }
    //Установщик слушателя
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
