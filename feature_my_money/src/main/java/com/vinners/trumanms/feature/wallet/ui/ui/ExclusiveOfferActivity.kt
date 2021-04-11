package com.vinners.trumanms.feature.wallet.ui.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.feature.wallet.R
import com.vinners.trumanms.feature.wallet.databinding.ActivityExclusiveOfferBinding
import com.vinners.trumanms.feature.wallet.ui.di.DaggerWalletComponent
import com.vinners.trumanms.feature.wallet.ui.di.WalletViewModelFactory
import javax.inject.Inject

class ExclusiveOfferActivity : BaseActivity<ActivityExclusiveOfferBinding,WalletInfoViewModel>(R.layout.activity_exclusive_offer) {
    @Inject
    lateinit var viewModelFactory: WalletViewModelFactory

    override val viewModel: WalletInfoViewModel by viewModels {
        viewModelFactory
    }

    override fun onInitDependencyInjection() {

    }

    override fun onInitDataBinding() {
        DaggerWalletComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitViewModel() {

    }

}