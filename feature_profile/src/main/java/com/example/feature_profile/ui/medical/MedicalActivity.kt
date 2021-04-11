package com.example.feature_profile.ui.medical

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.feature_profile.R
import com.example.feature_profile.databinding.ActivityMedicalBinding
import com.example.feature_profile.di.DaggerProfileComponent
import com.example.feature_profile.di.ProfileViewModelFactory
import com.example.feature_profile.ui.ProfileViewModel
import com.vinners.trumanms.core.base.BaseActivity
import javax.inject.Inject

class MedicalActivity : BaseActivity<ActivityMedicalBinding,ProfileViewModel>(R.layout.activity_medical) {
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
       viewBinding.backBtn.setOnClickListener {
           onBackPressed()
       }
    }

    override fun onInitViewModel() {

    }

}