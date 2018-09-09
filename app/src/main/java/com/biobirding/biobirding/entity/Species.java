package com.biobirding.biobirding.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "species")
public class Species implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer id;

    @ColumnInfo(name = "scientific_name")
    @NonNull
    private String scientificName;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "conservationState")
    private String conservationState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(@NonNull String scientificName) {
        this.scientificName = scientificName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getConservationState() {
        return conservationState;
    }

    public void setConservationState(String conservationState) {
        this.conservationState = conservationState;
    }
}