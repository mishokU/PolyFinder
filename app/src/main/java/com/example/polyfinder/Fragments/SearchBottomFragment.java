package com.example.polyfinder.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.polyfinder.R;
import com.example.polyfinder.Transmitter;

import java.util.ArrayList;
import java.util.List;

public class SearchBottomFragment extends Fragment
        implements RadioGroup.OnCheckedChangeListener {

    private View mView;
    private EditText mSearchText;
    private RadioGroup mRequestGroup;
    private RadioGroup mCategotyGroup;
    private ImageView mCloseFragment;
    private Button mFilterButton;
    private String mRequestType ="";
    private String mCategoryType = "";

    private RadioButton mDocuments;
    private RadioButton mClothing;
    private RadioButton mOthers;
    private RadioButton mEat;
    private RadioButton mElectronics;
    private List<RadioButton> radioButtons;

    private Transmitter transmitter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate((R.layout.search_fragment),container,false);

        findAllViews();
        findRadioButtons();
        setOnActions();

        return mView;
    }

    private void findRadioButtons() {
        radioButtons = new ArrayList<>();

        radioButtons.add(mDocuments = mView.findViewById(R.id.documents));
        radioButtons.add(mOthers = mView.findViewById(R.id.others));
        radioButtons.add(mClothing = mView.findViewById(R.id.clothing));
        radioButtons.add(mEat = mView.findViewById(R.id.eat));
        radioButtons.add(mElectronics = mView.findViewById(R.id.electronics));
    }

    private void findAllViews() {
        mSearchText = mView.findViewById(R.id.search_text);
        mRequestGroup = mView.findViewById(R.id.request_group);
        mCategotyGroup = mView.findViewById(R.id.categoty_group);
        mCloseFragment = mView.findViewById(R.id.close_fragment);
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
        mCloseFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transmitter.OnCloseSend(true);
            }
        });
    }

    private void doFilter() {
        transmitter.onDataSend(mRequestType,mCategoryType,mSearchText.getText().toString());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if(group == mRequestGroup) {
            switch (checkedId) {
                case R.id.only_lost:
                    mRequestType = "lost";
                    break;
                case R.id.only_found:
                    mRequestType = "found";
                    break;
            }
        }

        if(group == mCategotyGroup){
            switch (checkedId) {
                case R.id.documents:
                    mCategoryType = "Documents";
                    break;
                case R.id.electronics:
                    mCategoryType = "Electronics";
                    break;
                case R.id.others:
                    mCategoryType = "Others";
                    break;
                case R.id.eat:
                    mCategoryType = "Eat";
                    break;
                case R.id.clothing:
                    mCategoryType = "Clothing";
                    break;
            }
        }
    }

    private void resetCategoryButtons() {
        for(RadioButton radioButton: radioButtons){
            if(radioButton.isChecked()){
                radioButton.setChecked(false);
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
