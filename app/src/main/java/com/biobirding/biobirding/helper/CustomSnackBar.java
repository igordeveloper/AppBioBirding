package com.biobirding.biobirding.helper;

import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.biobirding.biobirding.R;
import com.biobirding.biobirding.entity.User;

import java.io.Serializable;

public class CustomSnackBar {

    public static void make(View view, String message) {

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        TextView textView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setAllCaps(true);
        textView.setTextSize(16);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        snackbar.show();

    }

    public static class Search implements Serializable {

        private User user;
        private String fullName;
        private String email;
        private String nickname;
    }
}
