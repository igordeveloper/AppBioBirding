package com.biobirding.biobirding.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.User;
import com.biobirding.biobirding.threads.RecoverPasswordThread;

public class RecoverPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Boolean response;

    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        this.email = findViewById(R.id.email);

        Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.getContext();

                Handler handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                    response = (Boolean) msg.obj;
                    AlertDialog.Builder alert = new AlertDialog.Builder(RecoverPasswordActivity.this);

                    if(response){
                        alert.setMessage(R.string.send_email);
                    }else{
                        alert.setMessage(R.string.not_found_email);
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
                    return true;
                    }
                });

                User user = new User();
                user.setEmail(email.getText().toString());
                RecoverPasswordThread newPasswordThread = new RecoverPasswordThread(handler, user);
                newPasswordThread.start();

            }
        });

    }
}
