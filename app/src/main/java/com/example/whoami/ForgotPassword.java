package com.example.whoami;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    TextInputEditText email;
    Button resetPass;
    ProgressBar progress;
    TextView logIn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.username);
        resetPass = findViewById(R.id.buttonReset);
        progress = findViewById(R.id.progress);
        logIn = findViewById(R.id.loginText);

        fAuth = FirebaseAuth.getInstance();

        logIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString().trim();

                if(emailStr.isEmpty()){
                    email.setError("Email is required!");
                    email.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
                    email.setError("Invalid Email Address!");
                    email.requestFocus();
                    return;
                }

                progress.setVisibility(View.VISIBLE);
                fAuth.sendPasswordResetEmail(emailStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progress.setVisibility(View.GONE);
                            Toast.makeText(ForgotPassword.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(ForgotPassword.this, "Try Again! Something wrong happened.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}