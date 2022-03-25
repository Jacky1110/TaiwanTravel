package com.jotangi.nickyen.home.notify

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
class NotifyAdapter(val mContext: Context, private val mData: List<NotifyModel>) :
    RecyclerView.Adapter<NotifyAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var txtName: TextView = v.findViewById(R.id.tv_title)
        var txtContent: TextView = v.findViewById(R.id.tv_content)
        var txtDate: TextView = v.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_notify, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.apply {
            txtName.text = data.pushMessage
            txtContent.text = data.pushMessage
            txtDate.text = data.pushDate
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}