package com.biobirding.biobirding.activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.LastUpdate;
import com.biobirding.biobirding.helper.CustomSnackBar;
import com.biobirding.biobirding.threads.SynchronizeSpeciesThread;

import java.util.Date;

public class UpdateSpeciesActivity extends AppCompatActivity {

    protected Button update;
    protected ProgressBar progressBar;

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


                        Handler handler = new Handler(new Handler.Callback() {
                            @Override
                            @SuppressWarnings("unchecked")
                            public boolean handleMessage(Message msg) {
                                boolean species = (boolean) msg.obj;
                                if(species){

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "BioBirding").build();
                                            LastUpdate lastUpdate = new LastUpdate();
                                            lastUpdate.setTimestamp(new Date().getTime());
                                            database.lastUpdateDao().insert(lastUpdate);
                                            startActivity(new Intent(UpdateSpeciesActivity.this, MainActivity.class));
                                        }
                                    }).start();

                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    update.setEnabled(true);
                                }
                                return true;
                            }
                        });

                        SynchronizeSpeciesThread selectAllSpeciesThread = new SynchronizeSpeciesThread(handler, getApplicationContext());
                        selectAllSpeciesThread.start();

                    }else{
                        CustomSnackBar.make(view.getRootView(), getResources().getString(R.string.without_connection));

                    }
                }

            }
        });
    }
}
