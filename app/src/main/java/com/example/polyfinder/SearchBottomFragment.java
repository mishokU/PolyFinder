package com.example.polyfinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchBottomFragment extends Fragment
        implements RadioGroup.OnCheckedChangeListener {

    private View mView;
    private RadioGroup mRequestGroup;
    private RadioGroup mCategotyGroup;
    private Button mPublish;
    private String mRequestType;
    private String mCategotyType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate((R.layout.search_fragment),container,false);

        mRequestGroup = mView.findViewById(R.id.request_group);
        mCategotyGroup = mView.findViewById(R.id.categoty_group);
        mPublish = mView.findViewById(R.id.publish);

        mRequestGroup.setOnCheckedChangeListener(this);
        mCategotyGroup.setOnCheckedChangeListener(this);

        setOnActions();

        return mView;
    }

    private void setOnActions() {
        mPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToServer();
            }
        });
    }

    private void addToServer() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if(group == mRequestGroup) {
            switch (checkedId) {
                case R.id.only_lost:
                    mRequestType = "Only Lost";
                    break;
                case R.id.all:
                    mRequestType = "All";
                    break;
                case R.id.only_found:
                    mRequestType = "Only Found";
                    break;
            }
        }

        if(group == mCategotyGroup){
            switch (checkedId) {
                case R.id.documents:
                    mCategotyType = "Documents";
                    break;
                case R.id.electronics:
                    mCategotyType = "Electronics";
                    break;
                case R.id.others:
                    mCategotyType = "Others";
                    break;
                case R.id.eat:
                    mCategotyType = "Eat";
                    break;
                case R.id.clothing:
                    mCategotyType = "Clothing";
                    break;
            }
        }
    }

    public String getRequestType(){
        return mRequestType;
    }

    public String getCategotyType(){
        return mCategotyType;
    }
}
