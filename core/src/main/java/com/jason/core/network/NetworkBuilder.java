package com.jason.core.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jason.core.stetho.StethoUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jason on 2018/3/22.
 */

public class NetworkBuilder {

    public static final String BASE_URL_HEAD_NAME = "base_url";
    private static final String LOG_TAG = "JCNet";
    private static final String BASE_URL = "http://www.163.com/";

    private static final String GSON_DF = "yyyy-MM-dd HH:mm:ss";
    private static final Gson sGson = new GsonBuilder().setDateFormat(GSON_DF).create();

    private static OkHttpClient sOkHttpClient;
    private static Retrofit sRetrofit;

    private static OkHttpClient buildOkHttpClient() {
        if (null == sOkHttpClient) {
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequestsPerHost(10);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .dispatcher(dispatcher)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new GzipInterceptor())
                    .addInterceptor(new BuiltInInterceptor())
                    .addInterceptor(new BaseUrlSwitchInterceptor())
                    .addInterceptor(new LogInterceptor(LOG_TAG, true));
            builder = StethoUtils.addStethoNetworkInterceptor(builder);
            sOkHttpClient = builder.build();
        }
        return sOkHttpClient;
    }

    private static Retrofit buildRetrofit() {
        if (null == sRetrofit) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    // 配置okhttp
                    .client(buildOkHttpClient())
                    // GSON
                    .addConverterFactory(GsonConverterFactory.create(sGson))
                    // 针对rxjava2.x
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build();
        }
        return sRetrofit;
    }

    public static <T> T build(final Class<T> service) {
        return buildRetrofit().create(service);
    }
}
