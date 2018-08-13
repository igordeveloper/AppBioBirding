package com.biobirding.biobirding;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.biobirding.biobirding.webservice.SpeciesCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public class AddSpeciesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_species, container, false);

        Button addSpecies = view.findViewById(R.id.addSpecies);

        addSpecies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(){

                    SpeciesCall speciesCall = new SpeciesCall(getContext());

                    @Override
                    public void run() {
                        try{
                            JSONObject json = speciesCall.insert("3", "4");
                            String authorized = json.getString("authorized");
                            Log.d("authorized", authorized);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

            }
        });

        return view;
    }



}
