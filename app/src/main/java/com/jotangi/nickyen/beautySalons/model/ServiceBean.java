package com.jotangi.nickyen.beautySalons.model;

import com.google.gson.annotations.SerializedName;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/8/19
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 * xid: "2",
 * store_id: "81",
 * hid: "1",
 * service_code: "2",
 * service_name: "洗髮",
 * service_time: "15",
 * service_price: "250",
 * service_status: "0"
 **/
public class ServiceBean
{
    @SerializedName("xid")
    private String xid;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("hid")
    private String hid;
    @SerializedName("service_code")
    private String serviceCode;
    @SerializedName("service_name")
    private String serviceName;
    @SerializedName("service_time")
    private String serviceTime;
    @SerializedName("service_price")
    private String servicePrice;
    @SerializedName("service_status")
    private String serviceStatus;
    boolean check;

    public String getXid()
    {
        return xid;
    }

    public void setXid(String xid)
    {
        this.xid = xid;
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

    public String getServiceCode()
    {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode)
    {
        this.serviceCode = serviceCode;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getServiceTime()
    {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime)
    {
        this.serviceTime = serviceTime;
    }

    public String getServicePrice()
    {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice)
    {
        this.servicePrice = servicePrice;
    }

    public String getServiceStatus()
    {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus)
    {
        this.serviceStatus = serviceStatus;
    }

    public boolean isCheck()
    {
        return this.check;
    }

    public void setCheck(boolean check)
    {
        this.check = check;
    }

    @Override
    public String toString()
    {
        return "ServiceBean{" +
                "xid='" + xid + '\'' +
                ", storeId='" + storeId + '\'' +
                ", hid='" + hid + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceTime='" + serviceTime + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", check=" + check +
                '}';
    }
}
