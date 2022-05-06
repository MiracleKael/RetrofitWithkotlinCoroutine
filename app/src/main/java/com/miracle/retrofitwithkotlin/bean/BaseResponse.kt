package com.miracle.retrofitwithkotlin.bean

/**
 * created by miracle on 2022/4/24
 * Desc:
 */
data class BaseResponse<T>(
    var errorCode: Int,
    var errorMsg: String,
    var data: T?
)
