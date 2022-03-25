package com.jotangi.nickyen.merch.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/7/2
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class MerchMemberInfoBean implements Serializable
{

    @SerializedName("mid")
    private String mid;
    @SerializedName("member_id")
    private String memberId;
    @SerializedName("member_pwd")
    private String memberPwd;
    @SerializedName("member_name")
    private String memberName;
    @SerializedName("member_type")
    private String memberType;
    @SerializedName("member_gender")
    private String memberGender;
    @SerializedName("member_email")
    private String memberEmail;
    @SerializedName("member_birthday")
    private String memberBirthday;
    @SerializedName("member_address")
    private String memberAddress;
    @SerializedName("member_phone")
    private String memberPhone;
    @SerializedName("member_picture")
    private String memberPicture;
    @SerializedName("member_totalpoints")
    private String memberTotalpoints;
    @SerializedName("member_usingpoints")
    private String memberUsingpoints;
    @SerializedName("member_status")
    private String memberStatus;
    @SerializedName("recommend_code")
    private String recommendCode;
    @SerializedName("member_sid")
    private String memberSid;

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

    public String getMemberPwd()
    {
        return memberPwd;
    }

    public void setMemberPwd(String memberPwd)
    {
        this.memberPwd = memberPwd;
    }

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public String getMemberType()
    {
        return memberType;
    }

    public void setMemberType(String memberType)
    {
        this.memberType = memberType;
    }

    public String getMemberGender()
    {
        return memberGender;
    }

    public void setMemberGender(String memberGender)
    {
        this.memberGender = memberGender;
    }

    public String getMemberEmail()
    {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail)
    {
        this.memberEmail = memberEmail;
    }

    public String getMemberBirthday()
    {
        return memberBirthday;
    }

    public void setMemberBirthday(String memberBirthday)
    {
        this.memberBirthday = memberBirthday;
    }

    public String getMemberAddress()
    {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress)
    {
        this.memberAddress = memberAddress;
    }

    public String getMemberPhone()
    {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone)
    {
        this.memberPhone = memberPhone;
    }

    public String getMemberPicture()
    {
        return memberPicture;
    }

    public void setMemberPicture(String memberPicture)
    {
        this.memberPicture = memberPicture;
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

    public String getMemberStatus()
    {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus)
    {
        this.memberStatus = memberStatus;
    }

    public String getRecommendCode()
    {
        return recommendCode;
    }

    public void setRecommendCode(String recommendCode)
    {
        this.recommendCode = recommendCode;
    }

    public String getMemberSid()
    {
        return memberSid;
    }

    public void setMemberSid(String memberSid)
    {
        this.memberSid = memberSid;
    }

    @Override
    public String toString()
    {
        return "MerchMemberInfoBean{" +
                "mid='" + mid + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberPwd='" + memberPwd + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberType='" + memberType + '\'' +
                ", memberGender='" + memberGender + '\'' +
                ", memberEmail='" + memberEmail + '\'' +
                ", memberBirthday='" + memberBirthday + '\'' +
                ", memberAddress='" + memberAddress + '\'' +
                ", memberPhone='" + memberPhone + '\'' +
                ", memberPicture='" + memberPicture + '\'' +
                ", memberTotalpoints='" + memberTotalpoints + '\'' +
                ", memberUsingpoints='" + memberUsingpoints + '\'' +
                ", memberStatus='" + memberStatus + '\'' +
                ", recommendCode='" + recommendCode + '\'' +
                ", memberSid='" + memberSid + '\'' +
                '}';
    }
}
