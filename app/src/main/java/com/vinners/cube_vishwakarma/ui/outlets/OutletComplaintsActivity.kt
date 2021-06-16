package com.vinners.cube_vishwakarma.ui.outlets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.databinding.ActivityEditOutletBinding
import com.vinners.cube_vishwakarma.databinding.ActivityOutletComplaintsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.MainActivityViewModel
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintRecyclerAdapter
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintsClickListener
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails.MyComplaintDetailsActivity
import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity.Companion.OUTLET_ID
import java.util.*
import javax.inject.Inject

class OutletComplaintsActivity :  BaseActivity<ActivityOutletComplaintsBinding, OutletsViewModel>(R.layout.activity_outlet_complaints),
    AllComplaintsClickListener {

    companion object {
        const val ENABLE_TOTAL_ACTIVITY = "enable_total"
        const val ENABLE_DUE_ACTIVITY = "enable_due"
        const val ENABLE_WORKING_ACTIVITY = "enable_WORKING"
        const val ENABLE_PENDING_ACTIVITY = "enable_pending"
        const val ENABLE_DONE_ACTIVITY = "enable_done"
        const val ENABLE_DRAFT_ACTIVITY = "enable_draft"
        const val ENABLE_ESTIMATE_ACTIVITY = "enable_estimate"
        const val ENABLE_BILLED_ACTIVITY = "enable_billed"
        const val ENABLE_PAYMENT_ACTIVITY = "enable_payment"

    }

    val statustotal : String = ""
    val statusdue : String = "Due"
    val statusworking : String = "Working"
    val statuspending : String = "Pending Letter"
    val statusDone : String = "Done"
    val statusDraft : String = "Draft"
    val statusEstimated : String = "Estimated"
    val statusBilled : String = "Billed"
    val statusPayment : String = "Payment"
    lateinit var startDate:String
    lateinit var endDate:String
    var regionalOfficeIds:String ?= null



    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var appInfo : AppInfo

    private val allComplaintRecyclerAdapter: AllComplaintRecyclerAdapter by lazy {
        AllComplaintRecyclerAdapter()
                .apply {
                    updateViewList(emptyList())
                   setAllComplaintsListener(this@OutletComplaintsActivity)
                }
    }

    var outletid : String? = null

    override val viewModel: OutletsViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }

    override fun onInitDataBinding() {
        outletid = intent.getStringExtra(OUTLET_ID)
        viewBinding.outletComplaintsToolbar.setNavigationOnClickListener {
            onBackPressed()

        }
        viewBinding.complaintsRecycler.layoutManager = LinearLayoutManager(this)
        allComplaintRecyclerAdapter.updateViewList(Collections.emptyList())
        viewBinding.complaintsRecycler.adapter = allComplaintRecyclerAdapter



    }

    override fun onInitViewModel() {
        val enableTotal = intent.getBooleanExtra(ENABLE_TOTAL_ACTIVITY, false)
        val enableDue = intent.getBooleanExtra(ENABLE_DUE_ACTIVITY, false)
        val enableWorking = intent.getBooleanExtra(ENABLE_WORKING_ACTIVITY, false)
        val enablePending = intent.getBooleanExtra(ENABLE_PENDING_ACTIVITY, false)
        val enabledone = intent.getBooleanExtra(ENABLE_DONE_ACTIVITY, false)
        val enabledraft = intent.getBooleanExtra(ENABLE_DRAFT_ACTIVITY, false)
        val enableestimate = intent.getBooleanExtra(ENABLE_ESTIMATE_ACTIVITY, false)
        val enablebilled = intent.getBooleanExtra(ENABLE_BILLED_ACTIVITY, false)
        val enablepayment = intent.getBooleanExtra(ENABLE_PAYMENT_ACTIVITY, false)

        if  (enableTotal == true || enableDue == true || enableWorking == true || enablePending == true||
                enabledone == true || enabledraft == true || enableestimate == true || enablebilled == true ||
                enablepayment == true) {
            if (intent.getStringExtra("startDateF").equals("null").not()) {
                startDate = intent.getStringExtra("startDateF")
            } else {
                startDate = intent.getStringExtra("startDate")

            }

            if (intent.getStringExtra("endDateF").equals("null").not()) {
                endDate = intent.getStringExtra("endDateF")
            } else {

                endDate = intent.getStringExtra("endDate")
            }
            regionalOfficeIds = intent.getStringExtra("regionalOfficeId")
        }else{

        }
        viewBinding.refreshLayout.setOnRefreshListener {

            if (!viewBinding.refreshLayout.isRefreshing) {
                viewBinding.refreshLayout.isRefreshing = true
            }

            if (enableTotal == true ){
                viewModel.getComplaintWithStatus(statustotal,startDate,endDate, regionalOfficeIds)
            } else if(enableDue == true ){
                viewModel.getComplaintWithStatus(statusdue,startDate,endDate, regionalOfficeIds)
            } else if(enableWorking == true ) {
                viewModel.getComplaintWithStatus(statusworking,startDate,endDate, regionalOfficeIds)
            }else if(enablePending == true) {
                viewModel.getComplaintWithStatus(statuspending,startDate,endDate, regionalOfficeIds)
            }else if(enabledone == true) {
                viewModel.getComplaintWithStatus(statusDone,startDate,endDate, regionalOfficeIds)
            }else if (enabledraft == true ) {
                viewModel.getComplaintWithStatus(statusDraft,startDate,endDate, regionalOfficeIds)
            }else if(enableestimate == true) {
                viewModel.getComplaintWithStatus(statusEstimated,startDate,endDate, regionalOfficeIds)
            }else if(enablebilled == true) {
                viewModel.getComplaintWithStatus(statusBilled,startDate,endDate, regionalOfficeIds)
            }else if(enablepayment == true){
                viewModel.getComplaintWithStatus(statusPayment,startDate,endDate, regionalOfficeIds)
            }else {
                viewModel.getComplaintByOutletId(outletid!!)
            }

        }
        if (enableTotal == true){
            viewBinding.outletComplaintsToolbar.setTitle("All Complaints")
            viewModel.getComplaintWithStatus(statustotal,startDate,endDate, regionalOfficeIds)
            viewModel.complaintStatusState.observe(this, androidx.lifecycle.Observer {
                when(it){
                    Lce.Loading->{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityVisible()
                        viewBinding.refreshLayout.isRefreshing = false
                    }
                    is Lce.Content->{
                        if (it.content.isEmpty()){
                            viewBinding.refreshLayout.isRefreshing = false
                            viewBinding.progressBar.setVisibilityGone()
                            viewBinding.errorLayout.root.setVisibilityVisible()
                            viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                            viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                            viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                        } else {
                            viewBinding.errorLayout.root.setVisibilityGone()
                            viewBinding.progressBar.setVisibilityGone()
                            allComplaintRecyclerAdapter.updateViewList(it.content)
                            if (!viewBinding.refreshLayout.isRefreshing) {
                                viewBinding.refreshLayout.isRefreshing = false
                            }
                        }
                    }
                    is Lce.Error->{
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.refreshLayout.isRefreshing = false
                        viewBinding.progressBar.setVisibilityGone()
                        showInformationDialog(it.error)

                    }
                }
            })

        } else if (enableDue == true){
            viewBinding.outletComplaintsToolbar.setTitle("Due Complaints")
            viewModel.getComplaintWithStatus(statusdue,startDate,endDate, regionalOfficeIds)
            viewModel.complaintStatusState.observe(this, androidx.lifecycle.Observer {
                when(it){
                    Lce.Loading->{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityVisible()
                        viewBinding.refreshLayout.isRefreshing = false
                    }
                    is Lce.Content->{
                        if (it.content.isEmpty()){
                            viewBinding.refreshLayout.isRefreshing = false
                            viewBinding.progressBar.setVisibilityGone()
                            viewBinding.errorLayout.root.setVisibilityVisible()
                            viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                            viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                            viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                        } else {
                            viewBinding.errorLayout.root.setVisibilityGone()
                            viewBinding.progressBar.setVisibilityGone()
                            allComplaintRecyclerAdapter.updateViewList(it.content)
                            if (!viewBinding.refreshLayout.isRefreshing) {
                                viewBinding.refreshLayout.isRefreshing = false
                            }
                        }
                    }
                    is Lce.Error->{
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.refreshLayout.isRefreshing = false
                        viewBinding.progressBar.setVisibilityGone()
                        showInformationDialog(it.error)

                    }
                }
            })


        }else if (enableWorking == true){
            viewBinding.outletComplaintsToolbar.setTitle("Working Complaints")
            viewModel.getComplaintWithStatus(statusworking,startDate,endDate, regionalOfficeIds)
            viewModel.complaintStatusState.observe(this, androidx.lifecycle.Observer {
                when(it){
                    Lce.Loading->{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityVisible()
                        viewBinding.refreshLayout.isRefreshing = false
                    }
                    is Lce.Content->{
                        if (it.content.isEmpty()){
                            viewBinding.refreshLayout.isRefreshing = false
                            viewBinding.progressBar.setVisibilityGone()
                            viewBinding.errorLayout.root.setVisibilityVisible()
                            viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                            viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                            viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                        } else {
                            viewBinding.errorLayout.root.setVisibilityGone()
                            viewBinding.progressBar.setVisibilityGone()
                            allComplaintRecyclerAdapter.updateViewList(it.content)
                            if (!viewBinding.refreshLayout.isRefreshing) {
                                viewBinding.refreshLayout.isRefreshing = false
                            }
                        }
                    }
                    is Lce.Error->{
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.refreshLayout.isRefreshing = false
                        viewBinding.progressBar.setVisibilityGone()
                        showInformationDialog(it.error)

                    }
                }
            })


        }else if (enablePending == true){
            viewBinding.outletComplaintsToolbar.setTitle("Pending Letter Complaints")
            viewModel.getComplaintWithStatus(statuspending,startDate,endDate, regionalOfficeIds)
            viewModel.complaintStatusState.observe(this, androidx.lifecycle.Observer {
                when(it){
                    Lce.Loading->{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityVisible()
                        viewBinding.refreshLayout.isRefreshing = false
                    }
                    is Lce.Content->{
                        if (it.content.isEmpty()){
                            viewBinding.refreshLayout.isRefreshing = false
                            viewBinding.progressBar.setVisibilityGone()
                            viewBinding.errorLayout.root.setVisibilityVisible()
                            viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                            viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                            viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                        } else {
                            viewBinding.errorLayout.root.setVisibilityGone()
                            viewBinding.progressBar.setVisibilityGone()
                            allComplaintRecyclerAdapter.updateViewList(it.content)
                            if (!viewBinding.refreshLayout.isRefreshing) {
                                viewBinding.refreshLayout.isRefreshing = false
                            }
                        }
                    }
                    is Lce.Error->{
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.refreshLayout.isRefreshing = false
                        viewBinding.progressBar.setVisibilityGone()
                        showInformationDialog(it.error)

                    }
                }
            })

        }else if (enabledone == true){
            viewBinding.outletComplaintsToolbar.setTitle("Done Complaints")
            viewModel.getComplaintWithStatus(statusDone,startDate,endDate, regionalOfficeIds)
            viewModel.complaintStatusState.observe(this, androidx.lifecycle.Observer {
                when(it){
                    Lce.Loading->{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityVisible()
                        viewBinding.refreshLayout.isRefreshing = false
                    }
                    is Lce.Content->{
                        if (it.content.isEmpty()){
                            viewBinding.refreshLayout.isRefreshing = false
                            viewBinding.progressBar.setVisibilityGone()
                            viewBinding.errorLayout.root.setVisibilityVisible()
                            viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                            viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                            viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                        } else {
                            viewBinding.errorLayout.root.setVisibilityGone()
                            viewBinding.progressBar.setVisibilityGone()
                            allComplaintRecyclerAdapter.updateViewList(it.content)
                            if (!viewBinding.refreshLayout.isRefreshing) {
                                viewBinding.refreshLayout.isRefreshing = false
                            }
                        }
                    }
                    is Lce.Error->{
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.refreshLayout.isRefreshing = false
                        viewBinding.progressBar.setVisibilityGone()
                        showInformationDialog(it.error)

                    }
                }
            })

        }else if (enabledraft == true){
            viewBinding.outletComplaintsToolbar.setTitle("Draft Complaints")
            viewModel.getComplaintWithStatus(statusDraft,startDate,endDate, regionalOfficeIds)
            viewModel.complaintStatusState.observe(this, androidx.lifecycle.Observer {
                when(it){
                    Lce.Loading->{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityVisible()
                        viewBinding.refreshLayout.isRefreshing = false
                    }
                    is Lce.Content->{
                        if (it.content.isEmpty()){
                            viewBinding.refreshLayout.isRefreshing = false
                            viewBinding.progressBar.setVisibilityGone()
                            viewBinding.errorLayout.root.setVisibilityVisible()
                            viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                            viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                            viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                        } else {
                            viewBinding.errorLayout.root.setVisibilityGone()
                            viewBinding.progressBar.setVisibilityGone()
                            allComplaintRecyclerAdapter.updateViewList(it.content)
                            if (!viewBinding.refreshLayout.isRefreshing) {
                                viewBinding.refreshLayout.isRefreshing = false
                            }
                        }
                    }
                    is Lce.Error->{
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.refreshLayout.isRefreshing = false
                        viewBinding.progressBar.setVisibilityGone()
                        showInformationDialog(it.error)

                    }
                }
            })

        }else if (enableestimate == true){
            viewBinding.outletComplaintsToolbar.setTitle("Estimated Complaints")
            viewModel.getComplaintWithStatus(statusEstimated,startDate,endDate, regionalOfficeIds)
            viewModel.complaintStatusState.observe(this, androidx.lifecycle.Observer {
                when(it){
                    Lce.Loading->{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityVisible()
                        viewBinding.refreshLayout.isRefreshing = false
                    }
                    is Lce.Content->{
                        if (it.content.isEmpty()){
                            viewBinding.progressBar.setVisibilityGone()
                            viewBinding.refreshLayout.isRefreshing = false
                            viewBinding.errorLayout.root.setVisibilityVisible()
                            viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                            viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                            viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                        } else {
                            viewBinding.errorLayout.root.setVisibilityGone()
                            viewBinding.progressBar.setVisibilityGone()
                            allComplaintRecyclerAdapter.updateViewList(it.content)
                            if (!viewBinding.refreshLayout.isRefreshing) {
                                viewBinding.refreshLayout.isRefreshing = false
                            }
                        }
                    }
                    is Lce.Error->{
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.refreshLayout.isRefreshing = false
                        viewBinding.progressBar.setVisibilityGone()
                        showInformationDialog(it.error)

                    }
                }
            })

        }else if (enablebilled == true){
            viewBinding.outletComplaintsToolbar.setTitle("Billed Complaints")
            viewModel.getComplaintWithStatus(statusBilled,startDate,endDate, regionalOfficeIds)
            viewModel.complaintStatusState.observe(this, androidx.lifecycle.Observer {
                when(it){
                    Lce.Loading->{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityVisible()
                        viewBinding.refreshLayout.isRefreshing = false
                    }
                    is Lce.Content->{
                        if (it.content.isEmpty()){
                            viewBinding.refreshLayout.isRefreshing = false
                            viewBinding.progressBar.setVisibilityGone()
                            viewBinding.errorLayout.root.setVisibilityVisible()
                            viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                            viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                            viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                        } else {
                            viewBinding.errorLayout.root.setVisibilityGone()
                            viewBinding.progressBar.setVisibilityGone()
                            allComplaintRecyclerAdapter.updateViewList(it.content)
                            if (!viewBinding.refreshLayout.isRefreshing) {
                                viewBinding.refreshLayout.isRefreshing = false
                            }
                        }
                    }
                    is Lce.Error->{
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.refreshLayout.isRefreshing = false
                        viewBinding.progressBar.setVisibilityGone()
                        showInformationDialog(it.error)

                    }
                }
            })

        }else if (enablepayment == true){
            viewBinding.outletComplaintsToolbar.setTitle("Payment Complaints")
            viewModel.getComplaintWithStatus(statusPayment,startDate,endDate, regionalOfficeIds)
            viewModel.complaintStatusState.observe(this, androidx.lifecycle.Observer {
                when(it){
                    Lce.Loading->{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityVisible()
                        viewBinding.refreshLayout.isRefreshing = false
                    }
                    is Lce.Content->{
                        if (it.content.isEmpty()){
                            viewBinding.refreshLayout.isRefreshing = false
                            viewBinding.progressBar.setVisibilityGone()
                            viewBinding.errorLayout.root.setVisibilityVisible()
                            viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                            viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                            viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                        } else {
                            viewBinding.errorLayout.root.setVisibilityGone()
                            viewBinding.progressBar.setVisibilityGone()
                            allComplaintRecyclerAdapter.updateViewList(it.content)
                            if (!viewBinding.refreshLayout.isRefreshing) {
                                viewBinding.refreshLayout.isRefreshing = false
                            }
                        }
                    }
                    is Lce.Error->{
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.refreshLayout.isRefreshing = false
                        viewBinding.progressBar.setVisibilityGone()
                        showInformationDialog(it.error)

                    }
                }
            })

        }
        else{
            viewBinding.outletComplaintsToolbar.setTitle("Complaints")
            outletid.let { viewModel.getComplaintByOutletId(it!!) }
            viewModel.complaintsbyoutletListState.observe(this, androidx.lifecycle.Observer {
            when(it){
                Lce.Loading->{
                    viewBinding.errorLayout.root.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityVisible()
                    viewBinding.refreshLayout.isRefreshing = false
                }
                is Lce.Content->{
                    if (it.content.isEmpty()){
                        viewBinding.refreshLayout.isRefreshing = false
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        allComplaintRecyclerAdapter.updateViewList(it.content)
                        if (!viewBinding.refreshLayout.isRefreshing) {
                            viewBinding.refreshLayout.isRefreshing = false
                        }
                    }
                }
                is Lce.Error->{
                    viewBinding.progressBar.setVisibilityGone()
                    viewBinding.refreshLayout.isRefreshing = false
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }
            }
        })

        }
    }


    override fun OnAllComplaintsClick(myComplaintList: MyComplaintList) {
        Intent(this, MyComplaintDetailsActivity::class.java).apply {
            putExtra("complaintId", myComplaintList.id)
        }.also {
            startActivity(it)
        }
    }
}