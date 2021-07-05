package com.example.filemanager.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private static Toast myToast;

    public static void showToast(Context mContext, String text) {
        if (myToast != null) {
            myToast.cancel();
        } else {
            myToast = new Toast(mContext);
        }
        myToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        myToast.show();
    }

    public static void showLongToast(Context mContext, String text) {
        if (myToast != null) {
            myToast.cancel();
        } else {
            myToast = new Toast(mContext);
        }
        myToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        myToast.show();
    }
}
