package com.example.whoami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.whoami.facedetect.FaceDetectActivity;

public class ChoicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices);
        final Button button = findViewById(R.id.option1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intentMain = new Intent(ChoicesActivity.this ,
                        GeneralActivity.class);
                ChoicesActivity.this.startActivity(intentMain);
                Log.i("Content "," General layout ");
            }
        });
        final Button button2 = findViewById(R.id.option2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intentMain = new Intent(ChoicesActivity.this ,
                        FaceDetectActivity.class);
                ChoicesActivity.this.startActivity(intentMain);
                Log.i("Content "," General layout ");
            }
        });
    }
}