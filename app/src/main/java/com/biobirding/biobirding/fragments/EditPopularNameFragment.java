package com.biobirding.biobirding.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.webservice.PopularNameCall;

import org.json.JSONException;

import java.io.IOException;

public class EditPopularNameFragment extends Fragment {

    private EditText popularNameText;
    private Button editPopularName;
    private Species species;
    private PopularName popularName;
    private Handler handler = new Handler();
    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_popular_name, container, false);

        if(getArguments() != null) {
            Bundle bundle = getArguments();
            this.species = (Species) bundle.getSerializable("species");
            this.popularName = (PopularName) bundle.getSerializable("popularName");
        }

        this.context = getContext();

        TextView scientificName = view.findViewById(R.id.scientific_name);
        scientificName.setText(species.getScientificName());

        Button deletePopularName = view.findViewById(R.id.delete_popular_name);
        deletePopularName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage(R.string.message_delete_popular_name);
                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PopularNameCall popularNameCall = new PopularNameCall();
                                try {
                                    popularNameCall.delete(popularName);
                                } catch (InterruptedException | IOException | JSONException e) {
                                    e.printStackTrace();
                                }

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        redirectActivity();
                                    }
                                });
                            }
                        }).start();
                    }
                });

                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }

        });


        this.popularNameText = view.findViewById(R.id.popular_name);
        this.popularNameText.setText(popularName.getName());
        this.popularNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && getActivity()!= null){
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputMethodManager != null){
                        inputMethodManager.hideSoftInputFromWindow(popularNameText.getWindowToken(), 0);
                    }
                }
            }
        });

        this.editPopularName = view.findViewById(R.id.edit_popular_name);
        this.editPopularName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()) {

                    new Thread(new Runnable() {

                        String exception = null;
                        Boolean response = false;

                        @Override
                        public void run() {

                            PopularNameCall popularNameCall = new PopularNameCall();
                            try {
                                response = popularNameCall.update(popularName, popularNameText.getText().toString());
                            } catch (InterruptedException | IOException | JSONException e) {
                                exception = e.getMessage();
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                    if(exception == null){
                                        if(response){
                                            alert.setMessage(R.string.update_popular_name);
                                        }else{
                                            alert.setMessage(R.string.fail);
                                            editPopularName.setEnabled(true);
                                        }
                                    }else{
                                        alert.setMessage(exception);
                                        editPopularName.setEnabled(true);
                                    }

                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(response){
                                                redirectActivity();
                                            }
                                        }
                                    });

                                    alert.show();
                                }
                            });
                        }
                    }).start();

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
        if(TextUtils.isEmpty(popularNameText.getText().toString())){
            popularNameText.setError(getString(R.string.requiredText));
            return false;
        }

        return true;
    }
}
