package com.miracle.retrofitwithkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.miracle.retrofitwithkotlin.databinding.ActivitySecBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response

class SecActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySecBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySecBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val list = arrayListOf<Interceptor>()
        list.add(ReceivedCookiesInterceptor())
        val loginApi = LoginApi(list)

        viewBinding.btRegist.setOnClickListener {
            val userName = viewBinding.etUserName.text.toString()
            val userPwd = viewBinding.etPwd.text.toString()
            val reUserPwd = userPwd

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    loginApi.register(userName, userPwd, reUserPwd)
                }.onSuccess {
                    if (it.errorCode == 0) {
                        Toast.makeText(this@SecActivity, "注册成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SecActivity, it.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }.onFailure {
                    Log.i("miracle", "onFailure==" + it.message)
                    Toast.makeText(this@SecActivity, "it.message", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewBinding.btLogin.setOnClickListener {
            val userName = viewBinding.etUserName.text.toString()
            val userPwd = viewBinding.etPwd.text.toString()
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    loginApi.login(userName, userPwd)
                }.onSuccess {
                    if (it.errorCode == 0) {
                        Toast.makeText(this@SecActivity, "登录成功", Toast.LENGTH_SHORT).show()
                        it.data?.let { loginData ->

                        }
                    } else {
                        Toast.makeText(this@SecActivity, "登录失败==" + it.errorMsg, Toast.LENGTH_SHORT)
                            .show()
                    }

                }.onFailure {
                    Log.i("miracle", "onFailure==" + it.message)
                    Toast.makeText(this@SecActivity, "it.message", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    class ReceivedCookiesInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            val headerList = response.headers("Set-Cookie")
            if (headerList.isNotEmpty()) {
                //解析cookie
                for (header in headerList) {
                    Log.i("miracle", "header==" + header)
                }
            }
            return response
        }
    }

}