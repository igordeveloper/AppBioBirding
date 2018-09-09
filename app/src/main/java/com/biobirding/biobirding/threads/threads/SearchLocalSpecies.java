package com.biobirding.biobirding.threads.dao;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.biobirding.biobirding.database.AppDatabase;

import java.util.List;

public class SearchLocalSpecies implements Runnable{

    private Handler handler;
    private String search;
    private Context context;

    public SearchLocalSpecies(Context context, Handler handler, String search) {
        this.context = context;
        this.handler = handler;
        this.search = search;
    }

    @Override
    public void run() {
        List species = AppDatabase.getAppDatabase(context).speciesDao().findByName(this.search);
        Log.d("run", String.valueOf(species.size()));
        Message message = Message.obtain();
        message.obj = species;
        handler.sendMessage(message);
    }
}
