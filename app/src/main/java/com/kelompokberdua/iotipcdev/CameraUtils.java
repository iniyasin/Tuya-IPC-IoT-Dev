package com.kelompokberdua.iotipcdev;

import static com.kelompokberdua.iotipcdev.Constant.INTENT_DEV_ID;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.kelompokberdua.iotipcdev.devicemanagement.DeviceDetail;
import com.thingclips.smart.android.camera.sdk.ThingIPCSdk;
import com.thingclips.smart.android.camera.sdk.api.IThingIPCCore;

public final class CameraUtils {
    private CameraUtils() {
    }

    public static void init(Application application) {
        FrescoManager.initFresco(application);
//        CameraDoorbellManager.getInstance().init(application);
    }

    public static boolean ipcProcess(Context context, String devId) {
        IThingIPCCore cameraInstance = ThingIPCSdk.getCameraInstance();
        if (cameraInstance != null) {
            if (cameraInstance.isIPCDevice(devId)) {
                Intent intent = new Intent(context, DeviceDetail.class);
                intent.putExtra(INTENT_DEV_ID, devId);
                context.startActivity(intent);
                return true;
            }
        }
        return false;
    }
}
