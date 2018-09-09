package com.biobirding.biobirding.customAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.Species;

public class SpeciesAdapter extends BaseAdapter{

    private Activity context;
    private ArrayList<Species> species;
    private static LayoutInflater inflater;

    public SpeciesAdapter(Activity context, ArrayList<Species> species){
        this.context = context;
        this.species = species;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.species.size();
    }

    @Override
    public Species getItem(int position) {
        return this.species.get(position);
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
        textView.setText(species.get(position).getScientificName());
        return view;
    }
}
