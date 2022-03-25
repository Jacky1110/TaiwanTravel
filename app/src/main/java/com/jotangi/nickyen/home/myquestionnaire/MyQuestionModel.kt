package com.jotangi.nickyen.home.myquestionnaire

import com.google.gson.annotations.SerializedName

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/11/4
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
class MyQuestionModel (_name: String,_name2: String){
    var name = _name
    var name2 = _name2
}

data class MyQuestionnaireModel(
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("enddate")
    var enddate: String? = null,
    @SerializedName("questionnaire_title")
    var questionnaire_title: String? = null,
    @SerializedName("startdate")
    var startdate: String? = null,
    @SerializedName("store_id")
    var store_id: String? = null

) {
    override fun toString(): String {
        return "MyQuestionnaireModel(description=$description, enddate=$enddate, questionnaire_title=$questionnaire_title, startdate=$startdate, store_id=$store_id)"
    }
}