package com.miracle.net

import android.util.Log
import com.google.gson.JsonSyntaxException
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

/**
 * created by miracle on 2022/4/24
 * Desc:
 */
abstract class BaseNetworkApi<T>() {
    private val TAG = BaseNetworkApi::class.simpleName
    private var newUrl: String? = null
    private var interceptors: ArrayList<Interceptor>? = null

    constructor(url: String) : this() {
        this.newUrl = url
    }

    constructor(interceptors: ArrayList<Interceptor>) : this() {
        this.interceptors = interceptors
    }

    constructor(url: String, interceptors: ArrayList<Interceptor>) : this() {
        this.newUrl = url
        this.interceptors = interceptors
    }

    protected val service: T by lazy {
        createRetrofit().create(getServiceClass())
    }

    private fun getServiceClass(): Class<T> {
        val genType = javaClass.genericSuperclass as ParameterizedType
        return genType.actualTypeArguments[0] as Class<T>
    }

    private fun createRetrofit(): Retrofit {
        return createRetrofit(newUrl ?: NetConfig.baseUrl, interceptors)
    }

    private fun createRetrofit(url: String, interceptors: ArrayList<Interceptor>?): Retrofit {
        if (url.isEmpty()) {
            Log.e(TAG, "url is Empty")
        }
        return Retrofit.Builder()
            .baseUrl(url)
            .client(createClient(interceptors))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createClient(interceptors: ArrayList<Interceptor>?): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(8, TimeUnit.SECONDS)
        NetConfig.commonInterceptors.forEach { interceptor ->
            okHttpClient.addInterceptor(interceptor)
        }
        interceptors?.forEach { interceptor ->
            okHttpClient.addInterceptor(interceptor)
        }
        return okHttpClient.build()
    }

    protected suspend fun <T> getResult(block: suspend () -> T): Result<T> {
        runCatching {
            block()
        }.onSuccess {
            Log.i(TAG, "onSuccess")
            return Result.success(it)
        }.onFailure {
            //处理UnknownHostException
            when (it) {
                is UnknownHostException -> {
                    //处理Host未知异常返回
                    return dealUnknownHostException(it)
                }
                is ConnectException,
                is SocketTimeoutException,
                is SocketException,
                -> {
                    //处理超时返回
                    return dealTimeoutException(it)
                }
                is HttpException -> {
                    //处理HttpException返回，包含了登录失效
                    return dealHttpException(it)
                }
                is NumberFormatException,
                is IllegalArgumentException,
                is IllegalStateException,
                is JsonSyntaxException -> {
                    //处理Json解析异常
                    return dealJsonSyntaxException(it)
                }
                is CancellationException -> {
                    Log.i(TAG, "CancellationException")
                    return Result.failure(RequestException.of(ErrorCode.CANCEL, "cancel"))
                }
                else -> {
                    //处理一些其他的异常
                    return dealOtherException(it)
                }
            }
        }
        return Result.failure(RequestException.of(ErrorCode.OTHER, "unknown"))
    }

    private fun <T1> dealJsonSyntaxException(it: Throwable): Result<T1> {
        Log.i(TAG, "dealJsonSyntaxException")
        return Result.failure(
            RequestException.of(
                ErrorCode.JSON_SYNTAX_EXCEPTION,
                it.message ?: "unknown"
            )
        )
    }

    private fun <T1> dealHttpException(it: HttpException): Result<T1> {
        Log.i(TAG, "httpException")
        return if (it.code() == ErrorCode.UNAUTHORIZED) {
            // 这里刷新token，然后重试

            Result.failure(RequestException.of(ErrorCode.UNAUTHORIZED, "授权失败，请重新登录授权"))
        } else {
            Result.failure(
                RequestException.of(
                    ErrorCode.OTHER,
                    it.message ?: "unknown"
                )
            )
        }
    }

    private fun <T1> dealOtherException(it: Throwable): Result<T1> {
        Log.i(TAG, "dealOtherException")
        return Result.failure(
            RequestException.of(
                ErrorCode.OTHER,
                it.message ?: "unknown"
            )
        )
    }

    private fun <T1> dealTimeoutException(it: Throwable): Result<T1> {
        Log.i(TAG, "dealTimeoutException")
        return Result.failure(
            RequestException.of(
                ErrorCode.TIMEOUT,
                "请求超时"
            )
        )
    }

    private fun <T1> dealUnknownHostException(it: UnknownHostException): Result<T1> {
        Log.i(TAG, "it is UnknownHostException")
        if (NetworkUtil.isConnected()) {
            //host错误或者服务器开小差了
            return Result.failure(
                RequestException.of(
                    ErrorCode.HOST_ERROR,
                    it.message ?: "host错误或者服务器开小差了"
                )
            )
        } else {
            //设备网络连接异常的情况
            return Result.failure(
                RequestException.of(
                    ErrorCode.NETWORK_EXCEPTION,
                    "网络连接异常"
                )
            )
        }
    }
}