package com.squeegy.android.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squeegy.android.R;
import com.squeegy.android.base.BaseFragment;
import com.squeegy.android.ui.widget.AppWebView;
import com.squeegy.android.util.AppConstants;

public class FaqFragment extends BaseFragment{

    private AppWebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.layout_webview,container,false);
        webView = (AppWebView) view.findViewById(R.id.app_web_view);
        webView.loadUrl(AppConstants.URL_FAQ);

        return view;
    }
}
