package com.example.polyfinder.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Adapters.ChatAdapter;
import com.example.polyfinder.Adapters.MainTypeRequestAdapter;
import com.example.polyfinder.Items.ChatItem;
import com.example.polyfinder.Items.MessageItem;
import com.example.polyfinder.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.push_message) public ImageButton mPushMessage;
    @BindView(R.id.photo_push) public ImageButton mPhotoPush;
    @BindView(R.id.toolbar) public Toolbar mToolbar;

    @BindView(R.id.recyclerview) public RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MessageItem> mChatItemList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);

        createList();
        setToolbar();
        setRecyclerViewAdapter();
        setOnActions();
    }

    private void createList() {
        mChatItemList.add(new MessageItem("your_message","hi, my name is Misha","12:12"));
        mChatItemList.add(new MessageItem("friend_message","hi, Misha","12:13"));
        mChatItemList.add(new MessageItem("your_message","What is your Name?","12:14"));
        mChatItemList.add(new MessageItem("friend_message","My name is Anastasha","12:15"));
        mChatItemList.add(new MessageItem("your_message","hi, my name is Misha","12:12"));
        mChatItemList.add(new MessageItem("friend_message","hi, Misha","12:13"));
        mChatItemList.add(new MessageItem("your_message","What is your Name?","12:14"));
        mChatItemList.add(new MessageItem("friend_message","My name is Anastasha","12:15"));
        mChatItemList.add(new MessageItem("your_message","hi, my name is Misha","12:12"));
        mChatItemList.add(new MessageItem("friend_message","hi, Misha","12:13"));
        mChatItemList.add(new MessageItem("your_message","What is your Name?","12:14"));
        mChatItemList.add(new MessageItem("friend_message","My name is Anastasha","12:15"));
    }

    private void setToolbar() {
      setSupportActionBar(mToolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setTitle("Anastasya");
      getSupportActionBar().setSubtitle("online");
    }

    private void setRecyclerViewAdapter() {
        mLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        mAdapter = new ChatAdapter(mChatItemList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ((ChatAdapter) mAdapter).setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ChatActivity.this,"Clicked: " + position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.chat_menu),menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setOnActions() {
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
