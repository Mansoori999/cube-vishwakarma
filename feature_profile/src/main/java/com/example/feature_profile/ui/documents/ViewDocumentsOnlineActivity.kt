package com.example.feature_profile.ui.documents

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.example.feature_profile.R
import com.example.feature_profile.databinding.ActivityViewDocumentsOnlineBinding
import com.example.feature_profile.di.DaggerProfileComponent
import com.example.feature_profile.di.ProfileViewModelFactory
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import javax.inject.Inject

class ViewDocumentsOnlineActivity : BaseActivity<ActivityViewDocumentsOnlineBinding,DocumentsViewModel>(R.layout.activity_view_documents_online) {
   companion object{
       const val URL = "file_url"
   }
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    override val viewModel: DocumentsViewModel by viewModels {
        viewModelFactory
    }

    override fun onInitDependencyInjection() {
        DaggerProfileComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        val url = intent.getStringExtra(URL)
        val loadUrl ="https://docs.google.com/gview?embedded=true&url="+appInfo.getFullAttachmentUrl(url)
        viewBinding.webView.progress
        viewBinding.webView.loadUrl(loadUrl)
        val webSetting = viewBinding.webView.settings.apply {
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
        }
        viewBinding.webView.webViewClient = MyBrowser()
    }

    override fun onInitViewModel() {

    }
    inner class MyBrowser : WebViewClient() {


        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return false
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            viewBinding.progressBar.setVisibilityVisible()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            viewBinding.progressBar.setVisibilityGone()
        }

    }
}