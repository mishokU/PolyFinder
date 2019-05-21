package com.example.polyfinder.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.polyfinder.Activities.ChatActivity;
import com.example.polyfinder.Activities.MainActivity;
import com.example.polyfinder.Holders.FoundItemHolder;
import com.example.polyfinder.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

public class BottomProfileRequestDialog extends BottomSheetDialogFragment {

    private View view;
    private Button communicate;
    private TextView title;
    private TextView description;
    private TextView user_name;
    private TextView user_phone;
    private ImageView request_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        findAllViews();
        setOnClick();
        getDataFromView();

        return view;
    }

    private void getDataFromView() {
        Bundle bundle = getArguments();
        if(bundle != null){
            setTitle(bundle.getString("title"));
            setDescription(bundle.getString("description"));
            setRequestImageURL(bundle.getString("imageURL"));
        }
    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    public void setDescription(String description){
        this.description.setText(description);
    }

    public void setUserName(String name){
        this.user_name.setText(name);
    }

    public void setUserPhone(String phone){
        this.user_phone.setText(phone);
    }

    public void setRequestImageURL(String image){
        Picasso.get().load(image).placeholder(R.mipmap.request_default).into(request_image);
    }

    private void findAllViews() {
        communicate = view.findViewById(R.id.communicate_button);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        user_name = view.findViewById(R.id.user_name);
        user_phone = view.findViewById(R.id.user_phone);
        request_image = view.findViewById(R.id.request_photo);
    }

    private void setOnClick() {
        communicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
