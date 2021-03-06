package com.example.polyfinder.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.polyfinder.R;
import com.example.polyfinder.Transmitter;

import com.example.polyfinder.TransmitterPhoto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class ProfilePhotoBottomFragment extends BottomSheetDialogFragment {

    private static final int GALLERY_PICK = 1;
    private View view;
    private ImageView mCloseFragment;
    private LinearLayout mTakePhoto;
    private LinearLayout mUploadPhoto;
    private LinearLayout mDeletePhoto;
    private TransmitterPhoto transmitterPhoto;

    private Context mContext;

    private DatabaseReference requestDatabase;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private String currentUserId;
    private DatabaseReference reference;
  
    private static final int CAMERA_REQUEST = 10;
    private static final int GALLERY_REQUEST = 20;

    private String request_image_url = "default";
    private String request_thumb_image_url = "default";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_photo_fragment, container,false);


        getFirebaseData();
        findViews();
        setOnClicks();

        return view;
    }

    private void getFirebaseData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        currentUserId = currentUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        requestDatabase = FirebaseDatabase.getInstance().getReference().child("Requests");

        storageReference = FirebaseStorage.getInstance().getReference();
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
                setPhotoFromPhone();
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


    private void setPhotoFromPhone() {

        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        System.out.println("ooooooooooooooooooooooooooooooo");
        startActivityForResult(Intent.createChooser(gallery, "SELECT IMAGE"), GALLERY_PICK);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (requestCode == GALLERY_PICK) {
            System.out.println("ERROR DOWNLOADING IMAGE");
            System.out.println("ooooooooooooooooooooooooooooooo");
            Uri imageUri = intent.getData();//READY TO CROP THE IMAGE

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(getContext(),this);
            System.out.println("ooooooooooooooooooooooooooooooo");
        }
        super.startActivityForResult(intent, requestCode, options);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_PICK) {
            System.out.println("ERROR DOWNLOADING IMAGE");
            System.out.println("ooooooooooooooooooooooooooooooo");
            Uri imageUri = data.getData();//READY TO CROP THE IMAGE

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(getContext(),this);
            System.out.println("ooooooooooooooooooooooooooooooo");
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            System.out.println("ooooooooooooooooooooooooooooooo");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                File thumb_file = new File(resultUri.getPath());

                String currentUid = currentUser.getUid();


                Bitmap thumb_bitmap = new Compressor(getContext())
                        .setMaxHeight(100)
                        .setMaxWidth(100)
                        .compressToBitmap(thumb_file);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                final StorageReference filepath = storageReference.child("Profile_Images").child(currentUid + ".jpg");
                final StorageReference thumb_filepath = storageReference.child("Profile_Images").child("thumbs").child(currentUid + ".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String download_link = uri.toString();
                                request_image_url = download_link;

                                Map updateMap = new HashMap<>();
                                updateMap.put("imageUrl", download_link);

                                reference.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //image_load_progress.dismiss();
                                            Toast.makeText(getContext(), "Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                                            transmitterPhoto.onImageURLSend(request_image_url);
                                        }
                                    }
                                });

                            }
                        });
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), (CharSequence) error, Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if(context instanceof TransmitterPhoto) {
            transmitterPhoto = (TransmitterPhoto) context;
        } else{
            throw new RuntimeException(context.toString()
                    + "must implement Transmitter");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        transmitterPhoto = null;
    }
}
