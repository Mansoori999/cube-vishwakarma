package com.vinners.cube_vishwakarma.ui.splash

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseFragment
import com.vinners.cube_vishwakarma.core.extensions.navigateSafely
import com.vinners.cube_vishwakarma.databinding.FragmentSplashBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import java.util.logging.Handler
import javax.inject.Inject

class SplashFragment :
    BaseFragment<FragmentSplashBinding, SplashViewModel>(R.layout.fragment_splash) {

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    override val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onInitDataBinding() {
        //Do Nothing
    }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitViewModel() {
        viewModel.launcherState
            .observe(this, Observer {
                when (it) {
                    // LauncherActivityState.LocaleNotSelected -> startSelectLanguageActivity()
                    // LauncherActivityState.AppVersionDiscontinued -> startAppVersionDiscontinuedActivity()
                    // LauncherActivityState.UserNotLoggedIn -> startLoginActivity()
                    // LauncherActivityState.UserLoggedIn -> startMainActivity()
                    LauncherActivityState.ShowAppIntro -> showAppIntro()
                }
            })

    }

    private fun showAppIntro() {
        android.os.Handler().postDelayed(
            Runnable {
                findNavController()
                    .navigateSafely(
                        actionId = R.id.action_splashFragment_to_appIntroFragment
                    )
            }, 3000
        )

    }

    private fun startAppVersionDiscontinuedActivity() {
        findNavController()
            .navigateSafely(
                actionId = R.id.action_splashFragment_to_appVersionDiscontinuedFragment
            )
    }

    private fun startSelectLanguageActivity() {
        findNavController().navigateSafely(
            actionId = R.id.selectLanguageFragment
        )
    }

    private fun startMainActivity() {

    }

    private fun startLoginActivity() {
//        startActivity(Intent(requireContext(), AuthActivity::class.java))
    }
}
