package com.vinners.cube_vishwakarma.feature_auth.ui.login.OtpVerify

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.base.BaseFragment
import com.vinners.cube_vishwakarma.feature_auth.R
import com.vinners.cube_vishwakarma.feature_auth.databinding.ActivityOtpcheckBinding
import com.vinners.cube_vishwakarma.feature_auth.ui.otpVerify.OtpViewModel


class OtpCheckActivity : BaseActivity<ActivityOtpcheckBinding, OtpViewModel>(R.layout.activity_otpcheck){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pin = viewBinding.pinview
        initView()

    }

    private fun initView() {
        viewBinding.continuebutton.setOnClickListener {  }
    }

    override val viewModel: OtpViewModel by viewModels {defaultViewModelProviderFactory }


    override fun onInitDependencyInjection() {

    }

    override fun onInitDataBinding() {

    }

    override fun onInitViewModel() {

    }
}


