package com.vinners.cube_vishwakarma.data.models.dashboard

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.util.*


data class DashBoardResponseData(
		val dashBoardResponseData: List<DashBoardResponseDataItem>
)

@Entity(tableName = DashBoardResponseDataItem.TABLE_NAME)
data class DashBoardResponseDataItem(
		@PrimaryKey
		val dashboardCount: List<DashboardCountItem> = emptyList(),

		val monthWiseChart: MonthWiseChart? = null,

//		@TypeConverters(ROWiseConverter::class)
		val roWiseChart: RoWiseChart? = null

//		@ColumnInfo(name = TABLE_DASHBOARD_COUNT_COLUMN)
//		@ColumnInfo(name = TABLE_MONTHWISE_COLUMN)
//		@ColumnInfo(name = TABLE_ROWISE_COLUMN)

		){
	companion object {
		@Ignore
		const val TABLE_NAME : String = "dashboard"

		@Ignore
		const val TABLE_DASHBOARD_COUNT_COLUMN  = "dashboardCount"

		@Ignore
		const val TABLE_MONTHWISE_COLUMN  = "monthWiseChart"
		@Ignore
		const val TABLE_ROWISE_COLUMN  = "roWiseChart"


	}

//	@PrimaryKey(autoGenerate = true)
//	@ColumnInfo(name = "id")
//	var id: Int = 0
}

data class RoWiseChart(
		val roList: List<String> = emptyList(),
		val data: List<Int> = emptyList()
)

data class MonthWiseChart(
		val data: List<Int> = emptyList(),
		val monthList: List<String> = emptyList()
)

data class DashboardCountItem(
		val total: String? = null,
		val billed: String? = null,
		val due: String? = null,
		val draft: String? = null,
		val estimated: String? = null,
		val cancelled: String? = null,
		val working: String? = null,
		val payment: String? = null,
		val done: String? = null,
		val pendingletter: String? = null,
		val hold: String? = null
)

class Converter {

	@TypeConverter
	fun fromString(value: String?): List<DashboardCountItem> {
		val listType = object :
				TypeToken<List<DashboardCountItem?>?>() {}.type
		return Gson()
				.fromJson<List<DashboardCountItem>>(value, listType)
	}

	@TypeConverter
	fun listToString(list: List<DashboardCountItem?>?): String {
		val gson = Gson()
		return gson.toJson(list)
	}
}

class MonthWiseConverter{
	@TypeConverter
	fun fromMediaToJson(stat: MonthWiseChart): String {
		return Gson().toJson(stat)
	}
	@TypeConverter
	fun fromJsonToMedia(jsonData: String): MonthWiseChart {
		val type = object : TypeToken<MonthWiseChart>() {}.type
		return Gson().fromJson<MonthWiseChart>(jsonData, type)
	}
}

class ROWiseConverter{
	@TypeConverter
	fun fromMediaToJson(stat: RoWiseChart): String {
		return Gson().toJson(stat)
	}
	@TypeConverter
	fun fromJsonToMedia(jsonData: String): RoWiseChart {
		val type = object : TypeToken<RoWiseChart>() {}.type
		return Gson().fromJson<RoWiseChart>(jsonData, type)
	}
}




