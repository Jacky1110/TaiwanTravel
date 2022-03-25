package com.jotangi.nickyen.merch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/7/13
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class MerchOrderBean implements Serializable
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
    private String mId;
    @SerializedName("member_name")
    private String memberName;

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

    public String getMId()
    {
        return mId;
    }

    public void setMId(String mId)
    {
        this.mId = mId;
    }

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    @Override
    public String toString()
    {
        return "MerchOrderBean{" +
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
                ", mId='" + mId + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
