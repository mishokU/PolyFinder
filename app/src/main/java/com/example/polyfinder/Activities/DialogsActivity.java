package com.example.polyfinder.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Adapters.DialogsAdapter;
import com.example.polyfinder.Items.DialogItem;
import com.example.polyfinder.Items.Users;
import com.example.polyfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.recyclerview) public RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<DialogItem> mDialogItems = new ArrayList<>();

    private DatabaseReference rootRef;
    private FirebaseAuth auth;
    private String currentUser;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogs_activity);
        ButterKnife.bind(this);

        rootRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser().getUid();

        //addItem("Misha", "Hi","fefwew");
        setToolbar();
        setDialogAdapter();
        loadDialogs();
    }

    private void loadDialogs(){
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference();


        chatRef.child("Chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.hasChild(currentUser)){
                    final String chat_user_id = dataSnapshot.getRef().getKey();

                    chatRef.child("Chats").child(currentUser).child(chat_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            DialogItem chat = dataSnapshot.getValue(DialogItem.class);

                            String message = chat.getLastMessage();
                            String image = chat.getUserImage();
                            String name = chat.getUserName();
                            String userId = chat.getUserId();

                            mDialogItems.add(new DialogItem(name, message, image, userId));
                            mAdapter.notifyDataSetChanged();
                            System.out.println("LAST OOOOOOOOOO" + message + name + image + userId);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addItem(String user_name,String  message, String image_url, String user_id){
        mDialogItems.add(new DialogItem(user_name,message,image_url, user_id));
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
                createChat(mDialogItems.get(position));
            }
        });
    }

    private void createChat(DialogItem item) {
        Intent intent = new Intent(DialogsActivity.this, ChatActivity.class);
        intent.putExtra("user_id", item.getUserId());
        intent.putExtra("user_name", item.getUserName());
        intent.putExtra("user_image", item.getUserImage());

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back to main tape");
    }
}
