package com.example.whoami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class amgDetector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amg_detector);
        final Button button = findViewById(R.id.option1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intentMain = new Intent(amgDetector.this ,
                        GeneralActivity.class);
                amgDetector.this.startActivity(intentMain);
                Log.i("Content "," General layout ");
            }
        });
    }
}