package com.vinners.cube_vishwakarma.core.locale

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalisationRepository @Inject constructor(
    private val localePreferences: LocalePreferences
) {
    suspend fun getCurrentLanguage(): Language {
        val languageString = localePreferences.getCurrentLanguageShort() ?: Language.ENGLISH.languageNameShort
        return getLanguageForShortName(languageString)
    }

    private fun getLanguageForShortName(languageShortName : String) : Language {
        Language.values().forEach {
            if(it.languageNameShort == languageShortName)
                return it
        }

        throw IllegalArgumentException("no language found for supplied string $languageShortName")
    }

    private val _language: MutableLiveData<Language> = MutableLiveData()
    val language: LiveData<Language> get() = _language

    suspend fun isLocaleSet() = localePreferences.getCurrentLanguageShort() != null

    fun setLocale(language: Language) {
        localePreferences.setLocale(language)
        _language.postValue(language)
    }
}