package com.miracle.net

import androidx.annotation.IntDef

@IntDef(
    ErrorCode.OTHER,//其他错误，目前还未关注和处理的
    ErrorCode.NETWORK_EXCEPTION,//无网络，网络连接异常
    ErrorCode.HOST_ERROR,//host异常
    ErrorCode.TIMEOUT,//超时
    ErrorCode.CANCEL,//取消
    ErrorCode.JSON_SYNTAX_EXCEPTION,//数据解析异常
    ErrorCode.OK,//请求正常
    ErrorCode.UNAUTHORIZED,//无授权
    ErrorCode.CUSTOM_FIRST,//自定义，可自行修改
    ErrorCode.VALUE_IS_NULL//空值
)
@Retention(AnnotationRetention.SOURCE)
annotation class ErrorCode {
    companion object {
        const val OTHER = 0
        const val NETWORK_EXCEPTION = 1
        const val HOST_ERROR = 2
        const val TIMEOUT = 3
        const val CANCEL = 4
        const val JSON_SYNTAX_EXCEPTION = 5
        const val OK = 200
        const val UNAUTHORIZED = 401
        const val CUSTOM_FIRST = 600
        const val VALUE_IS_NULL = CUSTOM_FIRST + 1
    }
}