package com.biobirding.biobirding.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.biobirding.biobirding.R;

public final class CustomSimpleDialog extends AlertDialog.Builder {

    public CustomSimpleDialog(Context context, String message) {
        super(context);
        setCancelable(false);
        setMessage(message);
        setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
    }
}
