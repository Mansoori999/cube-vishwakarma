package com.vinners.cube_vishwakarma.feature_auth.ui.login.OtpVerify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Chronometer
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
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.base.CoreApplication
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.feature_auth.R
import com.vinners.cube_vishwakarma.feature_auth.databinding.ActivityOtpcheckBinding
import com.vinners.cube_vishwakarma.feature_auth.di.AuthViewModelFactory
import com.vinners.cube_vishwakarma.feature_auth.di.DaggerAuthComponent
import com.vinners.cube_vishwakarma.feature_auth.ui.otpVerify.OtpReaderBroadcast
import com.vinners.cube_vishwakarma.feature_auth.ui.otpVerify.OtpViewModel
import javax.inject.Inject


class OtpCheckActivity : BaseActivity<ActivityOtpcheckBinding, OtpViewModel>(R.layout.activity_otpcheck){
    companion object {
        const val INTENT_EXTRA_OTP_TOKEN = "OtpToken"
        const val INTENT_EXTRA_MOBILE = "mobile"
        const val INTENT_EXTRA_IS_REGISTER = "isRegister"
        const val INTENT_REFERAL_CODE = "referal_code"
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

    private var optEntered: Int? = null

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


    private val smsRetrieverClient: SmsRetrieverClient
        get() {
            return SmsRetriever.getClient(this@OtpCheckActivity)
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
            .inject(this@OtpCheckActivity)
    }
    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
    override fun onInitDataBinding() {
        viewBinding.arrowBackBtn.setOnClickListener {
            onBackPressed()
        }
        viewBinding.et1.setOnClickListener {
            showSoftKeyboard(viewBinding.YoEt)
        }
        viewBinding.et2.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et3.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et4.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        //viewBinding.et5.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        initSmsRetrieverApi()
        viewBinding.otpcontinuebutton.setBackgroundColor(Color.parseColor("#2383E1"))
        otpToken = intent?.getStringExtra(INTENT_EXTRA_OTP_TOKEN)
        mobileNo = intent?.getStringExtra(INTENT_EXTRA_MOBILE)
        isRegister = intent.getBooleanExtra(INTENT_EXTRA_IS_REGISTER, false)
        referralCode = intent.getStringExtra(INTENT_REFERAL_CODE)
        viewBinding.phoneNo.text = "+91${mobileNo}"
        startListeningForOtpTypeEvents()
        (this.application as CoreApplication).reInitCoreDependencies()
        viewBinding.otpcontinuebutton.setOnClickListener {
            val optEntered1 = viewBinding.et1.text.toString().toInt()
            val optEntered2 = viewBinding.et2.text.toString().toInt()
            val optEntered3 = viewBinding.et3.text.toString().toInt()
            val optEntered4 = viewBinding.et4.text.toString().toInt()

            val otpEnter = "$optEntered1$optEntered2$optEntered3$optEntered4".toInt()
//            if (otpEnter == 4691){
//                viewModel.otpLoginConfirm(otpToken, token)
//
//            }else {
                if (otpToken != null)
                    viewModel.otpLoginConfirm(otpToken, token)
//            else if (otpToken.isNullOrEmpty().not() && token.isNullOrEmpty().not())
//                viewModel.otpConfirm(otpToken!!, token!!, referralCode,gps, address)
                else
                    showInformationDialog("Otp must not be empty")
//            }
        }

        viewBinding.otpAgain.setOnClickListener {
            startChronometer()
            if (otpToken.isNullOrEmpty().not())
                viewModel.resendOtp(otpToken!!)
        }
    }
    private fun startChronometer() {
        viewBinding.chronometer.format = "%s"
        viewBinding.chronometer.start()
        viewBinding.otpAgain.setVisibilityGone()
        viewBinding.chronometer.setVisibilityVisible()
        viewBinding.chronometer.onChronometerTickListener =
            Chronometer.OnChronometerTickListener { chronometer ->
                val time = chronometer.text.toString()
                if (time == "00:30") {
                    viewBinding.chronometer.stop()
                    viewBinding.chronometer.setVisibilityGone()
                    viewBinding.otpAgain.setVisibilityVisible()
                }
            }
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
                                this@OtpCheckActivity,
                                R.drawable.edittext_background
                            )

                        val editTextNext =
                            viewBinding.editTextHolder.getChildAt(highlightedEditTextIndex - 1) as EditText
                        editTextNext.background =
                            ContextCompat.getDrawable(
                                this@OtpCheckActivity,
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
                            this@OtpCheckActivity,
                            R.drawable.edittext_background
                        )
                    val editTextNext =
                        viewBinding.editTextHolder.getChildAt(currentLength) as EditText
                    editTextNext.background =
                        ContextCompat.getDrawable(
                            this@OtpCheckActivity,
                            R.drawable.edittext_background
                        )
                    highlightedEditTextIndex = currentLength
                }
                token = s.toString()

                if (s.length == 4 && otpToken.isNullOrEmpty().not() && token.isNullOrEmpty()
                        .not()
                ) {
                    if (otpToken != null)
                        viewModel.otpLoginConfirm(otpToken, token)
//                    else if (otpToken.isNullOrEmpty().not() && token.isNullOrEmpty().not())
//                        viewModel.otpConfirm(otpToken!!, token!!, referralCode,gps,address)
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

    override fun onInitViewModel() {
        viewModel.registerSmsReceiver().observe(this@OtpCheckActivity, Observer {
            registerReceiver()
        })

        viewModel.otpLoginConfirmState().observe(this@OtpCheckActivity, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.otpcontinuebutton.showProgress {
                        buttonText = "Loading"
                        progressColor = Color.WHITE
                    }
                    viewBinding.otpcontinuebutton.isEnabled = false
                }
                is Lce.Content -> {
                    if (it.content.name.isNullOrEmpty().not()) {
                        viewBinding.otpcontinuebutton.hideProgress(
                            "Continue"
                        )
                        viewBinding.otpcontinuebutton.isEnabled = true
                        Toast.makeText(
                            this,
                            "login successfully",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        val intent = Intent(
                            this@OtpCheckActivity,
                            Class.forName("com.vinners.cube_vishwakarma.ui.MainActivity")
                        )
                        // intent.putExtra(DashBoardActivity.INTENT_USER_NAME, it.content.firstName)
                        startActivity(intent)
                        finish()
                    } else {
//                        val intent = Intent(this@OtpCheckActivity, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
                    }
                }
                is Lce.Error -> {
                    viewBinding.otpcontinuebutton.hideProgress(
                       "Continue"
                    )
                    viewBinding.otpcontinuebutton.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })


    }
}


