package com.biobirding.biobirding;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.helper.CustomSimpleDialog;
import com.biobirding.biobirding.helper.CustomSnackBar;

public class MainActivity extends BaseActivity {

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

                //CustomSimpleDialog alert = new CustomSimpleDialog(view.getContext(), "123");
                //alert.show();

                CustomSnackBar.make(view, "123");
                //startActivity(new Intent(MainActivity.this, SpeciesActivity.class));

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




        new Thread(){
            public void run(){
                /*Log.d("thread", "--------------------------------thread");
                AppDatabase dbb = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "Species").build();

                Species s = new Species();
                s.setId(999);
                s.setScientificName("tangara cayana");
                s.setNotes("dasdas");
                s.setConservationState("bom");

                dbb.speciesDao().insert(s);*/

                /*for (Species sp : species) {
                    Log.d("specie/:", sp.getScientificName());
                }*/
            }
        }.start();




    }
}
