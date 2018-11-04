package com.biobirding.biobirding.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.biobirding.biobirding.CatalogReceiver;
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.Catalog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertCatalogWithoutGpsFragment extends Fragment {

    private Handler handler = new Handler();
    private Button insert;
    private EditText latitude;
    private EditText longitude;
    private Catalog catalog;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_catalog_without_gps, container, false);
        this.insert = view.findViewById(R.id.insert);
        this.latitude = view.findViewById(R.id.latitude);
        this.longitude = view.findViewById(R.id.longitude);

        this.context = getContext();

        if(getArguments() != null){
            Bundle bundle = getArguments();
            this.catalog = (Catalog) bundle.getSerializable("catalog");
        }

        this.insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()){
                    insert.setEnabled(false);
                    Log.d("dasda", latitude.getText().toString());
                    catalog.setLatitude(Double.parseDouble(latitude.getText().toString()));
                    catalog.setLongitude(Double.parseDouble(longitude.getText().toString()));
                    new Thread(new Runnable() {

                        String exception = null;
                        Boolean response = false;

                        @Override
                        public void run() {

                            AppDatabase database = Room.databaseBuilder(context,
                                    AppDatabase.class, "BioBirding").build();
                            database.catalogDao().insert(catalog);
                            response = true;

                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                    if(exception == null){
                                        if(response){
                                            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                            Intent catalogIntent = new Intent(context, CatalogReceiver.class);
                                            PendingIntent alarmCatalogIntent = PendingIntent.getBroadcast(context, 0, catalogIntent, 0);
                                            if(alarmMgr != null){
                                                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                                                        60000, alarmCatalogIntent);
                                                alert.setMessage(R.string.insert_species);
                                            }
                                        }else{
                                            alert.setMessage(R.string.fail);
                                        }
                                    }else{
                                        alert.setMessage(exception);
                                    }

                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(response){
                                                redirectActivity();
                                            }
                                        }
                                    });

                                    alert.show();
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        return view;
    }

    public void redirectActivity(){
        if(getFragmentManager() != null) {
            SelectLocalSpeciesFragment selectLocalSpeciesFragment = new SelectLocalSpeciesFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, selectLocalSpeciesFragment);
            transaction.commit();
        }
    }

    public boolean validateFields(){
        if(TextUtils.isEmpty(latitude.getText().toString())){
            latitude.setError(getString(R.string.requiredText));
            latitude.requestFocus();
            insert.setEnabled(true);
            return false;
        }

        Pattern expressionLatitude = Pattern.compile("^([+\\-])?(?:90(?:(?:\\.0{1,7})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,7})?))$");
        Matcher latitudeMatcher = expressionLatitude.matcher(latitude.getText().toString());

        if(!latitudeMatcher.matches()){
            latitude.setError(getString(R.string.coordinate_error));
            latitude.requestFocus();
            insert.setEnabled(true);
            return false;
        }

        if(TextUtils.isEmpty(longitude.getText().toString())){
            longitude.setError(getString(R.string.requiredText));
            longitude.requestFocus();
            insert.setEnabled(true);
            return false;
        }

        Pattern expressionLongitude = Pattern.compile("^([+\\-])?(?:180(?:(?:\\.0{1,7})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,7})?))$");
        Matcher longitudeMatcher = expressionLongitude.matcher(longitude.getText().toString());

        if(!longitudeMatcher.matches()){
            longitude.setError(getString(R.string.coordinate_error));
            longitude.requestFocus();
            insert.setEnabled(true);
            return false;
        }

        return true;
    }

}