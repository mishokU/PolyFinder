package com.example.polyfinder;

public class RequestItem {

    public static final int LOST_ITEM = 0;
    public static final int FOUND_ITEM = 1;

    private int type;
    private String title;
    private String description;
    private String imageURL;

    public RequestItem(int type, String title, String description, String imageURL){
        this.type = type;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
    }

    public int getType(){
        return type;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getImageURL(){
        return imageURL;
    }
}
