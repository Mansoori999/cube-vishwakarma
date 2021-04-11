package com.example.feature_profile.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.feature_profile.R
import com.example.feature_profile.di.DaggerProfileComponent
import com.example.feature_profile.di.ProfileViewModelFactory
import com.himanshu.trumanms.core.databinding.ActivityLogOutBinding
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.feature.auth.ui.login.LoginActivity
import javax.inject.Inject

class LogOutActivity : BaseActivity<ActivityLogOutBinding,ProfileViewModel>(R.layout.activity_log_out) {

    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

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
        viewBinding.logOutBtn.setOnClickListener {

        }
    }

    override fun onInitViewModel() {

    }

}