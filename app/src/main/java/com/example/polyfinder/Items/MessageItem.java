package com.example.polyfinder.Items;

public class MessageItem {

    public static final int YOUR_MESSAGE = 0;
    public static final int FRIEND_MESSAGE = 1;

    private String message;
    private String time;
    private String type;

    public MessageItem(String type,String message,String time){
        this.message = message;
        this.time = time;
        this.type = type;
    }

    public String getMessage(){
        return message;
    }

    public String getTime(){
        return time;
    }

    public String getType() {return  type;}
}
