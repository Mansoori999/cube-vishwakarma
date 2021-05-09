package com.vinners.cube_vishwakarma.ui.attendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.vinners.cube_vishwakarma.R

class AttendanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendence)
        val attendanceToolbar = findViewById<Toolbar>(R.id.complaint_toolbar)
        attendanceToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}