package com.example.feature_profile.ui

import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.feature_profile.R
import com.example.feature_profile.di.DaggerProfileComponent
import com.example.feature_profile.di.ProfileViewModelFactory
import com.himanshu.trumanms.core.databinding.FragmentLogOutBinding
import com.vinners.trumanms.core.base.BaseFragment
import javax.inject.Inject

class LogoutFragment: BaseFragment<FragmentLogOutBinding,ProfileViewModel>(R.layout.fragment_log_out) {

    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    override val viewModel: ProfileViewModel by viewModels {
        viewModelFactory
    }

    override fun onInitDependencyInjection() {
        DaggerProfileComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {

    }

    override fun onInitViewModel() {

    }
}