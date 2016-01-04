package com.squeegy.android.ui.activity;

import android.os.Bundle;

import com.squeegy.android.base.BaseActivity;


public class SplashActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionBar();
    }

    private void hideActionBar() {

        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
    }
}
