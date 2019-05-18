package com.example.polyfinder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoundItem extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView description;
    private ImageView image;

    public FoundItem(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);
        image = itemView.findViewById(R.id.found_image);
    }

    public TextView getTitleView(){
        return title;
    }

    public TextView getDescriptionView(){
        return description;
    }
}
