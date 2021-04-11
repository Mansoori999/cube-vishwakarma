package com.vinners.trumanms.feature.auth.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vinners.trumanms.core.di.modules.ViewModelKey
import com.vinners.trumanms.feature.auth.ui.dashboard.DashboardViewModel
import com.vinners.trumanms.feature.auth.ui.forgotPassword.ForgotPasswordViewModel
import com.vinners.trumanms.feature.auth.ui.login.LoginViewModel
import com.vinners.trumanms.feature.auth.ui.otp.OtpViewModel
import com.vinners.trumanms.feature.auth.ui.register.RegisterViewModel
import com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog.StateCityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthModule {

    @Binds
    abstract fun bindViewModelFactory(factoryAuth: AuthViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    abstract fun bindForgotPasswordViewModel(viewModel: ForgotPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OtpViewModel::class)
    abstract fun bindOtpViewModel(viewModel: OtpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun bindDashboardViewModel(viewModel: DashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StateCityViewModel::class)
    abstract fun bindStateCityViewModel(viewModel: StateCityViewModel): ViewModel
}