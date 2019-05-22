package com.example.polyfinder.Items;

public class Messages {

    public static final int YOUR_MESSAGE = 0;
    public static final int FRIEND_MESSAGE = 1;

    private String message;
    private String time;
    private String type;
    public String from;

    public Messages(){}

    public Messages(String from, String message, String time){
        this.message = message;
        this.time = time;
        this.from = from;
    }

    public String getMessage(){
        return message;
    }

    public String getFrom() {
        return from;
    }

    public String getTime(){
        return time;
    }

    public String getType() {return  type;}

    public void setType(String type){
        this.type = type;
    }
}
