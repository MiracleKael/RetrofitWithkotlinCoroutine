package com.miracle.retrofitwithkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.miracle.net.RequestException
import com.miracle.retrofitwithkotlin.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btJumpSec.setOnClickListener {
            startActivity(Intent(this, SecActivity::class.java))
        }

        loadBannerData()
    }


    private fun loadBannerData() {
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
    }
}