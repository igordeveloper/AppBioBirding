package com.biobirding.biobirding.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.biobirding.biobirding.entity.Species;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment{

    private Long minDate;
    private Long maxDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        this.minDate = getArguments().getLong("minDate");

        if(this.minDate == 0L){
            calendar.set(2018, 0, 1, 0,0,1);
            this.minDate = calendar.getTimeInMillis();
        }else{
            this.minDate = getArguments().getLong("minDate");
            long oneYear = 1000L * 60 * 60 * 24 * 365;
            this.maxDate = this.minDate + oneYear;
        }

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        dialog.getDatePicker().setMinDate(this.minDate);

        if(getArguments().getLong("minDate") > 0L){
            dialog.getDatePicker().setMaxDate(this.maxDate);
        }

        return dialog;
    }
}
