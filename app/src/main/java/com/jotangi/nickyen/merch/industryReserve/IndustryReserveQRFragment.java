package com.jotangi.nickyen.merch.industryReserve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.industry.model.IndustryOrderBean;
import com.jotangi.nickyen.merch.MerchMainActivity;
import com.jotangi.nickyen.merch.reserve.OrderModel;
import com.jotangi.nickyen.merch.reserve.ReserveInputFragment;
import com.jotangi.nickyen.model.ShopBean;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.Optional;

public class IndustryReserveQRFragment extends Fragment
{
    private String price;
    private String storeName;

    private IndustryOrderBean industryOrderBean;

    private ImageButton btnGoBack;
    private ImageView imgCode;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            price = getArguments().getString("price");
            industryOrderBean = new Gson().fromJson(getArguments().getString("order"), IndustryOrderBean.class);
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
        storeName = AppUtility.convertStringToUTF8(industryOrderBean.getProgramName() +" X" +industryOrderBean.getProgramPerson());
        String str = "";
        try
        {
            str = "spot://payment?s_id=" + industryOrderBean.getStoreId() + "&program_name=" + storeName + "&total=" + price + "&booking_no=" + industryOrderBean.getBookingNo()+"&memberID="+industryOrderBean.getMemberId();
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