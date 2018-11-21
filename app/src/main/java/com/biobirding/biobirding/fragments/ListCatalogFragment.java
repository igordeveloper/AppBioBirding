package com.biobirding.biobirding.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.customAdapters.ListCatalogAdapter;
import com.biobirding.biobirding.helper.CatalogHelper;
import com.biobirding.biobirding.helper.SearchHelper;
import com.biobirding.biobirding.webservice.CatalogCall;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;

public class ListCatalogFragment extends Fragment {

    private ListView listView;
    private ArrayList<CatalogHelper> catalogHelpers;
    private ListCatalogAdapter adapter;
    private SearchHelper searchHelper;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_catalog, container, false);
        this.listView = view.findViewById(R.id.popularNameListView);

        initList(getContext());

        if(getArguments() != null){
            Bundle bundle = getArguments();
            this.searchHelper = (SearchHelper) bundle.getSerializable("searchHelper");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //Receive CatalogHelper object
                CatalogHelper catalogHelper = (CatalogHelper) parent.getAdapter().getItem(position);

                //Change to EditCatalogFragment
                EditCatalogFragment editCatalogFragment = new EditCatalogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("catalogHelper", catalogHelper);
                bundle.putSerializable("searchHelper", searchHelper);
                editCatalogFragment.setArguments(bundle);

                if(getFragmentManager() != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, editCatalogFragment);
                    transaction.commit();
                }
            }
        });

        selectFilter();
        return view;
    }


    public void selectFilter(){

        new Thread(new Runnable() {

            ArrayList<CatalogHelper> catalogHelperArrayList;

            @Override
            public void run() {

                CatalogCall catalogCall = new CatalogCall();
                try {
                    catalogHelperArrayList = catalogCall.selectFilter(searchHelper);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            catalogHelpers.clear();
                            catalogHelpers.addAll(catalogHelperArrayList);
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (InterruptedException | IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void initList(Context context){
        catalogHelpers = new ArrayList<>();
        adapter = new ListCatalogAdapter((Activity) context, catalogHelpers);
        this.listView.setAdapter(adapter);
    }

}
