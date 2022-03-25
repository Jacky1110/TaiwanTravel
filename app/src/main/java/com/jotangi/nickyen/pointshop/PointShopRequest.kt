package com.jotangi.nickyen.pointshop

import android.os.Parcelable
import com.jotangi.nickyen.AppUtility
import com.jotangi.nickyen.model.UserBean
import kotlinx.android.parcel.Parcelize


@Parcelize
open class BaseRequest (
    var member_id:String? = AppUtility.DecryptAES2(UserBean.member_id),
    var member_pwd:String? = AppUtility.DecryptAES2(UserBean.member_pwd),
):Parcelable

@Parcelize
open class EcorderInfoRequest(
    var order_no:String? = ""
):Parcelable,BaseRequest()

@Parcelize
open class EcorderListRequest (
    var order_startdate:String? = "",
    var order_enddate:String? = ""
):Parcelable,BaseRequest()

@Parcelize
open class OrderRequest (
    var order_amount:Int? = 0,
    var coupon_no:String? = "",
    var discount_amount:Int? = 0,
    var order_pay:Int? = 0,
    var bonus_point:Int? = 0,
    var delivery_type:Int? = 1,
    var recipient_name:String? = "qq",
    var recipient_addr:String? = "qq",
    var recipient_phone:String? = "012",
    var recipient_mail:String? = "qq",
    var invoice_type:Int? = 1,
    var invoice_phone:String? = "012",
    var company_title:String? = "qq",
    var uniform_no:String?= "qq"
):Parcelable,BaseRequest()

