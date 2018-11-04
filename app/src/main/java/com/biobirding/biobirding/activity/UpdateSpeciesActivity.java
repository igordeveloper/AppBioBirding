package com.biobirding.biobirding.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.LastUpdate;
import com.biobirding.biobirding.entity.LocalSpecies;
import com.biobirding.biobirding.helper.CustomSnackBar;
import com.biobirding.biobirding.SpeciesUpdateReceiver;
import com.biobirding.biobirding.webservice.PopularNameCall;


import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class UpdateSpeciesActivity extends AppCompatActivity {

    private Button update;
    private ProgressBar progressBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_species);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                if(cm != null){
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if(isConnected){

                        update.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);

                        new Thread(new Runnable() {

                            AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "BioBirding").build();

                            ArrayList<LocalSpecies> localSpeciesArrayList;
                            Boolean response = false;

                            @Override
                            public void run() {
                                database.lastUpdateDao().deleteAll();
                                PopularNameCall popularNameCall = new PopularNameCall();

                                try {

                                    localSpeciesArrayList = popularNameCall.selectAll();
                                    database.localSpeciesDao().deleteAll();

                                    int c = 0;
                                    for (LocalSpecies l : localSpeciesArrayList) {
                                        database.localSpeciesDao().insert(l);
                                        c++;
                                    }

                                    if(c == localSpeciesArrayList.size()){

                                        LastUpdate lastUpdate = new LastUpdate();
                                        Long tsLong = System.currentTimeMillis()/1000;
                                        lastUpdate.setTimestamp(tsLong);
                                        database.lastUpdateDao().insert(lastUpdate);
                                        response = true;

                                    }else{
                                        database.localSpeciesDao().deleteAll();
                                    }
                                } catch (InterruptedException | IOException | JSONException e) {
                                    e.printStackTrace();
                                }


                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(response){

                                            AlarmManager alarmMgr = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                                            Intent speciesIntent = new Intent(getApplicationContext(), SpeciesUpdateReceiver.class);
                                            PendingIntent alarmSpeciesIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, speciesIntent, 0);
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTimeInMillis(System.currentTimeMillis());
                                            calendar.set(Calendar.HOUR_OF_DAY, new Random().nextInt(23));
                                            calendar.set(Calendar.MINUTE, new Random().nextInt(59));
                                            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                                    AlarmManager.INTERVAL_DAY, alarmSpeciesIntent);

                                            startActivity(new Intent(UpdateSpeciesActivity.this, MainActivity.class));
                                        }else{
                                            progressBar.setVisibility(View.GONE);
                                            update.setEnabled(true);
                                        }

                                    }
                                });

                            }
                        }).start();
                    }else{
                        CustomSnackBar.make(view.getRootView(), getResources().getString(R.string.without_connection));
                    }
                }
            }
        });
    }
}
