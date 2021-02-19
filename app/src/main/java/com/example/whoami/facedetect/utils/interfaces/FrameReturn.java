package com.example.whoami.facedetect.utils.interfaces;

import android.graphics.Bitmap;

import com.example.whoami.facedetect.utils.common.FrameMetadata;
import com.example.whoami.facedetect.utils.common.GraphicOverlay;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;


public interface FrameReturn{
    void onFrame(
            Bitmap image ,
            FirebaseVisionFace face ,
            FrameMetadata frameMetadata,
            GraphicOverlay graphicOverlay
    );
}