package com.biobirding.biobirding.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.biobirding.biobirding.BackupReceiver;
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.LastUpdate;
import com.biobirding.biobirding.helper.CustomSnackBar;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends BaseActivity {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermission();

        if(getSupportActionBar() !=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        alarmMgr = (AlarmManager)getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), BackupReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, new Random().nextInt(23));
        calendar.set(Calendar.MINUTE, new Random().nextInt(59));


        /*New register button*/
        Button registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, CatalogActivity.class));


            }
        });
        /*Species button*/
        Button speciesButton = findViewById(R.id.buttonSpecies);
        speciesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(accessLevel <= 2){
                    startActivity(new Intent(MainActivity.this, SpeciesActivity.class));
                }else{
                    CustomSnackBar.make(view, view.getContext().getString(R.string.unauthorized_access));
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Check if app is synchronized with webservice*/
        new Thread ( new Runnable() {
            @Override
            public void run() {
                AppDatabase database = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "BioBirding").build();
                List<LastUpdate> lastUpdateList = database.lastUpdateDao().getAll();
                if(lastUpdateList.size() == 0){
                    startActivity(new Intent(MainActivity.this, UpdateSpeciesActivity.class));
                }
            }
        }).start();
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1 );
    }
}
