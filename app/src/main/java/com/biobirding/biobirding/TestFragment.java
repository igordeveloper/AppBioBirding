package com.biobirding.biobirding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class TestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getContext();

        View view = inflater.inflate(R.layout.fragment_test, container, false);
        TextView text = (TextView) view.findViewById(R.id.input);
        text.setText(getArguments().getString("specieName"));
        return view;
    }


}
