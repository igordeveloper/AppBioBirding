package com.biobirding.biobirding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ExampleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_example, container, false);
        String species[] = {"Arara", "Bem-te-vi", "Carcará", "Tucanuçu", "Sanhaçu", "Tico-Tico"};

        ListView listView = view.findViewById(R.id.speciesListView);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                species
                );

        listView.setAdapter(listViewAdapter);

        // Inflate the layout for this fragment
        return view;
    }


}
