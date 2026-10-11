package com.tgp.mockexam.core.model

/** A value available in both supported languages. */
data class Bilingual(
    val en: String,
    val te: String,
) {
    fun forLanguage(language: ContentLanguage): String = when (language) {
        ContentLanguage.ENGLISH -> en.ifBlank { te }
        ContentLanguage.TELUGU -> te.ifBlank { en }
    }
}

enum class ContentLanguage { ENGLISH, TELUGU }
