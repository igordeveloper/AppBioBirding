package com.biobirding.biobirding.customAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;

import java.util.ArrayList;

public class PopularNameAdapter extends BaseAdapter{

    private Activity context;
    private ArrayList<PopularName> popularNames;
    private static LayoutInflater inflater;

    public PopularNameAdapter(Activity context, ArrayList<PopularName> popularNames){
        this.context = context;
        this.popularNames = popularNames;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.popularNames.size();
    }

    @Override
    public PopularName getItem(int position) {
        return this.popularNames.get(position);
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
        textView.setText(popularNames.get(position).getName());
        return view;
    }
}
