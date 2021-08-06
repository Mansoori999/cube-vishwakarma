package com.vinners.cube_vishwakarma.ui.dashboard.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseFragment
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.FragmentSummeryBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.dashboard.MainActivityViewModel
import com.vinners.cube_vishwakarma.ui.outlets.OutletComplaintsActivity
import javax.inject.Inject


class SummeryFragment : BaseFragment<FragmentSummeryBinding, MainActivityViewModel>(R.layout.fragment_summery) {
    companion object {
        fun newInstance() = SummeryFragment()

    }

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    lateinit var defaultStartDate: String
    lateinit var defaultEndDate: String
    lateinit var startDateF: String
    lateinit var endDateF: String
    var startdate: String? = null
    var enddate: String? = null
    var startdatef: String? = null
    var enddatef: String? = null
    var regionalOfficeIds: String? = null
    var fyearDateId: Int? = null
    var fyearDateName: String? = null
    var roIds: String? = null
    val isClicked: Boolean = false
    var subadminId :String? = null
    var roselectedId: String? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//       defaultStartDate = arguments?.getString("defaultStartDate")!!
//        defaultEndDate = arguments?.getString("defaultEndDate")!!
//         startDateF = arguments?.getString("startDateF")!!
//        endDateF = arguments?.getString("endDateF")!!
//        regionalOfficeIds = arguments?.getString("regionalOfficeIds")
//         roselectedId = arguments?.getString("roselectedId")
//         subadminId = arguments?.getString("subadminId")
//
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
//        startDateF = startdatef.toString()
//        endDateF = enddatef.toString()
//        val data = activity?.getIntent()?.extras
//        val preferences = context?.getSharedPreferences("data", Context.MODE_PRIVATE)
        val preferences: SharedPreferences = context?.getSharedPreferences("summerydata", 0)!!

        defaultStartDate = preferences.getString("defaultStartDate", "")!!
        defaultEndDate = preferences.getString("defaultEndDate", "")!!
        startDateF = preferences.getString("startDateF", "")!!
        endDateF = preferences.getString("endDateF", "")!!
        regionalOfficeIds = preferences.getString("regionalOfficeIds", "")
        roselectedId = preferences.getString("roselectedId", "")
        subadminId = preferences.getString("subadminId", "")


        val myAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.card_anim)
        viewBinding.totalCardView.setOnClickListener {
            viewBinding.totalCardView.startAnimation(myAnim)
            val intent = Intent(context, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_TOTAL_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId", subadminId)
            startActivity(intent)

        }
        viewBinding.dueCardView.setOnClickListener {
            viewBinding.dueCardView.startAnimation(myAnim)
            val intent = Intent(context, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_DUE_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId", subadminId)
            startActivity(intent)

        }
        viewBinding.workingCardView.setOnClickListener {
            viewBinding.workingCardView.startAnimation(myAnim)
            val intent = Intent(context, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_WORKING_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId", subadminId)
            startActivity(intent)

        }
        viewBinding.pendingCardView.setOnClickListener {
            viewBinding.pendingCardView.startAnimation(myAnim)
            val intent = Intent(context, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_PENDING_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId", subadminId)
            startActivity(intent)

        }

        viewBinding.doneCardView.setOnClickListener {
            viewBinding.doneCardView.startAnimation(myAnim)
            val intent = Intent(context, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_DONE_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId", subadminId)
            startActivity(intent)

        }

        viewBinding.draftCardView.setOnClickListener {
            viewBinding.draftCardView.startAnimation(myAnim)
            val intent = Intent(context, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_DRAFT_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId", subadminId)
            startActivity(intent)

        }
        viewBinding.estimatedCardView.setOnClickListener {
            viewBinding.estimatedCardView.startAnimation(myAnim)
            val intent = Intent(context, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_ESTIMATE_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId", subadminId)
            startActivity(intent)

        }
        viewBinding.billedCardView.setOnClickListener {
            viewBinding.billedCardView.startAnimation(myAnim)
            val intent = Intent(context, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_BILLED_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId", subadminId)
            startActivity(intent)

        }
        viewBinding.paymentCardView.setOnClickListener {
            viewBinding.paymentCardView.startAnimation(myAnim)
            val intent = Intent(context, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_PAYMENT_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId", subadminId)
            startActivity(intent)

        }
    }

    override fun onInitViewModel() {
        viewModel.dashboardDaoState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.loadingData.setVisibilityVisible()
                }
                is Lce.Content -> {
                    it.content.dashboardCount.forEach {
                        viewBinding.loadingData.setVisibilityGone()
                        viewBinding.totals.text = it.total
                        viewBinding.due.text = it.due
                        viewBinding.working.text = it.working
                        viewBinding.pending.text = it.pendingletter
                        viewBinding.done.text = it.done
                        viewBinding.draft.text = it.draft
                        viewBinding.estimated.text = it.estimated
                        viewBinding.billed.text = it.billed
                        viewBinding.payment.text = it.payment

                    }
//                    dataEnteriesYAxis =  it.content.monthWiseChart!!.data.toList()
//                    monthlyDataXAxis = it.content.monthWiseChart!!.monthList
//                    roDataEnteriesYAxis = it.content.roWiseChart!!.data.toList()
//                    roListXAxis = it.content.roWiseChart!!.roList
//                    monthWiseBarChart()
//                    roWiseBarChart()
//                    viewPager.adapter = sectionsPagerAdapter
//                    tabs.setupWithViewPager(viewPager)
                }
                is Lce.Error -> {
                    viewBinding.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)

                }
            }
        })
        viewModel.getDashboardDaoData()

    }


}