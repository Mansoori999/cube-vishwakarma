package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment


import android.content.Intent

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseFragment
import coil.api.load
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.FragmentAllBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintRecyclerAdapter
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintsClickListener
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails.MyComplaintDetailsActivity

import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel.AllComplaintFragmentViewModel
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel.MyComplaintSharedViewModel
import java.util.*
import javax.inject.Inject

class AllFragment : BaseFragment<FragmentAllBinding,AllComplaintFragmentViewModel>(R.layout.fragment_all), AllComplaintsClickListener {
    companion object {
        fun newInstance() = AllFragment()

    }

    private val allComplaintRecyclerAdapter: AllComplaintRecyclerAdapter by lazy {
        AllComplaintRecyclerAdapter()
                .apply {
                    updateViewList(emptyList())
                    setAllComplaintsListener(this@AllFragment)
                }
    }


    @Inject
    lateinit var viewModelFactory : LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager


    var userid : String? = null
    var adminUserid : String = ""


    override val viewModel: AllComplaintFragmentViewModel by viewModels{ viewModelFactory }

    fun allComplaintSearchFilter(newText: String?){
        allComplaintRecyclerAdapter.filter.filter(newText)

    }

    private lateinit var sharedViewModel: MyComplaintSharedViewModel

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }

    override fun onInitDataBinding() {
        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(MyComplaintSharedViewModel::class.java)
        }
        userid = userSessionManager.userId
        viewBinding.allcomplaintFragmentRecycler.layoutManager = LinearLayoutManager(context)
        allComplaintRecyclerAdapter.updateViewList(Collections.emptyList())
        viewBinding.allcomplaintFragmentRecycler.adapter = allComplaintRecyclerAdapter
//        viewBinding.refreshLayout.setOnRefreshListener {
//
//            if (!viewBinding.refreshLayout.isRefreshing) {
//                viewBinding.refreshLayout.isRefreshing = true
//            }
//
//            if (userSessionManager.designation!!.toLowerCase().equals("admin")){
//                viewModel.getComplaintList(adminUserid)
//            }else{
//                viewModel.getComplaintList(userid!!)
//
//            }
//
//        }

    }

    override fun onInitViewModel() {

//        viewModel.complaintDaoListState.observe(this, Observer {
//            when(it){
//                Lce.Loading ->{
//                    viewBinding.errorLayout.root.setVisibilityGone()
//                    viewBinding.progressBar.setVisibilityVisible()
////                    viewBinding.refreshLayout.isRefreshing = false
//                }
//                is Lce.Content-> {
//                    if (it.content.isEmpty()){
//                        viewBinding.progressBar.setVisibilityGone()
//                        viewBinding.errorLayout.root.setVisibilityVisible()
//                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
//                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
//                        viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
//                    } else {
//                        viewBinding.errorLayout.root.setVisibilityGone()
//                        viewBinding.progressBar.setVisibilityGone()
//                        allComplaintRecyclerAdapter.updateViewList(it.content)
//                    }
//
//                }
//                is Lce.Error ->
//                {
//                    viewBinding.progressBar.setVisibilityGone()
////                    viewBinding.refreshLayout.isRefreshing = false
//                    viewBinding.progressBar.setVisibilityGone()
//                    showInformationDialog(it.error)
//
//                }
//
//
//            }
//        })


//        viewModel.getComplaintDaoList()

        sharedViewModel.data().observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.errorLayout.root.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityVisible()
//                    viewBinding.refreshLayout.isRefreshing = false
                }
                is Lce.Content-> {
                    if (it.content.isEmpty()){
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        allComplaintRecyclerAdapter.updateViewList(Collections.emptyList())
                        viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        allComplaintRecyclerAdapter.updateViewList(it.content)
//                        if (!viewBinding.refreshLayout.isRefreshing) {
//                            viewBinding.refreshLayout.isRefreshing = false
//                        }
                    }

                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
//                    viewBinding.refreshLayout.isRefreshing = false
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })
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