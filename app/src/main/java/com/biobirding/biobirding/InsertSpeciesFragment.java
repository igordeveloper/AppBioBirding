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
import com.biobirding.biobirding.threads.InsertSpecieThread;

public class AddSpeciesFragment extends Fragment {

    private EditText scientificName;
    private EditText notes;
    private Spinner spinner;

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

        Button addSpecies = view.findViewById(R.id.addSpecies);
        this.scientificName = view.findViewById(R.id.scientific_name);
        this.notes = view.findViewById(R.id.notes);


        addSpecies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(validateFields()) {
                    Handler handler = new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            String message = (String) msg.obj;
                            //CustomSimpleDialog alert = new CustomSimpleDialog(message, getContext());
                            //alert.show();
                            redirectActivity();
                            return true;
                        }
                    });

                    String conservationState = null;
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
