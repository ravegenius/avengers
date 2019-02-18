package com.jason.avengers.other.services;

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
    Observable<String> loginChargingStations(@Body Map<String, String> map);

    @Headers({NetworkBuilder.BASE_URL_HEAD_NAME + ":" + BASE_URL})
    @POST("ichargeservice/rest/ChargePileWebc/1.0/F103")
    Observable<String> getChargingStations(@Body Map<String, String> map);

    @Headers({NetworkBuilder.BASE_URL_HEAD_NAME + ":" + BASE_URL})
    @POST("ichargeservice/rest/ChargePileWebc/1.0/F105")
    Observable<String> applyChargingStations(@Body Map<String, String> map);
}
