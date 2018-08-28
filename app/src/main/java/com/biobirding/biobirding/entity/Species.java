package com.biobirding.biobirding.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "species")
public class Species implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "scientific_name")
    @NonNull
    private String scientificName;

    @ColumnInfo(name = "characteristics")
    private String characteristics;

    public Species(@NonNull String scientificName){
        this.scientificName = scientificName;

    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    @NonNull
    public String getScientificName() {
        return scientificName;
    }

    public String getCharacteristics() {
        return characteristics;
    }
}