package com.jason.avengers.resume.services;

import com.jason.avengers.network.NetworkResult;
import com.jason.avengers.user.beans.UserBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by jason on 2018/3/25.
 */

public interface ResumeService {

    @GET("resume")
    Observable<NetworkResult<UserBean>> getResume();
}
