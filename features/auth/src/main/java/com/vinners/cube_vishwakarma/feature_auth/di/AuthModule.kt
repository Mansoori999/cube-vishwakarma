package com.vinners.cube_vishwakarma.feature_auth.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vinners.cube_vishwakarma.core.di.modules.ViewModelKey
import com.vinners.cube_vishwakarma.feature_auth.ui.login.LoginViewModel
import com.vinners.cube_vishwakarma.feature_auth.ui.otpVerify.OtpViewModel
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
    @ViewModelKey(OtpViewModel::class)
    abstract fun bindOtpViewModel(viewModel: OtpViewModel): ViewModel

}