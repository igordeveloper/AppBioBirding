package com.biobirding.biobirding.customAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.biobirding.biobirding.entity.LocalSpecies;
import com.biobirding.biobirding.entity.Species;

import java.util.ArrayList;

public class LocalSpeciesAdapter extends BaseAdapter{

    private Activity context;
    private ArrayList<LocalSpecies> localSpecies;
    private static LayoutInflater inflater;

    public LocalSpeciesAdapter(Activity context, ArrayList<LocalSpecies> localSpecies){
        this.context = context;
        this.localSpecies = localSpecies;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.localSpecies.size();
    }

    @Override
    public LocalSpecies getItem(int position) {
        return this.localSpecies.get(position);
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
        String item = localSpecies.get(position).getScientificName() +"\n"+localSpecies.get(position).getName()+"\n";
        textView.setText(item);
        return view;
    }
}
