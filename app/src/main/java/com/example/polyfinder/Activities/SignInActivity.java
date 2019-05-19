package com.example.polyfinder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.polyfinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.email) public EditText mEmail;
    @BindView(R.id.password) public EditText mPassword;
    @BindView(R.id.create_account) public Button mCreateAccountButton;
    @BindView(R.id.forget_password) public Button mForgetPasswordButton;
    @BindView(R.id.sign_in_button) public Button mSignInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        ButterKnife.bind(this);

        setOnClicks();
    }

    private void setOnClicks() {
        mCreateAccountButton.setOnClickListener(this);
        mForgetPasswordButton.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mCreateAccountButton){
            launchActivity(RegisterActivity.class);
        }

        if(v == mForgetPasswordButton){
            //lanchActvitiy(ForgetPasswordActvity.class);
        }

        if(v == mSignInButton){
            launchActivity(MainActivity.class);
        }
    }

    private void launchActivity(Class activity) {
        Intent intent = new Intent(this,activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}
