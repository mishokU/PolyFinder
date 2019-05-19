package com.example.polyfinder.Items;

public class DialogItem {

    private String user_name;
    private String message;
    private String image_url;

    public DialogItem(String user_name,String message, String image_url){
        this.user_name = user_name;
        this.message = message;
        this.image_url = image_url;
    }

    public String getUserName(){
        return user_name;
    }

    public String getMessage(){
        return message;
    }

    public String getImageUrl(){
        return image_url;
    }
}
