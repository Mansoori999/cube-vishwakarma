package com.vinners.trumanms.ui.splash

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.vinners.trumanms.R
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.databinding.ActivitySplashBinding
import com.vinners.trumanms.di.DaggerLauncherComponent
import com.vinners.trumanms.di.LauncherViewModelFactory
import com.vinners.trumanms.feature.auth.ui.AuthActivity
import com.vinners.trumanms.feature.auth.ui.login.LoginActivity
import com.vinners.trumanms.feature.auth.ui.otp.OtpActivity
import com.vinners.trumanms.feature.auth.ui.register.RegisterActivity
import com.vinners.trumanms.ui.appIntro.AppIntroActivity
import com.vinners.trumanms.ui.appVersionDiscontinued.VersionBlockActivity
import com.vinners.trumanms.ui.home.HomeActivity
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
                    LauncherActivityState.ShowAppIntro -> showAppIntro()
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
        val intent = Intent(this, AppIntroActivity::class.java)
        //intent.putExtra(OtpActivity.INTENT_REFERAL_CODE,referralCode)
        startActivity(intent)
        finish()
    }

    private fun startAppVersionDiscontinuedActivity() {
        startActivity(Intent(this,VersionBlockActivity::class.java))
    }

    private fun startSelectLanguageActivity() {

    }

    private fun startMainActivity() {
        if (userSessionManager.empCode.isNullOrEmpty())
            startActivity(Intent(this, RegisterActivity::class.java))
        else
            startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
       // intent.putExtra(OtpActivity.INTENT_REFERAL_CODE,referralCode)
        startActivity(intent)
        finish()
    }
}