package com.vinners.cube_vishwakarma.feature_auth.ui.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.vinners.cube_vishwakarma.core.base.BaseFragment
import com.vinners.cube_vishwakarma.core.extensions.navigateSafely
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.core.taskState.Lse
import com.vinners.cube_vishwakarma.feature_auth.R
import com.vinners.cube_vishwakarma.feature_auth.databinding.FragmentLoginBinding
import com.vinners.cube_vishwakarma.feature_auth.di.AuthViewModelFactory
import com.vinners.cube_vishwakarma.feature_auth.di.DaggerAuthComponent
import com.vinners.cube_vishwakarma.feature_auth.ui.login.OtpVerify.OtpCheckActivity


import javax.inject.Inject

@Suppress("UNREACHABLE_CODE")
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {

    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory

    private var highlightedEditTextIndex = 0
    private var previousLength = 0
    private var mobileNo: String = ""
    lateinit var navController: NavController
    private var isVerified: Boolean = false

    override val viewModel: LoginViewModel by viewModels { viewModelFactory }


    override fun onInitDependencyInjection() {
        DaggerAuthComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {

        viewBinding.loginButton.setBackgroundColor(Color.parseColor("#2383E1"))
        viewBinding.loginButton.setOnClickListener {

            val email = viewBinding.textEmail.text
            val password = viewBinding.textPassword.text

                if (email.toString().trim().isEmpty()) {
                    viewBinding.textEmail.error = "Required"
                    Toast.makeText(requireContext(), "Login ID Required", Toast.LENGTH_SHORT)
                        .show()
                } else if (password.toString().trim().isEmpty()) {
                    viewBinding.textPassword.error = "Required"
                    Toast.makeText(requireContext(), "4 Digit PIN Required", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(context, OtpCheckActivity::class.java)
                    startActivity(intent)
                }
            }

    }


    override fun onInitViewModel() {

    }

}

//        viewBinding.userPasswordInputLayout.visibility = View.GONE
//        viewBinding.et1.setOnClickListener {
//            showSoftKeyboard(viewBinding.YoEt)
//        }
//        viewBinding.et2.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
//        viewBinding.et3.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
//        viewBinding.et4.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
//        viewBinding.et5.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
//        viewBinding.et6.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
//        viewBinding.et7.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
//        viewBinding.et8.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
//        viewBinding.et9.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }
//        viewBinding.et10.setOnClickListener { showSoftKeyboard(viewBinding.YoEt) }

//        startListeningForOtpTypeEvents()
            /* viewBinding.loginBtn.setOnClickListener {
             if (viewBinding.userPasswordInputLayout.isVisible) {
                 viewModel.login(
                     mobileNo,
                     viewBinding.passwordEt.text.toString()
                 )
             } else {
                 viewModel.isUserRegister(mobileNo)
             }
         }
        viewBinding.loginOtpBtn.setOnClickListener {
            viewModel.isUserRegister(mobileNo)
            // viewModel.loginWithOtp(mobileNo)
        }
        viewBinding.forgotPassword.setOnClickListener {
            viewModel.forgetPassword(mobileNo)
        }
    }


    override fun onInitViewModel() {

        viewModel.registerWithOtpState.observe(this, Observer {
             when (it) {
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
             }
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    isVerified = it.content.isOtpVerified
                    if (it.content.isOtpVerified)
                        viewModel.loginWithOtp(mobileNo)
                    else
                        openConfirmOtpScreen(it.content.otpToken)
                }
                is Lce.Error -> {
                    showInformationDialog(it.error)
                }
            }
        })

        viewModel.loginFormState.observe(
            this,
            Observer {
                showInformationDialog(it)
            })

        viewModel.loginStateChange.observe(
            this,
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
                                requireContext(),
                                "login successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
//                            val bundle =
//                                bundleOf(DashboardFragment.INTENT_USER_NAME to loginResult.content.firstName)
//                            findNavController().navigateSafely(
//                                R.id.open_dashboard_from_login_action,
//                                args = bundle
//                            )
                        }
                    }
                    is Lce.Error -> {
                        showInformationDialog(loginResult.error)
                    }
                }
            })
        viewModel.loginWithOtpState.observe(viewLifecycleOwner, Observer {
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
        viewModel.forgetPasswordState.observe(viewLifecycleOwner, Observer {
            when (it) {
                Lse.Loading -> {

                }
                is Lse.Success -> {
                    Toast.makeText(requireContext(), "Password Sent", Toast.LENGTH_SHORT).show()
                }
                is Lse.Error -> {

                }
            }
        })
    }

    private fun showPasswordView() {
        viewBinding.userPasswordInputLayout.setVisibilityVisible()
    }

    private fun openConfirmOtpScreen(otpToken: String?) {
//        val bundle =
//            bundleOf(
//                OtpConfirmFragment.INTENT_EXTRA_OTP_TOKEN to otpToken,
//                OtpConfirmFragment.INTENT_EXTRA_MOBILE to mobileNo,
//                OtpConfirmFragment.INTENT_EXTRA_IS_REGISTER to isVerified
//            )
//        findNavController().navigateSafely(
//            R.id.open_otp_confirm_action,
//            args = bundle
//        )
    }

    private fun openRegistrationForm() {
        // viewBinding.userPasswordInputLayout.setVisibilityGone()
//        val bundle =
//            bundleOf(RegisterActivity.INTENT_EXTRA_MOBILE to mobileNo)
//        findNavController().navigateSafely(
//            R.id.open_register_fragment_action,
//            args = bundle
//        )
    }

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}*/




