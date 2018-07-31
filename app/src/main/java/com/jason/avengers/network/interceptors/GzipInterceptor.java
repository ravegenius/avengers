package com.jason.avengers.network.interceptors;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

public class GzipInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String contentEncodingHeader = originalRequest.header("Content-Encoding");
        if (originalRequest.body() == null || contentEncodingHeader == null || !"gzip".equalsIgnoreCase(contentEncodingHeader)) {
            return chain.proceed(originalRequest);
        }
        //处理gzip
        Request.Builder requestBuilder = originalRequest.newBuilder().method(originalRequest.method(), gzip(originalRequest.body()));
        // 添加Encoding header解决  IllegalStateException: Cannot stream a request body without chunked encoding or a known content length问题
        // 详情见http://stackoverflow.com/questions/25493288/2-0-0-illegalstateexception-cannot-stream-a-request-body-without-chunked-encod
        requestBuilder.addHeader("Transfer-Encoding", "chunked");
        Request compressedRequest = requestBuilder.build();

        return chain.proceed(compressedRequest);
    }


    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1; // We don't know the compressed length in advance!
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}