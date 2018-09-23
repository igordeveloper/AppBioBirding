package com.biobirding.biobirding.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.threads.InsertPopularNameThread;

public class InsertPopularNameFragment extends Fragment {

    private EditText popularName;
    private Button addPopularName;
    private Species species;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_popular_name, container, false);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            this.species = (Species) bundle.getSerializable("species");
        }

        this.addPopularName = view.findViewById(R.id.edit_popular_name);
        TextView scientificName = view.findViewById(R.id.scientific_name);
        this.popularName = view.findViewById(R.id.popular_name);
        scientificName.setText(species.getScientificName());

        addPopularName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()) {

                    Handler handler = new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            Object response = msg.obj;

                            if(getContext() != null){
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                if(response.getClass() == String.class ){
                                    alert.setMessage((String)response);
                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            addPopularName.setEnabled(true);
                                        }
                                    });
                                    alert.show();
                                }

                                if(response.getClass() == Boolean.class ){
                                    alert.setMessage(R.string.insert_popular_name);
                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            redirectActivity();
                                        }
                                    });
                                    alert.show();
                                }
                                return true;
                            }
                            return false;
                        }
                    });

                    PopularName name = new PopularName();
                    name.setName(popularName.getText().toString());
                    name.setId(species.getId());

                    InsertPopularNameThread insertPopularNameThread = new InsertPopularNameThread(handler, name);
                    insertPopularNameThread.start();
                }
            }
        });

        return view;
    }

    public void redirectActivity(){
        if(getFragmentManager() != null) {
            ListOfPopularNamesFragment listOfPopularNamesFragment = new ListOfPopularNamesFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("species", species);
            listOfPopularNamesFragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, listOfPopularNamesFragment);
            transaction.commit();
        }
    }


    public boolean validateFields(){
        if(TextUtils.isEmpty(popularName.getText().toString())){
            popularName.setError(getString(R.string.requiredText));
            return false;
        }

        return true;
    }
}
