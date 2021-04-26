package com.vinners.cube_vishwakarma.ui


import android.os.Bundle
import androidx.activity.viewModels
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.databinding.ActivityMainBinding



class MainActivity : BaseActivity<ActivityMainBinding , MainActivityViewModel>(R.layout.activity_main) {

    //Issey change kar lo
    override val viewModel: MainActivityViewModel by viewModels {defaultViewModelProviderFactory }

    override fun onInitDependencyInjection() {

    }

    override fun onInitDataBinding() {

    }

    override fun onInitViewModel() {

    }

}