package com.vinners.cube_vishwakarma.ui.complaints.dashboardFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.AllFragment

class MonthlyFragment : Fragment() {

    companion object {
        fun newInstance() = MonthlyFragment()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_mothly, container, false)
    }


}