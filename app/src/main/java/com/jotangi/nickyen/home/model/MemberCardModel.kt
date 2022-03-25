package com.jotangi.nickyen.home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/9/15
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
@Parcelize
data class MemberCard(
    val card_type: String? = null,
    val member_date: String? = null,
    val member_id: String? = null,
    val sid: String? = null,
    val store_id: String? = null,
    val store_name: String? = null,
    val store_picture: String? = null
) : Parcelable