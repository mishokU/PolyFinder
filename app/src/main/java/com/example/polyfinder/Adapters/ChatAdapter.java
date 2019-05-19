package com.example.polyfinder.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Holders.FoundItemHolder;
import com.example.polyfinder.Holders.FriendMessageHolder;
import com.example.polyfinder.Holders.LostItemHolder;
import com.example.polyfinder.Holders.YourMessageHolder;
import com.example.polyfinder.Items.ChatItem;
import com.example.polyfinder.Items.MessageItem;
import com.example.polyfinder.Items.Requests;
import com.example.polyfinder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    private ArrayList<MessageItem> mMessages;
    private OnItemClickListener mOnItemClickListener;
    private View view;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public ChatAdapter(ArrayList<MessageItem> messages){
        this.mMessages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case MessageItem.FRIEND_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_message, parent, false);
                return new FriendMessageHolder(view,mOnItemClickListener);
            case MessageItem.YOUR_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_message, parent, false);
                return new YourMessageHolder(view,mOnItemClickListener);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageItem messageItem = mMessages.get(position);
        if(messageItem != null){
            switch (messageItem.getType()){
                case "your_message":
                    ((YourMessageHolder) holder).getMessageView().setText(messageItem.getMessage());
                    ((YourMessageHolder) holder).getTimeView().setText(messageItem.getTime());
                    break;
                case "friend_message":
                    ((FriendMessageHolder) holder).getMessageView().setText(messageItem.getMessage());
                    ((FriendMessageHolder) holder).getTimeView().setText(messageItem.getTime());
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mMessages.get(position).getType()){
            case "your_message":{
                return MessageItem.YOUR_MESSAGE;}
            case "friend_message":{
                return MessageItem.FRIEND_MESSAGE;}
            default:{
                return -1;}
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

}
