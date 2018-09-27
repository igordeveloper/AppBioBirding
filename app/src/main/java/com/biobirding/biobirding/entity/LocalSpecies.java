package com.biobirding.biobirding.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "popular_name",
        primaryKeys = {"name", "id"},
        foreignKeys = @ForeignKey(entity = Species.class,
        parentColumns = "id",
        childColumns = "id"),
        indices = {@Index("id")}
)

public class PopularName implements Serializable{

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "id")
    private Integer id;

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
}
