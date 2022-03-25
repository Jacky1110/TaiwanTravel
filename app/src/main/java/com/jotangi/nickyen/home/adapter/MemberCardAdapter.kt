package com.jotangi.nickyen.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConstant
import com.jotangi.nickyen.model.MemberBean
import com.squareup.picasso.Picasso

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/9/19
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
class MemberCardAdapter(val mContext: Context, private val mData: List<MemberBean>) :
    RecyclerView.Adapter<MemberCardAdapter.ViewHolder>() {

    var onItemClick : (MemberBean)-> Unit = {}

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var txtStoreName: TextView = v.findViewById(R.id.tv_store_name)
        var imgMember: ImageView = v.findViewById(R.id.img_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_member_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            txtStoreName.text = data.storeName
            Picasso.with(mContext).load(ApiConstant.API_IMAGE+data.storePicture).into(imgMember)
        }
        holder.itemView.setOnClickListener {
            onItemClick.invoke(data)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}