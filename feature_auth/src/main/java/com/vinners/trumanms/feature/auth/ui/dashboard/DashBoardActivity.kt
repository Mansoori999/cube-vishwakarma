package com.vinners.trumanms.feature.auth.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.base.CoreApplication
import com.vinners.trumanms.core.extensions.navigateSafely
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.ActivityDashBoardBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import com.vinners.trumanms.feature.auth.ui.login.LoginActivity
import javax.inject.Inject

class DashBoardActivity :
    BaseActivity<ActivityDashBoardBinding, DashboardViewModel>(R.layout.activity_dash_board) {

    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory

    companion object {
        const val INTENT_USER_NAME = "firstName"
        const val INTENT_MOBILE_NO = "mobile"
    }

    private var firstName: String? = null

    override val viewModel: DashboardViewModel by viewModels {
        viewModelFactory
    }

    override fun onInitDependencyInjection() {
        val coreComponent = (application as CoreApplication).initOrGetCoreDependency()
        DaggerAuthComponent
            .builder()
            .coreComponent(coreComponent)
            .build()
    }

    override fun onInitDataBinding() {
        firstName = intent?.getStringExtra(INTENT_USER_NAME)
        viewBinding.mobileEt.text = firstName
        viewBinding.logOutBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onInitViewModel() {

    }

    override fun onBackPressed() {
        val i = Intent(this@DashBoardActivity, LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
       // i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
       // i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
    }
}