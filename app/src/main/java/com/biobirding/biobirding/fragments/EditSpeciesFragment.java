package com.biobirding.biobirding.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.webservice.SpeciesCall;

import org.json.JSONException;

import java.io.IOException;


public class EditSpeciesFragment extends Fragment {

    private Species species;
    private EditText scientificName;
    private EditText notes;
    private Spinner spinner;
    private Handler handler = new Handler();
    private Context context;
    private Button editSpecies;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_species, container, false);
        this.scientificName = view.findViewById(R.id.scientific_name);
        this.notes = view.findViewById(R.id.notes);
        this.spinner = view.findViewById(R.id.sexLIst);

        this.context = getContext();

        if (getContext() != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.items, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(adapter);
        }

        if(getArguments() != null){
            Bundle bundle = getArguments();
            this.species = (Species) bundle.getSerializable("species");

            if (species != null) {
                this.scientificName.setText(species.getScientificName());
            }
        }


        /* Edit button */
        editSpecies = view.findViewById(R.id.editSpecies);
        editSpecies.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editSpecies.setEnabled(false);

                if (validateFields()) {


                    new Thread(new Runnable() {

                        String exception;
                        Boolean response;

                        @Override
                        public void run() {

                            String conservationState = "";
                            if (spinner.getSelectedItemId() != 0) {
                                conservationState = spinner.getSelectedItem().toString();
                            }

                            species.setScientificName(scientificName.getText().toString());
                            species.setNotes(notes.getText().toString());
                            species.setConservationState(conservationState);

                            SpeciesCall speciesCall = new SpeciesCall();
                            try {
                                response = speciesCall.update(species);
                            } catch (InterruptedException | IOException | JSONException e) {
                                exception = e.getMessage();
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                    if(exception == null){
                                        if(response){
                                            alert.setMessage(R.string.update_species);
                                        }else{
                                            alert.setMessage(R.string.fail);
                                            editSpecies.setEnabled(true);
                                        }
                                    }else{
                                        alert.setMessage(exception);
                                        editSpecies.setEnabled(true);
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



        new Thread(new Runnable() {

            String exception;
            Species speciesResponse;

            @Override
            public void run() {

                SpeciesCall speciesCall = new SpeciesCall();
                try {
                    speciesResponse = speciesCall.select(species);
                } catch (InterruptedException | IOException | JSONException e) {
                    exception = e.getMessage();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);

                        if(exception == null){
                            if(species.getScientificName().equals(speciesResponse.getScientificName())){
                                notes.setText(speciesResponse.getNotes());
                                if(!speciesResponse.getConservationState().equals("null")){
                                    for (int i = 0; i < spinner.getCount(); i++) {
                                        if (spinner.getItemAtPosition(i).equals(speciesResponse.getConservationState())) {
                                            spinner.setSelection(i);
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

        return view;

    }

    public void redirectActivity(){
        if(getFragmentManager() != null) {
            ListOfSpeciesFragment speciesLIstFragment = new ListOfSpeciesFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, speciesLIstFragment);
            transaction.commit();
        }
    }

    public boolean validateFields(){
        if(TextUtils.isEmpty(scientificName.getText().toString())){
            scientificName.setError(getString(R.string.requiredText));
            editSpecies.setEnabled(true);
            return false;
        }
        return true;
    }
}
