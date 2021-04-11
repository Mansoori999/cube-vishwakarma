package com.vinners.cube_vishwakarma.core.locale

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class LocalePreferences @Inject constructor(
    @Named("session_independent_pref") private val localeSharedPreferences: SharedPreferences
) {

    fun setLocale(language: Language) {
        localeSharedPreferences.edit().putString(LANGUAGE, language.languageNameShort).apply()
    }

    suspend fun getCurrentLanguageShort() = localeSharedPreferences.getString(LANGUAGE, null)

    companion object {
        private const val LANGUAGE = "language"
    }
}