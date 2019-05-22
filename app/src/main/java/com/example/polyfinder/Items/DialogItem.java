package com.example.polyfinder.Items;

public class DialogItem {

    private String userName;
    private String lastMessage;
    private String userImage;
    private String userId;

    public DialogItem(){}

    public DialogItem(String userName,String lastMessage, String userImage, String userId){
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.userImage = userImage;
        this.userId = userId;
    }

    public String getUserName(){
        return userName;
    }

    public String getLastMessage(){
        return lastMessage;
    }

    public String getUserImage(){
        return userImage;
    }

    public String getUserId() {
        return userId;
    }
}
