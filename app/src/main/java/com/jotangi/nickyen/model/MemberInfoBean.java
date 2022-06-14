package com.jotangi.nickyen.model;

import com.google.gson.annotations.SerializedName;

/**
 * mid : 3
 * member_id : 0912345688
 * member_pwd : 1234
 * member_name : Nike Yen
 * member_type : 1
 * member_gender : 1
 * member_email : nike.yen@jotangi.com
 * member_birthday : 1990-01-01
 * member_address : 新北市中和區中和路1號
 * member_phone : 0912345688
 * member_picture : null
 * member_totalpoints : 168
 * member_usingpoints : 32
 * member_status : 0
 * recommand_code : 654321
 * bonuswillget ： 100
 */

public class MemberInfoBean
{
    @SerializedName("0")
    public static String mid;
    @SerializedName("1")
    public static String member_id;
    @SerializedName("2")
    public static String member_pwd;
    @SerializedName("3")
    public static String member_name;
    @SerializedName("4")
    public static String member_type;
    @SerializedName("5")
    public static String member_gender;
    @SerializedName("6")
    public static String member_email;
    @SerializedName("7")
    public static String member_birthday;
    @SerializedName("8")
    public static String member_address;
    @SerializedName("9")
    public static String member_phone;
    @SerializedName("10")
    public static Object member_picture;
    @SerializedName("11")
    public static String member_totalpoints;
    @SerializedName("12")
    public static String member_usingpoints;
    @SerializedName("13")
    public static String member_status;
    @SerializedName("14")
    public static String recommend_code;
    @SerializedName("16")
    public static String bonus_will_get;


    public static int member_points;
    public static String bonus_end_date;
    public static String totalBonus;
    public static String day;
    public static String week;
    public static String month;
    public static String decryptId;
    public static String decryptPwd;
    public static String decryptName;
    public static String decryptEmail;
    public static String decryptBirthday;
    public static String decryptAddress;
    public static String decryptPhone;
    public static String decryptRecommendCode;

    public String getMid()
    {
        return mid;
    }

    public void setMid(String mid)
    {
        this.mid = mid;
    }

    public String getMember_id()
    {
        return member_id;
    }

    public void setMember_id(String member_id)
    {
        this.member_id = member_id;
    }

    public String getMember_pwd()
    {
        return member_pwd;
    }

    public void setMember_pwd(String member_pwd)
    {
        this.member_pwd = member_pwd;
    }

    public String getMember_name()
    {
        return member_name;
    }

    public void setMember_name(String member_name)
    {
        this.member_name = member_name;
    }

    public String getMember_type()
    {
        return member_type;
    }

    public void setMember_type(String member_type)
    {
        this.member_type = member_type;
    }

    public String getMember_gender()
    {
        return member_gender;
    }

    public void setMember_gender(String member_gender)
    {
        this.member_gender = member_gender;
    }

    public String getMember_email()
    {
        return member_email;
    }

    public void setMember_email(String member_email)
    {
        this.member_email = member_email;
    }

    public String getMember_birthday()
    {
        return member_birthday;
    }

    public void setMember_birthday(String member_birthday)
    {
        this.member_birthday = member_birthday;
    }

    public String getMember_address()
    {
        return member_address;
    }

    public void setMember_address(String member_address)
    {
        this.member_address = member_address;
    }

    public String getMember_phone()
    {
        return member_phone;
    }

    public void setMember_phone(String member_phone)
    {
        this.member_phone = member_phone;
    }

    public Object getMember_picture()
    {
        return member_picture;
    }

    public void setMember_picture(Object member_picture)
    {
        this.member_picture = member_picture;
    }

    public String getMember_totalpoints()
    {
        return member_totalpoints;
    }

    public void setMember_totalpoints(String member_totalpoints)
    {
        this.member_totalpoints = member_totalpoints;
    }

    public String getMember_usingpoints()
    {
        return member_usingpoints;
    }

    public void setMember_usingpoints(String member_usingpoints)
    {
        this.member_usingpoints = member_usingpoints;
    }

    public String getMember_status()
    {
        return member_status;
    }

    public void setMember_status(String member_status)
    {
        this.member_status = member_status;
    }

    public String getRecommand_code()
    {
        return recommend_code;
    }

    public void setRecommand_code(String recommand_code)
    {
        this.recommend_code = recommand_code;
    }

    public static String getBonus_will_get()
    {
        return MemberInfoBean.bonus_will_get;
    }

    public static void setBonus_will_get(final String bonus_will_get)
    {
        MemberInfoBean.bonus_will_get = bonus_will_get;
    }
}
