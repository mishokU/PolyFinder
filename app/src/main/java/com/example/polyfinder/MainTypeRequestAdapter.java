package com.example.polyfinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainTypeRequestAdapter extends RecyclerView.Adapter {

    private ArrayList<RequestItem> mRequestItems;

    public MainTypeRequestAdapter(ArrayList<RequestItem> requestItems){
        this.mRequestItems = requestItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case RequestItem.FOUND_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.found_item, parent,false);
                return new FoundItem(view);
            case RequestItem.LOST_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lost_item, parent,false);
                return new LostItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RequestItem item = mRequestItems.get(position);
        if(item != null){
            switch (item.getType()){
                case RequestItem.FOUND_ITEM:
                    ((FoundItem)holder).getTitleView().setText(item.getTitle());
                    ((FoundItem) holder).getDescriptionView().setText(item.getDescription());
                    // Image
                    break;
                case RequestItem.LOST_ITEM:
                    ((LostItem) holder).getTitleView().setText(item.getTitle());
                    ((LostItem) holder).getDescriptionView().setText(item.getDescription());
                    // Image
            }
        }
    }

    @Override
    public int getItemCount() {
        return mRequestItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mRequestItems.get(position).getType()){
            case 0:
                return RequestItem.LOST_ITEM;
            case 1:
                return RequestItem.FOUND_ITEM;
            default:
                return -1;
        }
    }
}
