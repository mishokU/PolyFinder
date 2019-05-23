package com.example.polyfinder.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.polyfinder.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ProfilePhotoBottomFragment extends BottomSheetDialogFragment {

    private View view;
    private ImageView mCloseFragment;
    private LinearLayout mTakePhoto;
    private LinearLayout mUploadPhoto;
    private LinearLayout mDeletePhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_photo_fragment, container,false);

        findViews();
        setOnClicks();

        return view;
    }

    private void setOnClicks() {
        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Take photo", Toast.LENGTH_SHORT).show();
            }
        });

        mUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Upload photo", Toast.LENGTH_SHORT).show();
            }
        });

        mDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Delete photo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findViews() {
        mTakePhoto = view.findViewById(R.id.take_photo);
        mUploadPhoto = view.findViewById(R.id.upload_photo);
        mDeletePhoto = view.findViewById(R.id.remove_photo);
    }
}
