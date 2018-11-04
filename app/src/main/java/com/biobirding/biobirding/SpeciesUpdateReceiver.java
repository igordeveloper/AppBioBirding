package com.biobirding.biobirding;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.LastUpdate;
import com.biobirding.biobirding.entity.LocalSpecies;
import com.biobirding.biobirding.webservice.LogCall;
import com.biobirding.biobirding.webservice.PopularNameCall;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class SpeciesUpdateReceiver extends BroadcastReceiver {

    private Long lastLocalUpdate;
    private AppDatabase database;
    private ConnectivityManager cm;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("----->----", "backup");

        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        database = Room.databaseBuilder(context, AppDatabase.class, "BioBirding").build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                lastLocalUpdate = database.lastUpdateDao().getAll();
                if(lastLocalUpdate != null) {
                    try {
                        LogCall logCall = new LogCall();
                        Long lastSpeciesUpdate = logCall.lastSpeciesUpdate();
                        if (lastLocalUpdate < lastSpeciesUpdate && lastLocalUpdate > 0L) {

                            if (cm != null) {

                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                                if (isConnected) {

                                    PopularNameCall popularNameCall = new PopularNameCall();
                                    ArrayList<LocalSpecies> localSpeciesArrayList;
                                    localSpeciesArrayList = popularNameCall.selectAll();

                                    if(localSpeciesArrayList.size() > 0){
                                        database.localSpeciesDao().deleteAll();
                                        database.lastUpdateDao().deleteAll();

                                        for (LocalSpecies l : localSpeciesArrayList) {
                                            database.localSpeciesDao().insert(l);
                                        }

                                        LastUpdate lastUpdate = new LastUpdate();
                                        Long tsLong = System.currentTimeMillis() / 1000;
                                        lastUpdate.setTimestamp(tsLong);
                                        database.lastUpdateDao().insert(lastUpdate);
                                    }
                                }
                            }
                        }

                    } catch (InterruptedException | IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }
}
