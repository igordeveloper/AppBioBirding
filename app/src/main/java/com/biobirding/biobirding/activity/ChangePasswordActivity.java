package com.biobirding.biobirding.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.User;
import com.biobirding.biobirding.helper.HashPassword;
import com.biobirding.biobirding.webservice.LoginCall;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class ChangePasswordActivity extends BaseActivity {

    private Button changePassword;
    private EditText password;
    private EditText replyPassword;
    private SharedPreferences sharedPref;
    private User user;
    private String newPassword;
    private Context context;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() !=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        this.context = this;

        this.password = findViewById(R.id.password);
        this.replyPassword = findViewById(R.id.reply_password);

        this.changePassword = findViewById(R.id.save);
        this.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword.setEnabled(false);

                if(validateFields()){
                    try {

                        sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
                        String rg = sharedPref.getString("rg_bio", "");
                        String password = sharedPref.getString("password_bio", "");
                        String nickname = sharedPref.getString("nickname_bio", "");

                        user = new User();
                        user.setRg(rg);
                        user.setPassword(password);
                        user.setNickname(nickname);


                        newPassword = HashPassword.encode256(replyPassword.getText().toString());

                        new Thread(new Runnable() {

                            String exception = null;
                            Boolean response = false;

                            @Override
                            public void run() {

                                    LoginCall loginCall = new LoginCall();
                                try {
                                    response = loginCall.updatePassword(user, newPassword);
                                } catch (IOException | JSONException | InterruptedException e) {
                                    exception = e.getMessage();
                                }

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                        if(exception == null){
                                            if(response){
                                                alert.setMessage(R.string.change_password);
                                            }else{
                                                alert.setMessage(R.string.fail);
                                                changePassword.setEnabled(true);
                                            }
                                        }else{
                                            alert.setMessage(exception);
                                            changePassword.setEnabled(true);
                                        }

                                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(response){
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.putString("password_bio", newPassword);
                                                    editor.apply();
                                                    startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                                                }
                                            }
                                        });

                                        alert.show();

                                    }
                                });

                            }
                        }).start();

                    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }else{
                    changePassword.setEnabled(true);
                }
            }
        });

    }

    public boolean validateFields(){
        if(TextUtils.isEmpty(this.password.getText().toString())
            || this.password.getText().length() < 6 ){
            this.password.setError(getString(R.string.requiredPasword));
            return false;
        }

        if(TextUtils.isEmpty(this.replyPassword.getText().toString())
                || this.password.getText().length() < 6 ){
            this.replyPassword.setError(getString(R.string.requiredPasword));
            return false;
        }

        if(!this.replyPassword.getText().toString().equals(this.password.getText().toString())){
            this.password.setError(getString(R.string.differentPassword));
            this.replyPassword.setError(getString(R.string.differentPassword));
            return false;
        }

        return true;
    }
}




