package com.day.loan.utils;

import android.util.Log;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/23 下午4:33
 * - @Email whynightcode@gmail.com
 */
public class Logger {

    private static boolean isTure=true;

    public static void e(String tag, String message) {
        if (isTure) {
            Log.e(tag, message);
        }
    }
}
