package com.vinners.trumanms.ui.appIntro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.api.load
import com.vinners.trumanms.R

class SlideFirstFragment : Fragment() {
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var slideImage: ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.slide_fragment_layout, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(view: View) {
        title = view.findViewById(R.id.title)
        description = view.findViewById(R.id.descprition)
        slideImage = view.findViewById(R.id.slideImg)
        title.text = getString(R.string.money_title)
        description.text = getString(R.string.slide_1_des)
        slideImage.setImageResource(R.drawable.app_intro_money)
        slideImage.load(R.drawable.app_intro_money)
    }
}