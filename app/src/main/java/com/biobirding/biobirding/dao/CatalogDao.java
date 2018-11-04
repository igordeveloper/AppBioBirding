package com.biobirding.biobirding.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.biobirding.biobirding.entity.Catalog;

import java.util.List;

@Dao
public interface CatalogDao {

    @Insert
    void insert(Catalog catalog);

    @Query("SELECT * FROM catalog")
    List<Catalog> getAll();

    @Query("DELETE FROM catalog where id=:id")
    void delete(Integer id);
}