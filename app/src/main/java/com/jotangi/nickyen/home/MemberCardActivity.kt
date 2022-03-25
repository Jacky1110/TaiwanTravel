package com.jotangi.nickyen.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.databinding.ActivityMemberCardBinding
import com.jotangi.nickyen.home.adapter.MemberCardAdapter
import com.jotangi.nickyen.model.MemberBean
import com.jotangi.nickyen.shop.ProductFragment
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MemberCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMemberCardBinding
    private lateinit var memberCardAdapter: MemberCardAdapter
    private var memberCardList = mutableListOf<MemberBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.typeRed)
        binding.ibGoBack.setOnClickListener { finish() }

        initMemberCardList()
        getMemberCardList()
        binding.refreshLayout.setOnRefreshListener {
            memberCardList.clear()
            getMemberCardList()
        }
    }

    private fun initMemberCardList() {
        memberCardAdapter = MemberCardAdapter(this, memberCardList)
        memberCardAdapter.onItemClick = {
            Timber.e("$it")
                val productFragment = ProductFragment.newInstance2(it,"")
                supportFragmentManager?.beginTransaction()?.replace(R.id.member2Layout, productFragment, null)?.addToBackStack(null)?.commit()
        }
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@MemberCardActivity)
            adapter = memberCardAdapter
        }
    }

    private fun getMemberCardList() {
        binding.progressBar.makeVisible()
        binding.refreshLayout.isRefreshing = true
        ApiConnection.getMemberCardList(object : ApiConnection.OnConnectResultListener {
            override fun onSuccess(jsonString: String) {
                Timber.e("onSuccess -> $jsonString")
                GlobalScope.launch(Dispatchers.Main) {
                    binding.progressBar.makeGone()
                    binding.refreshLayout.isRefreshing = false
                    try {
                        val type = object : TypeToken<ArrayList<MemberBean?>?>() {}.type
                        Gson().fromJson<ArrayList<MemberBean>>(jsonString, type)?.let {
                            Timber.e("onSuccess -> $it")
                            memberCardList.addAll(it)
                            binding.tvNumber.text = memberCardList.size.toString()
                            memberCardAdapter?.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onFailure(message: String) {
                GlobalScope.launch(Dispatchers.Main) {
                    binding.progressBar.makeGone()
                    binding.refreshLayout.isRefreshing = false
                }
                Timber.e("onFailure -> $message")
            }
        })
    }
}