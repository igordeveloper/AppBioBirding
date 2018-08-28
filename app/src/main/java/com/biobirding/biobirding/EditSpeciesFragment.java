package com.biobirding.biobirding;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.helper.CustomSimpleDialog;
import com.biobirding.biobirding.webservice.SpeciesCall;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;


public class EditSpeciesFragment extends Fragment {

    private Species species;
    private EditText scientificName;
    private EditText notes;
    private JSONObject json;
    SpeciesCall speciesCall;
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


        this.speciesCall = new SpeciesCall(getContext());

        /* Edit button */
        Button editSpecies = view.findViewById(R.id.editSpecies);
        editSpecies.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateFields()){

                    new Thread(){

                        @Override
                        public void run() {
                            try {
                                String conservationState = null;
                                if(spinner.getSelectedItemId() != 0){
                                    conservationState = spinner.getSelectedItem().toString();
                                    Log.d("conservationState", conservationState);
                                }
                                json = speciesCall.update(species.getScientificName(),scientificName.getText().toString(), notes.getText().toString(), conservationState );

                                if (getActivity() != null) {

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (json.has("exception")) {
                                                try {
                                                    CustomSimpleDialog alert = new CustomSimpleDialog(getContext(), json.getString("exception"));
                                                    alert.show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }else if (!json.has("authorized")) {
                                                startActivity(new Intent(getActivity(), LogoffActivity.class));
                                            }else {
                                                try {
                                                    CustomSimpleDialog alert = new CustomSimpleDialog(getContext(), json.getString("response"));
                                                    alert.show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                redirectActivity();
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
            }
        });

        /* Search specie*/
        new Thread(){

            @Override
            public void run() {
                try {
                    json = speciesCall.select(species.getScientificName());

                    if(getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject species = json.getJSONObject("species");
                                    if (species.getString("notes") != "null"){
                                        notes.setText(species.getString("notes"));
                                        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
                                        int pos;
                                        if(species.getString("conservationState") != "null"){
                                            pos = adapter.getPosition(species.getString("conservationState"));
                                        }else{
                                            pos = 0;
                                        }
                                        spinner.setSelection(pos);
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

        if(TextUtils.isEmpty(notes.getText().toString())){
            notes.setError(getString(R.string.requiredText));
            return false;
        }
        return true;
    }

}
