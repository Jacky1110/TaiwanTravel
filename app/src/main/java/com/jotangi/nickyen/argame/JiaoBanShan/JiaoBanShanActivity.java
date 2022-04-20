package com.jotangi.nickyen.argame.JiaoBanShan;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.GoldenTriangleActivity;
import com.jotangi.nickyen.argame.model.MyARCoupon;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JiaoBanShanActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout btnBack;
    private ImageButton B1, B2, B3, B4, B5, B6, B7, B8;
    private ImageView ivMenuFalse, ivMenuTrue;
    private ArrayList<StoreBean> storeList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private boolean position1, position2, position3, position4, position5, position6, position7, position8, isGift;
    private TextView bt1, bt2, bt3;
    LinearLayout linearlayout;
    boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiao_ban_shan);

        Intent intent = getIntent();
        String signal = intent.getStringExtra("gain");
        if (null != signal && signal.equals("gain")) {
            getMyCoupon();
        }

        btnBack = findViewById(R.id.btnARBack);

        B1 = findViewById(R.id.B1);
        B2 = findViewById(R.id.B2);
        B3 = findViewById(R.id.B3);
        B4 = findViewById(R.id.B4);
        B5 = findViewById(R.id.B5);
        B6 = findViewById(R.id.B6);
        B7 = findViewById(R.id.B7);
        B8 = findViewById(R.id.B8);

        ivMenuFalse = findViewById(R.id.tvMenuFalse);
        ivMenuTrue = findViewById(R.id.tvMenuTrue);
        linearlayout = findViewById(R.id.menuLayout);
        bt1 = findViewById(R.id.tvMenuActivity);
        bt2 = findViewById(R.id.tvMenuCoupon);
        bt3 = findViewById(R.id.bowlT3);

        btnBack.setOnClickListener(this);

        B1.setOnClickListener(this);
        B2.setOnClickListener(this);
        B3.setOnClickListener(this);
        B4.setOnClickListener(this);
        B5.setOnClickListener(this);
        B6.setOnClickListener(this);
        B7.setOnClickListener(this);
        B8.setOnClickListener(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);

        ivMenuFalse.setOnClickListener(this);
        ivMenuTrue.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("jiao", MODE_PRIVATE);

        position1 = sharedPreferences.getBoolean("isStatus1", false);
        position2 = sharedPreferences.getBoolean("isStatus2", false);
        position3 = sharedPreferences.getBoolean("isStatus3", false);
        position4 = sharedPreferences.getBoolean("isStatus4", false);
        position5 = sharedPreferences.getBoolean("isStatus5", false);
        position6 = sharedPreferences.getBoolean("isStatus6", false);
        position7 = sharedPreferences.getBoolean("isStatus7", false);
        position8 = sharedPreferences.getBoolean("isStatus8", false);
        isGift = sharedPreferences.getBoolean("isGift", false);
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
            case R.id.btnARBack:
                finish();
                break;
            case R.id.B1: // 復興區歷史文化館 aid 50
                loadInfo("50");
                break;
            case R.id.B2: // 復興區介壽國小 aid 51
                loadInfo("51");
                break;
            case R.id.B3: //角板山行館 aid 52
                loadInfo("52");
                break;
            case R.id.B4: // 角板山公園 aid 53
                loadInfo("53");
                break;
            case R.id.B5: // 角板山時光隧道 aid 54
                loadInfo("54");
                break;
            case R.id.B6: // 復興青年活動中心 aid 55
                loadInfo("55");
                break;
            case R.id.B7: // 角板山天幕廣場 aid 56
                loadInfo("56");
                break;
            case R.id.B8: // 福興宮 57
                loadInfo("57");
                break;
            case R.id.tvMenuActivity:
                showDialog();
                break;
            case R.id.tvMenuCoupon:
                getMyCoupon();
                break;
            case R.id.bowlT3:
                getDialog3();
                break;
        }
    }

    private void isShowLayout() {
        if (isShow) {
            linearlayout.setVisibility(View.VISIBLE);
        } else {
            linearlayout.setVisibility(View.INVISIBLE);
        }
    }

    String flag="";
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
                        }
                        if ("ARCOUPON4".equals(coupon.getCoupon_id())) {
                            flag="1";
                            runOnUiThread(() -> {
                                showDialog2(coupon);
                            });
                        }else if ("ARCOUPON3".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                            flag="2";
                        }else if ("ARCOUPON5".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                            flag="3";
                        }
                    });
                    if (!flag.equals("1")){
                        runOnUiThread(() ->{
                            AppUtility.showMyDialog(JiaoBanShanActivity.this, "您尚無優惠券", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
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
                    AppUtility.showMyDialog(JiaoBanShanActivity.this, "您尚無優惠券", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
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
                        Dialog dialog = new Dialog(JiaoBanShanActivity.this);
                        dialog.setContentView(R.layout.dialog_ar_jiao_ban_shan);
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

                        Picasso.with(JiaoBanShanActivity.this).load(ApiConstant.API_IMAGE + storeList.get(0).getAr_picture()).into(img);

                        Button btnFind = dialog.findViewById(R.id.btn_find);
                        btnFind.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (storeList.get(0).getAid().equals("50") && position1 && isGift) // 復興區歷史文化館 50
                                {
                                    Toast.makeText(JiaoBanShanActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (storeList.get(0).getAid().equals("51") && position2 && isGift) // 復興區介壽國小 51
                                {
                                    Toast.makeText(JiaoBanShanActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (storeList.get(0).getAid().equals("52") && position3 && isGift) { // 角板山行館 52
                                    Toast.makeText(JiaoBanShanActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (storeList.get(0).getAid().equals("53") && position4 && isGift) { // 角板山公園 53
                                    Toast.makeText(JiaoBanShanActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (storeList.get(0).getAid().equals("54") && position5 && isGift) { // 角板山時光隧道 54
                                    Toast.makeText(JiaoBanShanActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (storeList.get(0).getAid().equals("55") && position6 && isGift) { // 復興青年活動中心 55
                                    Toast.makeText(JiaoBanShanActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (storeList.get(0).getAid().equals("56") && position7 && isGift) { // 角板山天幕廣場 56
                                    Toast.makeText(JiaoBanShanActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (storeList.get(0).getAid().equals("57") && position8 && isGift) { // 福興宮 57
                                    Toast.makeText(JiaoBanShanActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                Intent in = new Intent(JiaoBanShanActivity.this, JiaoBanShanCameraActivity.class);
                                in.putExtra("jiao", new Gson().toJson(storeList.get(0)));
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
                        Toast.makeText(JiaoBanShanActivity.this, "連線有誤，請重新操作", Toast.LENGTH_SHORT).show();
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
            dialog.setContentView(R.layout.dialog_jiao_activity);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));

            Button btnBack = dialog.findViewById(R.id.bdBack);

            btnBack.setOnClickListener(v -> {
                if (dialog != null) {

                    dialog.dismiss();
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
                                        AppUtility.showMyDialog(JiaoBanShanActivity.this, "網路出現問題，請重新領取", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
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

    private void getDialog3() {
        closeLayout();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ar_quintuple_stimulus_vouchers);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));
        ImageView btnClose = dialog.findViewById(R.id.img_close);
        btnClose.setOnClickListener(v -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });
    }

    private void closeLayout() {
        isShow = false;
        isShowLayout();
        ivMenuFalse.setVisibility(View.VISIBLE);
        ivMenuTrue.setVisibility(View.GONE);
    }
}
