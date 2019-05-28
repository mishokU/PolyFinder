package com.example.polyfinder.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyfinder.Adapters.DialogsAdapter;
import com.example.polyfinder.Adapters.MainTypeRequestAdapter;
import com.example.polyfinder.Fragments.ProfilePhotoBottomFragment;
import com.example.polyfinder.Items.Requests;
import com.example.polyfinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static android.media.MediaRecorder.VideoSource.CAMERA;



public class ProfileActivity extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.recyclerview) public RecyclerView recyclerView;
    @BindView(R.id.user_name) public TextView userName;
    @BindView(R.id.telephone) public TextView userPhoneNumber;
    @BindView(R.id.profile_image) public CircleImageView profileImage;
    @BindView(R.id.change_photo) public de.hdodenhof.circleimageview.CircleImageView change_photo_button;

    private ArrayList<Requests> mRequestItems = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private DatabaseReference reference;
    private DatabaseReference requestDatabase;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);

        initFireBase();
        loadData();

        setOnClicks();
        setUpToolbar();
        setUpAdapter();

        loadRequests();
    }

    private void setOnClicks() {
        change_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfilePhotoBottomFragment profilePhotoBottomFragment = new ProfilePhotoBottomFragment();
                profilePhotoBottomFragment.show(getSupportFragmentManager(),"settings");
                //setPhotoFromPhone();
            }
        });
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //startActivityForResult(galleryIntent, GALLERY);
    }

    private void setPhotoFromPhone() {
        /*Intent photoPickerIntent = new Intent(ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);*/

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
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                File thumb_file = new File(resultUri.getPath());

                String currentUid = currentUser.getUid();

                Bitmap thumb_bitmap = new Compressor(this)
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

                                Map updateMap = new HashMap<>();
                                updateMap.put("imageUrl", download_link);

                                reference.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //image_load_progress.dismiss();
                                            Toast.makeText(ProfileActivity.this, "Successfully Uploaded!", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }
                        });
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(ProfileActivity.this, (CharSequence) error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initFireBase() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        currentUserId = currentUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        requestDatabase = FirebaseDatabase.getInstance().getReference().child("Requests");

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void loadData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String txt_name = dataSnapshot.child("username").getValue().toString();
                String txt_email = dataSnapshot.child("email").getValue().toString();
                String image = dataSnapshot.child("imageUrl").getValue().toString();
                if(dataSnapshot.hasChild("phone")){
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    userPhoneNumber.setText(phone);
                }

                userName.setText(txt_name);
                //email.setText(txt_email);
                Picasso.get().load(image).placeholder(R.mipmap.ic_launcher).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void loadRequests(){
        requestDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @com.google.firebase.database.annotations.Nullable String s) {
                Requests request = dataSnapshot.getValue(Requests.class);
                //System.out.println(dataSnapshot.getKey() + "REQUEST FROM DATABASE");
                if(request.getFrom().equals(currentUserId)) {
                    addItem(request);
                }
                mAdapter.notifyDataSetChanged();
                System.out.println(mRequestItems.size() + "SIZEEEE");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @com.google.firebase.database.annotations.Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addItem(Requests request){//(String type,String title,String description,String imageURL){

        int mType = 0;
        if(request.getType().equals("found")){
            mType = Requests.FOUND_ITEM;
        }else {
            mType = Requests.LOST_ITEM;
        }
        //
        mRequestItems.add(0, request);//new Requests(type, title,description,imageURL)
    }

    private void setUpAdapter() {
        mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mAdapter = new MainTypeRequestAdapter(mRequestItems);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        setUpTouchListener();
    }

    private void setUpTouchListener() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mRequestItems.remove(((MainTypeRequestAdapter)mAdapter).getRequestAt(viewHolder.getAdapterPosition()));
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("На главную ленту");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.profile_menu),menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                openSettings();
                return true;
            case R.id.log_out:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent activity = new Intent(this, SignInActivity.class);
        activity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(activity);
        finish();
        overridePendingTransition(0,0);
    }

    private void openSettings() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}