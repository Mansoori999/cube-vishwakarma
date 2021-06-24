package com.vinners.cube_vishwakarma.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = CachedOutlets.TABLE_NAME)
data class CachedOutlets(

    @ColumnInfo(name = COLUMN_NAME)
    var name: String? = null,

    @ColumnInfo(name = COLUMN_CUSTOMER_CODE)
    var customercode: String? = null,

    @ColumnInfo(name = COLUMN_REGIONAL_OFFICE)
    var regionaloffice: String? = null,

    @ColumnInfo(name = COLUMN_SALES_AREA)
    var salesarea: String? = null,

    @ColumnInfo(name = COLUMN_REGIONAL_OFFICE_ID)
    var roid: Int? = null,

    @ColumnInfo(name = COLUMN_SALES_AREA_ID)
    var said: Int? = null,

    @ColumnInfo(name = COLUMN_DITRICT_NAME)
    var districtname: String? = null,

    @ColumnInfo(name = COLUMN_OUTLET_ID)
    var outletid: String? = null,

    @ColumnInfo(name = COLUMN_GPS)
    var gps: String? = null,


) {

    companion object {
        @Ignore
        const val TABLE_NAME: String = "outlets"

        @Ignore
        const val COLUMN_NAME: String = "name"


        @Ignore
        const val COLUMN_CUSTOMER_CODE: String = "customercode"

        @Ignore
        const val COLUMN_REGIONAL_OFFICE: String = "regionaloffice"

        @Ignore
        const val COLUMN_SALES_AREA: String = "salesarea"

        @Ignore
        const val COLUMN_REGIONAL_OFFICE_ID: String = "roid"

        @Ignore
        const val COLUMN_SALES_AREA_ID: String = "said"


        @Ignore
        const val COLUMN_DITRICT_NAME: String = "districtname"

        @Ignore
        const val COLUMN_OUTLET_ID: String = "outletid"


        @Ignore
        const val COLUMN_GPS: String = "gps"

    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

//    constructor(customercode: String?, outletid: String) {
//        this.customercode = customercode
//        this.outletid = outletid
//    }

}