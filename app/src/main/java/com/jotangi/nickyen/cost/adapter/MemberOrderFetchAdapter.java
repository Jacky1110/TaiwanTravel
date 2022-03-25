package com.jotangi.nickyen.cost.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.cost.CostGeneralActivity;
import com.jotangi.nickyen.member.MemberRecordInfoFragment;
import com.jotangi.nickyen.member.model.OrderListBean;
import com.jotangi.nickyen.model.ShopBean;
import com.jotangi.nickyen.shop.ProductFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/7/14
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class MemberOrderFetchAdapter extends RecyclerView.Adapter<MemberOrderFetchAdapter.ViewHolder>
{
    private ArrayList<OrderListBean> orderData;
    private Context context;

    public MemberOrderFetchAdapter(final ArrayList<OrderListBean> orderData, final Context context)
    {
        this.orderData = orderData;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        View v;
        TextView txtStoreName, txtOrderDate, txtAmount, btnOrder;

        public ViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            v = itemView;
            txtStoreName = itemView.findViewById(R.id.tv_store_name);
            txtOrderDate = itemView.findViewById(R.id.tv_order_date);
            txtAmount = itemView.findViewById(R.id.tv_amount);
            btnOrder = itemView.findViewById(R.id.btn_order);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        return new MemberOrderFetchAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_record, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int i)
    {
        holder.txtOrderDate.setText(orderData.get(i).getOrderDate());
        holder.txtAmount.setText("消費金額：NT$" + AppUtility.strAddComma(orderData.get(i).getOrderPay()));
        holder.txtStoreName.setText(orderData.get(i).getStoreName());
        holder.btnOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MemberRecordInfoFragment infoFragment = MemberRecordInfoFragment.newInstance(orderData.get(i));
                ((CostGeneralActivity)v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.recordLayout, infoFragment, null).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return orderData.size();
    }
}
