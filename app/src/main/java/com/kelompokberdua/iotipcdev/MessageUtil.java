package com.kelompokberdua.iotipcdev;

import android.os.Message;

public class MessageUtil {
    public static Message getMessage(int msgWhat, int arg) {
        Message msg = new Message();
        msg.what = msgWhat;
        msg.arg1 = arg;
        return msg;
    }

    public static Message getMessage(int msgWhat, int arg, Object obj) {
        Message msg = new Message();
        msg.what = msgWhat;
        msg.arg1 = arg;
        msg.obj = obj;
        return msg;
    }
}
