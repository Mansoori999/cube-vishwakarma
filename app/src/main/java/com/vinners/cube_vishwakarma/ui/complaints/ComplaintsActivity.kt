package com.vinners.cube_vishwakarma.ui.complaints

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityComplaintRequestBinding
import com.vinners.cube_vishwakarma.databinding.ActivityComplaintsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.ComplaintRequestActivity
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.ComplaintRequestViewModel
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequestView.ComplaintRequestViewActivity
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.MyComplaintActivity
import javax.inject.Inject


class ComplaintsActivity : BaseActivity<ActivityComplaintsBinding, ComplaintRequestViewModel>(R.layout.activity_complaints) {

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: ComplaintRequestViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }

    override fun onInitDataBinding() {
        val complaintsToolbar = findViewById<Toolbar>(R.id.complaint_toolbar)
        complaintsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val myComplaint = findViewById<CardView>(R.id.complaint)
        myComplaint.setOnClickListener {
            val intent = Intent(this, MyComplaintActivity::class.java)
            startActivity(intent)
        }
        val complaintRequest = findViewById<CardView>(R.id.complaint_request)
        if (userSessionManager.designation?.toLowerCase().equals("sub admin")){
            complaintRequest.setVisibilityVisible()
        }else{
            complaintRequest.setVisibilityGone()
        }
        complaintRequest.setOnClickListener {
            val intent = Intent(this, ComplaintRequestViewActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onInitViewModel() {

    }
}