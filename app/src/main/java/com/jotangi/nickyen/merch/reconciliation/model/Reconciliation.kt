package com.jotangi.nickyen.merch.reconciliation.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by N!ck Yen on Date: 2021/12/29
 * {
 * pid: "1",
 * store_id: "121",
 * profit_month: "2021-11",
 * start_date: "2021-11-01",
 * end_date: "2021-11-30",
 * total_amount: "13208",
 * total_order: "20",
 * profit_pdf: "uploads/profit.pdf",
 * billing_date: "2021-12-05",
 * billing_flag: "0",
 * pay_date: null
 * total_amountD: "35980",
 * total_amountG: "1794",
 * total_amountI: "1794",
 * total_amountJ: "3588",
 * }
 */
@Parcelize
data class Reconciliation(
//    @SerializedName("billing_date")
    var billing_date: String? = null,
//    @SerializedName("billing_flag")
    var billing_flag: String? = null,
//    @SerializedName("end_date")
    var end_date: String? = null,
//    @SerializedName("pay_date")
    var pay_date: String? = null,
//    @SerializedName("pid")
    var pid: String? = null,
//    @SerializedName("profit_month")
    var profit_month: String? = null,
//    @SerializedName("profit_pdf")
    var profit_pdf: String? = null,
//    @SerializedName("start_date")
    var start_date: String? = null,
//    @SerializedName("store_id")
    var store_id: String? = null,
//    @SerializedName("pid")
    var total_amount: String? = null,
//    @SerializedName("total_order")
    var total_order: String? = null,

    var total_amountD: String? = null,
    val total_amountG: String? = null,
    var total_amountI: String? = null,
    var total_amountJ: String? = null,

    ) : Parcelable