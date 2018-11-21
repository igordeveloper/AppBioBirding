package com.biobirding.biobirding.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.activity.DatePickerFragment;
import com.biobirding.biobirding.entity.User;
import com.biobirding.biobirding.helper.SearchHelper;
import com.biobirding.biobirding.webservice.CatalogCall;
import org.json.JSONException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FilterSearchFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private Handler handler = new Handler();
    private Spinner stateSpinner;
    private Spinner citySpinner;
    private ArrayList<String> response;
    private ArrayAdapter<String> adapterState;
    private ArrayAdapter<String> adapterCity;
    List<String> cities = new ArrayList<>();
    private String state;
    private Button send;

    private EditText startDate;
    private EditText finishDate;
    private EditText species;
    private EditText identificationCode;
    private Long minDate = 0L;
    private FragmentTransaction transaction;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter_search, container, false);
        species = view.findViewById(R.id.species);
        identificationCode = view.findViewById(R.id.identificationCode);

        startDate = view.findViewById(R.id.startDate);
        startDate.setShowSoftInputOnFocus(false);
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.setFocusable(false);

        finishDate = view.findViewById(R.id.finishDate);
        finishDate.setShowSoftInputOnFocus(false);
        finishDate.setInputType(InputType.TYPE_NULL);
        finishDate.setFocusable(false);
        finishDate.setEnabled(false);

        if(getActivity()!=null){
            startDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v4.app.DialogFragment date = new DatePickerFragment();
                    startDate.setError(null);
                    Bundle bundle = new Bundle();
                    bundle.putLong("minDate", minDate);
                    date.setArguments(bundle);
                    date.show(getActivity().getSupportFragmentManager(), "date");
                }
            });


            finishDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v4.app.DialogFragment date = new DatePickerFragment();
                    finishDate.setError(null);
                    Bundle bundle = new Bundle();
                    bundle.putLong("minDate", minDate);
                    date.setArguments(bundle);
                    date.show(getActivity().getSupportFragmentManager(), "date");
                }
            });
        }

        this.stateSpinner = view.findViewById(R.id.state);
        this.citySpinner = view.findViewById(R.id.city);

        if (getContext() != null) {

            new Thread(new Runnable() {

                String exception = null;

                @Override
                public void run() {

                    CatalogCall catalogCall = new CatalogCall();
                    try {
                        response = catalogCall.selectStateGroup();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                if(exception == null){
                                    List<String> states = new ArrayList<>();
                                    states.add(getContext().getString(R.string.state));
                                    states.addAll(response);

                                    adapterState = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, states);
                                    adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    stateSpinner.setAdapter(adapterState);
                                }else{
                                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                    alert.setMessage(exception);
                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alert.show();
                                }
                            }
                        });

                    } catch (InterruptedException | IOException | JSONException e) {
                        Log.d("exception", e.getMessage());
                    }



                }
            }).start();

        }


        this.stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(stateSpinner.getSelectedItemId() != 0){
                    state = stateSpinner.getSelectedItem().toString();
                    cities();
                }else{
                    cities.clear();
                    cities.add(getContext().getString(R.string.city));
                    adapterCity = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cities);
                    adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapterCity.notifyDataSetChanged();
                    citySpinner.setAdapter(adapterCity);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        send = view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()){
                    SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences("bio", Context.MODE_PRIVATE);

                    User user = new User();
                    user.setRg(sharedPref.getString("rg_bio", ""));
                    user.setAccessLevel(sharedPref.getInt("access_level", 0));

                    SearchHelper searchHelper = new SearchHelper();
                    searchHelper.setState(stateSpinner.getSelectedItemId() == 0 ? "" : stateSpinner.getSelectedItem().toString());
                    searchHelper.setCity(citySpinner.getSelectedItemId() == 0 ? "" : citySpinner.getSelectedItem().toString());
                    searchHelper.setStartDate(startDate.getText().toString());
                    searchHelper.setFinishDate(finishDate.getText().toString());
                    searchHelper.setSpeciesName(species.getText().toString());
                    searchHelper.setIdentificationCode(identificationCode.getText().toString());
                    searchHelper.setUser(user);

                    if(getFragmentManager() != null) {
                        ListCatalogFragment listCatalogFragment = new ListCatalogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("searchHelper", searchHelper);
                        listCatalogFragment.setArguments(bundle);
                        transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, listCatalogFragment);
                        transaction.commit();

                    }

                }
            }
        });


        return view;

    }


    public void cities(){

        new Thread(new Runnable() {

            String exception = null;

            @Override
            public void run() {

                CatalogCall catalogCall = new CatalogCall();
                try {
                    response = catalogCall.selectCityGroup(state);
                } catch (InterruptedException | IOException | JSONException e) {
                    Log.d("exception", e.getMessage());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    if(getContext() != null){
                        if(exception == null){
                            cities.clear();
                            cities.add(getContext().getString(R.string.city));
                            cities.addAll(response);

                            adapterCity = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cities);
                            adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            adapterCity.notifyDataSetChanged();
                            citySpinner.setAdapter(adapterCity);
                        }else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setMessage(exception);
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alert.show();
                        }
                    }
                    }
                });
            }
        }).start();
    }


    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0,0,1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String fullDate = simpleDateFormat.format(calendar.getTime());

        if(minDate == 0L){

            this.startDate.setText(fullDate);
            this.startDate.setEnabled(false);

            this.minDate = calendar.getTimeInMillis();
            this.startDate.setEnabled(false);
            this.finishDate.setEnabled(true);
        }else{
            this.finishDate.setText(fullDate);
            this.finishDate.setEnabled(false);
            this.send.setEnabled(true);
        }
    }


    public void updateDate(int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0,0,1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String fullDate = simpleDateFormat.format(calendar.getTime());

        if(minDate == 0L){
            this.startDate.setText(fullDate);
            this.startDate.setEnabled(false);

            this.minDate = calendar.getTimeInMillis();
            this.startDate.setEnabled(false);
            this.finishDate.setEnabled(true);
        }else{
            this.finishDate.setText(fullDate);
            this.finishDate.setEnabled(false);
            this.send.setEnabled(true);
        }
    }

    public boolean validateFields(){
        if(TextUtils.isEmpty(startDate.getText().toString())){
            startDate.setError(getString(R.string.requiredText));
            return false;
        }

        if(TextUtils.isEmpty(finishDate.getText().toString())){
            finishDate.setError(getString(R.string.requiredText));
            return false;
        }

        return true;
    }

}