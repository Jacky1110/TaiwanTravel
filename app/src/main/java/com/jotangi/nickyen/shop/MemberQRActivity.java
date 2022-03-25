package com.jotangi.nickyen.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.home.MyDiscountNew2Activity;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.model.UserBean;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;

public class MemberQRActivity extends AppCompatActivity
{
    private ImageButton btnGoBack;
    private ImageView imgCode, imgContent;
    private TextView txtName, txtContent, txtDate, txtTitle;
    CouponListBean couponListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_qr);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        String str = getIntent().getStringExtra("ticket");
        couponListBean = new Gson().fromJson(str, CouponListBean.class);

        initView();

    }

    private void initView()
    {
        txtTitle = findViewById(R.id.textview);
        if (couponListBean.getCouponType().equals("3"))
        {

            txtTitle.setText("入會禮");
        } else if (couponListBean.getCouponType().equals("6"))
        {
            txtTitle.setText("註冊禮");
        } else
        {
            txtTitle.setText("優惠券");
        }
        txtName = findViewById(R.id.tv_content);
        txtName.setText("憑本券可兌換" + couponListBean.getCouponName());
        txtContent = findViewById(R.id.tv_content1);
        txtContent.setText(couponListBean.getCouponDescription());
        txtDate = findViewById(R.id.tv_date);
        txtDate.setText(couponListBean.getCouponStartdate() + "～" + couponListBean.getCouponEnddate());
        imgCode = findViewById(R.id.iv_qrcode);
        imgContent = findViewById(R.id.iv_content);
        PicassoTrustAll.getInstance(this).load(ApiConstant.API_IMAGE + couponListBean.getCouponPicture()).into(imgContent);
        btnGoBack = findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MemberQRActivity.this, MyDiscountNew2Activity.class);
                startActivity(i);
                finish();
            }
        });
        genCode();
    }

    private void genCode()
    {
        String str = null;
        try
        {
            str = "spot://payment?m_id=" + AppUtility.DecryptAES2(UserBean.member_id) + "&coupon_no=" + couponListBean.getCouponNo();
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