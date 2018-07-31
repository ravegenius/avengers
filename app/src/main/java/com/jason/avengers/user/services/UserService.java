package com.jason.avengers.user.services;

import com.jason.avengers.network.NetworkResult;
import com.jason.avengers.user.beans.UserBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by jason on 2018/4/23.
 */

public interface UserService {

    @GET("user")
    Observable<NetworkResult<UserBean>> getUser();
}
