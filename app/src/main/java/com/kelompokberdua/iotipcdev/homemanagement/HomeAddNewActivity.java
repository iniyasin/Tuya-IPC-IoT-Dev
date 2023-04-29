package com.kelompokberdua.iotipcdev.homemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kelompokberdua.iotipcdev.R;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.bean.HomeBean;
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback;

import java.util.ArrayList;

public class HomeAddNewActivity extends AppCompatActivity {
    EditText homeNameEdt;
    EditText cityEdt;
    Button btnAddNewHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_add_new);

        homeNameEdt = findViewById(R.id.id_home_name);
        cityEdt = findViewById(R.id.id_city);
        btnAddNewHome = findViewById(R.id.btn_add_home);

        btnAddNewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThingHomeSdk.getHomeManagerInstance().createHome(homeNameEdt.getText().toString(),
                        0,
                        0,
                        cityEdt.getText().toString(),
                        new ArrayList<>(),
                        new IThingHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
                        Toast.makeText(getBaseContext(), "Add new home success: " + bean.getName(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(getBaseContext(), "code: " + errorCode + "error:" + errorMsg, Toast.LENGTH_LONG).show();
                        Log.d("code: " + errorCode, "error:" + errorMsg);
                    }
                });
            }
        });
    }
}