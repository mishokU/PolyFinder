package com.example.polyfinder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class SearchBottomFragment extends Fragment
        implements RadioGroup.OnCheckedChangeListener {

    private View mView;
    private EditText mSearchText;
    private RadioGroup mRequestGroup;
    private RadioGroup mCategotyGroup;
    private Button mFilterButton;
    private String mRequestType;
    private String mCategotyType;

    private Transmitter transmitter;

    public interface Transmitter {
        public void onDataSend(String type,String category,String search);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate((R.layout.search_fragment),container,false);

        findAllViews();
        setOnActions();

        return mView;
    }

    private void findAllViews() {

        mSearchText = mView.findViewById(R.id.search_text);
        mRequestGroup = mView.findViewById(R.id.request_group);
        mCategotyGroup = mView.findViewById(R.id.categoty_group);

        mFilterButton = mView.findViewById(R.id.do_filter);
    }

    private void setOnActions() {

        mRequestGroup.setOnCheckedChangeListener(this);
        mCategotyGroup.setOnCheckedChangeListener(this);

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFilter();
            }
        });
    }

    private void doFilter() {
        transmitter.onDataSend(mRequestType,mCategotyType,mSearchText.getText().toString());
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Transmitter) {
            transmitter = (Transmitter) context;
        } else{
            throw new RuntimeException(context.toString()
            + "must implement Transmitter");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        transmitter = null;
    }
}
