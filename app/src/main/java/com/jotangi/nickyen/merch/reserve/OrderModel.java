package com.jotangi.nickyen.merch.reserve;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/9/9
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class OrderModel
{
    private String code;
    private String item;
    private String fee;

    public OrderModel(final String code, final String item, final String fee)
    {
        this.code = code;
        this.item = item;
        this.fee = fee;
    }

    public String getItem()
    {
        return this.item;
    }

    public void setItem(final String item)
    {
        this.item = item;
    }

    public String getFee()
    {
        return this.fee;
    }

    public void setFee(final String fee)
    {
        this.fee = fee;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setCode(final String code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "OrderModel{" +
                "code='" + code + '\'' +
                ", item='" + item + '\'' +
                ", fee='" + fee + '\'' +
                '}';
    }
}
