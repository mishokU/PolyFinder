package com.example.polyfinder.Fragments;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.polyfinder.R;
import com.example.polyfinder.Transmitter;


public class BottomLostRequest extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private View view;
    private RelativeLayout mainRelative;
    private TextView focus;
    private EditText mTitle;
    private Button mPublish;
    private EditText mDescription;
    private Transmitter transmitter;
    private ImageView mCloseFragment;
    private Button mType;
    private RelativeLayout mTypePlace;
    private ConstraintLayout mTextPlace;
    private RadioGroup mRadioGroup;
    private String mCategotyType;

    private boolean isOpen = true;

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
        mType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTypeMenu();
            }
        });
    }

    private void findViews() {
        mCloseFragment = view.findViewById(R.id.close_fragment);
        mTitle = view.findViewById(R.id.title);
        mDescription = view.findViewById(R.id.description);
        mPublish = view.findViewById(R.id.publish);
        mCloseFragment = view.findViewById(R.id.close_fragment);
        mType = view.findViewById(R.id.type);
        mTextPlace = view.findViewById(R.id.text_place);
        mTypePlace = view.findViewById(R.id.type_place);
        mRadioGroup = view.findViewById(R.id.radio_group);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(group == mRadioGroup){
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
}
