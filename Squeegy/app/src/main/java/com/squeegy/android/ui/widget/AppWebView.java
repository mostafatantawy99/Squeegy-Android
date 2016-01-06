package com.squeegy.android.ui.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class AppWebView extends WebView implements
		Callback {

	
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public AppWebView(Context context, AttributeSet attrs, int defStyleAttr,
					  int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public AppWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public AppWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AppWebView(Context context) {
		super(context);
		init();
	}

	@SuppressLint("JavascriptInterface")
	public void init(){
		setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		getSettings().setJavaScriptEnabled(true);
		getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
		getSettings().setSupportMultipleWindows(false);
		addJavascriptInterface(AppWebView.this, "appwebview");
		setWebViewClient(webViewClient);
		setWebChromeClient(chromeClient);
	}

	WebViewClient webViewClient = new WebViewClient() {

		@Override
		public void onFormResubmission(WebView view, Message dontResend,
				Message resend) {
			resend.sendToTarget();
		}

		@Override
		public void onReceivedSslError(WebView view,
				android.webkit.SslErrorHandler handler,
				android.net.http.SslError error) {
			handler.proceed();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {

			if (errorCode == -6 || errorCode == -2 || errorCode == -7) {
				// NETWORK ERROR
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url,
				android.graphics.Bitmap favicon) {
			// SHOW DIALOG
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// giving WebView Focus-up
			view.requestFocus(View.FOCUS_DOWN);
			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_UP:
						if (!v.hasFocus()) {
							v.requestFocus();
						}
						break;
					}
					return false;
				}
			});

			// Customizing JS for webpage
//			 view.loadUrl(MdotJSHelper.getScript(R.string.main_css,
//			 view.getContext()));

		};

	};

	WebChromeClient chromeClient = new WebChromeClient() {

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final android.webkit.JsResult result) {

			return true;
		};

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);


		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
//			if (newProgress < 70) {
//				 view.loadUrl(MdotJSHelper.getScript(R.string.main_css,
//				 view.getContext()));
//			}
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				Callback callback) {
			super.onGeolocationPermissionsShowPrompt(origin, callback);

		}

		public void onCloseWindow(WebView window) {
			super.onCloseWindow(window);
			window.goBack();
		}
	};

	@Override
	public void invoke(String arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

}