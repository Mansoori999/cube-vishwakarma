package com.vinners.trumanms.di


import com.vinners.trumanms.core.di.CoreComponent
import com.vinners.trumanms.core.di.scopes.FeatureScope
import com.vinners.trumanms.notification.NotificationService
import com.vinners.trumanms.ui.languageSelection.SelectLanguageFragment
import com.vinners.trumanms.ui.LauncherActivity
import com.vinners.trumanms.ui.appIntro.AppIntroFragment
import com.vinners.trumanms.ui.appIntro.AppIntroActivity
import com.vinners.trumanms.ui.appVersionDiscontinued.VersionBlockActivity
import com.vinners.trumanms.ui.home.HomeActivity
import com.vinners.trumanms.ui.splash.SplashActivity
import com.vinners.trumanms.ui.splash.SplashFragment
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

    fun inject(appIntroActivity: AppIntroActivity)

    fun inject(splashFragment: SplashFragment)

    fun inject(languageFragment: SelectLanguageFragment)

    fun inject(appIntroFragment: AppIntroFragment)

    fun inject(homeActivity: HomeActivity)

    fun inject(versionBlockActivity: VersionBlockActivity)
}