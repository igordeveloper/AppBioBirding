package com.biobirding.biobirding;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biobirding.biobirding.entity.User;
import com.biobirding.biobirding.helper.CustomSimpleDialog;
import com.biobirding.biobirding.threads.LoginThread;
import com.biobirding.biobirding.threads.NewPasswordThread;
import com.biobirding.biobirding.utils.HashPassword;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class NewPasswordActivity extends AppCompatActivity {

    private EditText email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        this.email = findViewById(R.id.email);


        Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler(new Handler.Callback() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public boolean handleMessage(Message msg) {
                    String message = (String)msg.obj;
                    CustomSimpleDialog alert = new CustomSimpleDialog(getResources().getString(R.string.send_email), NewPasswordActivity.this);
                    alert.show();
                    return true;
                    }
                });

                User user = new User();
                user.setEmail(email.getText().toString());
                NewPasswordThread newPasswordThread = new NewPasswordThread(handler, user);
                newPasswordThread.start();


            }
        });

    }



}
