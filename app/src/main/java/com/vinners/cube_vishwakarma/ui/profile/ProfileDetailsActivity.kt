package com.vinners.cube_vishwakarma.ui.profile

import android.Manifest
import android.annotation.TargetApi
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import coil.api.load
import com.jsibbold.zoomage.ZoomageView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityProfileDetailsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import java.io.File
import javax.inject.Inject

class ProfileDetailsActivity : BaseActivity<ActivityProfileDetailsBinding, ProfileActivityViewModel>(R.layout.activity_profile_details) {

    companion object{
        private const val PERMISSION_REQUEST_STORAGE = 233
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
        const val REFRESH_DATA = "refresh_profile"

    }
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    var personalDoc:Boolean = false
    var bankDetail:Boolean= false
    var otherDetail : Boolean= false

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: ProfileActivityViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.profileToolbar.setNavigationOnClickListener {
            onBackPressed()
        }



    }

    override fun onInitViewModel() {

            viewModel.profile.observe(this, Observer {

                if (it.aadhaarno == null || it.pan == null || it.voterid == null || it.dlnumber == null){
                    viewBinding.adharno.setText("Aadhar no : NA")
                    viewBinding.panNo.setText("Pan card no : NA")
                    viewBinding.voterid.setText("Voter Id : NA")
                    viewBinding.drivinglicence.setText("Driving Licence no : NA")
                }else{
                    viewBinding.adharno.setText("Aadhar no : ${it.aadhaarno}")
                    viewBinding.panNo.setText("Pan card no : ${it.pan}")
                    viewBinding.voterid.setText("Voter Id : ${it.voterid}")
                    viewBinding.drivinglicenceno.setText("Driving Licence no : ${it.dlnumber}")
                }

                    if (it.panpic.isNullOrEmpty().not())
                        viewBinding.pancardpic.imageView.load(appInfo.getFullAttachmentUrl(it.panpic!!))
                    else
                        viewBinding.pancardpic.imageView.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                getResources(),
                                R.drawable.ic_nodocuploaded,
                                null
                            )
                        )

                    if (it.dlpic.isNullOrEmpty().not())
                        viewBinding.drivingImage.imageView.load(appInfo.getFullAttachmentUrl(it.dlpic!!))
                    else
                        viewBinding.drivingImage.imageView.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                getResources(),
                                R.drawable.ic_nodocuploaded,
                                null
                            )
                        )

                    adharPicAndVoiterIdPic(it)
                openImageDialog(it)

            })
            viewModel.initViewModel()



    }

    private fun openImageDialog(image: Profile?) {
        viewBinding.pancardpic.imageView.setOnClickListener {
            val imageUrl : String = image?.panpic.toString()
            imageOpenDialog(imageUrl)
        }
        viewBinding.drivingImage.imageView.setOnClickListener {
            val imageUrl : String = image?.dlpic.toString()
            imageOpenDialog(imageUrl)
        }

    }

    private fun adharPicAndVoiterIdPic(it: Profile) {
        val picArray = it.aadhaarpic?.trim()?.split(",")

        if (picArray != null && picArray.size != 0 ) {

            for (i in 0 until picArray.size) {
                if (i == 0) {
                    viewBinding.adharpicfront.imageView.load(appInfo.getFullAttachmentUrl(picArray[i]))
                    viewBinding.adharpicfront.imageView.setOnClickListener{
                        val imageUrl :String = picArray[i]
                        imageOpenDialog(imageUrl)
                }
                } else if (i == 1) {
                    viewBinding.adharpicback.imageView.load(appInfo.getFullAttachmentUrl(picArray[i]))
                    viewBinding.adharpicback.imageView.setOnClickListener{
                        val imageUrl :String = picArray[i]
                        imageOpenDialog(imageUrl)
                    }
                }
            }
        }
        val voterIdPicArray = it.voteridpic?.trim()?.split(",")
        if (voterIdPicArray != null && voterIdPicArray.size != 0 ) {

            for (i in 0 until voterIdPicArray.size) {
                if (i == 0) {
                    viewBinding.voterpicfront.imageView.load(appInfo.getFullAttachmentUrl(voterIdPicArray[i]))
                    viewBinding.voterpicfront.imageView.setOnClickListener{
                        val imageUrl :String = voterIdPicArray[i]
                        imageOpenDialog(imageUrl)

                }
                } else if (i == 1) {
                    viewBinding.voterpicback.imageView.load(appInfo.getFullAttachmentUrl(voterIdPicArray[i]))
                    viewBinding.voterpicback.imageView.setOnClickListener{
                        val imageUrl :String = voterIdPicArray[i]
                        imageOpenDialog(imageUrl)
                }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions(url: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos from the Web.")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            // Permission has already been granted
            downloadImage(url)
        }
    }

    private var msg: String? = ""
    private var lastMsg = ""

    private fun downloadImage(url: String) {
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {
                    this.runOnUiThread {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
    }

    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
//            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> ""
        }
        return msg
    }

    private fun imageOpenDialog(imageUrl: String){
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.big_picture_image)
        val downloadButton = dialog.findViewById<Button>(R.id.downloadButton)
        downloadButton.setBackgroundResource(R.drawable.ic_download)
        downloadButton.setOnClickListener { view ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val fullImagePath = appInfo.getFullAttachmentUrl(imageUrl)
                askPermissions(fullImagePath)

            } else {
                val fullImagePath = appInfo.getFullAttachmentUrl(imageUrl)
                downloadImage(fullImagePath)
            }
        }
        val close_btn = dialog.findViewById<Button>(R.id.close_image)
        close_btn.setBackgroundResource(R.drawable.cross)
        close_btn.setOnClickListener { view ->
            dialog.dismiss()

        }
        val fullPic = dialog.findViewById<ZoomageView>(R.id.big_picture)
        fullPic.load(appInfo.getFullAttachmentUrl(imageUrl))


        dialog.window!!.attributes.windowAnimations = R.style.MyAlertDialogStyle
        dialog.show()
    }

}