package com.jotangi.nickyen.constellation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
/\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/11/29
 * {cname: "魔羯座",
 * startday: "12.22",
 * endday: "01.20",
 * description: "雖然魔羯座不歡探聽八卦，但適時打探周邊的人與狀況，也是必要的，朋友間要常連絡，維繫感情。工作與課業有不錯表現，任何想法請不要留在紙上談兵，要抓緊機會不要遲疑，做錯比錯過好；多多投資自己，也要注意理財，和家人家也要多點耐心。",
 * picture: "uploads/star012.jpg",
 * coupon_no: "61a43933bf261"}
}
\/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
@Parcelize
data class ConstellationModel(
    val cname: String,
    val coupon_no: String,
    val description: String,
    val endday: String,
    val picture: String,
    val startday: String
): Parcelable