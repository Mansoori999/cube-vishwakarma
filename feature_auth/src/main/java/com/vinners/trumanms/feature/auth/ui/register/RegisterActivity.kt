package com.vinners.trumanms.feature.auth.ui.register

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import coil.api.load
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.himanshu.cameraintegrator.ImageCallback
import com.himanshu.cameraintegrator.ImagesSizes
import com.himanshu.cameraintegrator.integrator.CameraIntegrator
import com.himanshu.cameraintegrator.integrator.GalleryIntegrator
import com.himanshu.cameraintegrator.storage.StorageMode
import com.vinners.trumanms.core.AppConstants
import com.vinners.trumanms.core.DateTimeHelper
import com.vinners.trumanms.core.QuickAlertDialog
import com.vinners.trumanms.core.QuickAlertDialog.showNonCancellableMessageDialog
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.selectItemWithText
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.location.GpsSettingsCheckCallback
import com.vinners.trumanms.core.location.LocationHelper
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.auth.RegisterRequest
import com.vinners.trumanms.data.models.profile.Profile
import com.vinners.trumanms.data.models.stateAndCity.City
import com.vinners.trumanms.data.models.stateAndCity.Pincode
import com.vinners.trumanms.data.models.stateAndCity.State
import com.vinners.trumanms.data.models.stateAndCity.WorkCategory
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.ActivityRegisterBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog.*
import io.apptik.widget.multiselectspinner.BaseMultiSelectSpinner
import org.w3c.dom.Text
import java.io.File
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject


class RegisterActivity :
    BaseActivity<ActivityRegisterBinding, RegisterViewModel>(R.layout.activity_register),
    DatePickerDialog.OnDateSetListener, OnDialogClickListener {

    companion object {
        const val REQUEST_UPGRADE_GPS_SETTINGS = 120
        const val REQUEST_UPDATE_GPS_SETTINGS_MANUALLY = 121
        private const val PERMISSION_REQUEST_STORAGE = 233
        private const val PERMISSION_REQUEST_LOCATION = 243
        private const val PERMISSION_REQUEST_ACCOUNT = 244
        private const val ACCOUNTS_REQUEST_CODE = 111
        const val REQUEST_CODE = 101
        const val IS_FROM_PROFILE = "isFromProfile"
        const val INTENT_EXTRA_MOBILE = "mobile"
        const val INDIVIDUAL_USER_TYPE = "Individual"
        const val AGENCY_USER_TYPE = "Agency"
    }

    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager
    private var dateOfBirth: String? = null
    private var isFromProfile: Boolean = false
    var workCategoryItemsWrapperIndividual: String? = null
    var workCategoryItemsWrapperAgency: String? = null
    var languageItemsWrapperIndividual: String? = null
    var languageItemsWrapperAgency: String? = null

    private var isIndividualLayoutSelected: Boolean = false
    private var gender: String? = null
    private var mobile: String? = null
    private var userType: String? = null
    private var userName: String? = null
    private var date: Date? = null
    private var stateId: String? = null
    private var state: State? = null
    private var city: City? = null
    private var pinCode: Pincode? = null
    private var profile: Profile? = null
    private val language: String? = null
    private var workCategory: String? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var address: String? = null


    override val viewModel: RegisterViewModel by viewModels { viewModelFactory }

    private val cameraIntegrator: CameraIntegrator by lazy {
        CameraIntegrator(this)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setImageDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }

    private val galleryIntegrator: GalleryIntegrator by lazy {
        GalleryIntegrator(this)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setImageDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }
    private val locationHelper: LocationHelper by lazy {
        LocationHelper(this)
            .apply {
                setRequiredGpsPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                setLocationCallback(locationCallback)
                init()
            }
    }
    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(location: LocationResult?) {
            if (location == null)
                return
            latitude = location.lastLocation.latitude.toString()
            longitude = location.lastLocation.longitude.toString()
            try {
                val addressList = Geocoder(
                    this@RegisterActivity,
                    Locale.getDefault()
                ).getFromLocation(latitude?.toDouble()!!, longitude?.toDouble()!!, 1)
                address = addressList[0].locality
            } catch (e: Exception) {

            }
        }
    }
    private val datePickerDialog: DatePickerDialog by kotlin.lazy {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR) - 18
        // date = calendar.time
        val datePicker = DatePickerDialog(
            this,
            android.app.AlertDialog.THEME_HOLO_LIGHT,
            this,
            year,
            month,
            dayOfMonth
        )
        datePicker
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.YEAR, year)
        date = calendar.time
        dateOfBirth = DateTimeHelper.getYYYYMMDDDate(calendar.time)
        viewBinding.layoutRegisterIndividualBasic.dobEt.setText(
            DateTimeHelper.getDDMMYYYYDate(
                calendar.time
            )
        )
    }

    override fun onInitDependencyInjection() {

        DaggerAuthComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this@RegisterActivity)
    }

    override fun onInitDataBinding() {
        mobile = intent?.getStringExtra(INTENT_EXTRA_MOBILE)
        isFromProfile = intent.getBooleanExtra(IS_FROM_PROFILE, false)
        if (mobile.isNullOrEmpty())
            mobile = userSessionManager.mobile

        setIndividualListener()
        setAgencyListener()
        commomLayoutListener()
        setEmailAddress()
        populateSpinner()
    }


    fun setEmailAddress() {
        val domainList = listOf(
            "@hotmail.com",
            "@gmail.com",
            "@yahoo.com",
            "@outlook.com",
            "@adinet.com.uy"
        )
        val customAdapter = CustomerAdapter(this, android.R.layout.simple_list_item_1, domainList)
        viewBinding.layoutRegisterIndividualBasic.emailEt.setAdapter(customAdapter)
        viewBinding.layoutRegisteragencyBasics.emailEt.setAdapter(customAdapter)
    }

    override fun onInitViewModel() {
        viewModel.profile.observe(this, androidx.lifecycle.Observer {
            this.profile = it
            setDataOnView(it)
        })
        viewModel.state.observe(this, androidx.lifecycle.Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    val stateList = it.content.sortedBy {
                        it.stateName
                    }
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.city.observe(this, androidx.lifecycle.Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    val cityList = it.content.sortedBy {
                        it.cityName
                    }

                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.pincode.observe(this, androidx.lifecycle.Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {

                }
                is Lce.Error -> {

                }
            }
        })

        viewModel.registerState.observe(this, androidx.lifecycle.Observer {
            when (it) {
                Lse.Loading -> {

                }
                is Lse.Success -> {
                    Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(
                        this@RegisterActivity,
                        Class.forName("com.vinners.trumanms.ui.home.HomeActivity")
                    )
                    startActivity(intent)
                    finish()
                }
                is Lse.Error -> {
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        viewModel.individualBasicValidate.observe(this, androidx.lifecycle.Observer {
            when (it) {
                ValidateState.Success -> {
                    viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.visibility =
                        View.GONE
                    viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.visibility =
                        View.VISIBLE
                    userName = viewBinding.layoutRegisterIndividualBasic.firstNameEt.text.toString()
                }
                is ValidateState.ValidateFailed -> {
                    showInformationDialog(it.result.message)
                }
            }
        })
        viewModel.agencyBasicsValidate.observe(this, androidx.lifecycle.Observer {
            when (it) {
                ValidateState.Success -> {
                    viewBinding.layoutRegisteragencyBasics.registerAgencyBasicsLayout.visibility =
                        View.GONE
                    viewBinding.layoutRegisterAgencyWork.registerAgencyWorkLayout.visibility =
                        View.VISIBLE
                    userName = viewBinding.layoutRegisteragencyBasics.firstNameEt.text.toString()
                }
                is ValidateState.ValidateFailed -> {
                    showInformationDialog(it.result.message)
                }
            }
        })

        viewModel.initViewModel()
        viewModel.getState()
    }

    fun setDataOnView(profile: Profile) {
        if (isFromProfile && profile.userType.equals(INDIVIDUAL_USER_TYPE, true)) {
            viewBinding.layoutRegisterUser.root.setVisibilityGone()
            viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.setVisibilityVisible()
        } else if (isFromProfile && profile.userType.equals(AGENCY_USER_TYPE, true)) {
            viewBinding.layoutRegisterUser.root.setVisibilityGone()
            viewBinding.layoutRegisteragencyBasics.registerAgencyBasicsLayout.setVisibilityVisible()
        }
        if (profile.userType.equals(INDIVIDUAL_USER_TYPE, true)) {
            isIndividualLayoutSelected = true
            viewBinding.layoutRegisterIndividualBasic.mobileEt.setText(mobile)
            userType = INDIVIDUAL_USER_TYPE
            viewBinding.layoutRegisterIndividualBasic.firstNameEt.setText(profile.firstName)
            viewBinding.layoutRegisterIndividualBasic.lastNameEt.setText(profile.lastName)
            viewBinding.layoutRegisterIndividualBasic.emailEt.setText(profile.email)
            viewBinding.layoutRegisterIndividualEducation.selectedLanguageEt.setText(profile.languages)
            viewBinding.layoutRegisterIndividualEducation.selectedWorkCatEt.setText(profile.workCategory)
            viewBinding.layoutRegisterUser.individualSelectIcon.setVisibilityVisible()
        } else if (profile.userType.equals(AGENCY_USER_TYPE, true)) {
            isIndividualLayoutSelected = false
            viewBinding.layoutRegisteragencyBasics.mobileEt.setText(mobile)
            userType = AGENCY_USER_TYPE
            viewBinding.layoutRegisteragencyBasics.firstNameEt.setText(profile.firstName)
            viewBinding.layoutRegisteragencyBasics.lastNameEt.setText(profile.lastName)
            viewBinding.layoutRegisteragencyBasics.emailEt.setText(profile.email)
            viewBinding.layoutRegisterAgencyWork.selectedLanguageEt.setText(profile.languages)
            viewBinding.layoutRegisterAgencyWork.selectedWorkCatEt.setText(profile.workCategory)
            viewBinding.layoutRegisterUser.agencySelectIcon.setVisibilityVisible()
        }
        if (profile.userType.equals(INDIVIDUAL_USER_TYPE, true) && profile.languages.isNullOrEmpty()
                .not()
        ) {
            viewBinding.layoutRegisterIndividualEducation.selectedLanguageEt.text =
                profile.languages
            languageItemsWrapperIndividual = profile.languages
        } else if (profile.userType.equals(
                AGENCY_USER_TYPE,
                true
            ) && profile.languages.isNullOrEmpty().not()
        ) {
            viewBinding.layoutRegisterAgencyWork.selectedLanguageEt.text = profile.languages
            languageItemsWrapperAgency = profile.languages
        } else {
            viewBinding.layoutRegisterIndividualEducation.selectedLanguageEt.text =
                LanguageDialogFragment.LANGUAGE
            viewBinding.layoutRegisterAgencyWork.selectedLanguageEt.text =
                LanguageDialogFragment.LANGUAGE
        }

        if (profile.userType.equals(
                INDIVIDUAL_USER_TYPE,
                true
            ) && profile.workCategory.isNullOrEmpty().not()
        ) {
            viewBinding.layoutRegisterIndividualEducation.selectedWorkCatEt.text =
                profile.workCategory
            workCategoryItemsWrapperIndividual = profile.workCategory
        } else if (profile.userType.equals(
                AGENCY_USER_TYPE,
                true
            ) && profile.workCategory.isNullOrEmpty().not()
        ) {
            viewBinding.layoutRegisterAgencyWork.selectedWorkCatEt.text = profile.workCategory
            workCategoryItemsWrapperAgency = profile.workCategory
        } else {
            viewBinding.layoutRegisterIndividualEducation.selectedWorkCatEt.text =
                WorkCategoryDialogFragment.WORK_CATEGORY
            viewBinding.layoutRegisterAgencyWork.selectedWorkCatEt.text =
                WorkCategoryDialogFragment.WORK_CATEGORY
        }


        if (profile.dob.isNullOrEmpty().not()) {
            date = DateTimeHelper.getDateFromYYYYMMDDformate(profile.dob!!)
            dateOfBirth = profile.dob
            viewBinding.layoutRegisterIndividualBasic.dobEt.setText(
                DateTimeHelper.getDDMMYYYYDateFromString(profile.dob!!)
            )
        }
        if (profile.gender.equals("Female", true)) {
            viewBinding.layoutRegisterIndividualBasic.maleBtn.isEnabled = true
            viewBinding.layoutRegisterIndividualBasic.femaleBtn.isEnabled = false
            viewBinding.layoutRegisterIndividualBasic.maleBtnText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )
            viewBinding.layoutRegisterIndividualBasic.femaleBtnText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            viewBinding.layoutRegisterIndividualBasic.maleBtnLayout.setBackgroundResource(R.drawable.button_normal)
            viewBinding.layoutRegisterIndividualBasic.femaleBtnLayout.setBackgroundResource(R.drawable.button_disable)
            viewBinding.layoutRegisterIndividualBasic.firstNameSpinner.setSelection(1)
            gender = profile.gender
        } else {
            viewBinding.layoutRegisterIndividualBasic.maleBtn.isEnabled = false
            viewBinding.layoutRegisterIndividualBasic.femaleBtn.isEnabled = true
            viewBinding.layoutRegisterIndividualBasic.maleBtnText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            viewBinding.layoutRegisterIndividualBasic.femaleBtnText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )
            viewBinding.layoutRegisterIndividualBasic.maleBtnLayout.setBackgroundResource(R.drawable.button_disable)
            viewBinding.layoutRegisterIndividualBasic.femaleBtnLayout.setBackgroundResource(R.drawable.button_normal)
            viewBinding.layoutRegisterIndividualBasic.firstNameSpinner.setSelection(0)
            gender = profile.gender
        }
        if (profile.education.isNullOrEmpty().not())
            viewBinding.layoutRegisterIndividualEducation.educationSpinner.selectItemWithText(
                profile.education!!
            )
        if (profile.experience.isNullOrEmpty().not())
            viewBinding.layoutRegisterIndividualEducation.experienceSpinner.selectItemWithText(
                profile.experience!!
            )
        if (profile.twoWheeler)
            viewBinding.layoutRegisterIndividualEducation.yesRadioBtn.isChecked = true
        else if (!profile.twoWheeler)
            viewBinding.layoutRegisterIndividualEducation.noRadioBtn.isChecked = true
        else {

        }
        if (profile.state.isNullOrEmpty().not() && profile.stateId != null) {
            state = State(
                stateName = profile.state!!,
                stateId = profile.stateId.toString()
            )
            viewBinding.layoutRegisterIndividualWorkLocation.selectedStateEt.text = profile.state
        }
        if (profile.district.isNullOrEmpty()
                .not() && profile.districtId != null
        ) {
            city = City(
                cityName = profile.district!!,
                cityId = profile.districtId.toString()
            )
            viewBinding.layoutRegisterIndividualWorkLocation.selectedCityEt.setText(profile.district)
        }
        if (profile.pinCode.isNullOrEmpty().not() && profile.pinCodeId != null) {
            pinCode = Pincode(
                pincodeId = profile.pinCodeId.toString(),
                pincode = profile.pinCode!!
            )
            viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.setText(profile.pinCode)
        } else if (profile.pinCode.isNullOrEmpty().not() && profile.pinCodeId == null) {
            viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.setVisibilityVisible()
            viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.setText(profile.pinCode)
            viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.setText(
                PincodeDialogFragment.NOT_IN_LIST
            )
        }

        if (profile.agencyType.isNullOrEmpty().not())
            viewBinding.layoutRegisteragencyBasics.agencyTypeSpinner.selectItemWithText(profile.agencyType!!)
        if (profile.agencyName.isNullOrEmpty().not())
            viewBinding.layoutRegisteragencyBasics.agencyNameEt.setText(profile.agencyName)

        if (profile.designation.isNullOrEmpty().not())
            viewBinding.layoutRegisteragencyBasics.designationEt.setText(profile.designation)
        viewBinding.layoutRegisterAgencyWork.facebookEt.setText(profile.facebookPage)
        viewBinding.layoutRegisterAgencyWork.webPageEt.setText(profile.websitePage)
        viewBinding.layoutRegisteragencyBasics.whatsAppEt.setText(profile.whatsAppMobileNumber)
        viewBinding.layoutRegisterAgencyWork.teamSizeEt.setText(profile.teamSize)
        if (profile.yearsInBusiness.isNullOrEmpty().not()) {
            viewBinding.layoutRegisterAgencyWork.yearInBusinessSpinner.selectItemWithText(profile.yearsInBusiness!!)
        }


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
        alertBuilder.setIcon(R.drawable.ic_camera)
        alertBuilder.setItems(optionsForDialog, DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        })
        alertBuilder.setNegativeButton("Cancel") { dialog12, _ -> dialog12.dismiss() }
        alertBuilder.show()
    }


    private val imageCallback =
        ImageCallback { requestedBy, result, error ->

            if (result != null) {
                viewBinding.layoutRegisterAgencyWork.businessCardPic.load(File(result.imagePath))
            }
        }

    fun populateSpinner() {
    }

    fun setIndividualListener() {

        viewBinding.layoutRegisterUser.individualSelectLayout.setOnClickListener {
            isIndividualLayoutSelected = true
            viewBinding.layoutRegisterIndividualBasic.mobileEt.setText(mobile)
            userType = INDIVIDUAL_USER_TYPE
            viewBinding.layoutRegisterUser.registerUserLayout.visibility = View.GONE
            viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.visibility = View.VISIBLE
        }
        viewBinding.layoutRegisterIndividualBasic.basicsInfoBtn.setOnClickListener {
            viewModel.validateIndvidualBasics(
                viewBinding.layoutRegisterIndividualBasic.firstNameEt.text.toString(),
                viewBinding.layoutRegisterIndividualBasic.emailEt.text.toString(),
                date
            )
        }
        viewBinding.layoutRegisterIndividualEducation.educationBtn.setOnClickListener {
            viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.visibility = View.GONE
            viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.visibility =
                View.VISIBLE
        }
        viewBinding.layoutRegisterIndividualBasic.basicsBackBtn.setOnClickListener {
            if (isFromProfile!!)
                onBackPressed()
            else {
                viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.visibility = View.GONE
                viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.visibility =
                    View.GONE
                viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.visibility =
                    View.GONE
                viewBinding.layoutRegisterUser.registerUserLayout.visibility = View.VISIBLE
            }
        }
        viewBinding.layoutRegisterIndividualEducation.educationBackBtn.setOnClickListener {
            viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.visibility = View.VISIBLE
        }

        viewBinding.layoutRegisterIndividualBasic.selectDateIV.setOnClickListener {
            datePickerDialog.show()
        }

        viewBinding.layoutRegisterIndividualBasic.maleBtn.setOnClickListener {
            viewBinding.layoutRegisterIndividualBasic.maleBtn.isEnabled = false
            viewBinding.layoutRegisterIndividualBasic.femaleBtn.isEnabled = true
            viewBinding.layoutRegisterIndividualBasic.maleBtnText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            viewBinding.layoutRegisterIndividualBasic.femaleBtnText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )
            viewBinding.layoutRegisterIndividualBasic.maleBtnLayout.setBackgroundResource(R.drawable.button_disable)
            viewBinding.layoutRegisterIndividualBasic.femaleBtnLayout.setBackgroundResource(R.drawable.button_normal)
            viewBinding.layoutRegisterIndividualBasic.firstNameSpinner.setSelection(0)
            gender = viewBinding.layoutRegisterIndividualBasic.maleBtnText.text.toString()
        }
        viewBinding.layoutRegisterIndividualBasic.femaleBtn.setOnClickListener {
            viewBinding.layoutRegisterIndividualBasic.maleBtn.isEnabled = true
            viewBinding.layoutRegisterIndividualBasic.femaleBtn.isEnabled = false
            viewBinding.layoutRegisterIndividualBasic.maleBtnText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )
            viewBinding.layoutRegisterIndividualBasic.femaleBtnText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            viewBinding.layoutRegisterIndividualBasic.maleBtnLayout.setBackgroundResource(R.drawable.button_normal)
            viewBinding.layoutRegisterIndividualBasic.femaleBtnLayout.setBackgroundResource(R.drawable.button_disable)
            viewBinding.layoutRegisterIndividualBasic.firstNameSpinner.setSelection(1)
            gender = viewBinding.layoutRegisterIndividualBasic.femaleBtnText.text.toString()
        }
        viewBinding.layoutRegisterIndividualBasic.firstNameSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position == 0) {
                        viewBinding.layoutRegisterIndividualBasic.maleBtn.isEnabled = false
                        viewBinding.layoutRegisterIndividualBasic.femaleBtn.isEnabled = true
                        viewBinding.layoutRegisterIndividualBasic.maleBtnLayout.setBackgroundResource(
                            R.drawable.button_disable
                        )
                        viewBinding.layoutRegisterIndividualBasic.femaleBtnLayout.setBackgroundResource(
                            R.drawable.button_normal
                        )
                        gender =
                            viewBinding.layoutRegisterIndividualBasic.maleBtnText.text.toString()
                    } else {
                        viewBinding.layoutRegisterIndividualBasic.maleBtn.isEnabled = true
                        viewBinding.layoutRegisterIndividualBasic.femaleBtn.isEnabled = false
                        viewBinding.layoutRegisterIndividualBasic.maleBtnLayout.setBackgroundResource(
                            R.drawable.button_normal
                        )
                        viewBinding.layoutRegisterIndividualBasic.femaleBtnLayout.setBackgroundResource(
                            R.drawable.button_disable
                        )
                        gender =
                            viewBinding.layoutRegisterIndividualBasic.femaleBtnText.text.toString()
                    }
                }

            }
        viewBinding.layoutRegisterIndividualEducation.languageSelectionLayout.setOnClickListener {
            LanguageDialogFragment.launchDialogFragment(
                supportFragmentManager,
                languageItemsWrapperIndividual,
                this
            )
        }
        viewBinding.layoutRegisterIndividualEducation.workCatSelectionLayout.setOnClickListener {
            WorkCategoryDialogFragment.launchDialogFragment(
                supportFragmentManager,
                workCategoryItemsWrapperIndividual,
                this
            )
        }
    }

    fun setAgencyListener() {
        viewBinding.layoutRegisterUser.agencySelectLayout.setOnClickListener {
            isIndividualLayoutSelected = false
            viewBinding.layoutRegisteragencyBasics.mobileEt.setText(mobile)
            userType = AGENCY_USER_TYPE
            viewBinding.layoutRegisterUser.registerUserLayout.visibility = View.GONE
            viewBinding.layoutRegisteragencyBasics.registerAgencyBasicsLayout.visibility =
                View.VISIBLE
        }
        viewBinding.layoutRegisteragencyBasics.agencyContiBtn.setOnClickListener {
            viewModel.validateAgencyBasics(
                viewBinding.layoutRegisteragencyBasics.agencyNameEt.text.toString(),
                viewBinding.layoutRegisteragencyBasics.firstNameEt.text.toString(),
                viewBinding.layoutRegisteragencyBasics.emailEt.text.toString()
            )
        }
        viewBinding.layoutRegisterAgencyWork.agencyWorkContiBtn.setOnClickListener {
            viewBinding.layoutRegisterAgencyWork.registerAgencyWorkLayout.visibility = View.GONE
            viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.visibility =
                View.VISIBLE
        }

        viewBinding.layoutRegisterAgencyWork.agencyWorkBackBtn.setOnClickListener {
            viewBinding.layoutRegisterAgencyWork.registerAgencyWorkLayout.visibility = View.GONE
            viewBinding.layoutRegisteragencyBasics.registerAgencyBasicsLayout.visibility =
                View.VISIBLE
        }
        viewBinding.layoutRegisteragencyBasics.agencyBasicBackBtn.setOnClickListener {
            if (isFromProfile)
                onBackPressed()
            else {
                viewBinding.layoutRegisteragencyBasics.registerAgencyBasicsLayout.visibility =
                    View.GONE
                viewBinding.layoutRegisterUser.registerUserLayout.visibility = View.VISIBLE
            }
        }
        viewBinding.layoutRegisterAgencyWork.languageSelectionLayout.setOnClickListener {
            LanguageDialogFragment.launchDialogFragment(
                supportFragmentManager,
                languageItemsWrapperAgency,
                this
            )
        }
        viewBinding.layoutRegisterAgencyWork.workCatSelectionLayout.setOnClickListener {
            WorkCategoryDialogFragment.launchDialogFragment(
                supportFragmentManager,
                workCategoryItemsWrapperAgency,
                this
            )
        }
        viewBinding.layoutRegisterAgencyWork.clickImage.setOnClickListener {
            showCameraOptionDialog()
        }
    }

    fun commomLayoutListener() {


        viewBinding.layoutRegisterIndividualWorkLocation.workLocationBackBtn.setOnClickListener {
            if (isIndividualLayoutSelected) {
                viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.visibility =
                    View.GONE
                viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.visibility =
                    View.VISIBLE
            } else {
                viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.visibility =
                    View.GONE
                viewBinding.layoutRegisterAgencyWork.registerAgencyWorkLayout.visibility =
                    View.VISIBLE
            }
        }
        viewBinding.layoutRegisterUser.registerMainBackBtn.setOnClickListener {
            //requireActivity().onBackPressedDispatcher.addCallback(this, backPressListener)
        }
        viewBinding.layoutRegisterIndividualWorkLocation.workLocationBtn.setOnClickListener {
            if (viewBinding.layoutRegisterIndividualWorkLocation.selectedCityEt.text.equals(
                    CityDialogFragment.CITY
                )
            )
                showInformationDialog("Select Valid District")
            else if (viewBinding.layoutRegisterIndividualWorkLocation.cityEt.isVisible
                && viewBinding.layoutRegisterIndividualWorkLocation.cityEt.text.isNullOrEmpty()
            )
                showInformationDialog("Select Valid District")
            else if (viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.text.equals(
                    PincodeDialogFragment.PINCODE
                )
            )
                showInformationDialog("Select Valid Pincode")
            else if (viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.isVisible
                && viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.text.isNullOrEmpty()
            )
                showInformationDialog("Select Valid Pincode")
            else if (viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.isVisible
                && viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.text.length < 6
            )
                showInformationDialog("Pincode must not be less than 6 digit")
            else
                registerUser()
        }

        viewBinding.layoutRegisterIndividualWorkLocation.stateSelectionLayout.setOnClickListener {
            StateDialogFragment.launchDialogFragment(
                supportFragmentManager,
                this
            )
        }
        viewBinding.layoutRegisterIndividualWorkLocation.citySelectionLayout.setOnClickListener {
            CityDialogFragment.launchDialogFragment(
                supportFragmentManager,
                state?.stateId,
                this
            )
        }
        viewBinding.layoutRegisterIndividualWorkLocation.pincodeSelectionLayout.setOnClickListener {
            //  if (city?.districtiId.isNullOrEmpty().not())
            PincodeDialogFragment.launchDialogFragment(
                supportFragmentManager,
                state?.stateId,
                this
            )
        }
    }


    fun registerUser() {
        val mobileNo = if (isIndividualLayoutSelected)
            viewBinding.layoutRegisterIndividualBasic.mobileEt.text.toString()
        else
            viewBinding.layoutRegisteragencyBasics.mobileEt.text.toString()
        val firstName = if (isIndividualLayoutSelected)
            viewBinding.layoutRegisterIndividualBasic.firstNameEt.text.toString()
        else
            viewBinding.layoutRegisteragencyBasics.firstNameEt.text.toString()

        val lastName = if (isIndividualLayoutSelected)
            viewBinding.layoutRegisterIndividualBasic.lastNameEt.text.toString()
        else
            viewBinding.layoutRegisteragencyBasics.lastNameEt.text.toString()

        val email = if (isIndividualLayoutSelected)
            viewBinding.layoutRegisterIndividualBasic.emailEt.text.toString()
        else
            viewBinding.layoutRegisteragencyBasics.emailEt.text.toString()

        val cityId =
            if (viewBinding.layoutRegisterIndividualWorkLocation.selectedCityEt.text.equals(
                    CityDialogFragment.CITY
                )
                || viewBinding.layoutRegisterIndividualWorkLocation.selectedCityEt.text.equals(
                    CityDialogFragment.NOT_IN_LIST
                )
            )
                null
            else {
                try {
                    Integer.parseInt(city?.cityId!!)
                } catch (e: Exception) {
                    null
                }

            }

        val districtId =
            if (viewBinding.layoutRegisterIndividualWorkLocation.selectedCityEt.text.equals(
                    CityDialogFragment.CITY
                )
                || viewBinding.layoutRegisterIndividualWorkLocation.selectedCityEt.text.equals(
                    CityDialogFragment.NOT_IN_LIST
                )
            )
                null
            else {
                try {
                    Integer.parseInt(city?.cityId!!)
                } catch (e: Exception) {
                    null
                }

            }

        val pincodeId =
            if (viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.text.equals(
                    PincodeDialogFragment.PINCODE
                )
                || viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.text.equals(
                    PincodeDialogFragment.NOT_IN_LIST
                )
            )
                null
            else {
                try {
                    Integer.parseInt(pinCode?.pincodeId!!)
                } catch (e: Exception) {
                    null
                }

            }
        val stateId =
            if (viewBinding.layoutRegisterIndividualWorkLocation.selectedStateEt.text.equals(
                    StateDialogFragment.STATE
                )
            )
                null
            else {
                try {
                    Integer.parseInt(state?.stateId!!)
                } catch (e: Exception) {
                    null
                }
            }

        val checkedWheeler =
            if (viewBinding.layoutRegisterIndividualEducation.yesRadioBtn.isChecked)
                true
            else
                false

        val state =
            if (viewBinding.layoutRegisterIndividualWorkLocation.selectedStateEt.text.equals(
                    StateDialogFragment.STATE
                )
            )
                null
            else
                state?.stateName
        val district =
            if (viewBinding.layoutRegisterIndividualWorkLocation.selectedCityEt.text.equals(
                    CityDialogFragment.CITY
                )
            )
                null
            else
                city?.cityName

        val pincode =
            if (viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.text.equals(
                    PincodeDialogFragment.PINCODE
                )
            )
                null
            else if (viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.text.equals(
                    PincodeDialogFragment.NOT_IN_LIST
                )
            )
                viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.text.toString()
            else
                pinCode?.pincode


        val education =
            if (viewBinding.layoutRegisterIndividualEducation.educationSpinner.count == 0 ||
                viewBinding.layoutRegisterIndividualEducation.educationSpinner.selectedItemPosition == 0
            )
                null
            else
                viewBinding.layoutRegisterIndividualEducation.educationSpinner.selectedItem.toString()
        val language =
            if (userType.equals(INDIVIDUAL_USER_TYPE) && viewBinding.layoutRegisterIndividualEducation.selectedLanguageEt.text.equals(
                    LanguageDialogFragment.LANGUAGE
                )
            )
                null
            else if (userType.equals(AGENCY_USER_TYPE) && viewBinding.layoutRegisterAgencyWork.selectedLanguageEt.text.equals(
                    LanguageDialogFragment.LANGUAGE
                )
            )
                null
            else if (userType.equals(INDIVIDUAL_USER_TYPE, true))
                viewBinding.layoutRegisterIndividualEducation.selectedLanguageEt.text.toString()
            else
                viewBinding.layoutRegisterAgencyWork.selectedLanguageEt.text.toString()
        val workCategory =
            if (userType.equals(INDIVIDUAL_USER_TYPE) && viewBinding.layoutRegisterIndividualEducation.selectedWorkCatEt.text.equals(
                    WorkCategoryDialogFragment.WORK_CATEGORY
                )
            )
                null
            else if (userType.equals(AGENCY_USER_TYPE) && viewBinding.layoutRegisterAgencyWork.selectedWorkCatEt.text.equals(
                    WorkCategoryDialogFragment.WORK_CATEGORY
                )
            )
                null
            else if (userType.equals(INDIVIDUAL_USER_TYPE, true))
                viewBinding.layoutRegisterIndividualEducation.selectedWorkCatEt.text.toString()
            else
                viewBinding.layoutRegisterAgencyWork.selectedWorkCatEt.text.toString()
        val registerRequest = RegisterRequest(
            firstName = userName!!,
            lastName = lastName,
            title = viewBinding.layoutRegisterIndividualBasic.firstNameSpinner.selectedItem.toString(),
            userType = userType!!,
            agencyName = viewBinding.layoutRegisteragencyBasics.agencyNameEt.text.toString(),
            agencyType = if (viewBinding.layoutRegisteragencyBasics.agencyTypeSpinner.count == 0
                || viewBinding.layoutRegisteragencyBasics.agencyTypeSpinner.selectedItemPosition == 0
            )
                null
            else
                viewBinding.layoutRegisteragencyBasics.agencyTypeSpinner.selectedItem.toString(),
            mobile = mobile,
            whatsAppMobileNumber = viewBinding.layoutRegisteragencyBasics.whatsAppEt.text.toString(),
            email = email,
            dob = dateOfBirth,
            gender = gender,
            district = district,
            cityId = cityId,
            password = "pass",
            pinCode = pincode,
            pinCodeId = pincodeId,
            workCategory = workCategory,
            experience = if (viewBinding.layoutRegisterIndividualEducation.experienceSpinner.count != 0 &&
                viewBinding.layoutRegisterIndividualEducation.experienceSpinner.selectedItemPosition != 0
            )
                viewBinding.layoutRegisterIndividualEducation.experienceSpinner.selectedItem.toString()
            else
                null,
            designation = viewBinding.layoutRegisteragencyBasics.designationEt.text.toString(),
            teamSize = viewBinding.layoutRegisterAgencyWork.teamSizeEt.text.toString(),

            languages = language,
            education = education,
            websitePage = viewBinding.layoutRegisterAgencyWork.webPageEt.text.toString(),
            facebookPage = viewBinding.layoutRegisterAgencyWork.facebookEt.text.toString(),
            twoWheeler = checkedWheeler,
            yearsInBusiness = if (viewBinding.layoutRegisterAgencyWork.yearInBusinessSpinner.count != 0
                && viewBinding.layoutRegisterAgencyWork.yearInBusinessSpinner.selectedItemPosition != 0
            )
                viewBinding.layoutRegisterAgencyWork.yearInBusinessSpinner.selectedItem.toString()
            else
                null,
            state = state,
            stateId = stateId,
            districtId = districtId,
            accountNo = profile?.accountNo,
            bankName = profile?.bankName,
            nameOnBank = profile?.nameOnBank,
            mobileOnBank = profile?.mobileOnBank,
            ifsc = profile?.ifsc,
            gps = "${latitude},${longitude}",
            gpsAddress = address,
            profilePic = profile?.profilePic,
            profilePicUpdated = profile?.profilePicUpdated,
            adhar = profile?.aadharNo
        )
        viewModel.register(registerRequest)
    }


    override fun onBackPressed() {
        if (viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.isVisible && !isFromProfile) {
            viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterUser.registerUserLayout.visibility = View.VISIBLE
        } else if (viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.isVisible) {
            viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.visibility =
                View.VISIBLE
        } else if (viewBinding.layoutRegisteragencyBasics.registerAgencyBasicsLayout.isVisible && !isFromProfile) {
            viewBinding.layoutRegisteragencyBasics.registerAgencyBasicsLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterUser.registerUserLayout.visibility = View.VISIBLE
        } else if (viewBinding.layoutRegisterAgencyWork.registerAgencyWorkLayout.isVisible) {
            viewBinding.layoutRegisterAgencyWork.registerAgencyWorkLayout.visibility =
                View.GONE
            viewBinding.layoutRegisteragencyBasics.registerAgencyBasicsLayout.visibility =
                View.VISIBLE
        } else if (isIndividualLayoutSelected && viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.isVisible) {
            viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.visibility =
                View.VISIBLE
        } else if (!isIndividualLayoutSelected && viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.isVisible) {
            viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterAgencyWork.registerAgencyWorkLayout.visibility =
                View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }

    override fun onCityClick(city: City) {
        this.city = city
        viewBinding.layoutRegisterIndividualWorkLocation.selectedCityEt.text = city.cityName
        if (viewBinding.layoutRegisterIndividualWorkLocation.selectedCityEt.text.equals(
                CityDialogFragment.NOT_IN_LIST
            )
        ) {
            viewBinding.layoutRegisterIndividualWorkLocation.cityEt.setVisibilityVisible()
        } else
            viewBinding.layoutRegisterIndividualWorkLocation.cityEt.setVisibilityGone()
        viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.setVisibilityGone()
        viewBinding.layoutRegisterIndividualWorkLocation.cityEt.setText("")
        viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.setText("")
        viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.setText(
            PincodeDialogFragment.PINCODE
        )
    }

    override fun onPincodeClick(pinCode: Pincode) {
        this.pinCode = pinCode
        viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.text = pinCode.pincode
        if (viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.text.equals(
                PincodeDialogFragment.NOT_IN_LIST
            )
        )
            viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.setVisibilityVisible()
        else {
            viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.setVisibilityGone()
            viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.setText("")
        }
    }

    override fun onStateClick(state: State) {
        viewBinding.layoutRegisterIndividualWorkLocation.selectedStateEt.text = state.stateName
        stateId = state.stateId
        this.state = state
        city = null
        viewBinding.layoutRegisterIndividualWorkLocation.cityEt.setVisibilityGone()
        viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.setVisibilityGone()
        viewBinding.layoutRegisterIndividualWorkLocation.cityEt.setText("")
        viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.setText("")
        viewBinding.layoutRegisterIndividualWorkLocation.selectedCityEt.setText(CityDialogFragment.CITY)
        viewBinding.layoutRegisterIndividualWorkLocation.selectedPincodeEt.setText(
            PincodeDialogFragment.PINCODE
        )
    }

    override fun onLanguageClick(language: StringBuilder) {
        if (userType.equals(INDIVIDUAL_USER_TYPE) && language.isEmpty()) {
            viewBinding.layoutRegisterIndividualEducation.selectedLanguageEt.text =
                LanguageDialogFragment.LANGUAGE
            languageItemsWrapperIndividual = language.toString()
        } else if (userType.equals(AGENCY_USER_TYPE) && language.isEmpty()) {
            languageItemsWrapperAgency = language.toString()
            viewBinding.layoutRegisterAgencyWork.selectedLanguageEt.text =
                LanguageDialogFragment.LANGUAGE
        } else if (userType.equals(INDIVIDUAL_USER_TYPE) && language.isEmpty().not()) {
            languageItemsWrapperIndividual = language.toString()
            viewBinding.layoutRegisterIndividualEducation.selectedLanguageEt.text = language
        } else if (userType.equals(AGENCY_USER_TYPE) && language.isEmpty().not()) {
            languageItemsWrapperAgency = language.toString()
            viewBinding.layoutRegisterAgencyWork.selectedLanguageEt.text = language
        }
    }

    override fun onWorkCatClick(workCat: StringBuilder) {
        if (userType.equals(INDIVIDUAL_USER_TYPE) && workCat.isEmpty()) {
            viewBinding.layoutRegisterIndividualEducation.selectedWorkCatEt.text =
                WorkCategoryDialogFragment.WORK_CATEGORY
            workCategoryItemsWrapperIndividual = workCat.toString()

        } else if (userType.equals(AGENCY_USER_TYPE) && workCat.isEmpty()) {
            workCategoryItemsWrapperAgency = workCat.toString()
            viewBinding.layoutRegisterAgencyWork.selectedWorkCatEt.text =
                WorkCategoryDialogFragment.WORK_CATEGORY
        } else if (userType.equals(INDIVIDUAL_USER_TYPE) && workCat.isEmpty().not()) {
            workCategoryItemsWrapperIndividual = workCat.toString()
            viewBinding.layoutRegisterIndividualEducation.selectedWorkCatEt.text = workCat
        } else if (userType.equals(AGENCY_USER_TYPE) && workCat.isEmpty().not()) {
            workCategoryItemsWrapperAgency = workCat.toString()
            viewBinding.layoutRegisterAgencyWork.selectedWorkCatEt.text = workCat
        }
    }

    override fun onResume() {
        super.onResume()
        locationHelper.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()

        if (locationHelper.isRequestingForLocation)
            locationHelper.stopLocationUpdates()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_UPGRADE_GPS_SETTINGS -> {

                if (resultCode == Activity.RESULT_OK) {
                    locationHelper.startLocationUpdates()
                } else if (resultCode == Activity.RESULT_CANCELED)
                    showRedirectToGpsPageDialog()

            }
            REQUEST_UPDATE_GPS_SETTINGS_MANUALLY -> {
                locationHelper.startLocationUpdates()
            }
            CameraIntegrator.REQUEST_IMAGE_CAPTURE -> {

                if (resultCode == Activity.RESULT_OK)
                    cameraIntegrator.parseResults(requestCode, resultCode, data, imageCallback)
            }
        }
    }

    private fun showRedirectToGpsPageDialog() {

        showNonCancellableMessageDialog(
            this,
            "Important!!, Gps Not Turned On You will be redirected to settings page , Please turn on GPS and set mode to High Accuracy",
            object : QuickAlertDialog.OnClickListener {
                override fun okButtonClicked() {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(
                        intent,
                        REQUEST_UPDATE_GPS_SETTINGS_MANUALLY
                    )
                }

                override fun onCancelButtonClick() {

                }
            })
    }

    private fun checkForGpsStatus() {
        locationHelper.checkForGpsSettings(object : GpsSettingsCheckCallback {

            override fun requiredGpsSettingAreUnAvailable(status: ResolvableApiException?) {
                status?.startResolutionForResult(
                    this@RegisterActivity,
                    REQUEST_UPGRADE_GPS_SETTINGS
                )
            }

            override fun requiredGpsSettingAreAvailable() {
                locationHelper.startLocationUpdates()
            }

            override fun gpsSettingsNotAvailable() {
                // showRedirectToGpsPageDialog()
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // cameraIntegrator.initiateCapture()
                }
            }
            PERMISSION_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkForGpsStatus()
                }
            }

        }
    }

    private fun locationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_REQUEST_LOCATION
        )
    }
}