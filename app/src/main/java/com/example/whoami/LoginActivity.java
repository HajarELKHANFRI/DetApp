package com.example.whoami;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.EmptyStackException;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText email,pass;
    Button signIn;
    TextView registerText,resetPass;
    ProgressBar progress;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        progress = findViewById(R.id.progress);
        signIn = findViewById(R.id.buttonLogin);
        registerText = findViewById(R.id.signUpText);
        resetPass = findViewById(R.id.resetPassText);

        fAuth = FirebaseAuth.getInstance();

        registerText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //reset password
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,password;
                username = email.getText().toString().trim();
                password = pass.getText().toString().trim();

                if(username.isEmpty()){
                    email.setError("Email is required!");
                    email.requestFocus();
                    return;
                }

                if(password.isEmpty()){
                    pass.setError("Password is required!");
                    pass.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                    email.setError("Invalid Email Address!");
                    email.requestFocus();
                    return;
                }

                progress.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Logged In!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),amgDetector.class);
                            startActivity(intent);
                            finish();
                        }else{
                            progress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Failed to login! Please check your credentials.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }




}