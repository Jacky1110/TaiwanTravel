package com.jotangi.nickyen.beautySalons.calendar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *Created by Luke Liu on 2021/9/13.
 */

@Parcelize
data class Reservation (
    var reserveTime: String,
    var isBooked: Boolean = false
): Parcelable