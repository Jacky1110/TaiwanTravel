package com.jotangi.nickyen.member;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.member.model.OrderListBean;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.model.ShopBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MemberRecordInfoFragment extends BaseFragment
{
    static OrderListBean orderData;

    private Button btnBack;
    private ImageButton btnGoBack;
    private TextView txtTitle, txtContent, txtStoreName, txtOrderDate, txtBonus, txtOrderStatus
            , txtPayType, txtOrderAmount, txtActualAmount, txtMemberName, txtMemberTel
            , txtHongLiZengDian, txtHongLiDate, txtExpireDate, txtDiscount;
    private List<OrderListBean> orderListBeanList = new ArrayList<>();

    public MemberRecordInfoFragment()
    {
        // Required empty public constructor
    }

    public static MemberRecordInfoFragment newInstance(OrderListBean data)
    {
        MemberRecordInfoFragment fragment = new MemberRecordInfoFragment();
        Bundle args = new Bundle();
        orderData = data;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_member_record_info, container, false);
        initView(v);
        getOrderInfo(orderData.getOrderNo());
        return v;
    }

    private void getOrderInfo(String orderNo)
    {
        ApiConnection.getOrderInfo(orderNo, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<OrderListBean>>()
                {
                }.getType();
                orderListBeanList = new Gson().fromJson(jsonString, type);
                Log.d("??????", "onSuccess: "+orderListBeanList);
                getActivity().runOnUiThread(() ->
                {
                    txtHongLiZengDian.setText(orderListBeanList.get(0).getBonusGet());
                    txtHongLiDate.setText(orderListBeanList.get(0).getBonusDate());
                    txtExpireDate.setText(orderListBeanList.get(0).getBonusEndDate());
                    txtDiscount.setText(orderListBeanList.get(0).getDiscountAmount());
                });
            }

            @Override
            public void onFailure(String message)
            {
                getActivity().runOnUiThread(() ->
                {
                    txtHongLiZengDian.setText("-");
                    txtHongLiDate.setText("-");
                    txtExpireDate.setText("-");
                    txtDiscount.setText("-");
                });
            }
        });
    }

    private void initView(View v)
    {
        btnBack = v.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);

        txtTitle = v.findViewById(R.id.textview);
        txtTitle.setText("????????????");

        txtContent = v.findViewById(R.id.tvContent);
        txtContent.setText("????????????");
        //??????
        txtStoreName = v.findViewById(R.id.tv_store_name);
        txtStoreName.setText(orderData.getStoreName());
        //????????????
        txtOrderDate = v.findViewById(R.id.tv_order_date);
        txtOrderDate.setText(orderData.getOrderDate());
        //????????????
        txtBonus = v.findViewById(R.id.tv_bonus);
        txtBonus.setText(orderData.getBonusPoint());
        //????????????
        txtOrderStatus = v.findViewById(R.id.tv_order_status);
        if (orderData.getOrderStatus().equals("1"))
        {
            txtOrderStatus.setText("?????????");
        } else
        {
            txtOrderStatus.setText("?????????");
        }
        //????????????
        txtPayType = v.findViewById(R.id.tv_pay_type);
        if (orderData.getPayType().equals("1"))
        {
            txtPayType.setText("??????");
        } else if (orderData.getPayType().equals("2"))
        {
            txtPayType.setText("?????????");
        } else
        {
            txtPayType.setText("????????????");
        }
        //????????????
        txtOrderAmount = v.findViewById(R.id.tv_order_amount);
        txtOrderAmount.setText(AppUtility.strAddComma(orderData.getOrderAmount()));
        //????????????
        txtActualAmount = v.findViewById(R.id.tv_actual_amount);
        txtActualAmount.setText(AppUtility.strAddComma(orderData.getOrderPay()));
        // ????????????
        txtHongLiZengDian = v.findViewById(R.id.tv_hongLiZengDian);
        // ??????????????????
        txtHongLiDate = v.findViewById(R.id.tv_hongLiDate);
        // ??????????????????
        txtExpireDate = v.findViewById(R.id.tv_Expire_Date);
        // ????????????
        txtDiscount = v.findViewById(R.id.tv_cash_ticket);
        //????????????
        txtMemberName = v.findViewById(R.id.tv_member_name);
        txtMemberName.setText(MemberInfoBean.decryptName);
        //????????????
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