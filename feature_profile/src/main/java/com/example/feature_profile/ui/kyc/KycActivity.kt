package com.example.feature_profile.ui.kyc

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import coil.api.load
import coil.decode.DataSource
import coil.request.LoadRequestBuilder
import coil.request.Request
import coil.request.Request.Listener
import com.example.feature_profile.R
import com.example.feature_profile.databinding.ActivityKycBinding
import com.example.feature_profile.di.DaggerProfileComponent
import com.example.feature_profile.di.ProfileViewModelFactory
import com.example.feature_profile.ui.AstericPasswordTransformation
import com.example.feature_profile.ui.bankDetails.BankDetailsActivity
import com.example.feature_profile.ui.bankDetails.BankDetailsViewModel
import com.github.razir.progressbutton.hideDrawable
import com.github.razir.progressbutton.showProgress
import com.himanshu.cameraintegrator.ImageCallback
import com.himanshu.cameraintegrator.ImagesSizes
import com.himanshu.cameraintegrator.integrator.CameraIntegrator
import com.himanshu.cameraintegrator.integrator.GalleryIntegrator
import com.himanshu.cameraintegrator.storage.StorageMode
import com.jsibbold.zoomage.ZoomageView
import com.vinners.core.logger.Logger
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.AppConstants
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.bank.Bank
import com.vinners.trumanms.data.models.profile.ProfileDetails

import java.io.File
import java.lang.Exception
import javax.inject.Inject

class KycActivity : BaseActivity<ActivityKycBinding, KycViewModel>(R.layout.activity_kyc) {
    companion object {
        private const val PERMISSION_REQUEST_STORAGE = 233
        const val REQUEST_CODE = 101
        const val ADHAR = "Aadhar"
        const val PAN = "PAN"
    }

    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    @Inject
    lateinit var logger: Logger

    private var highlightedEditTextIndex = 0
    private var previousLength = 0
    private var aadharNo: String? = null
    private lateinit var fullPic: ZoomageView
    private lateinit var dialog: Dialog
    private var isAdharFrontClick: Boolean = false
    private var isAdharBackClick: Boolean = false
    private var isPanClick: Boolean = false
    private var adharLocalFrontUrl: String? = null
    private var adharLocalBackUrl: String? = null
    private var adharRemoteFrontUrl: String? = null
    private var adharRemoteBackUrl: String? = null
    private var panUrl: String? = null
    private var isPanUploading: Boolean = false

    override val viewModel: KycViewModel by viewModels {
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

    private val spinnerItemClickListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (viewBinding.docSpinner.selectedItem.equals(ADHAR)) {
                viewBinding.panLayout.root.setVisibilityGone()
                viewBinding.aadharLayout.root.setVisibilityVisible()
            } else if (viewBinding.docSpinner.selectedItem.equals(PAN)) {
                viewBinding.aadharLayout.root.setVisibilityGone()
                viewBinding.panLayout.root.setVisibilityVisible()
            } else {
                viewBinding.aadharLayout.root.setVisibilityGone()
                viewBinding.panLayout.root.setVisibilityGone()
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

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
        viewBinding.aadharLayout.et1.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et2.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et3.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et4.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et5.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et6.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et7.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et8.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et9.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et10.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et11.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }
        viewBinding.aadharLayout.et12.setOnClickListener { showSoftKeyboard(viewBinding.aadharLayout.YoEt) }

        startListeningForOtpTypeEvents()
        viewBinding.aadharLayout.reEnterAadharEt.transformationMethod =
            AstericPasswordTransformation()
        viewBinding.panLayout.reEnterPanEt.transformationMethod = AstericPasswordTransformation()

        viewBinding.panLayout.panEt.doOnTextChanged { text, start, count, after ->
            if (viewBinding.panLayout.panNoLayout.error != null)
                viewBinding.panLayout.panNoLayout.error = null
        }
        viewBinding.panLayout.reEnterPanEt.doOnTextChanged { text, start, count, after ->
            if (viewBinding.panLayout.reEnterPanNoLayout.error != null)
                viewBinding.panLayout.reEnterPanNoLayout.error = null
        }
        viewBinding.aadharLayout.aadharEt.doOnTextChanged { text, start, count, after ->
            if (viewBinding.aadharLayout.aadharNoLayout.error != null)
                viewBinding.aadharLayout.aadharNoLayout.error = null
        }
        viewBinding.aadharLayout.reEnterAadharEt.doOnTextChanged { text, start, count, after ->
            if (viewBinding.aadharLayout.reEnteraAadharNoLayout.error != null)
                viewBinding.aadharLayout.reEnteraAadharNoLayout.error = null
        }

        viewBinding.docSpinner.onItemSelectedListener = spinnerItemClickListener
        viewBinding.panLayout.clickPanPhoto.setOnClickListener {
            isPanClick = true
            isAdharBackClick = false
            isAdharFrontClick = false
            if (storagePermissions())
                showCameraOptionDialog()
            else
                requestStoragePermissions()
        }
        viewBinding.aadharLayout.clickFrontAdharPhoto.setOnClickListener {
            isPanClick = false
            isAdharBackClick = false
            isAdharFrontClick = true
            if (storagePermissions())
                showCameraOptionDialog()
            else
                requestStoragePermissions()
        }
        viewBinding.aadharLayout.clickBackAdharPhoto.setOnClickListener {
            isPanClick = false
            isAdharBackClick = true
            isAdharFrontClick = false
            if (storagePermissions())
                showCameraOptionDialog()
            else
                requestStoragePermissions()
        }
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
        viewBinding.panImageContainer.setOnClickListener {
            if (panUrl.isNullOrEmpty().not()) {
                fullPic.load(appInfo.getFullAttachmentUrl(panUrl!!))
                dialog.show()
            }
        }
        viewBinding.adharFrontImageContainer.setOnClickListener {
            if (adharRemoteFrontUrl.isNullOrEmpty().not()) {
                fullPic.load(appInfo.getFullAttachmentUrl(adharRemoteFrontUrl!!))
                dialog.show()
            }
        }
        viewBinding.adharBackImageContainer.setOnClickListener {
            if (adharRemoteBackUrl.isNullOrEmpty().not()) {
                fullPic.load(appInfo.getFullAttachmentUrl(adharRemoteBackUrl!!))
                dialog.show()
            }
        }
        viewBinding.panLayout.uploadDocBtn.setOnClickListener {
            isPanUploading = true
            if (isPanDetailsValidated()) {
                val panNo = viewBinding.panLayout.panEt.text.toString()
                val bank = Bank(
                    panNo = panNo
                )
                viewModel.uploadDocumentss("pan", bank, panUrl, null)
            }
        }
        viewBinding.aadharLayout.uploadDocBtn.setOnClickListener {
            isPanUploading = false
            if (isAadharDetailsValidated()) {
                val adharNo = viewBinding.aadharLayout.aadharEt.text.toString()
                val bank = Bank(
                    adharNo = aadharNo
                )
                viewModel.uploadDocumentss("adhar", bank, adharLocalFrontUrl, adharLocalBackUrl)
            }
        }
    }

    override fun onInitViewModel() {
        viewModel.profileDetailsState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    setDataOnView(it.content)
                    //viewBinding.docSpinner.setSelection(0)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.kycState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {
                    if (isPanUploading) {
                        viewBinding.panLayout.uploadDocBtn.showProgress {
                            buttonText = "Loading"
                            progressColor = Color.WHITE
                        }
                        viewBinding.panLayout.uploadDocBtn.isEnabled = false
                    } else {
                        viewBinding.aadharLayout.uploadDocBtn.showProgress {
                            buttonText = "Loading"
                            progressColor = Color.WHITE
                        }
                        viewBinding.aadharLayout.uploadDocBtn.isEnabled = false
                    }
                }
                is Lse.Success -> {
                    if (isPanUploading) {
                        viewBinding.panLayout.uploadDocBtn.hideDrawable(
                            "Upload PAN"
                        )
                        viewBinding.panLayout.uploadDocBtn.isEnabled = true
                        Toast.makeText(this, "PAN Updated Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        viewBinding.aadharLayout.uploadDocBtn.hideDrawable(
                            "Upload Aadhar"
                        )
                        viewBinding.aadharLayout.uploadDocBtn.isEnabled = true
                        Toast.makeText(this, "Aadhar Updated Successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                    viewModel.getProfileDetails()
                }
                is Lse.Error -> {
                    if (isPanUploading) {
                        viewBinding.panLayout.uploadDocBtn.hideDrawable(
                            "Upload PAN"
                        )
                        viewBinding.panLayout.uploadDocBtn.isEnabled = true
                    } else {
                        viewBinding.aadharLayout.uploadDocBtn.hideDrawable(
                            "Upload Aadhar"
                        )
                        viewBinding.aadharLayout.uploadDocBtn.isEnabled = true
                    }
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.getProfileDetails()
    }

    fun setDataOnView(profileDetails: ProfileDetails) {
        if (profileDetails.adharpic.isNullOrEmpty() && profileDetails.panpic.isNullOrEmpty()) {
            viewBinding.imageContainerLayout.setVisibilityGone()
            viewBinding.documentsUploadLayout.setVisibilityVisible()
            val documentList = mutableListOf("Select Documents", ADHAR, PAN)
            populateSpiner(documentList)
        } else if (profileDetails.adharpic.isNullOrEmpty()
                .not() && profileDetails.panpic.isNullOrEmpty()
        ) {
            viewBinding.aadharLayout.root.setVisibilityGone()
            viewBinding.documentsUploadLayout.setVisibilityVisible()
            viewBinding.imageContainerLayout.setVisibilityVisible()
            val documentList = mutableListOf("Select Documents", PAN)
            populateSpiner(documentList)
        } else if (profileDetails.adharpic.isNullOrEmpty() && profileDetails.panpic.isNullOrEmpty()
                .not()
        ) {
            viewBinding.panLayout.root.setVisibilityGone()
            viewBinding.documentsUploadLayout.setVisibilityVisible()
            viewBinding.imageContainerLayout.setVisibilityVisible()
            val documentList = mutableListOf("Select Documents", ADHAR)
            populateSpiner(documentList)
        } else {
            viewBinding.aadharLayout.root.setVisibilityGone()
            viewBinding.panLayout.root.setVisibilityGone()
            viewBinding.documentsUploadLayout.setVisibilityGone()
            viewBinding.imageContainerLayout.setVisibilityVisible()
        }

        if (profileDetails.adharpic.isNullOrEmpty().not()) {
            val fullAdharPicUrl = profileDetails.adharpic?.split(",")
            adharRemoteFrontUrl = fullAdharPicUrl?.get(0)
            adharRemoteBackUrl = fullAdharPicUrl?.get(1)
            // viewBinding.imageContainerLayout.setVisibilityVisible()
            viewBinding.adharFrontImageContainer.load(
                appInfo.getFullAttachmentUrl(
                    adharRemoteFrontUrl!!
                )
            ) {
               listener(object : Request.Listener{
                   override fun onError(request: Request, throwable: Throwable) {
                       super.onError(request, throwable)
                       logger.d("aadhar image - ${throwable.localizedMessage}")
                   }
               })

            }
            viewBinding.adharBackImageContainer.load(appInfo.getFullAttachmentUrl(adharRemoteBackUrl!!))
            viewBinding.adharNo.text = "Aadhar No - ${profileDetails.adhar}"
        }
        if (profileDetails.panpic.isNullOrEmpty().not()) {
            panUrl = profileDetails.panpic
            viewBinding.imageContainerLayout.setVisibilityVisible()
            viewBinding.panImageContainer.load(appInfo.getFullAttachmentUrl(profileDetails.panpic!!))
            viewBinding.panNo.text = "PAN No - ${profileDetails.pan}"
        }
    }

    fun populateSpiner(documentList: List<String>) {

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, documentList)
        viewBinding.docSpinner.adapter = adapter
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
                if (isAdharFrontClick) {
                    viewBinding.aadharLayout.adharFrontContainer.load(File(imagePath))
                    adharLocalFrontUrl = imagePath
                } else if (isAdharBackClick) {
                    viewBinding.aadharLayout.adharBackContainer.load(File(imagePath))
                    adharLocalBackUrl = imagePath
                } else if (isPanClick) {
                    viewBinding.panLayout.panContainer.load(File(imagePath))
                    panUrl = imagePath
                }
                // imageFilePath = result.imagePath
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

    private fun isPanDetailsValidated(): Boolean {
        val panDocNumber = viewBinding.panLayout.panEt.text.toString()
        if (panUrl == null) {
            showInformationDialog("Please Select Image")
            return false
        } else if (panDocNumber.isEmpty()) {
            viewBinding.panLayout.panNoLayout.error = "Invalid PAN Number"
            return false
        } else if (panDocNumber.length < 10
            || !panDocNumber[0].isLetter()
            || !panDocNumber[1].isLetter()
            || !panDocNumber[2].isLetter()
            || !panDocNumber[3].isLetter()
            || !panDocNumber[4].isLetter()
            || !panDocNumber[3].equals(('P'))
            || !panDocNumber[9].isLetter()
        ) {
            viewBinding.panLayout.panNoLayout.error = "Invalid PAN Number"
            return false
        } else if (viewBinding.panLayout.reEnterPanEt.text.toString()
                .equals(viewBinding.panLayout.panEt.text.toString()).not()
        ) {
            viewBinding.panLayout.reEnterPanNoLayout.error = "Entered value must match PAN no"
            return false
        }
        return true
    }

    private fun isAadharDetailsValidated(): Boolean {
        val aadharDocNumber = viewBinding.aadharLayout.aadharEt.text.toString()
        if (aadharNo.isNullOrEmpty()) {
            showInformationDialog("Please Enter Aadhar Number")
            return false
        } else if (adharLocalFrontUrl.isNullOrEmpty()) {
            showInformationDialog("Please Select Front Image")
            return false
        } else if (adharLocalBackUrl.isNullOrEmpty()) {
            showInformationDialog("Please Select Back Image")
            return false
        } else if (aadharNo?.length!! < 12 || aadharNo?.startsWith('0')!! || aadharNo?.startsWith(
                '1'
            )!!
        ) {
            showInformationDialog("Invalid aadhar no")
            return false
        } else if (viewBinding.aadharLayout.reEnterAadharEt.text.toString()
                .equals(aadharNo).not()
        ) {
            viewBinding.aadharLayout.reEnteraAadharNoLayout.error =
                "Entered value must match aadhar no"
            return false
        }
        return true
    }

    private fun startListeningForOtpTypeEvents() {
        viewBinding.aadharLayout.YoEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val currentLength = s!!.length
                Log.d("TAG", s.toString())

                if (currentLength > previousLength) {
                    //User has Entered A digit
                    insertDigitInCurrentAndHighlightNextOne(s, currentLength)
                } else {
                    //User has deleted A digit
                    deleteHighlightedDigit(s)
                }

                previousLength = currentLength
            }

            private fun deleteHighlightedDigit(s: CharSequence) {
                if (highlightedEditTextIndex == 0) {
                    val editText = viewBinding.aadharLayout.editTextHolder.getChildAt(0) as EditText
                    editText.text = null
                } else {

                    val editText =
                        viewBinding.aadharLayout.editTextHolder.getChildAt(highlightedEditTextIndex) as EditText
                    val text = editText.text.toString()

                    if (highlightedEditTextIndex == 11 && text.isNotEmpty()) {
                        editText.text = null
                        editText.background =
                            ContextCompat.getDrawable(
                                this@KycActivity,
                                com.vinners.trumanms.feature.auth.R.drawable.login_mobile_edit_text_background
                            )
                    } else {

                        editText.text = null
                        editText.background =
                            ContextCompat.getDrawable(
                                this@KycActivity,
                                com.vinners.trumanms.feature.auth.R.drawable.login_mobile_edit_text_background
                            )

                        val editTextNext =
                            viewBinding.aadharLayout.editTextHolder.getChildAt(
                                highlightedEditTextIndex - 1
                            ) as EditText
                        editTextNext.background =
                            ContextCompat.getDrawable(
                                this@KycActivity,
                                com.vinners.trumanms.feature.auth.R.drawable.login_mobile_edit_text_background
                            )

                        if (text.isEmpty())
                            editTextNext.text = null

                        highlightedEditTextIndex--
                    }
                }
                //  mobileNo = s.toString()
            }

            private fun insertDigitInCurrentAndHighlightNextOne(
                s: CharSequence,
                currentLength: Int
            ) {
                val editText =
                    viewBinding.aadharLayout.editTextHolder.getChildAt(previousLength) as EditText
                editText.setText(s.subSequence(previousLength, currentLength).toString())

                if (currentLength <= 12) {
                    editText.setBackgroundResource(com.vinners.trumanms.feature.auth.R.color.white)
                    val editTextNext =
                        viewBinding.aadharLayout.editTextHolder.getChildAt(currentLength) as EditText
                    editTextNext.background =
                        ContextCompat.getDrawable(
                            this@KycActivity,
                            R.drawable.login_mobile_edit_text_background
                        )
                    if (currentLength < 12)
                        highlightedEditTextIndex = currentLength
                }
                aadharNo = s.toString()
            }
        })
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

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
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