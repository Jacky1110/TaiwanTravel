package com.jotangi.nickyen.argame.JiaoBanShan;


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
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.jotangi.nickyen.argame.GoldenTriangleActivity;
import com.jotangi.nickyen.argame.GoldenTriangleCameraActivity;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JiaoBanShanCameraActivity extends AppCompatActivity {
    private SurfaceView ARSurfaceView;
    private CameraSource ARCamera;
    BarcodeDetector barcodeDetector;
    private FrameLayout btnCatch;
    private int ans; // 經緯度距離
    private ArrayList<StoreBean> storeList = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    private boolean position1, position2, position3, position4, position5, position6, position7, position8, isGift;
    int count;

    private StoreBean storeBean;

    private static final double EARTH_RADIUS = 6378137.0;

    private String mLon = "";
    private String mLat = "";

    private String lat = "";
    private String lon = "";
    private String aid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_camera);

        String s = getIntent().getStringExtra("jiao");
        storeBean = new Gson().fromJson(s, StoreBean.class);
        Log.d("豪豪", "onCreate: " + storeBean);

        sharedPreferences = getSharedPreferences("jiao", MODE_PRIVATE);

        position1 = sharedPreferences.getBoolean("isStatus1", false);
        position2 = sharedPreferences.getBoolean("isStatus2", false);
        position3 = sharedPreferences.getBoolean("isStatus3", false);
        position4 = sharedPreferences.getBoolean("isStatus4", false);
        position5 = sharedPreferences.getBoolean("isStatus5", false);
        position6 = sharedPreferences.getBoolean("isStatus6", false);
        position7 = sharedPreferences.getBoolean("isStatus7", false);
        position8 = sharedPreferences.getBoolean("isStatus8", false);
        count = sharedPreferences.getInt("count", 0);
        isGift = sharedPreferences.getBoolean("isGift", false);

        //=============這是目前位置的經緯度 計算結果跟公式在最下方(也有API可以CALL)========================

        if (MainActivity.myLocation != null) {
            mLon = String.valueOf(MainActivity.myLocation.getLongitude());
            mLat = String.valueOf(MainActivity.myLocation.getLatitude());
        }

        if (storeBean.getAr_latitude() != null || storeBean.getAr_longitude() != null) {
            lat = storeBean.getAr_latitude();
            lon = storeBean.getAr_longitude();
        }
        if (storeBean.getAid() != null) {
            aid = storeBean.getAid();
        }

        ARSurfaceView = findViewById(R.id.ARSurfaceView);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        btnCatch = findViewById(R.id.btnARFind);

        //先抓儲存資料的做初始狀態
        ArrayList<Boolean> booleanArrayList = new ArrayList<>();

        booleanArrayList.add(position1);
        booleanArrayList.add(position2);
        booleanArrayList.add(position3);
        booleanArrayList.add(position4);
        booleanArrayList.add(position5);
        booleanArrayList.add(position6);
        booleanArrayList.add(position7);
        booleanArrayList.add(position8);

        ARCamera = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true).setRequestedPreviewSize(720, 480).build();

        //加入事件到surfaceView
        ARSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    //檢查授權
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    //如果授權許可，啟動相機
                    ARCamera.start(ARSurfaceView.getHolder());

                } catch (Exception e) {
                    Toast.makeText(JiaoBanShanCameraActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

        //=============目前位置跟人偶位置的"距離"===========
        //單位是公尺(?沒確認) 不顯示 可以用下面log看 定位到那邊距離會正常
        if (!mLat.isEmpty() && !mLon.isEmpty() && !lat.isEmpty() && !lon.isEmpty()) {

            ans = Integer.parseInt(AppUtility.getDistance(mLat, mLon, lat, lon));

            btnCatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Dialog dialog = new Dialog(JiaoBanShanCameraActivity.this);
                            if (ans < 50) {
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
                                txtContent.setText("唉呀，\n沒有成功...\n可能要再靠近一些喔！");

                                btnBack.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (dialog != null) {
                                            dialog.dismiss();
                                            Intent i = new Intent(JiaoBanShanCameraActivity.this, JiaoBanShanActivity.class);
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
        } else {
            btnCatch.setOnClickListener(v ->
            {
                Toast.makeText(this, "請划掉點點app並操作重新啟動", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void countNumber() {

        if (storeBean.getAid().equals("50") && storeBean.getAid() != null && !position1) { // 復興區歷史文化館 50
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus1", true)
                    .commit();
        }
        if (storeBean.getAid().equals("51") && storeBean.getAid() != null && !position2) { // 復興區介壽國小 51
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus2", true)
                    .commit();
        }
        if (storeBean.getAid().equals("52") && storeBean.getAid() != null && !position3) { // 角板山行館 52
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus3", true)
                    .commit();
        }
        if (storeBean.getAid().equals("53") && storeBean.getAid() != null && !position4) { // 角板山公園 53
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus4", true)
                    .commit();
        }
        if (storeBean.getAid().equals("54") && storeBean.getAid() != null && !position5) { // 角板山時光隧道 54
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus5", true)
                    .commit();
        }
        if (storeBean.getAid().equals("55") && storeBean.getAid() != null && !position6) {  // 復興青年活動中心 55
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus6", true)
                    .commit();
        }
        if (storeBean.getAid().equals("56") && storeBean.getAid() != null && !position7) { // 角板山天幕廣場 56
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus7", true)
                    .commit();
        }
        if (storeBean.getAid().equals("57") && storeBean.getAid() != null && !position8) { // 福興宮 57
            count += 1;
            sharedPreferences.edit()
                    .putInt("count", count)
                    .putBoolean("isStatus8", true)
                    .commit();
        }
        showDialog();
    }

    private void showDialog() {
        if (count > 3 && !isGift) {
            getCoupon();
            runOnUiThread(() -> {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_ar_success);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                TextView txtContent = dialog.findViewById(R.id.tv_content);
                txtContent.setText("已經收集4個吉祥物\n並獲得一份禮品兌換券\n(請至活動地圖右上角\n\"優惠券\"頁面查看內容)");
                Button btnBack = dialog.findViewById(R.id.btn_close);
                btnBack.setText("查看優惠券");

                btnBack.setOnClickListener(v1 -> {
                    if (dialog != null) {
                        dialog.dismiss();
                        Intent i = new Intent(this, JiaoBanShanActivity.class);
                        i.putExtra("gain", "gain");
                        startActivity(i);
                        finish();
                    }
                });
            });

        } else if (count > 3 && isGift) {
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
                txtTitle.setText("恭喜您找到吉祥物！");
                txtContent.setText("您已領過一份禮品兌換券了\n請至活動地圖右上角\n\"優惠券\"頁面查看內容)\n(每個帳號限領一次)");
                Button btnBack = dialog.findViewById(R.id.btn_close);
                btnBack.setText("確認");

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null) {
                            dialog.dismiss();
                            startActivity(new Intent(JiaoBanShanCameraActivity.this, JiaoBanShanActivity.class));
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
            txtTitle.setText("恭喜您找到吉祥物！");
            txtContent.setText("收集任意4個吉祥物\n就可獲得1份禮品兌換券\n(每個帳號限領一份)");
            Button btnBack = dialog.findViewById(R.id.btn_close);
            btnBack.setText("確認");

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.dismiss();
                        startActivity(new Intent(JiaoBanShanCameraActivity.this, JiaoBanShanActivity.class));
                        finish();
                    }
                }
            });

        }
    }

    private void getCoupon() {
        ApiConnection.getCoupon2("ARCOUPON4", new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                sharedPreferences.edit()
                        .putBoolean("isGift", true)
                        .commit();
            }

            @Override
            public void onFailure(String message) {
//                sharedPreferences.edit()
//                        .putBoolean("isGift", false)
//                        .commit();
            }
        });
        ;
    }

    private void loadInfo(String s) {
        ApiConnection.getARShopInfo(s, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<StoreBean>>() {
                }.getType();
                storeList = new Gson().fromJson(jsonString, type);
                runOnUiThread(() -> {
                    Dialog dialogCollect = new Dialog(JiaoBanShanCameraActivity.this);
                    dialogCollect.setContentView(R.layout.dialog_gold_recipe);
                    dialogCollect.setCanceledOnTouchOutside(false);
                    dialogCollect.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    dialogCollect.show();
                    dialogCollect.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                    TextView txtTitle = dialogCollect.findViewById(R.id.gdT);
                    TextView txtContent = dialogCollect.findViewById(R.id.gdTC);
                    ImageView img = dialogCollect.findViewById(R.id.gdI);
                    txtTitle.setText("恭喜您找到「" + storeList.get(0).getAr_name2() + "」");
                    txtContent.setText(storeList.get(0).getArDescript2());
                    Picasso.with(JiaoBanShanCameraActivity.this).load(ApiConstant.API_IMAGE + storeList.get(0).getAr_picture2()).into(img);

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
                        Toast.makeText(JiaoBanShanCameraActivity.this, "連線有誤，請重新操作", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}
