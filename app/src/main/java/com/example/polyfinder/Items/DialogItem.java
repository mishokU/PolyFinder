package com.example.polyfinder.Items;

public class DialogItem {

    private String userName;
    private String lastMessage;
    private String timestamp;
    private String userImage;
    private String userId;

    public DialogItem(){}

    public DialogItem(String userName,String lastMessage,String timestamp, String userImage, String userId){

        System.out.println("message: " + userName);
        System.out.println("message: " + lastMessage);
        System.out.println("message: " + timestamp);
        System.out.println("message: " + userImage);
        System.out.println("message: " + userId);

        this.userName = userName;
        this.lastMessage = lastMessage;
        this.userImage = userImage;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getUserName(){
        return userName;
    }

    public String getLastMessage(){
        return lastMessage;
    }

    public String getTime() { return timestamp; }

    public String getUserImage(){
        return userImage;
    }

    public String getUserId() {
        return userId;
    }
}
