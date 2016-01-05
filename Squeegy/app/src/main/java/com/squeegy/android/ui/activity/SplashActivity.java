package com.squeegy.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import com.squeegy.android.R;
import com.squeegy.android.base.BaseActivity;
import com.squeegy.android.util.AppConstants;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(isNetworkAvailable() && isLocationEnabled()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    SplashActivity.this.finish();
                }
            }, AppConstants.SPLASH_DELAY);
        }else if(!isLocationEnabled()){

           displayLocationEnable();

            return;
        } else if(!isNetworkAvailable()){
            displayInternetEnable();

            return;
        }




    }

    private void displayInternetEnable() {

        findViewById(R.id.cv_internet_fail).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_net_fail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.this.finish();
            }
        });
    }

    private void displayLocationEnable() {
        findViewById(R.id.cv_location_fail).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_location_fail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
                SplashActivity.this.finish();
            }
        });
    }


    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
}
