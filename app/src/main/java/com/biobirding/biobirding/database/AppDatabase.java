package com.biobirding.biobirding.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.biobirding.biobirding.dao.CatalogDao;
import com.biobirding.biobirding.dao.LastUpdateDao;
import com.biobirding.biobirding.dao.LocalSpeciesDao;
import com.biobirding.biobirding.entity.LocalCatalog;
import com.biobirding.biobirding.entity.LastUpdate;
import com.biobirding.biobirding.entity.LocalSpecies;


@Database(entities = {LocalSpecies.class, LastUpdate.class, LocalCatalog.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract LocalSpeciesDao localSpeciesDao();
    public abstract LastUpdateDao lastUpdateDao();
    public abstract CatalogDao catalogDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "BioBirding")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}