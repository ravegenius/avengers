package com.jason.avengers.network;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * Created by jason on 2018/3/30.
 */

public class NetworkTools {

    private static final int CODE_SUCCESS = 200;
    private static final String MSG_DEFAULT = "未知错误";
    private static final String MSG_TIMEOUT = "请求超时-网络环境差";
    private static final String MSG_UNKNOWN_HOST = "未知域名";

    public static boolean checkNetworkSuccess(NetworkResult result) {
        return result != null && result.getCode() == CODE_SUCCESS;
    }

    public static String parseNetworkThrowable(NetworkResult result) {
        return result != null ? result.getMsg() : MSG_DEFAULT;
    }

    public static String parseNetworkThrowable(Throwable throwable) {
        if (throwable instanceof TimeoutException) {
            return MSG_TIMEOUT;
        } else if (throwable instanceof UnknownHostException) {
            return MSG_UNKNOWN_HOST;
        } else {
            return MSG_DEFAULT;
        }
    }
}
