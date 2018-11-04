package com.biobirding.biobirding.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.webservice.ReportCall;
import org.json.JSONException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ReportActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener{

    EditText startDate;
    EditText finishDate;
    Long minDate = 0L;
    Button requestReport;
    private Handler handler = new Handler();
    private Context context;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() !=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);

        requestReport = findViewById(R.id.requestReport);
        requestReport.setEnabled(false);

        startDate = findViewById(R.id.startDate);
        startDate.setShowSoftInputOnFocus(false);
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.setFocusable(false);

        finishDate = findViewById(R.id.finishDate);
        finishDate.setShowSoftInputOnFocus(false);
        finishDate.setInputType(InputType.TYPE_NULL);
        finishDate.setFocusable(false);
        finishDate.setEnabled(false);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment date = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("minDate", minDate);
                date.setArguments(bundle);
                date.show(getSupportFragmentManager(), "igor");
            }
        });

        finishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment date = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("minDate", minDate);
                date.setArguments(bundle);
                date.show(getSupportFragmentManager(), "igor");
            }
        });

        requestReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);
                requestReport.setEnabled(false);

                context = v.getContext();

                new Thread(new Runnable() {

                    String exception = null;
                    Boolean response = false;

                    @Override
                    public void run() {

                        SharedPreferences sharedPref = Objects.requireNonNull(getSharedPreferences("bio", Context.MODE_PRIVATE));
                        String rg = sharedPref.getString("rg_bio", "");
                        ReportCall reportCall = new ReportCall();
                        try {
                            response = reportCall.send(rg, startDate.getText().toString(), finishDate.getText().toString());
                        } catch (InterruptedException | IOException | JSONException e) {
                            exception = e.getMessage();
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                if(exception == null){
                                    if(response){
                                        alert.setMessage(R.string.send_report);
                                    }else{
                                        alert.setMessage(R.string.no_report);
                                        requestReport.setEnabled(true);
                                        loading.setVisibility(View.GONE);
                                        startDate.setText("");
                                        finishDate.setText("");
                                        startDate.setEnabled(true);
                                        minDate = 0L;
                                    }
                                }else{
                                    alert.setMessage(exception);
                                    requestReport.setEnabled(true);
                                    loading.setVisibility(View.GONE);
                                    startDate.setText("");
                                    finishDate.setText("");
                                    startDate.setEnabled(true);
                                    minDate = 0L;
                                }

                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(response){
                                            startActivity(new Intent(ReportActivity.this, MainActivity.class));
                                        }
                                    }
                                });

                                alert.show();

                            }
                        });

                    }
                }).start();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0,0,1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String fullDate = simpleDateFormat.format(calendar.getTime());

        if(minDate == 0L){

            this.startDate.setText(fullDate);
            this.startDate.setEnabled(false);

            this.minDate = calendar.getTimeInMillis();
            this.startDate.setEnabled(false);
            this.finishDate.setEnabled(true);
        }else{
            this.finishDate.setText(fullDate);
            this.finishDate.setEnabled(false);
            this.requestReport.setEnabled(true);
        }
    }
}
