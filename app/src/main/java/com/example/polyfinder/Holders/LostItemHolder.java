package com.example.polyfinder.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Adapters.MainTypeRequestAdapter;
import com.example.polyfinder.R;
import com.squareup.picasso.Target;

public class LostItemHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView description;
    private ImageView image;
    private ImageView user_image;
    public ImageView left_trash_can;
    public RelativeLayout foreground;

    public LostItemHolder(@NonNull View itemView, final MainTypeRequestAdapter.OnItemClickListener listener) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);
        image = itemView.findViewById(R.id.lost_image);
        left_trash_can = itemView.findViewById(R.id.left_trash_can);
        foreground = itemView.findViewById(R.id.foreground);
        user_image = itemView.findViewById(R.id.request_image_user_photo);

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

    public ImageView getMiniUserPhoto(){return user_image;}

    public TextView getTitleView(){
        return title;
    }

    public TextView getDescriptionView(){
        return description;
    }

    public ImageView getImage() { return image;
    }
}
