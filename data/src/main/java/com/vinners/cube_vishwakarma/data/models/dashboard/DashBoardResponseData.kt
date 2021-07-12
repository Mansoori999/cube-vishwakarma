package com.vinners.cube_vishwakarma.data.models.dashboard

data class DashBoardResponseData(
	val dashBoardResponseData: List<DashBoardResponseDataItem>
)

data class DashBoardResponseDataItem(
	val dashboardCount: List<DashboardCountItem> = emptyList(),
	val monthWiseChart: MonthWiseChart? = null,
	val roWiseChart: RoWiseChart? = null
)

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

