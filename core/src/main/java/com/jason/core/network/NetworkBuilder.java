package com.jason.core.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jason.core.stetho.StethoUtils;

import java.util.Map;
import java.util.WeakHashMap;
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

    private static Map<String, Retrofit> sRetrofits = new WeakHashMap<>();

    private static final String GSON_DF = "yyyy-MM-dd HH:mm:ss";
    private static final String LOG_TAG = "JCNet";
    private static final String BASE_URL = "https://www.baidu.com/";
    //配置Gson
    private static final Gson sGson = new GsonBuilder().setDateFormat(GSON_DF).create();
    private static OkHttpClient sOkHttpClient;

    static {
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
        sOkHttpClient = builder.build();
    }

    private static Retrofit init(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                // 配置okhttp
                .client(sOkHttpClient)
                // GSON
                .addConverterFactory(GsonConverterFactory.create(sGson))
                // 针对rxjava2.x
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    public static <T> T build(final Class<T> service) {
        return build(BASE_URL, service);
    }

    public static <T> T build(final String hostUrl, final Class<T> service) {
        Retrofit retrofit;
        if (sRetrofits.containsKey(hostUrl)) {
            retrofit = sRetrofits.get(hostUrl);
        } else {
            retrofit = init(hostUrl);
            sRetrofits.put(hostUrl, retrofit);
        }
        return retrofit.create(service);
    }
}
