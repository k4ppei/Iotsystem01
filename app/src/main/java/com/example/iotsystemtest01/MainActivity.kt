package com.example.iotsystemtest01

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Download Button
        val buttonDownload: Button = findViewById(R.id.buttonDownload)
        buttonDownload.setOnClickListener {
            // ボタンがクリックされたとき# Assistant should continue the code from previous message.
            // ボタンがクリックされたときに画像をダウンロード
            downloadImage(1) // ダウンロードしたい画像のID
        }
    }
    fun downloadImage(id: Int) {
        val url = "http://192.168.0.1:5005/download/$id"
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                } else {
                    // レスポンスボディをBitmapに変換
                    val inputStream: InputStream = response.body?.byteStream() ?: return
                    val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                    // UIスレッドでImageViewに設定
                    runOnUiThread {
                        val imageView: ImageView = findViewById(R.id.imageView)
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
        })
    }
}
