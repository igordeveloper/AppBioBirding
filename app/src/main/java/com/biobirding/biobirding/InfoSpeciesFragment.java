package com.biobirding.biobirding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.biobirding.biobirding.entity.Species;

public class InfoSpeciesFragment extends Fragment {

    private Species species;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_species, container, false);
        TextView text = view.findViewById(R.id.scientific_name);

        if(getArguments() != null){
            Bundle bundle = getArguments();
            this.species = (Species) bundle.getSerializable("species");
            if (species != null) {
                text.setText(species.getScientificName());
            }
        }


        /*Button to edit a species*/
        Button editSpecies = view.findViewById(R.id.buttonEditSpecies);
        editSpecies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager() != null) {
                    EditSpeciesFragment editSpeciesFragment = new EditSpeciesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("species", species);
                    editSpeciesFragment.setArguments(bundle);
                    transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, editSpeciesFragment);
                    transaction.commit();

                }
            }
        });

        /*Button to edit popular names*/
        Button editPopularNames = view.findViewById(R.id.buttonPopularNames);
        editPopularNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager()!=null){
                    ListOfPopularNamesFragment listOfPopularNamesFragment = new ListOfPopularNamesFragment();
                    listOfPopularNamesFragment.setArguments(species);
                    transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, listOfPopularNamesFragment);
                    transaction.commit();
                }
            }
        });

        return view;
    }



}
