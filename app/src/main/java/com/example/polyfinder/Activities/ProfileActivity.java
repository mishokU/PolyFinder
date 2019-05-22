package com.example.polyfinder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Adapters.DialogsAdapter;
import com.example.polyfinder.Adapters.MainTypeRequestAdapter;
import com.example.polyfinder.Fragments.ProfilePhotoBottomFragment;
import com.example.polyfinder.Items.Requests;
import com.example.polyfinder.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.recyclerview) public RecyclerView recyclerView;
    @BindView(R.id.user_name) public TextView userName;
    @BindView(R.id.telephone) public TextView userPhoneNumber;
    @BindView(R.id.profile_image) public CircleImageView profileImage;
    @BindView(R.id.change_photo) public de.hdodenhof.circleimageview.CircleImageView change_photo_button;

    private ArrayList<Requests> mRequestItems = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private DatabaseReference reference;
    private FirebaseUser currentUser;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);

        initFireBase();
        loadData();

        setOnClicks();
        setUpToolbar();
        setUpAdapter();
    }

    private void setOnClicks() {
        change_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePhotoBottomFragment profilePhotoBottomFragment = new ProfilePhotoBottomFragment();
                profilePhotoBottomFragment.show(getSupportFragmentManager(),"settings");
            }
        });
    }

    private void initFireBase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String user_id = currentUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void loadData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String txt_name = dataSnapshot.child("username").getValue().toString();
                String txt_email = dataSnapshot.child("email").getValue().toString();
                String image = dataSnapshot.child("imageUrl").getValue().toString();
                if(dataSnapshot.hasChild("phone")){
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    userPhoneNumber.setText(phone);
                }

                userName.setText(txt_name);
                //email.setText(txt_email);
                Picasso.get().load(image).placeholder(R.mipmap.ic_launcher).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setUpAdapter() {
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mAdapter = new MainTypeRequestAdapter(mRequestItems);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        setUpTouchListener();
    }

    private void setUpTouchListener() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mRequestItems.remove(((MainTypeRequestAdapter)mAdapter).getRequestAt(viewHolder.getAdapterPosition()));
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back to main tape");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.profile_menu),menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                openSettings();
                return true;
            case R.id.log_out:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent activity = new Intent(this, SignInActivity.class);
        activity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(activity);
        finish();
        overridePendingTransition(0,0);
    }

    private void openSettings() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}