package com.kelompokberdua.iotipcdev;

import android.app.Application;

import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThingHomeSdk.init(this, "9qwhgqgqduqwxprgeaya", "4afmvwj95kphj57wvkafh3rh7fyv5su3");
        ThingHomeSdk.setDebugMode(true);
        ZXingLibrary.initDisplayOpinion(this);
    }
}
