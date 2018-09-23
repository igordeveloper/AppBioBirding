package com.biobirding.biobirding.activity;
import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;

import java.util.List;

public class NewRegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() !=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "BioBirding").build();
                List<PopularName> popularNames = database.popularNameDao().getAll();

                int c = 0;
                for (PopularName p : popularNames) {
                    Species species = database.speciesDao().get(p.getId());
                    Log.d("Name", ++c + " - "  + p.getName() + " - "  +species.getScientificName());
                }
            }
        }){

        }.start();


    }
}
