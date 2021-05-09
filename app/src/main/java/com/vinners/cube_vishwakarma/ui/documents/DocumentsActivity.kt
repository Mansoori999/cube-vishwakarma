package com.vinners.cube_vishwakarma.ui.documents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.vinners.cube_vishwakarma.R

class DocumentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documents)

        val documentToolbar = findViewById<Toolbar>(R.id.complaint_toolbar)
        documentToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}