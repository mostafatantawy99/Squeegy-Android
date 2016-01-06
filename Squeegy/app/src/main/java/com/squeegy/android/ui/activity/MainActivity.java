package com.squeegy.android.ui.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squeegy.android.R;
import com.squeegy.android.ui.fragment.FaqFragment;
import com.squeegy.android.ui.fragment.HSWFragment;
import com.squeegy.android.ui.fragment.HomeFragment;
import com.squeegy.android.ui.fragment.PayDetailsFragment;
import com.squeegy.android.ui.fragment.PricingFragment;
import com.squeegy.android.ui.fragment.PrivacyPolicyFragment;
import com.squeegy.android.ui.fragment.ProfileFragment;
import com.squeegy.android.ui.fragment.SendFeedbackFragment;
import com.squeegy.android.ui.fragment.TermsFragment;
import com.squeegy.android.ui.fragment.VehicalInfoFragment;
import com.squeegy.android.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String TAG = "MainActivity";
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mFragmentManager = getSupportFragmentManager();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            displayScreenByName(AppConstants.NAV_ITEM_NEARBY);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_home){
            displayScreenByName(AppConstants.NAV_ITEM_NEARBY);
        }else if(id==R.id.nav_faq){
            displayScreenByName(AppConstants.NAV_ITEM_FAQ);
        }else if(id==R.id.nav_feedback){
            displayScreenByName(AppConstants.NAV_ITEM_FEEDBACK);
        }else if(id==R.id.nav_hsw){
            displayScreenByName(AppConstants.NAV_ITEM_HSW);
        }else if(id==R.id.nav_pricing){
            displayScreenByName(AppConstants.NAV_ITEM_PRICING);
        }else if(id==R.id.nav_privacy){
            displayScreenByName(AppConstants.NAV_ITEM_PP);
        }else if(id==R.id.nav_tc){
            displayScreenByName(AppConstants.NAV_ITEM_TC);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayScreenByName(String screenName) {

        // To avoid fragment stack overflow exception.
        mFragmentManager.popBackStack();
        Fragment screenFragment=null;

        if(screenName.equalsIgnoreCase(AppConstants.NAV_ITEM_NEARBY)){

            screenFragment= HomeFragment.instantiate(getApplicationContext(), HomeFragment.class.getName(), null);

        }else if(screenName.equalsIgnoreCase(AppConstants.NAV_ITEM_PROFILE)){

            screenFragment= ProfileFragment.instantiate(getApplicationContext(), ProfileFragment.class.getName(), null);

        }else if(screenName.equalsIgnoreCase(AppConstants.NAV_ITEM_VEHICAL_INFORMATION)){

            screenFragment= VehicalInfoFragment.instantiate(getApplicationContext(), VehicalInfoFragment.class.getName(), null);

        }else if(screenName.equalsIgnoreCase(AppConstants.NAV_ITEM_PAY_DETAILS)){

            screenFragment= PayDetailsFragment.instantiate(getApplicationContext(), PayDetailsFragment.class.getName(), null);

        }else if(screenName.equalsIgnoreCase(AppConstants.NAV_ITEM_HSW)){

            screenFragment= HSWFragment.instantiate(getApplicationContext(), HSWFragment.class.getName(), null);

        }else if(screenName.equalsIgnoreCase(AppConstants.NAV_ITEM_PRICING)){

            screenFragment= PricingFragment.instantiate(getApplicationContext(), PricingFragment.class.getName(), null);

        }else if(screenName.equalsIgnoreCase(AppConstants.NAV_ITEM_FAQ)){

            screenFragment= FaqFragment.instantiate(getApplicationContext(), FaqFragment.class.getName(), null);

        }else if(screenName.equalsIgnoreCase(AppConstants.NAV_ITEM_FEEDBACK)){

          //  screenFragment= SendFeedbackFragment.instantiate(getApplicationContext(), SendFeedbackFragment.class.getName(), null);

            sendMail();
            return;
        }else if(screenName.equalsIgnoreCase(AppConstants.NAV_ITEM_TC)){

            screenFragment= TermsFragment.instantiate(getApplicationContext(), TermsFragment.class.getName(), null);

        }else if(screenName.equalsIgnoreCase(AppConstants.NAV_ITEM_PP)){

            screenFragment= PrivacyPolicyFragment.instantiate(getApplicationContext(), PrivacyPolicyFragment.class.getName(), null);

        }

        if(screenFragment!=null){
            mFragmentManager.beginTransaction().replace(R.id.fragment_container, screenFragment,screenName).commitAllowingStateLoss();
        }
    }

    private void sendMail() {

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{AppConstants.FEEDBACK_MAIL_ID});
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Squeegy Android App Support");
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName();

        final String extraText = "*** Support Information ***\n" +
                "\nAndroid OS Version: " + android.os.Build.VERSION.RELEASE + " (" + android.os.Build.VERSION.CODENAME + ")" +
                "\nDevice: " + android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL ;
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "\n\n\n " + extraText);
        startActivity(Intent.createChooser(intent, "Send feedback..."));
    }
}

