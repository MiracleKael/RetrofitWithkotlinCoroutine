package com.miracle.retrofitwithkotlin

import android.app.Application
import android.util.Log
import com.miracle.net.NetConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * created by miracle on 2022/4/24
 * Desc:
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val list = ArrayList<Interceptor>()
        list.add(initLogConfig())
        NetConfig.initBaseUrl("https://www.wanandroid.com")
            .initCommonInterceptors(list)
            .setTimeout(5, TimeUnit.SECONDS)
    }

    //初始化日志拦截器
    private fun initLogConfig(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message -> Log.i("MyApplication NetLog", message) }.apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
}