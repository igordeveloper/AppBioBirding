package com.biobirding.biobirding;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.customAdapters.SpeciesAdapter;
import com.biobirding.biobirding.threads.SearchSpeciesThread;
import java.util.ArrayList;

public class ListOfSpeciesFragment extends Fragment {

    private ListView listView;
    private ArrayList<Species> speciesList;
    private SpeciesAdapter adapter;
    private EditText txtSearch;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_of_species_, container, false);
        this.listView = view.findViewById(R.id.speciesListView);
        txtSearch = view.findViewById(R.id.txtSearch);
        initList(getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Hide keyboard
                if(getActivity() != null){
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputMethodManager != null){
                        inputMethodManager.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
                    }
                }

                //Receive specie object
                Species species = (Species) parent.getAdapter().getItem(position);

                //Change to InfoSpeciesFragment
                InfoSpeciesFragment infoSpeciesFragment = new InfoSpeciesFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("species", species);
                infoSpeciesFragment.setArguments(bundle);

                if(getFragmentManager() != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, infoSpeciesFragment);
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
                    transaction.commit();
                }
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
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

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            @SuppressWarnings("unchecked")
            public boolean handleMessage(Message msg) {
                ArrayList<Species> species = (ArrayList<Species>) msg.obj;
                speciesList.clear();
                speciesList.addAll(species);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        SearchSpeciesThread searchSpeciesThread = new SearchSpeciesThread(handler, search);
        searchSpeciesThread.start();
    }

    public void initList(Context context){
        speciesList = new ArrayList<>();
        adapter = new SpeciesAdapter((Activity) context, speciesList);
        this.listView.setAdapter(adapter);
    }
}