package com.jotangi.nickyen.questionnaire

import com.google.gson.annotations.SerializedName

/**
 *Created by Luke Liu on 2021/9/13.
 */

data class Questionnaire(
    @SerializedName("question")
    var question: String? = null,
    @SerializedName("qid")
    var qid: String? = null,
    @SerializedName("isRequired")
    var isRequired: Boolean = true,
    @SerializedName("chooseType")
    var chooseType: String? = null,
    @SerializedName("answers")
    var answers: List<QuestionnaireAnswer>? = null,
    @SerializedName("isSent")
    var isSent: Boolean = false
)


data class QuestionnaireAnswer(
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
    @SerializedName("answer")
    var answer: String? = null,
    @SerializedName("isOther")
    var isOther: Boolean = false,
    @SerializedName("other")
    var other: String? = null
)


data class QuestionnaireList(
    @SerializedName("question")
    var question: String? = null,
    @SerializedName("qid")
    var qid: String? = null,
    @SerializedName("choose_type")
    var choose_type: String? = null,
    @SerializedName("q01")
    var q01: String? = null,
    @SerializedName("q02")
    var q02: String? = null,
    @SerializedName("q03")
    var q03: String? = null,
    @SerializedName("q04")
    var q04: String? = null,
    @SerializedName("q05")
    var q05: String? = null,
    @SerializedName("q06")
    var q06: String? = null,
    @SerializedName("q07")
    var q07: String? = null,
    @SerializedName("q08")
    var q08: String? = null,
    @SerializedName("q09")
    var q09: String? = null,
    @SerializedName("q10")
    var q10: String? = null
)