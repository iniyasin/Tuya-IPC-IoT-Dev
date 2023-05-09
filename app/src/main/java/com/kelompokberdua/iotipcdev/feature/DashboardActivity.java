package com.kelompokberdua.iotipcdev.feature;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kelompokberdua.iotipcdev.R;
import com.kelompokberdua.iotipcdev.data.Preferences;
import com.kelompokberdua.iotipcdev.homemanagement.HomeManagementActivity;

public class DashboardActivity extends AppCompatActivity {
    Button btnLogin;
    Button btnRegister;
    boolean isLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnLogin = findViewById(R.id.id_login);
        btnRegister = findViewById(R.id.id_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        isLogged = Preferences.getKeyLoginStatus(getBaseContext());

        if (isLogged) {
            Intent intent = new Intent(DashboardActivity.this, HomeManagementActivity.class);
            startActivity(intent);
        }
    }
}