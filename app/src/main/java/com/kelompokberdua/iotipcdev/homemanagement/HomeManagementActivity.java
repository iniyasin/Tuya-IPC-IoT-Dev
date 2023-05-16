package com.kelompokberdua.iotipcdev.homemanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kelompokberdua.iotipcdev.R;
import com.thingclips.smart.android.user.bean.User;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.bean.HomeBean;
import com.thingclips.smart.home.sdk.bean.WeatherBean;
import com.thingclips.smart.home.sdk.callback.IIGetHomeWetherSketchCallBack;
import com.thingclips.smart.home.sdk.callback.IThingGetHomeListCallback;
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback;
import com.thingclips.smart.sdk.api.IThingDevActivatorListener;
import com.thingclips.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

public class HomeManagementActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnNewHome;
    Button btnListHome;
    Button btnCurrentHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_management);

        User user = ThingHomeSdk.getUserInstance().getUser();
        if (user != null) {
          Log.d("UID", user.getUid());
        }

        btnNewHome = findViewById(R.id.btn_new_home);
        btnListHome = findViewById(R.id.btn_list_home);
        btnCurrentHome = findViewById(R.id.btn_current_home);

        btnNewHome.setOnClickListener(this);
        btnListHome.setOnClickListener(this);
        btnCurrentHome.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_new_home) {
            Intent intent = new Intent(HomeManagementActivity.this, HomeAddNewActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_list_home) {
            Intent intent = new Intent(HomeManagementActivity.this, HomeListActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_current_home) {
            ThingHomeSdk.getHomeManagerInstance().queryHomeList(new IThingGetHomeListCallback() {
                @Override
                public void onSuccess(List<HomeBean> homeBeans) {
                    for (HomeBean home : homeBeans) {
                        Log.d("Home: ", home.getName());
                    }
                    ThingHomeSdk.newHomeInstance(homeBeans.get(0).getHomeId()).getHomeDetail(new IThingHomeResultCallback() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(HomeBean bean) {
                            Toast.makeText(getBaseContext(), "Detail: " + "\nHome: " + bean.getName() + "\nID: " + bean.getHomeId(), Toast.LENGTH_LONG).show();
                            Log.d("LOG DETAIL HOME -> ","Detail: " + "\nHome: " + bean.getName() + "\nID: " + bean.getHomeId());
//                            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
//                            builder.setMessage("Detail: " + "\nHome: " + bean.getName() + "\nID: " + bean.getHomeId())
//                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            finish();
//                                        }
//                                    });
//                            // Create the AlertDialog object and return it
//                            builder.create();
                        }

                        @Override
                        public void onError(String errorCode, String errorMsg) {
                            Toast.makeText(getBaseContext(), "code home detail error: " + errorCode + "error home detail:" + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(String errorCode, String error) {
                    Toast.makeText(getBaseContext(), "code query home list error: " + errorCode + "error query home list:" + error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}