package com.vinners.trumanms.ui.appIntro

import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import coil.api.load
import com.vinners.trumanms.R
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.databinding.FragmentAppIntroBinding
import com.vinners.trumanms.di.DaggerLauncherComponent
import com.vinners.trumanms.di.LauncherViewModelFactory
import com.vinners.trumanms.feature.auth.ui.login.LoginActivity
import javax.inject.Inject

class AppIntroFragment :
    BaseFragment<FragmentAppIntroBinding, AppIntroViewModel>(R.layout.fragment_app_intro) {

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory
    var count: Int = 0

    private val handler: Handler
        get() {
            val handler = Handler()

            return handler
        }

    private lateinit var appIntroPagerAdapter: AppIntroPagerAdapter

    override val viewModel: AppIntroViewModel by viewModels {
        viewModelFactory
    }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        appIntroPagerAdapter = AppIntroPagerAdapter(activity?.supportFragmentManager!!)

        val slideFirstFragment = SlideFirstFragment()
        appIntroPagerAdapter.addFragment(slideFirstFragment)

        val slideSecondFragment = SlideSecondFragment()
        appIntroPagerAdapter.addFragment(slideSecondFragment)

        val slideThirdGragment = SlideThirdGragment()
        appIntroPagerAdapter.addFragment(slideThirdGragment)
        viewBinding.appIntroViewPager.adapter = appIntroPagerAdapter
        viewBinding.appIntroViewPager.beginFakeDrag()
        viewBinding.appIntroViewPager.addOnPageChangeListener(viewPagerListener)
        setListener()
        addDtsLayout(0)
        handler.postDelayed(runnable, 5000)
    }

    override fun onInitViewModel() {

    }

    private val viewPagerListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
            if (position == 0)
                viewBinding.backBtn.visibility = View.GONE
            else
                viewBinding.backBtn.visibility = View.VISIBLE
            if (position == viewBinding.appIntroViewPager.childCount)
                viewBinding.nextBtn.text = getString(R.string.getStart)
            else
                viewBinding.nextBtn.text = getString(R.string.next)

            addDtsLayout(position)
        }

    }

    fun currentItem(): Int {
        return viewBinding.appIntroViewPager.currentItem + 1
    }

    fun setListener() {
        viewBinding.nextBtn.setOnClickListener {
            if (currentItem() < viewBinding.appIntroViewPager.childCount)
                viewBinding.appIntroViewPager.currentItem = currentItem()
            else
                startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        viewBinding.skpBtn.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        viewBinding.backBtn.setOnClickListener {
            viewBinding.appIntroViewPager.setCurrentItem(viewBinding.appIntroViewPager.currentItem - 1)
        }
    }

    fun addDtsLayout(position: Int) {
        viewBinding.dotsLayout.removeAllViews()
        for (i in 0 until appIntroPagerAdapter.count) {
            var dots = ImageView(requireContext())
            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            param.setMargins(10, 0, 0, 0)

            dots.layoutParams = param
            if (i == position) {
                param.height = 30
                param.width = 30
                dots.load(R.drawable.circle_solid)
            } else {
                param.height = 35
                param.width = 35
                dots.load(R.drawable.circle_outer)
            }

            viewBinding.dotsLayout.addView(dots)
        }
    }

    val runnable = object : Runnable {
        override fun run() {

            if (viewBinding.appIntroViewPager.currentItem == 0) {
                viewBinding.appIntroViewPager.setCurrentItem(1)
            } else if (viewBinding.appIntroViewPager.currentItem == 1)
                viewBinding.appIntroViewPager.setCurrentItem(2)
            else
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            handler.postDelayed(this, 5000)
        }
    }
}