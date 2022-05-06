使用方法：

1.写一个xxxApi类继承BaseNetworkApi，参考Demo中TestApi

class TestApi : BaseNetworkApi<TestApi.TestApiService>() {

```
interface TestApiService {  
    @GET("banner/json")  
    suspend fun getBannerData(): BannerResp  
}  

suspend fun getBannerData() = getResult {  
    service.getBannerData()  
}  
```

}

2.超简单调用接口
```
lifecycleScope.launch {  
   withContext(Dispatchers.IO) {  
      TestApi().getBannerData()  
   }.onFailure {  
      val exception = it as RequestException  
      Log.i("miracle", "onFailure==" + exception.message)  
   }.onSuccess {  
      Log.i("miracle", "onSuccess")  
      viewBinding.content.text = it.toString()  
   }  
}
```
3.如果想知道接口的具体异常，可自行处理

onFailure {  
 val exception = it as RequestException  
 ......

}

```
class RequestException private constructor(val code: Int, message: String) : RuntimeException(message) {
override fun toString(): String {  
    return "exception code is $code msg is $message"  
}  

companion object {  
    fun of(code: Int, message: String) = RequestException(code, message)  
}  
```

}

Code的枚举值如下：

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

4.其他的有需要后续完善