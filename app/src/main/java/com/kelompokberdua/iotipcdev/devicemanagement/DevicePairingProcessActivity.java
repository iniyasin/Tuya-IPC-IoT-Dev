package com.kelompokberdua.iotipcdev.devicemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.kelompokberdua.iotipcdev.R;
import com.kelompokberdua.iotipcdev.devicepairing.CreateQrCodeActivity;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.builder.ThingCameraActivatorBuilder;
import com.thingclips.smart.sdk.api.IThingCameraDevActivator;
import com.thingclips.smart.sdk.api.IThingSmartCameraActivatorListener;
import com.thingclips.smart.sdk.bean.DeviceBean;

public class DevicePairingProcessActivity extends AppCompatActivity {
    String token, wifiSsid, passwordSsid;
    TextView pairingStatus;
    private IThingCameraDevActivator mThingActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_pairing_process);

        token = getIntent().getStringExtra("tokenPairing");
        wifiSsid = getIntent().getStringExtra("wifiSsid");
        passwordSsid = getIntent().getStringExtra("passwordSsid");

        pairingStatus = findViewById(R.id.id_status_pairing);

        ThingCameraActivatorBuilder builder = new ThingCameraActivatorBuilder()
                .setContext(getBaseContext())
                .setSsid(wifiSsid)
                .setPassword(passwordSsid)
                .setToken(token)
                .setTimeOut(120)
                .setListener(new IThingSmartCameraActivatorListener() {
                    @Override
                    public void onQRCodeSuccess(String qrcodeUrl) {
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(
                                DevicePairingProcessActivity.this,
                                "code: " + errorCode + "error:" + errorMsg,
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    @Override
                    public void onActiveSuccess(DeviceBean devResp) {
                        pairingStatus.setText(devResp.getConnectionStatus());
                        Toast.makeText(DevicePairingProcessActivity.this,"config success!",Toast.LENGTH_LONG).show();
                    }
                });
        mThingActivator = ThingHomeSdk.getActivatorInstance().newCameraDevActivator(builder);
        mThingActivator.start();
    }
}