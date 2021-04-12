package com.vinners.cube_vishwakarma.feature_auth.ui

import android.content.pm.PackageManager
import androidx.activity.viewModels
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.feature_auth.R
import com.vinners.cube_vishwakarma.feature_auth.databinding.ActivityAuthBinding
import com.vinners.cube_vishwakarma.feature_auth.di.AuthViewModelFactory
import javax.inject.Inject


class AuthActivity : BaseActivity<ActivityAuthBinding, AuthViewModel>(R.layout.activity_auth) {

    companion object {
        private const val PERMISSION_REQUEST_STORAGE = 233
        const val REQUEST_CODE = 101
    }

    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory

    override val viewModel: AuthViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {

//        val coreComponent = (application as CoreApplication).initOrGetCoreDependency()
//        DaggerAuthComponent
//            .builder()
//            .coreComponent(coreComponent)
//            .build()
    }

    override fun onInitDataBinding() {
    }

    override fun onInitViewModel() {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //showCameraOptionDialog()
                } else {
                    //TODO handle manual permission here
                }
                return
            }
        }
    }


}
