package com.kelompokberdua.iotipcdev.homemanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kelompokberdua.iotipcdev.R;
import com.kelompokberdua.iotipcdev.adapter.DeviceListAdapter;
import com.kelompokberdua.iotipcdev.devicepairing.CreateQrCodeActivity;
import com.kelompokberdua.iotipcdev.devicepairing.ScanQrCodeActivity;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.bean.HomeBean;
import com.thingclips.smart.home.sdk.bean.WeatherBean;
import com.thingclips.smart.home.sdk.callback.IIGetHomeWetherSketchCallBack;
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback;
import com.thingclips.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

public class HomeDetailActivity extends AppCompatActivity implements View.OnClickListener{
    TextView idHome, idHomeName, idHomeCity, idHomeWeather, idHomeTemperature;
    Button btnScanQrCode, btnCreateQrCode;
    RecyclerView deviceListView;

    DeviceListAdapter deviceListAdapter;
    private long mHomeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);

        idHome = findViewById(R.id.id_home);
        idHomeName = findViewById(R.id.id_home_name);
        idHomeCity = findViewById(R.id.id_home_city);
        idHomeWeather = findViewById(R.id.id_home_weather);
        idHomeTemperature = findViewById(R.id.id_home_temperature);

        deviceListView = findViewById(R.id.id_device_listview);

        btnScanQrCode = findViewById(R.id.btn_scan_qr_code);
        btnCreateQrCode = findViewById(R.id.btn_create_qr_code);

        btnScanQrCode.setOnClickListener(this);
        btnCreateQrCode.setOnClickListener(this);

        initDetailHome();
    }

    void initDetailHome() {
        mHomeId = getIntent().getLongExtra("homeId", 0);

        ThingHomeSdk.newHomeInstance(mHomeId).getHomeDetail(new IThingHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean bean) {
                idHome.setText(String.valueOf(bean.getHomeId()));
                idHomeName.setText(bean.getName());
                idHomeCity.setText(bean.getGeoName());

                initListView(bean.getDeviceList());

                // Get home weather info
                ThingHomeSdk.newHomeInstance(mHomeId).getHomeWeatherSketch(bean.getLon(),
                        bean.getLat(),
                        new IIGetHomeWetherSketchCallBack() {
                            @Override
                            public void onSuccess(WeatherBean result) {
                                idHomeWeather.setText(result.getCondition());
                                idHomeTemperature.setText(result.getTemp());
                            }

                            @Override
                            public void onFailure(String errorCode, String errorMsg) {
                                Toast.makeText(
                                        HomeDetailActivity.this,
                                        "get home weather error->$errorMsg",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                Toast.makeText(
                        HomeDetailActivity.this,
                        "code: " + errorCode + "error:" + errorMsg,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    void initListView(List<DeviceBean> deviceBeans) {
        if (deviceBeans != null && !deviceBeans.isEmpty()) {
            deviceListView.setVisibility(View.VISIBLE);

            deviceListAdapter = new DeviceListAdapter();
            deviceListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            deviceListView.setAdapter(deviceListAdapter);

            deviceListAdapter.data = (ArrayList<DeviceBean>) deviceBeans;
            deviceListAdapter.homeId = mHomeId;
            deviceListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_scan_qr_code) {
            Intent intent = new Intent(HomeDetailActivity.this, ScanQrCodeActivity.class);
            intent.putExtra("homeId", mHomeId);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_create_qr_code) {
            Intent intent = new Intent(HomeDetailActivity.this, CreateQrCodeActivity.class);
            intent.putExtra("homeId", mHomeId);
            startActivity(intent);
        }
    }
}