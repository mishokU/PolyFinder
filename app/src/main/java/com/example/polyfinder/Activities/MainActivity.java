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
import com.example.polyfinder.Items.RequestItem;
import com.example.polyfinder.R;
import com.example.polyfinder.Fragments.SearchBottomFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SearchBottomFragment.Transmitter, Filterable {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.fab) public FloatingActionButton fab;
    @BindView(R.id.request_sheet) public NestedScrollView mRequestSheet;
    @BindView(R.id.request_pager) public ViewPager mRequestViewPager;

    @BindView(R.id.recyclerview) public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<RequestItem> mRequestItemList = new ArrayList<>();

    private String mCategory;
    private String mType;
    private String mSearch;

    private BottomSheetBehavior mBottomSheetBehavior;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        createList();
        setRecyclerViewAdapter();
        setFragmentAdapters();
        setAllSheets();
        setUpViews();
        addItem("Found","Hi","Hi found", "dqwpokdqwkodpqkw");
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
        mRequestItemList.add(new RequestItem(1,"Hi","Hi found", "dqwpokdqwkodpqkw"));
        mRequestItemList.add(new RequestItem(0,"Hi","Hi found", "dqwpokdqwkodpqkw"));
        mRequestItemList.add(new RequestItem(0,"Hi","Hi found", "dqwpokdqwkodpqkw"));
    }


    public void addItem(String type,String title,String description,String imageURL){

        //Chose type
        int mType = 0;
        if(type.equals("Found")){
            mType = RequestItem.FOUND_ITEM;
        }else {
            mType = RequestItem.LOST_ITEM;
        }
        //
        mRequestItemList.add(0,new RequestItem(mType,title,description,imageURL));
    }

    private void setRecyclerViewAdapter() {
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        mAdapter = new MainTypeRequestAdapter(mRequestItemList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ((MainTypeRequestAdapter) mAdapter).setOnItemClickListener(new MainTypeRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,"Clicked: " + position,Toast.LENGTH_SHORT).show();
            }
        });

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
    public Filter getFilter() {
        return null;
    }
}
