package com.biobirding.biobirding.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.User;
import com.biobirding.biobirding.helper.HashPassword;
import com.biobirding.biobirding.webservice.UserCall;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private EditText nickname;
    private EditText password;
    private User user;

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

                new Thread(new Runnable() {

                    UserCall userCall = new UserCall();
                    String hashPassword;


                    @Override
                    public void run() {
                        try {
                            hashPassword = HashPassword.encode256(password.getText().toString());
                            user = userCall.validate(nickname.getText().toString(), hashPassword);
                        } catch (IOException | JSONException | InterruptedException | NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(user != null){
                                    SharedPreferences sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("nickname_bio", user.getNickname());
                                    editor.putString("password_bio", hashPassword);
                                    editor.putString("rg_bio", user.getRg());
                                    editor.putBoolean("authenticate_bio", true);
                                    editor.putInt("access_level", user.getAccessLevel());
                                    editor.apply();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }else{
                                    notAuthorized();
                                }
                            }
                        });

                    }

                }).start();
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