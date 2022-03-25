package com.jotangi.nickyen.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/9
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class ShopBean implements Serializable
{

    @SerializedName("sid")
    private String sid;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("store_type")
    private String storeType;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("shopping_area")
    private String shoppingArea;
    @SerializedName("store_phone")
    private String storePhone;
    @SerializedName("store_address")
    private String storeAddress;
    @SerializedName("store_website")
    private String storeWebsite;
    @SerializedName("store_picture")
    private String storePicture;
    @SerializedName("store_longitude")
    private String storeLongitude;
    @SerializedName("store_latitude")
    private String storeLatitude;
    @SerializedName("store_status")
    private String storeStatus;
    @SerializedName("store_descript") //從info得到
    private String storeDescript;
    @SerializedName("store_facebook")
    private String storeFacebook;
    @SerializedName("store_news")
    private String storeNews;
    @SerializedName("distance")
    private String distance;
    @SerializedName("store_opentime") //從info得到
    private String storeOpenTime;

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

    public String getStoreType()
    {
        return storeType;
    }

    public void setStoreType(String storeType)
    {
        this.storeType = storeType;
    }

    public String getStoreName()
    {
        return storeName;
    }

    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }

    public String getShoppingArea()
    {
        return shoppingArea;
    }

    public void setShoppingArea(String shoppingArea)
    {
        this.shoppingArea = shoppingArea;
    }

    public String getStorePhone()
    {
        return storePhone;
    }

    public void setStorePhone(String storePhone)
    {
        this.storePhone = storePhone;
    }

    public String getStoreAddress()
    {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress)
    {
        this.storeAddress = storeAddress;
    }

    public String getStoreWebsite()
    {
        return storeWebsite;
    }

    public void setStoreWebsite(String storeWebsite)
    {
        this.storeWebsite = storeWebsite;
    }

    public String getStorePicture()
    {
        return storePicture;
    }

    public void setStorePicture(String storePicture)
    {
        this.storePicture = storePicture;
    }

    public String getStoreLongitude()
    {
        return storeLongitude;
    }

    public void setStoreLongitude(String storeLongitude)
    {
        this.storeLongitude = storeLongitude;
    }

    public String getStoreLatitude()
    {
        return storeLatitude;
    }

    public void setStoreLatitude(String storeLatitude)
    {
        this.storeLatitude = storeLatitude;
    }

    public String getStoreStatus()
    {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus)
    {
        this.storeStatus = storeStatus;
    }

    public String getStoreDescript()
    {
        return this.storeDescript;
    }

    public void setStoreDescript(final String storeDescript)
    {
        this.storeDescript = storeDescript;
    }

    public String getStoreFacebook()
    {
        return this.storeFacebook;
    }

    public void setStoreFacebook(final String storeFacebook)
    {
        this.storeFacebook = storeFacebook;
    }

    public String getStoreNews()
    {
        return this.storeNews;
    }

    public void setStoreNews(final String storeNews)
    {
        this.storeNews = storeNews;
    }

    public String getDistance()
    {
        return this.distance;
    }

    public void setDistance(final String distance)
    {
        this.distance = distance;
    }

    public String getStoreOpenTime()
    {
        return this.storeOpenTime;
    }

    public void setStoreOpenTime(final String storeOpenTime)
    {
        this.storeOpenTime = storeOpenTime;
    }

    @Override
    public String toString()
    {
        return "ShopBean{" +
                "sid='" + sid + '\'' +
                ", storeId='" + storeId + '\'' +
                ", storeType='" + storeType + '\'' +
                ", storeName='" + storeName + '\'' +
                ", shoppingArea='" + shoppingArea + '\'' +
                ", storePhone='" + storePhone + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", storeWebsite='" + storeWebsite + '\'' +
                ", storePicture='" + storePicture + '\'' +
                ", storeLongitude='" + storeLongitude + '\'' +
                ", storeLatitude='" + storeLatitude + '\'' +
                ", storeStatus='" + storeStatus + '\'' +
                ", storeDescript='" + storeDescript + '\'' +
                ", storeFacebook='" + storeFacebook + '\'' +
                ", storeNews='" + storeNews + '\'' +
                ", distance='" + distance + '\'' +
                ", storeOpenTime='" + storeOpenTime + '\'' +
                '}';
    }
}
