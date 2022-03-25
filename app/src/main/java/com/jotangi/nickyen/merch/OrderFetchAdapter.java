package com.jotangi.nickyen.merch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.merch.model.MerchOrderBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/7/13
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class OrderFetchAdapter extends BaseQuickAdapter<MerchOrderBean, BaseViewHolder>
{
    public OrderFetchAdapter(int layoutResId, @Nullable List<MerchOrderBean> data)
    {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MerchOrderBean merchOrderBean)
    {
        //日期
        String date =merchOrderBean.getOrderDate().substring(0,10);
        String time =merchOrderBean.getOrderDate().substring(11);
        baseViewHolder.setText(R.id.tv_order_date, date+"\n"+"  "+time);
        //付款方式
        String titleStr;
        switch (merchOrderBean.getPayType()) {
            case "1":
                titleStr = "現金";
                break;
            case "2":
                titleStr = "信用卡";
                break;
            default:
                titleStr = "其他支付";
        }
        baseViewHolder.setText(R.id.tv_pay_type, titleStr);
        //金額
        baseViewHolder.setText(R.id.tv_amount,"NT$"+ AppUtility.strAddComma(merchOrderBean.getOrderPay()));
        //退款
//        baseViewHolder.setText(R.id.tv_refund, merchOrderBean.());

        TextView btnDetail = baseViewHolder.getView(R.id.btn_order);
        btnDetail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putString("merchOrderBean", new Gson().toJson(merchOrderBean));
                Navigation.findNavController(view).navigate(R.id.action_merchRecordFragment_to_merchRecordDetailFragment, bundle);
            }
        });

    }
}
