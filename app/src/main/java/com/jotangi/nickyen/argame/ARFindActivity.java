package com.jotangi.nickyen.argame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.base.BaseActivity;

import java.util.ArrayList;

public class ARFindActivity extends BaseActivity
{

    private FrameLayout btnBack, btnFind;

    private TextView title, txtContent, txtAddress;
    private ImageView storeImg;
    private String latlocation;
    private String lonlocation;
    private String aid;

    private ArrayList<StoreBean> storeBean2;
    private boolean b, b2, b3, b4, b5, b6, b7, b8;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_find);

        SharedPreferences doll = getSharedPreferences("doll", MODE_PRIVATE);
        b = doll.getBoolean("isStatus1", false);
        b2 = doll.getBoolean("isStatus2", false);
        b3 = doll.getBoolean("isStatus3", false);
        b4 = doll.getBoolean("isStatus4", false);
        b5 = doll.getBoolean("isStatus5", false);
        b6 = doll.getBoolean("isStatus6", false);
        b7 = doll.getBoolean("isStatus7", false);
        b8 = doll.getBoolean("isStatus8", false);

        storeBean2 = ARStoreActivity.storeBean;

        title = findViewById(R.id.ar_find_title);
        txtContent = findViewById(R.id.tv_content);
        txtAddress = findViewById(R.id.tv_address);
        storeImg = findViewById(R.id.img_ARFind);

        btnBack = findViewById(R.id.btnARBack);
        btnFind = findViewById(R.id.btnARFind);
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ARFindActivity.this,ARStoreActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //接收上一頁按鈕傳來的店家編號(笨蛋方法 有更好的可以改 但這個方法可以正常使用
        Intent intent = getIntent();
        String data = intent.getStringExtra("pass");

        if (data != null && storeBean2 != null)
        {
            setData(data);
        } else
        {
            Toast.makeText(this, "發生了點問題，請退出重新", Toast.LENGTH_SHORT).show();
        }

        btnFind.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (data.equals("1") && b)
                {
                    makeToastTextAndShow("您已領取過", 3500);
                    return;
                }
                if (data.equals("2") && b2)
                {
                    makeToastTextAndShow("您已領取過", 3500);
                    return;
                }
                if (data.equals("3") && b3)
                {
                    makeToastTextAndShow("您已領取過", 3500);
                    return;
                }
                if (data.equals("4") && b4)
                {
                    makeToastTextAndShow("您已領取過", 3500);
                    return;
                }
                if (data.equals("5") && b5)
                {
                    makeToastTextAndShow("您已領取過", 3500);
                    return;
                }
                if (data.equals("6") && b6)
                {
                    makeToastTextAndShow("您已領取過", 3500);
                    return;
                }
                if (data.equals("7") && b7)
                {
                    makeToastTextAndShow("您已領取過", 3500);
                    return;
                }
                if (data.equals("") && b8)
                {
                    makeToastTextAndShow("您已領取過", 3500);
                    return;
                }
                Intent intent = new Intent(ARFindActivity.this, ARCameraActivity.class);
                //======================這個是店家的經緯度===========================
                intent.putExtra("lat", latlocation);
                intent.putExtra("lon", lonlocation);
                intent.putExtra("aid", aid);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setData(String data)
    {
        try
        {
            if (data.equals("1"))
            {
                title.setText(storeBean2.get(0).getAr_name());
                txtContent.setText(storeBean2.get(0).getArDescript2() + "\n\n\n" + storeBean2.get(0).getAr_descript());
                latlocation = storeBean2.get(0).getAr_latitude();
                lonlocation = storeBean2.get(0).getAr_longitude();
                aid = storeBean2.get(0).getAid();
                String ImgUrl = ApiConstant.API_IMAGE + storeBean2.get(0).getAr_picture();
                PicassoTrustAll.getInstance(this).load(ImgUrl).into(storeImg);
            }
            if (data.equals("2"))
            {
                title.setText(storeBean2.get(1).getAr_name());
                txtContent.setText(storeBean2.get(1).getArDescript2() + "\n\n\n" + storeBean2.get(1).getAr_descript());
                latlocation = storeBean2.get(1).getAr_latitude();
                lonlocation = storeBean2.get(1).getAr_longitude();
                aid = storeBean2.get(1).getAid();
                String ImgUrl = ApiConstant.API_IMAGE + storeBean2.get(1).getAr_picture();
                PicassoTrustAll.getInstance(this).load(ImgUrl).into(storeImg);
                txtAddress.setText("地址：北埔鄉公園街16巷內");
            }
            if (data.equals("3"))
            {
                title.setText(storeBean2.get(2).getAr_name());
                txtContent.setText(storeBean2.get(2).getArDescript2() + "\n\n\n" + storeBean2.get(2).getAr_descript());
                latlocation = storeBean2.get(2).getAr_latitude();
                lonlocation = storeBean2.get(2).getAr_longitude();
                aid = storeBean2.get(2).getAid();
                String ImgUrl = ApiConstant.API_IMAGE + storeBean2.get(2).getAr_picture();
                PicassoTrustAll.getInstance(this).load(ImgUrl).into(storeImg);
                txtAddress.setText("地址：北埔鄉公園街16巷內");
            }
            if (data.equals("4"))
            {
                title.setText(storeBean2.get(3).getAr_name());
                txtContent.setText(storeBean2.get(3).getArDescript2() + "\n\n\n" + storeBean2.get(3).getAr_descript());
                latlocation = storeBean2.get(3).getAr_latitude();
                lonlocation = storeBean2.get(3).getAr_longitude();
                aid = storeBean2.get(3).getAid();
                String ImgUrl = ApiConstant.API_IMAGE + storeBean2.get(3).getAr_picture();
                PicassoTrustAll.getInstance(this).load(ImgUrl).into(storeImg);
                txtAddress.setText("地址：北埔鄉公園街16巷內");
            }
            if (data.equals("5"))
            {
                title.setText(storeBean2.get(4).getAr_name());
                txtContent.setText(storeBean2.get(4).getArDescript2() + "\n\n\n" + storeBean2.get(4).getAr_descript());
                latlocation = storeBean2.get(4).getAr_latitude();
                lonlocation = storeBean2.get(4).getAr_longitude();
                aid = storeBean2.get(4).getAid();
                String ImgUrl = ApiConstant.API_IMAGE + storeBean2.get(4).getAr_picture();
                PicassoTrustAll.getInstance(this).load(ImgUrl).into(storeImg);
            }
            if (data.equals("6"))
            {
                title.setText(storeBean2.get(5).getAr_name());
                txtContent.setText(storeBean2.get(5).getArDescript2() + "\n\n\n" + storeBean2.get(5).getAr_descript());
                latlocation = storeBean2.get(5).getAr_latitude();
                lonlocation = storeBean2.get(5).getAr_longitude();
                aid = storeBean2.get(5).getAid();
                String ImgUrl = ApiConstant.API_IMAGE + storeBean2.get(5).getAr_picture();
                PicassoTrustAll.getInstance(this).load(ImgUrl).into(storeImg);
            }
            if (data.equals("7"))
            {
                title.setText(storeBean2.get(6).getAr_name());
                txtContent.setText(storeBean2.get(6).getArDescript2() + "\n\n\n" + storeBean2.get(6).getAr_descript());
                latlocation = storeBean2.get(6).getAr_latitude();
                lonlocation = storeBean2.get(6).getAr_longitude();
                aid = storeBean2.get(6).getAid();
                String ImgUrl = ApiConstant.API_IMAGE + storeBean2.get(6).getAr_picture();
                PicassoTrustAll.getInstance(this).load(ImgUrl).into(storeImg);
            }
            if (data.equals("8"))
            {
                title.setText(storeBean2.get(7).getAr_name());
                txtContent.setText(storeBean2.get(7).getArDescript2() + "\n\n\n" + storeBean2.get(7).getAr_descript());
                latlocation = storeBean2.get(7).getAr_latitude();
                lonlocation = storeBean2.get(7).getAr_longitude();
                aid = storeBean2.get(7).getAid();
                String ImgUrl = ApiConstant.API_IMAGE + storeBean2.get(7).getAr_picture();
                PicassoTrustAll.getInstance(this).load(ImgUrl).into(storeImg);
            }
        } catch (ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
    }
}
