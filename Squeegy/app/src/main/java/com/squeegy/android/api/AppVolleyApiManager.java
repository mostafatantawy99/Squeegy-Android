package com.squeegy.android.api;


import android.content.Context;

import com.squeegy.android.util.AppLog;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Base class for Volly init. and This calls
 *
 *
 */
public class AppVolleyApiManager {

	public static AppVolleyApiManager apiManager;
	private static RequestQueue requestQueue;
	private static ImageLoader imageLoader;
	private static ImageLoader.ImageCache imageCache = new BitmapLruCache();
	
	private boolean isLiveFeedReq;
	
	public synchronized static void initVolley(Context context) {
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(context);
			imageLoader=new ImageLoader(requestQueue, imageCache);
		}
	}
	
	public static AppVolleyApiManager instance(){
		if(apiManager==null){
			apiManager=new AppVolleyApiManager();
		}
		return apiManager;
	}

	public synchronized static RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public void clearCache() {
		if (requestQueue != null) {
			requestQueue.getCache().clear();
		}
	}

	public void clearCache(String key) {
		if (requestQueue != null) {
			requestQueue.getCache().remove(key);
		}
	}

	public void cancelAllRequests() {
		if (requestQueue != null) {
			requestQueue.cancelAll(new RequestQueue.RequestFilter() {
				@Override
				public boolean apply(Request<?> request) {
					return true;
				}
			});
		}
	}

	public void cancelRequest(String tag) {
		if (requestQueue != null) {
			requestQueue.cancelAll(tag);
		}
	}

	public void getStringResponse(Boolean isRefresh,Context context, String url,
			final OnNetWorkResponse netWorkResponse) {

		if(isRefresh){
			clearCache(url);
		}
		StringRequest stringRequest = new StringRequest(Method.GET,url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						netWorkResponse.onSuccessResponse(arg0);

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

						netWorkResponse.onError(arg0.getMessage());
					}
				});
		if (getRequestQueue() == null) {
			initVolley(context);
		}
		//stringRequest.setCacheEntry(parseIgnoreCacheHeaders());
		stringRequest.setTag(url);
		if(!isLiveFeedReq()){
			stringRequest.setShouldCache(true);
		}
		getRequestQueue().add(stringRequest);
	}
	
	public void getJsonObjectResponse(Context context,Boolean isRefresh, String url, final OnNetWorkResponse netWorkResponse,final Map<String, String> params){

		if(isRefresh){
			clearCache(url);
		}

		JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				AppLog.v("", "API CALL : "+response.toString());
				netWorkResponse.onSuccessResponse(response);
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.v("", "API CALL : " + error.getMessage());
				//netWorkResponse.onError(error.getMessage());
				String json = "";
				NetworkResponse response = error.networkResponse;
				if(response != null && response.data != null){
					json = new String(response.data);
					netWorkResponse.onError("Error from volley");
				}
			}
		}){
			@Override
			protected Map<String, String> getParams() {

				return params;
			}
		};
		if(getRequestQueue()==null){
			initVolley(context);
		}
		if(!isLiveFeedReq()){
			jsonObjRequest.setShouldCache(true);
		}
		jsonObjRequest.setCacheEntry(parseIgnoreCacheHeaders());
		jsonObjRequest.setTag(url);
		getRequestQueue().add(jsonObjRequest);
	} 
	
	public void getJsonArrayResponse(Boolean isRefresh,Context context,String url,final OnNetWorkResponse netWorkResponse,final Map<String, String> params){

		if(isRefresh){
			clearCache(url);
		}
		JsonArrayRequest jsonArrayjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				netWorkResponse.onSuccessResponse(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				netWorkResponse.onError(error.getMessage());
			}
		}){
			@Override
			protected Map<String, String> getParams() {

				return params;
			}
		};;
		if(getRequestQueue()==null){
			initVolley(context);
		}
		if(!isLiveFeedReq()){
			jsonArrayjRequest.setShouldCache(true);
		}
		jsonArrayjRequest.setCacheEntry(parseIgnoreCacheHeaders());
		jsonArrayjRequest.setTag(url);
		getRequestQueue().add(jsonArrayjRequest);
	} 
	
	public void postJsonObjectResponse(Boolean isRefresh,Context context,String url,JSONObject jsonObject,final OnNetWorkResponse netWorkResponse,final HashMap<String,String> params) {


		if(isRefresh){
			clearCache(url);
		}

		JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				AppLog.v("", "API CALL : "+response.toString());
				netWorkResponse.onSuccessResponse(response);
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.v("", "API CALL : "+error.getMessage());
				netWorkResponse.onError(error.getMessage());
			}
		}){
			@Override
			protected Map<String, String> getParams() {
				params.put("Content-Type","application/json");
				return params;
			}
		};
		if(getRequestQueue()==null){
			initVolley(context);
		}
		if(!isLiveFeedReq()){
			jsonObjRequest.setShouldCache(true);
		}
		
		jsonObjRequest.setCacheEntry(parseIgnoreCacheHeaders());
		jsonObjRequest.setTag(url);
		getRequestQueue().add(jsonObjRequest);
	} 
	
	public void putJsonObjectResponse(Boolean isRefresh,Context context,String url,JSONObject jsonObject,final OnNetWorkResponse netWorkResponse,final HashMap<String,String> params){
		AppLog.v("", "API CALL : "+url);

		if(isRefresh){
			clearCache(url);
		}
		JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				AppLog.v("", "API CALL : "+response.toString());
				netWorkResponse.onSuccessResponse(response);
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.v("", "API CALL : "+error.getMessage());
				netWorkResponse.onError(error.getMessage());
			}
		}){
			@Override
			protected Map<String, String> getParams() {

				return params;
			}
		};
		if(getRequestQueue()==null){
			initVolley(context);
		}
		if(!isLiveFeedReq()){
			jsonObjRequest.setShouldCache(true);
		}
		
		jsonObjRequest.setCacheEntry(parseIgnoreCacheHeaders());
		jsonObjRequest.setTag(url);
		getRequestQueue().add(jsonObjRequest);
	} 
	
	//TODO : should modify according to REQ.
	public Cache.Entry parseIgnoreCacheHeaders() {
	    long now = System.currentTimeMillis();
	    long cacheHitButRefreshed = 2 * 60 * 1000; // in 2 minutes cache will be hit, but also refreshed on background
	    long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
	    long softExpire = now + cacheHitButRefreshed;
	    long ttl = now + cacheExpired;
	    if(isLiveFeedReq){
	    	softExpire=0;
	    	ttl=0;
	    }
	    
	    Cache.Entry entry = new Cache.Entry();
	    entry.softTtl = softExpire;
	    entry.ttl = ttl;
	    entry.serverDate = new Date().getTime();

	    return entry;
	}
	
	public boolean isLiveFeedReq() {
		return isLiveFeedReq;
	}

	public void setLiveFeedReq(boolean isLiveFeedRequest) {
		isLiveFeedReq = isLiveFeedRequest;
	}

	public static ImageLoader getImageLoader(Context context) {
		if(imageLoader==null){
			initVolley(context);
		}
		return imageLoader;
	}

	public static void setImageLoader(ImageLoader imageLoader) {
		AppVolleyApiManager.imageLoader = imageLoader;
	}
}
