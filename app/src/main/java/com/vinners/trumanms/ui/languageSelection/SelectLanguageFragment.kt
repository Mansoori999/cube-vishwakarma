package com.vinners.trumanms.ui.languageSelection

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vinners.trumanms.R
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.locale.Language
import com.vinners.trumanms.databinding.FragmentSelectLanguageBinding
import com.vinners.trumanms.di.DaggerLauncherComponent
import com.vinners.trumanms.di.LauncherViewModelFactory
import com.vinners.trumanms.feature.auth.ui.AuthActivity
import javax.inject.Inject

class SelectLanguageFragment :
    BaseFragment<FragmentSelectLanguageBinding, LanguageViewModel>(R.layout.fragment_select_language) {

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    override val viewModel: LanguageViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        setListeners()
    }

    override fun onInitViewModel() {
        viewModel.events
            .languageSelectionCompleted
            .observe(viewLifecycleOwner, Observer {
                startLoginActivity()
            })
    }

    private fun setListeners() {
        viewBinding.englishLanguageCard.setOnClickListener {
            viewModel.setLanguage(requireContext(), Language.ENGLISH)
        }

        viewBinding.hindiLanguageCard.setOnClickListener {
            viewModel.setLanguage(requireContext(), Language.HINDI)
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
    }
}
