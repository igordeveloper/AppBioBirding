package com.biobirding.biobirding.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.webservice.UserCall;

import org.json.JSONException;

import java.io.IOException;

public class RecoverPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Context context;
    private Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        this.context = this;

        this.email = findViewById(R.id.email);

        Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.getContext();

                new Thread(new Runnable() {

                    String exception = null;
                    Boolean response = false;

                    @Override
                    public void run() {
                        UserCall userCall = new UserCall();
                        try {
                            response = userCall.recoverPassword(email.getText().toString());
                        } catch (IOException | JSONException | InterruptedException e) {
                            exception = e.getMessage();
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                if(exception == null){
                                    if(response){
                                        alert.setMessage(R.string.send_email);
                                    }else{
                                        alert.setMessage(R.string.not_found_email);
                                    }
                                }else{
                                    alert.setMessage(exception);
                                }

                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(response){
                                            startActivity(new Intent(RecoverPasswordActivity.this, LoginActivity.class));
                                        }
                                    }
                                });

                                alert.show();
                            }
                        });
                    }
                }).start();

            }
        });
    }
}
