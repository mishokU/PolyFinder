package com.example.polyfinder.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Items.DialogItem;
import com.example.polyfinder.Holders.DialogItemHolder;
import com.example.polyfinder.R;

import java.util.List;

public class DialogsAdapter extends RecyclerView.Adapter<DialogItemHolder> {

    private List<DialogItem> mDialogItems;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    public DialogsAdapter(List<DialogItem> mDialogItems){
        this.mDialogItems = mDialogItems;
    }

    @NonNull
    @Override
    public DialogItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item, parent, false);
        return new DialogItemHolder(view,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogItemHolder holder, int position) {
        DialogItem dialogItem = mDialogItems.get(position);
        //(DialogItemHolder) holder.getImageView().setImageBitmap();
        holder.getMessageView().setText(dialogItem.getLastMessage());
        holder.getUserNameView().setText(dialogItem.getUserName());
    }

    @Override
    public int getItemCount() {
        return mDialogItems.size();
    }
}
