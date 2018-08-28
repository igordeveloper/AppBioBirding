package com.biobirding.biobirding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.biobirding.biobirding.entity.Species;

public class ListOfPopularNamesFragment extends Fragment {

    private Species species;
    private FragmentTransaction transaction;

    public void setArguments(@Nullable Species species) {
        this.species = species;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_of_popular_names, container, false);
        TextView scientificName = view.findViewById(R.id.scientific_name);
        scientificName.setText(species.getScientificName());
        return view;
    }



}
