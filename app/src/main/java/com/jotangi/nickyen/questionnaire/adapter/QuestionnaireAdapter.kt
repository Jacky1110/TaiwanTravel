package com.jotangi.nickyen.questionnaire.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nickyen.R
import com.jotangi.nickyen.questionnaire.Questionnaire
import com.jotangi.nickyen.questionnaire.QuestionnaireAnswer
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeVisible

class QuestionnaireAdapter(val mContext: Context,  private val mData: List<Questionnaire>) :
    RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder>() {

    var onOtherClick: (questionnaire:Questionnaire, questionnaireAnswer: QuestionnaireAnswer) -> Unit = {questionnaire, answer -> }
    var onAnswerClick: (questionnaire:Questionnaire, questionnaireAnswer: QuestionnaireAnswer) -> Unit = {questionnaire, answer -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_questionnaire, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvTitle: TextView = v.findViewById(R.id.tvTitle1)
        var tvRequired: TextView = v.findViewById(R.id.tvRequired1)
        var tvNote: TextView = v.findViewById(R.id.tvNote)
        var rvQuestionnaireAnswer: RecyclerView = v.findViewById(R.id.rvQuestionnaireAnswer)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = mData[position]

        holder.apply {
            tvTitle.text = data.question

            if (data.isRequired){
                tvRequired.makeVisible()
            }else{
                tvRequired.makeGone()
            }

            data.answers?.let {
                val questionnaireAnswerAdapter = QuestionnaireAnswerAdapter(it)
                questionnaireAnswerAdapter.onAnswerClick = { answer ->
                    onAnswerClick.invoke(data, answer)
                }
                questionnaireAnswerAdapter.onOtherClick = { answer ->
                    onOtherClick.invoke(data, answer)
                }

                rvQuestionnaireAnswer.layoutManager = LinearLayoutManager(mContext)
                rvQuestionnaireAnswer.adapter = questionnaireAnswerAdapter
            }
            // the last line text
            if (position == mData.size -1){
                tvNote.makeVisible()
            }else{
                tvNote.makeGone()
            }
        }
    }
}