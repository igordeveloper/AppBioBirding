package com.biobirding.biobirding.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.biobirding.biobirding.threads.InsertSpecieThread;

public class InsertSpeciesFragment extends Fragment {

    private EditText scientificName;
    private EditText notes;
    private Spinner spinner;
    private Button addSpecies;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_species, container, false);

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

                    Handler handler = new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            Object response = msg.obj;

                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                            if(response.getClass() == String.class ){
                                alert.setMessage((String)response);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        addSpecies.setEnabled(true);
                                    }
                                });
                                alert.show();
                            }

                            if(response.getClass() == Boolean.class ){
                                alert.setMessage(R.string.insert_species);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        redirectActivity();
                                    }
                                });
                                alert.show();
                            }
                            return true;
                        }
                    });

                    String conservationState = "";
                    if(spinner.getSelectedItemId() != 0){
                        conservationState = spinner.getSelectedItem().toString();
                    }

                    Species species = new Species();
                    species.setScientificName(scientificName.getText().toString());
                    species.setNotes(notes.getText().toString());
                    species.setConservationState(conservationState);

                    InsertSpecieThread insertSpecieThread = new InsertSpecieThread(handler, species);
                    insertSpecieThread.start();
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
            return false;
        }

        return true;
    }
}
