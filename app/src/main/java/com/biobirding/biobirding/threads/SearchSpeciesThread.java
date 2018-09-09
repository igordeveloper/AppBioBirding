package com.biobirding.biobirding.threads;

import android.os.Handler;
import android.os.Message;

import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.webservice.SpeciesCall;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

public class SearchSpeciesThread extends Thread {

    private Handler handler;
    private String search;

    public SearchSpeciesThread( Handler handler, String search){
        this.handler = handler;
        this.search = search;
    }

    public void run() {

        ArrayList<Species> speciesArrayList = null;
        try {
            SpeciesCall speciesCall = new SpeciesCall();
            speciesArrayList = speciesCall.search(this.search);
            Message message = Message.obtain();
            message.obj = speciesArrayList;
            handler.sendMessage(message);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
