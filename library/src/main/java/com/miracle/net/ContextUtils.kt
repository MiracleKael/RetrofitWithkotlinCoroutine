package com.miracle.net

import android.content.Context

/**
 * created by miracle on 2022/4/29
 * Desc:用于lib中获取Application中的上下文
 */
object ContextUtils {

    private var applicationContext: Context? = null
    fun getApplicationContext(): Context? {
        if (null != applicationContext) {
            return applicationContext
        }
        val activityThread = getActivityThread()
        if (null != activityThread) {
            try {
                val getApplication = activityThread.javaClass.getDeclaredMethod("getApplication")
                getApplication.isAccessible = true
                applicationContext = getApplication.invoke(activityThread) as Context
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return applicationContext
    }

    private fun getActivityThread(): Any? {
        try {
            val clz = Class.forName("android.app.ActivityThread")
            val method = clz.getDeclaredMethod("currentActivityThread")
            method.isAccessible = true
            return method.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}