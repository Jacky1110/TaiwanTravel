package com.jotangi.nickyen.industry.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nickyen.R
import com.jotangi.nickyen.beautySalons.calendar.Reservation

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/8
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
class IndustryCalendarAdapter(private val mData: List<Reservation>) :
    RecyclerView.Adapter<IndustryCalendarAdapter.ViewHolder>() {

    var onItemClick: (Reservation) -> Unit = {}

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvTime: TextView = v.findViewById(R.id.tvTime)
        var tvReservation: TextView = v.findViewById(R.id.tvReservation)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndustryCalendarAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_check_calendar, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IndustryCalendarAdapter.ViewHolder, position: Int) {
        var data = mData[position]

        holder.apply {
            tvTime.text = data.reserveTime
            if (data.isBooked) {
                tvReservation.setText(R.string.reservation_unavailable)
                tvReservation.setBackgroundResource(R.drawable.btn_unreservation_bg)

            } else {
                tvReservation.setText(R.string.reservation_available)
                tvReservation.setBackgroundResource(R.drawable.btn_reservation_bg)
                tvReservation.setOnClickListener {
                    onItemClick.invoke(data)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}