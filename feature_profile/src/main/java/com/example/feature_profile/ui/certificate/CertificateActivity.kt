package com.example.feature_profile.ui.certificate

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import coil.api.load
import com.example.feature_profile.R
import com.example.feature_profile.databinding.ActivityCerificateBinding
import com.example.feature_profile.di.DaggerProfileComponent
import com.example.feature_profile.di.ProfileViewModelFactory
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.AppConstants
import com.vinners.trumanms.core.StorageHelper
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.Documents
import com.vinners.trumanms.data.models.certificate.Certificate
import java.io.File
import javax.inject.Inject

class CertificateActivity :
    BaseActivity<ActivityCerificateBinding, CertificateViewModel>(R.layout.activity_cerificate),
    CertificateClickListener {
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    private var file: File? = null

    override val viewModel: CertificateViewModel by viewModels {
        viewModelFactory
    }
    private val certficateRecyclerAdapter: CertificateRecyclerAdapter by lazy {
        CertificateRecyclerAdapter(appInfo).apply {
            updateList(emptyList())
            setListener(this@CertificateActivity)
        }
    }

    override fun onInitDependencyInjection() {
        DaggerProfileComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.certificateRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.certificateRecyclerView.adapter = certficateRecyclerAdapter
        viewBinding.refreshCertificateLayout.setOnRefreshListener {
            viewModel.getCertificate()
            viewBinding.refreshCertificateLayout.isRefreshing = false
        }
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onInitViewModel() {
        viewModel.certificateState.observe(this, Observer {
            when (it) {
                Lce.Loading -> documentsLoading()
                is Lce.Content -> documentsLoaded(it.content)
                is Lce.Error -> documentsLoadFailed(it.error)
            }
        })
        viewModel.downloadCertificateState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    file = it.content
                    Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                }
                is Lce.Error -> {
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.getCertificate()
    }

    private fun documentsLoading() {
        viewBinding.errorLayout.root.setVisibilityGone()
        certficateRecyclerAdapter.updateList(emptyList())
        viewBinding.progressBar.setVisibilityVisible()
    }

    private fun documentsLoaded(documentsList: List<Certificate>) {
        viewBinding.progressBar.setVisibilityGone()
        if (documentsList.isEmpty()) {
            certficateRecyclerAdapter.updateList(emptyList())
            showNoDataFoundLayout()
        } else {
            viewBinding.errorLayout.root.setVisibilityGone()
            certficateRecyclerAdapter.updateList(documentsList)
        }
    }

    private fun showNoDataFoundLayout() {
        viewBinding.errorLayout.root.setVisibilityVisible()
        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.errorLayout.messageTv.text = "No Certificate Found"
        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
    }

    private fun documentsLoadFailed(errorMessage: String) {
        viewBinding.progressBar.setVisibilityGone()
        certficateRecyclerAdapter.updateList(emptyList())
        viewBinding.errorLayout.root.setVisibilityVisible()

        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.errorLayout.messageTv.text = errorMessage
        viewBinding.errorLayout.errorActionButton.setVisibilityVisible()
        viewBinding.errorLayout.errorActionButton.text = "Re-Try"
        viewBinding.errorLayout.errorActionButton.setOnClickListener {
            viewModel.getCertificate()
        }
    }

    override fun onDownloadCertificate(certificate: Certificate, position: Int) {
        val policyFile = StorageHelper.accessExternalPublicFile(this,
            "truman_certificate_" + certificate.applicationid+".pdf"
        )
        viewModel.downloadCertificate(certificate.applicationid.toString(),policyFile)
    }

    override fun onViewClick(certificate: Certificate, position: Int) {
        val policyFile = StorageHelper.accessExternalPublicFile(this,
            "truman_certificate_" + certificate.applicationid+".pdf"
        )
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val fileUri = FileProvider.getUriForFile(
                    this,
                    appInfo.packageName + ".provider",
                    policyFile
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                fileUri
            } else {
                Uri.fromFile(policyFile)
            }
            intent.setDataAndType(uri, "application/pdf")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}