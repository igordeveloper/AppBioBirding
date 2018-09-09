package com.biobirding.biobirding;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.biobirding.biobirding.customAdapters.PopularNameAdapter;
import com.biobirding.biobirding.customAdapters.SpeciesAdapter;
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.webservice.PopularNameCall;

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
    private JSONObject json;
    private Species species;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_of_popular_names, container, false);
        this.listView = view.findViewById(R.id.popularNameListView);
        TextView scientificName = view.findViewById(R.id.scientific_name);

        if(getArguments() != null){
            Bundle bundle = getArguments();
            this.species = (Species) bundle.getSerializable("species");

            if (species != null) {
                scientificName.setText(species.getScientificName());
            }
        }

        initList(getContext());
        selectAllNames();
        return view;
    }


    public void selectAllNames(){
        new Thread() {

            PopularNameCall popularNameCall = new PopularNameCall();

            @Override
            public void run() {
                try {
                    json = popularNameCall.selectAll(species);

                    if(getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(json.has("exception")){
                                    try {
                                        alertDialog(json.getString("exception"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                try {
                                    if(json.getString("authorized").equals("false")){
                                        startActivity(new Intent(getActivity(), LogoffActivity.class));
                                    }else{
                                        try {
                                            popularNameList.clear();
                                            JSONArray popularNameCall = json.getJSONArray("response");
                                            for(int i = 0; i < popularNameCall.length(); i++){
                                                JSONObject finalObject = popularNameCall.getJSONObject(i);
                                                PopularName popularName = new PopularName();
                                                popularName.setName(finalObject.getString("name"));
                                                popularNameList.add(popularName);
                                            }
                                            adapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    public void alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void initList(Context context){
        popularNameList = new ArrayList<PopularName>();
        adapter = new PopularNameAdapter((Activity) context, popularNameList);
        this.listView.setAdapter(adapter);
    }

}
