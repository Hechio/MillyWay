package com.stevehechio.milkyway.utils

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun View.gone(): View {
    if (visibility != View.GONE){
        visibility = View.GONE
    }
    return this
}

fun View.visible(): View {
    if (visibility != View.VISIBLE){
        visibility = View.VISIBLE
    }
    return this
}

fun String.toDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date: Date?
    var convertedDate: String? = null
    try {
        date = dateFormat.parse(this)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        convertedDate = simpleDateFormat.format(date) ?: return this.split("T").first()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return convertedDate!!
}