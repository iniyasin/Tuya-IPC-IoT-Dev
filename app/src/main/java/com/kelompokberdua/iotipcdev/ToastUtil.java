package com.kelompokberdua.iotipcdev;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void shortToast(Context context, String tips) {
        Toast.makeText(context, tips, Toast.LENGTH_SHORT).show();
    }
}
