package com.jason.avengers.common.sms;

import com.jason.core.network.NetworkBuilder;
import com.jason.core.network.NetworkResult;
import com.jason.core.network.NetworkTools;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jason on 2018/9/4.
 */

public enum SMSManager {

    INSTANCE;

    public void sendMessage(final String phone, final SMSCallback callback) {
        Observable.just(phone)
                .flatMap(new Function<String, ObservableSource<NetworkResult>>() {
                    @Override
                    public ObservableSource<NetworkResult> apply(String phone) throws Exception {
                        return NetworkBuilder.build(SMSService.class).smsSendMessage(phone);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<NetworkResult>() {
                            @Override
                            public void accept(NetworkResult result) throws Exception {
                                if (NetworkTools.checkNetworkSuccess(result)) {
                                    if (callback != null) {
                                        callback.onSMSResult(true);
                                    }
                                } else {
                                    if (callback != null) {
                                        callback.onSMSResult(false);
                                    }
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (callback != null) callback.onSMSResult(false);
                            }
                        });
    }

    public interface SMSCallback {

        void onSMSResult(boolean success);
    }

    public interface SMSService {

        @FormUrlEncoded
        @POST("smsSendMessage")
        Observable<NetworkResult> smsSendMessage(String phone);
    }
}
