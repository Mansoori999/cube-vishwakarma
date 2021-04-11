package com.vinners.cube_vishwakarma.core.locale

/**
 * List Of Languages App Supports
 */
enum class Language constructor(
    private val language: String
) {
    ENGLISH("en"),
    HINDI("hi");

    val languageNameShort get() = language
}