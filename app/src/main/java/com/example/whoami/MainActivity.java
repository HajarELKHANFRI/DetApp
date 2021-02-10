package com.example.whoami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;

    Animation rightAnim,leftAnim;
    ImageView icon,logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        rightAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        leftAnim = AnimationUtils.loadAnimation(this,R.anim.left_animation);

        icon = findViewById(R.id.icon);
        logo = findViewById(R.id.logo);

        icon.setAnimation(rightAnim);
        logo.setAnimation(leftAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(homeIntent);
                finish();

            }
        },SPLASH_TIME_OUT);
    }
}