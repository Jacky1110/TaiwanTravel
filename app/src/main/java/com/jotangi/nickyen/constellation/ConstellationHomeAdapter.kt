package com.jotangi.nickyen.constellation

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jotangi.nickyen.R
import com.jotangi.nickyen.home.myquestionnaire.MyQuestionModel
import com.jotangi.nickyen.home.myquestionnaire.MyQuestionnaireModel

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/11/25
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
class ConstellationHomeAdapter(layoutResId: Int, data: MutableList<MyQuestionModel>?) :
    BaseQuickAdapter<MyQuestionModel, BaseViewHolder>(layoutResId, data) {

    override fun convert(holder: BaseViewHolder, item: MyQuestionModel) {

        val txtName: TextView = holder.getView(R.id.tv_name)
        val txtContent: TextView = holder.getView(R.id.tv_descript)
        val txtNotice: TextView = holder.getView(R.id.tv_price)
        val txtStatus: TextView = holder.getView(R.id.tv_status)
        val layoutStatus: ConstraintLayout = holder.getView(R.id.layout_status)

        txtName.text = "歲末年終運勢分析拿好康"
        txtContent.text = item.name
        txtNotice.text = item.name2
    }
}