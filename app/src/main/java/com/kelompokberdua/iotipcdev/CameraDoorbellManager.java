package com.kelompokberdua.iotipcdev;

import static com.kelompokberdua.iotipcdev.Constant.INTENT_MSGID;

import android.app.Application;
import android.content.Intent;

import com.thingclips.smart.android.camera.sdk.ThingIPCSdk;
import com.thingclips.smart.android.camera.sdk.api.IThingIPCDoorBellManager;
import com.thingclips.smart.android.camera.sdk.bean.ThingDoorBellCallModel;
import com.thingclips.smart.android.camera.sdk.callback.ThingSmartDoorBellObserver;
import com.thingclips.smart.android.common.utils.L;
import com.thingclips.smart.sdk.bean.DeviceBean;

public class CameraDoorbellManager {
    private static final String TAG = "CameraDoorbellManager";
    public static final String EXTRA_AC_DOORBELL = "ac_doorbell";

    IThingIPCDoorBellManager doorBellInstance = ThingIPCSdk.getDoorbell().getIPCDoorBellManagerInstance();

    private CameraDoorbellManager() {
    }

    public static CameraDoorbellManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final CameraDoorbellManager INSTANCE = new CameraDoorbellManager();
    }

    public void init(Application application) {
        if (doorBellInstance != null) {
            doorBellInstance.addObserver(new ThingSmartDoorBellObserver() {
                @Override
                public void doorBellCallDidReceivedFromDevice(ThingDoorBellCallModel callModel, DeviceBean deviceBean) {
                    L.d(TAG, "Receiving a doorbell call");
                    if (null == callModel) {
                        return;
                    }
                    String type = callModel.getType();
                    String messageId = callModel.getMessageId();
                    if (EXTRA_AC_DOORBELL.equals(type)) {
//                        Intent intent = new Intent(application.getApplicationContext(), CameraDoorBellActivity.class);
//                        intent.putExtra(INTENT_MSGID, messageId);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                        application.getApplicationContext().startActivity(intent);
                    }
                }
            });
        }
    }

    public void deInit() {
        if (doorBellInstance != null) {
            doorBellInstance.removeAllObservers();
        }
    }
}
