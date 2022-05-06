package com.miracle.retrofitwithkotlin.bean

/**
 * created by miracle on 2022/5/5
 * Desc:
 */
data class LoginResp(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val coinCount: Int,
    val collectIds: List<Any>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)