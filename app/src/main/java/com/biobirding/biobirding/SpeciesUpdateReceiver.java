package com.biobirding.biobirding;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.biobirding.biobirding.activity.MainActivity;
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
    private Context context;

    @Override
    public void onReceive(final Context context, Intent intent) {

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

                                        Integer total = localSpeciesArrayList.size();
                                        Integer count = 0;

                                        PackageManager pm = context.getPackageManager();
                                        pm.setComponentEnabledSetting(new ComponentName(context, MainActivity.class),
                                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                                        database.localSpeciesDao().deleteAll();
                                        database.lastUpdateDao().deleteAll();

                                        for (LocalSpecies l : localSpeciesArrayList) {
                                            database.localSpeciesDao().insert(l);
                                            count++;
                                        }

                                        if(count.equals(total)){
                                            LastUpdate lastUpdate = new LastUpdate();
                                            Long tsLong = System.currentTimeMillis() / 1000;
                                            lastUpdate.setTimestamp(tsLong);
                                            database.lastUpdateDao().insert(lastUpdate);
                                        }

                                        pm.setComponentEnabledSetting(new ComponentName(context, MainActivity.class),
                                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

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
