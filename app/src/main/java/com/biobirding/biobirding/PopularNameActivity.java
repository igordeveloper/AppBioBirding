package com.biobirding.biobirding;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.biobirding.biobirding.entity.Species;

public class PopularNameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_name);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        Intent intent = getIntent();
        if(intent.getExtras() != null){
            Species species = (Species) intent.getExtras().getSerializable("species");


            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ListOfPopularNamesFragment listOfPopularNamesFragment = new ListOfPopularNamesFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("species", species);
                listOfPopularNamesFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, listOfPopularNamesFragment);
                fragmentTransaction.commit();
            }
        }

    }
}
