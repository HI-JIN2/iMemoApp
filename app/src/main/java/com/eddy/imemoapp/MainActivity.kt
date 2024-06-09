package com.eddy.imemoapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView // 웹뷰 변수 선언
    private lateinit var progressBar: ProgressBar

    private var doubleBackToExitPressedOnce = false
    private val handler = Handler(Looper.getMainLooper())
    private val backPressRunnable = Runnable { doubleBackToExitPressedOnce = false }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView) // 웹뷰 객체 가져오기
        progressBar = findViewById(R.id.progressBar)

//        webView.webViewClient = WebViewClient() // 웹뷰 클라이언트 생성

//        if (savedInstanceState != null) webView.restoreState(savedInstanceState)
//        else webView.loadUrl("https://www.icloud.com/notes")


        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(
                view: WebView?,
                url: String?,
                favicon: android.graphics.Bitmap?
            ) {
                progressBar.visibility = View.VISIBLE
                webView.visibility = View.INVISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                handler.postDelayed({
                    webView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }, 3000) // 3초 후에 ProgressBar를 숨김
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://www.icloud.com/notes")


        // OnBackPressedCallback 등록
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    if (doubleBackToExitPressedOnce) {
                        finish()
                    } else {
                        doubleBackToExitPressedOnce = true
                        Toast.makeText(this@MainActivity, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT)
                            .show()
                        handler.postDelayed(backPressRunnable, 2000) // 2초 후에 다시 false로 설정
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(backPressRunnable) // 액티비티가 파괴될 때 핸들러 콜백 제거
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }
}