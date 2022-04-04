package com.jotangi.nickyen.argame.BeiPuSleepingTiger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.databinding.ActivityBeiPuSleepingTigerNextBinding;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BeiPuSleepingTigerNextActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityBeiPuSleepingTigerNextBinding binding;
    private StoreBean storeBean;
    private ArrayList<BeiPuStore> storeList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    boolean position1, position2, position3, position4, position5, position6, position7, position8, position9, isGift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBeiPuSleepingTigerNextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null) {
            String s = getIntent().getStringExtra("sleeping");
            storeBean = new Gson().fromJson(s, StoreBean.class);
            getData(storeBean);
            setUILayout(storeBean);
            binding.btnScan.setOnClickListener(this);
        }

        binding.btnBack.setOnClickListener(this);
    }

    private void getData(StoreBean storeBean) {
        ApiConnection.arStore(storeBean.getAid(), new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<BeiPuStore>>() {
                }.getType();
                storeList = new Gson().fromJson(jsonString, type);
                runOnUiThread(() -> {
                    binding.rv.setLayoutManager(new LinearLayoutManager(BeiPuSleepingTigerNextActivity.this));
                    binding.rv.setAdapter(new StoreAdapter(R.layout.item_bei_pu_sleeping_tiger, storeList));
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void setUILayout(StoreBean storeBean) {
        binding.tvContent.setText(storeBean.getAr_name() + "介紹");
        binding.tvContent.setText(storeBean.getAr_descript());
        Picasso.with(this).load(ApiConstant.API_IMAGE + storeBean.getAr_picture()).into(binding.img);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnScan:
                prevent();
                break;
            case R.id.btnBack:
                startActivity(new Intent(this,BeiPuSleepingTigerActivity.class));
                finish();
                break;
        }
    }

    // 防呆
    private void prevent() {
        sharedPreferences = getSharedPreferences("sleepingTiger", MODE_PRIVATE);
        position1 = sharedPreferences.getBoolean("isStatus1", false);
        position2 = sharedPreferences.getBoolean("isStatus2", false);
        position3 = sharedPreferences.getBoolean("isStatus3", false);
        position4 = sharedPreferences.getBoolean("isStatus4", false);
        position5 = sharedPreferences.getBoolean("isStatus5", false);
        position6 = sharedPreferences.getBoolean("isStatus6", false);
        position7 = sharedPreferences.getBoolean("isStatus8", false);
        position8 = sharedPreferences.getBoolean("isStatus7", false);
        position9 = sharedPreferences.getBoolean("isStatus9", false);
        isGift = sharedPreferences.getBoolean("isGift", false);
        if (storeBean.getAid().equals("58") && position1 && isGift) // 柿餅 aid 58
        {
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (storeBean.getAid().equals("59") && position2 && isGift) // 膨風茶 aid 59
        {
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (storeBean.getAid().equals("60") && position3 && isGift) { // 擂茶 aid 60
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (storeBean.getAid().equals("61") && position4 && isGift) { // 客家美食 aid 61
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (storeBean.getAid().equals("62") && position5 && isGift) { // 北埔睡虎 aid 62
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (storeBean.getAid().equals("63") && position6 && isGift) { // 客家糕餅 aid 63
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (storeBean.getAid().equals("65") && position7 && isGift) { // 鹹豬肉 aid 65
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (storeBean.getAid().equals("64") && position8 && isGift) { // 客家美食 aid 64
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (storeBean.getAid().equals("66") && position9 && isGift) { // 胡椒鴨 aid 6
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this, BeiPuSleepingCameraActivity.class);
        i.putExtra("sleeping", new Gson().toJson(storeBean));
        startActivity(i);
        finish();
    }

    private class StoreAdapter extends BaseQuickAdapter<BeiPuStore, BaseViewHolder> {

        TextView txtName, txtAddress;

        public StoreAdapter(int layoutResId, @Nullable List<BeiPuStore> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, BeiPuStore beiPuStore) {
            txtName = baseViewHolder.getView(R.id.tv_name);
            txtAddress = baseViewHolder.getView(R.id.tv_address);

            txtName.setText(beiPuStore.getStoreName());
            txtAddress.setText(beiPuStore.getStoreAddress());
        }
    }
}