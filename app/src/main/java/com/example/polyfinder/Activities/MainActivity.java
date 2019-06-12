package com.example.polyfinder.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        Transmitter, Filterable {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.fab) public FloatingActionButton fab;
    @BindView(R.id.request_sheet) public NestedScrollView mRequestSheet;
    @BindView(R.id.request_pager) public ViewPager mRequestViewPager;
    @BindView(R.id.swipe_refresh) public SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.nested_scroll_view) public NestedScrollView mNestedScrollView;

    @BindView(R.id.recyclerview) public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Requests> mRequestsList = new ArrayList<>();
    private ArrayList<Requests> mFullRequests = new ArrayList<>();

    private BottomSheetBehavior mBottomSheetBehavior;
    private BottomProfileRequestDialog bottomProfileRequestDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private DatabaseReference requestDatabase;
    private String oldestKey ="";
    private String newKey ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        requestDatabase = FirebaseDatabase.getInstance().getReference().child("Requests");
        bottomProfileRequestDialog = new BottomProfileRequestDialog();

        setRecyclerViewAdapter();
        setFragmentAdapters();
        setAllSheets();
        setUpViews();
        seuUpSwipeRefresh();
        loadRequests();
        //reloadRequests();
    }

    private void reloadRequests() {
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    requestDatabase.orderByKey().endAt(oldestKey).limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                oldestKey = child.getKey();
                                Requests request = child.getValue(Requests.class);
                                addItem(request);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void seuUpSwipeRefresh() {
        mSwipeRefreshLayout.setColorSchemeColors(getColor(R.color.request_start));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        //reloadRequests();
                    }
                },2000);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
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

    private void loadRequests(){
        requestDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                oldestKey = dataSnapshot.getKey();
                Requests request = dataSnapshot.getValue(Requests.class);
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
        mFullRequests.add(0,request);
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
            }
        });

    }

    private void openFullRequest(Requests requests) {
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
        System.out.println("type: " + type + " " + category + " " + search);
        getFilter().filter(type + " " + category + " " + search);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void OnCloseSend(Boolean isClose) {
        if(isClose){
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //bottomProfileRequestDialog.dismiss();
        }
    }

    @Override
    public Filter getFilter() {
        return mainTapeFilter;
    }

    private Filter mainTapeFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Requests> filteredList = new ArrayList<>();
            String[] filterPattern = constraint.toString().split(" ");
            //If there are type category and search
            if(filterPattern.length == 3) {
                for (Requests request : mFullRequests) {
                    if (request.getType().equals(filterPattern[0]) && request.getCategory().equals(filterPattern[1]) &&
                            (request.getDescription().contains(filterPattern[2]) || request.getTitle().contains(filterPattern[2]))) {
                        filteredList.add(request);
                    }
                }
            } else if(filterPattern.length == 2) {
                System.out.println("filter 0: " + filterPattern[0]);
                System.out.println("filter 1: " + filterPattern[1]);
                for (Requests request : mFullRequests) {
                    if (request.getType().equals(filterPattern[0]) && request.getCategory().equals(filterPattern[1])) {
                        filteredList.add(request);
                    }
                }
            } else if(filterPattern.length == 1) {
                for (Requests request : mFullRequests) {
                    if (request.getType().equals(filterPattern[0]) ||
                       (request.getCategory().equals(filterPattern[0]) ||
                       (request.getDescription().contains(filterPattern[0]) ||
                       (request.getTitle().contains(filterPattern[0]))))) {
                        filteredList.add(request);
                    }
                }
            } else {
                filteredList.addAll(mFullRequests);
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mRequestsList.clear();
            mRequestsList.addAll((List)results.values);
            mAdapter.notifyDataSetChanged();
        }
    };
}
