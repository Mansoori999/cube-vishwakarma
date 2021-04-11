package com.vinners.trumanms.feature.auth.di

import com.vinners.trumanms.core.di.CoreComponent
import com.vinners.trumanms.core.di.scopes.FeatureScope
import com.vinners.trumanms.feature.auth.ui.AuthActivity
import com.vinners.trumanms.feature.auth.ui.dashboard.DashBoardActivity
import com.vinners.trumanms.feature.auth.ui.dashboard.DashboardFragment
import com.vinners.trumanms.feature.auth.ui.forgotPassword.ForgotPasswordFragment
import com.vinners.trumanms.feature.auth.ui.login.LoginActivity
import com.vinners.trumanms.feature.auth.ui.login.LoginFragment
import com.vinners.trumanms.feature.auth.ui.login.PrivacyPolicyActivity
import com.vinners.trumanms.feature.auth.ui.otp.OtpActivity
import com.vinners.trumanms.feature.auth.ui.otp.OtpConfirmFragment
import com.vinners.trumanms.feature.auth.ui.register.RegisterActivity
import com.vinners.trumanms.feature.auth.ui.register.RegisterFragment
import com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog.*
import dagger.Component

@FeatureScope
@Component(
    modules = [
        AuthModule::class
    ],
    dependencies = [CoreComponent::class]
)
interface AuthComponent {

    fun inject(fragment: LoginFragment)

    fun inject(fragment: RegisterFragment)

    fun inject(fragment: ForgotPasswordFragment)

    fun inject(activity: AuthActivity)

    fun inject(activity: LoginActivity)

    fun inject(activity: OtpActivity)

    fun inject(activity: RegisterActivity)

    fun inject(activity: DashBoardActivity)

    fun inject(fragment: OtpConfirmFragment)

    fun inject(fragment: DashboardFragment)

    fun inject(stateDialogFragment: StateDialogFragment)

    fun inject(cityDialogFragment: CityDialogFragment)

    fun inject(languageDialogFragment: LanguageDialogFragment)

    fun inject(workCategoryDialogFragment: WorkCategoryDialogFragment)

    fun inject(pincodeDialogFragment: PincodeDialogFragment)

    fun inject(privacyPolicyActivity: PrivacyPolicyActivity)
}