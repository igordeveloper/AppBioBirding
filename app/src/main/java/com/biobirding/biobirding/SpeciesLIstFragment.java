package com.biobirding.biobirding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class SpeciesLIstFragment extends Fragment {

    protected ListView listView;
    protected EditText editText;
    protected String species[];
    protected ArrayList<String> listSpecies;
    protected ArrayAdapter<String> listViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_species_list, container, false);
        this.listView = view.findViewById(R.id.speciesListView);
        this.editText = view.findViewById(R.id.txtSearch);

        initList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TestFragment test = new TestFragment();
                Bundle args = new Bundle();
                args.putString("specieName", (String) ((TextView) view).getText());
                test.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, test);
                transaction.addToBackStack(null);
                transaction.commit();

                /*Toast.makeText(getContext(), ((TextView) view).getText(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    initList();
                }else{
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    public void searchItem(String str){
        for (String specie:species){
            if(!specie.contains(str)){
                listSpecies.remove(specie);
            }
        }

        this.listViewAdapter.notifyDataSetChanged();

    }

    public void initList(){
        this.species = new String[] {"Arara",
                "Bem-te-vi",
                "Carcará",
                "Tucanuçu",
                "Sanhaçu",
                "Tico-Tico",
                "Pardal",
                "Gavião-Carijó",
                "Sabiá-pocá",
                "Sabiá-do-Campo",
                "Saíra-Amarela",
                "Corruíra",
                "Cambacica"};

        this.listSpecies = new ArrayList<>(Arrays.asList(this.species));

        this.listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                this.listSpecies
        );

        this.listView.setAdapter(listViewAdapter);

    }

}
