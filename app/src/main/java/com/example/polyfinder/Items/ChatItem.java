package com.example.polyfinder.Items;

public class ChatItem {

    public static final int MY_MESSAGE_ITEM = 0;
    public static final int FRIEND_MESSAGE_ITEM = 1;

    private int type;
    private String title;
    private String time;
    private String imageURL;

    public ChatItem(int type, String title,String time, String imageURL){
        this.type = type;
        this.title = title;
        this.time = time;
        this.imageURL = imageURL;
    }

    public int getType(){
        return type;
    }

    public String getTitle(){
        return title;
    }

    public String getTime(){
        return time;
    }

    public String getImageURL(){
        return imageURL;
    }
}
