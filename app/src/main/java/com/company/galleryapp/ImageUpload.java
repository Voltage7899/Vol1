package com.company.galleryapp;

import com.google.firebase.database.Exclude;

public class ImageUpload {
    //Модель данных загружаемой в бд
    private String name;
    private String imageUri;
    private String key;

    public ImageUpload(){

    }

    public ImageUpload(String name, String imageUri) {
        this.name = name;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
