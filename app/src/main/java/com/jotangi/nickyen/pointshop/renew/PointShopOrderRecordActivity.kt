package com.jotangi.nickyen.pointshop.renew

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nickyen.R
import com.jotangi.nickyen.databinding.ActivityPointshopOrderRecordBinding
import com.jotangi.nickyen.pointshop.EcorderListRequest
import com.jotangi.nickyen.pointshop.renew.adapter.PointShopOrderRecordAdapter

class PointShopOrderRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPointshopOrderRecordBinding
    private lateinit var viewModel: PointShopViewNewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointshopOrderRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this,R.color.typeRed)
        viewModel = ViewModelProvider(this).get(PointShopViewNewModel::class.java)

        binding.apply {
            ibGoBack.setOnClickListener {
                finish()
            }
            progressBar.visibility = View.VISIBLE
        }

        viewModel.ecorderListItem.observe(this){
            binding.progressBar.visibility = View.GONE
            if(it.isNotEmpty()){
                binding.recycleView.apply {
                    layoutManager = LinearLayoutManager(this@PointShopOrderRecordActivity,LinearLayoutManager.VERTICAL,false)
                    addItemDecoration(DividerItemDecoration(this@PointShopOrderRecordActivity,DividerItemDecoration.VERTICAL))
                    adapter = PointShopOrderRecordAdapter(this@PointShopOrderRecordActivity,it)
                }
            }else{
                binding.tvNoData.visibility = View.VISIBLE
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getEcorderList(
            EcorderListRequest()
        )
    }
}