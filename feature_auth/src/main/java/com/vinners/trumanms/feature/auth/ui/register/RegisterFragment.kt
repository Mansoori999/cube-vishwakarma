package com.vinners.trumanms.feature.auth.ui.register

import android.Manifest
import android.app.AlertDialog.THEME_HOLO_LIGHT
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.bumptech.glide.Glide
import com.himanshu.cameraintegrator.ImageCallback
import com.himanshu.cameraintegrator.ImagesSizes
import com.himanshu.cameraintegrator.integrator.CameraIntegrator
import com.himanshu.cameraintegrator.integrator.GalleryIntegrator
import com.himanshu.cameraintegrator.storage.StorageMode
import com.vinners.trumanms.core.AppConstants
import com.vinners.trumanms.core.DateTimeHelper
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.extensions.navigateSafely
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.auth.RegisterRequest
import com.vinners.trumanms.data.models.stateAndCity.City
import com.vinners.trumanms.data.models.stateAndCity.Pincode
import com.vinners.trumanms.data.models.stateAndCity.State
import com.vinners.trumanms.data.models.stateAndCity.WorkCategory
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.FragmentRegisterBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import com.vinners.trumanms.feature.auth.ui.dashboard.DashboardFragment
import dagger.internal.DoubleCheck.lazy
import io.apptik.widget.multiselectspinner.BaseMultiSelectSpinner
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject

class RegisterFragment : AppCompatActivity() {

  /*  companion object {
        private const val PERMISSION_REQUEST_STORAGE = 233
        const val REQUEST_CODE = 101
        const val INTENT_EXTRA_MOBILE = "mobile"
    }

    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory

    private var dateOfBirth: String? = null
    val workCategoryItemsWrapper = StringBuilder()
    val languageItemsWrapper = StringBuilder()

    private var isIndividualLayoutSelected: Boolean = false
    private var gender: String? = null
    private var mobile: String? = null
    private var userType: String? = null
    private var userName: String? = null
    private var date: Date? = null

    private val cameraIntegrator: CameraIntegrator by kotlin.lazy {
        CameraIntegrator(this@RegisterFragment)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setPublicDirectoryName(AppConstants.PRIVATE_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }

    private val galleryIntegrator: GalleryIntegrator by kotlin.lazy {
        GalleryIntegrator(this@RegisterFragment)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setPublicDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }


    private val datePickerDialog: DatePickerDialog
        get() {
            val calendar = Calendar.getInstance()
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val year = calendar.get(Calendar.YEAR) - 18
            // date = calendar.time
            val datePicker = DatePickerDialog(
                requireContext(),
                android.app.AlertDialog.THEME_HOLO_LIGHT,
                this,
                year,
                month,
                dayOfMonth
            )
            return datePicker
        }

    override val viewModel: RegisterViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerAuthComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    private val backPressListener = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.isVisible) {
                viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.visibility =
                    View.GONE
                viewBinding.layoutRegisterUser.registerUserLayout.visibility = View.VISIBLE
            } else if (viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.isVisible) {
                viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.visibility =
                    View.GONE
                viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.visibility =
                    View.VISIBLE
            } else if (viewBinding.layoutRegisteragencyBasics.registerAgencyBasicsLayout.isVisible) {
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
                findNavController().navigateSafely(R.id.open_login_from_register_action)
            }
        }

    }

    override fun onInitDataBinding() {
        mobile = arguments?.getString(INTENT_EXTRA_MOBILE)
        setIndividualListener()
        setAgencyListener()
        commomLayoutListener()
        cityStateSpinnerListener()
        populateSpinner()
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressListener)
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
        val alertBuilder = AlertDialog.Builder(requireContext())
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
                viewBinding.layoutRegisterAgencyWork.businessCardPic.load(File(result.imagePath))
            }
        }

    override fun onInitViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    val stateList = it.content.sortedBy {
                        it.stateName
                    }
                    populateStates(stateList)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.city.observe(viewLifecycleOwner, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    val cityList = it.content.sortedBy {
                        it.cityName
                    }
                    populateCities(cityList)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.pincode.observe(viewLifecycleOwner, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    populatePincode(it.content)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.workCategory.observe(viewLifecycleOwner, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    populateWorkSpinner(it.content)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.registerState.observe(viewLifecycleOwner, Observer {
            when (it) {
                Lse.Loading -> {

                }
                is Lse.Success -> {
                    Toast.makeText(requireContext(), "Registerd Successfully", Toast.LENGTH_SHORT)
                        .show()
                    val bundle = bundleOf(
                        DashboardFragment.INTENT_USER_NAME to userName
                    )
                    findNavController().navigateSafely(
                        R.id.open_dashboard_from_register_action,
                        args = bundle
                    )
                }
                is Lse.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        viewModel.individualBasicValidate.observe(viewLifecycleOwner, Observer {
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
        viewModel.agencyBasicsValidate.observe(viewLifecycleOwner, Observer {
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
        viewModel.getState()
        viewModel.getWorkCategory()
    }

    fun populateSpinner() {

        val languageList = mutableListOf<String?>(
            "Hindi",
            "English",
            "Punjabi",
            "Tamil"
        )
        val languageAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_multiple_choice,
            languageList
        )

        viewBinding.layoutRegisterIndividualEducation.languageSpinner.setListAdapter(languageAdapter)
            .setAllCheckedText<BaseMultiSelectSpinner>("All Selected")
            .setTitle<BaseMultiSelectSpinner>("Language")
            .setAllUncheckedText<BaseMultiSelectSpinner>("Language")
            .setListener<BaseMultiSelectSpinner> {
                for (i in it!!.indices) {
                    if (it[i]) {
                        languageItemsWrapper.append(languageList.get(i))
                        if (i <= it?.size.minus(1))
                            languageItemsWrapper.append(",")
                    }
                }
            }
        viewBinding.layoutRegisterAgencyWork.agencyLanguageSpinner.setListAdapter(languageAdapter)
            .setAllCheckedText<BaseMultiSelectSpinner>("All Selected")
            .setTitle<BaseMultiSelectSpinner>("Language")
            .setAllUncheckedText<BaseMultiSelectSpinner>("Language")
            .setListener<BaseMultiSelectSpinner> {
                for (i in it!!.indices) {
                    if (it[i]) {
                        languageItemsWrapper.append(languageList.get(i))
                        if (i <= it?.size.minus(1))
                            languageItemsWrapper.append(",")
                    }
                }
            }

    }

    fun setIndividualListener() {
        viewBinding.layoutRegisterUser.individualSelectLayout.setOnClickListener {
            isIndividualLayoutSelected = true
            viewBinding.layoutRegisterIndividualBasic.mobileEt.setText(mobile)
            userType = "Individual"
            viewBinding.layoutRegisterUser.registerUserLayout.visibility = View.GONE
            viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.visibility =
                View.GONE
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
            viewBinding.layoutRegisterIndividualBasic.fragmentBasicLayout.visibility = View.GONE
            viewBinding.layoutRegisterIndividualWorkLocation.registerLocationWorkLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterIndividualEducation.registerEducationLayout.visibility =
                View.GONE
            viewBinding.layoutRegisterUser.registerUserLayout.visibility = View.VISIBLE
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
            viewBinding.layoutRegisterIndividualBasic.maleBtnLayout.setBackgroundResource(R.drawable.button_disable)
            viewBinding.layoutRegisterIndividualBasic.femaleBtnLayout.setBackgroundResource(R.drawable.button_normal)
            viewBinding.layoutRegisterIndividualBasic.firstNameSpinner.setSelection(0)
            gender = viewBinding.layoutRegisterIndividualBasic.maleBtnText.text.toString()
        }
        viewBinding.layoutRegisterIndividualBasic.femaleBtn.setOnClickListener {
            viewBinding.layoutRegisterIndividualBasic.maleBtn.isEnabled = true
            viewBinding.layoutRegisterIndividualBasic.femaleBtn.isEnabled = false
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
    }

    fun setAgencyListener() {
        viewBinding.layoutRegisterUser.agencySelectLayout.setOnClickListener {
            isIndividualLayoutSelected = false
            viewBinding.layoutRegisteragencyBasics.mobileEt.setText(mobile)
            userType = "Agency"
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
            viewBinding.layoutRegisteragencyBasics.registerAgencyBasicsLayout.visibility = View.GONE
            viewBinding.layoutRegisterUser.registerUserLayout.visibility = View.VISIBLE
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
            requireActivity().onBackPressedDispatcher.addCallback(this, backPressListener)
        }
        viewBinding.layoutRegisterIndividualWorkLocation.workLocationBtn.setOnClickListener {
            registerUser()
        }
    }

    fun populateStates(stateList: List<State>) {
        val mutStateList = mutableListOf<State>()
        mutStateList.add(0, State("State", null))
        stateList.forEach {
            mutStateList.add(it)
        }
        val stateAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_items,
            mutStateList
        )
        viewBinding.layoutRegisterIndividualWorkLocation.stateSpinner.adapter = stateAdapter
    }

    fun populateCities(cityList: List<City>) {
        val mutCityList = mutableListOf<City>()
        mutCityList.add(0, City("City", null, null))
        mutCityList.add(1, City("Not in list", null, null))
        cityList.forEach {
            mutCityList.add(it)
        }
        val stateAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_items,
            mutCityList
        )
        viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.adapter = stateAdapter
    }

    fun populatePincode(pincodeList: List<Pincode>) {
        val mutPincodeList = mutableListOf<Pincode>()
        mutPincodeList.add(0, Pincode(null, "Pincode"))
        mutPincodeList.add(1, Pincode(null, "Not in list"))
        pincodeList.forEach {
            mutPincodeList.add(it)
        }
        val stateAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_items,
            mutPincodeList
        )
        viewBinding.layoutRegisterIndividualWorkLocation.pincodeSpinner.adapter = stateAdapter
    }

    fun cityStateSpinnerListener() {
        viewBinding.layoutRegisterIndividualWorkLocation.stateSpinner.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem =
                        viewBinding.layoutRegisterIndividualWorkLocation.stateSpinner.getItemAtPosition(
                            position
                        ) as State
                    if (viewBinding.layoutRegisterIndividualWorkLocation.stateSpinner.count != 0 && position != 0)
                        viewModel.getCity(selectedItem.stateId)
                }

            })
        viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem =
                        viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.getItemAtPosition(
                            position
                        ) as City
                    if (viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.count != 0 && position > 1) {
                        viewBinding.layoutRegisterIndividualWorkLocation.cityEt.visibility =
                            View.GONE
                        viewModel.getPincode(selectedItem.districtiId)
                    } else if (position == 1)
                        viewBinding.layoutRegisterIndividualWorkLocation.cityEt.visibility =
                            View.VISIBLE
                    else
                        viewBinding.layoutRegisterIndividualWorkLocation.cityEt.visibility =
                            View.GONE
                }

            })
        viewBinding.layoutRegisterIndividualWorkLocation.pincodeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position == 1)
                        viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.visibility =
                            View.VISIBLE
                    else
                        viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.visibility =
                            View.GONE
                }

            }
    }

    fun populateWorkSpinner(workList: List<WorkCategory>) {
        val mutWorkList = mutableListOf<String?>()
        workList.forEach {
            mutWorkList.add(it.work)
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_multiple_choice,
            mutWorkList
        )
        viewBinding.layoutRegisterIndividualEducation.individualWorkSpinner.setListAdapter(adapter)
            .setTitle<BaseMultiSelectSpinner>("Work Category")
            .setAllUncheckedText<BaseMultiSelectSpinner>("Work Category")
            .setAllCheckedText<BaseMultiSelectSpinner>("All Selected")
            .setListener<BaseMultiSelectSpinner> {
                for (i in it!!.indices) {
                    if (it[i]) {
                        workCategoryItemsWrapper.append(mutWorkList.get(i))
                        if (i <= it?.size.minus(1))
                            workCategoryItemsWrapper.append(",")
                    }
                }
            }
        viewBinding.layoutRegisterAgencyWork.agencyWorkSpinner.setListAdapter(adapter)
            .setTitle<BaseMultiSelectSpinner>("Work Category")
            .setAllUncheckedText<BaseMultiSelectSpinner>("Work Category")
            .setAllCheckedText<BaseMultiSelectSpinner>("All Selected")
            .setListener<BaseMultiSelectSpinner> {
                for (i in it!!.indices) {
                    if (it[i]) {
                        workCategoryItemsWrapper.append(mutWorkList.get(i))
                        if (i <= it?.size.minus(1))
                            workCategoryItemsWrapper.append(",")
                    }
                }
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

        val cityId = if (viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.count == 0
            || viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.selectedItemPosition == 0
            || viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.selectedItemPosition == 1
        )
            null
        else
            Integer.parseInt((viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.selectedItem as City).cityId!!)

        val pincodeId =
            if (viewBinding.layoutRegisterIndividualWorkLocation.pincodeSpinner.count == 0
                || viewBinding.layoutRegisterIndividualWorkLocation.pincodeSpinner.selectedItemPosition == 0
                || viewBinding.layoutRegisterIndividualWorkLocation.pincodeSpinner.selectedItemPosition == 1
            )
                null
            else
                Integer.parseInt((viewBinding.layoutRegisterIndividualWorkLocation.pincodeSpinner.selectedItem as Pincode).pincodeId!!)

        val checkedWheeler =
            if (viewBinding.layoutRegisterIndividualEducation.yesRadioBtn.isChecked)
                true
            else
                false

        val city = if (viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.count == 0
            || viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.selectedItemPosition == 0
        )
            null
        else if (viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.selectedItemPosition == 1)
            viewBinding.layoutRegisterIndividualWorkLocation.cityEt.text.toString()
        else
            (viewBinding.layoutRegisterIndividualWorkLocation.citySpinner.selectedItem as City).cityName

        val pincode = if (viewBinding.layoutRegisterIndividualWorkLocation.pincodeSpinner.count == 0
            || viewBinding.layoutRegisterIndividualWorkLocation.pincodeSpinner.selectedItemPosition == 0
        )
            null
        else if (viewBinding.layoutRegisterIndividualWorkLocation.pincodeSpinner.selectedItemPosition == 1)
            viewBinding.layoutRegisterIndividualWorkLocation.pncodeEt.text.toString()
        else
            (viewBinding.layoutRegisterIndividualWorkLocation.pincodeSpinner.selectedItem as Pincode).pincode

        val registerRequest = RegisterRequest(
            firstName = userName!!,
            lastName = lastName,
            userType = userType!!,
            agencyType = if (viewBinding.layoutRegisteragencyBasics.agencyTypeSpinner.count == 0
                || viewBinding.layoutRegisteragencyBasics.agencyTypeSpinner.selectedItemPosition == 0
            )
                null
            else
                viewBinding.layoutRegisteragencyBasics.agencyTypeSpinner.selectedItem.toString(),
            mobile = mobileNo,
            whatsAppMobileNumber = viewBinding.layoutRegisteragencyBasics.whatsAppEt.text.toString(),
            email = email,
            dob = dateOfBirth,
            gender = gender,
            city = city,
            cityId = cityId,
            password = "pass",
            pinCode = pincode,
            pinCodeId = pincodeId,
            workCategory = workCategoryItemsWrapper.toString(),
            experience = if (viewBinding.layoutRegisterIndividualEducation.experienceSpinner.count != 0 ||
                viewBinding.layoutRegisterIndividualEducation.experienceSpinner.selectedItemPosition != 0
            )
                viewBinding.layoutRegisterIndividualEducation.experienceSpinner.selectedItem.toString()
            else
                null,
            designation = if (viewBinding.layoutRegisteragencyBasics.designationSpinner.count != 0 ||
                viewBinding.layoutRegisteragencyBasics.designationSpinner.selectedItemPosition != 0
            )
                viewBinding.layoutRegisteragencyBasics.designationSpinner.selectedItem.toString()
            else
                null,
            teamSize = viewBinding.layoutRegisterAgencyWork.teamSizeEt.text.toString(),
            languages = languageItemsWrapper.toString(),
            education = if (viewBinding.layoutRegisterIndividualEducation.educationSpinner.count != 0 ||
                viewBinding.layoutRegisterIndividualEducation.educationSpinner.selectedItemPosition != 0
            )
                viewBinding.layoutRegisterIndividualEducation.educationSpinner.selectedItem.toString()
            else
                null,
            websitePage = viewBinding.layoutRegisterAgencyWork.webPageEt.text.toString(),
            facebookPage = viewBinding.layoutRegisterAgencyWork.facebookEt.text.toString(),
            twoWheeler = checkedWheeler,
            gps = "37.26839,78.838772",
            gpsAddress = "faridabad"
        )
        viewModel.register(registerRequest)
    }*/
}
