package com.jotangi.nickyen.argame.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * aid: "1",
 * ar_name: "1 "井旁拱手人"",
 * shopping_area: "3",
 * ar_address: "新竹縣北埔鄉南興村6鄰廟前街27號旁的窄巷進入前方20公尺",
 * ar_picture: "uploads/AR001.jpg",
 * ar_latitude: "24.699341888443985",
 * ar_longitude: "121.05773064838668",
 * coupon_id: "1",
 * ar_status: "0",
 * ar_descript2: "我已經躲很久了，你也找太慢了吧。"
 */
public class StoreBean implements Serializable {

    @SerializedName("aid")
    private String aid;
    @SerializedName("ar_name")
    private String ar_name;
    @SerializedName("ar_address")
    private String ar_address;
    @SerializedName("ar_picture")
    private String ar_picture;
    @SerializedName("ar_latitude")
    private String ar_latitude;
    @SerializedName("ar_longitude")
    private String ar_longitude;
    @SerializedName("coupon_id")
    private String coupon_id;
    @SerializedName("ar_status")
    private String ar_status;
    @SerializedName("ar_descript")
    private String ar_descript;
    @SerializedName("ar_descript2")
    private String ar_descript2;
    @SerializedName("shopping_area")
    private String shopping_area;

    @SerializedName("ar_picture2")
    private String ar_picture2;
    @SerializedName("ar_name2")
    private String ar_name2;

    public String getShopping_area() {
        return this.shopping_area;
    }

    public void setShopping_area(final String shopping_area) {
        this.shopping_area = shopping_area;
    }

    public String getAr_descript() {
        return this.ar_descript;
    }

    public void setAr_descript(final String ar_descript) {
        this.ar_descript = ar_descript;
    }

    public String getAid() {
        return this.aid;
    }

    public void setAid(final String aid) {
        this.aid = aid;
    }
    public String getAr_name2() {
        return this.ar_name2;
    }

    public void setAr_name2(final String ar_name2) {
        this.ar_name2 = ar_name2;
    }


    public String getAr_name() {
        return this.ar_name;
    }

    public void setAr_name(final String ar_name) {
        this.ar_name = ar_name;
    }

    public String getAr_address() {
        return this.ar_address;
    }

    public void setAr_address(final String ar_address) {
        this.ar_address = ar_address;
    }
    public String getAr_picture2() {
        return this.ar_picture2;
    }

    public void setAr_picture2(final String ar_picture2) {
        this.ar_picture2 = ar_picture2;
    }
    public String getAr_picture() {
        return this.ar_picture;
    }

    public void setAr_picture(final String ar_picture) {
        this.ar_picture = ar_picture;
    }

    public String getAr_latitude() {
        return this.ar_latitude;
    }

    public void setAr_latitude(final String ar_latitude) {
        this.ar_latitude = ar_latitude;
    }

    public String getAr_longitude() {
        return this.ar_longitude;
    }

    public void setAr_longitude(final String ar_longitude) {
        this.ar_longitude = ar_longitude;
    }

    public String getCoupon_id() {
        return this.coupon_id;
    }

    public void setCoupon_id(final String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getAr_status() {
        return this.ar_status;
    }

    public void setAr_status(final String ar_status) {
        this.ar_status = ar_status;
    }
    public String getArDescript2() {
        return this.ar_descript2;
    }

    public void setArDescript2(final String ar_descript2) {
        this.ar_descript2 = ar_descript2;
    }

    @Override
    public String toString()
    {
        return "StoreBean{" +
                "aid='" + aid + '\'' +
                ", ar_name='" + ar_name + '\'' +
                ", ar_address='" + ar_address + '\'' +
                ", ar_picture='" + ar_picture + '\'' +
                ", ar_latitude='" + ar_latitude + '\'' +
                ", ar_longitude='" + ar_longitude + '\'' +
                ", coupon_id='" + coupon_id + '\'' +
                ", ar_status='" + ar_status + '\'' +
                ", ar_descript='" + ar_descript + '\'' +
                ", shopping_area='" + shopping_area + '\'' +
                ", arDescript2='" +ar_descript2 + '\'' +
                '}';
    }
}
