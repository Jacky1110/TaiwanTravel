package com.jotangi.nickyen.industry.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.api.ApiConnection.OnConnectResultListener
import com.jotangi.nickyen.beautySalons.calendar.Reservation
import com.jotangi.nickyen.beautySalons.model.IsBookingBean
import com.jotangi.nickyen.beautySalons.model.WorkingDayBean
import com.jotangi.nickyen.utils.getCurrentTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/8
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
class IndustryCalendarViewModel : ViewModel() {

    val listWorkingDayBean = MutableLiveData<ArrayList<WorkingDayBean>>()
    val listReservation = MutableLiveData<ArrayList<Reservation>>()

    fun getWorkingDay(pid: String, onFailure: ((message: String?) -> Unit)? = null) {
        ApiConnection.getClassWorkingDay(pid, object : OnConnectResultListener {
            override fun onSuccess(jsonString: String?) {
                val type = object : TypeToken<ArrayList<WorkingDayBean?>?>() {}.type
                Gson().fromJson<ArrayList<WorkingDayBean>>(jsonString, type)?.let {
                    Timber.e("onSuccess -> $it")
                    GlobalScope.launch(Dispatchers.Main) {
                        listWorkingDayBean.value = it
                    }
                } ?: onFailure?.invoke(null)
            }

            override fun onFailure(message: String?) {
                Timber.e("onFailure -> $message")
                onFailure?.invoke(message)
            }
        })
    }

    fun getBookingDay(
        pid: String, workingDate: String, onFailure: ((message: String?) -> Unit)? = null
    ) {
//        ApiConnection.isClassBookingDay(pid, workingDate, object : OnConnectResultListener {
//            override fun onSuccess(jsonString: String?) {
                val isToday = workingDate == LocalDate.now().toString()
//                try {
//                    val type = object : TypeToken<ArrayList<IsBookingBean?>?>() {}.type
//                    Gson().fromJson<ArrayList<IsBookingBean>>(jsonString, type)?.let {
//                        Timber.e("pid($pid), workingDate($workingDate), onSuccess -> $it")
//                        setReservationData(workingDate, isToday, it)
//                    } ?: kotlin.run {
//                        setReservationData(workingDate, isToday)
//                    }
//                } catch (e: Exception) {
//                    setReservationData(workingDate, isToday)
//                }
//            }
//
//            override fun onFailure(message: String?) {
//                Timber.e("onFailure -> $message")
//                setReservationData(workingDate, isFailure = true)
//                onFailure?.invoke(message)
//            }
//        })message
        setReservationData(workingDate, isToday)
    }

    private fun setReservationData(workingDate: String, isToday: Boolean? = null, list: java.util.ArrayList<IsBookingBean>? = null, isFailure: Boolean = false) {
        GlobalScope.launch(Dispatchers.Main) {
            listReservation.value = if (isFailure) {
                null
            } else {
                val reservationList = arrayListOf<Reservation>()
                val currentTime = getCurrentTime()

                reserveTime(workingDate).forEach { time ->
                    var isBooked = list?.find {
                        it.reserveTime == time
                    }?.let { true } ?: false

                    if (!isBooked && isToday == true && time <= currentTime) {
                        isBooked = true
                    }

                    reservationList.add(Reservation(time, isBooked))
                }
                reservationList
            }
        }
    }

    private fun reserveTime(workingDate: String): MutableList<String> {
        val reserveTime = mutableListOf<String>()
        listWorkingDayBean.value?.find {
            it.workingdate == workingDate
        }?.timeperiod?.split("-")?.let { timePeriodList ->
            var startTime = timePeriodList.getOrNull(0)?.toIntOrNull()
            var endTime = timePeriodList.getOrNull(1)?.toIntOrNull()

            if (startTime != null && endTime != null &&
                startTime >= 0 && startTime <= 23 &&
                endTime >= 0 && endTime <= 23
            ) {
                while (startTime <= endTime) {
                    reserveTime.add(timeToReserveTimeString(startTime))
                    startTime++
                }
            }
        }
        return reserveTime
    }

    private fun timeToReserveTimeString(time: Int): String {
        return if (time > 9) "$time:00" else "0$time:00"
    }
}