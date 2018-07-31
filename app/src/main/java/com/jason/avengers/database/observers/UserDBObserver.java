package com.jason.avengers.database.observers;

import com.jason.avengers.database.entity.UserDBEntity;

import io.objectbox.reactive.DataObserver;

/**
 * Created by jason on 2018/7/26.
 */

public class UserDBObserver implements DataObserver<UserDBEntity> {

    @Override
    public void onData(UserDBEntity data) {
    }
}
