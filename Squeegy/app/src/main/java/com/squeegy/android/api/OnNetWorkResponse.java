package com.squeegy.android.api;



import org.json.JSONArray;
import org.json.JSONObject;

public interface OnNetWorkResponse {

	public void onSuccessResponse(String xmlStreamSource);
	
	public void onSuccessResponse(JSONObject responseObject);
	
	public void onSuccessResponse(JSONArray responseArray);

	public void onError(String error);

}
