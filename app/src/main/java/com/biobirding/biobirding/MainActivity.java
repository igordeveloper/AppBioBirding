package com.biobirding.biobirding;

import android.arch.persistence.room.Room;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.biobirding.biobirding.dao.SpeciesDao;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;

import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button buttonSpecies = findViewById(R.id.buttonSpecies);
        buttonSpecies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SpeciesActivity.class));
                return;
            }
        });




        new Thread(){
            public void run(){
                /*Log.d("thread", "--------------------------------thread");
                AppDatabase dbb = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "Species").build();

                Species s = new Species();
                s.setScientificName("Tangara Cayana");
                s.setCharacteristics("teste");
                dbb.speciesDao().insert(s);

                List<Species> species = dbb.speciesDao().getAll();
                List<PopularName> names = dbb.popularNameDao().getAll();

                for (Species sp : species) {
                    Log.d("specie/:", sp.getScientificName());
                }*/
            }
        }.start();




    }
}
