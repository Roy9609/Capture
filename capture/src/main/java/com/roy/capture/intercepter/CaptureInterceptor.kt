package com.roy.capture.intercepter

import android.net.Uri
import android.util.Log
import com.roy.capture.bean.CaptureData
import com.roy.capture.utils.formatData
import com.roy.capture.CaptureLib
import com.roy.capture.utils.TIME_LONG
import com.roy.capture.utils.isProbablyUtf8
import com.roy.capture.utils.promisesBody
import com.roy.capture.utils.readString
import com.roy.capture.utils.toJsonString
import com.roy.capturelib.middle.CaptureHelper

import okhttp3.*
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.min

class CaptureInterceptor : Interceptor {


    private var maxContentLength = 5L * 1024 * 1024

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var context = CaptureHelper.instance.context
        if (context!=null && !CaptureLib.instance.getSwitchCapture()) {
            return chain.proceed(request)
        }
        val captureData = CaptureData()
        captureData.method = request.method
        val url = request.url.toUrl().toString()
        captureData.url = url
        if (url.isNotBlank()) {
            val uri = Uri.parse(url)
            captureData.host = uri.host
            captureData.path = uri.path + if (uri.query != null) "?" + uri.query else ""
            captureData.scheme = uri.scheme
        }


        val requestBody = request.body
        captureData.requestTime = Date().formatData(TIME_LONG)
        captureData.requestTimeMillisecond = Date().time;
        requestBody?.contentType()?.let { captureData.requestContentType = it.toString() }
        captureData.requestHeaders = request.headers?.toJsonString()

        val startTime = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            captureData.errorMsg = e.toString()
            insert(captureData)
            throw e
        }
        try {

            captureData.responseTime = Date().formatData(TIME_LONG)
            captureData.duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)
            captureData.protocol = response.protocol.toString()
            captureData.responseCode = response.code
            captureData.responseMessage = response.message
            captureData.responseHeaders = response.headers.toJsonString()
            when {
                requestBody == null || bodyHasUnknownEncoding(request.headers)  -> {
                }
                requestBody is MultipartBody -> {
                    var formatRequestBody = ""
                    requestBody.parts.forEach {
                        val isStream =
                            it.body.contentType()?.toString()?.contains("otcet-stream") == true
                        val key = it.headers?.value(0)
                        formatRequestBody += if (isStream) {
                            "${key}; value=文件流\n"
                        } else {
                            val value = it.body.readString()
                            "${key}; value=${value}\n"
                        }
                    }
                    captureData.requestBody = decrypt( captureData.url,formatRequestBody)
                }
                else -> {
                    val buffer = Buffer()
                    requestBody.writeTo(buffer)
                    val charset: Charset =
                        requestBody.contentType()?.charset(StandardCharsets.UTF_8)
                            ?: StandardCharsets.UTF_8
                    if (buffer.isProbablyUtf8()) {
                        captureData.requestBody = decrypt( captureData.url,buffer.readString(charset))
                    }
                }
            }

            val responseBody = response.body

            responseBody?.let { body ->
                body.contentType()?.let { captureData.responseContentType = it.toString() }
            }

            val bodyHasUnknownEncoding = bodyHasUnknownEncoding(response.headers)

            if (responseBody != null && response.promisesBody() && !bodyHasUnknownEncoding) {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer()

                if (bodyGzipped(response.headers)) {
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val charset: Charset = responseBody.contentType()?.charset(StandardCharsets.UTF_8)
                    ?: StandardCharsets.UTF_8

                if (responseBody.contentLength() != 0L && buffer.isProbablyUtf8()) {
                    val body = readFromBuffer(buffer.clone(), charset)
                    //captureData.responseBody = decrypt(captureData.url,formatBody(body, captureData.responseContentType));
                    captureData.responseBody = decrypt(captureData.url,body);
                }
                captureData.responseContentLength = buffer.size
            }
            insert(captureData)
            return response
        } catch (e: Exception) {
            Log.d("MonitorHelper", e.message ?: "")
            return response
        }
    }


    private fun decrypt(requestUrl: String?,body:String?):String?{
        try {
            val url = "https://xx.xx.xxx.com";
            val url2 = "https://xxxx.xxx.xxxxxx.com"
            //这段正则的意思是只获取域名
            val reg = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?"

            val pattern: Pattern =  Pattern.compile(reg)
            val matcher: Matcher = pattern.matcher("{${requestUrl}}")
            var host = ""
            while (matcher.find()) {
               host = matcher.group()
            }
            if(url.contains(host) || url2.contains(host)){ //进行解密
               return  body //aes.decrypt(body)
            }else{
                return body
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""

    }



    private fun insert(captureData: CaptureData) {
        CaptureHelper.instance.insert(captureData)
    }


    private fun update(captureData: CaptureData) {
        CaptureHelper.instance.update(captureData)
    }


    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    private fun readFromBuffer(buffer: Buffer, charset: Charset?): String {
        val bufferSize = buffer.size
        val maxBytes = min(bufferSize, maxContentLength)
        var body: String = try {
            buffer.readString(maxBytes, charset!!)
        } catch (e: EOFException) {
            "\\n\\n--- Unexpected end of content ---"
        }
        if (bufferSize > maxContentLength) {
            body += "\\n\\n--- Content truncated ---"
        }
        return body
    }

    private fun bodyGzipped(headers: Headers): Boolean {
        return "gzip".equals(headers["Content-Encoding"], ignoreCase = true)
    }

}