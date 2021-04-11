package com.vinners.trumanms.feature.auth.ui.otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.extensions.navigateSafely
import com.vinners.trumanms.core.logger.LoggerImpl_Factory
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.FragmentOtpConfirmBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import com.vinners.trumanms.feature.auth.ui.dashboard.DashboardFragment
import java.util.logging.Logger
import javax.inject.Inject

class OtpConfirmFragment :
    BaseFragment<FragmentOtpConfirmBinding, OtpViewModel>(R.layout.fragment_otp_confirm) {

    companion object {
        const val INTENT_EXTRA_OTP_TOKEN = "OtpToken"
        const val INTENT_EXTRA_MOBILE = "mobile"
        const val INTENT_EXTRA_IS_REGISTER = "isRegister"
    }

    private var highlightedEditTextIndex = 0
    private var previousLength = 0
    private var otpToken: String? = null
    private var token: String? = null
    private var mobileNo: String? = null
    private var isRegister: Boolean? = false

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
            return SmsRetriever.getClient(requireContext())
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
            .inject(this)
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

                    if (highlightedEditTextIndex == 4 && text.isNotEmpty()) {
                        editText.text = null
                    } else {

                        editText.text = null
                        editText.background =
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.edittext_background
                            )

                        val editTextNext =
                            viewBinding.editTextHolder.getChildAt(highlightedEditTextIndex - 1) as EditText
                        editTextNext.background =
                            ContextCompat.getDrawable(
                                requireContext(),
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

                if (currentLength < 5) {
                    editText.background =
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.edittext_background
                        )
                    val editTextNext =
                        viewBinding.editTextHolder.getChildAt(currentLength) as EditText
                    editTextNext.background =
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.edittext_background
                        )
                    highlightedEditTextIndex = currentLength
                }
                token = s.toString()
                // viewModel.otpConfirm(otpToken,token)
            }
        })
    }

    override fun onInitDataBinding() {
        viewBinding.et1.setOnClickListener {
            showSoftKeyboard(viewBinding.YoEt)
        }
        viewBinding.et2.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et3.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et4.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        viewBinding.et5.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
        initSmsRetrieverApi()
        otpToken = arguments?.getString(INTENT_EXTRA_OTP_TOKEN)
        mobileNo = arguments?.getString(INTENT_EXTRA_MOBILE)
        isRegister = arguments?.getBoolean(INTENT_EXTRA_IS_REGISTER)
        startListeningForOtpTypeEvents()

        viewBinding.loginBtn.setOnClickListener {


        }
    }

    override fun onInitViewModel() {
        viewModel.registerSmsReceiver().observe(this, Observer {
            registerReceiver()
        })
        viewModel.unregisterSmsReceiver().observe(this, Observer {
            unregisterReceiver()
        })

        viewModel.otpConfirmState().observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }

                is Lce.Content -> {
                    Toast.makeText(requireContext(), "Otp Confirmed", Toast.LENGTH_SHORT).show()
                    val bundle = bundleOf(INTENT_EXTRA_MOBILE to mobileNo)
                    findNavController().navigateSafely(
                        R.id.open_register_from_otp_fragment_action,
                        args = bundle
                    )
                }
                is Lce.Error -> {
                    Toast.makeText(requireContext(), "Otp not confirmed", Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.otpLoginConfirmState().observe(viewLifecycleOwner, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    if (it.content.firstName != null) {
                        Toast.makeText(
                            requireContext(),
                            "login successfully",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        val bundle =
                            bundleOf(DashboardFragment.INTENT_USER_NAME to it.content.firstName)
                        findNavController().navigateSafely(
                            R.id.open_dashboard_from_otp_action,
                            args = bundle
                        )
                    }else{
                        val bundle = bundleOf(INTENT_EXTRA_MOBILE to mobileNo)
                        findNavController().navigateSafely(
                            R.id.open_register_from_otp_fragment_action,
                            args = bundle
                        )
                    }
                }
                is Lce.Error -> {

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
            .getInstance(requireContext())
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
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    private fun setOtpOnScreen(message: String?) {
        message ?: return

        if (message.length != 5)
            return

        repeat(viewBinding.YoEt.length()) {
            viewBinding.YoEt.text.delete(viewBinding.YoEt.length() - 1, viewBinding.YoEt.length())
        }

        for (i in 0..4)
            viewBinding.YoEt.text.append(message[i])
    }

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

}

