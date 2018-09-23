package com.biobirding.biobirding.threads;

import android.os.Handler;
import android.os.Message;

import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.webservice.SpeciesCall;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class SelectAllSpeciesThread extends Thread {

    private Handler handler;

    public SelectAllSpeciesThread(Handler handler){
        this.handler = handler;
    }

    public void run() {

        ArrayList<Species> speciesArrayList;
        try {
            SpeciesCall speciesCall = new SpeciesCall();
            speciesArrayList = speciesCall.selectAll();
            Message message = Message.obtain();
            message.obj = speciesArrayList;
            handler.sendMessage(message);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
