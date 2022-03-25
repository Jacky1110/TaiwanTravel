package com.jotangi.nickyen.pointshop.renew

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by N!ck Yen on Date: 2022/2/14
 */
/*
    rid: "15",
    product_no: "Y12345674",
    product_name: "安適康 疤痕護理矽膠筆 添加防曬係數 (4g/條)",
    pid: "2",
    product_description: "安適康 疤痕護理矽膠筆 添加防曬係數 (4g/條)",
    product_price: "1360",
    product_bonus: "0",
    product_stock: "1",
    product_picture: "uploads/Y12345674.jpg",
    product_picture1: null,
    product_picture2: null,
    product_picture3: null,
    product_picture4: null,
    product_status: "1",
    product_created_at: "2022-01-25 18:54:08",
    product_updated_at: null,
    product_created_by: null,
    product_updated_by: null,
    product_trash: "0"
    product_type: "A2",
    producttype_name: "美容保養",
 */
@Parcelize
data class PointShopModel(
    var pid: String?,
    var product_bonus: String?,
    var product_created_at: String?,
    var product_created_by: String?,
    var product_description: String?,
    var product_name: String?,
    var product_no: String?,
    var product_picture: String?,
    var product_picture1: String?,
    var product_picture2: String?,
    var product_picture3: String?,
    var product_picture4: String?,
    var product_price: String?,
    var product_status: String?,
    var product_stock: String?,
    var product_trash: String?,
    var product_type: String?,
    var product_updated_at: String?,
    var product_updated_by: String?,
    var producttype_name: String?,
    var rid: String?
) : Parcelable

/*
    pid: "2",
    product_type: "A2",
    producttype_name: "美容保養",
    producttype_picture: "uploads/producttype2.jpg"
*/
@Parcelize
data class TabModel(
    var pid: String?,
    var product_type: String?,
    var producttype_name: String?,
    var producttype_picture: String?
) : Parcelable

/*
    did: "8",
    member_id: "537",
    product_no: "Y12345677",
    product_spec: "",
    product_price: "490",
    order_qty: "13",
    total_amount: "6370",
    cart_created_at: "2022-02-16 11:42:56",
    cart_updated_at: "2022-02-16 13:58:09",
    product_name: "3M Cavilon長效保膚霜 3392G (92g/瓶)",
    producttype_name: "美容保養",
    product_picture: "uploads/Y12345677.jpg"
 */
@Parcelize
data class ShoppingCartModel(
    var cart_created_at: String?,
    var cart_updated_at: String?,
    var did: String?,
    var member_id: String?,
    var order_qty: String?,
    var product_name: String?,
    var product_no: String?,
    var product_picture: String?,
    var product_price: String?,
    var product_spec: String?,
    var producttype_name: String?,
    var total_amount: String?
) : Parcelable

@Parcelize
data class PointShopEcorderModel(
    var oid: String? = "",
    var order_no: String? = "",
    var order_date: String? = "",
    var store_id: String? = "",
    var member_id: String? = "",
    var order_amount: String? = "",
    var coupon_no: String? = "",
    var discount_amount: String? = "",
    var pay_type: String? = "",
    var order_pay: String? = "",
    var pay_status: String? = "",
    var bonus_point: String? = "",
    var deliverytype: String? = "",
    var recipientname: String? = "",
    var recipientaddr: String? = "",
    var recipientphone: String? = "",
    var recipientmail: String? = "",
    var deliverystatus: String? = "",
    var invoicetype: String? = "",
    var invoicephone: String? = "",
    var companytitle: String? = "",
    var uniformno: String? = "",
    var invoicestatus: String? = "",
    var order_status: String? = ""
):Parcelable

@Parcelize
data class PointShopEcorderData(
    var did:String? = "",
    var order_no: String? = "",
    var member_id: String? = "",
    var product_no: String? = "",
    var product_spec: String? = "",
    var product_price: String? = "",
    var order_qty: String? = "",
    var total_amount: String? = "",
    var deliverystatus: String? = "",
    var order_status: String? = "",
    var cart_created_at: String? = "",
    var product_name: String? = "",
    var producttype_name: String? = "",
    var product_picture: String? = ""
):Parcelable

@Parcelize
data class PointShopEcorderInfo(
    var oid:String? = "",
    var order_no:String? = "",
    var order_date:String? = "",
    var store_id:String? = "",
    var member_id:String? = "",
    var order_amount:String? = "",
    var coupon_no:String? = "",
    var discount_amount:String? = "",
    var pay_type:String? = "",
    var order_pay:String? = "",
    var pay_status:String? = "",
    var bonus_point:String? = "",
    var deliverytype:String? = "",
    var recipientname:String? = "",
    var recipientaddr:String? = "",
    var recipientphone:String? = "",
    var recipientmail:String? = "",
    var deliverystatus:String? = "",
    var invoicetype:String? = "",
    var invoicephone:String? = "",
    var companytitle:String? = "",
    var invoicestatus:String? = "",
    var order_status:String? = "",
    var product_list:List<PointShopEcorderData>? = arrayListOf()
):Parcelable

