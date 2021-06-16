package com.vinners.cube_vishwakarma.ui.documents

import android.Manifest
import android.annotation.TargetApi
import android.app.Dialog
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.jsibbold.zoomage.ZoomageView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.documents.DocumentsResponse
import com.vinners.cube_vishwakarma.databinding.ActivityDocumentDetailsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import java.io.File
import javax.inject.Inject

class DocumentDetailsActivity : BaseActivity<ActivityDocumentDetailsBinding, DocumentsViewModel>(R.layout.activity_document_details), DocumentsDetailsClickListener {

    companion object{

        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1


    }
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    override val viewModel: DocumentsViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var appInfo: AppInfo
    var catid: String? = null
    lateinit var id: String

    private val documentDetailsRecyclerAdapter: DocumentDetailsRecyclerAdapter by lazy {
        DocumentDetailsRecyclerAdapter()
            .apply {
                updateViewList(emptyList(), appInfo = appInfo)
                setDocumentsListener(this@DocumentDetailsActivity)

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

        viewBinding.documentToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        viewBinding.documentsRecycler.layoutManager = LinearLayoutManager(this)
        documentDetailsRecyclerAdapter.updateViewList(emptyList(), appInfo)
        documentDetailsRecyclerAdapter.setHasStableIds(true)
        viewBinding.documentsRecycler.adapter = documentDetailsRecyclerAdapter
        viewBinding.refreshLayout.setOnRefreshListener {

            if (!viewBinding.refreshLayout.isRefreshing) {
                viewBinding.refreshLayout.isRefreshing = true
            }

            viewModel.getDocumentsData()

        }


    }

    override fun onInitViewModel() {
        catid = intent.getStringExtra("catid")
        val gh=catid

        viewModel.documentState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.errorLayout.root.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityVisible()
                    viewBinding.refreshLayout.isRefreshing = false
                }
                is Lce.Content -> {

                    if (it.content.isEmpty()) {
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = "Not Documents Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        documentDetailsRecyclerAdapter.updateViewList(it.content.filter { it.catid == catid }, appInfo)
                        if (!viewBinding.refreshLayout.isRefreshing) {
                            viewBinding.refreshLayout.isRefreshing = false
                        }
                    }


                }
                is Lce.Error -> {
                    viewBinding.progressBar.setVisibilityGone()
                    viewBinding.refreshLayout.isRefreshing = false
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })

        viewModel.getDocumentsData()


    }


    override fun OnDocumentsClick(documentsResponse: DocumentsResponse) {
        val pathurl = documentsResponse.path
        val link = pathurl?.substringAfter(".")
        if (link.equals("ppt") || link.equals("pptx")){
            val url = appInfo.getFullAttachmentUrl(documentsResponse.path!!)
            val startwith = "/storage/emulated/0/"
//            val uri = Uri.parse(url);
//            val file = File(uri.path)
//            val file:File = File(URL(uri.toString()).toURI());
//            openFile(file)
            val pathUrl = startwith+documentsResponse.path
          showPdfOnClick(pathUrl)
        }else if (link.equals("pdf")){
            val url = appInfo.getFullAttachmentUrl(documentsResponse.path!!)
            val startwith = "/storage/emulated/0/"
            val pathUrl = startwith+documentsResponse.path
            Log.d("ff",pathUrl)
            showPdfOnClick(pathUrl)
//            val uri = Uri.parse(url);
//            val file = File(uri.path)
//            openFile(file)
//                if (Build.VERSION.SDK_INT >= 23) {
//                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "Please Wait until the file is download", Toast.LENGTH_LONG).show()
//                        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//                        val uri = Uri.parse(url)
//                        val request = DownloadManager.Request(uri)
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                            request.allowScanningByMediaScanner()
//                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                        }
//                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/" + "pdf")
//                        request.setMimeType("application/pdf")
//                        request.allowScanningByMediaScanner()
//                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
//                        downloadManager.enqueue(request)
//
//                    }else{
//                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
//                    }
//                }
//           showPdfOnClick(url)
        }else{
            val imageUrl : String = documentsResponse?.path.toString()
            imageOpenDialog(imageUrl)
        }

    }

    private fun haveStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error", "You have permission")
                true
            } else {
                Log.e("Permission error", "You have asked for permission")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                false
            }
        } else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error", "You already have the permission")
            true
        }
    }

    private fun showPdfOnClick(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val f = File(url)
//            val rootPath: String = root.value.getPath()
            var uri: Uri? = null
            // So you have to use Provider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                uri = FileProvider.getUriForFile(this, application.packageName + ".provider", f)

                // Add in case of if We get Uri from fileProvider.
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                uri = Uri.fromFile(f)
            }
            if (f.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf")
            } else if (f.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
            }
//            else if (f.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
//                // JPG file
//                intent.setDataAndType(uri, "image/jpeg")
//            } else {
//                intent.setDataAndType(uri, "*/*")
//            }
//            intent.setDataAndType(uri, "application/pdf")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
//            Toast.makeText(this, "No application found which can open the file", Toast.LENGTH_SHORT).show()
        }


    }


    private fun openFile(url: File) {
        try {
//          val uri = Uri.fromFile(url)

            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
           val uri: Uri = FileProvider.getUriForFile(this, application.packageName + ".provider", url)
//            val intent = Intent(Intent.ACTION_VIEW)
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
           if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf")
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg")
            } else {
                intent.setDataAndType(uri, "*/*")
            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No application found which can open the file", Toast.LENGTH_SHORT).show()
        }
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

}