package com.vinners.cube_vishwakarma.ui.tutorials

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.vinners.cube_vishwakarma.R

class TutorialsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorials)
        val tutorialToolbar = findViewById<Toolbar>(R.id.tutorial_toolbar)
        tutorialToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}