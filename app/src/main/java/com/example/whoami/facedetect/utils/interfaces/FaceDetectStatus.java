package com.example.whoami.facedetect.utils.interfaces;

import com.example.whoami.facedetect.utils.models.RectModel;

public interface FaceDetectStatus {
    void onFaceLocated(RectModel rectModel);
    void onFaceNotLocated() ;
}
