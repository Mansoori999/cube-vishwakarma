package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails


import android.Manifest
import android.annotation.TargetApi
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.Color.WHITE
import android.net.Uri
import android.os.*
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import coil.api.load
import com.himanshu.cameraintegrator.ImageCallback
import com.himanshu.cameraintegrator.ImagesSizes
import com.himanshu.cameraintegrator.Result
import com.himanshu.cameraintegrator.integrator.CameraIntegrator
import com.himanshu.cameraintegrator.integrator.GalleryIntegrator
import com.himanshu.cameraintegrator.storage.StorageMode
import com.jsibbold.zoomage.ZoomageView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.AppConstants
import com.vinners.cube_vishwakarma.core.DateTimeHelper
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.onItemSelected
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplainDetailsList
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityMyComplaintDetailsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequestView.ComplaintRequestViewActivity.Companion.COMPLAINT_REQUEST_STATUS
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequestView.ComplaintRequestViewActivity.Companion.COMPLAINT_REQUEST_VIEW
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel.AllComplaintFragmentViewModel
import java.io.File
import java.io.InputStream
import javax.inject.Inject


class MyComplaintDetailsActivity : BaseActivity<ActivityMyComplaintDetailsBinding, AllComplaintFragmentViewModel>(
        R.layout.activity_my_complaint_details
) {
    companion object{
        private const val PERMISSION_REQUEST_STORAGE = 233
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
        private const val MY_REQUEST_CODE_PERMISSION = 1000
        private const val MY_RESULT_CODE_FILECHOOSER = 2000
        private const val LOG_TAG = "filechooser"
        private const val LETTER_IMAGE = "letter_image"
        private const val MESUREMENT_IMAGE = "mesurment_image"
        private const val LAYOUT_IMAGE = "layout_image"
    }

    val REQUEST_GALLERY_PHOTO = 2
    @Inject
    lateinit var appInfo : AppInfo

    private lateinit var imageClickLabel : TextView
    private lateinit var ImageStoreView : ImageView

    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2

    var pic :String?=null


    private var imagesMap: MutableMap<Int, Result> = mutableMapOf()

    private var currentlyClickingImageForIndex: Int = -1

    private var currentlyClickingletterImageForIndex: Int = -1


    @Inject
    lateinit var viewModelFactory : LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    private val statusList = ArrayList<String>()

    private var id: String? = null

    private var enabledComplaintRequest : Boolean = false

    private var complaintRequestStatus : String? = null

    private var detailsId: String? = null
    private var status : String ? = null
     private var statusremarks : String? = null
    private var supervisorId: String? = null
    private var foremanId : String ? = null
    private var endUserId : String? = null
    private var supervisor: String? = null
    private var foreman : String ? = null
    private var endUser : String? = null

    private var supervisorid:String? = null
    private var foremanid:String? = null
    private var enduserid:String? = null

    private lateinit var supervisorSpinner:Spinner
    private lateinit var foremanSpinner:Spinner
    private lateinit var enduserSpinner:Spinner
    private lateinit var errorText:TextView
    private lateinit var errorTextstatus:TextView
    var nameshowfirst:String = "Select EndUser"
    var endidshowfirst:String = "XX"
    var designationnameshowfirst:String = ""

    var nameSupervisorshowfirst:String = "Select Supervisor"
    var SupervisorIdShowfirst:String = "XX"
    var SupervisorDesignationnameshowfirst:String = ""

    var nameForemanshowfirst:String  = "Select Foreman"
    var foremanidshowfirst:String = "XX"
    var foremanDesignationnameshowfirst:String = ""


    private lateinit var statusValue : String
    private var complaintid : String? = null
    private var reasonEt:String?=null

    private lateinit var reason:EditText
    private lateinit var tv:TextView

    var ret:String? = null
    private lateinit var donecontainer:LinearLayout
    private lateinit var filename : TextView
    private lateinit var filename1 : TextView
    private lateinit var filename2 : TextView


    private lateinit var bytes:ByteArray
     var inputStream : InputStream? = null
    private lateinit var uri:Uri
    var filePath:String ? = null

    private lateinit var statusSpinner:Spinner

    lateinit var imageClick1:TextView
    lateinit var imageClick2:TextView
    lateinit var imageClick3:TextView

    lateinit var imageView1:ImageView
    lateinit var imageView2:ImageView
    lateinit var imageView3:ImageView


    private var currentlyClickingImageFor: String = ""
    private var imageResult: File? = null
    private var mesurmentImageResult: File? = null
    private var layoutImageResult: File? = null


    private var myFilePath :File? = null
    private var mesurmentFilePath: File? = null
    private var layoutFilePath: File? = null



    override val viewModel: AllComplaintFragmentViewModel by viewModels{ viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)

    }

private inner class ClickComplaintDoneLetterImageOnClickListener:View.OnClickListener{
    override fun onClick(v: View?) {
        val clickedView = v ?: return

    }
}
    private inner class ClickOutletImageOnClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val clickedView = v ?: return
            currentlyClickingImageForIndex = when (clickedView.id) {
                R.id.clickImageLabel2 -> 0
                R.id.clickImageLabel3 -> 1
                else -> -1
            }
            showCameraOptionDialog()
//            if (storagePermissions())
//                cameraIntegrator.initiateCapture()
//            else
//                requestStoragePermissions()
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


    private val imageCallback = ImageCallback { _, result, error ->

        if (result?.imagePath != null) {

            if (currentlyClickingImageFor.equals(LETTER_IMAGE)) {
                imageResult = File(result.imagePath)
                imageView1.load(result.bitmap)
                imageView1.setOnClickListener {
                    try {
                        val myIntent = Intent(Intent.ACTION_VIEW)
                        val file = File(result.imagePath)
                        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                        val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                        myIntent.setDataAndType(Uri.fromFile(file), mimetype)
                        startActivity(myIntent)
                    } catch (e: java.lang.Exception) {
                        val data = e.message
                    }
                }
            } else if (currentlyClickingImageFor.equals(MESUREMENT_IMAGE)) {
                mesurmentImageResult = File(result.imagePath)
                imageView2.load(result.bitmap)
                imageView2.setOnClickListener {
                    try {
                        val myIntent = Intent(Intent.ACTION_VIEW)
                        val file = File(result.imagePath)
                        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                        val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                        myIntent.setDataAndType(Uri.fromFile(file), mimetype)
                        startActivity(myIntent)
                    } catch (e: java.lang.Exception) {
                        val data = e.message
                    }
                }
            }else if (currentlyClickingImageFor.equals(LAYOUT_IMAGE)) {
                layoutImageResult = File(result.imagePath)
                imageView3.load(result.bitmap)
                imageView3.setOnClickListener {
                    try {
                        val myIntent = Intent(Intent.ACTION_VIEW)
                        val file = File(result.imagePath)
                        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                        val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                        myIntent.setDataAndType(Uri.fromFile(file), mimetype)
                        startActivity(myIntent)
                    } catch (e: java.lang.Exception) {
                        val data = e.message
                    }
                }
            }


        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cameraIntegrator.saveState(outState)

    }
    override fun onRestoreInstanceState(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        savedInstanceState?.let {

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
            MY_REQUEST_CODE_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    Log.i( LOG_TAG,"Permission granted!");
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
                    doBrowseFile();
                }
                // Cancelled or denied.
                else {
                    Log.i(LOG_TAG, "Permission denied!");
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CameraIntegrator.REQUEST_IMAGE_CAPTURE) {
            cameraIntegrator.parseResults(requestCode, resultCode, data, imageCallback)
        }else if (requestCode == GalleryIntegrator.REQUEST_IMAGE_PICK){
            galleryIntegrator.parseResults(requestCode, resultCode, data, imageCallback)
        }
        when(requestCode){
            MY_RESULT_CODE_FILECHOOSER -> {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        var uri = data.data
                        val uriString = uri.toString()
                        if (currentlyClickingImageFor.equals(LETTER_IMAGE)) {
                            filename.setVisibilityVisible()
                            val path = UtilsFile(this).getPath(uri!!)
                            try {
                                imageResult = File(path)
                                val url = appInfo.getFullAttachmentUrl(path)
                                imageView1.background = Color.WHITE.toDrawable()
                                imageView1.setImageDrawable(
                                        ResourcesCompat.getDrawable(
                                                this.getResources(),
                                                R.drawable.default_pdf,
                                                null
                                        )
                                )
                                imageView1.setOnClickListener {
                                    try {
                                        val myIntent = Intent(Intent.ACTION_VIEW)
                                        val file = File(path)
                                        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                                        val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                                        myIntent.setDataAndType(Uri.fromFile(file), mimetype)
                                        startActivity(myIntent)
                                    } catch (e: java.lang.Exception) {
                                        // TODO: handle exception
                                        val data = e.message
                                    }
                                }
                                filename.setOnClickListener {
                                    try {
                                        val myIntent = Intent(Intent.ACTION_VIEW)
                                        val file = File(path)
                                        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                                        val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                                        myIntent.setDataAndType(Uri.fromFile(file), mimetype)
                                        startActivity(myIntent)
                                    } catch (e: java.lang.Exception) {
                                        val data = e.message
                                    }
                                }
                            } catch (e: Exception) {
                                Log.d(LOG_TAG, "Error: $e")
                                Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
                            }

                            if (uriString.startsWith("content://")) {
                                var cursor: Cursor? = null
                                try {
                                    cursor = this.getContentResolver().query(uri, null, null, null, null)
                                    if (cursor != null && cursor.moveToFirst()) {
//                                        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//                                        val yourRealPath = cursor.getString(column_index)
//                                        imageResult = File(yourRealPath)
                                        filename.setText(cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)))
                                    }
                                } finally {
                                    cursor!!.close()
                                }
                            } else if (uriString.startsWith("file://")) {
                                filename.setText(imageResult!!.name)
                            }
                        } else if (currentlyClickingImageFor.equals(MESUREMENT_IMAGE)) {
                            filename1.setVisibilityVisible()
                            val path = UtilsFile(this).getPath(uri!!)
                            try {
                                mesurmentImageResult = File(path)
                                imageView2.background = Color.WHITE.toDrawable()
                                imageView2.setImageDrawable(
                                        ResourcesCompat.getDrawable(
                                                this.getResources(),
                                                R.drawable.default_pdf,
                                                null
                                        )
                                )
                                imageView2.setOnClickListener {
                                    try {
                                        val myIntent = Intent(Intent.ACTION_VIEW)
                                        val file = File(path)
                                        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                                        val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                                        myIntent.setDataAndType(Uri.fromFile(file), mimetype)
                                        startActivity(myIntent)
                                    } catch (e: java.lang.Exception) {
                                        // TODO: handle exception
                                        val data = e.message
                                    }
                                }
                                filename1.setOnClickListener {
                                    try {
                                        val myIntent = Intent(Intent.ACTION_VIEW)
                                        val file = File(path)
                                        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                                        val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                                        myIntent.setDataAndType(Uri.fromFile(file), mimetype)
                                        startActivity(myIntent)
                                    } catch (e: java.lang.Exception) {
                                        // TODO: handle exception
                                        val data = e.message
                                    }
                                }
                            } catch (e: Exception) {
                                Log.d(LOG_TAG, "Error: $e")
                                Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
                            }

                            if (uriString.startsWith("content://")) {
                                var cursor: Cursor? = null
                                try {
                                    cursor = this.getContentResolver().query(uri, null, null, null, null)
                                    if (cursor != null && cursor.moveToFirst()) {
                                        filename1.setText(
                                                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                        )
                                    }
                                } finally {
                                    cursor!!.close()
                                }
                            } else if (uriString.startsWith("file://")) {
                                filename1.setText(mesurmentFilePath!!.name)
                            }
                        } else if (currentlyClickingImageFor.equals(LAYOUT_IMAGE)) {
                            filename2.setVisibilityVisible()

                            val path = UtilsFile(this).getPath(uri!!)
                            try {
                                layoutImageResult = File(path)
                                imageView3.background = Color.WHITE.toDrawable()
                                imageView3.setImageDrawable(
                                        ResourcesCompat.getDrawable(
                                                this.getResources(),
                                                R.drawable.default_pdf,
                                                null
                                        )
                                )
                                imageView3.setOnClickListener {
                                    try {
                                        val myIntent = Intent(Intent.ACTION_VIEW)
                                        val file = File(path)
                                        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                                        val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                                        myIntent.setDataAndType(Uri.fromFile(file), mimetype)
                                        startActivity(myIntent)
                                    } catch (e: java.lang.Exception) {
                                        // TODO: handle exception
                                        val data = e.message
                                    }
                                }
                                filename2.setOnClickListener {
                                    try {
                                        val myIntent = Intent(Intent.ACTION_VIEW)
                                        val file = File(path)
                                        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
                                        val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                                        myIntent.setDataAndType(Uri.fromFile(file), mimetype)
                                        startActivity(myIntent)
                                    } catch (e: java.lang.Exception) {
                                        // TODO: handle exception
                                        val data = e.message
                                    }
                                }
                            } catch (e: Exception) {
                                Log.d(LOG_TAG, "Error: $e")
                                Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
                            }

                            if (uriString.startsWith("content://")) {
                                var cursor: Cursor? = null
                                try {
                                    cursor = this.getContentResolver().query(
                                            uri!!,
                                            null,
                                            null,
                                            null,
                                            null
                                    )
                                    if (cursor != null && cursor.moveToFirst()) {
                                        filename2.setText(
                                                cursor.getString(
                                                        cursor.getColumnIndex(
                                                                OpenableColumns.DISPLAY_NAME
                                                        )
                                                )
                                        )
                                    }
                                } finally {
                                    cursor!!.close()
                                }
                            } else if (uriString.startsWith("file://")) {
                                filename2.setText(layoutFilePath!!.name)
                            }
                        }
                    }

                }
            }
        }

    }



    private fun setValidationUpdate():Boolean {
        if  (reason.isVisible && reason.text.isNullOrBlank()){
//            showInformationDialog("Please Write Reason")
            errorTextstatus.setVisibilityVisible()
            errorTextstatus.setText("Please Write Reason")
            return false
        }
//        statusValue.equals("Select Status") &&
        if (statusSpinner.selectedItem.equals("Select Status")){
            errorTextstatus.setVisibilityVisible()
            errorTextstatus.setText("Please Select Status")
//            showInformationDialog("Please Select Status")
            return false
        }

        if (donecontainer.isVisible && imageResult == null){
            errorTextstatus.setVisibilityVisible()
            errorTextstatus.setText("Please Click Letter Pic OR Select File")
//            showInformationDialog("Please Click Letter Pic OR Select File")
            return false
        }

        return true

    }
    fun showCameraOptionDialog() {
        val optionsForDialog = arrayOf<CharSequence>(
                "Open Camera",
                "Select from Gallery",
                "Select File"
        )
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Select An Option")
        alertBuilder.setIcon(R.drawable.ic_camera)
        alertBuilder.setItems(optionsForDialog, DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
                2 -> openFile()
            }
        })
        alertBuilder.setNegativeButton("Cancel") { dialog12, _ -> dialog12.dismiss() }
        alertBuilder.show()
    }

    fun openCamera() {
        if (storagePermissions())
            cameraIntegrator.initiateCapture()
        else
            requestStoragePermissions()
//        try {
//            cameraIntegrator.initiateCapture()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    fun openGallery() {
        if (storagePermissions())
            galleryIntegrator.initiateImagePick()
        else
            requestStoragePermissions()
//        try {
//            galleryIntegrator.initiateImagePick()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }
    fun openFile() {

//        askPermissionAndBrowseFile()
        if (storagePermissions())
            doBrowseFile()
//            galleryIntegrator.initiateImagePick()
        else
            requestStoragePermissions()
//        try {
//            galleryIntegrator.initiateImagePick()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    private fun askPermissionAndBrowseFile() {
        // for permission to access External Storage.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Level 23

            // Check if we have Call permission
            val permisson = ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (permisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_REQUEST_CODE_PERMISSION
                )
                return
            }
        }
        doBrowseFile()

    }


    private fun doBrowseFile() {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
//        chooseFile.type = "*/*"
        chooseFile.setType("application/pdf");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(chooseFile, MY_RESULT_CODE_FILECHOOSER)

    }

    private val cameraIntegrator: CameraIntegrator by lazy {
        CameraIntegrator(this)
                .apply {
                    setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                    setPublicDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                    setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
                }
    }


    private val galleryIntegrator: GalleryIntegrator by lazy {
        GalleryIntegrator(this)
                .apply {
                    setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                    setPublicDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                    setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
                }
    }




    override fun onInitViewModel() {
        id.let { viewModel.getComplainDetails(it!!) }
        viewModel.complaintDetailsState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.loadingData.setVisibilityVisible()
                }
                is Lce.Content -> {
                    if (it.content != null) {
                        viewBinding.loadingData.setVisibilityGone()
                        viewBinding.date.text =
                                DateTimeHelper.getDDMMYYYYDateFromString(it.content.fordate!!)
                        viewBinding.complaintId.text = it.content.complaintid
                        viewBinding.work.text = it.content.work
                        viewBinding.type.text = it.content.type
                        viewBinding.outletName.text = it.content.outletname
                        viewBinding.regional.text = it.content.regionaloffice
                        viewBinding.sales.text = it.content.salesarea
                        viewBinding.district.text = it.content.district
                        viewBinding.letter.text = it.content.letterstatus

                        viewBinding.subadmin.text = it.content.subadmin
                        viewBinding.supervisor.text = it.content.supervisor
                        viewBinding.foreman.text = it.content.foreman
                        viewBinding.enduser.text = it.content.enduser
                        viewBinding.order.text = it.content.orderBy
                        viewBinding.remarks.text = it.content.remarks
                        setImageAndPdf(it.content)
                        if (enabledComplaintRequest == true) {
                            viewBinding.status.text = complaintRequestStatus
                        } else {
                            viewBinding.status.text = it.content.status
                            setChangeStatus(it.content)
                        }

                    } else {
                        viewBinding.loadingData.setVisibilityVisible()
                    }


                }
                is Lce.Error -> {

                    viewBinding.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)

                }

            }
        })

        viewModel.allocateUserListState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    var designation: String? = null
                    it.content.forEach {
                        designation = it.designation
                    }

                    val enduserData = it.content.filter {
                        it.designation!!.toLowerCase()
                                .equals("enduser") || it.designation!!.toLowerCase().equals(
                                "end user"
                        )
                    }.map {
                        AllocateUserData(
                                id = it.id!!,
                                name = it.name!!,
                                designation = it.designation!!
                        )
                    }.toMutableList()
                    setendUserTypeSpinner(enduserData)

                    val supervisorData =
                            it.content.filter { it.designation!!.toLowerCase().equals("supervisor") }
                                    .map {
                                        AllocateUserData(
                                                id = it.id!!,
                                                name = it.name!!,
                                                designation = it.designation!!
                                        )
                                    }.toMutableList()
                    supervisorTypeSpinner(supervisorData)


                    val foremanData =
                            it.content.filter { it.designation!!.toLowerCase().equals("foreman") }.map {
                                AllocateUserData(
                                        id = it.id!!,
                                        name = it.name!!,
                                        designation = it.designation!!
                                )
                            }.toMutableList()
                    foremanTypeSpinner(foremanData)


                }
                is Lce.Error -> {

                }

            }
        })

        viewModel.upDateListState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.loadingData.setVisibilityVisible()
                    viewBinding.detailScreen.setVisibilityGone()

                }
                is Lce.Content -> {
                    if (status!!.equals("Working")) {
                        Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show()

                        id.let { viewModel.getComplainDetails(it!!) }
                        viewModel.complaintDetailsState.observe(this, Observer {
                            when (it) {
                                Lce.Loading -> {
                                    viewBinding.loadingData.setVisibilityVisible()
                                }
                                is Lce.Content -> {
                                    if (it.content != null) {
                                        viewBinding.loadingData.setVisibilityGone()
                                        viewBinding.date.text =
                                                DateTimeHelper.getDDMMYYYYDateFromString(
                                                        it.content.fordate!!
                                                )
                                        viewBinding.complaintId.text = it.content.complaintid
                                        viewBinding.work.text = it.content.work
                                        viewBinding.type.text = it.content.type
                                        viewBinding.outletName.text = it.content.outletname
                                        viewBinding.regional.text = it.content.regionaloffice
                                        viewBinding.sales.text = it.content.salesarea
                                        viewBinding.district.text = it.content.district
                                        viewBinding.letter.text = it.content.letterstatus
                                        viewBinding.status.text = it.content.status
                                        viewBinding.subadmin.text = it.content.subadmin
                                        viewBinding.supervisor.text = it.content.supervisor
                                        viewBinding.foreman.text = it.content.foreman
                                        viewBinding.enduser.text = it.content.enduser
                                        viewBinding.order.text = it.content.orderBy
                                        viewBinding.remarks.text = it.content.remarks
                                        setImageAndPdf(it.content)
                                        setChangeStatus(it.content)

                                    } else {
                                        viewBinding.loadingData.setVisibilityVisible()
                                    }


                                }
                                is Lce.Error -> {

                                    viewBinding.loadingData.setVisibilityGone()
                                    showInformationDialog(it.error)

                                }

                            }
                        })
                        viewBinding.loadingData.setVisibilityGone()
                        viewBinding.detailScreen.setVisibilityVisible()
//                        getSupportFragmentManager().beginTransaction()
//                                .add(android.R.id.content, WorkingFragment.newInstance())
//                                .commit()
////                        startActivity(Intent(this, WorkingFragment::class.java))
//                        finish()
                    } else if (status!!.equals("Hold")) {
                        Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show()
                        id.let { viewModel.getComplainDetails(it!!) }
                        viewModel.complaintDetailsState.observe(this, Observer {
                            when (it) {
                                Lce.Loading -> {
                                    viewBinding.loadingData.setVisibilityVisible()
                                }
                                is Lce.Content -> {
                                    if (it.content != null) {
                                        viewBinding.loadingData.setVisibilityGone()
                                        viewBinding.date.text =
                                                DateTimeHelper.getDDMMYYYYDateFromString(
                                                        it.content.fordate!!
                                                )
                                        viewBinding.complaintId.text = it.content.complaintid
                                        viewBinding.work.text = it.content.work
                                        viewBinding.type.text = it.content.type
                                        viewBinding.outletName.text = it.content.outletname
                                        viewBinding.regional.text = it.content.regionaloffice
                                        viewBinding.sales.text = it.content.salesarea
                                        viewBinding.district.text = it.content.district
                                        viewBinding.letter.text = it.content.letterstatus
                                        viewBinding.status.text = it.content.status
                                        viewBinding.subadmin.text = it.content.subadmin
                                        viewBinding.supervisor.text = it.content.supervisor
                                        viewBinding.foreman.text = it.content.foreman
                                        viewBinding.enduser.text = it.content.enduser
                                        viewBinding.order.text = it.content.orderBy
                                        viewBinding.remarks.text = it.content.remarks
                                        setImageAndPdf(it.content)
                                        setChangeStatus(it.content)

                                    } else {
                                        viewBinding.loadingData.setVisibilityVisible()
                                    }


                                }
                                is Lce.Error -> {

                                    viewBinding.loadingData.setVisibilityGone()
                                    showInformationDialog(it.error)

                                }

                            }
                        })
                        viewBinding.loadingData.setVisibilityGone()
                        viewBinding.detailScreen.setVisibilityVisible()
//                        getSupportFragmentManager().beginTransaction()
//                                .add(android.R.id.content, HoldFragment.newInstance())
//                                .commit()
////                        startActivity(Intent(this, HoldFragment::class.java))
//                        finish()
                    } else if (status!!.equals("Due")) {
                        Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show()
                        id.let { viewModel.getComplainDetails(it!!) }
                        viewModel.complaintDetailsState.observe(this, Observer {
                            when (it) {
                                Lce.Loading -> {
                                    viewBinding.loadingData.setVisibilityVisible()
                                }
                                is Lce.Content -> {
                                    if (it.content != null) {
                                        viewBinding.loadingData.setVisibilityGone()
                                        viewBinding.date.text =
                                                DateTimeHelper.getDDMMYYYYDateFromString(
                                                        it.content.fordate!!
                                                )
                                        viewBinding.complaintId.text = it.content.complaintid
                                        viewBinding.work.text = it.content.work
                                        viewBinding.type.text = it.content.type
                                        viewBinding.outletName.text = it.content.outletname
                                        viewBinding.regional.text = it.content.regionaloffice
                                        viewBinding.sales.text = it.content.salesarea
                                        viewBinding.district.text = it.content.district
                                        viewBinding.letter.text = it.content.letterstatus
                                        viewBinding.status.text = it.content.status
                                        viewBinding.subadmin.text = it.content.subadmin
                                        viewBinding.supervisor.text = it.content.supervisor
                                        viewBinding.foreman.text = it.content.foreman
                                        viewBinding.enduser.text = it.content.enduser
                                        viewBinding.order.text = it.content.orderBy
                                        viewBinding.remarks.text = it.content.remarks
                                        setImageAndPdf(it.content)
                                        setChangeStatus(it.content)

                                    } else {
                                        viewBinding.loadingData.setVisibilityVisible()
                                    }


                                }
//                                is Lce.Error ->
//                                {
//
//                                    viewBinding.loadingData.setVisibilityGone()
//                                    showInformationDialog(it.error)
//
//                                }

                            }
                        })

                        viewBinding.loadingData.setVisibilityGone()
                        viewBinding.detailScreen.setVisibilityVisible()
//                        getSupportFragmentManager().beginTransaction()
//                                .add(android.R.id.content, DueFragment.newInstance())
//                                .commit()
////                        startActivity(Intent(this, DueFragment::class.java))
//                        finish()
                    }

                }
                is Lce.Error -> {
                    viewBinding.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)
                }
            }
        })

        viewModel.requestforAllocatedUserListState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.loadingData.setVisibilityVisible()
                    viewBinding.detailScreen.setVisibilityGone()
                }
                is Lce.Content -> {
                    Toast.makeText(this, "Successfully Allocated User", Toast.LENGTH_SHORT).show()
                    id.let { viewModel.getComplainDetails(it!!) }
                    viewModel.complaintDetailsState.observe(this, Observer {
                        when (it) {
                            Lce.Loading -> {
                                viewBinding.loadingData.setVisibilityVisible()
                            }
                            is Lce.Content -> {
                                if (it.content != null) {
                                    viewBinding.loadingData.setVisibilityGone()
                                    viewBinding.date.text =
                                            DateTimeHelper.getDDMMYYYYDateFromString(
                                                    it.content.fordate!!
                                            )
                                    viewBinding.complaintId.text = it.content.complaintid
                                    viewBinding.work.text = it.content.work
                                    viewBinding.type.text = it.content.type
                                    viewBinding.outletName.text = it.content.outletname
                                    viewBinding.regional.text = it.content.regionaloffice
                                    viewBinding.sales.text = it.content.salesarea
                                    viewBinding.district.text = it.content.district
                                    viewBinding.letter.text = it.content.letterstatus

                                    viewBinding.subadmin.text = it.content.subadmin
                                    viewBinding.supervisor.text = it.content.supervisor
                                    viewBinding.foreman.text = it.content.foreman
                                    viewBinding.enduser.text = it.content.enduser
                                    viewBinding.order.text = it.content.orderBy
                                    viewBinding.remarks.text = it.content.remarks
                                    setImageAndPdf(it.content)
                                    if (enabledComplaintRequest == true) {
                                        viewBinding.status.text = complaintRequestStatus
                                    } else {
                                        viewBinding.status.text = it.content.status
                                        setChangeStatus(it.content)
                                    }

                                } else {
                                    viewBinding.loadingData.setVisibilityVisible()
                                }


                            }
                            is Lce.Error -> {

                                viewBinding.loadingData.setVisibilityGone()
                                showInformationDialog(it.error)

                            }

                        }
                    })

                    viewBinding.loadingData.setVisibilityGone()
                    viewBinding.detailScreen.setVisibilityVisible()

                }
                is Lce.Error -> {
                    viewBinding.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)

                }

            }
        })
    }

    private fun setImageAndPdf(content: MyComplainDetailsList) {
        val link = content.letterpic?.substringAfter(".")
        if (link.equals("pdf")) {
            viewBinding.letterimageView.setImageDrawable(
                    ResourcesCompat.getDrawable(
                            this.getResources(),
                            R.drawable.default_pdf,
                            null
                    )
            )
            viewBinding.letterimageView.background = Color.WHITE.toDrawable()
            val url = appInfo.getFullAttachmentUrl(content.letterpic!!)
            viewBinding.letterimageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

        } else {
            if (content.letterpic != null) {
                val imageUrl: String = content.letterpic!!
                viewBinding.letterimageView.load(appInfo.getFullAttachmentUrl(content.letterpic!!))
                viewBinding.letterimageView.setOnClickListener {
                    imageOpenDialog(imageUrl)
                }
                viewBinding.letterimageView.background = Color.WHITE.toDrawable()
            }
        }
        val measurementpic = content.measurementpic?.substringAfter(".")

        if (measurementpic.equals("pdf")) {
            viewBinding.mesurenmentImageView.setImageDrawable(
                    ResourcesCompat.getDrawable(
                            this.getResources(),
                            R.drawable.default_pdf,
                            null
                    )
            )
            viewBinding.mesurenmentImageView.background = Color.WHITE.toDrawable()
            val url = appInfo.getFullAttachmentUrl(content.measurementpic!!)
            viewBinding.mesurenmentImageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        } else {
            if (content.measurementpic != null) {
                viewBinding.mesurenmentImageView.load(
                        appInfo.getFullAttachmentUrl(
                                content.measurementpic!!
                        )
                )
                val imageUrl: String = content.measurementpic!!
                viewBinding.mesurenmentImageView.setOnClickListener {
                    imageOpenDialog(imageUrl)
                }
                viewBinding.mesurenmentImageView.background = Color.WHITE.toDrawable()
            }else{
                viewBinding.mesurementLabel.setVisibilityGone()
                viewBinding.mesurenmentImageView.setVisibilityGone()
            }
        }
        val layoutpic = content.layoutpic?.substringAfter(".")

        if (layoutpic.equals("pdf")) {
            viewBinding.layoutImageView.setImageDrawable(
                    ResourcesCompat.getDrawable(
                            this.getResources(),
                            R.drawable.default_pdf,
                            null
                    )
            )
            viewBinding.layoutImageView.background = Color.WHITE.toDrawable()
            val url = appInfo.getFullAttachmentUrl(content.layoutpic!!)
            viewBinding.layoutImageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        } else {
            if (content.layoutpic != null) {
                viewBinding.layoutImageView.load(appInfo.getFullAttachmentUrl(content.layoutpic!!))
                val imageUrl: String = content.layoutpic!!
                viewBinding.layoutImageView.setOnClickListener {
                    imageOpenDialog(imageUrl)
                }
                viewBinding.layoutImageView.background = Color.WHITE.toDrawable()
            }else{
                viewBinding.layoutImageView.setVisibilityGone()
                viewBinding.layoutLabel.setVisibilityGone()
            }
        }

    }


    private fun setChangeStatus(content: MyComplainDetailsList) {

        if (content.status?.toLowerCase().equals("done")){
            viewBinding.doneImageContainer.setVisibilityVisible()
        }else{
            viewBinding.doneImageContainer.setVisibilityGone()
        }

    if ((userSessionManager.designation!!.toLowerCase().equals("sub admin") ||
                    userSessionManager.designation!!.toLowerCase().equals("subadmin")||
                    userSessionManager.designation!!.toLowerCase().equals("foreman")||
                    userSessionManager.designation!!.toLowerCase().equals("supervisor") ||
                    userSessionManager.designation!!.toLowerCase().equals("enduser") ||
                    userSessionManager.designation!!.toLowerCase().equals("end user")) &&
            ((content.status?.toLowerCase().equals("due") ||
                content.status?.toLowerCase().equals("working") ||
                content.status?.toLowerCase().equals("hold")
                 ))){
        viewBinding.changeStatus.setVisibilityVisible()
        viewBinding.allocateBtn.setVisibilityVisible()
    }else {
        if (content.status?.toLowerCase().equals("due") ||
            content.status?.toLowerCase().equals("working") ||
            content.status?.toLowerCase().equals("hold")
        ) {
            viewBinding.changeStatus.setVisibilityGone()
            viewBinding.allocateBtn.setVisibilityGone()

        } else {
            viewBinding.changeStatus.setVisibilityGone()
            viewBinding.allocateBtn.setVisibilityGone()
        }
    }

        detailsId = content.id
        statusremarks = content.statusremarks
        complaintid = content.complaintid
        status = content.status
        supervisorId = content.supervisorid
        endUserId = content.enduserid
        foremanId = content.foremanid
        supervisor = content.supervisor
        endUser = content.enduser
        foreman = content.foreman

}

    override fun onInitDataBinding() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        id = intent.getStringExtra("complaintId")
        enabledComplaintRequest = intent.getBooleanExtra(COMPLAINT_REQUEST_VIEW, false)
        complaintRequestStatus = intent.getStringExtra(COMPLAINT_REQUEST_STATUS)

        if (enabledComplaintRequest == true) {
            viewBinding.detailsToolbar.setTitle("Complaint Request Details ")
            viewBinding.changeStatus.setVisibilityGone()

        }else{
            viewBinding.detailsToolbar.setTitle("Complaint Details ")
        }

        viewBinding.detailsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        viewBinding.changeStatus.setOnClickListener {
            val inflater: LayoutInflater = LayoutInflater.from(this)
            val dialogView = inflater.inflate(R.layout.complaint_details_dialog_layout, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Update Status - ${complaintid}")

            val  mAlertDialog = mBuilder.show()
            statusSpinner = dialogView.findViewById(R.id.status_spinner)

            reason = dialogView.findViewById<EditText>(R.id.reason)
            donecontainer = dialogView.findViewById(R.id.doneImage)
            filename = dialogView.findViewById(R.id.letter_name)
            filename1 = dialogView.findViewById(R.id.measurement_name)
            filename2 = dialogView.findViewById(R.id.layout_name)
            imageClick1 = dialogView.findViewById<TextView>(R.id.clickImageLabel1)
            imageClick2 = dialogView.findViewById<TextView>(R.id.clickImageLabel2)
            imageClick3 = dialogView.findViewById<TextView>(R.id.clickImageLabel3)
            imageView1 = dialogView.findViewById<ImageView>(R.id.imageView1)
            imageView2 = dialogView.findViewById(R.id.imageView2)
            imageView3 = dialogView.findViewById(R.id.imageView3)


            imageClick1.setOnClickListener {
                currentlyClickingImageFor = LETTER_IMAGE
                showCameraOptionDialog()
            }
            imageClick2.setOnClickListener {
                currentlyClickingImageFor = MESUREMENT_IMAGE
                showCameraOptionDialog()
            }
            imageClick3.setOnClickListener {
                currentlyClickingImageFor = LAYOUT_IMAGE
                showCameraOptionDialog()
            }
//            imageClick2.setOnClickListener(ClickOutletImageOnClickListener())
//            imageClick3.setOnClickListener(ClickOutletImageOnClickListener())

//            val imangelayout= inflater.inflate(R.layout.layout_click_image, dialogView as ViewGroup, false)
//            imageClickLabel = imangelayout.findViewById(R.id.clickImageLabel)
//            ImageStoreView = imangelayout.findViewById(R.id.imageView)
//            imageClickLabel.setOnClickListener(ClickOutletImageOnClickListener())
//            donecontainer.addView(imangelayout)




//             reasonEt = reason.text.toString()
//            val spinnerlist = arrayOf("Select Status","Working", "Hold", "Done","Cancelled")
//            val statusArrayList = status!!.split(",")
            statusList.clear()
            statusList.add(0, "Select Status")
            if (status!!.toLowerCase().equals("working")){
                statusList.add("Hold")
                statusList.add("Done")
                statusList.add("Cancelled")
            }else if (status!!.toLowerCase().equals("hold")){
                statusList.add("Working")
                statusList.add("Done")
                statusList.add("Cancelled")
            }else if (status!!.toLowerCase().equals("due")){
                statusList.add("Working")
                statusList.add("Hold")
                statusList.add("Done")
                statusList.add("Cancelled")
            }

            val aa = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    statusList
            )
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            with(statusSpinner) {
                prompt = "Select Status"
                adapter = aa
                gravity = Gravity.CENTER
            }

            statusSpinner.onItemSelected { adapterView, _, position, _ ->
                adapterView ?: return@onItemSelected
                if (statusSpinner.childCount != 0 && statusSpinner.selectedItemPosition != 0) {
                    statusValue = adapterView.getItemAtPosition(position).toString()
                    if (statusValue.toLowerCase().equals("cancelled")) {
                        reason.setVisibilityVisible()
                        errorTextstatus.setVisibilityGone()
                        errorTextstatus.setText("")
                    } else {
                        reason.setVisibilityGone()
                    }
                    if(statusValue.toLowerCase().equals("working")){
                        errorTextstatus.setVisibilityGone()
                        errorTextstatus.setText("")

                    }
                    if(statusValue.toLowerCase().equals("hold")){
                        errorTextstatus.setVisibilityGone()
                        errorTextstatus.setText("")

                    }
                    if(statusValue.toLowerCase().equals("due")){
                        errorTextstatus.setVisibilityGone()
                        errorTextstatus.setText("")

                    }
                    if(statusValue.toLowerCase().equals("done")){
                        errorTextstatus.setVisibilityGone()
                        errorTextstatus.setText("")
                        donecontainer.setVisibilityVisible()


                    }else{
                        donecontainer.setVisibilityGone()
                    }

                }

            }

            val cancle = dialogView.findViewById<Button>(R.id.cancelbtn)
            val update = dialogView.findViewById<Button>(R.id.uploadbtn)
            errorTextstatus = dialogView.findViewById<TextView>(R.id.errorTexts)
            cancle.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
            }
            update.setOnClickListener{
                if (setValidationUpdate() == true){

//                setValidationUpdate()
                    viewModel.upDateComplaints(
                            statusremarks = reason.text.toString(),
                            id = detailsId!!.toInt(),
                            status = statusValue,
                            letterPic = imageResult,
                            measurementPic = mesurmentImageResult,
                            layoutPic = layoutImageResult

                    )
                    mAlertDialog.dismiss()
                }
            }

        }

        viewBinding.allocateBtn.setOnClickListener {
            viewModel.allocateUserForComplaint()
            val inflater: LayoutInflater = LayoutInflater.from(this)
            val dialogView = inflater.inflate(
                    R.layout.complaint_details_allocate_dialog_layout,
                    null
            )
            val mBuilder = AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setTitle("Allocate User")

            val  mAlertDialog = mBuilder.show()
            supervisorSpinner = dialogView.findViewById(R.id.supervisor_spinner)
            foremanSpinner = dialogView.findViewById(R.id.foreman_spinner)
            enduserSpinner = dialogView.findViewById(R.id.enduser_spinner)
            errorText = dialogView.findViewById<TextView>(R.id.errorText)
            supervisorSpinner.onItemSelected { adapterView, _, position, _->
                adapterView ?: return@onItemSelected
                if (supervisorSpinner.childCount != 0 && supervisorSpinner.selectedItemPosition != 0){
                   val supervisorValue = adapterView.getItemAtPosition(position) as AllocateUserData
                    supervisorid = supervisorValue.id
                }
            }
            foremanSpinner.onItemSelected { adapterView, _, position, _->
                adapterView ?: return@onItemSelected
                if (foremanSpinner.childCount != 0 && foremanSpinner.selectedItemPosition != 0){
                    val foremanValue = adapterView.getItemAtPosition(position) as AllocateUserData
                    foremanid = foremanValue.id
                }
            }
            enduserSpinner.onItemSelected { adapterView, _, position, _->
                adapterView ?: return@onItemSelected
                if (enduserSpinner.childCount != 0 && enduserSpinner.selectedItemPosition != 0){
                    val endUserValue = adapterView.getItemAtPosition(position) as AllocateUserData
                    enduserid = endUserValue.id
                }
            }
            val cancle = dialogView.findViewById<Button>(R.id.cancelbtn)
            val update = dialogView.findViewById<Button>(R.id.uploadbtn)
            cancle.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
            }
            update.setOnClickListener{
//                setValidationForAllocateUser()
                if (setValidationForAllocateUser() == true) {
                    viewModel.requestforAllocatedUserForComplaint(
                            supervisorid = supervisorid,
                            enduserid = enduserid,
                            foremanid = foremanid,
                            compid = detailsId!!
                    )
                    mAlertDialog.dismiss()
                }
            }
        }

    }



    private fun setValidationForAllocateUser(): Boolean {
        if (supervisor.isNullOrEmpty() && foreman.isNullOrEmpty() && endUser.isNullOrEmpty()){
            if ((supervisorSpinner.childCount == 0 || supervisorSpinner.selectedItemPosition == 0) && (foremanSpinner.childCount == 0 || foremanSpinner.selectedItemPosition == 0) && (enduserSpinner.childCount == 0 || enduserSpinner.selectedItemPosition == 0)) {
                errorText.setVisibilityVisible()
//                showInformationDialog("Please Select Supervisor")
                errorText.setText("Please Select At-least One User")
                return false
            }
        }
//        if (foreman.isNullOrEmpty()){
//            if (foremanSpinner.childCount == 0 || foremanSpinner.selectedItemPosition == 0) {
//                showInformationDialog("Please Select Foreman")
//                return
//            }
//        }
//        if (endUser.isNullOrEmpty()){
//            if (enduserSpinner.childCount == 0 || enduserSpinner.selectedItemPosition == 0) {
//                showInformationDialog("Please Select EndUser")
//                return
//            }
//        }

        return true

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


    private fun supervisorTypeSpinner(supervisorData: MutableList<AllocateUserData>) {
        for (i in 0 until supervisorData.size){
            val endusername = supervisorData[i].name

            if (supervisor.isNullOrEmpty()){
                SupervisorIdShowfirst = "XX"
                nameSupervisorshowfirst = "Select Supervisor"
                SupervisorDesignationnameshowfirst = ""
            }else {
                if (endusername.equals(supervisor)) {
                    SupervisorIdShowfirst = supervisorData[i].id
                    nameSupervisorshowfirst = supervisorData[i].name
                    SupervisorDesignationnameshowfirst = supervisorData[i].designation


                }
            }
        }

        supervisorData.add(
                0,
                AllocateUserData(
                        id = SupervisorIdShowfirst,
                        name = nameSupervisorshowfirst,
                        designation = SupervisorDesignationnameshowfirst
                )
        )
        val setItems = supervisorData.distinctBy { it.name }
        supervisorData.clear()
        supervisorData.addAll(setItems)

        val aa = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                supervisorData
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(supervisorSpinner) {
            adapter = aa
            prompt = "Select Supervisor"
            gravity = Gravity.CENTER
        }

    }

    private fun foremanTypeSpinner(foremanData: MutableList<AllocateUserData>) {
        for (i in 0 until foremanData.size){
            val endusername = foremanData[i].name

            if (foreman.isNullOrEmpty()){
                foremanidshowfirst = "XX"
                nameForemanshowfirst = "Select Foreman"
                foremanDesignationnameshowfirst = ""
            }else {
                if (endusername.equals(foreman)) {
                    foremanidshowfirst = foremanData[i].id
                    nameForemanshowfirst = foremanData[i].name
                    foremanDesignationnameshowfirst = foremanData[i].designation


                }
            }
        }

        foremanData.add(
                0,
                AllocateUserData(
                        id = foremanidshowfirst!!,
                        name = nameForemanshowfirst!!,
                        designation = foremanDesignationnameshowfirst!!
                )
        )

        val setItems = foremanData.distinctBy { it.name }
        foremanData.clear()
        foremanData.addAll(setItems)

        val aa = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                foremanData
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(foremanSpinner) {
            adapter = aa
            prompt = "Select Foreman"
            gravity = Gravity.CENTER
        }
    }
    private fun setendUserTypeSpinner(enduserData: MutableList<AllocateUserData>) {
//        enduserData.sortBy { it.name }
//        val setItems = enduserData.distinctBy { it.id }
//        regionalOffice.clear()

        for (i in 0 until enduserData.size){
            val endusername = enduserData[i].name

            if (endUser.isNullOrEmpty()){
                endidshowfirst = "XX"
                nameshowfirst = "Select EndUser"
                designationnameshowfirst = ""
            }else {
                if (endusername.equals(endUser)) {
                    endidshowfirst = enduserData[i].id
                    nameshowfirst = enduserData[i].name
                    designationnameshowfirst = enduserData[i].designation


                }
            }
        }

        enduserData.add(
                0,
                AllocateUserData(
                        id = endidshowfirst,
                        name = nameshowfirst,
                        designation = designationnameshowfirst
                )
        )
        val setItems = enduserData.distinctBy { it.name }
        enduserData.clear()
        enduserData.addAll(setItems)
        val aa = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                enduserData
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(enduserSpinner) {
            adapter = aa
            prompt = "Select EndUser"
            gravity = Gravity.CENTER
        }

    }

}

