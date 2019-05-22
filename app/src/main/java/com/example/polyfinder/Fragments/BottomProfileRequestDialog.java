package com.example.polyfinder.Fragments;

import android.content.Context;
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
import com.example.polyfinder.Transmitter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class BottomProfileRequestDialog extends BottomSheetDialogFragment {

    private View view;
    private Button communicate;
    private TextView title;
    private TextView description;
    private TextView user_name;
    private TextView user_phone;
    private ImageView user_photo;
    private ImageView request_image;
    private String user_id;
    private Transmitter transmitter;
    private ImageView mCloseFragment;

    private DatabaseReference reference;
    private FirebaseUser currentUser;
    private StorageReference storageReference;

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
            setUserID(bundle.getString("user_id"));

            setUserData();
        }
    }

    private void setUserData() {
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        storageReference = FirebaseStorage.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String txt_name = dataSnapshot.child("username").getValue().toString();
                String image = dataSnapshot.child("imageUrl").getValue().toString();
                if(dataSnapshot.hasChild("phone")){
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    setUserPhone(phone);
                }

                setUserName(txt_name);
                Picasso.get().load(image).placeholder(R.mipmap.ic_launcher).into(user_photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUserName(String name) {
        this.user_name.setText(name);
    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    public void setDescription(String description){
        this.description.setText(description);
    }

    public void setUserID(String user_id){
        this.user_id = user_id;
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
        user_photo = view.findViewById(R.id.user_photo);
        mCloseFragment = view.findViewById(R.id.close_fragment);
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
        mCloseFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transmitter.OnCloseSend(true);
            }
        });
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
