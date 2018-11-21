package com.biobirding.biobirding.activity;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.helper.CustomSnackBar;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends BaseActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() !=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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

        /*Search button*/
        Button buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });

        /*Report button*/
        Button reportButton = findViewById(R.id.buttonReport);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ReportActivity.class));
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
                Long lastUpdateList = database.lastUpdateDao().getAll();
                if(lastUpdateList == null){
                    startActivity(new Intent(MainActivity.this, UpdateSpeciesActivity.class));
                }else{
                    requestPermission();
                }
            }
        }).start();
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1 );
    }
}
