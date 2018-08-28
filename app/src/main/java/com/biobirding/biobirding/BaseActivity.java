package com.biobirding.biobirding;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

    protected int accessLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
        boolean authenticate = sharedPref.getBoolean("authenticate_bio", false);
        this.accessLevel = sharedPref.getInt("access_level", 0);

        if(!authenticate){
            startActivity(new Intent(BaseActivity.this, LoginActivity.class));
        }

        ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(null, null, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        this.setTaskDescription(taskDesc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about:
                startActivity(new Intent(BaseActivity.this, TempScrollingActivity.class));
                return true;

            case R.id.editAccount:
                startActivity(new Intent(BaseActivity.this, TempGpsActivity.class));
                return true;

            case R.id.loggof:
                startActivity(new Intent(BaseActivity.this, LogoffActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
