package com.biobirding.biobirding.entity;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity(tableName = "last_update")
public class LastUpdate implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    private Long timestamp;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
