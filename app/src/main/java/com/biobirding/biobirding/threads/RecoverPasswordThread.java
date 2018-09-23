package com.biobirding.biobirding.threads;

import android.os.Handler;
import android.os.Message;

import com.biobirding.biobirding.entity.User;
import com.biobirding.biobirding.webservice.LoginCall;

import org.json.JSONException;

import java.io.IOException;

public class NewPasswordThread extends Thread {
    private Handler handler;
    private User user;

    public NewPasswordThread(Handler handler, User user){
        this.handler = handler;
        this.user = user;
    }

    public void run() {
        try {
            LoginCall loginCall = new LoginCall();
            String response = loginCall.newPassword(this.user);
            Message message = Message.obtain();
            message.obj = response;
            handler.sendMessage(message);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
