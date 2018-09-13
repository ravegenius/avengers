package com.jason.avengers.user.services;

import com.jason.core.network.NetworkResult;
import com.jason.avengers.user.beans.UserBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by jason on 2018/4/23.
 */

public interface UserService {

    @GET("getToken")
    Observable<NetworkResult<UserBean>> login2GetToken(String un, String pwd, String phone, String email, String code);

    @GET("getToken")
    Observable<NetworkResult<UserBean>> bind2GetToken(String snsToken, String snsType);

    @GET("getUser")
    Observable<NetworkResult<UserBean>> getUser(String token);

    @GET("logout")
    Observable<NetworkResult> logout(String token);
}
