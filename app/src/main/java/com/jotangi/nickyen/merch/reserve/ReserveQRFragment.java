package com.jotangi.nickyen.merch.reserve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.member.SuccessActivity;
import com.jotangi.nickyen.merch.MerchMainActivity;
import com.jotangi.nickyen.model.ShopBean;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Optional;

public class ReserveQRFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private String shop;
    private String total;
    private String bookingNo;
    private String storeName;

    private ShopBean shopBean;

    private ImageButton btnGoBack;
    private ImageView imgCode;

    public ReserveQRFragment()
    {
        // Required empty public constructor
    }

    public static ReserveQRFragment newInstance(String shop, String total, String booking, String desi, ArrayList<OrderModel> ser)
    {
        ReserveQRFragment fragment = new ReserveQRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, shop);
        args.putString(ARG_PARAM2, total);
        args.putString(ARG_PARAM3, booking);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            shop = getArguments().getString(ARG_PARAM1);
            total = getArguments().getString(ARG_PARAM2);
            bookingNo = getArguments().getString(ARG_PARAM3);
            shopBean = new Gson().fromJson(shop, ShopBean.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_reserve_q_r, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        imgCode = getView().findViewById(R.id.iv_qrcode);
        btnGoBack = getView().findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), MerchMainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        genCode();
    }

    private void genCode()
    {
        storeName = AppUtility.convertStringToUTF8(shopBean.getStoreName());
        String str = "";
        String service ="";
        for (OrderModel s: ReserveInputFragment.orderModels)
        {
            service += AppUtility.convertStringToUTF8(s.getItem())+","+ s.getFee()+"///";
        }

        service = Optional.ofNullable(service)
                .filter(s -> s.length() != 0)
                .map(s -> s.substring(0, s.length() - 3))
                .orElse(service);
        try
        {
            str = "spot://payment?s_id=" + shopBean.getSid() + "&store_name=" + storeName + "&total=" + total + "&booking_no=" + bookingNo
                    + "&designer=" + ReserveInputFragment.sDesigner + "&service="+ service + "";
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d("豪豪", "genCode: " + str);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try
        {
            Bitmap bit = encoder.encodeBitmap(str, BarcodeFormat.QR_CODE,
                    250, 250);
            imgCode.setImageBitmap(bit);
        } catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
}