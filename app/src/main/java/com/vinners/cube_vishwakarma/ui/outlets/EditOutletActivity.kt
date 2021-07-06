package com.vinners.cube_vishwakarma.ui.outlets

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import coil.ImageLoader
import coil.api.load
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.himanshu.cameraintegrator.ImageCallback
import com.himanshu.cameraintegrator.ImagesSizes
import com.himanshu.cameraintegrator.RequestSource
import com.himanshu.cameraintegrator.Result
import com.himanshu.cameraintegrator.integrator.CameraIntegrator
import com.himanshu.cameraintegrator.storage.StorageMode
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.AppConstants
import com.vinners.cube_vishwakarma.core.QuickAlertDialog
import com.vinners.cube_vishwakarma.core.QuickAlertDialog.showNonCancellableMessageDialog
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.location.GpsSettingsCheckCallback
import com.vinners.cube_vishwakarma.core.location.LocationHelper
import com.vinners.cube_vishwakarma.data.models.Location
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityEditOutletBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity.Companion.OUTLET_CUSTOMER_CODE

import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity.Companion.OUTLET_IMAGE
import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity.Companion.OUTLET_NAME
import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity.Companion.OUTLET_REGIONAL_OFFICE
import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity.Companion.OUTLET_SALES_AREA
import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity.Companion.OUTLET_SECONDARY_MAIL
import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity.Companion.OUTLET_SECONDARY_MOBILE
import java.io.File


import javax.inject.Inject

class EditOutletActivity : BaseActivity<ActivityEditOutletBinding, OutletsViewModel>(R.layout.activity_edit_outlet),
    OnMapReadyCallback {

    companion object{
        const val REQUEST_UPGRADE_GPS_SETTINGS = 120
        private const val PERMISSION_REQUEST_LOCATION = 243
        const val REQUEST_UPDATE_GPS_SETTINGS_MANUALLY = 121
        private const val PERMISSION_REQUEST_STORAGE = 233
        private const val BUNDLE_EXTRA_LOCATION_LAT = "lat"
        private const val BUNDLE_IS_REQUESTING_LOCATION = "requestingLocation"
        private const val BUNDLE_EXTRA_LOCATION_LONG = "long"
        private const val BUNDLE_EXTRA_OUTLET_IMAGE_PATH = "outlet_image"
    }
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var appInfo : AppInfo


    var pic :String?=null
    var outletId : String?=null
    private var map: GoogleMap? = null
    private var userLocationCaptured = false
    private var isRequestingForLocation = false

    private var userLocation: Location = Location(
        latitude = 0.0,
        longitude = 0.0
    )

    private var imageResult: File?= null
    private var imagesMap: MutableMap<Int, Result> = mutableMapOf()

    private var currentlyClickingImageForIndex: Int = -1

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: OutletsViewModel by viewModels { viewModelFactory }

    private val locationHelper: LocationHelper by lazy {
        LocationHelper(this)
            .apply {
                setRequiredGpsPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                setLocationCallback(locationCallback)
                init()
            }
    }
    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(location: LocationResult?) {
            if (location == null || map == null)
                return

            userLocation = Location(
                location.lastLocation.latitude,
                location.lastLocation.longitude
            )
            showLocationOnMap(userLocation)
            if (location != null && map != null) {
                viewBinding.refreshProgressbar.setVisibilityGone()
            } else {
                viewBinding.refreshProgressbar.setVisibilityVisible()
            }
        }
    }
    private fun showLocationOnMap(location: Location) {

        map?.let {

            map?.clear()
            val newLocation = LatLng(
                location.latitude,
                location.longitude
            )
            map?.addMarker(MarkerOptions().position(newLocation).title("Your Location"))

            val cameraPosition = CameraPosition.Builder()
                .target(newLocation)
                .zoom(15f)
                .build()
            map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }
    private val imageCallback = ImageCallback { _, result, error ->

        if (result?.imagePath != null) {
            if (currentlyClickingImageForIndex == -1)
                return@ImageCallback

            imagesMap.put(currentlyClickingImageForIndex, result)
            when (currentlyClickingImageForIndex) {
                0 ->viewBinding.outletImage.imageView.load(result.bitmap)
                else -> {
                }
            }
//            imageResult = File(result.imagePath)
//            viewBinding.outletImage.imageView.load(result.bitmap)
//            imageLoader.loadImage(imageView, result.bitmap)

        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_UPGRADE_GPS_SETTINGS -> {

                if (resultCode == Activity.RESULT_OK) {
                    locationHelper.startLocationUpdates()
                } else if (resultCode == Activity.RESULT_CANCELED)
                    showRedirectToGpsPageDialog()

            }
            REQUEST_UPDATE_GPS_SETTINGS_MANUALLY -> {
                locationHelper.startLocationUpdates()
            }
            CameraIntegrator.REQUEST_IMAGE_CAPTURE -> {

                if (resultCode == Activity.RESULT_OK)
                    cameraIntegrator.parseResults(requestCode, resultCode, data, imageCallback)
            }

        }
    }
    private val cameraIntegrator: CameraIntegrator by lazy {
        CameraIntegrator(this)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setPublicDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.editOutletToolbar.setOnClickListener {
            onBackPressed()
        }
        viewBinding.getCurrentLocation.setOnClickListener {

            viewBinding.mapHolder.setVisibilityVisible()
            viewBinding.getCurrentLocation.setVisibilityGone()
            Handler().postDelayed({

                val mapFragment =
                    supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)


                if (locationPermissions())
                    checkForGpsStatus()
                else
                    requestLocationPermissions()

            }, 300)
        }


        outletId = intent.getStringExtra(OutletDetalisActivity.OUTLET_ID)
        val outletName = intent.getStringExtra(OUTLET_NAME)
       val customerCode = intent.getStringExtra(OUTLET_CUSTOMER_CODE)
        val regional = intent.getStringExtra(OUTLET_REGIONAL_OFFICE)
        val sale = intent.getStringExtra(OUTLET_SALES_AREA)
        val secondaryMail = intent.getStringExtra(OUTLET_SECONDARY_MAIL)
        val secondaryMobile = intent.getStringExtra(OUTLET_SECONDARY_MOBILE)
        pic = intent.getStringExtra(OUTLET_IMAGE)
        viewBinding.outletNameET.setText("${outletName} - ${customerCode}")
        viewBinding.outletNameET.setTextColor(Color.parseColor("#cccccc"))
//        viewBinding.outletcodeET.setText(customerCode)
//        viewBinding.outletcodeET.setTextColor(Color.parseColor("#cccccc"))
        viewBinding.regionalET.setText(regional)
        viewBinding.regionalET.setTextColor(Color.parseColor("#cccccc"))
        viewBinding.saleET.setText(sale)
        viewBinding.saleET.setTextColor(Color.parseColor("#cccccc"))
        viewBinding.secondaryMailEt.setText(secondaryMail)
        viewBinding.secondaryMobileET.setText(secondaryMobile)
        if (pic != null){
            viewBinding.outletImage.imageView.load(appInfo.getFullAttachmentUrl(pic!!))
        }else{
            viewBinding.outletImage.imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_default_image,null))
        }
        viewBinding.outletImage.clickImageLabel.setOnClickListener(ClickOutletImageOnClickListener())


        viewBinding.uploadBtn.setOnClickListener {
            validateAndUploadEditOutletData()
        }
    }
//    viewBinding.outletImage.clickImageLabel.setOnClickListener {
//
//
//    }

    private inner class ClickOutletImageOnClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val clickedView = v ?: return
            currentlyClickingImageForIndex = when (clickedView.id) {
                R.id.clickImageLabel -> 0

                else -> -1
            }
            if (storagePermissions())
                cameraIntegrator.initiateCapture()
            else
                requestStoragePermissions()
        }

    }

    private fun validateAndUploadEditOutletData() {
        if (userLocation.latitude == 0.0) {
            showInformationDialog("Location not captured yet, retry when location is updated on map")
            return
        }
//        if (viewBinding.secondaryMailEt.text.isNullOrBlank()) {
//            showInformationDialog("Please Enter Secondary Mail")
//            return
//        }

//        if (imageResult == null) {
//            showInformationDialog("Please Capture Edit Outlet Image First")
//            return
//        }
//        if (viewBinding.secondaryMobileET.text.isNullOrBlank()) {
//            showInformationDialog("Please Enter Secondary Mobile")
//            return
//        }

//        if (viewBinding.secondaryMobileET.text.toString().length > 10 || viewBinding.secondaryMobileET.text.toString().length < 10) {
//            showInformationDialog("Please enter 10 digit mobile number ")
//            return
//        }


        viewModel.upDateEditOutletData(
                outletid = outletId,
                latitude = userLocation.latitude,
                longitude = userLocation.longitude,
                images = imagesMap.values.map { it.imagePath!! },
                secondarymobile = viewBinding.secondaryMobileET.text.toString(),
                secondarymail = viewBinding.secondaryMailEt.text.toString(),
                pic = pic
        )
    }

    private fun locationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    this,ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_REQUEST_LOCATION
        )
    }
    private fun checkForGpsStatus() {
        locationHelper.checkForGpsSettings(object : GpsSettingsCheckCallback {

            override fun requiredGpsSettingAreUnAvailable(status: ResolvableApiException?) {
                status?.startResolutionForResult(
                    this@EditOutletActivity,
                    REQUEST_UPGRADE_GPS_SETTINGS
                )
            }

            override fun requiredGpsSettingAreAvailable() {
                locationHelper.startLocationUpdates()
            }

            override fun gpsSettingsNotAvailable() {
                showRedirectToGpsPageDialog()
            }
        })
    }

    private fun showRedirectToGpsPageDialog() {

        showNonCancellableMessageDialog(
            this,
            getString(R.string.turn_on_gps_message),
            object : QuickAlertDialog.OnClickListener {
                override fun okButtonClicked() {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(
                        intent,
                        REQUEST_UPDATE_GPS_SETTINGS_MANUALLY
                    )
                }
            })
    }


    override fun onInitViewModel() {
        viewModel.uploadEditOutletDataState.observe(this, Observer {
            when (it) {
                UploadEditOutletState.EditOutletDataLoading ->{
                    viewBinding.refreshProgressbar.setVisibilityVisible()
                }
                is UploadEditOutletState.OutletDataUpdated->{
                    viewBinding.editoutlet.setVisibilityGone()
                    Toast.makeText(this, "Edit Outlet Data UpDated Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,OutletDetalisActivity::class.java)
                    intent.putExtra(OutletDetalisActivity.OUTLET_ID,outletId)
                    startActivity(intent)
                    finish()
                }
                is UploadEditOutletState.ErrorInUpDateEditOutletData->{
                    viewBinding.refreshProgressbar.setVisibilityGone()
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Unable to upDate Data")
                        .setMessage("Unable to update Edit Outlet Data, ${it.error}")
                        .setPositiveButton("Okay") { _, _ -> }
                        .show()
                }
            }
        })

    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {

            if (it.getDouble(BUNDLE_EXTRA_LOCATION_LAT, 0.0) != 0.0) {

                userLocation = Location(
                    latitude = it.getDouble(BUNDLE_EXTRA_LOCATION_LAT, 0.0),
                    longitude = it.getDouble(BUNDLE_EXTRA_LOCATION_LONG, 0.0)
                )
                showLocationOnMap(userLocation)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble(BUNDLE_EXTRA_LOCATION_LAT, userLocation.latitude)
        outState.putDouble(BUNDLE_EXTRA_LOCATION_LONG, userLocation.longitude)

        outState.putBoolean(BUNDLE_IS_REQUESTING_LOCATION, isRequestingForLocation)
        cameraIntegrator.saveState(outState)

    }
    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        savedInstanceState?.let {

            if (it.getDouble(BUNDLE_EXTRA_LOCATION_LAT, 0.0) != 0.0) {

                userLocation = Location(
                    latitude = it.getDouble(BUNDLE_EXTRA_LOCATION_LAT, 0.0),
                    longitude = it.getDouble(BUNDLE_EXTRA_LOCATION_LONG, 0.0)
                )
                showLocationOnMap(userLocation)
            }

            isRequestingForLocation = it.getBoolean(BUNDLE_IS_REQUESTING_LOCATION, false)
            cameraIntegrator.restoreState(it)


        }


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntegrator.initiateCapture()
                }
            }
            PERMISSION_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkForGpsStatus()
                }
            }
        }
    }
    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_STORAGE
        )
    }
    private fun storagePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()

        if (!userLocationCaptured)
            locationHelper.startLocationUpdates()

        viewBinding.mapEditOutlet.locationRefreshingProgressbar.visibility = View.GONE

    }

    override fun onPause() {
        super.onPause()
        locationHelper.stopLocationUpdates()
    }
    override fun onMapReady(map: GoogleMap?) {
        this.map = map

        if (userLocation.latitude != 0.0)
            showLocationOnMap(userLocation)
    }
}


