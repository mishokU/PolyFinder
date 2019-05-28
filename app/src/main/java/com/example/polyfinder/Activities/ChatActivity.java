package com.example.polyfinder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.polyfinder.Adapters.ChatAdapter;
import com.example.polyfinder.Items.Messages;
import com.example.polyfinder.Items.Users;
import com.example.polyfinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.push_message) public ImageButton mPushMessage;
    @BindView(R.id.photo_push) public ImageButton mPhotoPush;
    @BindView(R.id.toolbar) public Toolbar mToolbar;
    @BindView(R.id.input_field) public EditText message_text;
    @BindView(R.id.profile_image) public ImageView profile_image;
    @BindView(R.id.user_name) public TextView user_name;

    @BindView(R.id.recyclerview) public RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Messages> mChatItemList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;

    private DatabaseReference rootRef;
    private FirebaseAuth auth;

    private String currentUser;
    private String currentUserName;
    private String currentUserImage;

    private String chatUser;
    private String chatImage;
    private String chatUsername;

    private static final int MAX_TEXT_LENGTH = 60;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);

        getFriedData();

        getOwnData();

        startChat();

        createList();
        setToolbar();
        setRecyclerViewAdapter();
        setOnActions();
    }

    private void getFriedData() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser().getUid();

        chatUser = getIntent().getStringExtra("user_id");
        chatUsername = getIntent().getStringExtra("user_name");
        chatImage = getIntent().getStringExtra("user_image");

        Picasso.get().load(chatImage).placeholder(R.drawable.image_placeholder).into(profile_image);
        user_name.setText(chatUsername);
    }

    private void getOwnData() {
        DatabaseReference ref = rootRef.child("Users").child(currentUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);

                currentUserName = user.getUsername();
                currentUserImage = user.getImageUrl();
                Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.image_placeholder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startChat() {

        rootRef.child("Chats").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(chatUser)){
                    Map chatAddMap = new HashMap();
                    //chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
                    chatAddMap.put("userId", currentUser);
                    chatAddMap.put("userName", currentUserName);
                    chatAddMap.put("userImage", currentUserImage);



                    Map chatMap = new HashMap();
                    //chatAddMap.put("seen", false);

                    chatMap.put("userImage", chatImage);
                    chatMap.put("userName", chatUsername);
                    chatMap.put("timestamp", ServerValue.TIMESTAMP);
                    chatMap.put("userId", chatUser);


                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chats/" + currentUser + "/" + chatUser, chatMap);
                    chatUserMap.put("Chats/" + chatUser + "/" + currentUser, chatAddMap);

                    rootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if(databaseError != null){
                                System.out.println("Error is " + databaseError.getMessage().toString());
                            }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createList() {
        DatabaseReference messageRef = rootRef.child("messages").child(currentUser).child(chatUser);

        Query messageQuery = messageRef;//.limitToLast(currentPage * TOTAL_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Messages message = dataSnapshot.getValue(Messages.class);
                //String key = dataSnapshot.getKey();
                if(message.getFrom().equals(currentUser)){
                    message.setType("your_message");
                }
                else{
                    message.setType("friend_message");
                }

                lastMessageUpdate(message.getMessage());

                mChatItemList.add(message);
                System.out.println(mChatItemList.size());
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mChatItemList.size() - 1);
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

    private void lastMessageUpdate(String message) {

        DatabaseReference ref = rootRef.child("Chats").child(currentUser).child(chatUser);

        Map changeMap = new HashMap<>();
        changeMap.put("lastMessage", message);

        ref.updateChildren(changeMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                //Danila's code
            }
        });
    }

    private void sendMessage() {

        String message = message_text.getText().toString();

        if(message.length() > MAX_TEXT_LENGTH){

            Toast.makeText(this, "Слишком длинное сообщение", Toast.LENGTH_SHORT).show();

        }else if(!TextUtils.isEmpty(message)){

            String current_user_ref = "messages/" + currentUser + "/" + chatUser;
            String chat_user_ref =  "messages/" + chatUser + "/" + currentUser;

            DatabaseReference user_message_push = rootRef.child("messages")
                    .child(currentUser).child(chatUser).push();

            String push_id = user_message_push.getKey();

            String time = getCurrentTime();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", time);
            messageMap.put("from", currentUser);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            rootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    if(databaseError != null){
                        System.out.println("Error is " + databaseError.getMessage().toString());
                    }
                }
            });

        }
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    private void setToolbar() {
      setSupportActionBar(mToolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //getSupportActionBar().setSubtitle("online");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatActivity.this, DialogsActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    private void setRecyclerViewAdapter() {
        mLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
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
                sendMessage();
                message_text.setText("");
            }
        });

        message_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(mChatItemList.size());
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



}
