package com.example.polyfinder.Fragments;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.polyfinder.R;
import com.example.polyfinder.Transmitter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;


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
    private String mCategoryType;

    private boolean isOpen = true;

    private DatabaseReference newRequestRef;
    private FirebaseAuth auth;
    private String currentUser;

    private String request_id;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lost_fragment, container, false);

        auth = FirebaseAuth.getInstance();

        currentUser = auth.getCurrentUser().getUid();

        newRequestRef = FirebaseDatabase.getInstance().getReference().child("Requests");

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
        
        mPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishRequest();
            }
        });
    }

    private void publishRequest() {
        //final Intent intent = new Intent();
        DatabaseReference user_message_push = newRequestRef.push();


        request_id = user_message_push.getKey();

        String type = "lost";

        String title_txt = mTitle.getText().toString();
        String description_txt = mDescription.getText().toString();
        String request_image_url = "default";
        String request_thumb_image_url = "default";

        if(TextUtils.isEmpty(title_txt)||TextUtils.isEmpty(mCategoryType)||TextUtils.isEmpty(description_txt)) {
            Toast.makeText(getContext(), "Заполните Все Поля!", Toast.LENGTH_SHORT).show();
            System.out.println("TITLE "+ title_txt + " CATEGORY " + mCategoryType + "descript "+ description_txt);
        } else {

            Map requestMap = new HashMap();
            requestMap.put("title", title_txt);
            requestMap.put("category", mCategoryType);
            requestMap.put("description", description_txt);
            requestMap.put("time", ServerValue.TIMESTAMP);
            requestMap.put("from", currentUser);
            requestMap.put("type", type);
            requestMap.put("image", request_image_url);
            requestMap.put("thumb_image", request_thumb_image_url);

            newRequestRef.child(request_id).setValue(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        /*intent.putExtra("title",title.getText().toString());
                        intent.putExtra("category",spinner.getSelectedItem().toString());
                        intent.putExtra("description",description.getText().toString());
                        intent.putExtra("fragment",switchButton);
                        intent.putExtra("image", request_image_url);
                        intent.putExtra("thumb_image", request_thumb_image_url);*/

                        //returnToMainActivity();
                        transmitter.OnCloseSend(true);

                    }
                }
            });
        }
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
    
    
}
