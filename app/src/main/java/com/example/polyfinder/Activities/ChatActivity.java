package com.example.polyfinder.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview) public RecyclerView mRecyclerView;
    @BindView(R.id.push_message) public ImageButton mPushMessage;
    @BindView(R.id.photo_push) public ImageButton mPhotoPush;
    @BindView(R.id.toolbar) public Toolbar mToolbar;

    private RecyclerView.LayoutManager mLayoutManager;
    //private ArrayList<ChatItem> mChatItemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);

        setToolbar();
        setRecyclerViewAdapter();
        setOnActios();
    }

    private void setToolbar() {
      setSupportActionBar(mToolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setTitle("Назад на Главную");
    }

    private void setRecyclerViewAdapter() {

    }

    private void setOnActios() {
        mPushMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessageToList();
            }
        });

        mPhotoPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosePhoto();
            }
        });
    }

    private void chosePhoto() {
    }

    private void addMessageToList() {
    }

}
