package com.biobirding.biobirding.entity;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "catalog")
public class LocalCatalog implements Serializable {



    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Integer id;

    @NonNull
    @ColumnInfo(name = "rg")
    private String rg;

    @NonNull
    @ColumnInfo(name = "species")
    private Integer species;

    @NonNull
    @ColumnInfo(name = "age")
    private String age;

    @NonNull
    @ColumnInfo(name = "sex")
    private String sex;

    @NonNull
    @ColumnInfo(name = "latitude")
    private Double latitude;

    @NonNull
    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "identification_code")
    private String identificationCode;

    @NonNull
    @ColumnInfo(name = "timestamp")
    private Long timestamp;

    public Integer getId() { return id;}

    public void setId(Integer id) { this.id = id; }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public Integer getSpecies() {
        return species;
    }

    public void setSpecies(Integer species) {
        this.species = species;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIdentificationCode() {
        return identificationCode;
    }

    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
}

