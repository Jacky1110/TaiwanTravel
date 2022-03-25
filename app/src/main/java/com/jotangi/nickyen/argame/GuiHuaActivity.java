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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.databinding.ActivityGuiHuaBinding;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GuiHuaActivity extends AppCompatActivity implements View.OnClickListener
{
    private ActivityGuiHuaBinding binding;

    private ArrayList<StoreBean> storeList = new ArrayList<>();

//    private SharedPreferences sharedPreferences;
//    private boolean b, b2, b3, b4, b6;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityGuiHuaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnARBack.setOnClickListener(this);
        binding.btnImg1.setOnClickListener(this);
        binding.btnImg2.setOnClickListener(this);
        binding.btnImg3.setOnClickListener(this);
        binding.btnImg4.setOnClickListener(this);
        binding.btnImg6.setOnClickListener(this);

//        sharedPreferences = getSharedPreferences("guiHua", MODE_PRIVATE);
//        b = sharedPreferences.getBoolean("isStatus1", false);
//        b2 = sharedPreferences.getBoolean("isStatus2", false);
//        b3 = sharedPreferences.getBoolean("isStatus3", false);
//        b4 = sharedPreferences.getBoolean("isStatus4", false);
//        b6 = sharedPreferences.getBoolean("isStatus6", false);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnARBack:
                finish();
                break;
            case R.id.btnImg1: // 情人巷
                loadInfo("37");
                break;
            case R.id.btnImg2: // 音符桂花巷
                loadInfo("35");
                break;
            case R.id.btnImg3: // 康濟吊橋
                loadInfo("36");
                break;
            case R.id.btnImg4: // 洗衫坑
                loadInfo("34");
                break;
            case R.id.btnImg6: // 南庄老郵局
                loadInfo("33");
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
                        Dialog dialog = new Dialog(GuiHuaActivity.this);
                        dialog.setContentView(R.layout.dialog_ar_triangle);
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                        TextView txtTitle = dialog.findViewById(R.id.tv_title);
                        TextView txtContent = dialog.findViewById(R.id.tv_content);
                        TextView txtAddress = dialog.findViewById(R.id.tv_address);
                        RoundedImageView img = dialog.findViewById(R.id.riv);
                        txtTitle.setText(storeList.get(0).getAr_name());
                        txtContent.setText(storeList.get(0).getAr_descript());
                        txtAddress.setText("地址：" + storeList.get(0).getAr_address());

                        Picasso.with(GuiHuaActivity.this).load(ApiConstant.API_IMAGE + storeList.get(0).getAr_picture()).into(img);

                        Button btnFind = dialog.findViewById(R.id.btn_find);
                        btnFind.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
//                                if (storeList.get(0).getAid().equals("37") && b) // 情人巷
//                                {
//                                    Toast.makeText(GuiHuaActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("35") && b2) // 音符桂花巷
//                                {
//                                    Toast.makeText(GuiHuaActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("36") && b3) // 康濟吊橋
//                                {
//                                    Toast.makeText(GuiHuaActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("34") && b4) // 洗衫坑
//                                {
//                                    Toast.makeText(GuiHuaActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("33") && b6) // 南庄老郵局
//                                {
//                                    Toast.makeText(GuiHuaActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
                                if (dialog != null)
                                {
                                    dialog.dismiss();
                                }
                                Intent in = new Intent(GuiHuaActivity.this, GuiHuaCameraActivity.class);
                                in.putExtra("guiHua", new Gson().toJson(storeList.get(0)));
                                startActivity(in);
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
                        Toast.makeText(GuiHuaActivity.this, "連線有誤，請重新操作", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}