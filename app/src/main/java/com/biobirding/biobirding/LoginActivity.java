package com.biobirding.biobirding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import utils.HashPassword;
import com.biobirding.biobirding.webservice.Login;

public class LoginActivity extends AppCompatActivity {

    private JSONObject json;
    private String authorized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

                    Login login = new Login();
                    EditText nickname = findViewById(R.id.nickname);
                    EditText password = findViewById(R.id.password);
                    String pass = password.getText().toString();
                    HashPassword hash = new HashPassword();

                    public void run(){
                        try {
                            json = login.check(nickname.getText().toString(), hash.encode256(pass));
                            authorized = json.getString("authorized");
                        } catch (IOException | JSONException | NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("---------", json.toString());

                                if(json.has("authorized") && authorized.equals("true")){
                                    try {
                                        SharedPreferences sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("nickname_bio", nickname.getText().toString());
                                        editor.putString("password_bio", pass);
                                        editor.putBoolean("authenticate_bio", true);
                                        JSONObject userInfo = json.getJSONObject("userInfo");
                                        editor.putInt("access_level", Integer.parseInt(userInfo.getString("accessLevel")));
                                        editor.apply();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
        textInvalid.setText(null);
    }

    public void notAuthorized(){
        EditText nickname = findViewById(R.id.nickname);
        EditText password = findViewById(R.id.password);
        nickname.setText(null);
        password.setText(null);
        TextView textInvalid =  findViewById(R.id.invalid);
        textInvalid.setText(R.string.invalid);
    }
}