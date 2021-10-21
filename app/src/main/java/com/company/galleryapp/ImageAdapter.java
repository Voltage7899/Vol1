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

    private Context context;
    private List<ImageUpload> imageUploadList;
    private OnItemClickListener listener;

    public ImageAdapter(Context context,List<ImageUpload> imageUploads){
        this.context=context;
        this.imageUploadList=imageUploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_element,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageUpload imageUpload=imageUploadList.get(position);
        holder.name_image_field.setText(imageUpload.getName());
        Picasso.get().load(imageUpload.getImageUri()).into(holder.imageView_field);
    }

    @Override
    public int getItemCount() {
        return imageUploadList.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView name_image_field;
        public ImageView imageView_field;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            name_image_field=itemView.findViewById(R.id.name_element);
            imageView_field=itemView.findViewById(R.id.image_element);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
           if(listener!=null){
               int position=getAdapterPosition();
               if(position!=RecyclerView.NO_POSITION){
                   listener.onItemClick(position);
               }
           }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select");
            MenuItem delete=menu.add(Menu.NONE,1,1,"Delete");

            delete.setOnMenuItemClickListener(this);

        }

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
    public interface OnItemClickListener{
        void onItemClick(int position);

        void onDelete(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
