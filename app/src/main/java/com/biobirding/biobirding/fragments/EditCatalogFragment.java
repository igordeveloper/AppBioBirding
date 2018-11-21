package com.biobirding.biobirding.fragments;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.LocalSpecies;
import com.biobirding.biobirding.helper.CatalogHelper;
import com.biobirding.biobirding.helper.SearchHelper;
import com.biobirding.biobirding.webservice.CatalogCall;

import org.json.JSONException;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditCatalogFragment extends Fragment {

    private CatalogHelper catalogHelper;
    private Handler handler = new Handler();
    private Context context;
    private EditText latitude;
    private EditText longitude;
    private EditText temperature;
    private EditText humidity;
    private EditText wind;
    private EditText weather;
    private EditText identificationCode;
    private EditText notes;
    private EditText neighborhood;
    private EditText city;
    private EditText state;
    private EditText scientificName;
    private AppDatabase database;
    private  LocalSpecies localSpecies;
    private Spinner ageSpinner;
    private Spinner sexSpinner;
    private Integer newId;
    private SearchHelper searchHelper;
    private Button update;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_catalog, container, false);

        this.latitude = view.findViewById(R.id.latitude);
        this.longitude = view.findViewById(R.id.longitude);
        this.temperature = view.findViewById(R.id.temperature);
        this.humidity = view.findViewById(R.id.humidity);
        this.wind = view.findViewById(R.id.wind);
        this.weather = view.findViewById(R.id.weather);
        this.identificationCode = view.findViewById(R.id.identificationCode);
        this.notes = view.findViewById(R.id.notes);
        this.neighborhood = view.findViewById(R.id.neighborhood);
        this.city = view.findViewById(R.id.city);
        this.state = view.findViewById(R.id.state);
        this.scientificName = view.findViewById(R.id.scientificName);
        this.ageSpinner = view.findViewById(R.id.ageList);
        this.sexSpinner = view.findViewById(R.id.sexLIst);
        this.update = view.findViewById(R.id.update);

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

        //this.insert = view.findViewById(R.id.insert);
        this.context = getContext();
        new Thread(new Runnable() {

            String exception;

            @Override
            public void run() {

                CatalogCall catalogCall = new CatalogCall();
                try {

                    if(getArguments() != null) {
                        Bundle bundle = getArguments();
                        CatalogHelper c =(CatalogHelper) bundle.getSerializable("catalogHelper");
                        searchHelper = (SearchHelper) bundle.getSerializable("searchHelper");

                        newId = bundle.getInt("newId");
                        if(c != null){
                            catalogHelper = catalogCall.select(c.getId());
                        }

                        database = Room.databaseBuilder(context, AppDatabase.class, "BioBirding").build();
                        if(newId == 0){
                            localSpecies = database.localSpeciesDao().select(catalogHelper.getIdSpecies());
                        }else{
                            localSpecies = database.localSpeciesDao().select(newId);
                        }
                    }
                } catch (InterruptedException | IOException | JSONException e) {
                    exception = e.getMessage();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);

                        if(exception == null){
                            if(catalogHelper.getId() > 0){

                                latitude.setText(String.format(Locale.ENGLISH, "%.7f", catalogHelper.getLatitude()));
                                longitude.setText(String.format(Locale.ENGLISH, "%.7f", catalogHelper.getLongitude()));
                                temperature.setText(String.format(Locale.ENGLISH, "%.1f", catalogHelper.getTemperature()));
                                humidity.setText(String.format(Locale.ENGLISH, "%.1f", catalogHelper.getHumidity()));
                                wind.setText(String.format(Locale.ENGLISH, "%.1f", catalogHelper.getWind()));
                                weather.setText(catalogHelper.getWeather());
                                identificationCode.setText(catalogHelper.getIdentificationCode());
                                notes.setText(catalogHelper.getNotes());
                                neighborhood.setText(catalogHelper.getNeighborhood());
                                city.setText(catalogHelper.getCity());
                                state.setText(catalogHelper.getState());
                                scientificName.setText(localSpecies.getScientificName());

                                if(!catalogHelper.getAge().equals("null")){
                                    for (int i = 0; i < ageSpinner.getCount(); i++) {
                                        if (ageSpinner.getItemAtPosition(i).equals(catalogHelper.getAge())) {
                                            ageSpinner.setSelection(i);
                                            break;
                                        }
                                    }
                                }

                                if(!catalogHelper.getSex().equals("null")){
                                    for (int i = 0; i < sexSpinner.getCount(); i++) {
                                        if (sexSpinner.getItemAtPosition(i).equals(catalogHelper.getSex())) {
                                            sexSpinner.setSelection(i);
                                            break;
                                        }
                                    }
                                }

                            }else{
                                alert.setMessage(R.string.fail);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        redirectActivity();
                                    }
                                });
                                alert.show();

                            }
                        }else{
                            alert.setMessage(exception);
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    redirectActivity();
                                }
                            });
                            alert.show();
                        }



                    }
                });
            }
        }).start();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateFields()) {


                    update.setEnabled(false);

                    new Thread(new Runnable() {

                        String exception = null;
                        Boolean response = false;

                        @Override
                        public void run() {

                            CatalogCall catalogCall = new CatalogCall();
                            try {

                                if(newId >0){
                                    catalogHelper.setIdSpecies(newId);
                                }

                                catalogHelper.setSex(sexSpinner.getSelectedItem().toString());
                                catalogHelper.setAge(ageSpinner.getSelectedItem().toString());
                                catalogHelper.setLatitude(Double.parseDouble(latitude.getText().toString()));
                                catalogHelper.setLongitude(Double.parseDouble(longitude.getText().toString()));
                                catalogHelper.setTemperature(Double.parseDouble(temperature.getText().toString()));
                                catalogHelper.setHumidity(Double.parseDouble(humidity.getText().toString()));
                                catalogHelper.setWind(Double.parseDouble(wind.getText().toString()));
                                catalogHelper.setHumidity(Double.parseDouble(humidity.getText().toString()));
                                catalogHelper.setWeather(weather.getText().toString());
                                catalogHelper.setNotes(notes.getText().toString());
                                catalogHelper.setIdentificationCode(identificationCode.getText().toString());
                                catalogHelper.setNeighborhood(neighborhood.getText().toString());
                                catalogHelper.setCity(city.getText().toString());
                                catalogHelper.setState(state.getText().toString());

                                response = catalogCall.update(catalogHelper);

                            } catch (InterruptedException | IOException | JSONException e) {
                                exception = e.getMessage();
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                    if(exception == null){
                                        if(response){
                                            alert.setMessage(R.string.update_register);
                                        }else{
                                            alert.setMessage(R.string.fail);
                                            update.setEnabled(true);
                                        }
                                    }else{
                                        alert.setMessage(exception);
                                        update.setEnabled(true);
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


        scientificName.findViewById(R.id.scientificName).setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    SelectLocalSpeciesFragment selectLocalSpeciesFragment = new SelectLocalSpeciesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("catalogHelper", catalogHelper);
                    bundle.putSerializable("searchHelper", searchHelper);
                    selectLocalSpeciesFragment.setArguments(bundle);

                    if(getFragmentManager() != null) {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, selectLocalSpeciesFragment);
                        transaction.commit();
                    }
                    return super.onDoubleTap(e);
                }
            });

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });



        Button delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage(R.string.message_delete_catalog);
                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                CatalogCall catalogCall = new CatalogCall();
                                try {
                                    catalogCall.delete(catalogHelper);
                                } catch (InterruptedException | IOException | JSONException e) {
                                    e.printStackTrace();
                                }

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        redirectActivity();
                                    }
                                });
                            }
                        }).start();
                    }
                });

                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }

        });






        return view;
    }

    public void redirectActivity(){
        if(getFragmentManager() != null) {

            ListCatalogFragment listCatalogFragment = new ListCatalogFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("searchHelper", searchHelper);
            listCatalogFragment.setArguments(bundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, listCatalogFragment);
            transaction.commit();
        }
    }


    public boolean validateFields(){


        if(TextUtils.isEmpty(latitude.getText().toString())){
            latitude.setError(getString(R.string.requiredText));
            latitude.requestFocus();
            update.setEnabled(true);
            return false;
        }

        Pattern expressionLatitude = Pattern.compile("^([+\\-])?(?:90(?:(?:\\.0{1,7})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,7})?))$");
        Matcher latitudeMatcher = expressionLatitude.matcher(latitude.getText().toString());

        if(!latitudeMatcher.matches()){
            latitude.setError(getString(R.string.coordinate_error));
            latitude.requestFocus();
            update.setEnabled(true);
            return false;
        }

        if(TextUtils.isEmpty(longitude.getText().toString())){
            longitude.setError(getString(R.string.requiredText));
            longitude.requestFocus();
            update.setEnabled(true);
            return false;
        }

        Pattern expressionLongitude = Pattern.compile("^([+\\-])?(?:180(?:(?:\\.0{1,7})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,7})?))$");
        Matcher longitudeMatcher = expressionLongitude.matcher(longitude.getText().toString());

        if(!longitudeMatcher.matches()){
            longitude.setError(getString(R.string.coordinate_error));
            longitude.requestFocus();
            update.setEnabled(true);
            return false;
        }


        if(TextUtils.isEmpty(temperature.getText().toString())){
            temperature.setError(getString(R.string.requiredText));
            temperature.requestFocus();
            update.setEnabled(true);
            return false;
        }

        if(TextUtils.isEmpty(humidity.getText().toString())){
            humidity.setError(getString(R.string.requiredText));
            humidity.requestFocus();
            update.setEnabled(true);
            return false;
        }

        if(TextUtils.isEmpty(wind.getText().toString())){
            wind.setError(getString(R.string.requiredText));
            wind.requestFocus();
            update.setEnabled(true);
            return false;
        }

        if(TextUtils.isEmpty(weather.getText().toString())){
            wind.setError(getString(R.string.requiredText));
            wind.requestFocus();
            update.setEnabled(true);
            return false;
        }

        if(TextUtils.isEmpty(city.getText().toString())){
            city.setError(getString(R.string.requiredText));
            city.requestFocus();
            update.setEnabled(true);
            return false;
        }

        if(TextUtils.isEmpty(state.getText().toString())){
            state.setError(getString(R.string.requiredText));
            state.requestFocus();
            update.setEnabled(true);
            return false;
        }

        return true;
    }

}