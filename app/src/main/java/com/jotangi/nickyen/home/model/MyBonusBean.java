package com.jotangi.nickyen.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/7/18
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/

/**
 * bid: "1",
 * member_id: "58",
 * order_no: "16255356107491",
 * bonus_date: "2021-07-06 09:40:10",
 * bonus_type: "2",
 * bonus: "200"
 * store_name
 */
public class MyBonusBean implements Serializable
{

    @SerializedName("bid")
    private String bid;
    @SerializedName("member_id")
    private String memberId;
    @SerializedName("order_no")
    private String orderNo;
    @SerializedName("bonus_date")
    private String bonusDate;
    @SerializedName("bonus_type")
    private String bonusType;
    @SerializedName("bonus")
    private String bonus;
    @SerializedName("store_name")
    private String storeName;


    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid)
    {
        this.bid = bid;
    }

    public String getMemberId()
    {
        return memberId;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getBonusDate()
    {
        return bonusDate;
    }

    public void setBonusDate(String bonusDate)
    {
        this.bonusDate = bonusDate;
    }

    public String getBonusType()
    {
        return bonusType;
    }

    public void setBonusType(String bonusType)
    {
        this.bonusType = bonusType;
    }

    public String getBonus()
    {
        return bonus;
    }

    public void setBonus(String bonus)
    {
        this.bonus = bonus;
    }

    public MyBonusBean(final String bid, final String memberId, final String orderNo)
    {
        this.bid = bid;
        this.memberId = memberId;
        this.orderNo = orderNo;
    }

    public String getStoreName()
    {
        return this.storeName;
    }

    public void setStoreName(final String storeName)
    {
        this.storeName = storeName;
    }

    @Override
    public String toString()
    {
        return "MyBonusBean{" +
                "bid='" + bid + '\'' +
                ", memberId='" + memberId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", bonusDate='" + bonusDate + '\'' +
                ", bonusType='" + bonusType + '\'' +
                ", bonus='" + bonus + '\'' +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
