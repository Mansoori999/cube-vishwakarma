package com.vinners.cube_vishwakarma.core.locale

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

class LocaleManager {

    companion object {
        private val LANGUAGE_KEY = "language_chosen"
        private const val PREF_LOCALE = "locale_pref"


        fun setLocale(c: Context): Context {
            val savedLanguage = getLanguage(c)
            return savedLanguage?.let { updateResources(c, it) } ?: c
        }

        fun setNewLocale(c: Context, language: Language): Context {
            persistLanguage(c, language.languageNameShort)
            return updateResources(c, language.languageNameShort)
        }

        fun setNewLocale(c: Context, newLocale: Locale): Context {
            persistLanguage(c, newLocale.toString())
            return updateResources(c, newLocale)
        }

        fun getLanguage(c: Context): String? {
            val prefs = getLocalePreferences(c)
            val currentLocale = getLocale(c.resources)
            return if (!prefs.contains(LANGUAGE_KEY)) null else prefs.getString(
                LANGUAGE_KEY,
                currentLocale.toString()
            )
        }

        @SuppressLint("ApplySharedPref")
        private fun persistLanguage(c: Context, language: String) =
            getLocalePreferences(context = c)
                .edit()
                .putString(LANGUAGE_KEY, language).commit()


        private fun getLocalePreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(PREF_LOCALE, Context.MODE_PRIVATE)

        private fun updateResources(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)
            return updateResources(context, locale)
        }

        @Suppress("DEPRECATION")
        private fun updateResources(context: Context, locale: Locale): Context {
            val res = context.resources
            val config = Configuration(res.configuration)
            config.setLocale(locale)
            val newContext = context.createConfigurationContext(config)
            res.updateConfiguration(config, res.displayMetrics)

            return newContext
        }

        @Suppress("DEPRECATION")
        fun getLocale(res: Resources): Locale {
            val config = res.configuration
            return if (Build.VERSION.SDK_INT >= 24) config.locales.get(0) else config.locale
        }

        fun getSavedLocale(c: Context): Locale {
            val savedLanguage = getLanguage(c)
            return if (savedLanguage == null) getLocale(c.resources) else Locale(getLanguage(c))
        }
    }


}