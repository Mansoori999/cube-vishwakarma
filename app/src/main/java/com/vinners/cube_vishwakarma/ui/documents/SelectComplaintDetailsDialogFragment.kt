package com.vinners.cube_vishwakarma.ui.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import coil.api.load
import com.vinners.cube_vishwakarma.R

import com.vinners.cube_vishwakarma.core.base.BaseDialogFragment
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.FragmentSelectComplaintDialogBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import mobile.fitbitMerch.ui.masterData.ComplaintSelectionListener
import java.util.*
import javax.inject.Inject


class SelectComplaintDetailsDialogFragment : BaseDialogFragment<FragmentSelectComplaintDialogBinding,DocumentsViewModel>(R.layout.fragment_select_complaint_dialog),
    ComplaintSelectionListener {
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    companion object {
        var TAG = "SelectComplaintDetailsDialogFragment"
        fun newInstance() = SelectComplaintDetailsDialogFragment()
        fun newInstance(callBackToCheckActivity: ComplaintSelectionListener): SelectComplaintDetailsDialogFragment {
            val complaintSelectorDialogFragment = SelectComplaintDetailsDialogFragment()
            complaintSelectorDialogFragment.setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.DialogFragmentTheme
            )
            complaintSelectorDialogFragment.setComplaintSelectedCallback(callBackToCheckActivity)
            return complaintSelectorDialogFragment
        }
    }

    var userid : String? = null
    var adminUserid : String = ""

    @Inject
    lateinit var userSessionManager: UserSessionManager

    private var callbackToComplaintSelectorFragment: ComplaintSelectionListener? = null



    fun setComplaintSelectedCallback(callbackToComplaintSelectorFragment: ComplaintSelectionListener) {
        this.callbackToComplaintSelectorFragment = callbackToComplaintSelectorFragment
    }

    private val selectComplaintDetailsRecyclerAdapter: SelectComplaintDetailsRecyclerAdapter by lazy {
        SelectComplaintDetailsRecyclerAdapter()
            .apply {
                updateViewList(emptyList())
                setComplaintsListener(this@SelectComplaintDetailsDialogFragment)
            }
    }


    override fun onComplaintSelected(myComplaintList: MyComplaintList) {
        callbackToComplaintSelectorFragment?.onComplaintSelected(myComplaintList)
        dismiss()
    }

    override val viewModel: DocumentsViewModel by viewModels { viewModelFactory }


    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        userid = userSessionManager.userId
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        selectComplaintDetailsRecyclerAdapter.updateViewList(Collections.emptyList())
        viewBinding.recyclerView.adapter = selectComplaintDetailsRecyclerAdapter
        viewBinding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                selectComplaintDetailsRecyclerAdapter.filter.filter(p0)
                return true
            }

        })
    }

    override fun onInitViewModel() {
        viewModel.complaintListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.errorLayout.root.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityVisible()
                }
                is Lce.Content->
                {
                    val itemlist = it.content.filter {
                        it.status?.toLowerCase().equals("working")
                    }
                    if (itemlist.isEmpty()){
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        selectComplaintDetailsRecyclerAdapter.updateViewList(emptyList())
                        viewBinding.errorLayout.messageTv.text = "Not Complaint Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        selectComplaintDetailsRecyclerAdapter.updateViewList(itemlist)

                    }

                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityGone()

                }


            }
        })

        if (userSessionManager.designation!!.toLowerCase().equals("admin")){
            viewModel.getComplaintList(adminUserid)
        }else{
            viewModel.getComplaintList(userid!!)

        }

    }

}