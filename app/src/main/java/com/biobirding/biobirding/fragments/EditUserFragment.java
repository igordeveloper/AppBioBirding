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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.User;
import com.biobirding.biobirding.webservice.UserCall;

import org.json.JSONException;

import java.io.IOException;

public class EditUserFragment extends Fragment {

    private Spinner spinner;
    private EditText fullName;
    private EditText email;
    private EditText nickname;
    private EditText crBio;
    private EditText rg;
    private Button editUser;
    private Handler handler = new Handler();
    private Context context;
    private User user;
    private RadioButton enabled;
    private RadioButton disabled;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        this.context = getContext();

        this.spinner = view.findViewById(R.id.accessLevelList);
        this.fullName = view.findViewById(R.id.fullName);
        this.rg = view.findViewById(R.id.rg);
        this.email = view.findViewById(R.id.email);
        this.nickname = view.findViewById(R.id.nickname);
        this.crBio = view.findViewById(R.id.crbio);
        this.crBio = view.findViewById(R.id.crbio);
        this.editUser = view.findViewById(R.id.editUser);
        enabled  = view.findViewById(R.id.enabled);
        disabled  = view.findViewById(R.id.disabled);

        if(getArguments() != null){
            Bundle bundle = getArguments();
            this.user = (User) bundle.getSerializable("user");

            new Thread(new Runnable() {

                String exception;
                User userResponse;

                @Override
                public void run() {

                    UserCall userCall = new UserCall();
                    try {
                        userResponse = userCall.select(user);
                    } catch (InterruptedException | IOException | JSONException e) {
                        exception = e.getMessage();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            AlertDialog.Builder alert = new AlertDialog.Builder(context);

                            if(exception == null){
                                if(user.getRg().equals(userResponse.getRg())){

                                    fullName.setText(userResponse.getFullName());
                                    rg.setText(userResponse.getRg());
                                    email.setText(userResponse.getEmail());
                                    nickname.setText(userResponse.getNickname());
                                    crBio.setText(userResponse.getCrBio());
                                    spinner.setSelection(userResponse.getAccessLevel());

                                    if(userResponse.getEnabled()){
                                        enabled.setChecked(true);
                                    }else{
                                        disabled.setChecked(true);
                                    }

                                }else{
                                    alert.setMessage(R.string.fail);
                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            redirectActivity();
                                        }
                                    });
                                    alert.show();

                                }
                            }else{
                                alert.setMessage(exception);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        redirectActivity();
                                    }
                                });
                                alert.show();
                            }



                        }
                    });
                }
            }).start();

        }

        if (getContext() != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.accessLevel, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(adapter);
        }

        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editUser.setEnabled(false);

                if(validateFields()) {

                    new Thread(new Runnable() {

                        String exception = null;
                        Boolean response = false;

                        @Override
                        public void run() {

                            Integer accessLevel = 0;
                            Boolean status = null;
                            if(spinner.getSelectedItemId() != 0){
                                accessLevel = (int) spinner.getSelectedItemId();
                            }

                            if(enabled.isChecked()){
                                status = true;
                            }
                            if(disabled.isChecked()){
                                status = false;
                            }

                            User user = new User();
                            user.setRg(rg.getText().toString());
                            user.setFullName(fullName.getText().toString());
                            user.setCrBio(crBio.getText().toString());
                            user.setAccessLevel(accessLevel);
                            user.setEnabled(status);

                            UserCall userCall = new UserCall();
                            try {
                                response = userCall.update(user);
                            } catch (InterruptedException | IOException | JSONException e) {
                                exception = e.getMessage();
                            }


                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                    if(exception == null){
                                        if(response){
                                            alert.setMessage(R.string.msg_update_user);
                                        }else{
                                            alert.setMessage(R.string.fail);
                                            editUser.setEnabled(true);
                                        }
                                    }else{
                                        alert.setMessage(exception);
                                        editUser.setEnabled(true);
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

    public boolean validateFields(){
        if(TextUtils.isEmpty(fullName.getText().toString())){
            fullName.setError(getString(R.string.requiredText));
            editUser.setEnabled(true);
            return false;
        }

        if(TextUtils.isEmpty(rg.getText().toString())){
            rg.setError(getString(R.string.requiredText));
            editUser.setEnabled(true);
            return false;
        }


        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError(getString(R.string.requiredText));
            editUser.setEnabled(true);
            return false;
        }

        if(TextUtils.isEmpty(nickname.getText().toString())){
            nickname.setError(getString(R.string.requiredText));
            editUser.setEnabled(true);
            return false;
        }



        if(TextUtils.isEmpty(fullName.getText().toString())){
            fullName.setError(getString(R.string.requiredText));
            editUser.setEnabled(true);
            return false;
        }

        if (spinner.getSelectedItemId() == 0) {
            ((TextView)spinner.getSelectedView()).setError(getString(R.string.requiredText));
            editUser.setEnabled(true);
            return false;
        }

        return true;
    }

    public void redirectActivity(){
        if(getFragmentManager() != null) {
            ListUsersFragment listUsersFragment = new ListUsersFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, listUsersFragment);
            transaction.commit();
        }
    }

}