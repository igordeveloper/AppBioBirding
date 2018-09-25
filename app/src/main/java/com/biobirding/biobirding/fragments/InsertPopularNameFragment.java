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

public class InsertPopularNameFragment extends Fragment {

    private EditText popularName;
    private Button addPopularName;
    private Species species;
    private Handler handler = new Handler();
    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_popular_name, container, false);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            this.species = (Species) bundle.getSerializable("species");
        }

        this.context = getContext();

        this.addPopularName = view.findViewById(R.id.edit_popular_name);
        TextView scientificName = view.findViewById(R.id.scientific_name);
        this.popularName = view.findViewById(R.id.popular_name);
        this.popularName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && getActivity()!= null){
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputMethodManager != null){
                        inputMethodManager.hideSoftInputFromWindow(popularName.getWindowToken(), 0);
                    }
                }
            }
        });

        //Hide keyboard
        if(getActivity() != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager != null){
                inputMethodManager.hideSoftInputFromWindow(popularName.getWindowToken(), 0);
            }
        }
        scientificName.setText(species.getScientificName());

        addPopularName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()) {

                    new Thread(new Runnable() {

                        String exception = null;
                        Boolean response = false;

                        @Override
                        public void run() {

                            PopularName name = new PopularName();
                            name.setName(popularName.getText().toString());
                            name.setId(species.getId());

                            PopularNameCall popularNameCall = new PopularNameCall();
                            try {
                                response = popularNameCall.insert(name);
                            } catch (InterruptedException | JSONException | IOException e) {
                                exception = e.getMessage();
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                    if(exception == null){
                                        if(response){
                                            alert.setMessage(R.string.insert_popular_name);
                                        }else{
                                            alert.setMessage(R.string.fail);
                                            addPopularName.setEnabled(true);
                                        }
                                    }else{
                                        alert.setMessage(exception);
                                        addPopularName.setEnabled(true);
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
        if(TextUtils.isEmpty(popularName.getText().toString())){
            popularName.setError(getString(R.string.requiredText));
            return false;
        }

        return true;
    }
}
