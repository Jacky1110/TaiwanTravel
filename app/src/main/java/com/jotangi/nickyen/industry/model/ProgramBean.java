package com.jotangi.nickyen.industry.model;

import com.google.gson.annotations.SerializedName;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/6
 * pid: "1",
 * cid: "40",
 * store_id: "28",
 * store_name: "雲之南文創",
 * store_address: "桃園市平鎮區中正里中正一路16號1樓",
 * store_phone: "0915-254197",
 * class_name: "民族服飾穿戴體驗",
 * class_descript: "民族服飾穿戴體驗, 滇緬少數民族傳統服飾體驗, 一秒來到香格里拉!",
 * class_picture: "uploads/aa.jpg",
 * program_code: "101",
 * program_name: "民族服飾穿戴體驗(單人)",
 * program_descript: "民族服飾穿戴體驗",
 * program_time: "60",
 * program_price: "499",
 * program_limit: "1",
 * program_status: "0",
 * program_trash: "0"
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class ProgramBean
{
    @SerializedName("pid")
    private String pid;
    @SerializedName("cid")
    private String cid;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("store_address")
    private String storeAddress;
    @SerializedName("store_phone")
    private String storePhone;
    @SerializedName("class_name")
    private String className;
    @SerializedName("class_descript")
    private String classDescript;
    @SerializedName("class_picture")
    private String classPicture;
    @SerializedName("program_code")
    private String programCode;
    @SerializedName("program_name")
    private String programName;
    @SerializedName("program_descript")
    private String programDescript;
    @SerializedName("program_time")
    private String programTime;
    @SerializedName("program_price")
    private String programPrice;
    @SerializedName("program_limit")
    private String programLimit;
    @SerializedName("program_status")
    private String programStatus;
    @SerializedName("program_trash")
    private String programTrash;

    private boolean check;


    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getCid()
    {
        return cid;
    }

    public void setCid(String cid)
    {
        this.cid = cid;
    }

    public String getStoreId()
    {
        return storeId;
    }

    public void setStoreId(String storeId)
    {
        this.storeId = storeId;
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

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getClassDescript()
    {
        return classDescript;
    }

    public void setClassDescript(String classDescript)
    {
        this.classDescript = classDescript;
    }

    public String getClassPicture()
    {
        return classPicture;
    }

    public void setClassPicture(String classPicture)
    {
        this.classPicture = classPicture;
    }

    public String getProgramCode()
    {
        return programCode;
    }

    public void setProgramCode(String programCode)
    {
        this.programCode = programCode;
    }

    public String getProgramName()
    {
        return programName;
    }

    public void setProgramName(String programName)
    {
        this.programName = programName;
    }

    public String getProgramDescript()
    {
        return programDescript;
    }

    public void setProgramDescript(String programDescript)
    {
        this.programDescript = programDescript;
    }

    public String getProgramTime()
    {
        return programTime;
    }

    public void setProgramTime(String programTime)
    {
        this.programTime = programTime;
    }

    public String getProgramPrice()
    {
        return programPrice;
    }

    public void setProgramPrice(String programPrice)
    {
        this.programPrice = programPrice;
    }

    public String getProgramLimit()
    {
        return programLimit;
    }

    public void setProgramLimit(String programLimit)
    {
        this.programLimit = programLimit;
    }

    public String getProgramStatus()
    {
        return programStatus;
    }

    public void setProgramStatus(String programStatus)
    {
        this.programStatus = programStatus;
    }

    public String getProgramTrash()
    {
        return programTrash;
    }

    public void setProgramTrash(String programTrash)
    {
        this.programTrash = programTrash;
    }

    public boolean isCheck()
    {
        return this.check;
    }

    public void setCheck(final boolean check)
    {
        this.check = check;
    }

    public ProgramBean(final String pid, final String cid, final String storeId, final String storeName, final String programName)
    {
        this.pid = pid;
        this.cid = cid;
        this.storeId = storeId;
        this.storeName = storeName;
        this.programName = programName;
    }

    @Override
    public String toString()
    {
        return "ProgramBean{" +
                "pid='" + pid + '\'' +
                ", cid='" + cid + '\'' +
                ", storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", storePhone='" + storePhone + '\'' +
                ", className='" + className + '\'' +
                ", classDescript='" + classDescript + '\'' +
                ", classPicture='" + classPicture + '\'' +
                ", programCode='" + programCode + '\'' +
                ", programName='" + programName + '\'' +
                ", programDescript='" + programDescript + '\'' +
                ", programTime='" + programTime + '\'' +
                ", programPrice='" + programPrice + '\'' +
                ", programLimit='" + programLimit + '\'' +
                ", programStatus='" + programStatus + '\'' +
                ", programTrash='" + programTrash + '\'' +
                ", check=" + check +
                '}';
    }
}
