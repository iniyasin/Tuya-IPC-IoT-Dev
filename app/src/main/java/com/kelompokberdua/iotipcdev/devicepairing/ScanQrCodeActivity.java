package com.kelompokberdua.iotipcdev.devicepairing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kelompokberdua.iotipcdev.R;
import com.kelompokberdua.iotipcdev.homemanagement.HomeDetailActivity;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.builder.ThingQRCodeActivatorBuilder;
import com.thingclips.smart.sdk.api.IThingActivator;
import com.thingclips.smart.sdk.api.IThingDataCallback;
import com.thingclips.smart.sdk.api.IThingDevActivatorListener;
import com.thingclips.smart.sdk.api.IThingSmartActivatorListener;
import com.thingclips.smart.sdk.bean.DeviceBean;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ScanQrCodeActivity extends AppCompatActivity {
    private String mUuid;
    Button btnScan;
    private static final int REQUEST_CODE_SCAN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);

        btnScan = findViewById(R.id.btn_scan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ScanQrCodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
                    ActivityCompat.requestPermissions(ScanQrCodeActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_SCAN);
                    return;
                }

                if (ContextCompat.checkSelfPermission(ScanQrCodeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ScanQrCodeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_SCAN);
                    return;
                }

                Intent intent = new Intent(ScanQrCodeActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN) {

            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "result:" + result, Toast.LENGTH_LONG).show();
                    deviceQrCode(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "Failed to parse QR code", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void deviceQrCode(String result) {
        HashMap<String, Object> postData = new HashMap<>();
        postData.put("code", result);
        ThingHomeSdk.getRequestInstance()
                .requestWithApiNameWithoutSession("tuya.m.qrcode.parse",
                        "4.0",
                        postData,
                        String.class,
                        new IThingDataCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        initQrCode(result);
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        Toast.makeText(
                                ScanQrCodeActivity.this,
                                "code: " + errorCode + "error:" + errorMessage,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void initQrCode(String result) {
        long homeId = getIntent().getLongExtra("homeId", 0);
        try {
            JSONObject obj = new JSONObject(result);
            JSONObject actionObj = obj.optJSONObject("actionData");
            if (null != actionObj) {
                mUuid = actionObj.optString("uuid");
                ThingHomeSdk.getActivatorInstance().bindThingLinkDeviceWithQRCode(homeId, mUuid, new IThingDevActivatorListener() {
                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(
                                ScanQrCodeActivity.this,
                                "code: " + errorCode + "error:" + errorMsg,
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    @Override
                    public void onActiveSuccess(DeviceBean devResp) {
                        Toast.makeText(ScanQrCodeActivity.this, "onActiveSuccess --->>", Toast.LENGTH_LONG).show();
                    }
                });
//                ThingQRCodeActivatorBuilder builder = new ThingQRCodeActivatorBuilder()
//                        .setUuid(mUuid)
//                        .setHomeId(homeId)
//                        .setContext(this)
//                        .setTimeOut(1000)
//                        .setListener(new IThingSmartActivatorListener() {
//                            @Override
//                            public void onError(String errorCode, String errorMsg) {
//                                Toast.makeText(
//                                        ScanQrCodeActivity.this,
//                                        "code: " + errorCode + "error:" + errorMsg,
//                                        Toast.LENGTH_LONG
//                                ).show();
//                            }
//
//                            @Override
//                            public void onActiveSuccess(DeviceBean devResp) {
//                                Toast.makeText(ScanQrCodeActivity.this, "ActiveSuccess", Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onStep(String step, Object data) {
//                                Toast.makeText(
//                                        ScanQrCodeActivity.this,
//                                        "step: " + step + "data:" + data.toString(),
//                                        Toast.LENGTH_LONG
//                                ).show();
//                            }
//                        });
//                IThingActivator iTuyaActivator = ThingHomeSdk.getActivatorInstance().newQRCodeDevActivator(builder);
//                iTuyaActivator.start();
//                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}