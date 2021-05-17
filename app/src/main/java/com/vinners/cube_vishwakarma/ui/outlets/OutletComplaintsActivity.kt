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
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintRecyclerAdapter
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintsClickListener
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails.MyComplaintDetailsActivity
import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity.Companion.OUTLET_ID
import java.util.*
import javax.inject.Inject

class OutletComplaintsActivity :  BaseActivity<ActivityOutletComplaintsBinding, OutletsViewModel>(R.layout.activity_outlet_complaints),
    AllComplaintsClickListener {

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
        viewBinding.refreshLayout.setOnRefreshListener {

            if (!viewBinding.refreshLayout.isRefreshing) {
                viewBinding.refreshLayout.isRefreshing = true
            }

            viewModel.getComplaintByOutletId(outletid!!)

        }
    }

    override fun onInitViewModel() {
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

    override fun OnAllComplaintsClick(myComplaintList: MyComplaintList) {
        Intent(this, MyComplaintDetailsActivity::class.java).apply {
            putExtra("complaintId", myComplaintList.id)
        }.also {
            startActivity(it)
        }
    }
}