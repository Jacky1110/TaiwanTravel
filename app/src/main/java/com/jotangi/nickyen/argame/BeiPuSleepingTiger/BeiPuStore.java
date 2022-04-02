package com.jotangi.nickyen.argame.BeiPuSleepingTiger;

import com.google.gson.annotations.SerializedName;

public class BeiPuStore {

    @SerializedName("rid")
    private String rid;
    @SerializedName("qid")
    private String qid;
    @SerializedName("aid")
    private String aid;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("store_address")
    private String storeAddress;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    @Override
    public String toString() {
        return "BeiPuStore{" +
                "rid='" + rid + '\'' +
                ", qid='" + qid + '\'' +
                ", aid='" + aid + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                '}';
    }
}
