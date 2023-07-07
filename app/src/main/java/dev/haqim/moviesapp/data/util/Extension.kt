package dev.haqim.moviesapp.data.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

const val DEFAULT_DECIMAL_FORMAT = "###,###,###.##"

fun Int.toDecimalFormat(format : String = DEFAULT_DECIMAL_FORMAT) : String {
    val decimalFormat = DecimalFormat(format)
    return decimalFormat.format(this)
}

fun Int.toUSD(format : String = DEFAULT_DECIMAL_FORMAT): String{
    val formatted = this.toDecimalFormat(format)
    return "USD $formatted"
}

const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
const val DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val DATE_FORMAT_dd_MM_yyyy = "yyyy/MM/dd"

fun String.formatDate(
    currentFormat: String = DEFAULT_DATE_FORMAT,
    formatTo: String = DATE_FORMAT_dd_MM_yyyy,
    locale: Locale = Locale.getDefault()
): String {
    val date = SimpleDateFormat(currentFormat, locale).parse(this)
    val simpleDate = SimpleDateFormat(formatTo, locale)
    return simpleDate.format(date!!)
}