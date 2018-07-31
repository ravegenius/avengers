package com.jason.avengers.network.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BuiltInInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();
        Request.Builder builder = originRequest.newBuilder();
        // add public header paramters
        builder.header("Jason", "ravegenius1985@126.com");
        return chain.proceed(builder.build());
    }
}