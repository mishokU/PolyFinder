package com.example.polyfinder.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.polyfinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.sign_in_email) public EditText email;
    @BindView(R.id.sign_in_password) public EditText password;
    @BindView(R.id.create_account) public Button mCreateAccountButton;
    @BindView(R.id.forget_password) public Button mForgetPasswordButton;
    @BindView(R.id.sign_in_button) public Button mSignInButton;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    private ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

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
            String txt_login = email.getText().toString();
            String txt_password = password.getText().toString();
            if(TextUtils.isEmpty(txt_login)|| TextUtils.isEmpty(txt_password)){
                Toast.makeText(SignInActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else{
                progress = new ProgressDialog(SignInActivity.this);
                progress.setTitle("Авторизация...");
                progress.setMessage("Подождите, пока мы входим в Ваш аккаунт :)");
                progress.setCanceledOnTouchOutside(false);
                progress.show();
                signIn(txt_login, txt_password);
            }

        }
    }

    private void signIn(String txt_login, String txt_password) {

        mAuth.signInWithEmailAndPassword(txt_login, txt_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progress.hide();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        } else {
                            progress.hide();
                            Toast.makeText(SignInActivity.this, "Не удалось войти.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void launchActivity(Class activity) {
        Intent intent = new Intent(this,activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}
