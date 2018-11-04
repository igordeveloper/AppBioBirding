package com.biobirding.biobirding.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "popular_names",
        primaryKeys = {"name", "id", "scientific_name"}
)

public class LocalSpecies implements Serializable{

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "id")
    private Integer id;

    @NonNull
    @ColumnInfo(name = "scientific_name")
    private String scientificName;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(@NonNull String scientificName) {
        this.scientificName = scientificName;
    }
}
