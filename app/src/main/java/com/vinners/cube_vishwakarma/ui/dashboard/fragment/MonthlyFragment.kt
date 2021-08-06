package com.vinners.cube_vishwakarma.ui.dashboard.fragment


import android.graphics.Color
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseFragment
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.FragmentMothlyBinding


import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.dashboard.MainActivityViewModel
import javax.inject.Inject

class MonthlyFragment() : BaseFragment<FragmentMothlyBinding, MainActivityViewModel>(R.layout.fragment_mothly){

    companion object {
        fun newInstance() = MonthlyFragment()

    }

    var dataEnteriesYAxis = listOf<Int>()
    var monthlyDataXAxis = listOf<String>()


    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        return inflater.inflate(R.layout.fragment_mothly, container, false)
//    }

    override val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {

    }

    override fun onInitViewModel() {
        viewModel.dashboardDaoState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.loadingData.setVisibilityVisible()
                }
                is Lce.Content -> {
                    viewBinding.loadingData.setVisibilityGone()
                    dataEnteriesYAxis =  it.content.monthWiseChart!!.data.toList()
                    monthlyDataXAxis = it.content.monthWiseChart!!.monthList
                    monthWiseBarChart()


                }
                is Lce.Error -> {
                    viewBinding.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)

                }
            }
        })
        viewModel.getDashboardDaoData()

    }

    private fun monthWiseBarChart(){
        val barEntries: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until dataEnteriesYAxis.size) {
            barEntries.add(BarEntry(i.toFloat(), dataEnteriesYAxis[i].toFloat()))
        }

        val barDataSet = BarDataSet(barEntries,"Complaint")
        barDataSet.setColor(Color.parseColor("#2383E1"))
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.setFormSize(15f)
        viewBinding.barChart.getDescription().setEnabled(false)
        val xAxis = viewBinding.barChart.getXAxis()
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setDrawGridLines(false)
        xAxis.setCenterAxisLabels(false)
        xAxis.axisMaximum = monthlyDataXAxis.size.toFloat()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.valueFormatter = IndexAxisValueFormatter(monthlyDataXAxis)
        val barData = BarData(barDataSet)
        viewBinding.barChart.setFitBars(true)
        viewBinding.barChart.setDragEnabled(true)
        viewBinding.barChart.setData(barData)
        barData.barWidth = 0.5f
        viewBinding.barChart.setExtraOffsets(5f,5f,5f,15f)
        //Y-axis
        viewBinding.barChart.getAxisRight().setEnabled(false)
        val leftAxis: YAxis = viewBinding.barChart.getAxisLeft()
        leftAxis.setDrawGridLines(true)
//        leftAxisro.setSpaceTop(15f)
        leftAxis.setAxisMinimum(0f)
        val yAxisRight: YAxis = viewBinding.barChart.getAxisRight()
        yAxisRight.setEnabled(false)
        viewBinding.barChart.setExtraBottomOffset(1f)
//        viewBinding.contentMainContainer.barChart.setVisibleXRangeMaximum(12f)
//        viewBinding.contentMainContainer.barChart.setVisibleXRangeMinimum(12f)
        viewBinding.barChart.invalidate()

    }


}