package com.example.polyfinder.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.polyfinder.R;
import com.example.polyfinder.Transmitter;


public class BottomLostRequest extends Fragment {

    private View view;
    private RelativeLayout mainRelative;
    private TextView focus;
    private Transmitter transmitter;
    private ImageView mCloseFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lost_fragment, container, false);

        findViews();
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

    private void findViews() {
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
