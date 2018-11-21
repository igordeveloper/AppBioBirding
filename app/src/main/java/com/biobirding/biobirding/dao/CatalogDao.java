package com.biobirding.biobirding.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.biobirding.biobirding.entity.LocalCatalog;

import java.util.List;

@Dao
public interface CatalogDao {

    @Insert
    void insert(LocalCatalog localCatalog);

    @Query("SELECT * FROM catalog")
    List<LocalCatalog> getAll();

    @Query("DELETE FROM catalog where id=:id")
    void delete(Integer id);
}