package com.biobirding.biobirding.fragments;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.customAdapters.LocalSpeciesAdapter;
import com.biobirding.biobirding.database.AppDatabase;
import com.biobirding.biobirding.entity.LocalSpecies;
import com.biobirding.biobirding.helper.CatalogHelper;
import com.biobirding.biobirding.helper.SearchHelper;

import java.util.ArrayList;
import java.util.List;


public class SelectLocalSpeciesFragment extends Fragment {

    private ListView listView;
    private ArrayList<LocalSpecies> speciesList;
    private LocalSpeciesAdapter adapter;
    private EditText txtSearch;
    private String search;
    private Handler handler = new Handler();
    private CatalogHelper catalogHelper = null;
    private SearchHelper searchHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_local_species, container, false);
        this.listView = view.findViewById(R.id.speciesListView);
        txtSearch = view.findViewById(R.id.txtSearch);

        if(getArguments() !=null){
            Bundle bundle = getArguments();
            catalogHelper = (CatalogHelper) bundle.getSerializable("catalogHelper");
            searchHelper = (SearchHelper) bundle.getSerializable("searchHelper");
        }

        initList(getContext());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listView.setVisibility(View.INVISIBLE);

                //Hide keyboard
                if(getActivity() != null){
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputMethodManager != null){
                        inputMethodManager.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
                    }
                }

                //Receive localSpecies object
                LocalSpecies localSpecies = (LocalSpecies) parent.getAdapter().getItem(position);

                if(catalogHelper == null){

                    //Change to insertCatalogFragment
                    InsertCatalogFragment insertCatalogFragment = new InsertCatalogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("localSpecies", localSpecies);
                    insertCatalogFragment.setArguments(bundle);

                    if(getFragmentManager() != null) {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, insertCatalogFragment);
                        transaction.commit();
                    }
                }else{

                    //Change to insertCatalogFragment
                    EditCatalogFragment editCatalogFragment = new EditCatalogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("catalogHelper", catalogHelper);
                    bundle.putSerializable("searchHelper", searchHelper);
                    bundle.putInt("newId", localSpecies.getId());
                    editCatalogFragment.setArguments(bundle);

                    if(getFragmentManager() != null) {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, editCatalogFragment);
                        transaction.commit();
                    }
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
                    search = s.toString();
                    searchItem();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public void searchItem(){
        
        new Thread(new Runnable() {

            List<LocalSpecies> response;

            @Override
            public void run() {

                if(getContext() != null){
                    AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, "BioBirding").build();
                    response = database.localSpeciesDao().search(search);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            speciesList.clear();
                            speciesList.addAll(response);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

            }
        }).start();

    }

    public void initList(Context context){
        speciesList = new ArrayList<>();
        adapter = new LocalSpeciesAdapter((Activity) context, speciesList);
        this.listView.setAdapter(adapter);
    }

}