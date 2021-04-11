package com.vinners.trumanms.feature.auth.ui.login

import android.graphics.Bitmap
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.ActivityPrivacyPolicyBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import com.vinners.trumanms.feature.auth.ui.AuthViewModel
import javax.inject.Inject

class PrivacyPolicyActivity :
    BaseActivity<ActivityPrivacyPolicyBinding, AuthViewModel>(R.layout.activity_privacy_policy) {
    companion object{
        const val INTENT_EXTRA_URL = "url"
    }
    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory
    private var loadUrl: String? = null

    override val viewModel: AuthViewModel by viewModels {
        viewModelFactory
    }

    override fun onInitDependencyInjection() {
        DaggerAuthComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        loadUrl = intent.getStringExtra(INTENT_EXTRA_URL)
        viewBinding.webView.progress
        viewBinding.webView.loadUrl(loadUrl)
        val webSetting = viewBinding.webView.settings.apply {
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
        }
        viewBinding.webView.webViewClient = MyBrowser()
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
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

    override fun onInitViewModel() {

    }

}