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

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.customAdapters.UserAdapter;
import com.biobirding.biobirding.entity.User;
import com.biobirding.biobirding.webservice.UserCall;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class ListUsersFragment extends Fragment {

    private ListView listView;
    private ArrayList<User> userList;
    private UserAdapter adapter;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_users, container, false);
        this.listView = view.findViewById(R.id.usersListView);
        initList(getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Receive user object
                User user = (User) parent.getAdapter().getItem(position);

                //Change to EditUserFragment
                EditUserFragment editUserFragment = new EditUserFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                editUserFragment.setArguments(bundle);

                if(getFragmentManager() != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, editUserFragment);
                    transaction.commit();
                }
            }
        });

        FloatingActionButton add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getFragmentManager() != null) {
                    InsertUserFragment insertUserFragment = new InsertUserFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, insertUserFragment);
                    transaction.commit();
                }
            }
        });

        selectUsers();
        return view;
    }


    public void selectUsers(){

        new Thread(new Runnable() {

            ArrayList<User> users;

            @Override
            public void run() {

                UserCall userCall = new UserCall();
                try {
                    users = userCall.selectAll();
                } catch (InterruptedException | IOException | JSONException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userList.clear();
                        userList.addAll(users);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

    public void initList(Context context){
        userList = new ArrayList<>();
        adapter = new UserAdapter((Activity) context, userList);
        this.listView.setAdapter(adapter);
    }

}
