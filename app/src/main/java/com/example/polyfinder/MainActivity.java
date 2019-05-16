package com.example.polyfinder;

import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.fab) public FloatingActionButton fab;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpViews();
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
                launchActivity();
                return true;
            case R.id.profile:
                launchActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchActivity() {
    }

    private void showSearchFragment() {
    }

    @Override
    public void onClick(View v) {
        if(v == fab){
            openNewRequestFragment();
        }
    }

    private void openNewRequestFragment() {
        BottomFoundRequest foundRequest = new BottomFoundRequest();
        foundRequest.show(getSupportFragmentManager(), "foundFragment");
    }

}
