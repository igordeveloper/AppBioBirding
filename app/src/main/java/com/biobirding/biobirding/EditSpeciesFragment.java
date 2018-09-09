package com.biobirding.biobirding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.helper.CustomSimpleDialog;
import com.biobirding.biobirding.threads.SelectSpeciesThread;
import com.biobirding.biobirding.threads.UpdateSpecieThread;

public class EditSpeciesFragment extends Fragment {

    private Species species;
    private EditText scientificName;
    private EditText notes;
    private Spinner spinner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_species, container, false);
        this.scientificName = view.findViewById(R.id.scientific_name);
        this.notes = view.findViewById(R.id.notes);
        this.spinner = view.findViewById(R.id.conservationStateList);



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
        Button editSpecies = view.findViewById(R.id.editSpecies);
        editSpecies.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateFields()) {

                    Handler handler = new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            String message = (String) msg.obj;
                            CustomSimpleDialog alert = new CustomSimpleDialog(message, getContext());
                            alert.show();
                            redirectActivity();
                            return true;
                        }
                    });

                    String conservationState = null;
                    if (spinner.getSelectedItemId() != 0) {
                        conservationState = spinner.getSelectedItem().toString();
                    }

                    species.setScientificName(scientificName.getText().toString());
                    species.setNotes(notes.getText().toString());
                    species.setConservationState(conservationState);

                    UpdateSpecieThread updateSpecieThread = new UpdateSpecieThread(handler, species);
                    updateSpecieThread.start();

                }

            }
        });


        /* Search specie*/
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Species species = (Species) msg.obj;
                notes.setText(species.getNotes());
                if(!species.getConservationState().equals("null")){
                    for (int i = 0; i < spinner.getCount(); i++) {
                        if (spinner.getItemAtPosition(i).equals(species.getConservationState())) {
                            spinner.setSelection(i);
                            break;
                        }
                    }
                }
                return true;
            }
        });

        SelectSpeciesThread selectSpeciesThread = new SelectSpeciesThread(handler, species);
        selectSpeciesThread.start();

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
