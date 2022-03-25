package com.jotangi.nickyen.questionnaire.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nickyen.R
import com.jotangi.nickyen.questionnaire.QuestionnaireAnswer
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeVisible
import com.jotangi.nickyen.utils.setTextColorRes

class QuestionnaireAnswerAdapter(private val mData: List<QuestionnaireAnswer>) :
    RecyclerView.Adapter<QuestionnaireAnswerAdapter.ViewHolder>() {

    var onOtherClick: (questionnaireAnswer: QuestionnaireAnswer) -> Unit = {}
    var onAnswerClick: (questionnaireAnswer: QuestionnaireAnswer) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_questionnaire_answer, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvAnswer: TextView = v.findViewById(R.id.tvAnswer)
        var tvOther: TextView = v.findViewById(R.id.tvOther)
        var ivCheckBox: ImageView = v.findViewById(R.id.ivCheckBox)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = mData[position]

        holder.apply {
            if (data.answer.isNullOrEmpty()){
                itemView.makeGone()
            }else {
                itemView.makeVisible()
                tvAnswer.text = data.answer
                tvAnswer.setOnClickListener {
                    onAnswerClick.invoke(data)
                }

                tvOther.text = data.other
                tvOther.setOnClickListener {
                    onOtherClick.invoke(data)
                }

                if (data.isOther) {
                    if (data.other.isNullOrEmpty()) {
                        tvOther.text =
                            itemView.context.getString(R.string.questionnaire_your_answer)
                        tvOther.setTextColorRes(R.color.c_cecece_100)
                    } else {
                        tvOther.text = data.other
                        tvOther.setTextColorRes(R.color.black)
                    }
                    tvOther.makeVisible()
                } else {
                    tvOther.makeGone()
                }

                ivCheckBox.setImageResource(if (data.isSelected) R.drawable.ic_baseline_check_24 else android.R.color.transparent)
            }
        }
    }
}