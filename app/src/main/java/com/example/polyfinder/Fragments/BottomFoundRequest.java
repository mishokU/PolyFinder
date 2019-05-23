package com.example.polyfinder.Fragments;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.polyfinder.R;
import com.example.polyfinder.Transmitter;


public class BottomFoundRequest extends Fragment{

    private View view;
    private EditText mTitle;
    private EditText mDescription;
    private Button mPublish;
    private Button mType;
    private ImageView mCloseFragment;
    private Transmitter transmitter;
    private RelativeLayout mTypePlace;
    private androidx.constraintlayout.widget.ConstraintLayout mTextPlace;
    //private Type type;
    //private Image image;

    private boolean isOpen = true;

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
        mType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTypeMenu();
            }
        });
    }

    private void openTypeMenu() {
        if(isOpen) {
            int x = (int) mType.getX() + mType.getWidth() / 2;
            int y = (int) mType.getY() + mType.getHeight() / 2;

            int startRadius = 0;
            int endRadius = mTextPlace.getWidth();

            Animator anim = ViewAnimationUtils.createCircularReveal(mTypePlace, x, y, startRadius, endRadius);
            anim.setDuration(1000);
            mTypePlace.setVisibility(View.VISIBLE);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mTitle.setVisibility(View.INVISIBLE);
                    mDescription.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            anim.start();

            isOpen = false;
        } else {

            int x = (int) mType.getX() + mType.getWidth() / 2;
            int y = (int) mType.getY() + mType.getHeight() / 2;

            int startRadius = mTextPlace.getWidth();
            int endRadius = 0;

            Animator anim = ViewAnimationUtils.createCircularReveal(mTypePlace, x, y, startRadius, endRadius);
            anim.setDuration(1000);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mTitle.setVisibility(View.VISIBLE);
                    mDescription.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mTypePlace.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.start();
            isOpen = true;
        }
    }

    private void findAllViews() {
        mTitle = view.findViewById(R.id.title);
        mDescription = view.findViewById(R.id.description);
        mPublish = view.findViewById(R.id.publish);
        mCloseFragment = view.findViewById(R.id.close_fragment);
        mType = view.findViewById(R.id.type);
        mTextPlace = view.findViewById(R.id.text_place);
        mTypePlace = view.findViewById(R.id.type_place);
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
