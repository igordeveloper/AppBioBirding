package com.biobirding.biobirding.customAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.helper.CatalogHelper;

import java.util.ArrayList;

public class ListCatalogAdapter extends BaseAdapter{

    private Activity context;
    private ArrayList<CatalogHelper> catalogHelpers;
    private static LayoutInflater inflater;

    public ListCatalogAdapter(Activity context, ArrayList<CatalogHelper> catalogHelpers){
        this.context = context;
        this.catalogHelpers = catalogHelpers;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.catalogHelpers.size();
    }

    @Override
    public CatalogHelper getItem(int position) {
        return this.catalogHelpers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        view = (view == null) ? inflater.inflate(R.layout.list_view_catalog, null) : view;
        TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);

        String item1 = catalogHelpers.get(position).getSpecies() + "\n";

        String item2 = view.getContext().getString(R.string.age) + ": " + catalogHelpers.get(position).getAge() + " " +
                view.getContext().getString(R.string.sex) + ": " + catalogHelpers.get(position).getSex() + "\n"+
                view.getContext().getString(R.string.state) + ": " + catalogHelpers.get(position).getState() + "\n"+
                view.getContext().getString(R.string.city) + ": " + catalogHelpers.get(position).getCity()+ "\n"+
                view.getContext().getString(R.string.date) + ": " + catalogHelpers.get(position).getDate()+ "\n";

        textView1.setText(item1);
        textView2.setText(item2);

        return view;
    }
}
