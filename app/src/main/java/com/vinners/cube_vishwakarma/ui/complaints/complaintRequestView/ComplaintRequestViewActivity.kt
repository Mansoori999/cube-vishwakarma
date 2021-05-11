package com.vinners.cube_vishwakarma.ui.complaints.complaintRequestView

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.airbnb.lottie.LottieCompositionFactory
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.DateTimeHelper
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityComplaintRequestBinding
import com.vinners.cube_vishwakarma.databinding.ActivityComplaintRequestViewBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import androidx.lifecycle.Observer
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintRequestResponse
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.ComplaintRequestActivity
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.ComplaintRequestViewModel
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails.MyComplaintDetailsActivity

import java.util.*
import javax.inject.Inject

class ComplaintRequestViewActivity : BaseActivity<ActivityComplaintRequestViewBinding, ComplaintRequestViewModel>(R.layout.activity_complaint_request_view),
        DatePickerDialog.OnDateSetListener,ComplaintRequestViewClickListener {

    companion object {
        const val COMPLAINT_REQUEST_ID = "id"
        const val COMPLAINT_REQUEST_STATUS = "status"
        const val COMPLAINT_REQUEST_VIEW = "complaint_request_view"

    }
    private var startDate: String? = null
    private var endDate: String? = null
    private lateinit var fromDatePickerDialog: DatePickerDialog
    private lateinit var toDatePickerDialog: DatePickerDialog

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: ComplaintRequestViewModel by viewModels { viewModelFactory }

    private val complaintRequestViewRecyclerAdapter : ComplaintRequestViewRecyclerAdapter by lazy {
        ComplaintRequestViewRecyclerAdapter()
                .apply {
                    updateViewList(emptyList())
                    setComplaintsListener(this@ComplaintRequestViewActivity)
                }
    }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.requestToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        viewBinding.newComplaintRequestBtn.setOnClickListener {
            val intent = Intent(this, ComplaintRequestActivity::class.java)
            startActivity(intent)
        }
        viewBinding.viewComplaintRequestRecycler.layoutManager = LinearLayoutManager(this)
        complaintRequestViewRecyclerAdapter.updateViewList(Collections.emptyList())
        viewBinding.viewComplaintRequestRecycler.adapter = complaintRequestViewRecyclerAdapter

        initFromDatePicker()
        initToDatePicker()

        viewBinding.viewProgressBar.setVisibilityGone()
        complaintRequestViewRecyclerAdapter.updateViewList(emptyList())

        val composition =
                LottieCompositionFactory.fromAssetSync(
                        this,
                        "date_range.json"
                ).value
                        ?: throw IllegalArgumentException("Invalid composition lottie_success.json")

        viewBinding.informationLayout.root.setVisibilityVisible()
        viewBinding.informationLayout.informationAnimation.setComposition(composition)
        viewBinding.informationLayout.informationAnimation.playAnimation()
        viewBinding.informationLayout.informationTV.text = "Select A Date Range To Load Data"

        viewBinding.getDataButton.setOnClickListener {
            viewModel.getComplaintRequestView(startDate!!, endDate!!)
        }


    }

    private fun initFromDatePicker() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val beforeDate = calendar.add(Calendar.DAY_OF_YEAR, -15)

        fromDatePickerDialog = DatePickerDialog(this, this, year, month, day)
        fromDatePickerDialog.datePicker.minDate = calendar.timeInMillis
       // fromDatePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        viewBinding.fromDate.text = DateTimeHelper.getShortDate(calendar.time)
        viewBinding.fromDateContainer.setOnClickListener {
            fromDatePickerDialog.show()
        }
        startDate = DateTimeHelper.getyyyyMMddFormatDate(calendar.time)
    }

    private fun initToDatePicker() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        toDatePickerDialog = DatePickerDialog(this, this, year, month, day)
        toDatePickerDialog.datePicker.maxDate = calendar.timeInMillis
        viewBinding.toDate.text = DateTimeHelper.getShortDate(calendar.time)
        viewBinding.toDateContainer.setOnClickListener {
            toDatePickerDialog.show()
        }
        endDate = DateTimeHelper.getyyyyMMddFormatDate(calendar.time)
    }

    override fun onInitViewModel() {
        viewModel.viewComplaintRequestListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    complaintRequestViewRecyclerAdapter.updateViewList(emptyList())
                    viewBinding.informationLayout.root.setVisibilityGone()
                    viewBinding.viewProgressBar.setVisibilityVisible()
                }
                is Lce.Content->{
                    viewBinding.viewProgressBar.visibility = View.GONE

                    if (it.content.isNullOrEmpty()) {
                        complaintRequestViewRecyclerAdapter.updateViewList(emptyList())

                        val composition =
                                LottieCompositionFactory.fromAssetSync(
                                        this,
                                        "alert.json"
                                ).value
                                        ?: throw IllegalArgumentException("Invalid composition lottie_success.json")

                        viewBinding.informationLayout.root.setVisibilityVisible()
                        viewBinding.informationLayout.informationAnimation.setComposition(composition)
                        viewBinding.informationLayout.informationAnimation.playAnimation()
                        viewBinding.informationLayout.informationTV.text = "Not Data Found In Selected Date Range"

                    } else {
                        viewBinding.informationLayout.root.setVisibilityGone()
                        complaintRequestViewRecyclerAdapter.updateViewList(it.content)
                    }
                }
                is Lce.Error->{
                    viewBinding.viewProgressBar.visibility = View.GONE
                    complaintRequestViewRecyclerAdapter.updateViewList(emptyList())

                    val composition =
                            LottieCompositionFactory.fromAssetSync(
                                    this,
                                    "lottie_failure.json"
                            ).value
                                    ?: throw IllegalArgumentException("Invalid composition lottie_success.json")

                    viewBinding.informationLayout.root.setVisibilityVisible()
                    viewBinding.informationLayout.informationAnimation.setComposition(composition)
                    viewBinding.informationLayout.informationAnimation.playAnimation()
                    viewBinding.informationLayout.informationTV.text = it.error

                }

            }
        })
        viewModel.getComplaintRequestView(startDate!!, endDate!!)

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth )
        if (view == fromDatePickerDialog.datePicker) {
            viewBinding.fromDate.text = DateTimeHelper.getShortDate(calendar.time)
            startDate = DateTimeHelper.getyyyyMMddFormatDate(calendar.time)
        } else {
            viewBinding.toDate.text = DateTimeHelper.getShortDate(calendar.time)
            endDate = DateTimeHelper.getyyyyMMddFormatDate(calendar.time)
        }
    }

    override fun OnComplaintRequestClick(complaintList: ComplaintRequestResponse) {
        Intent(this, MyComplaintDetailsActivity::class.java).apply {
            putExtra("complaintId", complaintList.id)
            putExtra(COMPLAINT_REQUEST_STATUS,complaintList.status)
            putExtra(COMPLAINT_REQUEST_VIEW,true)
        }.also {
            startActivity(it)
        }
    }

}