package com.vinners.cube_vishwakarma.core.base

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.di.CoreComponent
import com.vinners.cube_vishwakarma.core.di.DaggerCoreComponent
import com.vinners.cube_vishwakarma.core.locale.LocaleManager
import com.vinners.cube_vishwakarma.core.logger.LoggerImpl
import javax.inject.Inject

open class CoreApplication constructor(
    private val appInfo: AppInfo
) : Application() {

    @Inject
    lateinit var logger: LoggerImpl
    private var coreComponent: CoreComponent? = null

    override fun onCreate() {
        super.onCreate()
        initOrGetCoreDependency()
        initLogger()
    }

    /**
     * Initialises And Returns [CoreComponent], this function will be used by
     * activity to create their Dagger Component
     */
    fun initOrGetCoreDependency(): CoreComponent {
        if (coreComponent == null) {
            coreComponent = DaggerCoreComponent
                .builder()
                .application(this)
                .appInfo(appInfo)
                .build()

//            coreComponent!!.injectApplication(this)
            logger = coreComponent!!.getLoggerImpl()
        }
        return coreComponent!!
    }

    /**
     * Deletes existing [CoreComponent] forcing to be created again and dependencies are created again
     */
    fun reInitCoreDependencies(): CoreComponent {
        coreComponent = null
        return initOrGetCoreDependency()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManager.setLocale(this)
    }

    private fun initLogger() {
        logger.init(appInfo.debugBuild)
    }
}