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


    private static Retrofit sRetrofit = null;

    private static final String GSON_DF = "yyyy-MM-dd HH:mm:ss";
    private static final String LOG_TAG = "Avengers";
    private static final String baseUrl = "https://www.baidu.com/";

    private static void init() {
        //配置你的Gson
        Gson gson = new GsonBuilder().setDateFormat(GSON_DF).create();

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(10);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(new GzipInterceptor())
                .addInterceptor(new BuiltInInterceptor())
                .addInterceptor(new LogInterceptor(LOG_TAG, true));

        builder = StethoUtils.addStethoNetworkInterceptor(builder);

        OkHttpClient okHttpClient = builder.build();

        sRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                // 配置okhttp
                .client(okHttpClient)
                // Gson
                .addConverterFactory(GsonConverterFactory.create(gson))
                // 针对rxjava2.x
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    public static <T> T build(final Class<T> service) {
        if (sRetrofit == null) {
            init();
        }
        return sRetrofit.create(service);
    }
}
