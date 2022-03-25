package com.jotangi.nickyen.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/28
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class MemberBean implements Serializable
{

    @SerializedName("member_id")
    private String memberId;
    @SerializedName("sid")
    private String sid;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("member_date")
    private String memberDate;
    @SerializedName("card_type")
    private String cardType;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("store_picture")
    private String storePicture;

    public String getMemberId()
    {
        return memberId;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public String getSid()
    {
        return sid;
    }

    public void setSid(String sid)
    {
        this.sid = sid;
    }

    public String getStoreId()
    {
        return storeId;
    }

    public void setStoreId(String storeId)
    {
        this.storeId = storeId;
    }

    public String getMemberDate()
    {
        return memberDate;
    }

    public void setMemberDate(String memberDate)
    {
        this.memberDate = memberDate;
    }

    public String getCardType()
    {
        return cardType;
    }

    public void setCardType(String cardType)
    {
        this.cardType = cardType;
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

    @Override
    public String toString()
    {
        return "MemberBean{" +
                "memberId='" + memberId + '\'' +
                ", sid='" + sid + '\'' +
                ", storeId='" + storeId + '\'' +
                ", memberDate='" + memberDate + '\'' +
                ", cardType='" + cardType + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storePicture='" + storePicture + '\'' +
                '}';
    }
}
