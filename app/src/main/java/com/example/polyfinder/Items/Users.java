package com.example.polyfinder.Items;

public class Users {

    String imageUrl;
    String status;
    String username;

    public Users(){

    }

    public Users(String username, String imageUrl, String status){
        this.username = username;
        this.status = status;
        this.imageUrl = imageUrl;

    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getUsername(){
        return username;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getStatus(){
        return status;
    }

}
