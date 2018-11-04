package com.biobirding.biobirding;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.Catalog;
import com.biobirding.biobirding.webservice.CatalogCall;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class CatalogReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context c, Intent intent) {

        this.context = c;



        new Thread(new Runnable() {
            @Override
            public void run() {

                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(cm != null) {
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if (isConnected) {
                        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "BioBirding").build();
                        List<Catalog> list = database.catalogDao().getAll();

                        Log.d("log------", "backup" + list.size());

                        if(list.size() > 0){
                            CatalogCall catalogCall = new CatalogCall();
                            Boolean response;
                            for (Catalog catalog:list) {
                                try {
                                    response = catalogCall.selectCount(catalog);
                                    Log.d("log------", "response" + response.toString());
                                    if(!response){
                                        catalogCall.insert(catalog);
                                    }else{
                                        database.catalogDao().delete(catalog.getId());
                                    }
                                } catch (InterruptedException | IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }else{
                            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            Intent catalogIntent = new Intent(context, CatalogReceiver.class);
                            PendingIntent catalogSpeciesIntent = PendingIntent.getBroadcast(context, 0, catalogIntent, 0);
                            alarmMgr.cancel(catalogSpeciesIntent);
                        }
                    }
                }
            }
        }).start();
    }

}
