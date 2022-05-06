package com.miracle.retrofitwithkotlin

import com.miracle.net.BaseNetworkApi
import com.miracle.retrofitwithkotlin.bean.BannerResp
import retrofit2.http.GET

/**
 * created by miracle on 2022/4/24
 * Desc:
 */
class TestApi : BaseNetworkApi<TestApi.TestApiService>() {

    interface TestApiService {
        @GET("banner/json")
        suspend fun getBannerData(): BannerResp
    }

    suspend fun getBannerData() = getResult {
        service.getBannerData()
    }


}