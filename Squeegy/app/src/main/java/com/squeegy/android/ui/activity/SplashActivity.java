package com.squeegy.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.squeegy.android.R;
import com.squeegy.android.base.BaseActivity;
import com.squeegy.android.util.AppConstants;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }, AppConstants.SPLASH_DELAY);

    }

//    private void hideActionBar() {
//
//        if(getSupportActionBar()!=null) {
//            getSupportActionBar().hide();
//        }
//    }
}
