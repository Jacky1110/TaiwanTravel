package com.jotangi.nickyen.argame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.model.MyARCoupon;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GoldenTriangleActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout btnBack;
    //    private ImageView btnImg1, btnImg2, btnImg3, btnImg4, btnImg5, btnImg6;
    private ImageButton B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, B11, B12;
    private ImageView ivMenuFalse, ivMenuTrue;
    private ArrayList<StoreBean> storeList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private boolean b, b2, b3, b4, b5, b6;
    private boolean bowl1, bowl2, bowl3, bowl4, bowl5, bowl6, bowl7, bowl8, bowl9, bowl10, bowl11, bowl12;
    private LinearLayout linearlayout;
    private TextView bt1, bt2;
    boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golden_triangle);
        Intent intent = getIntent();
        String signal = intent.getStringExtra("gain");
        if (null != signal && signal.equals("gain")) {
            getMyCoupon();
        }

        btnBack = findViewById(R.id.btnARBack);
//        btnImg1 = findViewById(R.id.btnImg1);
//        btnImg2 = findViewById(R.id.btnImg2);
//        btnImg3 = findViewById(R.id.btnImg3);
//        btnImg4 = findViewById(R.id.btnImg4);
//        btnImg5 = findViewById(R.id.btnImg5);
//        btnImg6 = findViewById(R.id.btnImg6);
        /*延續以前Rd logic*/
        B1 = findViewById(R.id.B1);
        B2 = findViewById(R.id.B2);
        B3 = findViewById(R.id.B3);
        B4 = findViewById(R.id.B4);
        B5 = findViewById(R.id.B5);
        B6 = findViewById(R.id.B6);
        B7 = findViewById(R.id.B7);
        B8 = findViewById(R.id.B8);
        B9 = findViewById(R.id.B9);
        B10 = findViewById(R.id.B10);
        B11 = findViewById(R.id.B11);
        B12 = findViewById(R.id.B12);

        ivMenuFalse = findViewById(R.id.tvMenuFalse);
        ivMenuTrue = findViewById(R.id.tvMenuTrue);
        linearlayout = findViewById(R.id.menuLayout);
        bt1 = findViewById(R.id.tvMenuActivity);
        bt2 = findViewById(R.id.tvMenuCoupon);

        btnBack.setOnClickListener(this);
//        btnImg1.setOnClickListener(this);
//        btnImg2.setOnClickListener(this);
//        btnImg3.setOnClickListener(this);
//        btnImg4.setOnClickListener(this);
//        btnImg5.setOnClickListener(this);
//        btnImg6.setOnClickListener(this);

        B1.setOnClickListener(this);
        B2.setOnClickListener(this);
        B3.setOnClickListener(this);
        B4.setOnClickListener(this);
        B5.setOnClickListener(this);
        B6.setOnClickListener(this);
        B7.setOnClickListener(this);
        B8.setOnClickListener(this);
        B9.setOnClickListener(this);
        B10.setOnClickListener(this);
        B11.setOnClickListener(this);
        B12.setOnClickListener(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);

        ivMenuFalse.setOnClickListener(this);
        ivMenuTrue.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("triangle", MODE_PRIVATE);
//        b = sharedPreferences.getBoolean("isStatus1", false);
//        b2 = sharedPreferences.getBoolean("isStatus2", false);
//        b3 = sharedPreferences.getBoolean("isStatus3", false);
//        b4 = sharedPreferences.getBoolean("isStatus4", false);
//        b5 = sharedPreferences.getBoolean("isStatus5", false);
//        b6 = sharedPreferences.getBoolean("isStatus6", false);
        /*延續原本rd邏輯*/
        bowl1 = sharedPreferences.getBoolean("isStatus1", false);
        bowl2 = sharedPreferences.getBoolean("isStatus2", false);
        bowl3 = sharedPreferences.getBoolean("isStatus3", false);
        bowl4 = sharedPreferences.getBoolean("isStatus4", false);
        bowl5 = sharedPreferences.getBoolean("isStatus5", false);
        bowl6 = sharedPreferences.getBoolean("isStatus6", false);
        bowl7 = sharedPreferences.getBoolean("isStatus7", false);
        bowl8 = sharedPreferences.getBoolean("isStatus8", false);
        bowl9 = sharedPreferences.getBoolean("isStatus9", false);
        bowl10 = sharedPreferences.getBoolean("isStatus10", false);
        bowl11 = sharedPreferences.getBoolean("isStatus11", false);
        bowl12 = sharedPreferences.getBoolean("isStatus12", false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMenuFalse:
                isShow = true;
                ivMenuTrue.setVisibility(View.VISIBLE);
                ivMenuFalse.setVisibility(View.GONE);
                isShowLayout();
                break;
            case R.id.tvMenuTrue:
                closeLayout();
                break;
            /*aid range 38 to 49*/
            case R.id.btnARBack:
                finish();
                break;
            case R.id.B1://雲滄小館
                loadInfo("38");
                break;
            case R.id.B2://楊家將
                loadInfo("39");
                break;
            case R.id.B3://閃妹小廚
                loadInfo("41");
                break;
            case R.id.B4://忠貞雲鄉米干
                loadInfo("43");
                break;
            case R.id.B5://阿秀米干
                loadInfo("42");
                break;
            case R.id.B6://王記山東水餃
                loadInfo("47");
                break;
            case R.id.B7://冰獨
                loadInfo("44");
                break;
            case R.id.B8://阿嬌米干
                loadInfo("40");
                break;
            case R.id.B9://七彩雲南(忠貞店)
                loadInfo("45");
                break;
            case R.id.B10://阿美金三角點心店
                loadInfo("46");
                break;
            case R.id.B11://異域Mortar&Pestle
                loadInfo("48");
                break;
            case R.id.B12://嘴角沾糖的女人
                loadInfo("49");
                break;
            case R.id.tvMenuActivity:
                showDialog();
                break;
            case R.id.tvMenuCoupon:
                getMyCoupon();
                break;
//            case R.id.btnImg1: //清真寺
//                loadInfo("9");
//                break;
//            case R.id.btnImg2: //國旗屋
//                loadInfo("13");
//                break;
//            case R.id.btnImg3: //忠貞新村文創園區
//                loadInfo("14");
//                break;
//            case R.id.btnImg4: //打歌場
//                loadInfo("11");
//                break;
//            case R.id.btnImg5: //目瑙縱歌
//                loadInfo("10");
//                break;
//            case R.id.btnImg6: //轉經筒
//                loadInfo("12");
//                break;
        }
    }

    private void isShowLayout() {
        if (isShow) {
            linearlayout.setVisibility(View.VISIBLE);
        } else {
            linearlayout.setVisibility(View.INVISIBLE);
        }
    }

    private void closeLayout() {
        isShow = false;
        isShowLayout();
        ivMenuFalse.setVisibility(View.VISIBLE);
        ivMenuTrue.setVisibility(View.GONE);
    }

    String  flag="";
    private void getMyCoupon() {
        ApiConnection.myCouponList2("", "", new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Log.d("getMyCoupon", "onSuccess: " + jsonString);
                Type type = new TypeToken<ArrayList<MyARCoupon>>() {
                }.getType();
                ArrayList<MyARCoupon> myARCouponArrayList;
                myARCouponArrayList = new Gson().fromJson(jsonString, type);
                if (null != myARCouponArrayList && !myARCouponArrayList.isEmpty()) {
                    myARCouponArrayList.forEach(coupon -> {
                        if ("ARCOUPON3".equals(coupon.getCoupon_id())){
                            sharedPreferences = getSharedPreferences("triangle", MODE_PRIVATE);
                            sharedPreferences.edit()
                                    .putBoolean("isGift", true)
                                    .commit();
                        }else if ("ARCOUPON4".equals(coupon.getCoupon_id())){
                            sharedPreferences = getSharedPreferences("jiao", MODE_PRIVATE);
                            sharedPreferences.edit()
                                    .putBoolean("isGift", true)
                                    .commit();

                        }else if("ARCOUPON5".equals(coupon.getCoupon_id())){
                            sharedPreferences = getSharedPreferences("sleepingTiger", MODE_PRIVATE);
                            sharedPreferences.edit()
                                    .putBoolean("isGift", true)
                                    .commit();
                        }else if("ARCOUPON6".equals(coupon.getCoupon_id())){
                            sharedPreferences = getSharedPreferences("park", MODE_PRIVATE);
                            sharedPreferences.edit()
                                    .putBoolean("isGift", true)
                                    .commit();
                        }
                        if ("ARCOUPON3".equals(coupon.getCoupon_id())) {
                            flag ="1";
                            runOnUiThread(() -> {
                                showDialog2(coupon);
                            });
                        }else if ("ARCOUPON4".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                            flag = "2";
                        }else if ("ARCOUPON5".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                           flag= "3";
                        }else if ("ARCOUPON6".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                            flag= "4";
                        }
                    });
                    if (!flag.equals("1")){
                        runOnUiThread(() ->{
                            AppUtility.showMyDialog(GoldenTriangleActivity.this, "您尚無優惠券", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
                                @Override
                                public void onCheck() {

                                }

                                @Override
                                public void onCancel() {

                                }
                            });

                        });
                    }
                }

            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> {
                    AppUtility.showMyDialog(GoldenTriangleActivity.this, "您尚無優惠券", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
                        @Override
                        public void onCheck() {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                });
            }
        });
    }

    private void loadInfo(String s) {
        ApiConnection.getARShopInfo(s, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<StoreBean>>() {
                }.getType();
                storeList = new Gson().fromJson(jsonString, type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Dialog dialog = new Dialog(GoldenTriangleActivity.this);
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

                        Picasso.with(GoldenTriangleActivity.this).load(ApiConstant.API_IMAGE + storeList.get(0).getAr_picture()).into(img);

                        Button btnFind = dialog.findViewById(R.id.btn_find);
                        btnFind.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if (storeList.get(0).getAid().equals("9") && b) //清真寺
//                                {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("10") && b2) //目腦縱歌
//                                {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("11") && b3) //打歌場
//                                {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("12") && b4) //轉經筒
//                                {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("13") && b5) //國旗屋
//                                {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                                if (storeList.get(0).getAid().equals("14") && b6) //忠貞新村文創
//                                {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
                                /*2022/03/21延續原本rd邏輯*/
//                                if (storeList.get(0).getAid().equals("38") && bowl1) //雲滄小館
//                                {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("39") && bowl2) //楊家將
//                                {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("41") && bowl3) {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("43") && bowl4) {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("42") && bowl5) {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("47") && bowl6) {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("44") && bowl7) {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("40") && bowl8) {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("45") && bowl9) {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("46") && bowl10) {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("48") && bowl11) {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("49") && bowl12) {
//                                    Toast.makeText(GoldenTriangleActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                Intent in = new Intent(GoldenTriangleActivity.this, GoldenTriangleCameraActivity.class);
                                in.putExtra("triangle", new Gson().toJson(storeList.get(0)));
                                startActivity(in);
                                finish();
                            }
                        });
                        Button btnBack = dialog.findViewById(R.id.btn_close);
                        btnBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoldenTriangleActivity.this, "連線有誤，請重新操作", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    private void showDialog() {
        runOnUiThread(() -> {
            closeLayout();
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_bowldirection);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));

            Button btnBack = dialog.findViewById(R.id.bdBack);

            btnBack.setOnClickListener(v -> {
                if (dialog != null) {

                    dialog.dismiss();
//                    Intent i = new Intent(GoldenTriangleActivity.this, GoldenTriangleActivity.class);
//                    startActivity(i);
//                    finish();
                }
            });
        });
    }

    private void showDialog2(MyARCoupon coupon) {
        closeLayout();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ar_coupon);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));

        ImageView img = dialog.findViewById(R.id.imgCoupon);
        Picasso.with(this).load(ApiConstant.API_IMAGE + coupon.getCoupon_picture()).placeholder(R.color.colorUnSelect).into(img);

        TextView txtDescription = dialog.findViewById(R.id.txtDescription);
        txtDescription.setText(coupon.getCoupon_description());
//        TextView txtUsingDate = dialog.findViewById(R.id.txtUsingDate);
//        txtUsingDate.setText(coupon.getCoupon_startdate() + "~" + coupon.getCoupon_enddate());

        ImageView btnClose = dialog.findViewById(R.id.img_close);
        btnClose.setOnClickListener(v -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        Button btnExchange = dialog.findViewById(R.id.btnExchange);
        // 已領取
        if ("1".equals(coupon.getUsing_flag())) {
            btnExchange.setText("已兌換");
            btnExchange.setBackgroundResource(R.drawable.bg_ar_radius_gray);
        }

        if ("0".equals(coupon.getUsing_flag())) {
            btnExchange.setText("兌換");
            btnExchange.setClickable(true);
            btnExchange.setOnClickListener(view ->
                    AppUtility.showMyDialog(this, "是否確認兌換\n(本券僅有一次兌換機會，確認兌換後無法取消)", getString(R.string.text_confirm), getString(R.string.text_cancel), new AppUtility.OnBtnClickListener() {
                        @Override
                        public void onCheck() {
                            ApiConnection.applyCoupon2(coupon.getCoupon_no(), new ApiConnection.OnConnectResultListener() {
                                @Override
                                public void onSuccess(String jsonString) {
                                    runOnUiThread(() -> {
                                        btnExchange.setText("已兌換");
                                        btnExchange.setClickable(false);
                                        btnExchange.setBackgroundResource(R.drawable.bg_ar_radius_gray);
                                    });
                                }

                                @Override
                                public void onFailure(String message) {
                                    runOnUiThread(() -> {
                                        AppUtility.showMyDialog(GoldenTriangleActivity.this, "網路出現問題，請重新領取", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
                                            @Override
                                            public void onCheck() {

                                            }

                                            @Override
                                            public void onCancel() {

                                            }
                                        });
                                    });
                                }
                            });
                        }

                        @Override
                        public void onCancel() {

                        }
                    }));
        }
    }
}