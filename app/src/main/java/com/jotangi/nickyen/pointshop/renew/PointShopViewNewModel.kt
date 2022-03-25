package com.jotangi.nickyen.pointshop.renew

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.pointshop.EcorderInfoRequest
import com.jotangi.nickyen.pointshop.EcorderListRequest
import com.jotangi.nickyen.pointshop.OrderRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by N!ck Yen on Date: 2022/2/15
 */
class PointShopViewNewModel : ViewModel() {

    val tabItem = MutableLiveData<ArrayList<TabModel>>()
    val listItem = MutableLiveData<ArrayList<PointShopModel>>()
    val orderNo = MutableLiveData<String>()
    val ecorderListItem = MutableLiveData<ArrayList<PointShopEcorderModel>>()
    val ecorderInfoItem = MutableLiveData<ArrayList<PointShopEcorderInfo>>()

    fun getListItem(strType: String, onFailure: ((message: String?) -> Unit)? = null) {
        ApiConnection.getProductList(strType, object : ApiConnection.OnConnectResultListener {
            override fun onSuccess(jsonString: String) {
                val type = object : TypeToken<ArrayList<PointShopModel?>?>() {}.type
                Gson().fromJson<ArrayList<PointShopModel>>(jsonString, type)?.let {
                    Timber.e("onSuccess -> $it")
                    GlobalScope.launch(Dispatchers.Main) {
                        if (!it.isNullOrEmpty()) {
                            listItem.value = it
                        }
                    }
                } ?: onFailure?.invoke(null)
            }

            override fun onFailure(message: String) {
                Timber.e("onFailure -> $message")
                onFailure?.invoke(message)
                listItem.postValue(null)
            }
        })
    }

    fun getEcorderList(request: EcorderListRequest){
        ApiConnection.getEcorderList(request,object :ApiConnection.OnConnectResultListener{
            override fun onSuccess(jsonString: String?) {
                val type = object : TypeToken<ArrayList<PointShopEcorderModel?>?>() {}.type
                Gson().fromJson<ArrayList<PointShopEcorderModel>>(jsonString, type)?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        if (!it.isNullOrEmpty()) {
                            ecorderListItem.value = it
                        }
                    }
                }
            }

            override fun onFailure(message: String?) {
                ecorderListItem.postValue(arrayListOf())
            }
        })
    }

    fun getEcorderInfo(request: EcorderInfoRequest){
        ApiConnection.getEcorderInfo(request,object :ApiConnection.OnConnectResultListener{
            override fun onSuccess(jsonString: String?) {
                val type = object : TypeToken<ArrayList<PointShopEcorderInfo?>?>() {}.type
                Log.d("eee",jsonString!!)
                try {
                    Gson().fromJson<ArrayList<PointShopEcorderInfo>>(jsonString, type)?.let {
                        GlobalScope.launch(Dispatchers.Main) {
                            if (!it.isNullOrEmpty()) {
                                ecorderInfoItem.value = it
                            }
                        }
                    }
                }catch (e:Exception){

                }
            }

            override fun onFailure(message: String?) {
                ecorderInfoItem.postValue(arrayListOf())
            }
        })
    }

    fun addEcorder(request:OrderRequest){
        ApiConnection.addEcorder(request,object :ApiConnection.OnConnectResultListener{
            override fun onSuccess(jsonString: String?) {
                orderNo.postValue(jsonString)
            }

            override fun onFailure(message: String?) {
                orderNo.postValue(message)
            }

        })
    }
}