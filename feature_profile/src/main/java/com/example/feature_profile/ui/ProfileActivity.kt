package com.example.feature_profile.ui

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import coil.api.load
import com.example.feature_profile.R
import com.example.feature_profile.databinding.ActivityProfileBinding
import com.example.feature_profile.di.DaggerProfileComponent
import com.example.feature_profile.di.ProfileViewModelFactory
import com.example.feature_profile.ui.bankDetails.BankDetailsActivity
import com.example.feature_profile.ui.certificate.CertificateActivity
import com.example.feature_profile.ui.documents.DocumentsActivity
import com.example.feature_profile.ui.jobHistory.JobHistoryActivity
import com.example.feature_profile.ui.kyc.KycActivity
import com.example.feature_profile.ui.medical.MedicalActivity
import com.example.notification.ui.NotificationActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.himanshu.cameraintegrator.ImageCallback
import com.himanshu.cameraintegrator.ImagesSizes
import com.himanshu.cameraintegrator.integrator.CameraIntegrator
import com.himanshu.cameraintegrator.integrator.GalleryIntegrator
import com.himanshu.cameraintegrator.storage.StorageMode
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.AppConstants
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.profile.UserProfile
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.feature.auth.ui.login.LoginActivity
import com.vinners.trumanms.feature.auth.ui.login.PrivacyPolicyActivity
import com.vinners.trumanms.feature.auth.ui.register.RegisterActivity
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class ProfileActivity :
    BaseActivity<ActivityProfileBinding, ProfileViewModel>(R.layout.activity_profile) {
    companion object {
        private const val PERMISSION_REQUEST_STORAGE = 233
        const val REQUEST_CODE = 101
    }

    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    @Inject
    lateinit var appInfo: AppInfo

    override val viewModel: ProfileViewModel by viewModels {
        viewModelFactory
    }

    private val cameraIntegrator: CameraIntegrator by lazy {
        CameraIntegrator(this)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setPublicDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }

    private val galleryIntegrator: GalleryIntegrator by lazy {
        GalleryIntegrator(this)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setPublicDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }

    override fun onInitDataBinding() {
        viewBinding.userIdET.text = userSessionManager.empCode
        viewBinding.appversionEt.text = appInfo.appVersion

        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
        viewBinding.profileEditLayout.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra(RegisterActivity.IS_FROM_PROFILE, true)
            startActivity(intent)
        }
        viewBinding.bankLayout.setOnClickListener {
            startActivity(Intent(this, BankDetailsActivity::class.java))
        }

        viewBinding.logoutLayout.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Log Out")
                .setMessage("Do you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.logout()
                }.setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }.show()
        }
        viewBinding.privacyPolicyLayout.setOnClickListener {
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            intent.putExtra(
                PrivacyPolicyActivity.INTENT_EXTRA_URL,
                "https://truman.ooo/privacypolicy"
            )
            startActivity(intent)
        }
        viewBinding.documentsLayout.setOnClickListener {
            startActivity(Intent(this, DocumentsActivity::class.java))
        }
        viewBinding.medicalBenefitsLayout.setOnClickListener {
            startActivity(Intent(this, MedicalActivity::class.java))
        }
        viewBinding.jobHistoryLayout.setOnClickListener {
            startActivity(Intent(this, JobHistoryActivity::class.java))
        }
        viewBinding.certificateLayout.setOnClickListener {
            startActivity(Intent(this, CertificateActivity::class.java))
        }
        viewBinding.myKycLayout.setOnClickListener {
            startActivity(Intent(this, KycActivity::class.java))
        }
        viewBinding.notificationIcon.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        viewBinding.userPic.setOnClickListener {
            if (storagePermissions())
                showCameraOptionDialog()
            else
                requestStoragePermissions()
        }
    }

    override fun onInitDependencyInjection() {
        DaggerProfileComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitViewModel() {
        viewModel.profile.observe(this, Observer {
            viewBinding.userNameET.text = it.firstName
            if (it.profilePic.isNullOrEmpty().not() && it.profilePicUpdated)
                viewBinding.userPic.load(File(it.profilePic))
            else if (it.profilePic.isNullOrEmpty().not())
                viewBinding.userPic.load(appInfo.getFullAttachmentUrl(it.profilePic!!))
            else if (it.gender.equals("Female", true))
                viewBinding.userPic.load(R.drawable.avatar_woman)
            else
                viewBinding.userPic.load(R.drawable.avatar_male)

        })
        viewModel.percentageProfileCompleteState.observe(this, Observer {
            viewBinding.progressPercentageTV.text = "$it%"
            viewBinding.progress.progress = it
        })
        viewModel.profilePicState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {

                }
                Lse.Success -> {
                    Toast.makeText(this, "profile Pic Updated Succefully", Toast.LENGTH_SHORT)
                        .show()
                }
                is Lse.Error -> {
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.profileState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    setProfiledata(it.content)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.logoutState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {

                }
                is Lse.Success -> {
                    Toast.makeText(this, "LogOut Successfully", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, LoginActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(i)
                }
                is Lse.Error -> {

                }
            }
        })
        viewModel.initViewModel()
        viewModel.getProfile()
    }

    fun setProfiledata(profile: UserProfile) {
        viewBinding.jobCompleteNo.text = profile.jobCompleted.toString()
        viewBinding.moneyEarned.text = profile.moneyEarned.toString()
        viewBinding.overallRating.text = "${profile.rateing}/5"
    }

    fun openCamera() {
        try {
            cameraIntegrator.initiateCapture()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openGallery() {
        try {
            galleryIntegrator.initiateImagePick()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showCameraOptionDialog() {
        val optionsForDialog = arrayOf<CharSequence>("Open Camera", "Select from Gallery")
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Select An Option")
        alertBuilder.setIcon(com.vinners.trumanms.feature.auth.R.drawable.ic_camera)
        alertBuilder.setItems(optionsForDialog, DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        })
        alertBuilder.setNegativeButton("Cancel") { dialog12, _ -> dialog12.dismiss() }
        alertBuilder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CameraIntegrator.REQUEST_IMAGE_CAPTURE)
            cameraIntegrator.parseResults(requestCode, resultCode, data, imageCallback)
        else if (requestCode == GalleryIntegrator.REQUEST_IMAGE_PICK)
            galleryIntegrator.parseResults(requestCode, resultCode, data, imageCallback)
    }

    private val imageCallback =
        ImageCallback { requestedBy, result, error ->

            if (result != null) {
                val imagePath = result.imagePath
                // viewBinding.userPic.load(File(imagePath))
                viewModel.updateProfilePic(imagePath)
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCameraOptionDialog()
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
