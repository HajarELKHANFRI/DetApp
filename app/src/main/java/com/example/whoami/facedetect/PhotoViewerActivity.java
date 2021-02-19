package com.example.whoami.facedetect;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.whoami.R;
import com.example.whoami.facedetect.utils.base.BaseActivity;
import com.example.whoami.facedetect.utils.base.PublicMethods;

import static com.example.whoami.facedetect.utils.base.Cons.IMG_EXTRA_KEY;
import static com.example.whoami.facedetect.utils.base.Cons.IMG_FILE;

public class PhotoViewerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        if (getIntent().hasExtra(IMG_EXTRA_KEY)) {
            ImageView imageView = findViewById(R.id.image);
            String imagePath = getIntent().getStringExtra(IMG_EXTRA_KEY);
            imageView.setImageBitmap(PublicMethods.getBitmapByPath(imagePath, IMG_FILE));
        }
    }
}
