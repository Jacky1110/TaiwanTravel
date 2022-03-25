package com.jotangi.nickyen.pointshop.renew

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jotangi.nickyen.R
import com.jotangi.nickyen.databinding.ActivityPointshopPayBinding

class PointShopPayActivity :AppCompatActivity(){
    private lateinit var binding: ActivityPointshopPayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointshopPayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.typeRed)
        binding.apply {
            ibGoBack.setOnClickListener {
                finish()
            }
        }

        var pay_url:String = intent.extras?.get("url").toString()
        Log.d("eee_PointShopPayActivity",pay_url)
        //pay_url = "https://eho.jotangi.net:10443/login.html"
        setupWeb(pay_url)
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        var url = intent?.data.toString()
//        Log.d("eee_onnewintent",url)
//        //to payresultactivity
//    }

    private fun setupWeb(url:String){
        binding.webView.run {
            with(settings){
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                javaScriptCanOpenWindowsAutomatically = true
            }
            webViewClient = object : WebViewClient(){
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Log.d("webview",request?.url.toString())
                    request?.url?.toString()?.let { url ->
                        return when{
                            url.startsWith("http") -> {
                                loadUrl(url)
                                true
                            }
                            else -> {
                                try {
                                    if (url.startsWith("line://") || url.startsWith("spot://")) {
                                        if(url.startsWith("spot://")){
                                            val intent2 = Intent(this@PointShopPayActivity,PointShopOrderRecordActivity::class.java)
                                            intent2.addFlags(FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP)
                                            startActivity(intent2)
                                            finish()
                                        }else
                                            startActivity(
                                                Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                            )
                                        true
                                    } else{
                                        startActivity(
                                            Intent.parseUri(url,Intent.URI_INTENT_SCHEME)
                                        )
                                        true
                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                    Toast.makeText(this@PointShopPayActivity,"未安裝相關付款應用程式",Toast.LENGTH_SHORT).show()
                                    false
                                }
                            }
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }
            loadUrl(url)
        }
    }

    override fun onBackPressed() {
        if(binding.webView.canGoBack()){
            binding.webView.goBack()
        }else
            super.onBackPressed()
    }
}