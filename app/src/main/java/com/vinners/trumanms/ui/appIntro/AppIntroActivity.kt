package com.vinners.trumanms.ui.appIntro

import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import coil.api.load
import com.vinners.trumanms.R
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.di.DaggerLauncherComponent
import com.vinners.trumanms.di.LauncherViewModelFactory
import com.vinners.trumanms.feature.auth.databinding.ActivityMainBinding
import com.vinners.trumanms.feature.auth.ui.login.LoginActivity
import com.vinners.trumanms.feature.auth.ui.otp.OtpActivity
import dagger.internal.DoubleCheck.lazy
import javax.inject.Inject


class AppIntroActivity :
    BaseActivity<ActivityMainBinding, AppIntroViewModel>(R.layout.activity_main) {

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory
    var count: Int = 0
    private var referralCode : String? = null

    private val handler: Handler by kotlin.lazy {
        val handler = android.os.Handler()
        handler
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
        referralCode = intent.getStringExtra(OtpActivity.INTENT_REFERAL_CODE)
        appIntroPagerAdapter = AppIntroPagerAdapter(supportFragmentManager)

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
        viewModel.events.observe(this, Observer {

            when (it) {
                AppIntroEvents.APP_INTRO_SHOWN -> startLoginActivity()
                AppIntroEvents.APP_INTRO_SKIPPED -> startLoginActivity()
            }
        })
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
            else {
                viewModel.setAppIntroAsShown()
            }
        }
        viewBinding.skpBtn.setOnClickListener {
            viewModel.setAppIntroAsSkipped()
        }
        viewBinding.backBtn.setOnClickListener {
            viewBinding.appIntroViewPager.setCurrentItem(viewBinding.appIntroViewPager.currentItem - 1)
        }
    }

    fun addDtsLayout(position: Int) {
        viewBinding.dotsLayout.removeAllViews()
        for (i in 0 until appIntroPagerAdapter.count) {
            var dots = ImageView(this)
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

    private val runnable = object : Runnable {
        override fun run() {

            if (viewBinding.appIntroViewPager.currentItem == 0) {
                viewBinding.appIntroViewPager.setCurrentItem(1)
            } else if (viewBinding.appIntroViewPager.currentItem == 1)
                viewBinding.appIntroViewPager.setCurrentItem(2)
            else if (viewBinding.appIntroViewPager.currentItem == 2) {
                viewModel.setAppIntroAsShown()
            }
            handler.postDelayed(this, 5000)
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(OtpActivity.INTENT_REFERAL_CODE,referralCode)
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}