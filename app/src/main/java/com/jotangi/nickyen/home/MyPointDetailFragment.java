package com.jotangi.nickyen.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.home.model.MyBonusBean;
import com.jotangi.nickyen.member.model.OrderListBean;
import com.jotangi.nickyen.model.MemberInfoBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyPointDetailFragment extends BaseFragment
{
    static MyBonusBean myBonusBean;
    private Button btnBack;
    private ImageButton btnGoBack;
    private ProgressBar progressBar;
    private TextView txtTitle, txtContent, txtStoreName, txtOrderDate, txtBonus
            , txtOrderStatus, txtPayType, txtOrderAmount, txtActualAmount, txtMemberName
            , txtMemberTel, txtNoData, txtHongLiZengDian, txtHongLiDate, txtDiscount;

    private List<OrderListBean> orderListBeanList;

    public static MyPointDetailFragment newInstance(MyBonusBean data)
    {
        MyPointDetailFragment fragment = new MyPointDetailFragment();
        Bundle args = new Bundle();
        myBonusBean = data;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_member_record_info, container, false);
        initView(v);
        getOrderInfo(myBonusBean.getOrderNo());
        return v;
    }

    private void getOrderInfo(String orderNo)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.getOrderInfo(orderNo, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<OrderListBean>>()
                {
                }.getType();
                orderListBeanList = new Gson().fromJson(jsonString, type);
                getActivity().runOnUiThread(() ->
                {
                    progressBar.setVisibility(View.GONE);
                    showUI(orderListBeanList.get(0));
                });
            }

            @Override
            public void onFailure(String message)
            {
                getActivity().runOnUiThread(() ->
                {
                    progressBar.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void showUI(OrderListBean orderListBean)
    {
        txtStoreName.setText(orderListBean.getApiStoreName());
        txtOrderDate.setText(orderListBean.getOrderDate());
        txtBonus.setText(orderListBean.getBonusPoint());
        if (orderListBean.getOrderStatus().equals("1"))
        {
            txtOrderStatus.setText("已完成");
        } else
        {
            txtOrderStatus.setText("未完成");
        }
        if (orderListBean.getPayType().equals("1"))
        {
            txtPayType.setText("現金");
        } else if (orderListBean.getPayType().equals("2"))
        {
            txtPayType.setText("信用卡");
        } else
        {
            txtPayType.setText("其他支付");
        }
        txtOrderAmount.setText(orderListBean.getOrderAmount());
        txtActualAmount.setText(orderListBean.getOrderPay());
        txtHongLiZengDian.setText(orderListBean.getBonusGet());
        txtHongLiDate.setText(orderListBean.getBonusDate());
        txtDiscount.setText(orderListBean.getDiscountAmount());
    }

    private void initView(View v)
    {
        btnBack = v.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);
        progressBar = v.findViewById(R.id.progressBar);

        txtTitle = v.findViewById(R.id.textview);
        txtTitle.setText("紅利點數");

        txtContent = v.findViewById(R.id.tvContent);
        txtContent.setText("紅利明細");

        txtNoData = v.findViewById(R.id.tv_noData);

        //店名
        txtStoreName = v.findViewById(R.id.tv_store_name);
//        txtStoreName.setText(orderData.getStoreName());
        //訂單日期
        txtOrderDate = v.findViewById(R.id.tv_order_date);
//        txtOrderDate.setText(orderData.getOrderDate());
        //紅利折抵
        txtBonus = v.findViewById(R.id.tv_bonus);
//        txtBonus.setText(orderData.getBonusPoint());
        //訂單狀態
        txtOrderStatus = v.findViewById(R.id.tv_order_status);
//        if (orderData.getOrderStatus().equals("1"))
//        {
//            txtOrderStatus.setText("已完成");
//        } else
//        {
//            txtOrderStatus.setText("未完成");
//        }
//        //付款方式
        txtPayType = v.findViewById(R.id.tv_pay_type);
//        if (orderData.getPayType().equals("1"))
//        {
//            txtPayType.setText("現金");
//        } else if (orderData.getPayType().equals("2"))
//        {
//            txtPayType.setText("信用卡");
//        } else
//        {
//            txtPayType.setText("其他支付");
//        }
        //訂單金額
        txtOrderAmount = v.findViewById(R.id.tv_order_amount);
//        txtOrderAmount.setText(orderData.getOrderAmount());
        //實付金額
        txtActualAmount = v.findViewById(R.id.tv_actual_amount);
//        txtActualAmount.setText(orderData.getOrderPay());
        // 紅利贈點
        txtHongLiZengDian = v.findViewById(R.id.tv_hongLiZengDian);
        // 紅利歸戶日期
        txtHongLiDate = v.findViewById(R.id.tv_hongLiDate);
        // 現金折抵
        txtDiscount = v.findViewById(R.id.tv_cash_ticket);
        //會員姓名
        txtMemberName = v.findViewById(R.id.tv_member_name);
        txtMemberName.setText(MemberInfoBean.decryptName);
        //會員電話
        txtMemberTel = v.findViewById(R.id.tv_member_phone);
        txtMemberTel.setText(MemberInfoBean.decryptPhone);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
        }
    }
}