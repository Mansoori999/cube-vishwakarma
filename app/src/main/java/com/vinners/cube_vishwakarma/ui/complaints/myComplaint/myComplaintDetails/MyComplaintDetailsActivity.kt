package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails


import android.content.Intent
import android.view.Gravity
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.DateTimeHelper
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.onItemSelected
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplainDetailsList
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityMyComplaintDetailsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.AllFragment
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.DueFragment
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.HoldFragment
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.WorkingFragment
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel.AllComplaintFragmentViewModel
import javax.inject.Inject

class MyComplaintDetailsActivity : BaseActivity<ActivityMyComplaintDetailsBinding,AllComplaintFragmentViewModel>(R.layout.activity_my_complaint_details) {

    @Inject
    lateinit var viewModelFactory : LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    private val statusList = ArrayList<String>()

    private var id: String? = null

    private var detailsId: String? = null
    private var status : String ? = null
     private var statusremarks : String? = null

    private var statusValue : String? = null
    private var complaintid : String? = null
    private var reasonEt:String?=null

    private lateinit var reason:EditText
    private lateinit var tv:TextView



    override val viewModel: AllComplaintFragmentViewModel by viewModels{ viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)

    }

    override fun onInitDataBinding() {
        id = intent.getStringExtra("complaintId")
        viewBinding.detailsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        viewBinding.changeStatus.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.complaint_details_dialog_layout,null)
            val mBuilder = AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setTitle("Update Status - ${complaintid}")

            val  mAlertDialog = mBuilder.show()
            var statusSpinner = dialogView.findViewById<Spinner>(R.id.status_spinner)
            reason = dialogView.findViewById<EditText>(R.id.reason)
//             reasonEt = reason.text.toString()
//            val spinnerlist = arrayOf("Select Status","Working", "Hold", "Done","Cancelled")
//            val statusArrayList = status!!.split(",")
            statusList.clear()
            statusList.add(0,"Select Status")
            if (status!!.equals("Working")){
                statusList.add("Hold")
                statusList.add("Done")
                statusList.add("Cancelled")
            }else if (status!!.equals("Hold")){
                statusList.add("Working")
                statusList.add("Done")
                statusList.add("Cancelled")
            }else if (status!!.equals("Due")){
                statusList.add("Working")
                statusList.add("Hold")
                statusList.add("Done")
                statusList.add("Cancelled")
            }

            val aa = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    statusList
            )
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            with(statusSpinner) {
                prompt = "Select Status"
                adapter = aa
                gravity = Gravity.CENTER
            }

            statusSpinner.onItemSelected { adapterView, _, position, _ ->
                adapterView ?: return@onItemSelected
                if (statusSpinner.childCount != 0 && statusSpinner.selectedItemPosition != 0) {
                    statusValue = adapterView.getItemAtPosition(position).toString()
                        if (statusValue!!.toLowerCase().equals("cancelled")) {
                            reason.setVisibilityVisible()
                        } else {
                            reason.setVisibilityGone()
                        }

                }

//                if (statusSpinner.childCount != 0 && statusSpinner.selectedItemPosition != 0) {
//                    val statusData = statusSpinner.selectedItem as StatusData
//
//                    if (statusData.name?.toLowerCase().equals("cancelled")){
//                        reason.setVisibilityVisible()
//                    }else{
//                        reason.setVisibilityGone()
//                    }
//                }
            }

            val cancle = dialogView.findViewById<Button>(R.id.cancelbtn)
            val update = dialogView.findViewById<Button>(R.id.uploadbtn)
            cancle.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
            }
            update.setOnClickListener{
                setValidationUpdate()
                mAlertDialog.dismiss()
            }

        }

    }

    private fun setValidationUpdate() {
        if  (reason.isVisible && reason.text.isNullOrBlank()){
            showInformationDialog("Please Write Reason")
            return
        }
        if (statusValue == null){
            showInformationDialog("Please Select Status")
            return
        }
        viewModel.upDateComplaints(
                statusremarks = reason.text.toString(),
                id = detailsId!!.toInt(),
                status = statusValue.toString()

        )

    }




    override fun onInitViewModel() {
        id.let { viewModel.getComplainDetails(it!!) }
        viewModel.complaintDetailsState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.loadingData.setVisibilityVisible()
                }
                is Lce.Content ->{
                    if (it.content != null){
                        viewBinding.loadingData.setVisibilityGone()
                        viewBinding.date.text = DateTimeHelper.getDDMMYYYYDateFromString(it.content.fordate!!)
                        viewBinding.complaintId.text = it.content.complaintid
                        viewBinding.work.text = it.content.work
                        viewBinding.type.text = it.content.type
                        viewBinding.outletName.text = it.content.outletname
                        viewBinding.regional.text = it.content.regionaloffice
                        viewBinding.sales.text = it.content.salesarea
                        viewBinding.district.text = it.content.district
                        viewBinding.letter.text = it.content.letterstatus
                        viewBinding.status.text = it.content.status
                        viewBinding.subadmin.text = it.content.subadmin
                        viewBinding.supervisor.text = it.content.supervisor
                        viewBinding.foreman.text = it.content.foreman
                        viewBinding.enduser.text = it.content.enduser
                        viewBinding.order.text = it.content.orderBy
                        viewBinding.remarks.text = it.content.remarks
                        setChangeStatus(it.content)

                    }else{
                        viewBinding.loadingData.setVisibilityVisible()
                    }



                }
                is Lce.Error ->
                {

                    viewBinding.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)

                }

            }
        })

        viewModel.upDateListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.loadingData.setVisibilityVisible()
                    viewBinding.detailScreen.setVisibilityGone()

                }
                is Lce.Content ->{
                    if (status!!.equals("Working")){
                        Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show()

                        id.let { viewModel.getComplainDetails(it!!) }
                        viewModel.complaintDetailsState.observe(this, Observer {
                            when(it){
                                Lce.Loading ->{
                                    viewBinding.loadingData.setVisibilityVisible()
                                }
                                is Lce.Content ->{
                                    if (it.content != null){
                                        viewBinding.loadingData.setVisibilityGone()
                                        viewBinding.date.text = DateTimeHelper.getDDMMYYYYDateFromString(it.content.fordate!!)
                                        viewBinding.complaintId.text = it.content.complaintid
                                        viewBinding.work.text = it.content.work
                                        viewBinding.type.text = it.content.type
                                        viewBinding.outletName.text = it.content.outletname
                                        viewBinding.regional.text = it.content.regionaloffice
                                        viewBinding.sales.text = it.content.salesarea
                                        viewBinding.district.text = it.content.district
                                        viewBinding.letter.text = it.content.letterstatus
                                        viewBinding.status.text = it.content.status
                                        viewBinding.subadmin.text = it.content.subadmin
                                        viewBinding.supervisor.text = it.content.supervisor
                                        viewBinding.foreman.text = it.content.foreman
                                        viewBinding.enduser.text = it.content.enduser
                                        viewBinding.order.text = it.content.orderBy
                                        viewBinding.remarks.text = it.content.remarks
                                        setChangeStatus(it.content)

                                    }else{
                                        viewBinding.loadingData.setVisibilityVisible()
                                    }



                                }
                                is Lce.Error ->
                                {

                                    viewBinding.loadingData.setVisibilityGone()
                                    showInformationDialog(it.error)

                                }

                            }
                        })
                        viewBinding.loadingData.setVisibilityGone()
                        viewBinding.detailScreen.setVisibilityVisible()
//                        getSupportFragmentManager().beginTransaction()
//                                .add(android.R.id.content, WorkingFragment.newInstance())
//                                .commit()
////                        startActivity(Intent(this, WorkingFragment::class.java))
//                        finish()
                    }else if (status!!.equals("Hold")){
                        Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show()
                        id.let { viewModel.getComplainDetails(it!!) }
                        viewModel.complaintDetailsState.observe(this, Observer {
                            when(it){
                                Lce.Loading ->{
                                    viewBinding.loadingData.setVisibilityVisible()
                                }
                                is Lce.Content ->{
                                    if (it.content != null){
                                        viewBinding.loadingData.setVisibilityGone()
                                        viewBinding.date.text = DateTimeHelper.getDDMMYYYYDateFromString(it.content.fordate!!)
                                        viewBinding.complaintId.text = it.content.complaintid
                                        viewBinding.work.text = it.content.work
                                        viewBinding.type.text = it.content.type
                                        viewBinding.outletName.text = it.content.outletname
                                        viewBinding.regional.text = it.content.regionaloffice
                                        viewBinding.sales.text = it.content.salesarea
                                        viewBinding.district.text = it.content.district
                                        viewBinding.letter.text = it.content.letterstatus
                                        viewBinding.status.text = it.content.status
                                        viewBinding.subadmin.text = it.content.subadmin
                                        viewBinding.supervisor.text = it.content.supervisor
                                        viewBinding.foreman.text = it.content.foreman
                                        viewBinding.enduser.text = it.content.enduser
                                        viewBinding.order.text = it.content.orderBy
                                        viewBinding.remarks.text = it.content.remarks
                                        setChangeStatus(it.content)

                                    }else{
                                        viewBinding.loadingData.setVisibilityVisible()
                                    }



                                }
                                is Lce.Error ->
                                {

                                    viewBinding.loadingData.setVisibilityGone()
                                    showInformationDialog(it.error)

                                }

                            }
                        })
                        viewBinding.loadingData.setVisibilityGone()
                        viewBinding.detailScreen.setVisibilityVisible()
//                        getSupportFragmentManager().beginTransaction()
//                                .add(android.R.id.content, HoldFragment.newInstance())
//                                .commit()
////                        startActivity(Intent(this, HoldFragment::class.java))
//                        finish()
                    }else if (status!!.equals("Due")){
                        Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show()
                        id.let { viewModel.getComplainDetails(it!!) }
                        viewModel.complaintDetailsState.observe(this, Observer {
                            when(it){
                                Lce.Loading ->{
                                    viewBinding.loadingData.setVisibilityVisible()
                                }
                                is Lce.Content ->{
                                    if (it.content != null){
                                        viewBinding.loadingData.setVisibilityGone()
                                        viewBinding.date.text = DateTimeHelper.getDDMMYYYYDateFromString(it.content.fordate!!)
                                        viewBinding.complaintId.text = it.content.complaintid
                                        viewBinding.work.text = it.content.work
                                        viewBinding.type.text = it.content.type
                                        viewBinding.outletName.text = it.content.outletname
                                        viewBinding.regional.text = it.content.regionaloffice
                                        viewBinding.sales.text = it.content.salesarea
                                        viewBinding.district.text = it.content.district
                                        viewBinding.letter.text = it.content.letterstatus
                                        viewBinding.status.text = it.content.status
                                        viewBinding.subadmin.text = it.content.subadmin
                                        viewBinding.supervisor.text = it.content.supervisor
                                        viewBinding.foreman.text = it.content.foreman
                                        viewBinding.enduser.text = it.content.enduser
                                        viewBinding.order.text = it.content.orderBy
                                        viewBinding.remarks.text = it.content.remarks
                                        setChangeStatus(it.content)

                                    }else{
                                        viewBinding.loadingData.setVisibilityVisible()
                                    }



                                }
//                                is Lce.Error ->
//                                {
//
//                                    viewBinding.loadingData.setVisibilityGone()
//                                    showInformationDialog(it.error)
//
//                                }

                            }
                        })

                        viewBinding.loadingData.setVisibilityGone()
                        viewBinding.detailScreen.setVisibilityVisible()
//                        getSupportFragmentManager().beginTransaction()
//                                .add(android.R.id.content, DueFragment.newInstance())
//                                .commit()
////                        startActivity(Intent(this, DueFragment::class.java))
//                        finish()
                    }

                }
                is Lce.Error ->{
                    viewBinding.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun setChangeStatus(content: MyComplainDetailsList) {

        if (content.status?.toLowerCase().equals("due") ||
                content.status?.toLowerCase().equals("working") ||
                content.status?.toLowerCase().equals("hold")){
            viewBinding.changeStatus.setVisibilityVisible()

        }else{
            viewBinding.changeStatus.setVisibilityGone()
        }

        detailsId = content.id
        statusremarks = content.statusremarks
        complaintid = content.complaintid
        status = content.status

    }

}

