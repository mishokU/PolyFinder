package com.example.polyfinder;

public interface Transmitter {
    void onDataSend(String type,String category,String search);
    void OnCloseSend(Boolean isClose);
}
