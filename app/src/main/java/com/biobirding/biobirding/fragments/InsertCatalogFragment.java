package com.biobirding.biobirding.fragments;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.biobirding.biobirding.CatalogReceiver;
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.LocalCatalog;
import com.biobirding.biobirding.entity.LocalSpecies;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class InsertCatalogFragment extends Fragment {

    private LocalSpecies localSpecies;
    private Handler handler = new Handler();
    private Spinner ageSpinner;
    private Spinner sexSpinner;
    private Button insert;
    private Double latitude = 0d;
    private Double longitude = 0d;
    private EditText identificationCode;
    private EditText notes;
    private Context context;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocalCatalog localCatalog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_catalog, container, false);
        this.insert = view.findViewById(R.id.insert);
        this.context = getContext();

        if(this.context != null){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {

                        if(location.getAccuracy() <= 15){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            };
        }

        if(getArguments() != null){
            Bundle bundle = getArguments();
            this.localSpecies = (LocalSpecies) bundle.getSerializable("localSpecies");
        }

        TextView title = view.findViewById(R.id.title);
        String t = localSpecies.getScientificName() + "\n" + localSpecies.getName();
        title.setText(t);

        this.ageSpinner = view.findViewById(R.id.ageList);
        this.sexSpinner = view.findViewById(R.id.sexLIst);

        if (getContext() != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.age, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.ageSpinner.setAdapter(adapter);

            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.sex, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.sexSpinner.setAdapter(adapter);

        }

        identificationCode = view.findViewById(R.id.identificationCode);
        notes = view.findViewById((R.id.notes));


        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insert.setEnabled(false);

                if(latitude != 0d){

                    new Thread(new Runnable() {

                        String exception = null;
                        Boolean response = false;

                        @Override
                        public void run() {

                            SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences("bio", Context.MODE_PRIVATE);
                            String rg = sharedPref.getString("rg_bio", "");

                            localCatalog = new LocalCatalog();
                            localCatalog.setRg(rg);
                            localCatalog.setSex(sexSpinner.getSelectedItem().toString());
                            localCatalog.setAge(ageSpinner.getSelectedItem().toString());
                            localCatalog.setIdentificationCode(identificationCode.getText().toString());
                            localCatalog.setNotes(notes.getText().toString());
                            localCatalog.setLatitude(latitude);
                            localCatalog.setLongitude(longitude);
                            localCatalog.setSpecies(localSpecies.getId());
                            localCatalog.setTimestamp((System.currentTimeMillis()/1000));

                            AppDatabase database = Room.databaseBuilder(context,
                                    AppDatabase.class, "BioBirding").build();
                            database.catalogDao().insert(localCatalog);
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
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage(R.string.no_location);
                    alert.setNeutralButton(R.string.wait, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startLocationUpdates();
                            insert.setEnabled(true);
                        }
                    });
                    alert.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(getFragmentManager() != null) {

                                SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences("bio", Context.MODE_PRIVATE);
                                String rg = sharedPref.getString("rg_bio", "");

                                localCatalog = new LocalCatalog();
                                localCatalog.setRg(rg);
                                localCatalog.setSex(sexSpinner.getSelectedItem().toString());
                                localCatalog.setAge(ageSpinner.getSelectedItem().toString());
                                localCatalog.setIdentificationCode(identificationCode.getText().toString());
                                localCatalog.setNotes(notes.getText().toString());
                                localCatalog.setLatitude(latitude);
                                localCatalog.setLongitude(longitude);
                                localCatalog.setSpecies(localSpecies.getId());
                                localCatalog.setTimestamp((System.currentTimeMillis()/1000));

                                InsertCatalogWithoutGpsFragment insertCatalogWithoutGpsFragment = new InsertCatalogWithoutGpsFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("catalog", localCatalog);
                                insertCatalogWithoutGpsFragment.setArguments(bundle);
                                transaction.replace(R.id.fragment_container, insertCatalogWithoutGpsFragment);
                                transaction.commit();
                            }
                        }
                    });
                    alert.show();
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

    private void requestPermission(){
        if(getActivity() != null){
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1 );
        }
    }

    private void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (getActivity() != null && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }




    @Override
    public void onStart() {
        super.onStart();
        requestPermission();

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null){
            boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!GPSEnabled){
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage(R.string.location_disable);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        redirectActivity();
                    }
                });
                alert.show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }



    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

}