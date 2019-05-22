package com.example.polyfinder.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Holders.FriendMessageHolder;
import com.example.polyfinder.Holders.YourMessageHolder;
import com.example.polyfinder.Items.Messages;
import com.example.polyfinder.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    private ArrayList<Messages> mMessages;
    private OnItemClickListener mOnItemClickListener;
    private View view;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public ChatAdapter(ArrayList<Messages> messages){
        this.mMessages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case Messages.FRIEND_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_message, parent, false);
                return new FriendMessageHolder(view,mOnItemClickListener);
            case Messages.YOUR_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_message, parent, false);
                return new YourMessageHolder(view,mOnItemClickListener);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages message = mMessages.get(position);
        if(message != null){
            switch (message.getType()){
                case "your_message":
                    ((YourMessageHolder) holder).getMessageView().setText(message.getMessage());
                    ((YourMessageHolder) holder).getTimeView().setText(message.getTime());
                    break;
                case "friend_message":
                    ((FriendMessageHolder) holder).getMessageView().setText(message.getMessage());
                    ((FriendMessageHolder) holder).getTimeView().setText(message.getTime());
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mMessages.get(position).getType()){
            case "your_message":{
                return Messages.YOUR_MESSAGE;}
            case "friend_message":{
                return Messages.FRIEND_MESSAGE;}
            default:{
                return -1;}
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

}
