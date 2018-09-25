package com.day.loan.utils;

import android.widget.Toast;

import com.day.loan.MyApp;

/**
 *
 * @author apple
 * @date 2017/4/6
 */

public class ToastUtils {
    private static Toast toast;

    public static void showToast( String message) {
        if (toast == null) {
            toast = Toast.makeText(MyApp.getApplication(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}
