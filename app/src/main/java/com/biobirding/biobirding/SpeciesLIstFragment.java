package com.biobirding.biobirding;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import com.biobirding.biobirding.threads.dao.SearchLocalSpecies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeciesLIstFragment extends Fragment {

    protected ListView listView;
    protected EditText editText;
    protected ArrayList<String> listSpecies;
    protected ArrayAdapter<String> listViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_species_list, container, false);
        this.listView = view.findViewById(R.id.speciesListView);
        this.editText = view.findViewById(R.id.txtSearch);
        initList(getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TestFragment test = new TestFragment();
                Bundle args = new Bundle();
                args.putString("specieName", (String) ((TextView) view).getText());
                test.setArguments(args);

                if(getFragmentManager() != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, test);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });


        FloatingActionButton add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager() != null) {
                    AddSpeciesFragment addSpeciesFragment = new AddSpeciesFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, addSpeciesFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")){
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }



    public void searchItem(String search){

        Handler mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                List species = (List) msg.obj;
                listSpecies.clear();

                for (Object s:species){
                    listSpecies.add(s.toString());
                }

                listViewAdapter.notifyDataSetChanged();
            }
        };

        SearchLocalSpecies listSpecies = new SearchLocalSpecies(getContext(), mHandler, search);
        Thread thread = new Thread(listSpecies);
        thread.start();
    }

    public void initList(Context context){
        this.listSpecies = new ArrayList<>(Arrays.asList(new String[] {}));
        this.listViewAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                this.listSpecies
        );
        this.listView.setAdapter(listViewAdapter);
    }
}