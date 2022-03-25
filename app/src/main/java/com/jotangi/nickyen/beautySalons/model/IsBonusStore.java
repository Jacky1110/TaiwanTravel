package com.jotangi.nickyen.beautySalons.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by N!ck Yen on Date: 2022/1/12
 * {"sid":"121",
 *  "store_service":"1",
 *  "contract_startdate":"2022-01-01",
 *  "contract_enddate":"2022-12-31"}
 */
public class IsBonusStore
{
    @SerializedName("sid")
    private String sid;
    @SerializedName("store_service")
    private String storeService;
    @SerializedName("contract_startdate")
    private String contractStartdate;
    @SerializedName("contract_enddate")
    private String contractEnddate;

    public String getSid()
    {
        return sid;
    }

    public void setSid(String sid)
    {
        this.sid = sid;
    }

    public String getStoreService()
    {
        return storeService;
    }

    public void setStoreService(String storeService)
    {
        this.storeService = storeService;
    }

    public String getContractStartdate()
    {
        return contractStartdate;
    }

    public void setContractStartdate(String contractStartdate)
    {
        this.contractStartdate = contractStartdate;
    }

    public String getContractEnddate()
    {
        return contractEnddate;
    }

    public void setContractEnddate(String contractEnddate)
    {
        this.contractEnddate = contractEnddate;
    }

    @Override
    public String toString()
    {
        return "IsBonusStore{" +
                "sid='" + sid + '\'' +
                ", storeService='" + storeService + '\'' +
                ", contractStartdate='" + contractStartdate + '\'' +
                ", contractEnddate='" + contractEnddate + '\'' +
                '}';
    }
}
