package com.squeegy.android.listener;


import com.squeegy.android.model.NearByCoords;

import java.util.List;

public interface OnNearByCoordsResponseListner {

    public void onSuccessResponse(List<NearByCoords> nearByCoordses);

    public void onError();

}
