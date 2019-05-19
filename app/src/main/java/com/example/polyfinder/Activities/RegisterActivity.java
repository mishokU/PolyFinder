package com.example.polyfinder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.polyfinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.email) public EditText mEmail;
    @BindView(R.id.password) public EditText mPassword;
    @BindView(R.id.rewrite_password) public EditText mRewritePassword;
    @BindView(R.id.user_name) public EditText mUserName;
    @BindView(R.id.enter_to_account) public TextView mEnterToAccountButton;
    @BindView(R.id.register_button) public Button mRegisterButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);

        setOnClicks();
    }

    private void setOnClicks() {
        mEnterToAccountButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mEnterToAccountButton){
            launchActivity(SignInActivity.class);
        }

        if(v == mRegisterButton){
            launchActivity(MainActivity.class);
        }
    }

    private void launchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);
    }
}
