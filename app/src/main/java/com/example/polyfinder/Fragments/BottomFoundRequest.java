package com.example.polyfinder.Fragments;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.polyfinder.R;
import com.example.polyfinder.Transmitter;


public class BottomFoundRequest extends Fragment{

    private View view;
    private EditText mTitle;
    private EditText mDescription;
    private Button mPublish;
    private ImageView mCloseFragment;
    private Transmitter transmitter;
    //private Type type;
    //private Image image;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.found_fragment, container, false);

        findAllViews();
        setOnClicks();

        return view;
    }

    private void setOnClicks() {
        mCloseFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transmitter.OnCloseSend(true);
            }
        });
    }

    private void findAllViews() {
        mTitle = view.findViewById(R.id.title);
        mDescription = view.findViewById(R.id.description);
        mPublish = view.findViewById(R.id.publish);
        mCloseFragment = view.findViewById(R.id.close_fragment);
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
