package com.biobirding.biobirding.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.biobirding.biobirding.entity.LocalSpecies;

import java.util.List;

@Dao
public interface LocalSpeciesDao {

    @Query("SELECT * FROM popular_names")
    List<LocalSpecies> getAll();

    @Query("SELECT * FROM popular_names " +
            "where scientific_name LIKE '%' || :value || '%'" +
            "OR name LIKE '%' || :value || '%'" +
            "GROUP BY scientific_name ")
    List<LocalSpecies> search(String value);

    @Insert
    void insert(LocalSpecies localSpecies);

    @Query("DELETE FROM popular_names")
    void deleteAll();
}