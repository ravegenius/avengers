package com.jason.avengers.common.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jason on 2018/3/15.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // CameraUtils.onActivityResult(requestCode, resultCode, data);
    }
}
