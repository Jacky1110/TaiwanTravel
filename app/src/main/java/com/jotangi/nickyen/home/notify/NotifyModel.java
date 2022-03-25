package com.jotangi.nickyen.home.notify;

import com.google.gson.annotations.SerializedName;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/28
 * {
 * pid: "1",
 * push_date: "2020-10-14 08:00:00",
 * member_type: "1",
 * push_message: "2020暢玩商圈買好買滿體驗地方文化魅力"
 * }
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class NotifyModel
{
    @SerializedName("pid")
    private String pid;
    @SerializedName("push_date")
    private String pushDate;
    @SerializedName("member_type")
    private String memberType;
    @SerializedName("push_message")
    private String pushMessage;

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getPushDate()
    {
        return pushDate;
    }

    public void setPushDate(String pushDate)
    {
        this.pushDate = pushDate;
    }

    public String getMemberType()
    {
        return memberType;
    }

    public void setMemberType(String memberType)
    {
        this.memberType = memberType;
    }

    public String getPushMessage()
    {
        return pushMessage;
    }

    public void setPushMessage(String pushMessage)
    {
        this.pushMessage = pushMessage;
    }

    @Override
    public String toString()
    {
        return "NotifyModel{" +
                "pid='" + pid + '\'' +
                ", pushDate='" + pushDate + '\'' +
                ", memberType='" + memberType + '\'' +
                ", pushMessage='" + pushMessage + '\'' +
                '}';
    }
}
