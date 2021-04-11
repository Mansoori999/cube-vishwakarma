package com.vinners.cube_vishwakarma.core.mdm

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigInteger
import java.net.NetworkInterface
import java.security.MessageDigest
import java.util.*

object DeviceInfo {
    // get System info.
    var OSNAME = System.getProperty("os.otpVerificationToken")
    var OSVERSION = System.getProperty("os.version")
    var RELEASE = Build.VERSION.RELEASE
    var DEVICE = Build.DEVICE
    var MODEL = Build.MODEL
    var PRODUCT = Build.PRODUCT
    var BRAND = Build.BRAND
    var DISPLAY = Build.DISPLAY
    var UNKNOWN = Build.UNKNOWN
    var HARDWARE = Build.HARDWARE
    var ID = Build.ID
    var MANUFACTURER = Build.MANUFACTURER
    var SERIAL = Build.SERIAL
    var USER = Build.USER
    var HOST = Build.HOST

    fun getDeviceInfo(
        activity: Context,
        device: Device?
    ): String {
        try {
            when (device) {
                Device.DEVICE_LANGUAGE -> return Locale.getDefault().displayLanguage
                Device.DEVICE_HARDWARE_MODEL -> return deviceName
                Device.DEVICE_NUMBER_OF_PROCESSORS -> return Runtime.getRuntime()
                    .availableProcessors().toString() + ""
                Device.DEVICE_TOTAL_MEMORY -> return getTotalMemory(
                    activity
                ).toString()
                Device.DEVICE_FREE_MEMORY -> return getFreeMemory(
                    activity
                ).toString()
                Device.DEVICE_USED_MEMORY -> {
                    val freeMem =
                        getTotalMemory(activity) - getFreeMemory(activity)
                    return freeMem.toString()
                }
                Device.DEVICE_MANUFACTURE -> return Build.MANUFACTURER
                Device.DEVICE_SYSTEM_VERSION -> return deviceName
                Device.DEVICE_VERSION -> return Build.VERSION.SDK_INT.toString()
                Device.DEVICE_IN_INCH -> return getDeviceInch(
                    activity
                ).toString()
                Device.DEVICE_NETWORK_TYPE -> return getNetworkType(
                    activity
                )
                Device.DEVICE_NETWORK -> return checkNetworkStatus(
                    activity
                )
                Device.DEVICE_TYPE -> return if (isTablet(activity)) {
                    if (getDeviceMoreThan5Inch(activity)) {
                        "Tablet"
                    } else "Mobile"
                } else {
                    "Mobile"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    val androidVersionName: String
        get() {
            var androidVersionName = ""
            val fields = VERSION_CODES::class.java.fields
            for (field in fields) {
                val fieldName = field.name
                var fieldValue = -1
                try {
                    fieldValue = field.getInt(Any())
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
                if (fieldValue == Build.VERSION.SDK_INT) {
                    androidVersionName = fieldName
                }
            }
            return androidVersionName
        }

    val androidVersionNameAndVersionCode: String
        get() {
            val builder = StringBuilder()
            builder.append("android : ").append(Build.VERSION.RELEASE)
            val fields = VERSION_CODES::class.java.fields
            for (field in fields) {
                val fieldName = field.name
                var fieldValue = -1
                try {
                    fieldValue = field.getInt(Any())
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
                if (fieldValue == Build.VERSION.SDK_INT) {
                    builder.append(" : ").append(fieldName).append(" : ")
                    builder.append("sdk=").append(fieldValue)
                }
            }
            return builder.toString()
        }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        var device_uuid = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        if (device_uuid == null) {
            device_uuid = "12356789" // for emulator testing
        } else {
            try {
                var _data = device_uuid.toByteArray()
                val _digest =
                    MessageDigest.getInstance("MD5")
                _digest.update(_data)
                _data = _digest.digest()
                val _bi = BigInteger(_data).abs()
                device_uuid = _bi.toString(36)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return device_uuid
    }

    @SuppressLint("NewApi")
    private fun getTotalMemory(activity: Context): Long {
        return try {
            val mi = ActivityManager.MemoryInfo()
            val activityManager =
                activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.getMemoryInfo(mi)
            mi.totalMem / 1048576L
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private fun getFreeMemory(activity: Context): Long {
        return try {
            val mi = ActivityManager.MemoryInfo()
            val activityManager =
                activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.getMemoryInfo(mi)
            mi.availMem / 1048576L
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private val deviceName: String
         get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }

    private fun capitalize(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }

    @SuppressLint("HardwareIds")
    fun isEmulator(context: Context): Boolean {
        val androidId = Settings.Secure.getString(context.contentResolver, "android_id")
        return "sdk" == Build.PRODUCT || "google_sdk" == Build.PRODUCT || androidId == null
    }

    fun isRooted(context: Context): Boolean {
        val isEmulator = isEmulator(context)
        val buildTags = Build.TAGS
        return if (!isEmulator && buildTags != null && buildTags.contains("test-keys")) {
            true
        } else {
            var file = File("/system/app/Superuser.apk")
            if (file.exists()) {
                true
            } else {
                file = File("/system/xbin/su")
                !isEmulator && file.exists()
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun getNetworkType(activity: Context): String {
        var networkStatus = ""
        val connMgr =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // check for wifi
        val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        // check for mobile data
        val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        networkStatus = if (wifi.isAvailable) {
            "Wifi"
        } else if (mobile.isAvailable) {
            getDataType(activity)
        } else {
            "noNetwork"
        }
        return networkStatus
    }

    @SuppressLint("MissingPermission")
    fun checkNetworkStatus(activity: Context): String {
        var networkStatus = ""
        try {
            // Get connect mangaer
            val connMgr =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // // check for wifi
            val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            // // check for mobile data
            val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (wifi.isAvailable) {
                networkStatus = "Wifi"
            } else if (mobile.isAvailable) {
                networkStatus = getDataType(activity)
            } else {
                networkStatus = "noNetwork"
                networkStatus = "0"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            networkStatus = "0"
        }
        return networkStatus
    }

    val totalStorageInMb: Long
        get() {
            val stat = StatFs(Environment.getExternalStorageDirectory().path)
            val bytesAvailable = stat.blockSizeLong * stat.blockCountLong
            return bytesAvailable / 1048576
        }

    fun isTablet(context: Context): Boolean {
        return context.resources
            .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    fun getDeviceMoreThan5Inch(activity: Context): Boolean {
        return try {
            val displayMetrics = activity.resources.displayMetrics
            // int width = displayMetrics.widthPixels;
            // int height = displayMetrics.heightPixels;
            val yInches = displayMetrics.heightPixels / displayMetrics.ydpi
            val xInches = displayMetrics.widthPixels / displayMetrics.xdpi
            val diagonalInches =
                Math.sqrt(xInches * xInches + yInches * yInches.toDouble())
            // 5inch device or bigger
            // smaller device
            diagonalInches >= 7
        } catch (e: Exception) {
            false
        }
    }

    fun getDeviceInch(activity: Context): Double {
        return try {
            val displayMetrics = activity.resources.displayMetrics
            val yInches = displayMetrics.heightPixels / displayMetrics.ydpi
            val xInches = displayMetrics.widthPixels / displayMetrics.xdpi
            Math.sqrt(xInches * xInches + yInches * yInches.toDouble())
        } catch (e: Exception) {
            0.0
        }
    }

    fun getDataType(activity: Context): String {
        var type = "Mobile Data"
        val tm =
            activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        when (tm.networkType) {
            TelephonyManager.NETWORK_TYPE_HSDPA -> type = "Mobile Data 3G"
            TelephonyManager.NETWORK_TYPE_HSPAP -> type = "Mobile Data 4G"
            TelephonyManager.NETWORK_TYPE_GPRS -> type = "Mobile Data GPRS"
            TelephonyManager.NETWORK_TYPE_EDGE -> type = "Mobile Data EDGE 2G"
        }
        return type
    }

    @SuppressLint("MissingPermission", "NewApi")
    fun getNetworkOperator(context: Context): List<String> {
        // Get System TELEPHONY service reference
        val carrierNames: MutableList<String> =
            ArrayList()
        try {
            val permission = Manifest.permission.READ_PHONE_STATE
            if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP_MR1 && ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val subscriptionInfos =
                    SubscriptionManager.from(context).activeSubscriptionInfoList
                for (i in subscriptionInfos.indices) {
                    carrierNames.add(subscriptionInfos[i].carrierName.toString())
                }
            } else {
                val telephonyManager =
                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                // Get carrier otpVerificationToken (Network Operator Name)
                carrierNames.add(telephonyManager.networkOperatorName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return carrierNames
    }
}