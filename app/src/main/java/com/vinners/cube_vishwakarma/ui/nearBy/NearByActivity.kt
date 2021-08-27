package com.vinners.cube_vishwakarma.ui.nearBy

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.*
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.QuickAlertDialog
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.location.GpsSettingsCheckCallback
import com.vinners.cube_vishwakarma.core.location.LocationHelper
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.Location
import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponseItem
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityNearByBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.dashboard.MainActivity
import com.vinners.cube_vishwakarma.ui.outlets.EditOutletActivity
import java.util.*
import javax.inject.Inject


class NearByActivity : BaseActivity<ActivityNearByBinding, NearByViewModel>(R.layout.activity_near_by),
    OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks{

    companion object{
        const val REQUEST_UPGRADE_GPS_SETTINGS = 120
        private const val PERMISSION_REQUEST_LOCATION = 243
        const val REQUEST_UPDATE_GPS_SETTINGS_MANUALLY = 121

        private const val BUNDLE_EXTRA_LOCATION_LAT = "lat"
        private const val BUNDLE_IS_REQUESTING_LOCATION = "requestingLocation"
        private const val BUNDLE_EXTRA_LOCATION_LONG = "long"

        const val OUTLET_BUTTON = "outlets"
        const val COMPLAINTS_BUTTON = "complaints"

    }
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: NearByViewModel by viewModels { viewModelFactory }

    private var map: GoogleMap? = null

    val areaOfIinterest : android.location.Location? = null
    val currentPosition :android.location.Location? = null
    var mGoogleApiClient: GoogleApiClient? = null
    private var userLocationCaptured = false
    private var isRequestingForLocation = false
    var lat:Double? = null
    var long:Double? = null
    val latLonglist = mutableListOf<LatLng>()
    var listMapLocation : LatLng? = null
    private var currentlyClickingButtonType: String = "none"
     private var range:String? = null
     var defaultRange:String? = null

    val locationlist = mutableListOf<NearByResponseItem>()

//    var markers = ArrayList<Marker>()
    private var userLocation: Location = Location(
    latitude = 0.0,
    longitude = 0.0
)

    private var destinationLocation: Location = Location(
        latitude = 0.0,
        longitude = 0.0
    )
    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }
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

            if (locationlist.isEmpty().not()) {
//                showNearByOutlets(map)
            }else {
                userLocation = Location(
                    location.lastLocation.latitude,
                    location.lastLocation.longitude
                )
                showLocationOnMap(userLocation)
            }
            if (location.equals(null).not() && map != null) {
                viewBinding.progressBar.setVisibilityGone()
                viewBinding.outletDataContainer.setVisibilityVisible()
                viewBinding.btnContainer.setVisibilityVisible()

            } else {
                viewBinding.progressBar.setVisibilityVisible()
                viewBinding.outletDataContainer.setVisibilityGone()
                viewBinding.btnContainer.setVisibilityGone()
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
            REQUEST_UPGRADE_GPS_SETTINGS -> {

                if (resultCode == Activity.RESULT_OK) {
                    locationHelper.startLocationUpdates()
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    showRedirectToGpsPageDialog()
                }

            }
            REQUEST_UPDATE_GPS_SETTINGS_MANUALLY -> {
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


    override fun onInitDataBinding() {

        viewBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        //            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
//
//            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
////            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show()
//            } else {
//                showGPSDisabledAlertToUser()
//            }
        Handler().postDelayed({

            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)


            if (locationPermissions())
                checkForGpsStatus()
            else
                requestLocationPermissions()

        }, 300)
        defaultRange = "${viewBinding.seekBar.getProgress()}"
        Log.d("akjl", defaultRange)
        viewBinding.seekBarText.setText("${viewBinding.seekBar.getProgress()}Km")
        val maxSizePoint = Point()
        windowManager.defaultDisplay.getSize(maxSizePoint)
        val maxX = maxSizePoint.x

        viewBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                val int =
                    (progress * (viewBinding.seekBar.getWidth() - 2 * viewBinding.seekBar.getThumbOffset())) / viewBinding.seekBar.getMax();
                viewBinding.seekBarText.setText("${progress}Km")
//                range = "${progress}"

                var value =
                    viewBinding.seekBar.getX() + int + viewBinding.seekBar.getThumbOffset() / 2
                viewBinding.seekBarText.setX(value)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                range = "${seekBar.progress}"
                Log.d("jjhja", range)
            }
        })

        viewBinding.imageOutletBtn.tag = OUTLET_BUTTON
        viewBinding.imageOutletBtn.setOnClickListener { view ->
            setButtonTag(view)
            viewBinding.imageOutletBtn.setEnabled(false)
            viewBinding.imageComplaintBtn.setEnabled(true)
            viewBinding.flagContainer.setVisibilityVisible()
            map?.clear()
            viewBinding.seekBar.setProgress(25)
            if (userLocation.latitude != 0.0 && map != null) {
                viewBinding.loadBtnContainer.setVisibilityVisible()
                viewBinding.refreshProgressbar.setVisibilityGone()
                viewModel.getNearByOutletByMap(
                    latitude = userLocation.latitude,
                    longitude = userLocation.longitude,
                    range = defaultRange!!,
                    type = currentlyClickingButtonType
                )
            } else {

                viewBinding.refreshProgressbar.setVisibilityVisible()
            }

            Log.d("akjl", defaultRange)
            Log.d("kjla", currentlyClickingButtonType)

        }
        viewBinding.imageComplaintBtn.tag = COMPLAINTS_BUTTON
        viewBinding.imageComplaintBtn.setOnClickListener { view ->
            setButtonTag(view)
            viewBinding.imageOutletBtn.setEnabled(true)
            viewBinding.imageComplaintBtn.setEnabled(false)
            viewBinding.mapContainer.setVisibilityVisible()
            viewBinding.flagContainer.setVisibilityVisible()
            map?.clear()
            viewBinding.seekBar.setProgress(25)
            if (userLocation.latitude != 0.0 && map != null) {
                viewBinding.refreshProgressbar.setVisibilityGone()
                viewBinding.loadBtnContainer.setVisibilityVisible()


                viewModel.getNearByOutletByMap(
                    latitude = userLocation.latitude,
                    longitude = userLocation.longitude,
                    range = defaultRange!!,
                    type = currentlyClickingButtonType
                )

            } else {
                viewBinding.refreshProgressbar.setVisibilityVisible()
            }
            Log.d("akjl", defaultRange)
            Log.d("kjla", currentlyClickingButtonType)



        }
        viewBinding.loadBtn.setOnClickListener {

            validateAndUploadNearByMap()

        }
    }

    private fun setButtonTag(view: View) {
        currentlyClickingButtonType = view.tag.toString()

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

    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
            .setCancelable(false)
            .setPositiveButton("Goto Settings",
                DialogInterface.OnClickListener { dialog, id ->
                    val callGPSSettingIntent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    startActivity(callGPSSettingIntent)
                })
        alertDialogBuilder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                dialog.cancel()
            })
        val alert: AlertDialog = alertDialogBuilder.create()
        alert.show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkForGpsStatus()
                }
            }
        }
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


    }


    private fun checkForGpsStatus() {
        locationHelper.checkForGpsSettings(object : GpsSettingsCheckCallback {

            override fun requiredGpsSettingAreUnAvailable(status: ResolvableApiException?) {
                status?.startResolutionForResult(
                    this@NearByActivity,
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

      viewBinding.refreshProgressbar.visibility = View.GONE

    }

    override fun onPause() {
        super.onPause()
        locationHelper.stopLocationUpdates()
    }


    override fun onInitViewModel() {

        viewModel.nearbyMapListState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.loadBtn.showProgress {
                        buttonText =
                            getString(com.vinners.cube_vishwakarma.feature_auth.R.string.loading)
                        progressColor = Color.WHITE
                    }
                    viewBinding.loadBtn.isEnabled = false
                    viewBinding.refreshProgressbar.setVisibilityVisible()
                }
                is Lce.Content -> {
                    viewBinding.refreshProgressbar.setVisibilityGone()
                    viewBinding.loadBtn.hideProgress(
                        "Load"
                    )
                    viewBinding.loadBtn.isEnabled = true
                    locationlist.clear()
                    locationlist.addAll(it.content)
//                showLocationOnMap(userLocation)
                    showNearByOutlets()


//                    locationHelper.startLocationUpdates()
                }
                is Lce.Error -> {
                    viewBinding.loadBtn.isEnabled = true
                    viewBinding.loadBtn.hideProgress(
                        "Load"
                    )
                    showInformationDialog(it.error)
                    viewBinding.refreshProgressbar.setVisibilityGone()
                }
            }
        })

    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map

        Log.d("jsahsag", locationlist.toString())
        if (locationlist.isEmpty().not()){
            showNearByOutlets()
        }else {
            if (userLocation.latitude != 0.0)
                showLocationOnMap(userLocation)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }



    }
    private fun validateAndUploadNearByMap() {
        if (userLocation.latitude == 0.0) {
            showInformationDialog("Location not captured yet, retry when location is updated on map")
            return
        }
        if (range == null){
           range = defaultRange
            return
        }

        Log.d("tagnsbb", range)

        viewModel.getNearByOutletByMap(
            latitude = userLocation.latitude,
            longitude = userLocation.longitude,
            range = range.toString(),
            type = currentlyClickingButtonType

        )

    }

    private fun showNearByOutlets() {


        map?.let {
            map?.clear()
            Log.d("kjshjaskj", locationlist.size.toString())
            for (i in 0 until locationlist.size) {
                try {
                    val gps = locationlist[i].gps

                    val latLng = gps?.split(",")
                    val latitude: Double = latLng!!.get(0).toDouble()
                    val longitude = latLng[1].toDouble()
                    val current = LatLng(userLocation.latitude, userLocation.longitude)
                    Log.d("sjhJHJ", userLocation.latitude.toString())
                    map?.addMarker(MarkerOptions().position(current).title("Your Location"))

                    val destination = LatLng(latitude, longitude)
                    if (locationlist[i].workingcomplaint == false) {
                        if (locationlist[i].complaintid != null){
                        map?.addMarker(
                            MarkerOptions()
                                .title("${locationlist[i].name} - ${locationlist[i].customercode}")
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        resizeBitmap(
                                            R.drawable.redmarker,
                                            80,
                                            80
                                        )
                                    )
                                )
                                .position(destination)
                                .snippet(
                                    "RO : ${locationlist[i].regionaloffice}   "  +  "SA : ${locationlist[i].salesarea}" + '\n' +
                                            "${locationlist[i].complaintid}"
                                )
                        )
                        }else{
                            map?.addMarker(
                                MarkerOptions()
                                    .title("${locationlist[i].name} - ${locationlist[i].customercode}")
                                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap(R.drawable.redmarker, 80, 80)))
                                    .position(destination)
                                    .snippet(
                                        "RO : ${locationlist[i].regionaloffice}   "  +  "SA : ${locationlist[i].salesarea}"
                                    )
                            )
                        }
//                        map?.addMarker(MarkerOptions().title(locationlist[i].name)
//                            .draggable(true)
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                            .position(LatLng(latitude, longitude)))
                    } else {


                        map?.addMarker(
                            MarkerOptions()
                                .title("${locationlist[i].name}")
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        resizeBitmap(
                                            R.drawable.mapmarker,
                                            80,
                                            80
                                        )
                                    )
                                )
                                .position(destination)
                                .snippet(
                                    "RO : ${locationlist[i].regionaloffice}   "  +  "SA : ${locationlist[i].salesarea}" + '\n' +
                                            "${locationlist[i].complaintid}"
                                )
                        )
                    }
                    val builder = LatLngBounds.Builder()
                    builder.include(destination)
                    builder.include(current)
                    val bounds = builder.build()
                    map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100), 2000, null);

                  map?.setInfoWindowAdapter(object :InfoWindowAdapter{
                      override fun getInfoWindow(p0: Marker?): View? {
                          return null
                      }

                      override fun getInfoContents(marker: Marker): View {
                          val view: View = layoutInflater.inflate(R.layout.map_info_layout, null)

                          val title = view.findViewById<View>(R.id.title) as TextView
                          val ro = view.findViewById<View>(R.id.ro) as TextView
//                          val sal = view.findViewById<View>(R.id.sal) as TextView
//                          val complaintid = view.findViewById<View>(R.id.complaintid) as TextView

                          title.setText(marker.getTitle())
                          ro.setText(marker.getSnippet())
//                          sal.text = marker.snippet
//                          complaintid.text = marker.snippet

                          return view
                      }

                  })

                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
        }
    }
    fun resizeBitmap(drawableName: Int, width: Int, height: Int): Bitmap? {
        val imageBitmap = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                drawableName.toString(), "drawable",
                packageName
            )
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }


    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(
                intrinsicWidth,
                intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onLocationChanged(location: android.location.Location) {
//       if (location == null){
//       }else{
//           showLocationOnMap(userLocation)
//           showNearByOutlets()
//       }
    }

    override fun onConnected(p0: Bundle?) {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(1000)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this
        )
    }

    override fun onConnectionSuspended(p0: Int) {

    }


}