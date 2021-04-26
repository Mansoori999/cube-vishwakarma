package com.vinners.cube_vishwakarma.feature_auth.di

import com.vinners.cube_vishwakarma.core.di.CoreComponent
import com.vinners.cube_vishwakarma.core.di.scopes.FeatureScope
import com.vinners.cube_vishwakarma.feature_auth.ui.login.LoginFragment
import com.vinners.cube_vishwakarma.feature_auth.ui.login.OtpVerify.OtpCheckActivity

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

    fun inject(fragment: OtpCheckActivity)


}