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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;
import com.biobirding.biobirding.threads.DeletePopularNameThread;
import com.biobirding.biobirding.threads.InsertPopularNameThread;
import com.biobirding.biobirding.threads.UpdatePopularNameThread;
import com.biobirding.biobirding.threads.UpdateSpecieThread;

public class EditPopularNameFragment extends Fragment {

    private EditText popularNameText;
    private Button editPopularName;
    private Species species;
    private PopularName popularName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_popular_name, container, false);

        if(getArguments() != null) {
            Bundle bundle = getArguments();
            this.species = (Species) bundle.getSerializable("species");
            this.popularName = (PopularName) bundle.getSerializable("popularName");
        }


        TextView scientificName = view.findViewById(R.id.scientific_name);
        scientificName.setText(species.getScientificName());

        Button deletePopularName = view.findViewById(R.id.delete_popular_name);
        deletePopularName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext() != null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage(R.string.message_delete_popular_name);
                    alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                                                    editPopularName.setEnabled(true);
                                                }
                                            });
                                            alert.show();
                                        }

                                        if(response.getClass() == Boolean.class ){
                                            alert.setMessage(R.string.delete_popular_name);
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

                            DeletePopularNameThread deletePopularNameThread = new DeletePopularNameThread(handler, popularName);
                            deletePopularNameThread.start();
                        }
                    });
                    alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();
                }
            }
        });


        this.popularNameText = view.findViewById(R.id.popular_name);
        this.popularNameText.setText(popularName.getName());

        this.editPopularName = view.findViewById(R.id.edit_popular_name);
        this.editPopularName.setOnClickListener(new View.OnClickListener() {
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
                                            editPopularName.setEnabled(true);
                                        }
                                    });
                                    alert.show();
                                }

                                if(response.getClass() == Boolean.class ){
                                    alert.setMessage(R.string.update_popular_name);
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

                    UpdatePopularNameThread updatePopularNameThread;
                    updatePopularNameThread = new UpdatePopularNameThread(handler, popularName, popularNameText.getText().toString());
                    updatePopularNameThread.start();
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
