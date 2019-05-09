package com.wayto.map.utils;

import android.util.Log;

/**
 * 日志工具类
 * <p>
 * author: hezhiWu <wuhezhi007@gmail.com>
 * version: V1.0.0
 * created at 2017/3/14 10:47
 * <p>
 * Copyright (c) 2017 Shenzhen O&M Cloud Co., Ltd. All rights reserved.
 */
public class MLog {

    /**
     * 用于自定义TAG
     */
    public static String LOG_TAG = "tag";
    /**
     * 日志前缀
     */
    public static String LOG_PRE = " <||> ";
    /**
     * 安全级别日志,true:不输出或保存日志，false:可输出或保存日志
     */
//    public static boolean IS_SECURITY_LOG = IConfigConstant.LOG_SWITCH;
    public static boolean IS_SECURITY_LOG = true;
    /**
     * 是否输出Log的位置，true:输出；false:不输出
     */
    public static boolean IS_LOG_POSITION = true;
    /**
     * 日志分隔字符
     */
    private static final String LOG_SPLIT = "  \t<==>  ";

    /**
     * 输出调试Log
     *
     * @param tag 标签
     * @param msg 信息
     */
    public static void d(String tag, String msg) {
        if (!IS_SECURITY_LOG) {
            return;
        }
        tag = LOG_TAG == null ? tag : LOG_TAG;
        String logMsg = (msg == null ? "" : msg);

        if (IS_LOG_POSITION) {
            logMsg = getPositionInfo() + LOG_SPLIT + logMsg;
        }
        Log.d(LOG_PRE + tag, logMsg);
    }

    /**
     * 输出浏览级别Log
     *
     * @param tag 标签
     * @param msg 信息
     */
    public static void v(String tag, String msg) {
        if (!IS_SECURITY_LOG) {
            return;
        }
        tag = LOG_TAG == null ? tag : LOG_TAG;
        String logMsg = (msg == null ? "" : msg);

        if (IS_LOG_POSITION) {
            logMsg = getPositionInfo() + LOG_SPLIT + logMsg;
        }
        Log.v(LOG_PRE + tag, logMsg);
    }

    /**
     * 输出警告级别Log
     *
     * @param tag 标签
     * @param msg 信息
     */
    public static void w(String tag, String msg) {
        if (!IS_SECURITY_LOG) {
            return;
        }
        tag = LOG_TAG == null ? tag : LOG_TAG;
        String logMsg = (msg == null ? "" : msg);

        if (IS_LOG_POSITION) {
            logMsg = getPositionInfo() + LOG_SPLIT + logMsg;
        }
        Log.w(LOG_PRE + tag, logMsg);
    }

    /**
     * 输出错误级别Log
     *
     * @param tag 标签
     * @param msg 信息
     */
    public static void e(String tag, String msg) {
        if (!IS_SECURITY_LOG) {
            return;
        }
        tag = LOG_TAG == null ? tag : LOG_TAG;
        String logMsg = (msg == null ? "" : msg);

        if (IS_LOG_POSITION) {
            logMsg = getPositionInfo() + LOG_SPLIT + logMsg;
        }
        Log.e(LOG_PRE + tag, logMsg);
    }

    /**
     * 输出信息级别
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (!IS_SECURITY_LOG) {
            return;
        }
        tag = LOG_TAG == null ? tag : LOG_TAG;
        String logMsg = (msg == null ? "" : msg);

        if (IS_LOG_POSITION) {
            logMsg = getPositionInfo() + LOG_SPLIT + logMsg;
        }
        Log.i(LOG_PRE + tag, logMsg);
    }


    /**
     * 获取Log信息
     *
     * @return
     */
    private static String getPositionInfo() {
        StackTraceElement element = new Throwable().getStackTrace()[2];
        return element.getFileName() + " ；Line " + element.getLineNumber() + " ；Method: " + element.getMethodName();
    }

}
