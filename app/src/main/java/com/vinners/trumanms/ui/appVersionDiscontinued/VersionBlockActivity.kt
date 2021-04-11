package com.vinners.trumanms.ui.appVersionDiscontinued

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.vinners.trumanms.R
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.databinding.ActivityVersionBlockBinding
import com.vinners.trumanms.di.DaggerLauncherComponent
import com.vinners.trumanms.di.LauncherViewModelFactory
import com.vinners.trumanms.ui.splash.SplashViewModel
import javax.inject.Inject

class VersionBlockActivity: BaseActivity<ActivityVersionBlockBinding,SplashViewModel>(R.layout.activity_version_block){
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    override val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onInitDataBinding() {
        viewBinding?.currentVersionTextView?.text =
            getString(R.string.curren_version, appInfo.appVersion)
        viewBinding?.updateAppBtn?.setOnClickListener {

            val i = Intent(Intent.ACTION_VIEW)
            i.data =
                Uri.parse("https://play.google.com/store/apps/details?id=${appInfo.packageName}")
            startActivity(i)
        }
    }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitViewModel() {

    }
}