package com.biobirding.biobirding.threads;

import android.os.Handler;
import android.os.Message;

import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.webservice.SpeciesCall;

public class InsertSpecieThread extends Thread {

    private Handler handler;
    private Species species;

    public InsertSpecieThread( Handler handler, Species species){
        this.handler = handler;
        this.species = species;
    }

    public void run() {
        SpeciesCall speciesCall = new SpeciesCall();
        Boolean response;
        Message message = Message.obtain();
        try {
            response = speciesCall.insert(this.species);
            message.obj = response;
        } catch (InterruptedException e) {
            message.obj = e.getMessage();
            e.printStackTrace();
        }
        handler.sendMessage(message);
    }
}
