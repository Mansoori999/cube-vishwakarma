package com.vinners.cube_vishwakarma.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vinners.cube_vishwakarma.ui.complaints.dashboardFragment.MonthlyFragment
import com.vinners.cube_vishwakarma.ui.complaints.dashboardFragment.ROWiseFragment
import com.vinners.cube_vishwakarma.ui.complaints.dashboardFragment.SummeryFragment
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.*



private val TAB_TITLES = arrayOf(
        "Monthly",
        "Summery",
        "RO Wise",

)

open class DashboardPagerAdapter(
        fm: FragmentManager
) : FragmentPagerAdapter(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    //Fragments
    private val monthlyFragment = MonthlyFragment.newInstance()
    private val summeryFragment = SummeryFragment.newInstance()
    private val rOWiseFragment = ROWiseFragment.newInstance()



    override fun getItem(position: Int): Fragment = when (position) {
        0 -> monthlyFragment
        1 -> summeryFragment
        2 -> rOWiseFragment

        else -> throw IllegalArgumentException("MyDashboardPagerAdapter : please specify fund for position $position")
    }

    override fun getPageTitle(position: Int): CharSequence {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        return  TAB_TITLES.size
    }
//
//    fun refreshFundsInFragments() {
//        myPendingViewOutletFragment.refreshFund()
//        AllViewOutletFragment.refreshFund()
//    }


}


