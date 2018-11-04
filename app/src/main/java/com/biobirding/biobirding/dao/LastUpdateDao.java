package com.biobirding.biobirding.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.biobirding.biobirding.entity.LastUpdate;
import com.biobirding.biobirding.entity.Species;

import java.util.List;

@Dao
public interface LastUpdateDao {

    @Query("SELECT * FROM last_update ORDER BY timestamp DESC LIMIT 1")
    Long getAll();

    @Insert
    void insert(LastUpdate lastUpdate);

    @Query("DELETE FROM last_update")
    void deleteAll();

}