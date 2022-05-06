package com.miracle.retrofitwithkotlin

import com.miracle.net.BaseNetworkApi
import com.miracle.retrofitwithkotlin.bean.BaseResponse
import com.miracle.retrofitwithkotlin.bean.LoginResp
import com.miracle.retrofitwithkotlin.bean.RegisterResp
import okhttp3.Interceptor
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * created by miracle on 2022/5/5
 * Desc:
 */
class LoginApi(interceptors: ArrayList<Interceptor>) :
    BaseNetworkApi<LoginApi.LoginApiService>(interceptors) {


    interface LoginApiService {
        @FormUrlEncoded
        @POST("/user/register")
        suspend fun register(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("repassword") repassword: String
        ): BaseResponse<RegisterResp>

        @FormUrlEncoded
        @POST("/user/login")
        suspend fun login(
            @Field("username") username: String,
            @Field("password") password: String
        ): BaseResponse<LoginResp>
    }

    suspend fun register(username: String, password: String, repassword: String) = getResult {
        service.register(username, password, repassword)
    }

    suspend fun login(username: String, password: String) = getResult {
        service.login(username, password)
    }
}