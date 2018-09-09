package com.biobirding.biobirding.threads;

import android.os.Handler;
import android.os.Message;

import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.webservice.SpeciesCall;

import org.json.JSONException;

import java.io.IOException;

public class SelectSpeciesThread extends Thread {
    private Handler handler;
    private Species species;

    public SelectSpeciesThread( Handler handler, Species species){
        this.handler = handler;
        this.species = species;
    }

    public void run() {

        try {
            SpeciesCall speciesCall = new SpeciesCall();
            Species species = speciesCall.select(this.species);
            Message message = Message.obtain();
            message.obj = species;
            handler.sendMessage(message);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
