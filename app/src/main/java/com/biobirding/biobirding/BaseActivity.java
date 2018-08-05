package com.biobirding.biobirding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TempBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
        boolean authenticate = sharedPref.getBoolean("authenticate_bio", false);

        Log.d("log", "check autenticado");
        if(!authenticate){
            startActivity(new Intent(TempBaseActivity.this, TempLoginActivity.class));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about:
                startActivity(new Intent(TempBaseActivity.this, TempScrollingActivity.class));
                return true;
            case R.id.editAccount:
                startActivity(new Intent(TempBaseActivity.this, TempLoginActivity.class));
                return true;
            case R.id.loggof:
                SharedPreferences sharedPref = getSharedPreferences("bio", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(TempBaseActivity.this, TempLoginActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
