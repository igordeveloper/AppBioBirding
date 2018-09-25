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

public class InsertSpeciesFragment extends Fragment {

    private EditText scientificName;
    private EditText notes;
    private Spinner spinner;
    private Button addSpecies;
    private Handler handler = new Handler();
    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_species, container, false);

        this.context = getContext();
        this.spinner = view.findViewById(R.id.conservationStateList);

        if (getContext() != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.items, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(adapter);
        }

        this.addSpecies = view.findViewById(R.id.addSpecies);
        this.scientificName = view.findViewById(R.id.scientific_name);
        this.notes = view.findViewById(R.id.notes);

        addSpecies.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addSpecies.setEnabled(false);

                if(validateFields()) {

                    new Thread(new Runnable() {

                        String exception = null;
                        Boolean response = false;

                        @Override
                        public void run() {

                            String conservationState = "";
                            if(spinner.getSelectedItemId() != 0){
                                conservationState = spinner.getSelectedItem().toString();
                            }

                            Species species = new Species();
                            species.setScientificName(scientificName.getText().toString());
                            species.setNotes(notes.getText().toString());
                            species.setConservationState(conservationState);

                            SpeciesCall speciesCall = new SpeciesCall();
                            try {
                                response = speciesCall.insert(species);
                            } catch (InterruptedException | IOException | JSONException e) {
                                exception = e.getMessage();
                            }


                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                    if(exception == null){
                                        if(response){
                                            alert.setMessage(R.string.insert_species);
                                        }else{
                                            alert.setMessage(R.string.fail);
                                            addSpecies.setEnabled(true);
                                        }
                                    }else{
                                        alert.setMessage(exception);
                                        addSpecies.setEnabled(true);
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
            ListOfSpeciesFragment speciesLIstFragment = new ListOfSpeciesFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, speciesLIstFragment);
            transaction.commit();
        }
    }

    public boolean validateFields(){
        if(TextUtils.isEmpty(scientificName.getText().toString())){
            scientificName.setError(getString(R.string.requiredText));
            addSpecies.setEnabled(true);
            return false;
        }

        return true;
    }
}
