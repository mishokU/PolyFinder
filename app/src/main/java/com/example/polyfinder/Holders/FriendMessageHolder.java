package com.example.polyfinder.Holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Adapters.ChatAdapter;
import com.example.polyfinder.R;

public class FriendMessageHolder extends RecyclerView.ViewHolder {

    private TextView message;
    private TextView time;

    public FriendMessageHolder(@NonNull View itemView, final ChatAdapter.OnItemClickListener listener) {
        super(itemView);

        this.message = itemView.findViewById(R.id.friend_message);
        this.time = itemView.findViewById(R.id.time);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }

    public TextView getMessageView(){
        return message;
    }

    public TextView getTimeView(){
        return time;
    }
}
