package com.vinners.cube_vishwakarma.ui.complaints.complaintRequest

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.onItemSelected
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityComplaintRequestBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.model.*
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequestView.ComplaintRequestViewActivity
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintRecyclerAdapter
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintsClickListener
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails.MyComplaintDetailsActivity
import com.vinners.cube_vishwakarma.ui.dashboardFilter.RegionalOfficeFilterData
import java.util.stream.Collectors
import javax.inject.Inject


class ComplaintRequestActivity : BaseActivity<ActivityComplaintRequestBinding,ComplaintRequestViewModel>(R.layout.activity_complaint_request), AllComplaintsClickListener {

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: ComplaintRequestViewModel by viewModels { viewModelFactory }

    private val allComplaintRecyclerAdapter: AllComplaintRecyclerAdapter by lazy {
        AllComplaintRecyclerAdapter()
                .apply {
                    updateViewList(emptyList())
                    setAllComplaintsListener(this@ComplaintRequestActivity)
                }
    }
    lateinit var recyclerView :RecyclerView
    private var progressBar: ProgressBar? = null
    lateinit var textError:TextView
    lateinit var errorImage:ImageView
    var errorContainer:LinearLayout? = null
    var userid : String? = null
    var adminUserid : String = ""


    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }

    override fun onInitDataBinding() {
        userid = userSessionManager.userId
        viewBinding.addComplaintDataMainLayout.complaintRequestToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        viewBinding.addComplaintDataMainLayout.regionalSpinner.onItemSelected { adapterView, _, _, _ ->
            adapterView ?: return@onItemSelected

            if (viewBinding.addComplaintDataMainLayout.regionalSpinner.childCount != 0 &&
                viewBinding.addComplaintDataMainLayout.regionalSpinner.selectedItemPosition != 0) {
                val regionalData = viewBinding.addComplaintDataMainLayout.regionalSpinner.selectedItem as RegionalOfficeData
                    setSalesOnSpinner(regionalData.id)



            }
        }

        viewBinding.addComplaintDataMainLayout.salesareaSpinner.onItemSelected { adapterView, view, i, l ->
            adapterView ?: return@onItemSelected

            if (viewBinding.addComplaintDataMainLayout.salesareaSpinner.childCount != 0 &&
                viewBinding.addComplaintDataMainLayout.salesareaSpinner.selectedItemPosition != 0) {

                val regionalData = viewBinding.addComplaintDataMainLayout.regionalSpinner.selectedItem as RegionalOfficeData
                val salesData = viewBinding.addComplaintDataMainLayout.salesareaSpinner.selectedItem as SalesAreaData
                    setOutletOnSpinner(
                            regionalData.id,
                            salesData.id
                    )

            }
        }


        viewBinding.addComplaintDataMainLayout.outletSpinner.onItemSelected { adapterView, view, i, l ->
            adapterView ?: return@onItemSelected

            if (viewBinding.addComplaintDataMainLayout.outletSpinner.childCount != 0 &&
                viewBinding.addComplaintDataMainLayout.outletSpinner.selectedItemPosition != 0) {

                val regionalData = viewBinding.addComplaintDataMainLayout.regionalSpinner.selectedItem as RegionalOfficeData
                val salesData = viewBinding.addComplaintDataMainLayout.salesareaSpinner.selectedItem as SalesAreaData
                val outletData =  viewBinding.addComplaintDataMainLayout.outletSpinner.selectedItem as OutletAreaData
                outletAreaInfo(outletData.id)

            }
        }


        viewBinding.addComplaintDataMainLayout.submitBtn.setOnClickListener {
            if ( validateAndSubmitComplaintData() == true) {
                val inflater = LayoutInflater.from(this)
//                val inflater: LayoutInflater = LayoutInflater.from(this)
                val dialogView = inflater.inflate(R.layout.viewcomplaint_during_request, null)
                val alertDialog = AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setTitle("Do you still want to generate complaint?")

                recyclerView = dialogView.findViewById(R.id.complaintsRecycler)
                progressBar = dialogView.findViewById(R.id.progressBar)
                recyclerView.layoutManager = LinearLayoutManager(this)
                allComplaintRecyclerAdapter.updateViewList(emptyList())
                recyclerView.adapter = allComplaintRecyclerAdapter
                errorContainer = dialogView.findViewById(R.id.errorContainer)
                textError = dialogView.findViewById(R.id.message_tv)
                errorImage = dialogView.findViewById(R.id.info_image_iv)
                if (userSessionManager.designation!!.toLowerCase().equals("admin")){
                    viewModel.getComplaintList(adminUserid)
                }else{
                    viewModel.getComplaintList(userid!!)

                }

                alertDialog.setPositiveButton("Yes") { dialogInterface, i ->
                    dialogInterface.dismiss()
                    val outletid = (viewBinding.addComplaintDataMainLayout.outletSpinner.selectedItem as OutletAreaData).id
                    val complaintTypeid = (viewBinding.addComplaintDataMainLayout.complaintTypeSpinner.selectedItem as ComplaintTypeData).id
                    val orderByid = (viewBinding.addComplaintDataMainLayout.orderBySpinner.selectedItem as OrderByData).id

                    viewModel.submitComplaintRequest(
                            typeid = complaintTypeid!!,
                            outletid = outletid,
                            orderby = orderByid!!,
                            work = viewBinding.addComplaintDataMainLayout.wordEt.text.toString(),
                            remarks = viewBinding.addComplaintDataMainLayout.editRemarks.text.toString()
                    )

                }

                alertDialog.setNegativeButton("Cancel"){ DialogInterface, i: Int ->
                    DialogInterface.dismiss()
                }
                alertDialog.show()
            }

        }
    }




    private fun validateAndSubmitComplaintData():Boolean {
        if (viewBinding.addComplaintDataMainLayout.regionalSpinner.childCount == 0 || viewBinding.addComplaintDataMainLayout.regionalSpinner.selectedItemPosition == 0) {
            showInformationDialog("Please Select Regional Office")
            return false
        }
        if (viewBinding.addComplaintDataMainLayout.salesareaSpinner.childCount == 0 ||
                viewBinding.addComplaintDataMainLayout.salesareaSpinner.selectedItemPosition == 0) {
            showInformationDialog("Please Select Sales Area")
            return false
        }
        if (viewBinding.addComplaintDataMainLayout.outletSpinner.childCount == 0 ||
                viewBinding.addComplaintDataMainLayout.outletSpinner.selectedItemPosition == 0) {
            showInformationDialog("Please Select Outlet Area")
            return false
        }
        if (viewBinding.addComplaintDataMainLayout.complaintTypeSpinner.childCount == 0 ||
                viewBinding.addComplaintDataMainLayout.complaintTypeSpinner.selectedItemPosition == 0) {
            showInformationDialog("Please Select Complaint Type")
            return false
        }
        if (viewBinding.addComplaintDataMainLayout.orderBySpinner.childCount == 0 ||
                viewBinding.addComplaintDataMainLayout.orderBySpinner.selectedItemPosition==0){
            showInformationDialog("Please Select Order By")
            return false
        }
        if (viewBinding.addComplaintDataMainLayout.wordEt.text.isNullOrBlank()) {
            showInformationDialog("Please Enter Work")
            return false
        }

        return true



    }

    private fun outletAreaInfo(id: Int) {
        val complaintList = viewModel.complaintRequestList ?: return
        val outletData = complaintList.filter {
            it.outletid == id
        } ?: return
        outletData.forEach {
            viewBinding.addComplaintDataMainLayout.districtEt.setText("District :${ it.district }")
            viewBinding.addComplaintDataMainLayout.locationET.setText("Location :${ it.location }")
            viewBinding.addComplaintDataMainLayout.categoryEt.setText("Category :${ it.category }")
        }

    }


    override fun onInitViewModel() {
        viewModel.loadRegionalAndSalesInfo
            .observe(this, Observer {
                when (it) {
                    LoadMetaForAddComplaintState.LoadingLoadMetaForAddComplaintState -> {
                        viewBinding.addComplaintDataMainLayout.root.setVisibilityGone()
                        viewBinding.addComplaintDataErrorLayout.root.setVisibilityGone()
                        viewBinding.addComplaintDataErrorWithRetryLayout.root.setVisibilityGone()
                        viewBinding.addDataLoadingLayout.setVisibilityVisible()
                    }
                    is LoadMetaForAddComplaintState.MetaForAddComplaintStateLoaded->{
                        viewBinding.addComplaintDataErrorLayout.root.setVisibilityGone()
                        viewBinding.addComplaintDataErrorWithRetryLayout.root.setVisibilityGone()
                        viewBinding.addDataLoadingLayout.setVisibilityGone()
                        viewBinding.addComplaintDataMainLayout.root.setVisibilityVisible()

                        val regionals = it.regionalAndSalesInfo.map {
                            RegionalOfficeData(
                                id = it.roid!!,
                                name = it.regionaloffice!!
                            )
                        }.toMutableList()

                        setRegionalOnRegionalOfficeSpinner(regionals)

                    }
                    is LoadMetaForAddComplaintState.ErrorInLoadingMetaForAddComplaintLoaded->{
                        viewBinding.addComplaintDataErrorWithRetryLayout.root.setVisibilityGone()
                        viewBinding.addDataLoadingLayout.setVisibilityGone()
                        viewBinding.addComplaintDataMainLayout.root.setVisibilityGone()

                        viewBinding.addComplaintDataErrorLayout.root.setVisibilityVisible()
                        viewBinding.addComplaintDataErrorLayout.informationTV.text = it.error
                    }

                }

            })
        viewModel.getNewComplaintData()
        viewModel.complaintTypeListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityVisible()
                }
                is Lce.Content->{
                    viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityGone()
                    val complaintType = it.content.map {
                        ComplaintTypeData(
                                id = it.id!!,
                                name = it.name!!
                        )
                    }.toMutableList()
                    setComplaintOnComplaintTypeSpinner(complaintType)


                }
                is Lce.Error ->{
                    viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityGone()
                }
            }
        })
        viewModel.getComplaintTypeData()

        viewModel.orderbyListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityVisible()
                }
                is Lce.Content->{
                    viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityGone()
                    val orderBy = it.content.map {
                        OrderByData(
                                id = it.id!!,
                                name = it.fullnamedesig!!
                        )
                    }.toMutableList()
                    setOrderOnOrderBySpinner(orderBy)
                }
                is Lce.Error ->{
                    viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityGone()
                }
            }
        })
        viewModel.getOrderByData()
        viewModel.complaintListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    progressBar?.setVisibilityVisible()

                }
                is Lce.Content->
                {
                    val itemlist = it.content.filter {
                        it.status?.toLowerCase().equals("due") ||
                                it.status?.toLowerCase().equals("working") ||
                                it.status?.toLowerCase().equals("hold") ||
                                it.status?.toLowerCase().equals("done")
                    }
                    if (itemlist.isEmpty()){
                        errorContainer?.setVisibilityVisible()
                        progressBar?.setVisibilityGone()
                        errorImage.load(R.drawable.ic_information)
                        textError.text = "Not Complaints Found"

                    } else {
                        progressBar?.setVisibilityGone()
                        allComplaintRecyclerAdapter.updateViewList(itemlist)
                        errorContainer?.setVisibilityGone()

                    }

                }
                is Lce.Error ->
                {
                   progressBar?.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })


        viewModel.submitListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.addComplaintDataMainLayout.root.setVisibilityGone()
                    viewBinding.addComplaintDataErrorLayout.root.setVisibilityGone()
                    viewBinding.addComplaintDataErrorWithRetryLayout.root.setVisibilityGone()
                    viewBinding.addDataLoadingLayout.setVisibilityVisible()
                }
                is Lce.Content ->{
                    Toast.makeText(this, "Complaint Request Data Submitted", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,ComplaintRequestViewActivity::class.java)
                    startActivity(intent)
                    finish()
//                    viewModel.loadRegionalAndSalesInfo
//                            .observe(this, Observer {
//                                when (it) {
//                                    LoadMetaForAddComplaintState.LoadingLoadMetaForAddComplaintState -> {
//                                        viewBinding.addComplaintDataMainLayout.root.setVisibilityGone()
//                                        viewBinding.addComplaintDataErrorLayout.root.setVisibilityGone()
//                                        viewBinding.addComplaintDataErrorWithRetryLayout.root.setVisibilityGone()
//                                        viewBinding.addDataLoadingLayout.setVisibilityVisible()
//                                    }
//                                    is LoadMetaForAddComplaintState.MetaForAddComplaintStateLoaded->{
//                                        viewBinding.addComplaintDataErrorLayout.root.setVisibilityGone()
//                                        viewBinding.addComplaintDataErrorWithRetryLayout.root.setVisibilityGone()
//                                        viewBinding.addDataLoadingLayout.setVisibilityGone()
//                                        viewBinding.addComplaintDataMainLayout.root.setVisibilityVisible()
//
//                                        val regionals = it.regionalAndSalesInfo.map {
//                                            RegionalOfficeData(
//                                                    id = it.roid!!,
//                                                    name = it.regionaloffice!!
//                                            )
//                                        }.toMutableList()
//                                        setRegionalOnRegionalOfficeSpinner(regionals)
//
//                                    }
//                                    is LoadMetaForAddComplaintState.ErrorInLoadingMetaForAddComplaintLoaded->{
//                                        viewBinding.addComplaintDataErrorWithRetryLayout.root.setVisibilityGone()
//                                        viewBinding.addDataLoadingLayout.setVisibilityGone()
//                                        viewBinding.addComplaintDataMainLayout.root.setVisibilityGone()
//
//                                        viewBinding.addComplaintDataErrorLayout.root.setVisibilityVisible()
//                                        viewBinding.addComplaintDataErrorLayout.informationTV.text = it.error
//                                    }
//
//                                }
//                                viewBinding.addComplaintDataMainLayout.editRemarks.text.clear()
//                                viewBinding.addComplaintDataMainLayout.wordEt.text.clear()
//                                viewBinding.addComplaintDataMainLayout.districtEt.text.clear()
//                                viewBinding.addComplaintDataMainLayout.locationET.text.clear()
//                                viewBinding.addComplaintDataMainLayout.categoryEt.text.clear()
//                            })
//                    viewModel.getNewComplaintData()
//                    viewModel.complaintTypeListState.observe(this, Observer {
//                        when(it){
//                            Lce.Loading ->{
//                                viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityVisible()
//                            }
//                            is Lce.Content->{
//                                viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityGone()
//                                val complaintType = it.content.map {
//                                    ComplaintTypeData(
//                                            id = it.id!!,
//                                            name = it.name!!
//                                    )
//                                }.toMutableList()
//                                setComplaintOnComplaintTypeSpinner(complaintType)
//
//
//                            }
//                            is Lce.Error ->{
//                                viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityGone()
//                            }
//                        }
//                    })
//                    viewModel.getComplaintTypeData()
//
//                    viewModel.orderbyListState.observe(this, Observer {
//                        when(it){
//                            Lce.Loading ->{
//                                viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityVisible()
//                            }
//                            is Lce.Content->{
//                                viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityGone()
//                                val orderBy = it.content.map {
//                                    OrderByData(
//                                            id = it.id!!,
//                                            name = it.fullnamedesig!!
//                                    )
//                                }.toMutableList()
//                                setOrderOnOrderBySpinner(orderBy)
//                            }
//                            is Lce.Error ->{
//                                viewBinding.addComplaintDataMainLayout.refreshProgressbar.setVisibilityGone()
//                            }
//                        }
//                    })
//                    viewModel.getOrderByData()

                }
                is Lce.Error ->{
                    viewBinding.addComplaintDataErrorWithRetryLayout.root.setVisibilityGone()
                    viewBinding.addDataLoadingLayout.setVisibilityGone()
                    viewBinding.addComplaintDataMainLayout.root.setVisibilityVisible()
                    viewBinding.addComplaintDataErrorLayout.root.setVisibilityGone()
                    MaterialAlertDialogBuilder(this)
                            .setTitle("Unable to submit Data")
                            .setMessage("Unable to submit Complaint Request data, ${it.error}")
                            .setPositiveButton("Okay") { _, _ -> }
                            .show()
                }
            }
        })
    }



    private fun setOrderOnOrderBySpinner(orderBy: MutableList<OrderByData>) {
        orderBy.sortBy { it.name }
        val setItems: Set<OrderByData> = LinkedHashSet(orderBy)
        orderBy.clear()
        orderBy.addAll(setItems)
        orderBy.add(
                0,
                OrderByData(
                        id = "XX",
                        name = "Select Order By"
                )
        )
        val aa = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                orderBy
        )

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(viewBinding.addComplaintDataMainLayout.orderBySpinner) {
            adapter = aa
            prompt = "Select Order By"
            gravity = Gravity.CENTER
        }
    }

    private fun setComplaintOnComplaintTypeSpinner(complaintType: MutableList<ComplaintTypeData>) {
        complaintType.sortBy { it.name }
        val setItems: Set<ComplaintTypeData> = LinkedHashSet(complaintType)
        complaintType.clear()
        complaintType.addAll(setItems)
        complaintType.add(
                0,
                ComplaintTypeData(
                        id = "XX",
                        name = "Select Complaint Type"
                )
        )
        val aa = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                complaintType
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(viewBinding.addComplaintDataMainLayout.complaintTypeSpinner) {
            adapter = aa
            prompt = "Select Complaint Type"
            gravity = Gravity.CENTER
        }
    }

    private fun setRegionalOnRegionalOfficeSpinner(regionals: MutableList<RegionalOfficeData>) {

        regionals.sortBy { it.name }
        val setItems: Set<RegionalOfficeData> = LinkedHashSet(regionals)
        regionals.clear()
        regionals.addAll(setItems)
        regionals.add(
            0,
            RegionalOfficeData(
                id = 0,
                name = "Select Regional Office"
            )
        )
        val aa = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            regionals
        )

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(viewBinding.addComplaintDataMainLayout.regionalSpinner) {
            adapter = aa
            prompt = "Select Regional Office"
            gravity = Gravity.CENTER
        }
    }



    private fun setSalesOnSpinner(id: Int) {

        val complaintList = viewModel.complaintRequestList ?: return

//        val regionalData = regionalList.find {
//            it.roid == id
//        } ?: return
        val sales = complaintList.filter {
            it.roid == id
        }.map {
            SalesAreaData(
                it.said,
                it.salesarea!!
            )
        }.distinctBy {
            it.id
        }.toMutableList()
        sales.sortBy { it.name }
        val setItems: Set<SalesAreaData> = LinkedHashSet(sales)
        sales.clear()
        sales.addAll(setItems)
        sales.add(
            0,
            SalesAreaData(
                id = 0,
                name = "Select Sales Area"
            )
        )
        val aa = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            sales
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(viewBinding.addComplaintDataMainLayout.salesareaSpinner) {
            adapter = aa
            prompt = "Select Sales Area"
            gravity = Gravity.CENTER
        }

    }

    private fun setOutletOnSpinner(reagionalId: Int, salesId: Int?) {
        val complaintList = viewModel.complaintRequestList ?: return

//        val campaignData = complaintList.find {
//            it.campid == campaignId
//        } ?: return

        val outlets = complaintList.filter {
            it.said == salesId && it.roid == reagionalId
        }.map {
            OutletAreaData(
                    it.outletid!!,
                "${ it.outlet!! } - ${it.customercode}"
            )
        }.distinctBy {
            it.id
        }.toMutableList()

        outlets.sortBy { it.name }
        val setItems: Set<OutletAreaData> = LinkedHashSet(outlets)
        outlets.clear()
        outlets.addAll(setItems)
        outlets.add(
            0,
            OutletAreaData(
                id = 0,
                name = "Select Outlet Area"
            )
        )

        val aa = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            outlets
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(viewBinding.addComplaintDataMainLayout.outletSpinner) {
            adapter = aa
            prompt = "Select Outlet Area"
            gravity = Gravity.CENTER
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