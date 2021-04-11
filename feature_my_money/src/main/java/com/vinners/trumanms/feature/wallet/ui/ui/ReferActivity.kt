package com.vinners.trumanms.feature.wallet.ui.ui

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.observe
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.feature.wallet.R
import com.vinners.trumanms.feature.wallet.databinding.ActivityReferBinding
import com.vinners.trumanms.feature.wallet.ui.di.DaggerWalletComponent
import com.vinners.trumanms.feature.wallet.ui.di.WalletViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class ReferActivity :
    BaseActivity<ActivityReferBinding, WalletInfoViewModel>(R.layout.activity_refer) {
    companion object {
        private const val PERMISSION_REQUEST_STORAGE = 233
    }

    @Inject
    lateinit var viewModelFactory: WalletViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    @Inject
    lateinit var userSessionManager: UserSessionManager

    private var picPath: String? = null
    private var userName: String? = null
    private var imageBitmap: Bitmap? = null
    private var returnedBitmap: Bitmap? = null
    private var imageView: ImageView? = null
    private var logoIcon: ImageView? = null
    private var view: View? = null
    private var isUploaded: Boolean = false
    private var invitationShortLink: String? = null

    override val viewModel: WalletInfoViewModel by viewModels {
        viewModelFactory
    }


    override fun onInitDependencyInjection() {
        DaggerWalletComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        //getView()
        createReferLink()
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
        viewBinding.inviteBtn.setOnClickListener {
            viewModel.generateReferralLink()
        }
    }

    override fun onInitViewModel() {

        viewModel.referralLink
            .observe(this) {

                when (it) {
                    Lce.Loading -> {
                        viewBinding.inviteBtn.showProgress {
                            progressColor =
                                ResourcesCompat.getColor(resources, R.color.color_primary, null)
                            buttonText = "Invite"
                        }
                    }
                    is Lce.Content -> {
                        viewBinding.inviteBtn.hideProgress(
                            "Invite"
                        )
                        showShareDialog(it.content)
                    }
                    is Lce.Error -> {
                        viewBinding.inviteBtn.hideProgress("Invite")
                        Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }

    private fun showShareDialog(content: String) {
        try {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "image/*"
            sharingIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            sharingIntent.putExtra(Intent.EXTRA_STREAM, getImageUri())
            sharingIntent.putExtra(
                Intent.EXTRA_TEXT,
                "\nSee tasks at companies that need you. Earn weekly, build CV and enjoy medical benefits. \n Click to install Truman Gig - \n $invitationShortLink"
            )
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        } catch (e: Exception) {
            Log.d("JobTag", e.message.toString())
            Toast.makeText(this, "Respective app not installed", Toast.LENGTH_SHORT).show()
        }
    }

    fun getView() {
        view = LayoutInflater.from(this).inflate(R.layout.referal_share_layout, null)
        imageView = view?.findViewById(R.id.shareImageView)
        val name = view?.findViewById<TextView>(R.id.shareText)
        logoIcon = view?.findViewById(R.id.logoIcon)
        name?.text = userName
        /*  if (picPath.isNullOrEmpty().not() && isUploaded) {
              Glide.with(this)
                  .load(picPath!!)
                  .addListener(object : RequestListener<Drawable> {
                      override fun onLoadFailed(
                          e: GlideException?,
                          model: Any?,
                          target: Target<Drawable>?,
                          isFirstResource: Boolean
                      ): Boolean {

                          Log.d("JobTag", e?.message.toString())
                          return true
                      }

                      override fun onResourceReady(
                          resource: Drawable?,
                          model: Any?,
                          target: Target<Drawable>?,
                          dataSource: DataSource?,
                          isFirstResource: Boolean
                      ): Boolean {
                          imageView?.setImageDrawable(resource)
                          imageBitmap = (imageView?.drawable as BitmapDrawable).bitmap
                          getBitmapFromView()
                          return false
                      }

                  })
                  .into(imageView!!)
          } else {
              Glide.with(this)
                  .load(BuildConfig.API_URL + "file/" + picPath!!)
                  .addListener(object : RequestListener<Drawable> {
                      override fun onLoadFailed(
                          e: GlideException?,
                          model: Any?,
                          target: Target<Drawable>?,
                          isFirstResource: Boolean
                      ): Boolean {

                          Log.d("JobTag", e?.message.toString())
                          return true
                      }

                      override fun onResourceReady(
                          resource: Drawable?,
                          model: Any?,
                          target: Target<Drawable>?,
                          dataSource: DataSource?,
                          isFirstResource: Boolean
                      ): Boolean {
                          imageView?.setImageDrawable(resource)
                          imageBitmap = (imageView?.drawable as BitmapDrawable).bitmap
                          getBitmapFromView()
                          return false
                      }
                  })
                  .into(imageView!!)*/
        getBitmapFromView()
    }

    private fun getBitmapFromView() {

        /*  val imageFile = ImageStorageHelper.createExternalPublicImageFile(
              AppConstants.DEFAULT_PREF_NAME,
              "referral",
              "jpg"
          )
          ImageStorageHelper.saveTo(imageFile, imageBitmap)*/

        view?.setDrawingCacheEnabled(true)

        view?.measure(
            View.MeasureSpec.makeMeasureSpec(
                resources.displayMetrics.widthPixels,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                resources.displayMetrics.heightPixels,
                View.MeasureSpec.EXACTLY
            )
        )
        view?.layout(0, 0, view?.measuredWidth!!, view?.measuredHeight!!)
        view?.buildDrawingCache(true)
        returnedBitmap = Bitmap.createBitmap(view?.getDrawingCache()!!)
        val canvas = Canvas(returnedBitmap!!)
        val drawble = view?.background
        if (drawble != null)
            drawble.draw(canvas)
        else
            canvas.drawColor(Color.WHITE)
        //imageView?.draw(canvas)
        //logoIcon?.draw(canvas)
        view?.isDrawingCacheEnabled = false
        view?.draw(canvas)
    }

    /* fun getImageUri(): Uri {
         val byteArrayOutputStream = ByteArrayOutputStream()
         // bitmapImage?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
         val path = insertImage(
             contentResolver,
             bitmapImage,
             UUID.randomUUID().toString(),
             null
         )
         return Uri.parse(path)
     }*/

    private fun getImageUri(): Uri {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.msg_image)
        var imageUri: Uri? = null
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (VERSION.SDK_INT >= VERSION_CODES.Q) {
            contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val collections =
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                imageUri =
                    resolver.insert(collections, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = this.externalCacheDir
            val imageFile = File(imagesDir, filename)
            fos = FileOutputStream(imageFile)

            imageUri = Uri.parse(imageFile.absolutePath)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return imageUri!!
    }

    fun createReferLink() {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://play.google.com/store/apps/details?id=com.vinners.trumanms&hl=en_IN&invitedby=${userSessionManager.mobile}"))
            .setDomainUriPrefix("https://trumannooo.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder("com.vinners.trumanms")
                    .setMinimumVersion(19)
                    .build()
            )
            .buildShortDynamicLink()
            .addOnSuccessListener {
                invitationShortLink = it.shortLink.toString()
            }
            .addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //getView()
                } else {
                    //TODO handle manual permission here
                }
                return
            }
        }
    }

    private fun storagePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this!!, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    this!!, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED

    }

    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(
            this!!,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_STORAGE
        )
    }
}