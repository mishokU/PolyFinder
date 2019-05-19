package com.example.polyfinder.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.polyfinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.register_email) public EditText email;
    @BindView(R.id.register_name) public EditText name;
    @BindView(R.id.register_password) public EditText password;
    @BindView(R.id.register_rewrite_password) public EditText rewrite_password;
    @BindView(R.id.register_button) public Button register_button;
    @BindView(R.id.sign_in_from_register) public TextView sign_in;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    private ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        setActions();

    }

    private void setActions() {

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_passwordApproval = rewrite_password.getText().toString();


                if(TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Заполните Все Поля!", Toast.LENGTH_SHORT).show();
                } else if(!txt_password.equals(txt_passwordApproval)){
                    Toast.makeText(RegisterActivity.this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                } else{
                    progress = new ProgressDialog(RegisterActivity.this);
                    progress.setTitle("Регистрируем Вас.");
                    progress.setMessage("Подождите, пока мы создаем Вам аккаунт :)");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                    register(txt_email, txt_name, txt_password);
                }
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

    }

    private void register(final String txt_email, final String txt_name, String txt_password) {

        mAuth.createUserWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String user_id = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("username", txt_name);
                            hashMap.put("email", txt_email);
                            hashMap.put("imageUrl", "default");


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progress.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        } else{
                            progress.hide();
                            Toast.makeText(RegisterActivity.this, "Невозможно зарегистрироваться с этим паролем и почтой.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
  
    private void launchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);
    }
}
