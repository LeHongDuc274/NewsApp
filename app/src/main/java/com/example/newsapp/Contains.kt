package com.example.newsapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.os.Build

object Contains {

    const val BASE_URL = "https://newsapi.org/"
    const val MY_API_KEY = "2e3723c25cce412899f9854450097ec8"
    const val DELAY_TIME_SEARCH = 200L
    const val ARTICLE_ARGS_KEY = "com.example.newsapp.articlekey"

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
}