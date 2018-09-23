package com.biobirding.biobirding.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.customAdapters.PopularNameAdapter;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.threads.SearchSpeciesThread;
import com.biobirding.biobirding.threads.SelectPopularNamesThread;
import com.biobirding.biobirding.webservice.PopularNameCall;
import com.biobirding.biobirding.entity.PopularName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

public class ListOfPopularNamesFragment extends Fragment {

    private ListView listView;
    private ArrayList<PopularName> popularNameList;
    private PopularNameAdapter adapter;
    private Species species;

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

                //Receive specie object
                PopularName popularName = (PopularName) parent.getAdapter().getItem(position);

                //Change to InfoSpeciesFragment
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


        selectAllNames(species);
        return view;
    }


    public void selectAllNames(Species species){

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                Object response = msg.obj;

                if(response.getClass() == String.class && getContext() != null){
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getContext());
                    alert.setMessage((String)response);
                    alert.show();
                }

                if(response.getClass() == ArrayList.class ){

                    Log.d("aqui", "aqui");

                    ArrayList<PopularName> popularNames = (ArrayList<PopularName>) msg.obj;
                    popularNameList.clear();
                    popularNameList.addAll(popularNames);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        SelectPopularNamesThread selectPopularNamesThread = new SelectPopularNamesThread(handler, species);
        selectPopularNamesThread.start();

    }

    public void initList(Context context){
        popularNameList = new ArrayList<>();
        adapter = new PopularNameAdapter((Activity) context, popularNameList);
        this.listView.setAdapter(adapter);
    }

}
