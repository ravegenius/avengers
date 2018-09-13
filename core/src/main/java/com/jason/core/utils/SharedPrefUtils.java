package com.jason.core.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefUtils {

    /**
     * 上下文
     */
    private static Context mContext;

    /**
     * 初始化SharedPreferences，必须使用该方法
     */
    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 抛出异常
     */
    private static void throwInit() {
        if (mContext == null) {
            throw new NullPointerException("在使用该方法前，需要使用init()方法，推荐将init()放入Application中");
        }
    }

    /**
     * 插入字符串
     *
     * @param name  文件名
     * @param key   key
     * @param value 值
     */
    public static void putString(String name, String key, String value) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    /**
     * 获取字符串
     *
     * @param name         文件名
     * @param key          key
     * @param defaultValue 没获取到时的默认值
     * @return 字符串
     */
    public static String getString(String name, String key, String defaultValue) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * 插入整型
     *
     * @param name  文件名
     * @param key   key
     * @param value 值
     */
    public static void putInt(String name, String key, int value) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 获取整型
     *
     * @param name         文件名
     * @param key          key
     * @param defaultValue 没获取到时的默认值
     * @return 整型
     */
    public static int getInt(String name, String key, int defaultValue) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 插入浮点型
     *
     * @param name  文件名
     * @param key   key
     * @param value 值
     */
    public static void putFloat(String name, String key, float value) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).apply();
    }

    /**
     * 获取浮点型
     *
     * @param name         文件名
     * @param key          key
     * @param defaultValue 没获取到时的默认值
     * @return 浮点型
     */
    public static float getFloat(String name, String key, float defaultValue) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultValue);
    }

    /**
     * 插入Long型
     *
     * @param name  文件名
     * @param key   key
     * @param value 值
     */
    public static void putLong(String name, String key, long value) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();
    }

    /**
     * 获取长整型
     *
     * @param name         文件名
     * @param key          key
     * @param defaultValue 没获取到时的默认值
     * @return 长整型
     */
    public static float getLong(String name, String key, long defaultValue) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    /**
     * 插入boolean型
     *
     * @param name  文件名
     * @param key   key
     * @param value 值
     */
    public static void putBoolean(String name, String key, boolean value) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 获取布尔型
     *
     * @param name         文件名
     * @param key          key
     * @param defaultValue 没获取到时的默认值
     * @return 布尔型
     */
    public static boolean getBoolean(String name, String key, boolean defaultValue) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 清除
     *
     * @param name 文件名
     */
    public static void clear(String name) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}