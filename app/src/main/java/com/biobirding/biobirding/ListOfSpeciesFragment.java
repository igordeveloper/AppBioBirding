package com.biobirding.biobirding;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.webservice.SpeciesCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ListOfSpeciesFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> speciesList;
    private ArrayAdapter<String> listViewAdapter;
    private JSONObject json;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_of_species_, container, false);
        this.listView = view.findViewById(R.id.speciesListView);
        EditText editText = view.findViewById(R.id.txtSearch);
        initList(getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Cria objeto para enviar para o fragmento InfoSpecies
                Species species = new Species((String) ((TextView) view).getText());
                InfoSpeciesFragment infoSpeciesFragment = new InfoSpeciesFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("species", species);
                infoSpeciesFragment.setArguments(bundle);

                if(getFragmentManager() != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, infoSpeciesFragment);
                    transaction.commit();
                }
            }
        });


        FloatingActionButton add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager() != null) {
                    AddSpeciesFragment addSpeciesFragment = new AddSpeciesFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, addSpeciesFragment);
                    transaction.commit();
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")){
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }



    public void searchItem(final String search){

        new Thread() {

            SpeciesCall speciesCall = new SpeciesCall(getContext());

            @Override
            public void run() {
                try {
                    json = speciesCall.search(search);

                    if(getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(json.has("exception")){
                                    try {
                                        alertDialog(json.getString("exception"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                try {
                                    if(json.getString("authorized").equals("false")){
                                        startActivity(new Intent(getActivity(), LogoffActivity.class));
                                    }else{
                                        try {
                                            speciesList.clear();
                                            JSONArray species = json.getJSONArray("species");
                                            for(int i = 0; i < species.length(); i++){
                                                JSONObject finalObject = species.getJSONObject(i);
                                                speciesList.add(finalObject.getString("scientificName"));
                                            }
                                            listViewAdapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    public void alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void initList(Context context){
        this.speciesList = new ArrayList<>(Arrays.asList(new String[] {}));
        this.listViewAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                this.speciesList
        );
        this.listView.setAdapter(listViewAdapter);
    }
}