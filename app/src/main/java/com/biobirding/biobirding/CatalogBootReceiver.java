package com.biobirding.biobirding;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Random;

public class CatalogBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent catalogIntent = new Intent(context, CatalogReceiver.class);
            PendingIntent alarmCatalogIntent = PendingIntent.getBroadcast(context, 0, catalogIntent, 0);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    60000, alarmCatalogIntent);
        }
    }
}