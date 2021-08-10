package com.vinners.cube_vishwakarma.feature_auth.ui.login


import android.content.Intent
import android.graphics.Color
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.vinners.core.BuildConfig.VERSION_NAME
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseFragment
import com.vinners.cube_vishwakarma.core.base.CoreApplication
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.feature_auth.BuildConfig
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

    @Inject
    lateinit var appInfo: AppInfo

    private var highlightedEditTextIndex = 0
    private var previousLength = 0
////    "9050254025","9050",
//    private val mobileNo: String? = null
    lateinit var loginId:String
    lateinit var pin:String
    lateinit var navController: NavController
    private var isVerified: Boolean = false
    private var referralCode: String? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var address: String? = null
    var isLoginIdValid:Boolean = false
    var isPasswordValid: Boolean = false

    lateinit var loginValidations: LoginValidations

    override val viewModel: LoginViewModel by viewModels { viewModelFactory }


    override fun onInitDependencyInjection() {
        DaggerAuthComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.passwordToggle.setOnClickListener {
            if(viewBinding.textPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                viewBinding.passwordToggle.setImageResource(R.drawable.ic_invisible)
                //Show Password
                viewBinding.textPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            }
            else{

                viewBinding.passwordToggle.setImageResource(R.drawable.ic_visibile)
                //Hide Password
                viewBinding.textPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())

            }

        }
        viewBinding.loginButton.setBackgroundColor(Color.parseColor("#2383E1"))
        viewBinding.loginButton.setOnClickListener {
           loginId = viewBinding.textEmail.text.toString()
            pin = viewBinding.textPassword.text.toString()
            if (setValidation()== true) {
                viewModel.loginWithOutOtp(loginid = loginId, password = pin)
            }
//            else{
//                Toast.makeText(requireContext(), "Enter Valid Login ID and 4 Digit Pin", Toast.LENGTH_LONG).show()
//            }


            }

        viewBinding.appVersionTV.text = "App Version: ${appInfo.appVersion}"

    }


    private fun setValidation() : Boolean{
        val loginId = viewBinding.textEmail.text.toString()
        val pin = viewBinding.textPassword.text.toString()
        if (loginId.isEmpty()){
            viewBinding.textEmail.error = "Login ID Required"
            Toast.makeText(requireContext(), "Login ID Required", Toast.LENGTH_SHORT)
                .show()
            return false

        }else if (pin.isEmpty()){
            viewBinding.textPassword.error = "4 Digit PIN Required"
            Toast.makeText(requireContext(), "4 Digit PIN Required", Toast.LENGTH_SHORT).show()
            return false

        }else if (pin.length != 4){
            viewBinding.textPassword.error = "Enter At-lest 4 Digit PIN"
            return false
        }
        return true

    }


    private fun openConfirmOtpScreen(otpToken: String?) {

        Intent(context, OtpCheckActivity::class.java).apply {
            putExtra(OtpCheckActivity.INTENT_EXTRA_OTP_TOKEN, otpToken)
            putExtra(OtpCheckActivity.INTENT_EXTRA_MOBILE, loginId)
            putExtra(OtpCheckActivity.INTENT_REFERAL_CODE, referralCode)
            putExtra(OtpCheckActivity.INTENT_EXTRA_IS_REGISTER, isVerified)
        }.also {
            startActivity(it)
        }
    }
    override fun onInitViewModel() {
        viewModel.loginWithoutOtpConfirmState().observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.loginButton.showProgress {
                        buttonText = getString(R.string.loading)
                        progressColor = Color.WHITE
                    }
                    viewBinding.loginButton.isEnabled = false
                }
                is Lce.Content -> {
                    viewBinding.loginButton.hideProgress(
                            R.string.login
                    )
                    viewBinding.loginButton.isEnabled = true
                    Toast.makeText(
                            context,
                            "login successfully",
                            Toast.LENGTH_SHORT
                    )
                            .show()


                    (requireActivity().application as CoreApplication).reInitCoreDependencies()
                    val intent = Intent(
                            context,
                            Class.forName("com.vinners.cube_vishwakarma.ui.dashboard.MainActivity")
                    )
                    startActivity(intent)
                    activity?.finish()
//                    openConfirmOtpScreen(it.content.otpToken)
                }
                is Lce.Error -> {
                    viewBinding.loginButton.isEnabled = true
                    viewBinding.loginButton.hideProgress(
                            R.string.login
                    )
                    showInformationDialog(it.error)
                }
            }
        })

//        viewModel.loginWithOtp(loginId)
    }

}





