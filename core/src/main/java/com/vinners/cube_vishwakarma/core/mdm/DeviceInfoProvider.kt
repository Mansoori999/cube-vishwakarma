package com.vinners.cube_vishwakarma.core.mdm

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.vinners.cube_vishwakarma.data.models.mdm.DeviceInformation
import com.vinners.cube_vishwakarma.data.models.mdm.MobileInformation
import com.vinners.cube_vishwakarma.data.models.mdm.NetworkInfo
import com.vinners.cube_vishwakarma.data.models.mdm.OsInformation
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeviceInfoProvider @Inject constructor(private val applicationContext: Context) {

    @Throws(Exception::class)
    fun getDeviceInfo(): MobileInformation {

        val mobileInformation = MobileInformation()

        val deviceInformation = DeviceInformation()
        deviceInformation.setBrandName(DeviceInfo.BRAND)
        deviceInformation.setDeviceScreenSize(DeviceInfo.getDeviceInch(applicationContext))
        deviceInformation.setTablet(DeviceInfo.isTablet(applicationContext))
        deviceInformation.setModelName(DeviceInfo.MODEL)
        deviceInformation.setTotalRam(
            DeviceInfo.getDeviceInfo(
                applicationContext,
                Device.DEVICE_TOTAL_MEMORY
            )
        )
        deviceInformation.setTotalStorage(DeviceInfo.totalStorageInMb.toString() + "MB")
        mobileInformation.deviceInformation = deviceInformation

        val osInformation = OsInformation()
        osInformation.setDeviceRooted(DeviceInfo.isRooted(applicationContext))
        osInformation.setOsVersionName(DeviceInfo.androidVersionName)
        osInformation.setVersionCode(Build.VERSION.SDK_INT)
        mobileInformation.osInformation = osInformation

        val networkInfo = NetworkInfo()
        val networkOperatorList = DeviceInfo.getNetworkOperator(applicationContext)
        networkInfo.setSim1ProviderNames(networkOperatorList)
        networkInfo.setSimCount(networkOperatorList.size)
        networkInfo.setInternetConnectionMode(DeviceInfo.checkNetworkStatus(applicationContext))
        mobileInformation.networkInfo = networkInfo

        try {
            val packageName = applicationContext.packageName
            val pInfo = applicationContext.packageManager.getPackageInfo(packageName, 0)
            mobileInformation.appVersion = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return mobileInformation
    }

}