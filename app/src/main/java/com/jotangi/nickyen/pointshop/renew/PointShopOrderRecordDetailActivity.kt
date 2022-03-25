package com.jotangi.nickyen.pointshop.renew

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nickyen.R
import com.jotangi.nickyen.databinding.ActivityPointshopOrderRecordDetailBinding
import com.jotangi.nickyen.pointshop.EcorderInfoRequest
import com.jotangi.nickyen.pointshop.renew.adapter.PointShopOrderRecordDetailAdapter

class PointShopOrderRecordDetailActivity :AppCompatActivity(){
    private lateinit var binding:ActivityPointshopOrderRecordDetailBinding
    private lateinit var viewModel: PointShopViewNewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointshopOrderRecordDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.typeRed)
        viewModel = ViewModelProvider(this).get(PointShopViewNewModel::class.java)

        binding.apply {
            ibGoBack.setOnClickListener {
                finish()
            }
            progressBar.visibility = View.VISIBLE
        }

        val order_no = intent.extras?.getString("order_no")
        order_no.let {
            viewModel.getEcorderInfo(EcorderInfoRequest(it))
        }

        viewModel.ecorderInfoItem.observe(this){
            binding.progressBar.visibility = View.GONE
            if(it.isNotEmpty()){
                binding.apply {
                    tvOrderNo.text = it[0].order_no
                    tvOrderDate.text = it[0].order_date
                    tvOrderAmount.text = it[0].order_amount
                    tvOrderStatus.text = it[0].order_status
                    tvOrderPayStatus.text = it[0].pay_status
                    tvOrderCar.text = it[0].deliverytype
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@PointShopOrderRecordDetailActivity,LinearLayoutManager.VERTICAL,false)
                        addItemDecoration(DividerItemDecoration(this@PointShopOrderRecordDetailActivity,DividerItemDecoration.VERTICAL))
                        adapter = PointShopOrderRecordDetailAdapter(this@PointShopOrderRecordDetailActivity,it[0].product_list!!)
                    }
                    tvOrderTotalAccount.text = it[0].order_amount
                    tvOrderPoint.text = it[0].bonus_point
                    tvOrderTotal.text = it[0].order_amount
                }
            }else{
                binding.tvNoData.visibility = View.VISIBLE
            }
        }
    }
}