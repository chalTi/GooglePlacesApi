package com.example.wentongwang.mygoogleplacesapi;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by wentongwang on 06/06/2017.
 */

public interface DQEService {

    @GET("CP")
    Observable<Map<String, DQEResult>> getCityWithCodePostal(@QueryMap Map<String, String> params);

    @GET("ADR")
    Observable<Map<String, DQEResult>> getAddressInCity(@QueryMap Map<String, String> params);

    @GET("NUM")
    Observable<Map<String, DQEResult>> getNumInAddress(@QueryMap Map<String, String> params);

    @GET("TEL")
    Observable<Map<String, PhoneCheckResponse>> checkPhoneNumber(@QueryMap Map<String, String> params);

    @GET("DQEEMAILLOOKUP")
    Observable<Map<String, EmailCheckResponse>> checkEmail(@QueryMap Map<String, String> params);
}
