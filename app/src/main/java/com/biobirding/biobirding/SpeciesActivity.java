package com.biobirding.biobirding;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TempSpeciesActivity extends BaseActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_activity_species);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listView = (ListView) findViewById(R.id.speciesList);

        String[] listItwms = new String[]{"ListView Example", "ListView with FAB", "FAB with Simple List View in Android", "ListView Adapter with Floating Action Button",
                "Android FAB and ListView Example", "List View and FAB Source Code", "FAB and List View Array", "Floating Action Button FAB", "ListView Example",
                "ListView with FAB", "FAB with Simple List View in Android", "ListView Adapter with Floating Action Button",
                "Android FAB and ListView Example", "List View and FAB Source Code", "FAB and List View Array"
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItwms);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
