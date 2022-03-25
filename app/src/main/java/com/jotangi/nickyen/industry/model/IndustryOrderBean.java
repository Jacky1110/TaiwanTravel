package com.jotangi.nickyen.industry.model;

import com.google.gson.annotations.SerializedName;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/12
 * rid: "2",
 * booking_no: "163403355251476",
 * store_id: "28",
 * pid: "2",
 * reserve_date: "2021-10-12",
 * reserve_time: "18:00",
 * mid: "558",
 * member_id: "0918565779",
 * member_name: "蕊仔",
 * member_email: "",
 * program_person: "4",
 * reserve_status: "0",
 * reserve_created_at: "2021-10-12 18:12:32",
 * store_name: "雲之南文創",
 * store_address: "桃園市平鎮區中正里中正一路16號1樓",
 * store_phone: "0915-254197",
 * store_picture: "uploads/1625735655.jpg",
 * program_name: "民族服飾穿戴體驗+手做DIY銀飾體驗",
 * program_price: "1000"
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class IndustryOrderBean
{
    @SerializedName("rid")
    private String rid;
    @SerializedName("booking_no")
    private String bookingNo;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("pid")
    private String pid;
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
    @SerializedName("member_email")
    private String memberEmail;
    @SerializedName("program_person")
    private String programPerson;
    @SerializedName("reserve_status")
    private String reserveStatus;
    @SerializedName("reserve_created_at")
    private String reserveCreatedAt;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("store_address")
    private String storeAddress;
    @SerializedName("store_phone")
    private String storePhone;
    @SerializedName("store_picture")
    private String storePicture;
    @SerializedName("program_name")
    private String programName;
    @SerializedName("program_price")
    private String programPrice;
    @SerializedName("reserve_remark")
    private String reserveRemark;
    @SerializedName("reserve_note")
    private String reserveNote;

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

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
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

    public String getMemberEmail()
    {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail)
    {
        this.memberEmail = memberEmail;
    }

    public String getProgramPerson()
    {
        return programPerson;
    }

    public void setProgramPerson(String programPerson)
    {
        this.programPerson = programPerson;
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

    public String getStoreAddress()
    {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress)
    {
        this.storeAddress = storeAddress;
    }

    public String getStorePhone()
    {
        return storePhone;
    }

    public void setStorePhone(String storePhone)
    {
        this.storePhone = storePhone;
    }

    public String getStorePicture()
    {
        return storePicture;
    }

    public void setStorePicture(String storePicture)
    {
        this.storePicture = storePicture;
    }

    public String getProgramName()
    {
        return programName;
    }

    public void setProgramName(String programName)
    {
        this.programName = programName;
    }

    public String getProgramPrice()
    {
        return programPrice;
    }

    public void setProgramPrice(String programPrice)
    {
        this.programPrice = programPrice;
    }

    public String getReserveRemark()
    {
        return this.reserveRemark;
    }

    public void setReserveRemark(final String reserveRemark)
    {
        this.reserveRemark = reserveRemark;
    }

    public String getReserveNote()
    {
        return this.reserveNote;
    }

    public void setReserveNote(final String reserveNote)
    {
        this.reserveNote = reserveNote;
    }

    public IndustryOrderBean(final String pid, final String programPerson, final String programName)
    {
        this.pid = pid;
        this.programPerson = programPerson;
        this.programName = programName;
    }

    @Override
    public String toString()
    {
        return "IndustryOrderBean{" +
                "rid='" + rid + '\'' +
                ", bookingNo='" + bookingNo + '\'' +
                ", storeId='" + storeId + '\'' +
                ", pid='" + pid + '\'' +
                ", reserveDate='" + reserveDate + '\'' +
                ", reserveTime='" + reserveTime + '\'' +
                ", mid='" + mid + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberEmail='" + memberEmail + '\'' +
                ", programPerson='" + programPerson + '\'' +
                ", reserveStatus='" + reserveStatus + '\'' +
                ", reserveCreatedAt='" + reserveCreatedAt + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", storePhone='" + storePhone + '\'' +
                ", storePicture='" + storePicture + '\'' +
                ", programName='" + programName + '\'' +
                ", programPrice='" + programPrice + '\'' +
                ", reserveRemark='" + reserveRemark + '\'' +
                ", reserveNote='" + reserveNote + '\'' +
                '}';
    }
}
