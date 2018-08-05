package com.biobirding.biobirding.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "species")
public class Species {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "scientific_name")
    private String scientificName;

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    @ColumnInfo(name = "characteristics")
    private String characteristics;


}