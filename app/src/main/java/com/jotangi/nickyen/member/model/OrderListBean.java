package com.jotangi.nickyen.member.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/7/14
 * == 這是多支Api共用的model有些參數部分api不會有==
 * oid: "42",
 * order_no: "16265264834005",
 * order_date: "2021-07-17 20:54:43",
 * store_id: "40",
 * member_id: "58",
 * order_amount: "123",
 * coupon_no: "",
 * discount_amount: "0",
 * pay_type: "1",
 * order_pay: "123",
 * pay_status: "1",
 * bonus_point: "0",
 * order_status: "1"
 * m_id: "0956377567",
 * member_name: "陳竹婷"
 * bonus_get："22"
 * bonus_date: "2021-12-13 10:24:29",
 * bonus_end_date: "2022-05-27 10:24:29",
 * sid: "124",
 * store_name: "七彩雲南 (桃園店)"
 * }
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class OrderListBean implements Serializable
{
    @SerializedName("oid")
    private String oid;
    @SerializedName("order_no")
    private String orderNo;
    @SerializedName("order_date")
    private String orderDate;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("member_id")
    private String memberId;
    @SerializedName("order_amount")
    private String orderAmount;
    @SerializedName("coupon_no")
    private String couponNo;
    @SerializedName("discount_amount")
    private String discountAmount;
    @SerializedName("pay_type")
    private String payType;
    @SerializedName("order_pay")
    private String orderPay;
    @SerializedName("pay_status")
    private String payStatus;
    @SerializedName("bonus_point")
    private String bonusPoint;
    @SerializedName("order_status")
    private String orderStatus;
    @SerializedName("m_id")
    private String mid;
    @SerializedName("member_name")
    private String memberName;
    @SerializedName("bonus_get")
    private String bonusGet;
    @SerializedName("bonus_date")
    private String bonusDate;
    @SerializedName("bonus_end_date")
    private String bonusEndDate;
    @SerializedName("sid")
    private String sid;
    @SerializedName("store_name")
    private String apiStoreName;

    private String storeName; // 用來儲存雙重for循環的storeName

    public String getOid()
    {
        return oid;
    }

    public void setOid(String oid)
    {
        this.oid = oid;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(String orderDate)
    {
        this.orderDate = orderDate;
    }

    public String getStoreId()
    {
        return storeId;
    }

    public void setStoreId(String storeId)
    {
        this.storeId = storeId;
    }

    public String getMemberId()
    {
        return memberId;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public String getOrderAmount()
    {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount)
    {
        this.orderAmount = orderAmount;
    }

    public String getCouponNo()
    {
        return couponNo;
    }

    public void setCouponNo(String couponNo)
    {
        this.couponNo = couponNo;
    }

    public String getDiscountAmount()
    {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getOrderPay()
    {
        return orderPay;
    }

    public void setOrderPay(String orderPay)
    {
        this.orderPay = orderPay;
    }

    public String getPayStatus()
    {
        return payStatus;
    }

    public void setPayStatus(String payStatus)
    {
        this.payStatus = payStatus;
    }

    public String getBonusPoint()
    {
        return bonusPoint;
    }

    public void setBonusPoint(String bonusPoint)
    {
        this.bonusPoint = bonusPoint;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getStoreName()
    {
        return this.storeName;
    }

    public void setStoreName(final String storeName)
    {
        this.storeName = storeName;
    }

    public String getMid()
    {
        return this.mid;
    }

    public void setMid(final String mid)
    {
        this.mid = mid;
    }

    public String getMemberName()
    {
        return this.memberName;
    }

    public void setMemberName(final String memberName)
    {
        this.memberName = memberName;
    }

    public String getBonusGet()
    {
        return this.bonusGet;
    }

    public void setBonusGet(final String bonusGet)
    {
        this.bonusGet = bonusGet;
    }

    public String getBonusDate()
    {
        return this.bonusDate;
    }

    public void setBonusDate(final String bonusDate)
    {
        this.bonusDate = bonusDate;
    }

    public String getBonusEndDate() {
        return this.bonusEndDate;
    }

    public void setBonusEndDate(final String bonusEndDate) {
        this.bonusEndDate = bonusEndDate;
    }

    public String getSid()
    {
        return this.sid;
    }

    public void setSid(final String sid)
    {
        this.sid = sid;
    }

    public String getApiStoreName()
    {
        return this.apiStoreName;
    }

    public void setApiStoreName(final String apiStoreName)
    {
        this.apiStoreName = apiStoreName;
    }

    @Override
    public String toString() {
        return "OrderListBean{" +
                "oid='" + oid + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", storeId='" + storeId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", orderAmount='" + orderAmount + '\'' +
                ", couponNo='" + couponNo + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", payType='" + payType + '\'' +
                ", orderPay='" + orderPay + '\'' +
                ", payStatus='" + payStatus + '\'' +
                ", bonusPoint='" + bonusPoint + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", mid='" + mid + '\'' +
                ", memberName='" + memberName + '\'' +
                ", bonusGet='" + bonusGet + '\'' +
                ", bonusDate='" + bonusDate + '\'' +
                ", bonusEndDate='" + bonusEndDate + '\'' +
                ", sid='" + sid + '\'' +
                ", apiStoreName='" + apiStoreName + '\'' +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
