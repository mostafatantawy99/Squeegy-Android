package com.squeegy.android.app;


import android.app.Application;

import com.squeegy.android.api.AppVolleyApiManager;

public class SqueegyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        //Volley init.
        AppVolleyApiManager.initVolley(getApplicationContext());

    }
}
