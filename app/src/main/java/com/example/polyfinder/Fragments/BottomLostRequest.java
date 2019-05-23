package com.example.polyfinder.Fragments;

import android.animation.Animator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class BottomLostRequest extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private static final int GALLERY_PICK = 1;
    private View view;
    private RelativeLayout mainRelative;
    private TextView focus;
    private EditText mTitle;
    private Button mPublish;
    private EditText mDescription;
    private ImageButton mRequestImage;
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
    private StorageReference storageReference;
    private String currentUser;

    private String request_id;
    private String request_image_url = "default";
    private String request_thumb_image_url = "default";

    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lost_fragment, container, false);

        auth = FirebaseAuth.getInstance();

        currentUser = auth.getCurrentUser().getUid();

        newRequestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        storageReference = FirebaseStorage.getInstance().getReference();

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

        mRequestImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhotoFromPhone();
            }
        });


      mRadioGroup.setOnCheckedChangeListener(this);
    }

    private void publishRequest() {

        //DatabaseReference user_message_push = newRequestRef.push();


        //request_id = user_message_push.getKey();

        String type = "lost";

        String title_txt = mTitle.getText().toString();
        String description_txt = mDescription.getText().toString();
        //request_image_url = "default";
        //request_thumb_image_url = "default";

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
        mRequestImage =view.findViewById(R.id.lost_request_image);

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
        mContext = context;
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
                    Toast.makeText(getActivity(),"Documents",Toast.LENGTH_SHORT).show();
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

    private void setPhotoFromPhone() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "SELECT IMAGE"), GALLERY_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            System.out.println("ERROR DOWNLOADING IMAGE");

            Uri imageUri = data.getData();//READY TO CROP THE IMAGE

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(mContext, this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                Uri resultUri = result.getUri();

                File thumb_file = new File(resultUri.getPath());

                Bitmap thumb_bitmap = new Compressor(mContext)
                        .setMaxHeight(100)
                        .setMaxWidth(100)
                        .compressToBitmap(thumb_file);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                DatabaseReference user_message_push = newRequestRef.child("requests").push();


                request_id = user_message_push.getKey();

                final StorageReference filepath = storageReference.child("Request_Images").child(request_id + ".jpg");
                final StorageReference thumb_filepath = storageReference.child("Request_Images").child("thumbs").child(request_id + ".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String download_link = uri.toString();
                                request_image_url = download_link;

                                UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        thumb_filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String thumb_download_url = uri.toString();
                                                request_thumb_image_url = thumb_download_url;

                                                //image_load_progress.dismiss();
                                                Toast.makeText(getActivity(), "Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                                                Picasso.get().load(request_image_url).placeholder(R.mipmap.ic_launcher).into(mRequestImage);

                                            }
                                        });
                                    }
                                });

                            }
                        });
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), (CharSequence) error, Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    
}
