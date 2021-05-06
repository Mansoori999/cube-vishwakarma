package com.vinners.cube_vishwakarma.ui.complaints.myComplaint

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.*



private val TAB_TITLES = arrayOf(
        "All",
        "Due",
        "Working",
        "Hold",
        "Cancelled",
        "Done",
        "Draft",
        "Estimated",
        "Billed",
        "Payment"
)

open class MyComplaintPagerAdapter(
        fm: FragmentManager
) : FragmentPagerAdapter(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    //Fragments
    private val allFragment = AllFragment.newInstance()
    private val dueFragment = DueFragment.newInstance()
    private val workingFragment = WorkingFragment.newInstance()
    private val holdFragment = HoldFragment.newInstance()
    private val cancelledFragment = CancelledFragment.newInstance()
    private val doneFragment = DoneFragment.newInstance()
    private val draftFragment = DraftFragment.newInstance()
    private val estimatedFragment = EstimatedFragment.newInstance()
    private val billedFragment = BilledFragment.newInstance()
    private val paymentFragment = PaymentFragment.newInstance()



    override fun getItem(position: Int): Fragment = when (position) {
        0 -> allFragment
        1 -> dueFragment
        2 -> workingFragment
        3 -> holdFragment
        4 -> cancelledFragment
        5 -> doneFragment
        6 -> draftFragment
        7-> estimatedFragment
        8 -> billedFragment
        9 -> paymentFragment
        else -> throw IllegalArgumentException("MyComplaintPagerAdapter : please specify fund for position $position")
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


