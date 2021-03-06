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
            case R.id.btnImg1: //???????????? ???????????????SPA
                loadInfo("15");
                break;
//            case R.id.btnImg2: //??????????????? ????????????(?????????)
//                loadInfo("16");
//                break;
            case R.id.btnImg4: //???????????? NANA????????????
                loadInfo("17");
                break;
            case R.id.btnImg5: //???????????? Vivian????????????????????????
                loadInfo("18");
                break;
            case R.id.btnImg6: //??????????????? ?????????_?????????
                loadInfo("19");
                break;
            case R.id.btnImg7: //??????????????? ?????????_????????? ???????????????
                loadInfo("32");
                break;
            case R.id.btnImg8: //??????????????? ?????????_????????? ????????????
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
                                s="??????";
                                break;
//                            case "16":
//                                s="?????????";
//                                break;
                            case "17":
                                s="??????";
                                break;
                            case "18":
                                s="??????";
                                break;
                            case "19":
                                s="?????????";
                                break;
                            case "31":
                                s="?????????";
                                break;
                            case "32":
                                s="?????????";
                                break;
                        }
                        txtTitle.setText(s+"??????  ??????\n"+storeList.get(0).getAr_name());
                        txtContent.setText(storeList.get(0).getArDescript2() + "\n\n\n" + storeList.get(0).getAr_descript());
                        txtAddress.setText("?????????"+storeList.get(0).getAr_address());

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
                        Toast.makeText(SalonsARActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}