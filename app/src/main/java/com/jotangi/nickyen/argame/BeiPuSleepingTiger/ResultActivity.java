package com.jotangi.nickyen.argame.BeiPuSleepingTiger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.argame.JiaoBanShan.JiaoBanShanActivity;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.databinding.ActivityResultBinding;

import java.util.ArrayList;


public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;
    private StoreBean storeBean;
    String qid = "";
    SharedPreferences sharedPreferences;
    boolean position1, position2, position3, position4, position5, position6, position7, position8, position9, isGift; // isGift用來驗證api沒成功但卻計算了
    Intent mapIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapIntent = new Intent(this, BeiPuSleepingTigerActivity.class);

        sharedPreferences = getSharedPreferences("sleepingTiger", MODE_PRIVATE);

        position1 = sharedPreferences.getBoolean("isStatus1", false);
        position2 = sharedPreferences.getBoolean("isStatus2", false);
        position3 = sharedPreferences.getBoolean("isStatus3", false);
        position4 = sharedPreferences.getBoolean("isStatus4", false);
        position5 = sharedPreferences.getBoolean("isStatus5", false);
        position6 = sharedPreferences.getBoolean("isStatus6", false);
        position7 = sharedPreferences.getBoolean("isStatus7", false);
        position8 = sharedPreferences.getBoolean("isStatus8", false);
        position9 = sharedPreferences.getBoolean("isStatus9", false);
        count = sharedPreferences.getInt("count", 0);
        isGift = sharedPreferences.getBoolean("isGift", false);

        ArrayList<Boolean> booleanArrayList = new ArrayList<>();
        booleanArrayList.add(position1);
        booleanArrayList.add(position2);
        booleanArrayList.add(position3);
        booleanArrayList.add(position4);
        booleanArrayList.add(position5);
        booleanArrayList.add(position6);
        booleanArrayList.add(position7);
        booleanArrayList.add(position8);
        booleanArrayList.add(position9);

        if (getIntent() != null) {
            String s = getIntent().getStringExtra("sleeping");
            storeBean = new Gson().fromJson(s, StoreBean.class);
            qid = getIntent().getStringExtra("qid");
            algorithm(storeBean, qid);
        }
    }

    private void algorithm(StoreBean storeBean, String qid) {
        compareQRCode(storeBean, qid);
    }

    private void compareQRCode(StoreBean storeBean, String qid) {
        if (storeBean.getAid().equals("58") && qid.equals("1")) // 柿餅 aid 58
        {
            binding.imgColorPosition1.setImageResource(R.drawable.bg_color_sleeping_tiger01);
            if (!position1){
            count += 1;
            }
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus1", true)
                    .commit();
            showUI();
        } else if (storeBean.getAid().equals("59") && qid.equals("2")) // 膨風茶 aid 59
        {
            binding.imgColorPosition1.setImageResource(R.drawable.bg_color_sleeping_tiger02);
            if (!position2){
                count += 1;
            }
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus2", true)
                    .commit();
            showUI();
        } else if (storeBean.getAid().equals("60") && qid.equals("3")) { // 擂茶 aid 60
            binding.imgColorPosition1.setImageResource(R.drawable.bg_color_sleeping_tiger03);
            if (!position3){
                count += 1;
            }
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus3", true)
                    .commit();
            showUI();
        } else if (storeBean.getAid().equals("61") && qid.equals("4")) { // 客家美食 aid 61
            binding.imgColorPosition1.setImageResource(R.drawable.bg_color_sleeping_tiger04);
            if (!position4){
                count += 1;
            }
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus4", true)
                    .commit();
            showUI();
        } else if (storeBean.getAid().equals("62") && qid.equals("5")) { // 北埔睡虎 aid 62
            binding.imgColorPosition1.setImageResource(R.drawable.bg_color_sleeping_tiger05);
            if (!position5){
                count += 1;
            }
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus5", true)
                    .commit();
            showUI();
        } else if (storeBean.getAid().equals("63") && qid.equals("6")) { // 客家糕餅 aid 63
            binding.imgColorPosition1.setImageResource(R.drawable.bg_color_sleeping_tiger06);
            if (!position6){
                count += 1;
            }
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus6", true)
                    .commit();
            showUI();
        } else if (storeBean.getAid().equals("65") && qid.equals("8")) { // 鹹豬肉 aid 65
            binding.imgColorPosition1.setImageResource(R.drawable.bg_color_sleeping_tiger08);
            if (!position8){
                count += 1;
            }
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus8", true)
                    .commit();
            showUI();
        } else if (storeBean.getAid().equals("64") && qid.equals("7")) { // 客家美食 aid 66
            binding.imgColorPosition1.setImageResource(R.drawable.bg_color_sleeping_tiger07);
            if (!position7){
                count += 1;
            }
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus7", true)
                    .commit();
            showUI();
        } else if (storeBean.getAid().equals("66") && qid.equals("9")) { // 胡椒鴨 aid 66
            binding.imgColorPosition1.setImageResource(R.drawable.bg_color_sleeping_tiger09);
            if (!position9){
                count += 1;
            }
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus9", true)
                    .commit();
            showUI();
        } else {
            binding.cLayoutPosition1.setVisibility(View.GONE);
            binding.tvContent.setText("找錯了！\n\n哎呀，\n找錯對象了，\n可能要換別隻哦！");
            binding.tvBtn.setText("返回");
            binding.btnLayout.setOnClickListener(view -> {
                startActivity(mapIntent);
                finish();
            });
        }

    }

    int count;

    private void showUI() {
        if (count > 2 && !isGift) {
            getCoupon();
            binding.tvContent.setText("找到了！\n恭喜您獲得禮品兌換券\n\n(請至活動頁面右上角\n\"優惠券\"查看內容)");
            binding.tvBtn.setText("查看優惠券");
            binding.btnLayout.setOnClickListener(view -> {
                mapIntent.putExtra("sleeping_gain", "gain");
                startActivity(mapIntent);
                finish();
            });
        } else if (count > 2 && isGift) {
            binding.tvContent.setText("找到了！\n\n你已領過禮品兌換券\n(請至活動頁面右上角\n\"優惠券\"查看內容)\n(每個帳號限領一次)");
            binding.tvBtn.setText("返回");
            binding.btnLayout.setOnClickListener(view -> {
                startActivity(mapIntent);
                finish();
            });
        } else {
            binding.tvContent.setText("找到了！\n\n找到任意3隻隱藏小睡虎，\n就可以獲得1份禮品兌換券哦！\n(每個帳號限領一次)");
            binding.tvBtn.setText("返回");
            binding.btnLayout.setOnClickListener(view -> {
                startActivity(mapIntent);
                finish();
            });
        }
    }

    private void getCoupon() {
        ApiConnection.getCoupon2("ARCOUPON5", new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                sharedPreferences.edit()
                        .putBoolean("isGift", true)
                        .commit();
            }

            @Override
            public void onFailure(String message) {
            }
        });
        ;
    }
}