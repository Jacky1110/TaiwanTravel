package com.jotangi.nickyen.argame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SalonsARActivity extends AppCompatActivity implements View.OnClickListener
{
    private FrameLayout btnBack;
    private ImageView btnImg1, btnImg2, btnImg4, btnImg5, btnImg6, btnImg7, btnImg8;

    private ArrayList<StoreBean> storeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salons_ar);

        btnBack = findViewById(R.id.btnARBack);
        btnImg1 = findViewById(R.id.btnImg1);
//        btnImg2 = findViewById(R.id.btnImg2);
        btnImg4 = findViewById(R.id.btnImg4);
        btnImg5 = findViewById(R.id.btnImg5);
        btnImg6 = findViewById(R.id.btnImg6);
        btnImg7 = findViewById(R.id.btnImg7);
        btnImg8 = findViewById(R.id.btnImg8);

        btnBack.setOnClickListener(this);
        btnImg1.setOnClickListener(this);
//        btnImg2.setOnClickListener(this);
        btnImg4.setOnClickListener(this);
        btnImg5.setOnClickListener(this);
        btnImg6.setOnClickListener(this);
        btnImg7.setOnClickListener(this);
        btnImg8.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnARBack:
                finish();
                break;
            case R.id.btnImg1: //伊登水晶 新女人美容SPA
                loadInfo("15");
                break;
//            case R.id.btnImg2: //維斯塔水晶 七彩雲南(桃園店)
//                loadInfo("16");
//                break;
            case R.id.btnImg4: //洛基水晶 NANA美學沙龍
                loadInfo("17");
                break;
            case R.id.btnImg5: //希拉水晶 Vivian造型國際美髮沙龍
                loadInfo("18");
                break;
            case R.id.btnImg6: //維納斯水晶 波菲爾_復興店
                loadInfo("19");
                break;
            case R.id.btnImg7: //阿克索水晶 波菲爾_中正店 星王美容院
                loadInfo("32");
                break;
            case R.id.btnImg8: //雅典娜水晶 波菲爾_幸福店 沐潔髮藝
                loadInfo("31");
                break;
        }
    }

    private void loadInfo(String s)
    {
        ApiConnection.getARShopInfo(s, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<StoreBean>>()
                {
                }.getType();
                storeList = new Gson().fromJson(jsonString, type);
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Dialog dialog = new Dialog(SalonsARActivity.this);
                        dialog.setContentView(R.layout.dialog_ar_triangle);
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                        TextView txtTitle = dialog.findViewById(R.id.tv_title);
                        TextView txtContent = dialog.findViewById(R.id.tv_content);
                        TextView txtAddress = dialog.findViewById(R.id.tv_address);
                        RoundedImageView img = dialog.findViewById(R.id.riv);
                        String s ="";
                        switch (storeList.get(0).getAid()){
                            case "15":
                                s="伊登";
                                break;
//                            case "16":
//                                s="維斯塔";
//                                break;
                            case "17":
                                s="洛基";
                                break;
                            case "18":
                                s="希拉";
                                break;
                            case "19":
                                s="維納斯";
                                break;
                            case "31":
                                s="雅典娜";
                                break;
                            case "32":
                                s="阿克索";
                                break;
                        }
                        txtTitle.setText(s+"水晶  地點\n"+storeList.get(0).getAr_name());
                        txtContent.setText(storeList.get(0).getArDescript2() + "\n\n\n" + storeList.get(0).getAr_descript());
                        txtAddress.setText("地址："+storeList.get(0).getAr_address());

                        Picasso.with(SalonsARActivity.this).load(ApiConstant.API_IMAGE + storeList.get(0).getAr_picture()).into(img);

                        Button btnFind = dialog.findViewById(R.id.btn_find);
                        btnFind.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (dialog != null)
                                {
                                    dialog.dismiss();
                                }
                                Intent in = new Intent(SalonsARActivity.this, SalonsCameraActivity.class);
                                in.putExtra("salons", new Gson().toJson(storeList.get(0)));
                                startActivity(in);
                                finish();
                            }
                        });
                        Button btnBack = dialog.findViewById(R.id.btn_close);
                        btnBack.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (dialog != null)
                                {
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(SalonsARActivity.this, "連線有誤，請重新操作", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}