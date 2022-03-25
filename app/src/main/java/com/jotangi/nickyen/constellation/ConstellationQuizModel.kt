package com.jotangi.nickyen.constellation

import com.google.gson.annotations.SerializedName
import com.jotangi.nickyen.questionnaire.QuestionnaireAnswer

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/11/26
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
data class ConstellationQuestionnaire(
    @SerializedName("question")
    var question: String? = null,
    @SerializedName("qid")
    var qid: String? = null,
    @SerializedName("isRequired")
    var isRequired: Boolean = true,
    @SerializedName("chooseType")
    var chooseType: String? = null,
    @SerializedName("answers")
    var answers: List<ConstellationQuestionnaireAnswer>? = null,
    @SerializedName("isSent")
    var isSent: Boolean = false
)

data class ConstellationQuestionnaireAnswer(
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
    @SerializedName("answer")
    var answer: String? = null,
    @SerializedName("isOther")
    var isOther: Boolean = false,
    @SerializedName("other")
    var other: String? = null
)

data class ConstellationQuestionnaireList(
    var question: String? = null,
//    var qid: String? = null,
    var question_type: String? = null, // 決定recycle裡面的不同item類型 0是一般問券 1是日期回填
    var choose_type: String? = null, // 0必填,1非必填的樣子
    var q01: String? = null,
    var q02: String? = null,
    var q03: String? = null,
    var q04: String? = null,
    var q05: String? = null,
    var q06: String? = null,
//    var q07: String? = null,
//    var q08: String? = null,
//    var q09: String? = null,
//    var q10: String? = null
)