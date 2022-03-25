package com.jotangi.nickyen.pointshop.renew.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConstant
import com.jotangi.nickyen.pointshop.renew.PointShopEcorderData
import com.squareup.picasso.Picasso

class PointShopOrderRecordDetailAdapter(mContext:Context,private val mData:List<PointShopEcorderData>)
    :RecyclerView.Adapter<PointShopOrderRecordDetailAdapter.ViewHolder>(){
    var context = mContext

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PointShopOrderRecordDetailAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_point_shop_ecorder_good,parent,false))
    }

    override fun onBindViewHolder(
        holder: PointShopOrderRecordDetailAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(v: View) :RecyclerView.ViewHolder(v){
        val goodName:TextView = v.findViewById(R.id.good_name)
        val goodPrice:TextView = v.findViewById(R.id.good_price)
        val goodPrice2:TextView = v.findViewById(R.id.good_price2)
        val goodNum:TextView = v.findViewById(R.id.good_num)
        val goodPic:ImageView = v.findViewById(R.id.good_pic)

        fun bind(model:PointShopEcorderData){
            goodName.text = model.product_name
            goodNum.text = model.order_qty
            goodPrice.text = model.product_price
            goodPrice2.text = model.total_amount
            Picasso.with(context).load(ApiConstant.API_MALL_IMAGE + model.product_picture).into(goodPic)
        }
    }


}