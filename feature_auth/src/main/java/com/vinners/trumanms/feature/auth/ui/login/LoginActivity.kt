package com.vinners.trumanms.feature.auth.ui.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.base.CoreApplication
import com.vinners.trumanms.core.location.GpsSettingsCheckCallback
import com.vinners.trumanms.core.location.LocationHelper
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.ActivityLoginBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import com.vinners.trumanms.feature.auth.ui.otp.OtpActivity
import com.vinners.trumanms.feature.auth.ui.register.RegisterActivity
import java.util.*
import javax.inject.Inject

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
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
    private var highlightedEditTextIndex = 0
    private var previousLength = 0
    private var mobileNo: String? = null
    private var isVerified: Boolean = false
    private var referralCode: String? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var address: String? = null

    override val viewModel: LoginViewModel by viewModels { viewModelFactory }

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
                    this@LoginActivity,
                    Locale.getDefault()
                ).getFromLocation(latitude?.toDouble()!!, longitude?.toDouble()!!, 1)
                address = addressList[0].locality
            } catch (e: Exception) {

            }
        }
    }

    override fun onInitDependencyInjection() {
        DaggerAuthComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this@LoginActivity)
    }

    override fun onInitDataBinding() {
        (this.application as CoreApplication).reInitCoreDependencies()
        // referralCode = intent.getStringExtra(OtpActivity.INTENT_REFERAL_CODE)
        getReferalCode()
        viewBinding.userPasswordInputLayout.visibility = View.GONE
        viewBinding.et1.setOnClickListener {
            showSoftKeyboard(viewBinding.YoEt)
        }
        viewBinding.et2.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et3.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et4.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et5.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et6.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et7.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et8.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et9.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et10.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }

        startListeningForOtpTypeEvents()
        /* viewBinding.loginBtn.setOnClickListener {
             if (viewBinding.userPasswordInputLayout.isVisible) {
                 viewModel.login(
                     mobileNo,
                     viewBinding.passwordEt.text.toString()
                 )
             } else {
                 viewModel.isUserRegister(mobileNo)
             }
         }*/
        viewBinding.loginOtpBtn.setOnClickListener {
            viewModel.isUserRegister(mobileNo)
            // viewModel.loginWithOtp(mobileNo)
        }
        viewBinding.forgotPassword.setOnClickListener {
            viewModel.forgetPassword(mobileNo)
        }
        viewBinding.policy.setOnClickListener {
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            intent.putExtra(
                PrivacyPolicyActivity.INTENT_EXTRA_URL,
                "https://truman.ooo/privacypolicy"
            )
            startActivity(intent)
        }
        viewBinding.terms.setOnClickListener {
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            intent.putExtra(
                PrivacyPolicyActivity.INTENT_EXTRA_URL,
                "https://truman.ooo/useragreement"
            )
            startActivity(intent)
        }
        if (locationPermissions())
            checkForGpsStatus()
        else
            requestLocationPermissions()
    }

    override fun onInitViewModel() {
        viewModel.registerWithOtpState.observe(this@LoginActivity, Observer {
            /* when (it) {
                 Lce.Loading -> {
                 }
                 is Lce.Content -> {
                     when (it.content) {
                         is UserNameCheckStates.OtpNotVerified -> {
                             val otpToken =
                                 (it.content as UserNameCheckStates.OtpNotVerified).otpToken
                             openConfirmOtpScreen(otpToken)
                         }
                         UserNameCheckStates.RegistrationFormNotCompleted -> openRegistrationForm()
                        UserNameCheckStates.ShowPasswordField -> showPasswordView()
                     }
                 }
                 is Lce.Error -> showInformationDialog(it.error)
             }*/
            when (it) {
                Lce.Loading -> {
                    viewBinding.loginOtpBtn.showProgress {
                        buttonText = getString(R.string.loading)
                        progressColor = Color.WHITE
                    }
                    viewBinding.loginOtpBtn.isEnabled = false
                }
                is Lce.Content -> {
                    viewBinding.loginOtpBtn.hideProgress(
                        R.string.login_with_otp
                    )
                    viewBinding.loginOtpBtn.isEnabled = true
                    isVerified = it.content.isOtpVerified
                    if (it.content.isOtpVerified)
                        viewModel.loginWithOtp(mobileNo)
                    else
                        openConfirmOtpScreen(it.content.otpToken)
                }
                is Lce.Error -> {
                    viewBinding.loginOtpBtn.isEnabled = true
                    viewBinding.loginOtpBtn.hideProgress(
                        R.string.login_with_otp
                    )
                    showInformationDialog(it.error)
                }
            }
        })

        viewModel.loginFormState.observe(
            this@LoginActivity,
            Observer {
                showInformationDialog(it)
            })

        viewModel.loginStateChange.observe(
            this@LoginActivity,
            Observer {
                val loginResult = it ?: return@Observer
                when (loginResult) {
                    Lce.Loading -> {

                    }
                    is Lce.Content -> {
                        if (loginResult.content.firstName == null) {
                            openRegistrationForm()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "login successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            /*  val bundle =
                                  bundleOf(DashboardFragment.INTENT_USER_NAME to loginResult.content.firstName)
                              findNavController().navigateSafely(
                                  R.id.open_dashboard_from_login_action,
                                  args = bundle
                              )*/
                        }
                    }
                    is Lce.Error -> {
                        showInformationDialog(loginResult.error)
                    }
                }
            })
        viewModel.loginWithOtpState.observe(this@LoginActivity, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {

                    openConfirmOtpScreen(it.content.otpToken)
                }
                is Lce.Error -> {
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.forgetPasswordState.observe(this@LoginActivity, Observer {
            when (it) {
                Lse.Loading -> {

                }
                is Lse.Success -> {
                    Toast.makeText(this, "Password Sent", Toast.LENGTH_SHORT).show()
                }
                is Lse.Error -> {

                }
            }
        })
    }

    private fun startListeningForOtpTypeEvents() {
        viewBinding.YoEt.addTextChangedListener(object : TextWatcher {
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
                    val editText = viewBinding.editTextHolder.getChildAt(0) as EditText
                    editText.text = null
                } else {

                    val editText =
                        viewBinding.editTextHolder.getChildAt(highlightedEditTextIndex) as EditText
                    val text = editText.text.toString()

                    if (highlightedEditTextIndex == 9 && text.isNotEmpty()) {
                        editText.text = null
                        editText.background =
                            ContextCompat.getDrawable(
                                this@LoginActivity,
                                R.drawable.login_mobile_edit_text_background
                            )
                    } else {

                        editText.text = null
                        editText.background =
                            ContextCompat.getDrawable(
                                this@LoginActivity,
                                R.drawable.login_mobile_edit_text_background
                            )

                        val editTextNext =
                            viewBinding.editTextHolder.getChildAt(highlightedEditTextIndex - 1) as EditText
                        editTextNext.background =
                            ContextCompat.getDrawable(
                                this@LoginActivity,
                                R.drawable.login_mobile_edit_text_background
                            )

                        if (text.isEmpty())
                            editTextNext.text = null

                        highlightedEditTextIndex--
                    }
                }
                mobileNo = s.toString()
            }

            private fun insertDigitInCurrentAndHighlightNextOne(
                s: CharSequence,
                currentLength: Int
            ) {
                val editText = viewBinding.editTextHolder.getChildAt(previousLength) as EditText
                editText.setText(s.subSequence(previousLength, currentLength).toString())

                if (currentLength <= 10) {
                    editText.setBackgroundResource(R.color.white)
                    val editTextNext =
                        viewBinding.editTextHolder.getChildAt(currentLength) as EditText
                    editTextNext.background =
                        ContextCompat.getDrawable(
                            this@LoginActivity,
                            R.drawable.login_mobile_edit_text_background
                        )
                    if (currentLength < 10)
                        highlightedEditTextIndex = currentLength
                }
                mobileNo = s.toString()
            }
        })
    }

    private fun openConfirmOtpScreen(otpToken: String?) {
        Intent(this@LoginActivity, OtpActivity::class.java).apply {
            putExtra(OtpActivity.INTENT_EXTRA_OTP_TOKEN, otpToken)
            putExtra(OtpActivity.INTENT_EXTRA_MOBILE, mobileNo)
            putExtra(OtpActivity.INTENT_REFERAL_CODE, referralCode)
            putExtra(OtpActivity.INTENT_EXTRA_IS_REGISTER, isVerified)
            putExtra(OtpActivity.INTENT_GPS, "${latitude},${longitude}")
            putExtra(OtpActivity.INTENT_ADDRESS, address)
        }.also {
            startActivity(it)
        }
    }

    private fun openRegistrationForm() {
        // viewBinding.userPasswordInputLayout.setVisibilityGone()
        /*
        val bundle =
            bundleOf(RegisterFragment.INTENT_EXTRA_MOBILE to mobileNo)
        findNavController().navigateSafely(
            R.id.open_register_fragment_action,
            args = bundle
        )*/
    }

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun checkForGpsStatus() {
        locationHelper.checkForGpsSettings(object : GpsSettingsCheckCallback {

            override fun requiredGpsSettingAreUnAvailable(status: ResolvableApiException?) {
                status?.startResolutionForResult(
                    this@LoginActivity,
                    RegisterActivity.REQUEST_UPGRADE_GPS_SETTINGS
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

    fun getReferalCode() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener {
                referralCode = it?.link?.getQueryParameter("invitedby")
                Log.d("Refer Success", "$referralCode")
            }
            .addOnFailureListener {
                //  Log.d("Refer Failure", it.localizedMessage)
            }
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

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}