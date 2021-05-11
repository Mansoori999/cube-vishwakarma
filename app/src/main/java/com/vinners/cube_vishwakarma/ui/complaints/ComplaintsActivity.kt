package com.vinners.cube_vishwakarma.ui.complaints

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.ComplaintRequestActivity
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequestView.ComplaintRequestViewActivity
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.MyComplaintActivity


class ComplaintsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaints)
        initView()
    }

    private fun initView() {
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
        complaintRequest.setOnClickListener {
            val intent = Intent(this, ComplaintRequestViewActivity::class.java)
            startActivity(intent)
        }
    }
}