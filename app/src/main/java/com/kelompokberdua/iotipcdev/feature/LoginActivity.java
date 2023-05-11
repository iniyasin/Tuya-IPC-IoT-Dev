package com.kelompokberdua.iotipcdev.feature;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kelompokberdua.iotipcdev.R;
import com.kelompokberdua.iotipcdev.data.Preferences;
import com.kelompokberdua.iotipcdev.homemanagement.HomeManagementActivity;
import com.thingclips.smart.android.user.api.ILoginCallback;
import com.thingclips.smart.android.user.bean.User;
import com.thingclips.smart.home.sdk.ThingHomeSdk;

public class LoginActivity extends AppCompatActivity {
    EditText emailEdt;
    EditText passwordEdt;
    Button loginBtn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdt = findViewById(R.id.id_email);
        passwordEdt = findViewById(R.id.id_password);
        loginBtn = findViewById(R.id.btn_login);

        Context context = getBaseContext();

        toolbar = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

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
                        Preferences.setIsLoggedUser(context, true);
                        Intent intent = new Intent(LoginActivity.this, HomeManagementActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(context, "code: " + code + "error:" + error, Toast.LENGTH_LONG).show();
                        Log.d("code: " + code, "error:" + error);
                    }
                });
            }
        });
    }
}