package com.example.polyfinder.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Adapters.DialogsAdapter;
import com.example.polyfinder.R;

public class DialogItemHolder extends RecyclerView.ViewHolder {

    private de.hdodenhof.circleimageview.CircleImageView mImageView;
    private TextView mUserName;
    private TextView mMessage;

    public DialogItemHolder(@NonNull View itemView, final DialogsAdapter.OnItemClickListener listener) {
        super(itemView);

        mImageView = itemView.findViewById(R.id.user_photo);
        mUserName = itemView.findViewById(R.id.user_name);
        mMessage = itemView.findViewById(R.id.message);

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

    public ImageView getImageView(){
        return mImageView;
    }

    public TextView getUserNameView(){
        return mUserName;
    }

    public TextView getMessageView(){
        return mMessage;
    }
}
