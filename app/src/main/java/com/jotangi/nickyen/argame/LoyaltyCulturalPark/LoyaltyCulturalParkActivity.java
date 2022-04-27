package com.jotangi.nickyen.argame.LoyaltyCulturalPark;

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
import com.jotangi.nickyen.argame.model.MyARCoupon;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.databinding.ActivityLoyaltyCulturalParkBinding;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoyaltyCulturalParkActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoyaltyCulturalParkBinding binding;
    private ArrayList<StoreBean> storeList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    boolean b, b2, b3, b4, b5, b6, b7, b8, b9, isGift;
    boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoyaltyCulturalParkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String signal = intent.getStringExtra("park");
        if ("gain".equals(signal)) {
            getARCoupon();
        }
        // 綁定點擊事件
        bindClick();
        // 判斷 徽章是否是亮的
        isBadge();
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
                loadInfo("67"); // 異域故事館 aid 67
                break;
            case R.id.cLayoutPosition2:
                loadInfo("68"); // 孤軍紀念廣場 aid 68
                break;
            case R.id.cLayoutPosition3:
                loadInfo("69"); // 彩繪牆_彩色家族 aid 69
                break;
            case R.id.cLayoutPosition4:
                loadInfo("70"); // 黑山銀花 aid 70
                break;
            case R.id.cLayoutPosition5:
                loadInfo("71"); // 冰獨 aid 71
                break;
            case R.id.cLayoutPosition6:
                loadInfo("72"); // 癮食聖堂 aid 72
                break;
            case R.id.cLayoutPosition7:
                loadInfo("73"); // 阿美1981 aid 73
                break;
            case R.id.cLayoutPosition8:
                loadInfo("74"); // 忠貞新村文化教室 aid 74
                break;
            case R.id.cLayoutPosition9:
                loadInfo("75"); // 嘟嘟車 aid 75
                break;
            case R.id.btnARBack:
                finish();
                break;

        }
    }

    private void isShowLayout() {
        if (isShow) {
            binding.menuLayout.setVisibility(View.VISIBLE);
        } else {
            binding.menuLayout.setVisibility(View.INVISIBLE);
        }
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
                        if ("ARCOUPON6".equals(coupon.getCoupon_id())) {
                            flag="1";
                            runOnUiThread(() -> {
                                showCouponDialog(coupon);
                            });
                        }else if ("ARCOUPON4".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                            flag="2";
                        }else if ("ARCOUPON3".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                            flag="3";
                        }else if ("ARCOUPON5".equals(coupon.getCoupon_id())&& !flag.equals("1")){
                            flag="4";
                        }
                    });
                    if (!flag.equals("1")){
                        runOnUiThread(() ->{
                            AppUtility.showMyDialog(LoyaltyCulturalParkActivity.this, "您尚無優惠券", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
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
                    AppUtility.showMyDialog(LoyaltyCulturalParkActivity.this, "您尚無優惠券", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
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
                        Dialog dialog = new Dialog(LoyaltyCulturalParkActivity.this);
                        dialog.setContentView(R.layout.dialog_ar_triangle);
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                        TextView txtTitle = dialog.findViewById(R.id.tv_title);
                        TextView txtContent = dialog.findViewById(R.id.tv_content);
                        TextView txtAddress = dialog.findViewById(R.id.tv_address);
                        RoundedImageView img = dialog.findViewById(R.id.riv);
                        txtTitle.setText("");
                        txtContent.setText(storeList.get(0).getArDescript2());
                        txtAddress.setTextColor(getResources().getColor(R.color.typeRed));
                        txtAddress.setText("按「找到了」，開啟系統相機對準跟線索圖片相符的實物");

                        Picasso.with(LoyaltyCulturalParkActivity.this).load(ApiConstant.API_IMAGE + storeList.get(0).getAr_picture2()).into(img);

                        Button btnFind = dialog.findViewById(R.id.btn_find);
                        btnFind.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

//                                if (storeList.get(0).getAid().equals("67") && b && isGift) // 異域故事館 aid 67
//                                {
//                                    Toast.makeText(LoyaltyCulturalParkActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("68") && b2 && isGift) // 孤軍紀念廣場 aid 68
//                                {
//                                    Toast.makeText(LoyaltyCulturalParkActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("69") && b3 && isGift) { // 彩繪牆_彩色家族 aid 69
//                                    Toast.makeText(LoyaltyCulturalParkActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("70") && b4 && isGift) { // 黑山銀花 aid 70
//                                    Toast.makeText(LoyaltyCulturalParkActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("71") && b5 && isGift) { // 冰獨 aid 71
//                                    Toast.makeText(LoyaltyCulturalParkActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("72") && b6 && isGift) { // 癮食聖堂 aid 72
//                                    Toast.makeText(LoyaltyCulturalParkActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("73") && b7 && isGift) { // 阿美1981 aid 73
//                                    Toast.makeText(LoyaltyCulturalParkActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("74") && b8 && isGift) { // 忠貞新村文化教室 aid 74
//                                    Toast.makeText(LoyaltyCulturalParkActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (storeList.get(0).getAid().equals("75") && b9 && isGift) { // 嘟嘟車 aid 75
//                                    Toast.makeText(LoyaltyCulturalParkActivity.this, "您已領取過", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                Intent in = new Intent(LoyaltyCulturalParkActivity.this, ParkCameraActivity.class);
                                in.putExtra("park", new Gson().toJson(storeList.get(0)));
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
                        Toast.makeText(LoyaltyCulturalParkActivity.this, "連線有誤，請重新操作", Toast.LENGTH_SHORT).show();
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
            dialog.setContentView(R.layout.dialog_bowldirection);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFFFF")));

            ImageView banner = dialog.findViewById(R.id.imageView7);
            banner.setImageResource(R.drawable.bg_loyalty_text);
            LinearLayout timeLayout = dialog.findViewById(R.id.timeLayout);
            timeLayout.setVisibility(View.GONE);
            TextView txtLocation = dialog.findViewById(R.id.txtLocation);
            txtLocation.setText("忠貞新村文化園區");
            TextView txtMode1 = dialog.findViewById(R.id.txtMode1);
            TextView txtMode2 = dialog.findViewById(R.id.txtMode2);
            TextView txtMode3 = dialog.findViewById(R.id.txtMode3);
            txtMode1.setText("1.點選「忠貞新村文化園區_解鎖忠貞故事」進入遊戲畫面");
            txtMode2.setText("2.依據忠貞新村故事地圖的提示，尋找園區內對應的故事地點，掃描地點門面，就會解鎖該地點的特色故事，並可收集到一把鑰匙。");
            txtMode3.setText("3.收集到五把鑰匙（含以上）就可以獲得一份禮品兌換券（每個帳號限領一份）。");
            Button btnBack = dialog.findViewById(R.id.bdBack);

            btnBack.setOnClickListener(v -> {
                if (dialog != null) {

                    dialog.dismiss();
                }
            });
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

//        ConstraintLayout barLayout = dialog.findViewById(R.id.layout);
//        barLayout.setBackgroundResource(R.color.ar_bei_pu_sleeping_tiger_bg);

        ImageView img = dialog.findViewById(R.id.imgCoupon);
        Picasso.with(this).load(ApiConstant.API_IMAGE + coupon.getCoupon_picture()).placeholder(R.color.colorUnSelect).into(img);

        TextView txtDescription = dialog.findViewById(R.id.txtDescription);
        txtDescription.setText(coupon.getCoupon_description());

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
                                        AppUtility.showMyDialog(LoyaltyCulturalParkActivity.this, "網路出現問題，請重新領取", getString(R.string.text_confirm), null, new AppUtility.OnBtnClickListener() {
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
        binding.btnARBack.setOnClickListener(this);
        binding.tvMenuActivity.setOnClickListener(this);
        binding.tvMenuCoupon.setOnClickListener(this);
        binding.tvMenuFalse.setOnClickListener(this);
        binding.tvMenuTrue.setOnClickListener(this);
    }

    private void isBadge() {
        sharedPreferences = getSharedPreferences("park", MODE_PRIVATE);
        b = sharedPreferences.getBoolean("isStatus1", false);
        b2 = sharedPreferences.getBoolean("isStatus2", false);
        b3 = sharedPreferences.getBoolean("isStatus3", false);
        b4 = sharedPreferences.getBoolean("isStatus4", false);
        b5 = sharedPreferences.getBoolean("isStatus5", false);
        b6 = sharedPreferences.getBoolean("isStatus6", false);
        b7 = sharedPreferences.getBoolean("isStatus7", false);
        b8 = sharedPreferences.getBoolean("isStatus8", false);
        b9 = sharedPreferences.getBoolean("isStatus9", false);
        isGift = sharedPreferences.getBoolean("isGift", false);

        setBadgeLayout();
    }

    private void setBadgeLayout() {

        if (b) {
            binding.imgUnlockPosition1.setVisibility(View.VISIBLE);
            binding.imgLockPosition1.setVisibility(View.GONE);

        }
        if (b2) {
            binding.imgUnlockPosition2.setVisibility(View.VISIBLE);
            binding.imgLockPosition2.setVisibility(View.GONE);
        }
        if (b3) {
            binding.imgUnlockPosition3.setVisibility(View.VISIBLE);
            binding.imgLockPosition3.setVisibility(View.GONE);
        }
        if (b4) {
            binding.imgUnlockPosition4.setVisibility(View.VISIBLE);
            binding.imgLockPosition4.setVisibility(View.GONE);
        }
        if (b5) {
            binding.imgUnlockPosition5.setVisibility(View.VISIBLE);
            binding.imgLockPosition5.setVisibility(View.GONE);
        }
        if (b6) {
            binding.imgUnlockPosition6.setVisibility(View.VISIBLE);
            binding.imgLockPosition6.setVisibility(View.GONE);
        }
        if (b7) {
            binding.imgUnlockPosition7.setVisibility(View.VISIBLE);
            binding.imgLockPosition7.setVisibility(View.GONE);
        }
        if (b8) {
            binding.imgUnlockPosition8.setVisibility(View.VISIBLE);
            binding.imgLockPosition8.setVisibility(View.GONE);
        }
        if (b9) {
            binding.imgUnlockPosition9.setVisibility(View.VISIBLE);
            binding.imgLockPosition9.setVisibility(View.GONE);
        }
    }

    private void closeLayout() {
        isShow = false;
        isShowLayout();
        binding.tvMenuFalse.setVisibility(View.VISIBLE);
        binding.tvMenuTrue.setVisibility(View.GONE);
    }
}