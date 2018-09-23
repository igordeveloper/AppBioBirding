package com.biobirding.biobirding.threads;

import android.os.Handler;
import android.os.Message;
import com.biobirding.biobirding.webservice.SpeciesCall;

public class SearchSpeciesThread extends Thread {

    private Handler handler;
    private String search;

    public SearchSpeciesThread( Handler handler, String search){
        this.handler = handler;
        this.search = search;
    }

    public void run() {

        SpeciesCall speciesCall = new SpeciesCall();
        Object response;
        Message message = Message.obtain();
        try {
            response = speciesCall.search(this.search);
            message.obj = response;
        } catch (InterruptedException ex) {
            response = ex.getMessage();
            message.obj = response;
        }
        handler.sendMessage(message);
    }
}
