package com.example.iotsystemtest01
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// GETボタン
        val buttonGet: Button = findViewById(R.id.buttonGet)
        buttonGet.setOnClickListener {
            // 指定URLにGETリクエストを実行するオブジェクトを生成
            val client:OkHttpClient = OkHttpClient()
            val url:String = "http://192.168.0.1:5005/json2"
            val request = Request.Builder()
                .url(url)
                .build()
            // 非同期通信でHTTPリクエストを送信
            client.newCall(request).enqueue(object : Callback {
                // 失敗したとき
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "GET : ERROR!", Toast.LENGTH_SHORT).show()
                    }
                }
                // 成功したとき
                override fun onResponse(call: Call, response: Response) {
                    // JSONデータを取得
                    val jsonData: JSONObject = JSONObject(response.body?.string())
                    runOnUiThread {
                        // TextViewにセットして表示
                        val textView: TextView = findViewById(R.id.textView)
                        textView.text = jsonData.toString()
                        Toast.makeText(applicationContext, "GET : OK!", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
// POST用入力欄、POSTボタン
        val editTextId: EditText = findViewById(R.id.editTextId)
        val editTextName: EditText = findViewById(R.id.editTextName)
        val buttonPost: Button = findViewById(R.id.buttonPost)
        buttonPost.setOnClickListener {
// POSTのボディにJSONデータを設定
            val jsonObj: JSONObject = JSONObject()
            jsonObj.put("ID", editTextId.text.toString())
            jsonObj.put("name", editTextName.text.toString())
            val body: RequestBody = jsonObj.toString().toRequestBody()
// 指定URLにPOSTリクエストを実行するオブジェクトを生成
            val client:OkHttpClient = OkHttpClient()
            val url:String = "http://192.168.0.1:5005"
            val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type","application/json")
                .build()
// 非同期通信でHTTPリクエストを送信
            client.newCall(request).enqueue(object : Callback {
                // 失敗したとき
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "POST : ERROR!", Toast.LENGTH_SHORT).show()
                    }
                }
                // 成功したとき
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "POST : OK!", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}
