package com.jotangi.nickyen.merch.reconciliation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.merch.reconciliation.model.Reconciliation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

/**
 * Created by N!ck Yen on Date: 2021/12/29
 */
class ReconciliationViewModel : ViewModel() {

    var liveReconciliationList = MutableLiveData<ArrayList<Reconciliation>>()

    fun getProfitInfo(month: String, status: String) {
        ApiConnection.getProfitInfo(month, object : ApiConnection.OnConnectResultListener {
            override fun onSuccess(jsonString: String?) {
                val type = object : TypeToken<ArrayList<Reconciliation?>?>() {}.type

                Gson().fromJson<ArrayList<Reconciliation>>(jsonString, type)?.let {
                    Timber.e("onSuccess -> $it")
                    it.reverse()

                    GlobalScope.launch(Dispatchers.Main) {
                        when (status) {
                            "1" -> {
                                liveReconciliationList.value = it
                            }
                            "2" -> {
                                liveReconciliationList.value = calculateList(it, status)
                            }
                            "3" -> {
                                liveReconciliationList.value = calculateList(it, status)
                            }
                        }
                    }
                }
            }

            override fun onFailure(message: String?) {
                Timber.d("onFailure -> $message")
                GlobalScope.launch(Dispatchers.Main) {
                    liveReconciliationList.value = null
                }
            }
        })
    }

    private fun calculateList(
        arrayList: ArrayList<Reconciliation>,
        status: String
    ): ArrayList<Reconciliation>? {
        var newArray = arrayListOf<Reconciliation>()
        return when (status) {
            "2" -> {
                arrayList.forEach {
                    if (it.billing_flag.equals("0")) {
                        newArray.add(it)
                    }
                }
                return newArray
            }
            "3" -> {
                arrayList.forEach {
                    if (it.billing_flag.equals("1")) {
                        newArray.add(it)
                    }
                }
                return newArray
            }
            else -> null
        }

    }
}