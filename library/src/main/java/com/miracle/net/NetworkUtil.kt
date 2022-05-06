package com.miracle.net

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

/**
 * created by miracle on 2022/4/25
 * Desc:
 */
object NetworkUtil {
    val TAG: String? = NetworkUtil::class.simpleName

    fun isConnected(): Boolean {
        return ContextUtils.getApplicationContext()?.let {
            val connectivityManager =
                it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val result: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                networkCapabilities != null
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                networkInfo != null && networkInfo.isAvailable
            }
            result
        } ?: false
    }
}