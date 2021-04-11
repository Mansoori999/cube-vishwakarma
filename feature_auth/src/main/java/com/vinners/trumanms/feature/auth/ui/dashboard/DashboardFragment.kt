package com.vinners.trumanms.feature.auth.ui.dashboard

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.extensions.navigateSafely
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.FragmentDashboardBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import javax.inject.Inject

class DashboardFragment :
    BaseFragment<FragmentDashboardBinding, DashboardViewModel>(R.layout.fragment_dashboard) {
    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory

    companion object {
        const val INTENT_USER_NAME = "firstName"
        const val INTENT_MOBILE_NO = "mobile"
    }

    private var firstName: String? = null

    private val backPressListener = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          //  findNavController().popBackStack(R.id.loginFragment,false)
        }
    }


    override val viewModel: DashboardViewModel by viewModels {
        viewModelFactory
    }

    override fun onInitDependencyInjection() {
        DaggerAuthComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        firstName = arguments?.getString(INTENT_USER_NAME)
        viewBinding.mobileEt.text = firstName
        viewBinding.logOutBtn.setOnClickListener {
            findNavController().navigateSafely(R.id.open_login_from_dashboard)
        }
    }

    override fun onInitViewModel() {

    }
}