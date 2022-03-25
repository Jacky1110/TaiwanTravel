package com.jotangi.nickyen.beautySalons.model;

import com.google.gson.annotations.SerializedName;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/8/18
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 * hid: "3",
 * store_id: "81",
 * nick_name: "Anna",
 * stylist_pic: "uploads/stylist3.jpg",
 * service_code: "1,2,3",
 * hairstylist_date: "2021-07-26 19:05:22"
 * },
 **/
public class DesignerBean
{
    @SerializedName("hid")
    private String hid;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("nick_name")
    private String nickName;
    @SerializedName("stylist_pic")
    private String stylistPic;
    @SerializedName("service_code")
    private String serviceCode;
    @SerializedName("hairstylist_date")
    private String hairstylistDate;

    public DesignerBean(final String hid, final String storeId, final String nickName, final String stylistPic, final String serviceCode, final String hairstylistDate)
    {
        this.hid = hid;
        this.storeId = storeId;
        this.nickName = nickName;
        this.stylistPic = stylistPic;
        this.serviceCode = serviceCode;
        this.hairstylistDate = hairstylistDate;
    }

    public String getHid()
    {
        return hid;
    }

    public void setHid(String hid)
    {
        this.hid = hid;
    }

    public String getStoreId()
    {
        return storeId;
    }

    public void setStoreId(String storeId)
    {
        this.storeId = storeId;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getStylistPic()
    {
        return stylistPic;
    }

    public void setStylistPic(String stylistPic)
    {
        this.stylistPic = stylistPic;
    }

    public String getServiceCode()
    {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode)
    {
        this.serviceCode = serviceCode;
    }

    public String getHairstylistDate()
    {
        return hairstylistDate;
    }

    public void setHairstylistDate(String hairstylistDate)
    {
        this.hairstylistDate = hairstylistDate;
    }

    @Override
    public String toString()
    {
        return "DesignerBean{" +
                "hid='" + hid + '\'' +
                ", storeId='" + storeId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", stylistPic='" + stylistPic + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", hairstylistDate='" + hairstylistDate + '\'' +
                '}';
    }
}
