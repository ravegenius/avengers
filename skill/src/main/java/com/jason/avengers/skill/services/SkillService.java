package com.jason.avengers.skill.services;

import com.jason.avengers.skill.beans.SkillBean;
import com.jason.core.network.NetworkResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by jason on 2018/3/25.
 */

public interface SkillService {

    @GET("skill")
    Observable<NetworkResult<SkillBean>> getSkill();
}
