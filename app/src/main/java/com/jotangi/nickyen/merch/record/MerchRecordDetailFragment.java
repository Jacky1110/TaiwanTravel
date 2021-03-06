package com.jotangi.nickyen.merch.record;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.member.model.OrderListBean;
import com.jotangi.nickyen.model.UserBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by N!ck Yen on Date: 2021/12/24
 */
public class MerchRecordDetailFragment extends Fragment implements View.OnClickListener
{
    private OrderListBean orderData;

    private Button btnBack;
    private ImageButton btnGoBack;

    private TextView txtTitle, txtContent, txtStoreName, txtOrderDate, txtBonus
            , txtOrderStatus, txtPayType, txtOrderAmount, txtActualAmount, txtMemberName
            , txtMemberTel, txtHongLiZengDian, txtHongLiDate, txtDiscount;

    private List<OrderListBean> orderListBeanList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            orderData = new Gson().fromJson(getArguments().getString("merchOrderBean"), OrderListBean.class);
        }
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
        ApiConnection.getMerchOrderInfo(orderNo, new ApiConnection.OnConnectResultListener()
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
        txtStoreName.setText(UserBean.MerchName);
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
        // ????????????
        txtDiscount = v.findViewById(R.id.tv_cash_ticket);
        //
        //????????????
        txtMemberName = v.findViewById(R.id.tv_member_name);
        txtMemberName.setText(orderData.getMemberName());
        //????????????
        txtMemberTel = v.findViewById(R.id.tv_member_phone);
        txtMemberTel.setText(orderData.getMid());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
            case R.id.btn_back:
                Navigation.findNavController(v).popBackStack();
                break;
        }
    }
}
