package de.ronnyfriedland.ui.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Properties

/**
 * Resolves localized strings from assets (i18n_<lang>.properties)
 * with automatic fallback to English.
 */
@Composable
fun localized(context: Context, key: String): String {
    val lang = Locale.getDefault().language.lowercase(Locale.ROOT).let { if (it in listOf("de", "es")) it else "en" }
    val properties = remember(lang) {
        val currentProps = runCatching {
            context.assets.open("i18n_$lang.properties").use { Properties().apply { load(it.reader(Charsets.UTF_8)) } }
        }.getOrNull()
        if (lang != "en") {
            val defaultProps = runCatching {
                context.assets.open("i18n_en.properties").use { Properties().apply { load(it.reader(Charsets.UTF_8)) } }
            }.getOrNull()
            Properties(defaultProps).apply {
                if (currentProps != null) putAll(currentProps)
            }
        } else {
            currentProps ?: Properties()
        }
    }
    return properties.getProperty(key) ?: key
}

/** Parses a dd.MM.yyyy date string into epoch milliseconds. */
fun parseDate(date: String): Long = try {
    SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY).parse(date)?.time ?: 0L
} catch (_: Exception) {
    0L
}

/** Formats a weight double with one decimal place, removing trailing zeros. */
fun fmt(value: Double): String = String.format(Locale.GERMANY, "%.1f", value).removeSuffix(",0")
