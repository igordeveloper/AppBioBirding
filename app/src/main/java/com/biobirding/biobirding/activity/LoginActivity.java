package com.biobirding.biobirding.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.User;
import com.biobirding.biobirding.threads.LoginThread;
import com.biobirding.biobirding.utils.HashPassword;

public class LoginActivity extends AppCompatActivity {

    private EditText nickname;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
        boolean authenticate = sharedPref.getBoolean("authenticate_bio", false);

        if(authenticate){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        setContentView(R.layout.activity_login);

        this.nickname = findViewById(R.id.nickname);
        this.password = findViewById(R.id.password);

        this.nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clearMessage();
            }
        });

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler(new Handler.Callback() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public boolean handleMessage(Message msg) {
                        User user = (User) msg.obj;

                        if(user.getAccessLevel() > 0){
                            SharedPreferences sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("nickname_bio", user.getNickname());
                            editor.putString("password_bio", user.getPassword());
                            editor.putBoolean("authenticate_bio", true);
                            editor.putInt("access_level", user.getAccessLevel());
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }else{
                            notAuthorized();
                        }

                        return true;
                    }
                });

                try {
                    HashPassword hash = new HashPassword();
                    User user = new User();
                    user.setAccessLevel(0);
                    user.setNickname(nickname.getText().toString());
                    user.setPassword(hash.encode256(password.getText().toString()));
                    LoginThread loginThread = new LoginThread(handler, user);
                    loginThread.start();
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        Button forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                startActivity(i);
            }
        });

    }

    public void clearMessage(){
        TextView textInvalid =  findViewById(R.id.invalid);
        textInvalid.setText(null);
    }

    public void notAuthorized(){
        this.nickname.setText(null);
        this.password.setText(null);
        TextView textInvalid =  findViewById(R.id.invalid);
        textInvalid.setText(R.string.invalid);
    }
}