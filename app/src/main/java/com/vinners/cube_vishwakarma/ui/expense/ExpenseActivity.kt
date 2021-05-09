package com.vinners.cube_vishwakarma.ui.expense

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.vinners.cube_vishwakarma.R

class ExpenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        val expenseToolbar = findViewById<Toolbar>(R.id.complaint_toolbar)
        expenseToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}