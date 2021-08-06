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
import com.vinners.cube_vishwakarma.databinding.FragmentROWiseBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.dashboard.MainActivityViewModel
import javax.inject.Inject


class ROWiseFragment : BaseFragment<FragmentROWiseBinding, MainActivityViewModel>(R.layout.fragment_r_o_wise) {
    companion object {
        fun newInstance() = ROWiseFragment()

    }

    var roDataEnteriesYAxis = listOf<Int>()
    var roListXAxis = listOf<String>()

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager
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
                    roDataEnteriesYAxis = it.content.roWiseChart!!.data.toList()
                    roListXAxis = it.content.roWiseChart!!.roList
                    roWiseBarChart()

                }
                is Lce.Error -> {
                    viewBinding.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)

                }
            }
        })
        viewModel.getDashboardDaoData()

    }

    private fun roWiseBarChart() {
        val roBarEntries: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until roDataEnteriesYAxis.size) {
            roBarEntries.add(BarEntry(i.toFloat(), roDataEnteriesYAxis[i].toFloat()))
        }

        val roBarDataSet = BarDataSet(roBarEntries,"Complaint")
        roBarDataSet.setColor(Color.parseColor("#2383E1"))
        roBarDataSet.valueTextColor = Color.BLACK
        roBarDataSet.setFormSize(15f)
        viewBinding.barChartRO.getDescription().setEnabled(false)
        val xAxisro = viewBinding.barChartRO.getXAxis()
        xAxisro.granularity = 1f
        xAxisro.isGranularityEnabled = true
        xAxisro.setDrawGridLines(false)
        xAxisro.setCenterAxisLabels(false)
        xAxisro.axisMaximum = roListXAxis.size.toFloat()
        xAxisro.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxisro.valueFormatter = IndexAxisValueFormatter(roListXAxis)
        val roBarData = BarData(roBarDataSet)
        viewBinding.barChartRO.setFitBars(true)
        viewBinding.barChartRO.setDragEnabled(true)
        viewBinding.barChartRO.setData(roBarData)
        roBarData.barWidth = 0.5f
        viewBinding.barChartRO.setExtraOffsets(5f,5f,5f,15f)
        //Y-axis
        viewBinding.barChartRO.getAxisRight().setEnabled(false)
        val leftAxisro: YAxis = viewBinding.barChartRO.getAxisLeft()
        leftAxisro.setDrawGridLines(true)
//        leftAxisro.setSpaceTop(15f)
        leftAxisro.setAxisMinimum(0f)
        val yAxisRight: YAxis = viewBinding.barChartRO.getAxisRight()
        yAxisRight.setEnabled(false)
//        viewBinding.barChart.setExtraBottomOffset(1f)
//        viewBinding.contentMainContainer.barChart.setVisibleXRangeMaximum(12f)
//        viewBinding.contentMainContainer.barChart.setVisibleXRangeMinimum(4f)
        viewBinding.barChartRO.invalidate()

    }

}