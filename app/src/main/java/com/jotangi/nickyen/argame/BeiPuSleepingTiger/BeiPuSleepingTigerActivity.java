package com.jotangi.nickyen.argame.BeiPuSleepingTiger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.GoldenTriangleActivity;
import com.jotangi.nickyen.argame.JiaoBanShan.JiaoBanShanActivity;
import com.jotangi.nickyen.argame.model.MyARCoupon;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.databinding.ActivityBeiPuSleepingTigerBinding;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BeiPuSleepingTigerActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityBeiPuSleepingTigerBinding binding;
    private ArrayList<StoreBean> storeList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    boolean b, b2, b3, b4, b5, b6, b7, b8, b9;

    boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBeiPuSleepingTigerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String signal = intent.getStringExtra("sleeping_gain");
        if (null != signal && signal.equals("gain")) {
            getARCoupon();
        }

        // 綁定點擊事件
        bindClick();

        // 判斷 徽章是否是亮的
        isBadge();
    }

    String flag="";
    private void getARCoupon() {
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
                        if ("ARCOUPON5".equals(coupon.getCoupon_id())) {
                            flag="1";
                            runOnUiThread(() -> {
                                showCouponDialog(coupon);
                            });
                        }else if ("ARCOUPON4".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                            flag="2";
                        }else if ("ARCOUPON3".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                            flag="3";
                        }else if ("ARCOUPON6".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                            flag="4";
                        }
                    });
                    if (!flag.equals("1")){
                        runOnUiThread(() ->{
                            AppUtility.showMyDialog(BeiPuSleepingTigerActivity.this, "您尚無優惠券", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
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
                    AppUtility.showMyDialog(BeiPuSleepingTigerActivity.this, "您尚無優惠券", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
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

    private void showCouponDialog(MyARCoupon coupon) {
        closeLayout();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ar_coupon);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));

        ConstraintLayout barLayout = dialog.findViewById(R.id.layout);
//        TextView txtName = dialog.findViewById(R.id.textView19);
//        txtName.setText("北埔魅力商圈_AR遊戲好禮");
        barLayout.setBackgroundResource(R.color.ar_bei_pu_sleeping_tiger_bg);


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
        btnExchange.setBackgroundResource(R.color.ar_bei_pu_sleeping_tiger_bg);
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
                                        AppUtility.showMyDialog(BeiPuSleepingTigerActivity.this, "網路出現問題，請重新領取", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
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

    private void bindClick() {
        binding.cLayoutPosition1.setOnClickListener(this);
        binding.cLayoutPosition2.setOnClickListener(this);
        binding.cLayoutPosition3.setOnClickListener(this);
        binding.cLayoutPosition4.setOnClickListener(this);
        binding.cLayoutPosition5.setOnClickListener(this);
        binding.cLayoutPosition6.setOnClickListener(this);
        binding.cLayoutPosition7.setOnClickListener(this);
        binding.cLayoutPosition8.setOnClickListener(this);
        binding.cLayoutPosition9.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
        binding.tvMenuActivity.setOnClickListener(this);
        binding.tvMenuCoupon.setOnClickListener(this);
        binding.tvMenuFalse.setOnClickListener(this);
        binding.tvMenuTrue.setOnClickListener(this);
    }

    private void isBadge() {
        sharedPreferences = getSharedPreferences("sleepingTiger", MODE_PRIVATE);
        b = sharedPreferences.getBoolean("isStatus1", false);
        b2 = sharedPreferences.getBoolean("isStatus2", false);
        b3 = sharedPreferences.getBoolean("isStatus3", false);
        b4 = sharedPreferences.getBoolean("isStatus4", false);
        b5 = sharedPreferences.getBoolean("isStatus5", false);
        b6 = sharedPreferences.getBoolean("isStatus6", false);
        b7 = sharedPreferences.getBoolean("isStatus7", false);
        b8 = sharedPreferences.getBoolean("isStatus8", false);
        b9 = sharedPreferences.getBoolean("isStatus9", false);

        setBadgeLayout();
    }

    private void setBadgeLayout() {

        if (b) {
            binding.imgColorPosition1.setVisibility(View.VISIBLE);
            binding.imgBlackPosition1.setVisibility(View.GONE);

        }
        if (b2) {
            binding.imgColorPosition2.setVisibility(View.VISIBLE);
            binding.imgBlackPosition2.setVisibility(View.GONE);
        }
        if (b3) {
            binding.imgColorPosition3.setVisibility(View.VISIBLE);
            binding.imgBlackPosition3.setVisibility(View.GONE);
        }
        if (b4) {
            binding.imgColorPosition4.setVisibility(View.VISIBLE);
            binding.imgBlackPosition4.setVisibility(View.GONE);
        }
        if (b5) {
            binding.imgColorPosition5.setVisibility(View.VISIBLE);
            binding.imgBlackPosition5.setVisibility(View.GONE);
        }
        if (b6) {
            binding.imgColorPosition6.setVisibility(View.VISIBLE);
            binding.imgBlackPosition6.setVisibility(View.GONE);
        }
        if (b7) {
            binding.imgColorPosition7.setVisibility(View.VISIBLE);
            binding.imgBlackPosition7.setVisibility(View.GONE);
        }
        if (b8) {
            binding.imgColorPosition8.setVisibility(View.VISIBLE);
            binding.imgBlackPosition8.setVisibility(View.GONE);
        }
        if (b9) {
            binding.imgColorPosition9.setVisibility(View.VISIBLE);
            binding.imgBlackPosition9.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvMenuFalse:
                isShow = true;
                binding.tvMenuTrue.setVisibility(View.VISIBLE);
                binding.tvMenuFalse.setVisibility(View.GONE);
                isShowLayout();
                break;
            case R.id.tvMenuTrue:
                closeLayout();
                break;
            case R.id.tvMenuActivity:
                showActivityDialog();
                break;
            case R.id.tvMenuCoupon:
                getARCoupon();
                break;
            case R.id.cLayoutPosition1:
                loadInfo("58"); // 柿餅 aid 58
                break;
            case R.id.cLayoutPosition2:
                loadInfo("59"); // 膨風茶 aid 59
                break;
            case R.id.cLayoutPosition3:
                loadInfo("60"); // 擂茶 aid 60
                break;
            case R.id.cLayoutPosition4:
                loadInfo("61"); // 客家美食 aid 61
                break;
            case R.id.cLayoutPosition5:
                loadInfo("62"); // 北埔睡虎 aid 62
                break;
            case R.id.cLayoutPosition6:
                loadInfo("63"); // 客家糕餅 aid 63
                break;
            case R.id.cLayoutPosition7:
                loadInfo("64"); // 客家美食 aid 64
                break;
            case R.id.cLayoutPosition8:
                loadInfo("65"); // 鹹豬肉 aid 65
                break;
            case R.id.cLayoutPosition9:
                loadInfo("66"); // 胡椒鴨 aid 66
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }

    private void loadInfo(String s) {
        ApiConnection.getARShopInfo(s, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<StoreBean>>() {
                }.getType();
                storeList = new Gson().fromJson(jsonString, type);
                Intent i = new Intent(BeiPuSleepingTigerActivity.this, BeiPuSleepingTigerNextActivity.class);
                i.putExtra("sleeping", new Gson().toJson(storeList.get(0)));
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BeiPuSleepingTigerActivity.this, "連線有誤，請重新操作", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    private void showActivityDialog() {
        runOnUiThread(() -> {
            closeLayout();
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_sleeping_tiger_activity);
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

    private void isShowLayout() {
        if (isShow) {
            binding.menuLayout.setVisibility(View.VISIBLE);
        } else {
            binding.menuLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void closeLayout() {
        isShow = false;
        isShowLayout();
        binding.tvMenuFalse.setVisibility(View.VISIBLE);
        binding.tvMenuTrue.setVisibility(View.GONE);
    }

}