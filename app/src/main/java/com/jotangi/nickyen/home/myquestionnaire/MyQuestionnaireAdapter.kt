package com.jotangi.nickyen.home.myquestionnaire

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConstant
import com.jotangi.nickyen.beautySalons.calendar.Reservation
import com.jotangi.nickyen.home.notify.NotifyModel
import com.jotangi.nickyen.model.NewsBean
import com.squareup.picasso.Picasso

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/28
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
class MyQuestionnaireAdapter(layoutResId: Int, data: MutableList<MyQuestionnaireModel>?) :
    BaseQuickAdapter<MyQuestionnaireModel, BaseViewHolder>(layoutResId, data) {

    override fun convert(holder: BaseViewHolder, item: MyQuestionnaireModel) {

        val txtName: TextView = holder.getView(R.id.tv_name)
        val txtContent: TextView = holder.getView(R.id.tv_descript)
        val txtNotice: TextView = holder.getView(R.id.tv_price)
        val txtStatus: TextView = holder.getView(R.id.tv_status)
        val layoutStatus: ConstraintLayout = holder.getView(R.id.layout_status)

        txtName.text = item.questionnaire_title
        txtContent.text = "問券期限｜ ${item.startdate}~${item.enddate}"
        txtNotice.text = item.description
    }
}
