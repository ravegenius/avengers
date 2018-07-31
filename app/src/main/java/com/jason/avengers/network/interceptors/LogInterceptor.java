package com.jason.avengers.network.interceptors;

import android.text.TextUtils;
import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static okhttp3.internal.http.StatusLine.HTTP_CONTINUE;

public final class LogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static int MAX_SIZE = 10 * 1024 * 1024;
    private final String mTag;
    private final ILogEvent mLogEvent;
    private final boolean mLogBody;

    public LogInterceptor(String tag, boolean logBody) {
        this(tag, logBody, new DefaultLogEvent());
    }

    public LogInterceptor(String tag, boolean logBody, ILogEvent logEvent) {
        mTag = tag;
        mLogEvent = logEvent;
        mLogBody = logBody;
        if (TextUtils.isEmpty(mTag) || mLogEvent == null) {
            throw new IllegalArgumentException("LogInterceptor IllegalArgumentException!!");
        }
    }

    public static boolean hasBody(Response response) {
        // HEAD requests never yield a body regardless of the response headers.
        if (response.request().method().equals("HEAD")) {
            return false;
        }

        int responseCode = response.code();
        if ((responseCode < HTTP_CONTINUE || responseCode >= 200)
                && responseCode != HTTP_NO_CONTENT
                && responseCode != HTTP_NOT_MODIFIED) {
            return true;
        }

        // If the Content-Length or Transfer-Encoding headers disagree with the response code, the
        // response is malformed. For best compatibility, we honor the headers.
        if (HttpHeaders.contentLength(response) != -1
                || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return true;
        }

        return false;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        StringBuilder startStrBuilder = new StringBuilder();
        startStrBuilder.append("--> ").append(request.method()).append(" ").append(request.url()).append(" ").append(protocol).append("\n")
                .append(mLogEvent.generateRequestHeaderInfo(request));
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (isSizeValid(buffer)) {
                startStrBuilder.append(buffer.readString(charset)).append("\n");
            }
        }
        Log.i(mTag, startStrBuilder.toString());
        mLogEvent.beforeRequest(request);
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            Log.e(mTag, mLogEvent.generateHttpExceptionInfo(request, e));
            throw e;
        }
        ResponseBody responseBody = response.body();
        StringBuilder resultStrBuilder = new StringBuilder();
        resultStrBuilder.append("<-- ").append(response.code()).append(" ").append(response.message()).append(' ')
                .append(response.request().url()).append("\n")
                .append(mLogEvent.generateResponseHeaderInfo(response)).append("\n");

        if (mLogBody && hasBody(response) && needPrintBody(responseBody)) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    Log.i(mTag, resultStrBuilder.toString());
                    Log.e(mTag, "Couldn't decode the response body; charset is likely malformed.");
                    return response;
                }
            }
            resultStrBuilder.append("size:").append(buffer.size()).append("\n");
            if (isSizeValid(buffer)) {
                resultStrBuilder.append(buffer.clone().readString(charset));
            } else {
                resultStrBuilder.append(" body is too large!! ");
            }
        }
        Log.i(mTag, resultStrBuilder.toString());
        return response;
    }

    /**
     * buffer size是否在max_size内
     *
     * @param buffer
     * @return
     */
    private boolean isSizeValid(Buffer buffer) {
        if (buffer == null) {
            return true;
        }
        return buffer.size() < MAX_SIZE;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    /**
     * 只有content-type一级类型为application或者text时才打印body
     * 其他类型video、audio、image都不打印body
     */
    private boolean needPrintBody(ResponseBody body) {
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return true;
        }
        String type = mediaType.type();
        if ("application".equals(type) || "text".equals(type)) {
            return true;
        }
        return false;
    }

    public interface ILogEvent {

        String generateRequestHeaderInfo(final Request request);

        String generateResponseHeaderInfo(final Response response);

        String generateHttpExceptionInfo(final Request request, final Exception e);

        void beforeRequest(final Request request);
    }

    public static class DefaultLogEvent implements LogInterceptor.ILogEvent {

        @Override
        public String generateRequestHeaderInfo(Request request) {
            if (request != null && request.headers() != null) {
                return request.headers().toString();
            }
            return "";
        }

        @Override
        public String generateResponseHeaderInfo(Response response) {
            if (response != null && response.headers() != null) {
                return response.headers().toString();
            }
            return "";
        }

        @Override
        public String generateHttpExceptionInfo(Request request, Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<-- HTTP FAILED: ")
                    .append(request.url()).append("\n")
                    .append(e);
            return stringBuilder.toString();
        }

        @Override
        public void beforeRequest(Request request) {
        }
    }
}