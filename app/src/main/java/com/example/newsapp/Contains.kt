package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.os.Build
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Contains {

    const val BASE_URL = "https://newsapi.org/"
    const val MY_API_KEY = "2e3723c25cce412899f9854450097ec8"
    const val DELAY_TIME_SEARCH = 200L
    const val ARTICLE_ARGS_KEY = "com.example.newsapp.articlekey"
    const val RAW_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val TARGET_PATTERN = "EEE MMM dd hh:mm aa"
    const val VIEWTYPE_LOADMORE = 1
    const val VIEWTYPE_NORMAL = 2
    const val PAGE_SIZE = 20
    fun checkNetWorkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            if (network == null) {
                return false
            }
            val capability = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                capability.hasTransport(TRANSPORT_WIFI) -> true
                capability.hasTransport(TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.let {
                return when (it.type) {
                    TYPE_WIFI -> true
//                    TYPE_MOBILE -> true
                    else -> false
                }
            }
        }
        return false
    }
    @SuppressLint("SimpleDateFormat")
    fun convertDatetime(rawString: String): String {
        var date: Date? = null
        try {
            var format= SimpleDateFormat(RAW_PATTERN)
            format.timeZone = TimeZone.getTimeZone("UTC")
            date = format.parse(rawString)
            // Log.e("tag",date.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (date != null) return SimpleDateFormat(TARGET_PATTERN).format(date)
        else return rawString
    }
}