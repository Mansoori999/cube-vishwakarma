package com.vinners.cube_vishwakarma.feature_auth.ui.login.OtpVerify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.chaos.view.PinView
import com.vinners.cube_vishwakarma.core.base.BaseFragment
import com.vinners.cube_vishwakarma.feature_auth.R
import com.vinners.cube_vishwakarma.feature_auth.databinding.ActivityOtpcheckBinding
import com.vinners.cube_vishwakarma.feature_auth.ui.otpverify.OtpViewModel
import kotlinx.coroutines.newFixedThreadPoolContext

class Otpcheck : BaseFragment<ActivityOtpcheckBinding, OtpViewModel>(R.layout.activity_otpcheck){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pin = viewBinding.pinview

    }

    override val viewModel: OtpViewModel by viewModels {defaultViewModelProviderFactory }


    override fun onInitDependencyInjection() {

    }

    override fun onInitDataBinding() {

    }

    override fun onInitViewModel() {

    }
}


