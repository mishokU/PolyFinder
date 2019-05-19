package com.example.polyfinder.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Adapters.DialogsAdapter;
import com.example.polyfinder.Items.DialogItem;
import com.example.polyfinder.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.recyclerview) public RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<DialogItem> mDialogItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogs_activity);
        ButterKnife.bind(this);

        addItem("Misha", "Hi","fefwew");
        setToolbar();
        setDialogAdapter();
    }

    private void addItem(String user_name,String  message, String image_url){
        mDialogItems.add(new DialogItem(user_name,message,image_url));
    }

    private void setDialogAdapter() {
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mAdapter = new DialogsAdapter(mDialogItems);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ((DialogsAdapter) mAdapter).setOnItemClickListener(new DialogsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                createChat();
            }
        });
    }

    private void createChat() {
        startActivity(new Intent(this,ChatActivity.class));
        finish();
        overridePendingTransition(0,0);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back to main tape");
    }
}
