package com.jotangi.nickyen.beautySalons.model;

import com.google.gson.annotations.SerializedName;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/8/21
 * {status: "true",
 * code: "0x0200",
 * responseMessage: [
 * {
 * workingdate: "",
 * workingtype: "H",
 * timeperiod: "0"
 * },
 * {
 * workingdate: "2021-08-22",
 * workingtype: "C",
 * timeperiod: "0"
 * }]}
 * WorkingType: W 工作日  C 例假日  H 請假
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class WorkingDayBean
{

    @SerializedName("workingdate")
    private String workingdate;
    @SerializedName("workingtype")
    private String workingtype;
    @SerializedName("timeperiod")
    private String timeperiod;

    public String getWorkingdate()
    {
        return workingdate;
    }

    public void setWorkingdate(String workingdate)
    {
        this.workingdate = workingdate;
    }

    public String getWorkingtype()
    {
        return workingtype;
    }

    public void setWorkingtype(String workingtype)
    {
        this.workingtype = workingtype;
    }

    public String getTimeperiod()
    {
        return timeperiod;
    }

    public void setTimeperiod(String timeperiod)
    {
        this.timeperiod = timeperiod;
    }

    @Override
    public String toString()
    {
        return "WorkingDayBean{" +
                "workingdate='" + workingdate + '\'' +
                ", workingtype='" + workingtype + '\'' +
                ", timeperiod='" + timeperiod + '\'' +
                '}';
    }
}
