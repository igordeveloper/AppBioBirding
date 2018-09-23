package com.biobirding.biobirding.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.biobirding.biobirding.entity.PopularName;

import java.util.List;

@Dao
public interface PopularNameDao {
    @Query("SELECT * FROM popular_name")
    List<PopularName> getAll();

    @Insert
    void insert(PopularName popularName);
}