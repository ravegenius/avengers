package com.jason.avengers.common;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jason on 2018/4/24.
 */

public class CameraUtils {

    private static final int REQUEST_CAMERA_SYSTEM_DATA = 201;
    private static final int REQUEST_CAMERA_SYSTEM_FILE = 202;

    public static void startSystem(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        activity.startActivityForResult(intent, REQUEST_CAMERA_SYSTEM_DATA);
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_CAMERA_SYSTEM_DATA:
                break;
            case REQUEST_CAMERA_SYSTEM_FILE:
                break;
        }
    }
}
