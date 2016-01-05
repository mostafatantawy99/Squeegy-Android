package com.squeegy.android.controller;


import android.content.Context;

import com.squeegy.android.api.AppVolleyApiManager;
import com.squeegy.android.api.OnNetWorkResponse;
import com.squeegy.android.listener.OnNearByCoordsResponseListner;
import com.squeegy.android.model.NearByCoords;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoordinateServiceController {

    public static void getNearbyCoOrdinates(Context context, final OnNearByCoordsResponseListner responseListner){

        String apiPath="http://api-stage.squeegyapp.com/v1/services/coords";

        AppVolleyApiManager.instance().getJsonObjectResponse(context, true, apiPath, new OnNetWorkResponse() {
            @Override
            public void onSuccessResponse(String xmlStreamSource) {

            }

            @Override
            public void onSuccessResponse(JSONObject responseObject) {

                if(responseObject!=null){
                    JSONArray dataArray = responseObject.optJSONArray("data");
                    List<NearByCoords> nearByCoordses=new ArrayList<NearByCoords>();

                    if(dataArray!=null && dataArray.length()>0){
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONArray nearByCoordsArray = dataArray.optJSONArray(i);
                            NearByCoords nearByCoords = new NearByCoords(nearByCoordsArray);
                            nearByCoordses.add(nearByCoords);
                        }

                    }
                    responseListner.onSuccessResponse(nearByCoordses);

                }else{
                    responseListner.onError();
                }
            }

            @Override
            public void onSuccessResponse(JSONArray responseArray) {

            }

            @Override
            public void onError(String error) {

            }
        },new HashMap<String, String>());

    }
}
