package com.jason.core.network;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.jason.core.network.NetworkBuilder.BASE_URL_HEAD_NAME;

public class BaseUrlSwitchInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 获取request
        Request request = chain.request();
        // 从request中获取headers，通过给定的键url_name
        List<String> headerValues = request.headers(BASE_URL_HEAD_NAME);
        if (headerValues != null && headerValues.size() > 0) {
            try {
                // 获取request的创建者builder
                Request.Builder builder = request.newBuilder();
                // 如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
                builder.removeHeader(BASE_URL_HEAD_NAME);
                String headerValue = headerValues.get(0);
                // 匹配获得新的BaseUrl
                HttpUrl baseUrl = HttpUrl.parse(headerValue);
                // 重建新的HttpUrl，修改需要修改的url部分
                baseUrl = request.url().newBuilder()
                        .scheme(baseUrl.scheme())
                        .host(baseUrl.host())
                        .port(baseUrl.port())
                        .build();
                // 然后返回一个response至此结束修改
                return chain.proceed(builder.url(baseUrl).build());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return chain.proceed(request);
        } else {
            return chain.proceed(request);
        }
    }
}