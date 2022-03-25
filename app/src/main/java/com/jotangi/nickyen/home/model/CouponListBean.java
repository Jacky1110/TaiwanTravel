package com.jotangi.nickyen.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/17
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class CouponListBean implements Serializable
{

    @SerializedName("pid")
    private String pid;
    @SerializedName("mid")
    private String mid;
    @SerializedName("coupon_no")
    private String couponNo;
    @SerializedName("using_flag")
    private String usingFlag;
    @SerializedName("using_date")
    private Object usingDate;
    @SerializedName("coupon_id")
    private String couponId;
    @SerializedName("coupon_name")
    private String couponName;
    @SerializedName("coupon_type")
    private String couponType;
    @SerializedName("coupon_description")
    private String couponDescription;
    @SerializedName("coupon_startdate")
    private String couponStartdate;
    @SerializedName("coupon_enddate")
    private String couponEnddate;
    @SerializedName("coupon_status")
    private String couponStatus;
    @SerializedName("coupon_rule")
    private String couponRule;
    @SerializedName("coupon_discount")
    private String couponDiscount;
    @SerializedName("discount_amount")
    private String discountAmount;
    @SerializedName("coupon_storeid")
    private String couponStoreid;
    @SerializedName("coupon_for")
    private String couponFor;
    @SerializedName("coupon_picture")
    private String couponPicture;

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getMid()
    {
        return mid;
    }

    public void setMid(String mid)
    {
        this.mid = mid;
    }

    public String getCouponNo()
    {
        return couponNo;
    }

    public void setCouponNo(String couponNo)
    {
        this.couponNo = couponNo;
    }

    public String getUsingFlag()
    {
        return usingFlag;
    }

    public void setUsingFlag(String usingFlag)
    {
        this.usingFlag = usingFlag;
    }

    public Object getUsingDate()
    {
        return usingDate;
    }

    public void setUsingDate(Object usingDate)
    {
        this.usingDate = usingDate;
    }

    public String getCouponId()
    {
        return couponId;
    }

    public void setCouponId(String couponId)
    {
        this.couponId = couponId;
    }

    public String getCouponName()
    {
        return couponName;
    }

    public void setCouponName(String couponName)
    {
        this.couponName = couponName;
    }

    public String getCouponType()
    {
        return couponType;
    }

    public void setCouponType(String couponType)
    {
        this.couponType = couponType;
    }

    public String getCouponDescription()
    {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription)
    {
        this.couponDescription = couponDescription;
    }

    public String getCouponStartdate()
    {
        return couponStartdate;
    }

    public void setCouponStartdate(String couponStartdate)
    {
        this.couponStartdate = couponStartdate;
    }

    public String getCouponEnddate()
    {
        return couponEnddate;
    }

    public void setCouponEnddate(String couponEnddate)
    {
        this.couponEnddate = couponEnddate;
    }

    public String getCouponStatus()
    {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus)
    {
        this.couponStatus = couponStatus;
    }

    public String getCouponRule()
    {
        return couponRule;
    }

    public void setCouponRule(String couponRule)
    {
        this.couponRule = couponRule;
    }

    public String getCouponDiscount()
    {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount)
    {
        this.couponDiscount = couponDiscount;
    }

    public String getDiscountAmount()
    {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public String getCouponStoreid()
    {
        return couponStoreid;
    }

    public void setCouponStoreid(String couponStoreid)
    {
        this.couponStoreid = couponStoreid;
    }

    public String getCouponFor()
    {
        return couponFor;
    }

    public void setCouponFor(String couponFor)
    {
        this.couponFor = couponFor;
    }

    public String getCouponPicture()
    {
        return couponPicture;
    }

    public void setCouponPicture(String couponPicture)
    {
        this.couponPicture = couponPicture;
    }

    @Override
    public String toString()
    {
        return "CouponListBean{" +
                "pid='" + pid + '\'' +
                ", mid='" + mid + '\'' +
                ", couponNo='" + couponNo + '\'' +
                ", usingFlag='" + usingFlag + '\'' +
                ", usingDate=" + usingDate +
                ", couponId='" + couponId + '\'' +
                ", couponName='" + couponName + '\'' +
                ", couponType='" + couponType + '\'' +
                ", couponDescription='" + couponDescription + '\'' +
                ", couponStartdate='" + couponStartdate + '\'' +
                ", couponEnddate='" + couponEnddate + '\'' +
                ", couponStatus='" + couponStatus + '\'' +
                ", couponRule='" + couponRule + '\'' +
                ", couponDiscount='" + couponDiscount + '\'' +
                ", discountAmount='" + discountAmount + '\'' +
                ", couponStoreid='" + couponStoreid + '\'' +
                ", couponFor='" + couponFor + '\'' +
                ", couponPicture='" + couponPicture + '\'' +
                '}';
    }
}
