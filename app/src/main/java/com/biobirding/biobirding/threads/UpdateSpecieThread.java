package com.biobirding.biobirding.threads;

import android.os.Handler;
import android.os.Message;

import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.webservice.SpeciesCall;

import org.json.JSONException;

import java.io.IOException;

public class UpdateSpecieThread extends Thread {
    private Handler handler;
    private Species species;

    public UpdateSpecieThread( Handler handler, Species species){
        this.handler = handler;
        this.species = species;
    }

    public void run() {

        try {
            SpeciesCall speciesCall = new SpeciesCall();
            String response = speciesCall.update(this.species);
            Message message = Message.obtain();
            message.obj = response;
            handler.sendMessage(message);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
