package com.vinners.cube_vishwakarma.data.models.outlets

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = OutletsList.TABLE_NAME)
data class OutletsList(

        @PrimaryKey
        @SerializedName("id")
        val outletid : String,

        @SerializedName("name")
        val name : String? =null,

        @SerializedName("customercode")
        val customercode : String? =null,

        @SerializedName("regionaloffice")
        val regionaloffice : String? =null,

        @SerializedName("salesarea")
        val salesarea : String? =null,

        @SerializedName("roid")
        val roid : Int? =null,

        @SerializedName("said")
        val said : Int? =null,

        @SerializedName("districtname")
        val districtname : String? =null,



        @SerializedName("gps")
        val gps : String? =null,


        ) {

        companion object {

                @Ignore
                const val TABLE_NAME : String = "outlets"

                @Ignore
                const val COLUMN_OUTLET_ID :String = "outletid"

                @Ignore
                val COLUMN_SUBADMIN_ID = "allocatedSubadminID"

                @Ignore
                val COLUMN_SUBADMIN_NAME = "allocatedSubadminName"

                @Ignore
                val COLUMN_SUPERVISOR_ID = "allocatedSupervisorID"

                @Ignore
                val COLUMN_SUPERVISOR_NAME = "allocatedSupervisorName"

                @Ignore
                val COLUMN_FIELDUSER_ID = "allocatedFielduserID"

                @Ignore
                val COLUMN_FIELDUSER_NAME = "allocatedFieldUserName"

                @Ignore
                val COLUMN_COMPLAINT_STATUS = "status"

                @Ignore
                const val COLUMN_REGIONAL_OFFICE :String = "regionaloffice"

                @Ignore
                const val COLUMN_SALES_AREA :String = "salesarea"

                @Ignore
                const val COLUMN_OUTLET_NAME :String = "name"

                @Ignore
               const val COLUMN_CUSTOMER_CODE = "customercode"

                @Ignore
                val COLUMN_CATEGORY = "category"

                @Ignore
                val COLUMN_LETTER_STATUS = "letterStatus"

                @Ignore
                val COLUMN_WORK = "work"

                @Ignore
                val COLUMN_ENGINEER_NAME = "engineerName"

                @Ignore
                val COLUMN_ENGINEER_DESIGNATION = "engineerDesignation"
        }
}