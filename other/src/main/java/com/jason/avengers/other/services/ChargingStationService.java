package com.jason.avengers.other.services;

import com.jason.avengers.other.beans.ChargingStationBean;
import com.jason.core.network.NetworkBuilder;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChargingStationService {

    String BASE_URL = "http://223.112.89.148:18082/";

    @Headers({NetworkBuilder.BASE_URL_HEAD_NAME + ":" + BASE_URL})
    @POST("ichargeservice/rest/UserCheckWebc/1.0/F703")
    Observable<ChargingStationBean<ChargingStationBean.ChargingStationUser>> loginChargingStations(@Body Map<String, String> map);

    @Headers({NetworkBuilder.BASE_URL_HEAD_NAME + ":" + BASE_URL})
    @POST("ichargeservice/rest/ChargePileWebc/1.0/F103")
    Observable<ChargingStationBean<ChargingStationBean.ChargingStationCollect>> getChargingStations(@Body Map<String, String> map);

    @Headers({NetworkBuilder.BASE_URL_HEAD_NAME + ":" + BASE_URL})
    @POST("ichargeservice/rest/ChargePileWebc/1.0/F105")
    Observable<ChargingStationBean> applyChargingStations(@Body Map<String, String> map);

    @Headers({NetworkBuilder.BASE_URL_HEAD_NAME + ":" + BASE_URL})
    @POST("ichargeservice/rest/ChargePileWebc/1.0/F106")
    Observable<ChargingStationBean> cancelChargingStations(@Body Map<String, String> map);
}
