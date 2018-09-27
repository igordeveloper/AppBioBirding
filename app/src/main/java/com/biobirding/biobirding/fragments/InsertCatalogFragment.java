package com.biobirding.biobirding.fragments;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.activity.LogoffActivity;
import com.biobirding.biobirding.customAdapters.LocalSpeciesAdapter;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.LocalSpecies;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SelectLocalSpeciesFragment extends Fragment {

    private FusedLocationProviderClient client;
    private ListView listView;
    private ArrayList<LocalSpecies> speciesList;
    private LocalSpeciesAdapter adapter;
    private EditText txtSearch;
    private String search;
    private Handler handler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_local_species, container, false);
        this.listView = view.findViewById(R.id.speciesListView);
        txtSearch = view.findViewById(R.id.txtSearch);

        initList(getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                listView.setVisibility(View.INVISIBLE);

                //Hide keyboard
                if(getActivity() != null){
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputMethodManager != null){
                        inputMethodManager.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
                    }
                }

                //Receive specie object
                LocalSpecies species = (LocalSpecies) parent.getAdapter().getItem(position);

                //Change to InfoSpeciesFragment
                InfoSpeciesFragment infoSpeciesFragment = new InfoSpeciesFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("species", species);
                infoSpeciesFragment.setArguments(bundle);

                if(getFragmentManager() != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, infoSpeciesFragment);
                    transaction.commit();
                }*/
            }
        });



        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals("")){
                    search = s.toString();
                    searchItem();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public void searchItem(){
        
        new Thread(new Runnable() {

            List<LocalSpecies> response;

            @Override
            public void run() {

                AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, "BioBirding").build();
                Log.d("---", search);
                response = database.localSpeciesDao().search(search);

                Log.d(">>>>>>>>", String.valueOf(response.size()));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        speciesList.clear();
                        speciesList.addAll(response);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    public void initList(Context context){
        speciesList = new ArrayList<>();
        adapter = new LocalSpeciesAdapter((Activity) context, speciesList);
        this.listView.setAdapter(adapter);
    }

    private void requestPermission() {
        if (getActivity() != null){
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1 );

        }
    }
}