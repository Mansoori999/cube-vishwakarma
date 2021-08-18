package com.vinners.cube_vishwakarma.data.models.complaints

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList

@Entity(tableName = MyComplaintList.TABLE_NAME)
data class MyComplaintList (

        @SerializedName("complaintid")
        val complaintid : String? =null,

        @PrimaryKey
        @SerializedName("id")
        var id : String,

        @SerializedName("fordate")
        var fordate : String? =null,

        @SerializedName("outlet")
        val outlet : String? =null,

        @SerializedName("outletid")
        val outletid : String? =null,

        @SerializedName("customercode")
        val customercode : String? =null,

        @SerializedName("letterstatus")
        val letterstatus : String? =null,

        @SerializedName("status")
        val status : String? =null,

        @SerializedName("outletcategory")
        val outletcategory : String? =null,

        @SerializedName("regionaloffice")
        val regionaloffice : String? =null,

        @SerializedName("salesarea")
        val salesarea : String? =null,

        @SerializedName("roid")
        val roid : Int? =null,

        @SerializedName("said")
        val said : Int? =null,

        @SerializedName("work")
        val work : String? =null,

        @SerializedName("subadmin")
        val subadmin : String? =null,

        @SerializedName("subadminid")
        val subadminid : String? =null,


        @SerializedName("orderby")
        val orderby : String? =null,

        @SerializedName("supervisor")
        val supervisor : String? =null,

        @SerializedName("enduser")
        val enduser : String? =null
        ) {

        companion object {


                @Ignore
                const val TABLE_NAME : String = "myComplaint"

                @Ignore
                const val COLUMN_COMPLAINT_ID :String = "complaintid"

                @Ignore
                const val COLUMN_ID:String = "id"

                @Ignore
                const val COLUMN_FORDATE = "fordate"

                @Ignore
                const val COLUMN_OUTLET_ID:String = "outletid"

                @Ignore
                const val COLUMN_OUTLET_NAME :String = "name"

                @Ignore
                const val  COLUMN_CUSTOMER_CODE = "customercode"

                @Ignore
                const val COLUMN_LETTER_STATUS = "letterstatus"

                @Ignore
                const val COLUMN_STATUS = "status"

                @Ignore
                const val COLUMN_OUTLET_CATEGORY = "outletcategory"

                @Ignore
                const val COLUMN_WORK = "work"

                @Ignore
                const val COLUMN_COMPLAINT_STATUS:String = "status"

                @Ignore
                const val COLUMN_REGIONAL_OFFICE :String = "regionaloffice"

                @Ignore
                const val COLUMN_REGIONAL_OFFICE_ID :String = "roid"

                @Ignore
                const val COLUMN_SALES_AREA :String = "salesarea"

                @Ignore
                const val COLUMN_SALES_AREA_ID :String = "said"

                @Ignore
                const val COLUMN_SUBAMIN_ID :String = "subadmin"




                @Ignore
                val COLUMN_SUBADMIN = "subadmin"


                @Ignore
                val COLUMN_ORDER_BY = "orderby"

                @Ignore
                val COLUMN_SUPERVISOR = "supervisor"

                @Ignore
                val COLUMN_ENDUSER = "enduser"
        }
}