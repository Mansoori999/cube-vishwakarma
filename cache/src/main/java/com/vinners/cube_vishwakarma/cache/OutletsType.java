package com.vinners.cube_vishwakarma.cache;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by for u on 9/24/2017.
 */
@Entity(tableName = OutletsType.TABLE_NAME)
public class OutletsType {

    @Ignore
    public static final String TABLE_NAME = "OutletsType";

    @Ignore
    public static final String COLUMN_TYPE = "type";

    @PrimaryKey
    @NonNull
    private String type;

    public OutletsType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return getType();
    }
}
