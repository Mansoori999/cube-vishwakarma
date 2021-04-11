package com.vinners.cube_vishwakarma.ui.languageSelection

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vinners.cube_vishwakarma.analytics.LauncherAnalytics
import com.vinners.cube_vishwakarma.core.analytics.AnalyticsHelper
import com.vinners.cube_vishwakarma.core.locale.Language
import com.vinners.cube_vishwakarma.core.locale.LocaleManager
import com.vinners.cube_vishwakarma.core.locale.LocalisationRepository
import javax.inject.Inject

interface LanguageViewModelEvents {
    val languageSelectionCompleted: LiveData<String>
}

class LanguageViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val localisationRepository: LocalisationRepository
) : ViewModel(),
    LanguageViewModelEvents {

    val events: LanguageViewModelEvents = this

    fun setLanguage(context: Context, language: Language) {
        LocaleManager.setNewLocale(context, language)

        localisationRepository.setLocale(language)
        registerLanguageSelectedInAnalytics(language)
        startLoginActivity()
    }

    private fun registerLanguageSelectedInAnalytics(language: Language) {
        val bundle = Bundle()
        bundle.putString(LauncherAnalytics.Params.LANGUAGE_SELECTED, language.languageNameShort)
        analyticsHelper.logEvent(LauncherAnalytics.Events.EVENT_LANGUAGE_SELECTED, bundle)
    }

    private fun startLoginActivity() {
        _languageSelected.value = "LanguageSelected"
    }

    private val _languageSelected = MutableLiveData<String>()
    override val languageSelectionCompleted: LiveData<String> get() = _languageSelected
}