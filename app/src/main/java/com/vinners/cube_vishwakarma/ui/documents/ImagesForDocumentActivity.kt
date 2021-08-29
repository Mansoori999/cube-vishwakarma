package com.vinners.cube_vishwakarma.ui.documents

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.gridlayout.widget.GridLayout
import com.bumptech.glide.Glide
import com.himanshu.cameraintegrator.ImageCallback
import com.himanshu.cameraintegrator.ImagesSizes
import com.himanshu.cameraintegrator.RequestSource
import com.himanshu.cameraintegrator.integrator.CameraIntegrator
import com.himanshu.cameraintegrator.storage.StorageMode
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.AppConstants
import com.vinners.cube_vishwakarma.core.DateTimeHelper
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.camera.ImageHelper
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.databinding.ActivityImagesForDocumentBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import mobile.fitbitMerch.ui.masterData.ComplaintSelectionListener
import mobile.fitbitMerch.ui.masterData.OutletSelectionListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class ImagesForDocumentActivity : BaseActivity<ActivityImagesForDocumentBinding, DocumentsViewModel>(
    R.layout.activity_images_for_document
),
    ComplaintSelectionListener,
    OutletSelectionListener {

    companion object{
        private const val PERMISSION_REQUEST_STORAGE = 233

    }

    private val imageListForSend = ArrayList<Uri>()
    private lateinit var imagesContainer: GridLayout
    private  var outlet: OutletsList? = null
    private lateinit var fullPic: ImageView
    private var complaint: MyComplaintList? = null
    private var checkboxTextColor: String? = null
    private val capturedImageList= ArrayList<String>()
    private var captureOrSelectedImagePath: String = null.toString()
    private var complaintId: String? = null
    private val imageTextList = ArrayList<String>()
    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    override val viewModel: DocumentsViewModel by viewModels { viewModelFactory }



    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.document_images_ok_menu, menu)
        return true
    }


    override fun onInitDataBinding() {
        setSupportActionBar(viewBinding.toolbar)
        viewBinding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        viewBinding.toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.ok_image -> onBackPressed()
                R.id.send_image -> if (imageListForSend.isEmpty()) {
                    showInformationDialog("Please select images")
                } else {
                    val uriImageList =
                        arrayOfNulls<Uri>(imageListForSend.size)
                    var i = 0
                    while (i < imageListForSend.size) {
                        uriImageList[i] = imageListForSend.get(i)
                        i++
                    }
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND_MULTIPLE
                    shareIntent.type = "application/image"
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    shareIntent.putParcelableArrayListExtra(
                        Intent.EXTRA_STREAM,
                        imageListForSend
                    )
                    shareIntent.setPackage("com.whatsapp")
                    startActivity(shareIntent)
                    imageListForSend.clear()
                    inflateImageLayout()
                }
            }
            false
        })
        imagesContainer = findViewById(R.id.inflate_grid_layout)
        viewBinding.imageSiteBtn.setOnClickListener(View.OnClickListener {
            viewBinding.imageOutletBtn.setEnabled(true)
            viewBinding.imageSiteBtn.setEnabled(false)
            viewBinding.complainDetailLayout.setVisibility(View.VISIBLE)
            viewBinding.outletDetailLayout.setVisibility(View.GONE)
            viewBinding.imageBeforeAfter.setVisibility(View.GONE)
            viewBinding.clickImage.setVisibility(View.GONE)
            viewBinding.complaintOutletView.setVisibility(View.VISIBLE)
            viewBinding.afterBeforeView.setVisibility(View.GONE)
            complaintId = "NA"
            viewBinding.outletName.setText("NA")
            viewBinding.complaintNumber.setText("NA")
            viewBinding.work.setText("NA")
            imagesContainer.removeAllViews()
        })

        viewBinding.imageOutletBtn.setOnClickListener(View.OnClickListener {
            viewBinding.imageOutletBtn.setEnabled(false)
            viewBinding.imageSiteBtn.setEnabled(true)
            viewBinding.complainDetailLayout.setVisibility(View.GONE)
            viewBinding.outletDetailLayout.setVisibility(View.VISIBLE)
            viewBinding.imageBeforeAfter.setVisibility(View.GONE)
            viewBinding.clickImage.setVisibility(View.GONE)
            viewBinding.complaintOutletView.setVisibility(View.VISIBLE)
            viewBinding.afterBeforeView.setVisibility(View.GONE)
            viewBinding.outletName.setText("NA")
            viewBinding.roName.setText("NA")
            viewBinding.outletSalesArea.setText("NA")
            viewBinding.outletCode.setText("NA")
            imagesContainer.removeAllViews()
        })
        viewBinding.beforeButton.setOnClickListener(View.OnClickListener {
            viewBinding.beforeButton.setEnabled(false)
            viewBinding.afterButton.setEnabled(true)
            viewBinding.workingButton.setEnabled(true)
            checkboxTextColor = viewBinding.beforeButton.getText().toString()
            viewBinding.clickImage.setVisibility(View.VISIBLE)
            inflateImageLayout()
        })

        viewBinding.workingButton.setOnClickListener(View.OnClickListener {
            viewBinding.beforeButton.setEnabled(true)
            viewBinding.afterButton.setEnabled(true)
            viewBinding.workingButton.setEnabled(false)
            checkboxTextColor = viewBinding.workingButton.getText().toString()
            viewBinding.clickImage.setVisibility(View.VISIBLE)
            inflateImageLayout()
        })
        viewBinding.afterButton.setOnClickListener(View.OnClickListener {
            viewBinding.beforeButton.setEnabled(true)
            viewBinding.afterButton.setEnabled(false)
            viewBinding.workingButton.setEnabled(true)
            checkboxTextColor = viewBinding.afterButton.getText().toString()
            viewBinding.clickImage.setVisibility(View.VISIBLE)
            inflateImageLayout()
        })


        viewBinding.clickImage.setOnClickListener(View.OnClickListener {
            if (storagePermissions())
                openCamera()
            else
                requestStoragePermissions()

        })

        viewBinding.outletDetailLayout.setOnClickListener(View.OnClickListener {
            viewBinding.imageBeforeAfter.setVisibility(View.GONE)
            viewBinding.afterBeforeView.setVisibility(View.GONE)
            SelectOutletsDetailsDialogFragment
                .newInstance(this)
                .show(
                    supportFragmentManager,
                    SelectOutletsDetailsDialogFragment.TAG
                )


        })

        viewBinding.complainDetailLayout.setOnClickListener(View.OnClickListener {
            viewBinding.imageBeforeAfter.setVisibility(View.GONE)
            viewBinding.afterBeforeView.setVisibility(View.GONE)
//            val complaintList: List<MyComplaintList> = viewModel.getComplaintList()!!
//            if (complaintList == null || complaintList.size == 0) {
//                showInformationDialog("Complaints Not Available")
//                return@OnClickListener
//            }
            SelectComplaintDetailsDialogFragment
                .newInstance(this)
                .show(
                    supportFragmentManager,
                    SelectComplaintDetailsDialogFragment.TAG
                )
        })

    }

    fun openCamera() {
        try {
            cameraIntegrator.initiateCapture()
        } catch (e: Exception) {
            e.printStackTrace()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CameraIntegrator.REQUEST_IMAGE_CAPTURE){
            if (resultCode == Activity.RESULT_OK) {
                cameraIntegrator.parseResults(requestCode, resultCode, data, imageCallback)
            }
        }

        inflateImageLayout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val imageCallback = ImageCallback { requestedBy, result, error ->

        if (requestedBy == RequestSource.SOURCE_CAMERA) {
            imageTextList.clear()
            val calendar = Calendar.getInstance()
            imageTextList.add(simpleDateFormat.format(calendar.time).toString())
            if (outlet != null) {
                imageTextList.add("⦿")
                imageTextList.add(outlet!!.customercode!!.trim())
                imageTextList.add(outlet!!.name!!.trim())
                imageTextList.add(outlet!!.regionaloffice!!.trim())
            }
            if (complaint != null) {
                imageTextList.add("⦿")
                imageTextList.add(complaint!!.complaintid!!.trim())
                imageTextList.add(complaint!!.outlet!!.trim())
                imageTextList.add(complaint!!.work!!.trim())
            }
            val image = result!!.bitmap
            val photo = addWatermark(resources, image)
            val stampImage: Bitmap =
                ImageHelper.stampImageWithText(photo, imageTextList, checkboxTextColor)

            val storageDir =
                Environment.getExternalStoragePublicDirectory("Cube")
            val subDirecory =
                File(storageDir, DateTimeHelper.getTodaysDateInDDMMYYYY())
            storageDir.mkdirs()
            subDirecory.mkdirs()
            if (outlet != null) {
                val outletDirectory = File(subDirecory, outlet!!.customercode)
                outletDirectory.mkdirs()
                val beforAfterDirectory =
                    File(outletDirectory, checkboxTextColor)
                beforAfterDirectory.mkdirs()
                val imageFile: File = ImageHelper.createImageFile(beforAfterDirectory)
                ImageHelper.saveTo(imageFile.absolutePath, stampImage)
                captureOrSelectedImagePath = imageFile.absolutePath
            } else if (complaint != null) {
                val complaintDirectory =
                    File(subDirecory, complaint!!.complaintid)
                complaintDirectory.mkdirs()
                val beforAfterDirectory =
                    File(complaintDirectory, checkboxTextColor)
                beforAfterDirectory.mkdirs()
                val imageFile: File = ImageHelper.createImageFile(beforAfterDirectory)
                ImageHelper.saveTo(imageFile.absolutePath, stampImage)
                captureOrSelectedImagePath = imageFile.absolutePath
            }
//                openCamera()
            inflateImageLayout()
        }

    }

    //    private fun addWaterMark(src: Bitmap): Bitmap {
//        val w = src.width
//        val h = src.height
//        val result = Bitmap.createBitmap(w, h, src.config)
//        val canvas = Canvas(result)
//        canvas.drawBitmap(src, 0.0f, 0.0f, null)
//        val waterMark = BitmapFactory.decodeResource(
//            this.getResources(),
//            R.drawable.ic_cube_app_login
//        )
//        val paint = Paint()
//        paint.color = Color.TRANSPARENT
//        paint.alpha = 50
//        paint.isAntiAlias = true
//        paint.isUnderlineText = false
//        canvas.drawBitmap(waterMark, 0f, 0f, null)
//        return result
//    }
    fun addWatermark(res: Resources, source: Bitmap): Bitmap? {
        val w: Int
        val h: Int
        val c: Canvas
        val paint: Paint
        val bmp: Bitmap
        val watermark: Bitmap
        val matrix: Matrix
        val scale: Float
        val r: RectF
        w = source.width
        h = source.height
        // Create the new bitmap
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
        // Copy the original bitmap into the new one
        paint.setColor(Color.WHITE)
        paint.setAntiAlias(true)
        c = Canvas(bmp)
        c.drawBitmap(source, 0f, 0f, paint)
        // Load the watermark
        watermark = BitmapFactory.decodeResource(res, R.drawable.ic_watermark)
        paint.alpha = 100
        paint.style = Paint.Style.FILL
        // Scale the watermark to be approximately 40% of the source image height
        scale = (h.toFloat() * 0.15 / watermark.height.toFloat()).toFloat()
        // Create the matrix
        matrix = Matrix()
        matrix.postScale(scale, scale)
        // Determine the post-scaled size of the watermark
        r = RectF(500f, -2000f, watermark.width.toFloat(), watermark.height.toFloat())
        matrix.mapRect(r)
        // Move the watermark to the bottom right corner
        matrix.postTranslate(w - r.width(), h - r.height())
        // Draw the watermark
        c.drawBitmap(watermark, matrix, paint)
        // Free up the bitmap memory
        watermark.recycle()
        return bmp
    }
    fun resizeBitmap(drawableName: String, width: Int, height: Int): Bitmap? {
        val imageBitmap = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                drawableName, "drawable",
                packageName
            )
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }
    fun inflateImageLayout() {
        imagesContainer.removeAllViews()
        val folder = Environment.getExternalStoragePublicDirectory("Cube")
        val subFolder = File(folder, DateTimeHelper.getTodaysDateInDDMMYYYY())
        var imageList: Array<File>? = null
        if (outlet != null) {
            val inFolder = File(subFolder, outlet!!.customercode)
            val afterBeforeFolder = File(inFolder, checkboxTextColor)
            imageList = afterBeforeFolder.listFiles()
        } else if (complaint != null) {
            val inFolder = File(subFolder, complaint!!.complaintid)
            val afterBeforeFolder = File(inFolder, checkboxTextColor)
            imageList = afterBeforeFolder.listFiles()
        }
        if (imageList != null) {
            for (i in imageList.indices) {
                val imageView =
                    LayoutInflater.from(this).inflate(R.layout.add_images_in_documents, null, false)
                val addedImage =
                    imageView.findViewById<ImageView>(R.id.selected_image)
                val crossImage =
                    imageView.findViewById<ImageView>(R.id.remove_image)
                imagesContainer.addView(imageView)
                capturedImageList.add(
                    imagesContainer.indexOfChild(imageView),
                    captureOrSelectedImagePath
                )
                Glide.with(this@ImagesForDocumentActivity)
                    .load(imageList[i]).into(addedImage)
                val finalImageList: Array<File> = imageList
                addedImage.setOnClickListener {
                    val imageDialog = Dialog(
                        this@ImagesForDocumentActivity,
                        android.R.style.Theme_Black_NoTitleBar_Fullscreen
                    )
                    imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    imageDialog.setContentView(R.layout.big_picture_image_delete_option)
                    fullPic =
                        imageDialog.findViewById(R.id.big_picture)
                    val deleteImage =
                        imageDialog.findViewById<ImageView>(R.id.delete_image)
                    val shareImage =
                        imageDialog.findViewById<ImageView>(R.id.share_image)
                    shareImage.setOnClickListener {
                        val uri =
                            FileProvider.getUriForFile(
                                this@ImagesForDocumentActivity,
                                this@ImagesForDocumentActivity.packageName
                                    .toString() + ".provider",
                                finalImageList[i]
                            )
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.type = "application/image"
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                        shareIntent.setPackage("com.whatsapp")
                        startActivity(shareIntent)
                    }
                    deleteImage.setOnClickListener {
                        AlertDialog.Builder(this@ImagesForDocumentActivity)
                            .setTitle("Delete Image")
                            .setMessage("Do you want to delete this image?")
                            .setPositiveButton(
                                "Yes"
                            ) { dialog, which ->
                                finalImageList[i].delete()
                                inflateImageLayout()
                                imageDialog.dismiss()
                            }
                            .setNegativeButton(
                                "No"
                            ) { dialog, which -> }.show()
                    }
                    Glide.with(applicationContext)
                        .load(finalImageList[i]).into(fullPic)
                    imageDialog.window!!.attributes.windowAnimations =
                        R.style.MyAlertDialogStyle
                    imageDialog.show()
                }
                addedImage.setOnLongClickListener {
                    crossImage.visibility = View.VISIBLE
                    val uri = FileProvider.getUriForFile(
                        this@ImagesForDocumentActivity,
                        this@ImagesForDocumentActivity.packageName
                            .toString() + ".provider",
                        finalImageList[i]
                    )
                    if (!imageListForSend.contains(uri)) imageListForSend.add(uri)
                    true
                }
                crossImage.setOnClickListener {
                    val uri = FileProvider.getUriForFile(
                        this@ImagesForDocumentActivity,
                        this@ImagesForDocumentActivity.packageName
                            .toString() + ".provider",
                        finalImageList[i]
                    )
                    imageListForSend.remove(uri)
                    crossImage.visibility = View.GONE
                }
                for (k in imageListForSend.indices) {
                    val uri = FileProvider.getUriForFile(
                        this@ImagesForDocumentActivity,
                        this@ImagesForDocumentActivity.packageName.toString() + ".provider",
                        finalImageList[i]
                    )
                    if (imageListForSend.contains(uri)) {
                        crossImage.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onComplaintSelected(myComplaintList: MyComplaintList) {
        outlet = null
        this.complaint = myComplaintList
        viewBinding.imageBeforeAfter.setVisibility(View.VISIBLE)

        viewBinding.clickImage.setVisibility(View.GONE)

        viewBinding.afterBeforeView.setVisibility(View.VISIBLE)

        complaintId = myComplaintList.complaintid
        viewBinding.outletName.setText(myComplaintList.outlet)
        viewBinding.complaintNumber.setText(myComplaintList.complaintid)
        viewBinding.work.setText(myComplaintList.work)

        viewBinding.outletNoName.setText("NA")
        viewBinding.roName.setText("NA")
        viewBinding.outletSalesArea.setText("NA")
        viewBinding.outletCode.setText("NA")

        imagesContainer.removeAllViews()
        viewBinding.beforeButton.setEnabled(true)
        viewBinding.afterButton.setEnabled(true)
        viewBinding.workingButton.setEnabled(true)
    }

    override fun onOutletSelected(outletsList: OutletsList) {
        this.outlet = outletsList
        this.complaint = null
        viewBinding.imageBeforeAfter.setVisibility(View.VISIBLE)
        viewBinding.afterBeforeView.setVisibility(View.VISIBLE)

        viewBinding.clickImage.setVisibility(View.GONE)

        viewBinding.outletNoName.setText(outlet!!.name)
        viewBinding.roName.setText(outlet!!.regionaloffice)
        viewBinding.outletSalesArea.setText(outlet!!.salesarea)
        viewBinding.outletCode.setText(outlet!!.customercode)

        complaintId = "NA"
        viewBinding.outletName.setText("NA")
        viewBinding.complaintNumber.setText("NA")
        viewBinding.work.setText("NA")

        imagesContainer.removeAllViews()
        viewBinding.beforeButton.setEnabled(true)
        viewBinding.afterButton.setEnabled(true)
        viewBinding.workingButton.setEnabled(true)
    }

    override fun onInitViewModel() {

    }

}