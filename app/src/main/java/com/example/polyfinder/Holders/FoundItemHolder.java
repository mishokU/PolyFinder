package com.example.polyfinder.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Adapters.MainTypeRequestAdapter;
import com.example.polyfinder.R;

public class FoundItemHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView description;
    private ImageView image;
    public ImageView right_trash_can;
    public RelativeLayout foreground;

    public FoundItemHolder(@NonNull View itemView, final MainTypeRequestAdapter.OnItemClickListener listener) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);
        image = itemView.findViewById(R.id.found_image);
        right_trash_can = itemView.findViewById(R.id.right_trash_can);
        foreground = itemView.findViewById(R.id.foreground);

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

    public TextView getTitleView(){
        return title;
    }

    public TextView getDescriptionView(){
        return description;
    }

    public ImageView getImage() { return image;
    }
}
