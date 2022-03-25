package com.jotangi.nickyen.home.notify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.MainActivity
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.api.ApiConnection.OnConnectResultListener
import com.jotangi.nickyen.databinding.ActivityNotifyBinding
import com.jotangi.nickyen.home.adapter.MemberCardAdapter
import com.jotangi.nickyen.merch.MerchMainActivity
import com.jotangi.nickyen.model.MemberBean
import com.jotangi.nickyen.questionnaire.QuestionnaireActivity
import com.jotangi.nickyen.shop.ProductFragment
import com.jotangi.nickyen.utils.SharedPreferencesUtil
import com.jotangi.nickyen.utils.makeGone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class NotifyActivity : AppCompatActivity() {

    companion object {
        const val PAGE_KEY = "page"

        @JvmStatic
        fun start(context: Context?, page: String) {
            context?.also {
                val intent = Intent(it, NotifyActivity::class.java)
                intent.putExtra(PAGE_KEY, page)
                it.startActivity(intent)
            }
        }
    }

    private var page: String? = null

    private lateinit var binding: ActivityNotifyBinding
    private var notifyList = mutableListOf<NotifyModel>()
    private lateinit var notifyAdapter: NotifyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.typeRed)

        intent?.apply {
            page = getStringExtra(PAGE_KEY)
        }

        binding.ibGoBack.setOnClickListener {

            if (page == "user") {
                startActivity(
                    Intent(this@NotifyActivity, MainActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            } else if (page == "merch") {
                startActivity(
                    Intent(this@NotifyActivity, MerchMainActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
//        when (page) {
//            "user" -> loadData()
//            "merch" ->  loadData()
//        }
        loadData()
        initNotify()
        binding.refreshLayout.setOnRefreshListener {
            getNotify()
//            initNotify()
        }
    }

    private fun initNotify() {
        notifyAdapter = NotifyAdapter(this@NotifyActivity, notifyList)
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@NotifyActivity)
            adapter = notifyAdapter
        }
    }

    private fun getNotify() {
        binding.refreshLayout.isRefreshing = true
        ApiConnection.getPushList(object : OnConnectResultListener {
            override fun onSuccess(jsonString: String) {
                GlobalScope.launch(Dispatchers.Main) {
                    binding.refreshLayout.isRefreshing = false
                    SharedPreferencesUtil(this@NotifyActivity, "notify")
                    SharedPreferencesUtil.putData("info", Gson().toJson(jsonString))
                    val type = object : TypeToken<ArrayList<NotifyModel?>?>() {}.type
                    Gson().fromJson<ArrayList<NotifyModel>>(jsonString, type)?.let {
                        Timber.e("onSuccess -> $it")
                        notifyList.clear()
                        notifyList.addAll(it)
                        notifyAdapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(message: String) {
                GlobalScope.launch(Dispatchers.Main) {
                    binding.refreshLayout.isRefreshing = false
                    loadData()
                    initNotify()
                }
                Timber.e("onFailure -> $message")
            }
        })
    }

    private fun loadData() {
        SharedPreferencesUtil(this, "notify")
        var info: String = SharedPreferencesUtil.getData("info", "") as String
        val type = object : TypeToken<ArrayList<NotifyModel?>?>() {}.type
        Gson().fromJson<ArrayList<NotifyModel>>(info, type)?.let {
            notifyList = it
        }
    }
}