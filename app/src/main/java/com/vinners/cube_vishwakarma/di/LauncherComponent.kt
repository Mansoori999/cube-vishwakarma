package com.vinners.cube_vishwakarma.di


import com.vinners.cube_vishwakarma.core.di.CoreComponent
import com.vinners.cube_vishwakarma.core.di.scopes.FeatureScope
import com.vinners.cube_vishwakarma.notification.NotificationService
import com.vinners.cube_vishwakarma.ui.languageSelection.SelectLanguageFragment
import com.vinners.cube_vishwakarma.ui.LauncherActivity
import com.vinners.cube_vishwakarma.ui.appVersionDiscontinued.VersionBlockActivity
import com.vinners.cube_vishwakarma.ui.splash.SplashActivity
import com.vinners.cube_vishwakarma.ui.splash.SplashFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [LauncherModule::class],
    dependencies = [CoreComponent::class]
)
interface LauncherComponent {

    fun inject(launcherActivity: LauncherActivity)

    fun inject(notificationService: NotificationService)

    fun inject(splashActivity: SplashActivity)

    fun inject(splashFragment: SplashFragment)

    fun inject(languageFragment: SelectLanguageFragment)

    fun inject(versionBlockActivity: VersionBlockActivity)
}