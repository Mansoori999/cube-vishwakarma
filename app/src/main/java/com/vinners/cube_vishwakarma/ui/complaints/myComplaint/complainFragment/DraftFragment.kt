package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseFragment
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import androidx.lifecycle.Observer
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.FragmentBilledBinding
import com.vinners.cube_vishwakarma.databinding.FragmentDraftBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintRecyclerAdapter
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintsClickListener
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails.MyComplaintDetailsActivity
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel.AllComplaintFragmentViewModel
import java.util.*
import javax.inject.Inject

class DraftFragment :  BaseFragment<FragmentDraftBinding, AllComplaintFragmentViewModel>(R.layout.fragment_draft),
        AllComplaintsClickListener {

    companion object {
        fun newInstance() = DraftFragment()
    }
    var adminUserid : String = ""
    private val allComplaintRecyclerAdapter: AllComplaintRecyclerAdapter by lazy {
        AllComplaintRecyclerAdapter()
            .apply {
                updateViewList(emptyList())
                setAllComplaintsListener(this@DraftFragment)
            }
    }
    fun allComplaintSearchFilter(newText: String?){
        allComplaintRecyclerAdapter.filter.filter(newText)

    }
    @Inject
    lateinit var viewModelFactory : LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager


    var userid : String? = null

    override val viewModel: AllComplaintFragmentViewModel by viewModels{ viewModelFactory }


    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        userid = userSessionManager.userId
        viewBinding.allcomplaintFragmentRecycler.layoutManager = LinearLayoutManager(context)
        allComplaintRecyclerAdapter.updateViewList(Collections.emptyList())
        viewBinding.allcomplaintFragmentRecycler.adapter = allComplaintRecyclerAdapter
        viewBinding.refreshLayout.setOnRefreshListener {

            if (!viewBinding.refreshLayout.isRefreshing) {
                viewBinding.refreshLayout.isRefreshing = true
            }

            if (userSessionManager.designation!!.toLowerCase().equals("admin")){
                viewModel.getComplaintList(adminUserid)
            }else{
                viewModel.getComplaintList(userid!!)

            }

        }
    }

    override fun onInitViewModel() {
        viewModel.complaintDaoListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.errorLayout.root.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityVisible()
                    viewBinding.refreshLayout.isRefreshing = false
                }
                is Lce.Content->
                {
                    val itemlist = it.content.filter {
                        it.status?.toLowerCase().equals("draft")
                    }
                    if (itemlist.isEmpty()){
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = "Not Draft Complaint Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        allComplaintRecyclerAdapter.updateViewList(itemlist)
                        if (!viewBinding.refreshLayout.isRefreshing) {
                            viewBinding.refreshLayout.isRefreshing = false
                        }
                    }

                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    viewBinding.refreshLayout.isRefreshing = false
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })
        viewModel.getComplaintDaoList()
//        if (userSessionManager.designation!!.toLowerCase().equals("admin")){
//            viewModel.getComplaintList(adminUserid)
//        }else{
//            viewModel.getComplaintList(userid!!)
//
//        }

    }

    override fun OnAllComplaintsClick(myComplaintList: MyComplaintList) {
        Intent(context, MyComplaintDetailsActivity::class.java).apply {
            putExtra("complaintId", myComplaintList.id)
        }.also {
            startActivity(it)
        }
    }


}