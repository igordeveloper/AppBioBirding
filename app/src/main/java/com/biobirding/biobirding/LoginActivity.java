package com.biobirding.biobirding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import utils.HashPassword;
import webservice.Login;

public class TempLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_activity_login);

        EditText nickname = findViewById(R.id.nickname);
        nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clearMessage();
            }
        });

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){

                    String response = new String();
                    Login login = new Login();
                    EditText nickname = findViewById(R.id.nickname);
                    EditText password = findViewById(R.id.password);
                    String pass = password.getText().toString();
                    HashPassword hash = new HashPassword();

                    public void run(){
                        try {
                            JSONObject json = login.check(nickname.getText().toString(), hash.encode256(pass));
                            response = json.getString("response");

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(response == "true"){
                                    SharedPreferences sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("nickname_bio", nickname.getText().toString());
                                    editor.putString("password_bio", pass);
                                    editor.putBoolean("authenticate_bio", true);
                                    editor.commit();
                                    startActivity(new Intent(TempLoginActivity.this, TempMainActivity.class));
                                }else{
                                    notAuthorized();
                                }
                            }
                        });
                    }
                }.start();
            }
        });
    }

    public void clearMessage(){
        TextView textInvalid =  findViewById(R.id.invalid);
        textInvalid.setVisibility(View.GONE);
    }

    public void notAuthorized(){
        EditText nickname = findViewById(R.id.nickname);
        EditText password = findViewById(R.id.password);
        nickname.setText(null);
        password.setText(null);
        TextView textInvalid =  findViewById(R.id.invalid);
        textInvalid.setVisibility(View.VISIBLE);
    }
}