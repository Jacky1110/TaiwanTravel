package com.jotangi.nickyen.argame.LoyaltyCulturalPark;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.databinding.ActivityArCameraBinding;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ParkCameraActivity extends AppCompatActivity {

    ActivityArCameraBinding binding;

    private BarcodeDetector barcodeDetector;
    private CameraSource ARCamera;

    StoreBean storeBean;
    private ArrayList<StoreBean> storeList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    boolean b, b2, b3, b4, b5, b6, b7, b8, b9, isGift;

    int count;

    Double mLon;
    Double mLat;

    String lat;
    String lon;
    String aid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String s = getIntent().getStringExtra("park");
        storeBean = new Gson().fromJson(s, StoreBean.class);
        Log.d("豪豪", "onCreate: " + storeBean);

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
        count = sharedPreferences.getInt("count", 0);
        isGift = sharedPreferences.getBoolean("isGift", false);

        if (null != Double.valueOf(MainActivity.myLocation.getLatitude()) || null != Double.valueOf(MainActivity.myLocation.getLongitude())) {
            mLon = MainActivity.myLocation.getLongitude();
            mLat = MainActivity.myLocation.getLatitude();
        }

        if (null != storeBean.getAr_latitude() || null != storeBean.getAr_longitude()) {
            lat = storeBean.getAr_latitude();
            lon = storeBean.getAr_longitude();
        }
        if (storeBean.getAid() != null) {
            aid = storeBean.getAid();
        }

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        //先抓儲存資料的做初始狀態
        ArrayList<Boolean> booleanArrayList = new ArrayList<>();

        booleanArrayList.add(b);
        booleanArrayList.add(b2);
        booleanArrayList.add(b3);
        booleanArrayList.add(b4);
        booleanArrayList.add(b5);
        booleanArrayList.add(b6);
        booleanArrayList.add(b7);
        booleanArrayList.add(b8);
        booleanArrayList.add(b9);

        ARCamera = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true).setRequestedPreviewSize(720, 480).build();

        //加入事件到surfaceView
        binding.ARSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    //檢查授權
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    //如果授權許可，啟動相機
                    ARCamera.start(binding.ARSurfaceView.getHolder());

                } catch (Exception e) {
                    Toast.makeText(ParkCameraActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                ARCamera.stop();
            }
        });

        if (mLat!=null && mLon!=null && lat!=null && lon!=null) {
            ApiConnection.getDistance(String.valueOf(mLat), String.valueOf(mLon), lat, lon, new ApiConnection.OnConnectResultListener() {
                @Override
                public void onSuccess(String jsonString) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.btnARFind.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    int ans = Integer.parseInt(jsonString);
//                                    Toast.makeText(ParkCameraActivity.this,String.valueOf(ans)+"\nmy-> "+String.valueOf(mLon)+"\n"+String.valueOf(mLat)+"\nnoMy->"+lon+"\n"+lat,Toast.LENGTH_LONG).show();
                                    Dialog dialog = new Dialog(ParkCameraActivity.this);
                                    if (aid.equals("70") && ans <= 20) {
                                        loadInfo(aid);
                                    } else if (aid.equals("71") && ans <= 20) {
                                        loadInfo(aid);
                                    } else if (aid.equals("72") && ans <= 20) {
                                        loadInfo(aid);
                                    } else if (aid.equals("73") && ans <= 20) {
                                        loadInfo(aid);
                                    }else if (aid.equals("75") && ans <= 40) {
                                        loadInfo(aid);
                                    } else if ((aid.equals("67") || aid.equals("68") || aid.equals("69") || aid.equals("74")) && ans <= 50) {
                                        loadInfo(aid);
                                    } else {
                                        dialog.setContentView(R.layout.dialog_ar_collection);
                                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                        dialog.setCanceledOnTouchOutside(false);
                                        dialog.show();
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                        TextView txtTitle = dialog.findViewById(R.id.tv_title);
                                        TextView txtContent = dialog.findViewById(R.id.tv_content);
                                        Button btnBack = dialog.findViewById(R.id.btn_close);
                                        txtTitle.setText("再接再厲");
                                        txtContent.setText("哎呀！\n不對喔～在找找看\n或再靠近一點試看看！");
                                        btnBack.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (dialog != null) {
                                                    dialog.dismiss();
                                                    Intent i = new Intent(ParkCameraActivity.this, LoyaltyCulturalParkActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(ParkCameraActivity.this, "網路連線有誤，請檢查連線", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } else {
            binding.btnARFind.setOnClickListener(v ->
            {
                Toast.makeText(this, "請划掉點點app並操作重新啟動", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void loadInfo(String s) {
        ApiConnection.getARShopInfo(s, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<StoreBean>>() {
                }.getType();
                storeList = new Gson().fromJson(jsonString, type);
                runOnUiThread(() -> {
                    Dialog dialogCollect = new Dialog(ParkCameraActivity.this);
                    dialogCollect.setContentView(R.layout.dialog_gold_recipe);
                    dialogCollect.setCanceledOnTouchOutside(false);
                    dialogCollect.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    dialogCollect.show();
                    dialogCollect.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                    TextView txtTitle = dialogCollect.findViewById(R.id.gdT);
                    TextView txtContent = dialogCollect.findViewById(R.id.gdTC);
                    ImageView img = dialogCollect.findViewById(R.id.gdI);
                    txtTitle.setText("恭喜您找到「" + storeList.get(0).getAr_name() + "」");
                    txtContent.setText(storeList.get(0).getAr_descript());
                    Picasso.with(ParkCameraActivity.this).load(ApiConstant.API_IMAGE + storeList.get(0).getAr_picture()).into(img);

                    Button btnFind = dialogCollect.findViewById(R.id.gdC);
                    btnFind.setOnClickListener(v -> {
                        if (dialogCollect != null) {
                            countNumber();
                            dialogCollect.dismiss();
                        }
                    });

                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ParkCameraActivity.this, "連線有誤，請重新操作", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    private void countNumber() {

        if (storeBean.getAid().equals("67") && storeBean.getAid() != null && !b) { // 異域故事館 aid 67
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus1", true)
                    .commit();
        }
        if (storeBean.getAid().equals("68") && storeBean.getAid() != null && !b2) { // 孤軍紀念廣場 aid 68
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus2", true)
                    .commit();
        }
        if (storeBean.getAid().equals("69") && storeBean.getAid() != null && !b3) { // 彩繪牆_彩色家族 aid 69
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus3", true)
                    .commit();
        }
        if (storeBean.getAid().equals("70") && storeBean.getAid() != null && !b4) { // 黑山銀花 aid 70
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus4", true)
                    .commit();
        }
        if (storeBean.getAid().equals("71") && storeBean.getAid() != null && !b5) { // 冰獨 aid 71
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus5", true)
                    .commit();
        }
        if (storeBean.getAid().equals("72") && storeBean.getAid() != null && !b6) {  // 癮食聖堂 aid 72
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus6", true)
                    .commit();
        }
        if (storeBean.getAid().equals("73") && storeBean.getAid() != null && !b7) { // 阿美1981 aid 73
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus7", true)
                    .commit();
        }
        if (storeBean.getAid().equals("74") && storeBean.getAid() != null && !b8) { // 忠貞新村文化教室 aid 74
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus8", true)
                    .commit();
        }
        if (storeBean.getAid().equals("75") && storeBean.getAid() != null && !b9) { // 嘟嘟車 aid 75
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus9", true)
                    .commit();
        }
        showDialog();
    }

    private void showDialog() {
        if (count > 4 && !isGift) {
            getCoupon();
            runOnUiThread(() -> {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_ar_success);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                TextView txtContent = dialog.findViewById(R.id.tv_content);
                txtContent.setText("恭喜您！\n已經找到5把鑰匙\n並獲得1份禮品兌換券\n(請至活動地圖右上角\n\"優惠券\"頁面查看內容)");
                Button btnBack = dialog.findViewById(R.id.btn_close);
                btnBack.setText("查看優惠券");

                btnBack.setOnClickListener(v1 -> {
                    if (dialog != null) {
                        dialog.dismiss();
                        Intent i = new Intent(this, LoyaltyCulturalParkActivity.class);
                        i.putExtra("park", "gain");
                        startActivity(i);
                        finish();
                    }
                });
            });

        } else if (count > 4 && isGift) {
            getCoupon();
            runOnUiThread(() -> {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_ar_success);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                TextView txtContent = dialog.findViewById(R.id.tv_content);
                TextView txtTitle = dialog.findViewById(R.id.tv_title);
                txtTitle.setText("恭喜你解鎖一個祕密！");
                txtContent.setText("您已領過禮品兌換券\n(請至活動地圖右上角\n\"優惠券\"頁面查看內容)\n(每個帳號限領一次)");
                Button btnBack = dialog.findViewById(R.id.btn_close);
                btnBack.setText("確認");

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null) {
                            dialog.dismiss();
                            startActivity(new Intent(ParkCameraActivity.this, LoyaltyCulturalParkActivity.class));
                            finish();
                        }
                    }
                });
            });
        } else {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_ar_success);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
            TextView txtContent = dialog.findViewById(R.id.tv_content);
            TextView txtTitle = dialog.findViewById(R.id.tv_title);
            txtTitle.setText("恭喜你解鎖一個祕密！");
            txtContent.setText("找到任意5把鑰匙\n就可獲得1份禮品兌換券\n(每個帳號限領一份)");
            Button btnBack = dialog.findViewById(R.id.btn_close);
            btnBack.setText("確認");

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.dismiss();
                        startActivity(new Intent(ParkCameraActivity.this, LoyaltyCulturalParkActivity.class));
                        finish();
                    }
                }
            });
        }
    }

    private void getCoupon() {
        ApiConnection.getCoupon2("ARCOUPON6", new ApiConnection.OnConnectResultListener() {
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