package com.biobirding.biobirding.activity;

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
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.helper.CustomSnackBar;
import com.biobirding.biobirding.webservice.PopularNameCall;
import com.biobirding.biobirding.webservice.SpeciesCall;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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
                            ArrayList<Species> speciesArrayList;
                            ArrayList<PopularName> popularNameArrayList;
                            Boolean response = false;

                            @Override
                            public void run() {

                                SpeciesCall speciesCall = new SpeciesCall();
                                PopularNameCall popularNameCall = new PopularNameCall();

                                try {
                                    speciesArrayList = speciesCall.selectAll();
                                    int c = 0;
                                    for (Species s: speciesArrayList) {
                                        database.speciesDao().insert(s);
                                        c++;
                                    }

                                    if(c == speciesArrayList.size()){
                                        popularNameArrayList = popularNameCall.selectAll();
                                        c = 0;
                                        for (PopularName p : popularNameArrayList) {
                                            database.popularNameDao().insert(p);
                                            c++;
                                        }
                                        if(c == popularNameArrayList.size()){
                                            LastUpdate lastUpdate = new LastUpdate();
                                            lastUpdate.setTimestamp(new Date().getTime());
                                            database.lastUpdateDao().insert(lastUpdate);
                                            response = true;
                                        }else{
                                            database.speciesDao().deleteAll();
                                            database.popularNameDao().deleteAll();
                                        }
                                    }else{
                                        database.speciesDao().deleteAll();
                                    }
                                } catch (InterruptedException | IOException | JSONException e) {
                                    e.printStackTrace();
                                }


                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        if(response){
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
