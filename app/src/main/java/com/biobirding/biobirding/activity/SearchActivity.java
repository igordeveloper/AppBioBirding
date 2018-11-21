package com.biobirding.biobirding.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.DatePicker;
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.fragments.FilterSearchFragment;

public class SearchActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FilterSearchFragment fragment = new FilterSearchFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        FragmentManager fm = getSupportFragmentManager();
        FilterSearchFragment fragment = (FilterSearchFragment)fm.findFragmentById(R.id.fragment_container);
        fragment.updateDate(year, month, dayOfMonth  );

    }
}
