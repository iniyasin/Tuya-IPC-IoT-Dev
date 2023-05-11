package com.kelompokberdua.iotipcdev.feature;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kelompokberdua.iotipcdev.R;
import com.kelompokberdua.iotipcdev.data.Preferences;
import com.thingclips.smart.android.user.api.IRegisterCallback;
import com.thingclips.smart.android.user.bean.User;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.sdk.api.IResultCallback;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEdt;
    EditText passwordEdt;
    EditText codeVerificationEdt;

    Button btnGetVerification;
    Button btnRegister;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEdt = findViewById(R.id.id_email);
        passwordEdt = findViewById(R.id.id_password);
        codeVerificationEdt = findViewById(R.id.id_verification_code);

        btnGetVerification = (Button) findViewById(R.id.btn_get_code);
        btnRegister = (Button) findViewById(R.id.btn_register);

        toolbar = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        btnGetVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerification(RegisterActivity.this);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerEmail(RegisterActivity.this);
            }
        });
    }

    void sendVerification(Context context) {
        // Returns a verification code to an email address.
        ThingHomeSdk.getUserInstance().sendVerifyCodeWithUserName(emailEdt.getText().toString(), "", "62", 1, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                Toast.makeText(context, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(context, "Verification code returned successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void registerEmail(Context context) {

        // Registers an account with an email address and a password.
        ThingHomeSdk.getUserInstance().registerAccountWithEmail("62",
                emailEdt.getText().toString(),
                passwordEdt.getText().toString(),
                codeVerificationEdt.getText().toString(),
                new IRegisterCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(context, "Registered successfully.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(context, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}