package com.biobirding.biobirding.threads;

import android.os.Handler;
import android.os.Message;

import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.webservice.PopularNameCall;
import com.biobirding.biobirding.webservice.SpeciesCall;

public class InsertPopularNameThread extends Thread {

    private Handler handler;
    private PopularName popularName;

    public InsertPopularNameThread(Handler handler, PopularName popularName){
        this.handler = handler;
        this.popularName = popularName;
    }

    public void run() {
        PopularNameCall popularNameCall = new PopularNameCall();
        Boolean response;
        Message message = Message.obtain();
        try {
            response = popularNameCall.insert(popularName);
            message.obj = response;
        } catch (InterruptedException e) {
            message.obj = e.getMessage();
            e.printStackTrace();
        }
        handler.sendMessage(message);
    }
}
