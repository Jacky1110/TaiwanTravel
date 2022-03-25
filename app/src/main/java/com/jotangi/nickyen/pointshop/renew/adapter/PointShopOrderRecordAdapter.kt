package com.jotangi.nickyen.pointshop.renew.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nickyen.R
import com.jotangi.nickyen.pointshop.renew.PointShopEcorderModel
import com.jotangi.nickyen.pointshop.renew.PointShopOrderRecordDetailActivity
import com.jotangi.nickyen.pointshop.renew.PointShopPayActivity

class PointShopOrderRecordAdapter(mContext:Context,private val mData:List<PointShopEcorderModel>)
    : RecyclerView.Adapter<PointShopOrderRecordAdapter.ViewHolder>() {
    var context = mContext

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_point_shop_ecorder,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(v: View) :RecyclerView.ViewHolder(v){
        val no:TextView = v.findViewById(R.id.tv_ecorder_no)
        val date:TextView = v.findViewById(R.id.tv_ecorder_date)
        val num:TextView = v.findViewById(R.id.tv_ecorder_num)
        val status:TextView = v.findViewById(R.id.tv_ecorder_status)
        val pay: Button = v.findViewById(R.id.btn_ecorder_pay)
        val detail: Button = v.findViewById(R.id.btn_ecorder_detail)

        fun bind(model:PointShopEcorderModel){
            no.text = model.order_no
            date.text = model.order_date
            num.text = model.order_pay
            if(model.pay_status == "1") {
                status.text = "已付款"
                pay.visibility = View.GONE
            }
            else {
                status.text = "未付款"
                pay.visibility = View.VISIBLE
            }
            pay.setOnClickListener {
                val domain = "https://tripspottest.jotangi.net/ddotpay/ecpayindex.php?orderid="
                var intent = Intent(context, PointShopPayActivity::class.java)
                intent.putExtra("url",domain+model.order_no)
                context.startActivity(intent)
            }
            detail.setOnClickListener {
                var intent = Intent(context,PointShopOrderRecordDetailActivity::class.java)
                intent.putExtra("order_no",model.order_no)
                context.startActivity(intent)
            }
        }
    }
}