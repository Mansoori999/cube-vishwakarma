package com.vinners.trumanms.feature.auth.ui.otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.Geocoder
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Chronometer.OnChronometerTickListener
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.base.CoreApplication
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.location.LocationHelper
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.ActivityOtpBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import com.vinners.trumanms.feature.auth.ui.register.RegisterActivity
import java.util.*
import javax.inject.Inject

class OtpActivity : BaseActivity<ActivityOtpBinding, OtpViewModel>(R.layout.activity_otp) {

    companion object {
        const val INTENT_EXTRA_OTP_TOKEN = "OtpToken"
        const val INTENT_EXTRA_MOBILE = "mobile"
        const val INTENT_EXTRA_IS_REGISTER = "isRegister"
        const val INTENT_REFERAL_CODE = "referal_code"
        const val INTENT_GPS = "gps"
        const val INTENT_ADDRESS ="address"
    }

    private var highlightedEditTextIndex = 0
    private var previousLength = 0
    private var otpToken: String? = null
    private var token: String? = null
    private var mobileNo: String? = null
    private var isRegister = false
    private var referralCode : String? = null
    private var gps: String? = null
    private var address: String? = null


    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory


    override val viewModel: OtpViewModel by viewModels { viewModelFactory }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (OtpReaderBroadcast.ACTION_OTP_DELIVER.equals(intent.action!!, ignoreCase = true)) {
                val message = intent.getStringExtra(OtpReaderBroadcast.INTENT_EXTRA_OTP_RECEIVED)
                setOtpOnScreen(message)
                unregisterReceiver()
            }
        }
    }

    private val smsRetrieverClient: SmsRetrieverClient
        get() {
            return SmsRetriever.getClient(this@OtpActivity)
        }

    private fun initSmsRetrieverApi() {
        smsRetrieverClient
            .startSmsRetriever()
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.d("SMS", it.localizedMessage)
            }
    }


    override fun onInitDependencyInjection() {
        DaggerAuthComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this@OtpActivity)
    }

    override fun onInitDataBinding() {
        //initViewModel()

        viewBinding.et1.setOnClickListener {
            showSoftKeyboard(viewBinding.YoEt)
        }
        viewBinding.et2.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et3.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et4.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        //viewBinding.et5.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        initSmsRetrieverApi()
        otpToken = intent?.getStringExtra(OtpConfirmFragment.INTENT_EXTRA_OTP_TOKEN)
        mobileNo = intent?.getStringExtra(OtpConfirmFragment.INTENT_EXTRA_MOBILE)
        isRegister = intent.getBooleanExtra(OtpConfirmFragment.INTENT_EXTRA_IS_REGISTER, false)
        referralCode = intent.getStringExtra(INTENT_REFERAL_CODE)
        gps = intent?.getStringExtra(INTENT_GPS)
        address = intent?.getStringExtra(INTENT_ADDRESS)
        startListeningForOtpTypeEvents()
        (this.application as CoreApplication).reInitCoreDependencies()
        viewBinding.otpConfirmBtn.setOnClickListener {
            if (isRegister)
                viewModel.otpLoginConfirm(otpToken, token,gps, address)
            else if (otpToken.isNullOrEmpty().not() && token.isNullOrEmpty().not())
                    viewModel.otpConfirm(otpToken!!, token!!, referralCode,gps, address)
            else
                showInformationDialog("Otp must not be empty")
        }
        viewBinding.mobileEt.text = "+91 ${mobileNo}"
        viewBinding.arrowBackBtn.setOnClickListener {
            onBackPressed()
        }
        viewBinding.otpAgain.setOnClickListener {
            startChronometer()
            if (otpToken.isNullOrEmpty().not())
                viewModel.resendOtp(otpToken!!)
        }
    }

    override fun onInitViewModel() {
        viewModel.registerSmsReceiver().observe(this@OtpActivity, Observer {
            registerReceiver()
        })
        viewModel.unregisterSmsReceiver().observe(this@OtpActivity, Observer {
            unregisterReceiver()
        })

        viewModel.otpConfirmState().observe(this@OtpActivity, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.otpConfirmBtn.showProgress {
                        buttonText = "Loading"
                        progressColor = Color.WHITE
                    }
                    viewBinding.otpConfirmBtn.isEnabled = false
                }

                is Lce.Content -> {
                    viewBinding.otpConfirmBtn.hideProgress(
                        R.string.contin
                    )
                    viewBinding.otpConfirmBtn.isEnabled = true
                    if (it.content.firstName.isNullOrEmpty().not()) {
                        val intent = Intent(
                            this@OtpActivity,
                            Class.forName("com.vinners.trumanms.ui.home.HomeActivity")
                        )
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@OtpActivity, RegisterActivity::class.java)
                        intent.putExtra(RegisterActivity.INTENT_EXTRA_MOBILE, mobileNo)
                        startActivity(intent)
                        finish()
                    }
                }
                is Lce.Error -> {
                    viewBinding.otpConfirmBtn.hideProgress(
                        R.string.contin
                    )
                    viewBinding.otpConfirmBtn.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.otpLoginConfirmState().observe(this@OtpActivity, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.otpConfirmBtn.showProgress {
                        buttonText = "Loading"
                        progressColor = Color.WHITE
                    }
                    viewBinding.otpConfirmBtn.isEnabled = false
                }
                is Lce.Content -> {
                    if (it.content.firstName.isNullOrEmpty().not()) {
                        viewBinding.otpConfirmBtn.hideProgress(
                            R.string.contin
                        )
                        viewBinding.otpConfirmBtn.isEnabled = true
                        Toast.makeText(
                            this,
                            "login successfully",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        val intent = Intent(
                            this@OtpActivity,
                            Class.forName("com.vinners.trumanms.ui.home.HomeActivity")
                        )
                        // intent.putExtra(DashBoardActivity.INTENT_USER_NAME, it.content.firstName)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@OtpActivity, RegisterActivity::class.java)
                        intent.putExtra(RegisterActivity.INTENT_EXTRA_MOBILE, mobileNo)
                        startActivity(intent)
                        finish()
                    }
                }
                is Lce.Error -> {
                    viewBinding.otpConfirmBtn.hideProgress(
                        R.string.contin
                    )
                    viewBinding.otpConfirmBtn.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.resendOtpState().observe(this, Observer {
            when (it) {
                Lse.Loading -> {

                }
                is Lse.Success -> {
                    viewBinding.chronometer.stop()
                    viewBinding.chronometer.setVisibilityGone()
                    viewBinding.otpAgain.setVisibilityVisible()
                    Toast.makeText(this@OtpActivity, "Otp Received", Toast.LENGTH_SHORT).show()
                }
                is Lse.Error -> {
                    viewBinding.chronometer.stop()
                    viewBinding.chronometer.setVisibilityGone()
                    viewBinding.otpAgain.setVisibilityVisible()
                    showInformationDialog(it.error)
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
                    deleteHighlightedDigit()
                }

                previousLength = currentLength
            }

            private fun deleteHighlightedDigit() {
                if (highlightedEditTextIndex == 0) {
                    val editText = viewBinding.editTextHolder.getChildAt(0) as EditText
                    editText.text = null
                } else {

                    val editText =
                        viewBinding.editTextHolder.getChildAt(highlightedEditTextIndex) as EditText
                    val text = editText.text.toString()

                    if (highlightedEditTextIndex == 3 && text.isNotEmpty()) {
                        editText.text = null
                    } else {

                        editText.text = null
                        editText.background =
                            ContextCompat.getDrawable(
                                this@OtpActivity,
                                R.drawable.edittext_background
                            )

                        val editTextNext =
                            viewBinding.editTextHolder.getChildAt(highlightedEditTextIndex - 1) as EditText
                        editTextNext.background =
                            ContextCompat.getDrawable(
                                this@OtpActivity,
                                R.drawable.edittext_background
                            )

                        if (text.isEmpty())
                            editTextNext.text = null

                        highlightedEditTextIndex--
                    }
                }
            }

            private fun insertDigitInCurrentAndHighlightNextOne(
                s: CharSequence,
                currentLength: Int
            ) {
                val editText = viewBinding.editTextHolder.getChildAt(previousLength) as EditText
                editText.setText(s.subSequence(previousLength, currentLength).toString())

                if (currentLength < 4) {
                    editText.background =
                        ContextCompat.getDrawable(
                            this@OtpActivity,
                            R.drawable.edittext_background
                        )
                    val editTextNext =
                        viewBinding.editTextHolder.getChildAt(currentLength) as EditText
                    editTextNext.background =
                        ContextCompat.getDrawable(
                            this@OtpActivity,
                            R.drawable.edittext_background
                        )
                    highlightedEditTextIndex = currentLength
                }
                token = s.toString()
                if (s.length == 4 && otpToken.isNullOrEmpty().not() && token.isNullOrEmpty()
                        .not()
                ) {
                    if (isRegister)
                        viewModel.otpLoginConfirm(otpToken, token,gps,address)
                    else if (otpToken.isNullOrEmpty().not() && token.isNullOrEmpty().not())
                        viewModel.otpConfirm(otpToken!!, token!!, referralCode,gps,address)
                    else
                        showInformationDialog("Otp must not be empty")
                }
            }
        })
    }

    public override fun onResume() {
        super.onResume()

        if (viewModel.isReadingOtp)
            registerReceiver()
    }

    private fun registerReceiver() {
        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(
                receiver,
                IntentFilter(OtpReaderBroadcast.ACTION_OTP_DELIVER)
            )
    }

    public override fun onPause() {
        super.onPause()

        if (viewModel.isReadingOtp)
            unregisterReceiver()
    }

    private fun unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    private fun setOtpOnScreen(message: String?) {
        message ?: return

        if (message.length != 4)
            return

        repeat(viewBinding.YoEt.length()) {
            viewBinding.YoEt.text.delete(viewBinding.YoEt.length() - 1, viewBinding.YoEt.length())
        }

        for (i in 0..3)
            viewBinding.YoEt.text.append(message[i])
    }

    private fun startChronometer() {
        viewBinding.chronometer.format = "%s"
        viewBinding.chronometer.start()
        viewBinding.otpAgain.setVisibilityGone()
        viewBinding.chronometer.setVisibilityVisible()
        viewBinding.chronometer.onChronometerTickListener =
            OnChronometerTickListener { chronometer ->
                val time = chronometer.text.toString()
                if (time == "00:30") {
                    viewBinding.chronometer.stop()
                    viewBinding.chronometer.setVisibilityGone()
                    viewBinding.otpAgain.setVisibilityVisible()
                }
            }
    }

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}