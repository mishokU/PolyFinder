package com.example.polyfinder.Items;

public class Requests {

    public static final int LOST_ITEM = 0;
    public static final int FOUND_ITEM = 1;

    private String type;
    private String title;
    private String description;
    private String image;
    private String thumb_image;
    private String from;
    private String category;
    private long time;

    public Requests(){}

    public Requests(String type, String title, String description, String image, String thumb_image, String from, String category, long time){
        this.type = type;
        this.title = title;
        this.description = description;
        this.image = image;
        this.thumb_image = thumb_image;
        this.from = from;
        this.category = category;
        this.time = time;

    }

    public Requests(String type, String title, String description, String image){
        this.type = type;
        this.title = title;
        this.description = description;
        this.image = image;

    }

    public String getType(){
        return type;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getImage(){
        return image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public long getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }

    public String getCategory() {
        return category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
