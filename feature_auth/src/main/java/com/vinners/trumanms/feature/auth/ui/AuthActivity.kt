package com.vinners.trumanms.feature.auth.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.ActivityAuthBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.ui.register.RegisterFragment
import javax.inject.Inject


class AuthActivity : BaseActivity<ActivityAuthBinding,AuthViewModel>(R.layout.activity_auth) {

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
        if (!storagePermissions())
            requestStoragePermissions()
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



    private fun storagePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            PERMISSION_REQUEST_STORAGE
        )
    }
}
