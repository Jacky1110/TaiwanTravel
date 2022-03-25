package com.jotangi.nickyen.beautySalons.model;

import com.google.gson.annotations.SerializedName;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/8/24
 * rid: "9",
 * booking_no: "16295558174205",
 * store_id: "81",
 * hid: "1",
 * reserve_date: "2021-08-23",
 * reserve_time: "0800",
 * mid: "58",
 * member_id: "0918565779",
 * member_name: "Peter Guo",
 * service_item: "1",
 * reserve_status: "0",
 * reserve_created_at: "2021-08-21 22:23:37"
 * nick_name: "Belle"  //store book list 新增
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class OrderBean
{
    @SerializedName("rid")
    private String rid;
    @SerializedName("booking_no")
    private String bookingNo;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("hid")
    private String hid;
    @SerializedName("reserve_date")
    private String reserveDate;
    @SerializedName("reserve_time")
    private String reserveTime;
    @SerializedName("mid")
    private String mid;
    @SerializedName("member_id")
    private String memberId;
    @SerializedName("member_name")
    private String memberName;
    @SerializedName("service_item")
    private String serviceItem;
    @SerializedName("reserve_status")
    private String reserveStatus;
    @SerializedName("reserve_created_at")
    private String reserveCreatedAt;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("store_picture")
    private String storePicture;
    @SerializedName("nick_name")
    private String nickName;
    @SerializedName("service_name")
    private String serviceName;
    @SerializedName("service_price")
    private String servicePrice;
    @SerializedName("member_email")
    private String memberEmail;
    @SerializedName("reserve_remark")
    private String reserveRemark;

    public String getRid()
    {
        return rid;
    }

    public void setRid(String rid)
    {
        this.rid = rid;
    }

    public String getBookingNo()
    {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo)
    {
        this.bookingNo = bookingNo;
    }

    public String getStoreId()
    {
        return storeId;
    }

    public void setStoreId(String storeId)
    {
        this.storeId = storeId;
    }

    public String getHid()
    {
        return hid;
    }

    public void setHid(String hid)
    {
        this.hid = hid;
    }

    public String getReserveDate()
    {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate)
    {
        this.reserveDate = reserveDate;
    }

    public String getReserveTime()
    {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime)
    {
        this.reserveTime = reserveTime;
    }

    public String getMid()
    {
        return mid;
    }

    public void setMid(String mid)
    {
        this.mid = mid;
    }

    public String getMemberId()
    {
        return memberId;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public String getServiceItem()
    {
        return serviceItem;
    }

    public void setServiceItem(String serviceItem)
    {
        this.serviceItem = serviceItem;
    }

    public String getReserveStatus()
    {
        return reserveStatus;
    }

    public void setReserveStatus(String reserveStatus)
    {
        this.reserveStatus = reserveStatus;
    }

    public String getReserveCreatedAt()
    {
        return reserveCreatedAt;
    }

    public void setReserveCreatedAt(String reserveCreatedAt)
    {
        this.reserveCreatedAt = reserveCreatedAt;
    }

    public String getStoreName()
    {
        return storeName;
    }

    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }

    public String getStorePicture()
    {
        return storePicture;
    }

    public void setStorePicture(String storePicture)
    {
        this.storePicture = storePicture;
    }

    public String getNickName()
    {
        return this.nickName;
    }

    public void setNickName(final String nickName)
    {
        this.nickName = nickName;
    }

    public String getServiceName()
    {
        return this.serviceName;
    }

    public void setServiceName(final String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getServicePrice()
    {
        return this.servicePrice;
    }

    public void setServicePrice(final String servicePrice)
    {
        this.servicePrice = servicePrice;
    }

    public String getMemberEmail()
    {
        return this.memberEmail;
    }

    public void setMemberEmail(final String memberEmail)
    {
        this.memberEmail = memberEmail;
    }

    public String getReserveRemark()
    {
        return this.reserveRemark;
    }

    public void setReserveRemark(final String reserveRemark)
    {
        this.reserveRemark = reserveRemark;
    }

    @Override
    public String toString()
    {
        return "OrderBean{" +
                "rid='" + rid + '\'' +
                ", bookingNo='" + bookingNo + '\'' +
                ", storeId='" + storeId + '\'' +
                ", hid='" + hid + '\'' +
                ", reserveDate='" + reserveDate + '\'' +
                ", reserveTime='" + reserveTime + '\'' +
                ", mid='" + mid + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", serviceItem='" + serviceItem + '\'' +
                ", reserveStatus='" + reserveStatus + '\'' +
                ", reserveCreatedAt='" + reserveCreatedAt + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storePicture='" + storePicture + '\'' +
                ", nickName='" + nickName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", memberEmail='" + memberEmail + '\'' +
                ", reserveRemark='" + reserveRemark + '\'' +
                '}';
    }
}
