package com.jotangi.nickyen.beautySalons.model;

import com.google.gson.annotations.SerializedName;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/8/21
 * {
 * status: "true",
 * code: "0x0200",
 * responseMessage: [
 * {
 * rid: "4",
 * booking_no: "16292238545647",
 * reserve_time: "12:00"
 * },
 * {
 * rid: "5",
 * booking_no: "16292238545648",
 * reserve_time: "15:00"
 * }]}
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class IsBookingBean
{

    @SerializedName("rid")
    private String rid;
    @SerializedName("booking_no")
    private String bookingNo;
    @SerializedName("reserve_time")
    private String reserveTime;

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

    public String getReserveTime()
    {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime)
    {
        this.reserveTime = reserveTime;
    }

    @Override
    public String toString()
    {
        return "IsBookingBean{" +
                "rid='" + rid + '\'' +
                ", bookingNo='" + bookingNo + '\'' +
                ", reserveTime='" + reserveTime + '\'' +
                '}';
    }
}
