package com.miracle.net

import okhttp3.Interceptor
import java.util.concurrent.TimeUnit

/**
 * created by miracle on 2022/4/29
 * Desc:
 */
object NetConfig {
    internal var baseUrl: String = ""

    internal var commonInterceptors = arrayListOf<Interceptor>()

    private var timeout: Long = 8

    private var timeUnit: TimeUnit = TimeUnit.SECONDS

    /**
     * 初始化baseUrl
     */
    fun initBaseUrl(baseUrl: String): NetConfig {
        this.baseUrl = baseUrl
        return this
    }

    /**
     * 初始化接口通用的拦截器
     */
    fun initCommonInterceptors(interceptors: ArrayList<Interceptor>): NetConfig {
        commonInterceptors.clear()
        commonInterceptors.addAll(interceptors)
        return this
    }

    fun setTimeout(timeout: Long, timeUnit: TimeUnit): NetConfig {
        this.timeout = timeout
        this.timeUnit = timeUnit
        return this
    }

}