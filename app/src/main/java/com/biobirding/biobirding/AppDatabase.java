package com.biobirding.biobirding;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.biobirding.biobirding.dao.PopularNameDao;
import com.biobirding.biobirding.dao.SpeciesDao;
import com.biobirding.biobirding.entity.PopularName;
import com.biobirding.biobirding.entity.Species;

@Database(entities = {Species.class, PopularName.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SpeciesDao speciesDao();
    public abstract PopularNameDao popularNameDao();
}