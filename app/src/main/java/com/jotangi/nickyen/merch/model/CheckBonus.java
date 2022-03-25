package com.jotangi.nickyen.merch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/7/9
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class CheckBonus implements Serializable
{

    @SerializedName("mid")
    private String mid;
    @SerializedName("member_name")
    private String memberName;
    @SerializedName("member_totalpoints")
    private String memberTotalpoints;
    @SerializedName("member_usingpoints")
    private String memberUsingpoints;

    public String getMid()
    {
        return mid;
    }

    public void setMid(String mid)
    {
        this.mid = mid;
    }

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public String getMemberTotalpoints()
    {
        return memberTotalpoints;
    }

    public void setMemberTotalpoints(String memberTotalpoints)
    {
        this.memberTotalpoints = memberTotalpoints;
    }

    public String getMemberUsingpoints()
    {
        return memberUsingpoints;
    }

    public void setMemberUsingpoints(String memberUsingpoints)
    {
        this.memberUsingpoints = memberUsingpoints;
    }

    @Override
    public String toString()
    {
        return "CheckBonus{" +
                "mid='" + mid + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberTotalpoints='" + memberTotalpoints + '\'' +
                ", memberUsingpoints='" + memberUsingpoints + '\'' +
                '}';
    }
}
