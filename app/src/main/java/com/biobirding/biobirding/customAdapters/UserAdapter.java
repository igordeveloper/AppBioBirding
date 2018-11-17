package com.biobirding.biobirding.customAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.biobirding.biobirding.entity.User;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter{

    private ArrayList<User> users;
    private static LayoutInflater inflater;

    public UserAdapter(Activity context, ArrayList<User> users){
        Activity context1 = context;
        this.users = users;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    @Override
    public User getItem(int position) {
        return this.users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        view = (view == null) ? inflater.inflate(android.R.layout.simple_list_item_1, null) : view;
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(users.get(position).getFullName());
        return view;
    }
}
