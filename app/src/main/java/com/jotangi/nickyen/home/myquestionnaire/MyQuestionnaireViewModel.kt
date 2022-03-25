package com.jotangi.nickyen.home.myquestionnaire

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jotangi.nickyen.AppUtility
import com.jotangi.nickyen.api.RetrofitRepository
import com.jotangi.nickyen.api.RetrofitService
import com.jotangi.nickyen.model.UserBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/28
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
class MyQuestionnaireViewModel : ViewModel() {

    private var dataList = MutableLiveData<List<MyQuestionnaireModel>>()

    fun getDataListObserve(): MutableLiveData<List<MyQuestionnaireModel>> {
        return dataList
    }

    fun getMyQuestionnaire() {
        val apiService = RetrofitRepository.getRetroInstance().create(RetrofitService::class.java)
        var call: Call<List<MyQuestionnaireModel>>? = null

        call = apiService.questionnaireTitle(
            AppUtility.DecryptAES2(UserBean.member_id),
            AppUtility.DecryptAES2(UserBean.member_pwd)
        )

        call.enqueue(object : Callback<List<MyQuestionnaireModel>> {
            override fun onResponse(
                call: Call<List<MyQuestionnaireModel>>,
                response: Response<List<MyQuestionnaireModel>>
            ) {
                if (response.isSuccessful) {
                    dataList.postValue(response.body())
                }else{
                    dataList.postValue(null)
                }
                Timber.e("onResponse -> $dataList")
            }

            override fun onFailure(call: Call<List<MyQuestionnaireModel>>, t: Throwable) {
                dataList.postValue(null)
                Timber.e("onFailure -> $t")
            }
        })
    }
}