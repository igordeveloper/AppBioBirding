package com.biobirding.biobirding.dao;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.biobirding.biobirding.entity.Species;

import java.util.List;

@Dao
public interface SpeciesDao {

    @Query("SELECT * FROM species where id = :id")
    Species get(Integer id);

    @Query("SELECT * FROM species")
    List<Species> getAll();

    @Query("SELECT scientific_name FROM species WHERE scientific_name LIKE '%' || :search || '%'")
    List<String> findByName(String search);

    @Query("DELETE FROM species")
    void deleteAll();

    @Insert
    void insert(Species species);
}