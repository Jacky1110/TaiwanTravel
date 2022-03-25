package com.jotangi.nickyen.beautySalons.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nickyen.R

class CheckCalendarAdapter(private val mData: List<Reservation>) :
    RecyclerView.Adapter<CheckCalendarAdapter.ViewHolder>() {

    var onItemClick: (Reservation) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckCalendarAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_check_calendar, parent, false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvTime: TextView = v.findViewById(R.id.tvTime)
        var tvReservation: TextView = v.findViewById(R.id.tvReservation)
    }

    override fun onBindViewHolder(holder: CheckCalendarAdapter.ViewHolder, position: Int) {

        val data = mData[position]

        holder.apply {
            tvTime.text = data.reserveTime

            if (data.isBooked){
                tvReservation.setText(R.string.reservation_unavailable)
                tvReservation.setBackgroundResource(R.drawable.btn_unreservation_bg)
            }else{
                tvReservation.setText(R.string.reservation_available)
                tvReservation.setBackgroundResource(R.drawable.btn_reservation_bg)
                tvReservation.setOnClickListener {
                    onItemClick.invoke(data)
                }
            }
        }
    }
}