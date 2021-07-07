package com.vinners.cube_vishwakarma.ui.outlets

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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
import com.himanshu.cameraintegrator.Result
import com.himanshu.cameraintegrator.integrator.CameraIntegrator
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.QuickAlertDialog
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.location.GpsSettingsCheckCallback
import com.vinners.cube_vishwakarma.core.location.LocationHelper
import com.vinners.cube_vishwakarma.data.models.Location
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityEditGpsLocationBinding
import com.vinners.cube_vishwakarma.databinding.ActivityEditOutletBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import java.io.File
import javax.inject.Inject

class EditGpsLocationActivity : BaseActivity<ActivityEditGpsLocationBinding, OutletsViewModel>(R.layout.activity_edit_gps_location),
    OnMapReadyCallback {
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    var pic :String?=null
    private var imagesMap: MutableMap<Int, Result> = mutableMapOf()
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
    lateinit var appInfo : AppInfo
    var outletId : String?=null

    private var map: GoogleMap? = null
    private var userLocationCaptured = false
    private var isRequestingForLocation = false

    private var userLocation: Location = Location(
        latitude = 0.0,
        longitude = 0.0
    )




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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            EditOutletActivity.REQUEST_UPGRADE_GPS_SETTINGS -> {

                if (resultCode == Activity.RESULT_OK) {
                    locationHelper.startLocationUpdates()
                } else if (resultCode == Activity.RESULT_CANCELED)
                    showRedirectToGpsPageDialog()

            }
            EditOutletActivity.REQUEST_UPDATE_GPS_SETTINGS_MANUALLY -> {
                locationHelper.startLocationUpdates()
            }


        }
    }

    private fun showRedirectToGpsPageDialog() {

        QuickAlertDialog.showNonCancellableMessageDialog(
            this,
            getString(R.string.turn_on_gps_message),
            object : QuickAlertDialog.OnClickListener {
                override fun okButtonClicked() {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(
                        intent,
                        EditOutletActivity.REQUEST_UPDATE_GPS_SETTINGS_MANUALLY
                    )
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
//        cameraIntegrator.saveState(outState)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
//            PERMISSION_REQUEST_STORAGE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    cameraIntegrator.initiateCapture()
//                }
//            }
           PERMISSION_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkForGpsStatus()
                }
            }
        }
    }

    private fun checkForGpsStatus() {
        locationHelper.checkForGpsSettings(object : GpsSettingsCheckCallback {

            override fun requiredGpsSettingAreUnAvailable(status: ResolvableApiException?) {
                status?.startResolutionForResult(
                    this@EditGpsLocationActivity,
                    EditOutletActivity.REQUEST_UPGRADE_GPS_SETTINGS
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
//            cameraIntegrator.restoreState(it)


        }


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

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }

    override fun onInitDataBinding() {
        outletId = intent.getStringExtra(OutletDetalisActivity.OUTLET_ID)
        val outletName = intent.getStringExtra(OutletDetalisActivity.OUTLET_NAME)
        val location = intent.getStringExtra(OutletDetalisActivity.OUTLET_LOCATION)
        val regional = intent.getStringExtra(OutletDetalisActivity.OUTLET_REGIONAL_OFFICE)
        val address = intent.getStringExtra(OutletDetalisActivity.OUTLET_ADDRESS)
        val customerCode = intent.getStringExtra(OutletDetalisActivity.OUTLET_CUSTOMER_CODE)
        val sale = intent.getStringExtra(OutletDetalisActivity.OUTLET_SALES_AREA)
        viewBinding.editOutletgpsToolbar.setOnClickListener {
            onBackPressed()
        }
        Handler().postDelayed({

            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)


            if (locationPermissions())
                checkForGpsStatus()
            else
                requestLocationPermissions()

        }, 300)

        viewBinding.outletNameET.setText("${outletName} - ${customerCode}")
        viewBinding.outletNameET.setTextColor(Color.parseColor("#cccccc"))
//        viewBinding.outletcodeET.setText(customerCode)
//        viewBinding.outletcodeET.setTextColor(Color.parseColor("#cccccc"))
        viewBinding.regionalET.setText(regional)
        viewBinding.regionalET.setTextColor(Color.parseColor("#cccccc"))
        viewBinding.saleET.setText(sale)
        viewBinding.saleET.setTextColor(Color.parseColor("#cccccc"))
        viewBinding.locationEt.setText(location)
        viewBinding.locationEt.setTextColor(Color.parseColor("#cccccc"))
        viewBinding.addressET.setText(address)
        viewBinding.addressET.setTextColor(Color.parseColor("#cccccc"))
        viewBinding.uploadBtn.setOnClickListener {
            validateAndUploadEditOutletData()
        }
    }


    private fun validateAndUploadEditOutletData() {
        if (userLocation.latitude == 0.0) {
            showInformationDialog("Location not captured yet, retry when location is updated on map")
            return
        }




        viewModel.editOutletGps(
            outletid = outletId,
            latitude = userLocation.latitude,
            longitude = userLocation.longitude
        )
    }

    private fun locationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
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

    override fun onInitViewModel() {
        viewModel.uploadEditOutletGPSDataState.observe(this, Observer {
            when (it) {
                UploadEditOutletGPSState.EditOutletDataGPSLoading ->{
                    viewBinding.refreshProgressbar.setVisibilityVisible()
                }
                is UploadEditOutletGPSState.OutletDataGPSUpdated->{
                    viewBinding.editoutlet.setVisibilityGone()
                    Toast.makeText(this, "Edit Outlet Location UpDated Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,OutletDetalisActivity::class.java)
                    intent.putExtra(OutletDetalisActivity.OUTLET_ID,outletId)
                    startActivity(intent)
                    finish()
                }
                is UploadEditOutletGPSState.ErrorInUpDateEditOutletGPSData->{
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
}