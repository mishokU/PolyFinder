package com.example.polyfinder.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Holders.FoundItemHolder;
import com.example.polyfinder.Holders.LostItemHolder;
import com.example.polyfinder.R;
import com.example.polyfinder.Items.Requests;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainTypeRequestAdapter extends RecyclerView.Adapter {

    private ArrayList<Requests> mRequests;
    private OnItemClickListener mOnItemClickListener;
    View view;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public MainTypeRequestAdapter(ArrayList<Requests> requests){
        this.mRequests = requests;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view;
        switch (viewType){
            case Requests.FOUND_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.found_item, parent,false);
                System.out.println("LOOST");
                return new FoundItemHolder(view,mOnItemClickListener);
            case Requests.LOST_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lost_item, parent,false);
                return new LostItemHolder(view,mOnItemClickListener);
        }
        return null;
    }

    public RequestItem getRequestAt(int position){
        return mRequestItems.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Requests item = mRequests.get(position);
        if(item != null){
            switch (item.getType()){
                case "found":
                    ((FoundItemHolder)holder).getTitleView().setText(item.getTitle());
                    ((FoundItemHolder) holder).getDescriptionView().setText(item.getDescription());
                    Picasso.get().load(item.getImage()).placeholder(R.mipmap.request_default)
                            .into(((FoundItemHolder) holder).getImage());
                    break;
                case "lost":
                    ((LostItemHolder) holder).getTitleView().setText(item.getTitle());
                    ((LostItemHolder) holder).getDescriptionView().setText(item.getDescription());
                    Picasso.get().load(item.getImage()).placeholder(R.mipmap.request_default)
                            .into(((LostItemHolder) holder).getImage());
                    break;// Image
            }
        }
    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mRequests.get(position).getType()){
            case "lost":{
                System.out.println("ADDING LOST ITEM "+ mRequests.get(position).getDescription());
                return Requests.LOST_ITEM;}
            case "found":{
                System.out.println("ADDING FOUND ITEM "+ mRequests.get(position).getDescription());
                return Requests.FOUND_ITEM;}
            default:{
                System.out.println("NOT ADDING ITEM "+ mRequests.get(position).getDescription());

                return -1;}
        }
    }
}
