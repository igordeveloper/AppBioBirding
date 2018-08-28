package com.biobirding.biobirding.database;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.biobirding.biobirding.dao.PopularNameDao;
import com.biobirding.biobirding.dao.SpeciesDao;
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;

@Database(entities = {Species.class, PopularName.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract SpeciesDao speciesDao();
    public abstract PopularNameDao popularNameDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Species")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}