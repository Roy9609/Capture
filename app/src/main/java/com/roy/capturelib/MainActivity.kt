package com.roy.capturelib

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.roy.captureoperate.CaptureOperate
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


/**
 *    desc   :
 *    date   : 2025/9/4 14:55
 *    author : Roy
 *    version: 1.0
 */
class MainActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.textView).setOnClickListener {
            sendRequest("https://www.wanandroid.com/article/list/0/json")

            CaptureOperate.instance.startCaptureListActivity(this)
        }

    }



    private fun sendRequest(url: String) {
        val okHttpClientBuilder = OkHttpClient.Builder()
        CaptureOperate.instance.captureInterceptor()?.apply {
            okHttpClientBuilder.addInterceptor(this)
        }

         val client =   okHttpClientBuilder.build()

        val request = Request.Builder().url(url).build();
        client.newCall(request).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, result, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}