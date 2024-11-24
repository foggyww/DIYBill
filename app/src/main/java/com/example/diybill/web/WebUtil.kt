package com.example.diybill.web

import com.example.diybill.db.Bill
import com.example.diybill.db.IOCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object WebUtil {

    const val BASE_URL = "http://60.204.154.28:5050/api/query"

    suspend fun requestChatBot(
        list:List<Bill>,
        ioCallback: IOCallback<String>
    ){
        val client = OkHttpClient()

        val json = """
            {
            'type': 'chatbot',
            'data': '吃饭:1000,买衣服:20,教育:500'
            }
        """.trimIndent()
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(BASE_URL)
            .post(requestBody)
            .build()
        withContext(Dispatchers.IO){
            try {
                val result=client.newCall(request).execute()
                result.body?.let {
                    val jsonObject = JSONObject(it.string())
                    ioCallback.onCompleted(jsonObject.toString())
                } ?:run {
                    throw Throwable("返回为空")
                }
            }catch (t:Throwable){
                ioCallback.onErrorHandler(t)
            }
        }
    }

}