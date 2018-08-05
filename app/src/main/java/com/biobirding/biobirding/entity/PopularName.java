package com.biobirding.biobirding.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "popular_name",
        primaryKeys = {"name", "scientific_name"},
        foreignKeys = @ForeignKey(entity = Species.class,
        parentColumns = "scientific_name",
        childColumns = "scientific_name"))

public class PopularName {

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

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
    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(@NonNull String scientificName) {
        this.scientificName = scientificName;
    }
}
