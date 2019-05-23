package com.example.polyfinder.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.polyfinder.Adapters.BottomFragmentsAdapter;
import com.example.polyfinder.Adapters.MainTypeRequestAdapter;
import com.example.polyfinder.Fragments.BottomFoundRequest;
import com.example.polyfinder.Fragments.BottomLostRequest;
import com.example.polyfinder.Fragments.BottomProfileRequestDialog;
import com.example.polyfinder.Items.Requests;
import com.example.polyfinder.R;
import com.example.polyfinder.Fragments.SearchBottomFragment;
import com.example.polyfinder.Transmitter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        Transmitter, Filterable {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.fab) public FloatingActionButton fab;
    @BindView(R.id.request_sheet) public NestedScrollView mRequestSheet;
    @BindView(R.id.request_pager) public ViewPager mRequestViewPager;

    @BindView(R.id.recyclerview) public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Requests> mRequestsList = new ArrayList<>();

    private String mCategory;
    private String mType;
    private String mSearch;

    private BottomSheetBehavior mBottomSheetBehavior;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private DatabaseReference requestDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        requestDatabase = FirebaseDatabase.getInstance().getReference().child("Requests");


        setRecyclerViewAdapter();
        setFragmentAdapters();
        setAllSheets();
        setUpViews();
        //createList();
        loadRequests();
        //addItem(new Requests("lost","Hi","Hi lmaaaaaan", "dqwpokdqwkodpqkw"));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if( currentUser == null){
            sendToStart();
        }
    }

    //CHECK IF USER IS AUTH
    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(startIntent);
        finish();
    }

    private void createList() {
        loadRequests();
       mRequestsList.add(new Requests("found","Hi","Hi found", "dqwpokdqwkodpqkw"));

    }

    private void loadRequests(){
        requestDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Requests request = dataSnapshot.getValue(Requests.class);
                System.out.println(dataSnapshot.getKey() + "REQUEST FROM DATABASE");

                    addItem(request);
                    mAdapter.notifyDataSetChanged();
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


    public void addItem(Requests request){//(String type,String title,String description,String imageURL){

        //Chose type
        int mType = 0;
        if(request.getType().equals("found")){
            mType = Requests.FOUND_ITEM;
        }else {
            mType = Requests.LOST_ITEM;
        }
        //
        mRequestsList.add(0, request);//new Requests(type, title,description,imageURL)
    }

    private void setRecyclerViewAdapter() {
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        mAdapter = new MainTypeRequestAdapter(mRequestsList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ((MainTypeRequestAdapter) mAdapter).setOnItemClickListener(new MainTypeRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                openFullRequest(mRequestsList.get(position));
                //Toast.makeText(MainActivity.this,"Clicked: " + position,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openFullRequest(Requests requests) {
        BottomProfileRequestDialog bottomProfileRequestDialog = new BottomProfileRequestDialog();
        Bundle bundle = new Bundle();

        bundle.putString("title", requests.getTitle());
        bundle.putString("description", requests.getDescription());
        bundle.putString("imageURL", requests.getImage());
        bundle.putString("user_id",requests.getFrom());

        bottomProfileRequestDialog.setArguments(bundle);
        bottomProfileRequestDialog.show(getSupportFragmentManager(),"request");
    }

    private void setFragmentAdapters() {
        BottomFragmentsAdapter mBottomFragmentsAdapter = new BottomFragmentsAdapter(getSupportFragmentManager());
        mBottomFragmentsAdapter.addFragment(new BottomFoundRequest());
        mBottomFragmentsAdapter.addFragment(new BottomLostRequest());
        mBottomFragmentsAdapter.addFragment(new SearchBottomFragment());
        mRequestViewPager.setAdapter(mBottomFragmentsAdapter);
    }

    private void setAllSheets() {
        mRequestSheet.setFillViewport (true);
        mBottomSheetBehavior = BottomSheetBehavior.from(mRequestSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_COLLAPSED) {
                    fab.show();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                closeKeyBoard();
            }
        });
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void setUpViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Доска Обьявлений");
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.main_menu),menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                showSearchFragment();
                return true;
            case R.id.message:
                launchActivity(DialogsActivity.class);
                return true;
            case R.id.profile:
                launchActivity(ProfileActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    private void showSearchFragment() {
        fab.hide();
        mRequestViewPager.setCurrentItem(2);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onClick(View v) {
        if(v == fab){
            fab.hide();
            openNewRequestFragment();
        }
    }

    private void openNewRequestFragment() {
        mRequestViewPager.setCurrentItem(0);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDataSend(String type, String category, String search) {
        mCategory = category;
        mType = type;
        mSearch = search;
    }

    @Override
    public void OnCloseSend(Boolean isClose) {
        if(isClose){
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
