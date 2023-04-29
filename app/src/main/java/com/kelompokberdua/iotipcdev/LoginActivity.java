package com.kelompokberdua.iotipcdev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kelompokberdua.iotipcdev.homemanagement.HomeManagementActivity;
import com.thingclips.smart.android.user.api.IBooleanCallback;
import com.thingclips.smart.android.user.api.ILoginCallback;
import com.thingclips.smart.android.user.api.IRegisterCallback;
import com.thingclips.smart.android.user.bean.User;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.bean.HomeBean;
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback;
import com.thingclips.smart.sdk.api.IResultCallback;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText emailEdt;
    EditText passwordEdt;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdt = findViewById(R.id.id_email);
        passwordEdt = findViewById(R.id.id_password);
        loginBtn = findViewById(R.id.btn_login);

        Context context = getBaseContext();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enables login to the app with the email address and password.
                ThingHomeSdk.getUserInstance().loginWithEmail("62",
                        emailEdt.getText().toString(),
                        passwordEdt.getText().toString(),
//                        "iniyasin7@gmail.com",
//                        "Yasin12345!",
                        new ILoginCallback() {
                    @Override
                    public void onSuccess(User user) {
                        Toast.makeText(context, "Logged in with Username: " + user.getUsername(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, HomeManagementActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(context, "code: " + code + "error:" + error, Toast.LENGTH_LONG).show();
                        Log.d("code: " + code, "error:" + error);
                    }
                });
//                List<String> rooms = new ArrayList<String>();
//                rooms.add("Rooms 1");
//                ThingHomeSdk.getHomeManagerInstance().createHome("Home test", -6.146446786441359, 105.85596507124647, "Rokan", rooms, new IThingHomeResultCallback() {
//                    @Override
//                    public void onSuccess(HomeBean bean) {
//                        Toast.makeText(context, "Added Home Success", Toast.LENGTH_LONG).show();
//                        Log.d("Home: ", bean.getName());
//                    }
//                    @Override
//                    public void onError(String errorCode, String errorMsg) {
//                        Toast.makeText(context, "code: " + errorCode + "error:" + errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                });

//                sendVerification(context);
//                registerEmail(context);
//                ThingHomeSdk.getUserInstance().touristBindWithUserName("86", "yasin","20", "yasin", new IBooleanCallback() {
//
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(context, "Logged in with Username: ", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onError(String code, String error) {
//                        Toast.makeText(context, "code: " + code + "error:" + error, Toast.LENGTH_LONG).show();
//                        Log.d("code: " + code, "error:" + error);
//                    }
//                });

            }
        });
    }
}