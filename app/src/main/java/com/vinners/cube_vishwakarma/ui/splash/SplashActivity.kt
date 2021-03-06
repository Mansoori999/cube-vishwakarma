package com.vinners.cube_vishwakarma.ui.splash

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivitySplashBinding

import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.feature_auth.ui.AuthActivity
import com.vinners.cube_vishwakarma.ui.appVersionDiscontinued.VersionBlockActivity
import com.vinners.cube_vishwakarma.ui.dashboard.MainActivity
import javax.inject.Inject


class SplashActivity :
    BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {

    }

    override fun onInitViewModel() {
        viewModel.launcherState
            .observe(this, Observer {
                when (it) {
                    LauncherActivityState.LocaleNotSelected -> startSelectLanguageActivity()
                    LauncherActivityState.AppVersionDiscontinued -> startAppVersionDiscontinuedActivity()
                   LauncherActivityState.UserNotLoggedIn -> startLoginActivity()
                    LauncherActivityState.UserLoggedIn -> startMainActivity()
//                    LauncherActivityState.ShowAppIntro -> showAppIntro()
                }
            })
        viewModel.appIntroState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    //showAppIntro(it.content.referCode)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.loginState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                   // startLoginActivity(it.content.referCode)
                }
                is Lce.Error -> {

                }
            }
        })

    }

    private fun showAppIntro() {
//        val intent = Intent(this, AppIntroActivity::class.java)
//        //intent.putExtra(OtpActivity.INTENT_REFERAL_CODE,referralCode)
//        startActivity(intent)
//        finish()
    }

    private fun startAppVersionDiscontinuedActivity() {
        startActivity(Intent(this,VersionBlockActivity::class.java))
    }

    private fun startSelectLanguageActivity() {

    }

    private fun startMainActivity() {
        if (userSessionManager.userId.isNullOrEmpty())
            startActivity(Intent(this, AuthActivity::class.java))
        else
            startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()

    }
}