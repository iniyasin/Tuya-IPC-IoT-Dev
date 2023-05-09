package com.kelompokberdua.iotipcdev.devicepairing;

import static android.graphics.Color.BLACK;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.kelompokberdua.iotipcdev.R;
import com.kelompokberdua.iotipcdev.devicemanagement.DevicePairingProcessActivity;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.builder.ThingCameraActivatorBuilder;
import com.thingclips.smart.sdk.api.IThingActivatorGetToken;
import com.thingclips.smart.sdk.api.IThingCameraDevActivator;
import com.thingclips.smart.sdk.api.IThingSmartCameraActivatorListener;
import com.thingclips.smart.sdk.bean.DeviceBean;

import java.util.Hashtable;

public class CreateQrCodeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout layoutInput, layoutResult;
    EditText wifiSsidEdt, passwordSsidEdt;
    Button btnGenerateQr, btnNextStep;
    ImageView imgQrCode;
    private long mHomeId;
    String tokenPairing;
    private IThingCameraDevActivator mThingActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr_code);

        mHomeId = getIntent().getLongExtra("homeId", 0);

        layoutInput = findViewById(R.id.id_layout_input);
        wifiSsidEdt = findViewById(R.id.id_wifi_ssid);
        passwordSsidEdt = findViewById(R.id.id_password_ssid);
        btnGenerateQr = findViewById(R.id.btn_generate_qr_code);
        btnNextStep = findViewById(R.id.btn_next_step);
        layoutResult = findViewById(R.id.layout_result);
        imgQrCode = findViewById(R.id.iv_qrcode);

        btnGenerateQr.setOnClickListener(this);
        btnNextStep.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_generate_qr_code) {
            ThingHomeSdk.getActivatorInstance().getActivatorToken(mHomeId,
                    new IThingActivatorGetToken() {
                        @Override
                        public void onSuccess(String token) {
                            tokenPairing = token;
                            ThingCameraActivatorBuilder builder = new ThingCameraActivatorBuilder()
                                    .setContext(getBaseContext())
                                    .setSsid(wifiSsidEdt.getText().toString())
                                    .setPassword(passwordSsidEdt.getText().toString())
                                    .setToken(token)
                                    .setTimeOut(1000)
                                    .setListener(new IThingSmartCameraActivatorListener() {
                                        @Override
                                        public void onQRCodeSuccess(String qrcodeUrl) {
                                            final Bitmap bitmap;
                                            try {
                                                bitmap = createQRCode(qrcodeUrl, 300);
                                                CreateQrCodeActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        imgQrCode.setImageBitmap(bitmap);
                                                        layoutInput.setVisibility(View.GONE);
                                                        imgQrCode.setVisibility(View.VISIBLE);
                                                        btnGenerateQr.setVisibility(View.GONE);
                                                        layoutResult.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            } catch (WriterException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(String errorCode, String errorMsg) {
                                            Toast.makeText(
                                                    CreateQrCodeActivity.this,
                                                    "code: " + errorCode + "error:" + errorMsg,
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }

                                        @Override
                                        public void onActiveSuccess(DeviceBean devResp) {
                                            Toast.makeText(CreateQrCodeActivity.this,"config success!",Toast.LENGTH_LONG).show();
                                        }
                                    });
                            mThingActivator = ThingHomeSdk.getActivatorInstance().newCameraDevActivator(builder);
                            mThingActivator.createQRCode();
//                            mThingActivator.start();
                        }

                        @Override
                        public void onFailure(String s, String s1) {
                            Toast.makeText(
                                    CreateQrCodeActivity.this,
                                    "code: " + s + "error:" + s1,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        } else if (view.getId() == R.id.btn_next_step) {
            Intent intent = new Intent(CreateQrCodeActivity.this, DevicePairingProcessActivity.class);
            intent.putExtra("tokenPairing", tokenPairing);
            intent.putExtra("wifiSsid", wifiSsidEdt.getText().toString());
            intent.putExtra("passwordSsid", passwordSsidEdt.getText().toString());
            startActivity(intent);
        }
    }

    public static Bitmap createQRCode(String url, int widthAndHeight)
            throws WriterException {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN,0);
        BitMatrix matrix = new MultiFormatWriter()
                .encode(
                        url,
                        BarcodeFormat.QR_CODE,
                        widthAndHeight,
                        widthAndHeight,
                        hints
                );

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mThingActivator) {
            mThingActivator.stop();
            mThingActivator.onDestroy();
        }
    }
}