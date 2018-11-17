package com.biobirding.biobirding.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.fragments.ListOfPopularNamesFragment;
import com.biobirding.biobirding.fragments.ListUsersFragment;

public class UserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() !=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ListUsersFragment listUsersFragment = new ListUsersFragment();
            fragmentTransaction.replace(R.id.fragment_container, listUsersFragment);
            fragmentTransaction.commit();
        }

    }
}
