package io.github.kuroka3.nof

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.Duration

class SearchActivity : AppCompatActivity() {
    lateinit var edtUrl: EditText
    lateinit var sel: Button
    lateinit var web: WebView

    companion object {
        lateinit var instance: SearchActivity
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        instance = this

        edtUrl = findViewById(R.id.select_url)
        sel = findViewById(R.id.select_this)
        web = findViewById(R.id.webView)

        web.webViewClient = NovelWebViewClient()

        val webSet = web.settings
        webSet.builtInZoomControls = true
        webSet.javaScriptEnabled = true

        web.loadUrl(edtUrl.text.toString())

        sel.setOnClickListener {
            var text = edtUrl.text.toString()

            if(!text.startsWith("https://novelpia.com/novel/")) {
                Toast.makeText(this, "선택한 사이트가 노벨피아의 소설이 아닙니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(text.contains("?")) {
                val index = text.indexOf("?")
                text = text.substring(0, index)
            }

            text = text.replace("https://novelpia.com/novel/", "")

            val intent = Intent(this@SearchActivity, DownloadActivity::class.java)
            intent.putExtra("selNov", text)
            startActivity(intent)
            finish()
        }
    }

    class NovelWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            instance.edtUrl.setText(url)
            super.onPageStarted(view, url, favicon)
        }
    }
}