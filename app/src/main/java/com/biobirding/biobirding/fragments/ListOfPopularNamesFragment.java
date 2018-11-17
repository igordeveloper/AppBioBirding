package com.biobirding.biobirding.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.customAdapters.PopularNameAdapter;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.webservice.PopularNameCall;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class ListOfPopularNamesFragment extends Fragment {

    private ListView listView;
    private ArrayList<PopularName> popularNameList;
    private PopularNameAdapter adapter;
    private Species species;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_of_popular_names, container, false);
        this.listView = view.findViewById(R.id.popularNameListView);
        TextView scientificName = view.findViewById(R.id.scientific_name);
        initList(getContext());

        if(getArguments() != null){
            Bundle bundle = getArguments();
            this.species = (Species) bundle.getSerializable("species");

            if (species != null) {
                scientificName.setText(species.getScientificName());
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Receive popularName object
                PopularName popularName = (PopularName) parent.getAdapter().getItem(position);

                //Change to EditPopularNameFragment
                EditPopularNameFragment editPopularNameFragment = new EditPopularNameFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("species", species);
                bundle.putSerializable("popularName", popularName);
                editPopularNameFragment.setArguments(bundle);

                if(getFragmentManager() != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, editPopularNameFragment);
                    transaction.commit();
                }
            }
        });

        FloatingActionButton add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getFragmentManager() != null) {
                    InsertPopularNameFragment popularNameFragment = new InsertPopularNameFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("species", species);
                    popularNameFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, popularNameFragment);
                    transaction.commit();
                }
            }
        });


        selectAllNames();
        return view;
    }


    public void selectAllNames(){

        new Thread(new Runnable() {

            ArrayList<PopularName> popularNames;

            @Override
            public void run() {

                PopularNameCall popularNameCall = new PopularNameCall();
                try {
                    popularNames = popularNameCall.selectAllFromSpecies(species);
                } catch (InterruptedException | IOException | JSONException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        popularNameList.clear();
                        popularNameList.addAll(popularNames);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

    public void initList(Context context){
        popularNameList = new ArrayList<>();
        adapter = new PopularNameAdapter((Activity) context, popularNameList);
        this.listView.setAdapter(adapter);
    }

}
