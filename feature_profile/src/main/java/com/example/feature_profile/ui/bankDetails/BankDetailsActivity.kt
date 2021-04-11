package com.example.feature_profile.ui.bankDetails

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import coil.api.load
import com.bumptech.glide.Glide
import com.example.feature_profile.R
import com.example.feature_profile.di.DaggerProfileComponent
import com.example.feature_profile.di.ProfileViewModelFactory
import com.example.feature_profile.ui.AstericPasswordTransformation
import com.example.feature_profile.ui.ProfileViewModel
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.himanshu.cameraintegrator.ImageCallback
import com.himanshu.cameraintegrator.ImagesSizes
import com.himanshu.cameraintegrator.integrator.CameraIntegrator
import com.himanshu.cameraintegrator.integrator.GalleryIntegrator
import com.himanshu.cameraintegrator.storage.StorageMode
import com.himanshu.trumanms.core.databinding.ActivityBankDetailsBinding
import com.jsibbold.zoomage.ZoomageView
import com.vinners.trumanms.core.AppConstants
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.bank.Bank
import com.vinners.trumanms.data.models.profile.Profile
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import java.io.File
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class BankDetailsActivity :
    BaseActivity<ActivityBankDetailsBinding, BankDetailsViewModel>(R.layout.activity_bank_details) {
    companion object {
        private const val PERMISSION_REQUEST_STORAGE = 233
        const val REQUEST_CODE = 101
    }
    private var imageFilePath: String? = null
    private var ifscCode: String? = null
    private lateinit var fullPic: ZoomageView
    private lateinit var dialog: Dialog
    private var bankName: String? = null
    private var branchName: String? = null
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: BankDetailsViewModel by viewModels {
        viewModelFactory
    }
    private val cameraIntegrator: CameraIntegrator by kotlin.lazy {
        CameraIntegrator(this)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setPublicDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }

    private val galleryIntegrator: GalleryIntegrator by kotlin.lazy {
        GalleryIntegrator(this)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setPublicDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }


    override fun onInitDependencyInjection() {
        DaggerProfileComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        getFullsizePicContainer()
        viewBinding.reEnterAccountEt.transformationMethod = AstericPasswordTransformation()
        viewBinding.accountEt.doOnTextChanged { text, start, count, after ->
            if (viewBinding.accNoLayout.error != null)
                viewBinding.accNoLayout.error = null
        }
        viewBinding.reEnterAccountEt.doOnTextChanged { text, start, count, after ->
            if (viewBinding.reEnterAccountLayout.error != null)
                viewBinding.reEnterAccountLayout.error = null
        }
        viewBinding.searchIfscCode.setOnClickListener {
            val ifscCode = viewBinding.ifscCodeEt.text.toString()

            if (ifscCode.isEmpty().not())
                viewModel.getBankDetails(ifscCode)
            else
                showInformationDialog("Enter IFSC Code")
        }
        viewBinding.clickChecquePhoto.setOnClickListener {
            if (storagePermissions())
                showCameraOptionDialog()
            else
                requestStoragePermissions()
        }
        viewBinding.saveBankDetailsBtn.setOnClickListener {
            if (validateFields()) {
                val accountNo = viewBinding.accountEt.text.toString()
                val nameOnBank = viewBinding.nameOnBankEt.text.toString()
                val bank = Bank(
                    bankName = bankName,
                    accountNo = accountNo,
                    nameOnBank = nameOnBank,
                   bankBranch = branchName,
                    ifsc = ifscCode
                )
                viewModel.uploadBankDetails("bank",bank,imageFilePath,null)
            }
        }
        viewBinding.sampleCheckqueImg.setOnClickListener {
            fullPic.load(R.drawable.ic_ifsc)
            dialog.show()
        }
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    fun validateFields(): Boolean {
        if (viewBinding.accountEt.text.isNullOrEmpty()) {
            viewBinding.accNoLayout.error = "Enter Valid Account No"
            return false
        }
        if (viewBinding.reEnterAccountEt.text.toString().equals(viewBinding.accountEt.text.toString()).not()) {
            viewBinding.reEnterAccountLayout.error = "Account number mismatched"
            return false
        }
        return true
    }

    override fun onInitViewModel() {
        viewModel.profileState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    if (it.content?.accountNo.isNullOrEmpty().not()) {
                        viewBinding.ifscLayout.setVisibilityGone()
                        viewBinding.bankDetailsLayout.setVisibilityVisible()
                        setBankDetails(it.content)
                    } else {
                        viewBinding.bankDetailsLayout.setVisibilityGone()
                        viewBinding.ifscLayout.setVisibilityVisible()
                    }
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.bankDetailsState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.searchIfscCode.showProgress {
                        buttonText ="Loading"
                        progressColor = Color.WHITE
                    }
                    viewBinding.searchIfscCode.isEnabled = false
                }
                is Lce.Content -> {
                    viewBinding.searchIfscCode.hideProgress(
                        "Search"
                    )
                    viewBinding.searchIfscCode.isEnabled = true
                    viewBinding.bankNoDataFoundLayout.setVisibilityGone()
                    viewBinding.bankDetailsContainer.setVisibilityVisible()
                    ifscCode = it.content.ifsc
                    bankName = it.content.bank
                    branchName = it.content.branch
                    viewBinding.bankNameET.text = " :  ${it.content.bank}"
                    viewBinding.branchNameET.text = " :  ${it.content.branch}"
                    viewBinding.ifscNameET.text = " :  ${it.content.ifsc}"
                }
                is Lce.Error -> {
                    viewBinding.searchIfscCode.hideProgress(
                        "Search"
                    )
                    viewBinding.searchIfscCode.isEnabled = true
                    viewBinding.bankDetailsContainer.setVisibilityGone()
                    viewBinding.bankNoDataFoundLayout.setVisibilityVisible()

                }
            }
        })
        viewModel.uploadBankDetailsState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {
                    viewBinding.saveBankDetailsBtn.showProgress {
                        buttonText ="Loading"
                        progressColor = Color.WHITE
                    }
                    viewBinding.saveBankDetailsBtn.isEnabled = false
                }
                is Lse.Success -> {
                    viewBinding.searchIfscCode.hideProgress(
                        "Submit"
                    )
                    viewBinding.saveBankDetailsBtn.isEnabled = true
                    Toast.makeText(this, "Bank Details Uploaded", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Lse.Error -> {
                    viewBinding.searchIfscCode.hideProgress(
                        "Submit"
                    )
                    viewBinding.saveBankDetailsBtn.isEnabled = true
                    Toast.makeText(this, "Error while updating", Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.getCachedProfile()
    }
    fun setBankDetails(metaData: Profile?) {
        viewBinding.bankName.text = metaData?.bankName
       viewBinding.branchName.text = metaData?.mobileOnBank
        viewBinding.accNo.text = metaData?.accountNo
        viewBinding.beneficiaryName.text = metaData?.nameOnBank
        viewBinding.ifscNo.text = metaData?.ifsc
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
                imageFilePath = result.imagePath
                 viewBinding.chequePhotoContainer.load(File(imagePath))
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
            this!!, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    this!!, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    this!!, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(
            this!!,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            PERMISSION_REQUEST_STORAGE
        )
    }


    private fun getFullsizePicContainer() {
        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.big_picture_image)
           // window.attributes.windowAnimations = R.style.MyAlertDialogStyle
        }
        fullPic = dialog.findViewById(R.id.big_picture)
    }
}