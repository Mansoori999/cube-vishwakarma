package com.vinners.cube_vishwakarma.data.models.dashboard

import com.google.gson.annotations.SerializedName

data class DashBoardResponse(
        @SerializedName("total")
        val total : String? =null,

        @SerializedName("done")
        val done : String? =null,

        @SerializedName("due")
        val due : String? =null,

        @SerializedName("working")
        val working : String? =null,

        @SerializedName("pendingletter")
        val pendingletter : String? =null,

        @SerializedName("estimated")
        val estimated : String? =null,

        @SerializedName("draft")
        val draft : String? =null,

        @SerializedName("billed")
        val billed : String? =null,

        @SerializedName("payment")
        val payment : String? =null

        ) {
}