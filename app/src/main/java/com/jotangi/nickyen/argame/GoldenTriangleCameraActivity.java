package com.jotangi.nickyen.argame;


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
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.home.MyDiscountNew2Activity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GoldenTriangleCameraActivity extends AppCompatActivity {
    private SurfaceView ARSurfaceView;
    private CameraSource ARCamera;
    BarcodeDetector barcodeDetector;
    private FrameLayout btnCatch;
    private String distance;
    private int ans;//經緯度距離
    private ArrayList<StoreBean> storeList = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    private boolean b, b2, b3, b4, b5, b6;
    private boolean bowl1, bowl2, bowl3, bowl4, bowl5, bowl6, bowl7, bowl8, bowl9, bowl10, bowl11, bowl12;
    int count = 0;

    private StoreBean storeBean;

    private static final double EARTH_RADIUS = 6378137.0;

    private String mLon = "";
    private String mLat = "";

    private String lat = "";
    private String lon = "";
    private String aid = "";

//    private boolean b; //判斷刷新狀態規則為：一天只能領取一個，不管哪個景點只能領一次。凌晨24:00刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_camera);

        String s = getIntent().getStringExtra("triangle");
        storeBean = new Gson().fromJson(s, StoreBean.class);
        Log.d("豪豪", "onCreate: " + storeBean);
        /*原先的引用參數*/
        sharedPreferences = getSharedPreferences("triangle", MODE_PRIVATE);
        b = sharedPreferences.getBoolean("isStatus1", false);
        b2 = sharedPreferences.getBoolean("isStatus2", false);
        b3 = sharedPreferences.getBoolean("isStatus3", false);
        b4 = sharedPreferences.getBoolean("isStatus4", false);
        b5 = sharedPreferences.getBoolean("isStatus5", false);
        b6 = sharedPreferences.getBoolean("isStatus6", false);
        /*2022/03/21新需求改成12個碗，先沿用原本邏輯*/
        bowl1 = sharedPreferences.getBoolean("isBowl1", false);
        bowl2 = sharedPreferences.getBoolean("isBowl2", false);
        bowl3 = sharedPreferences.getBoolean("isBowl3", false);
        bowl4 = sharedPreferences.getBoolean("isBowl4", false);
        bowl5 = sharedPreferences.getBoolean("isBowl5", false);
        bowl6 = sharedPreferences.getBoolean("isBowl6", false);
        bowl7 = sharedPreferences.getBoolean("isBowl7", false);
        bowl8 = sharedPreferences.getBoolean("isBowl8", false);
        bowl9 = sharedPreferences.getBoolean("isBowl9", false);
        bowl10 = sharedPreferences.getBoolean("isBowl10", false);
        bowl11 = sharedPreferences.getBoolean("isBowl11", false);
        bowl12 = sharedPreferences.getBoolean("isBowl12", false);

        //=============這是目前位置的經緯度 計算結果跟公式在最下方(也有API可以CALL)========================

        if (MainActivity.myLocation != null) {
            mLon = String.valueOf(MainActivity.myLocation.getLongitude());
            mLat = String.valueOf(MainActivity.myLocation.getLatitude());
        }

        if (storeBean.getAr_latitude() != null || storeBean.getAr_longitude() != null) {
            lat = storeBean.getAr_latitude();
            lon = storeBean.getAr_longitude();
        }
        if(storeBean.getAid()!=null){
            aid=storeBean.getAid();
        }

        ARSurfaceView = findViewById(R.id.ARSurfaceView);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        btnCatch = findViewById(R.id.btnARFind);

        //先抓儲存資料的做初始狀態
        ArrayList<Boolean> booleanArrayList = new ArrayList<>();

//        b = sharedPreferences.getBoolean("isStatus", false);
        /*原先rd邏輯*/
        booleanArrayList.add(b);
        booleanArrayList.add(b2);
        booleanArrayList.add(b3);
        booleanArrayList.add(b4);
        booleanArrayList.add(b5);
        booleanArrayList.add(b6);
        /*新需求*/
        booleanArrayList.add(bowl1);
        booleanArrayList.add(bowl2);
        booleanArrayList.add(bowl3);
        booleanArrayList.add(bowl4);
        booleanArrayList.add(bowl5);
        booleanArrayList.add(bowl6);
        booleanArrayList.add(bowl7);
        booleanArrayList.add(bowl8);
        booleanArrayList.add(bowl9);
        booleanArrayList.add(bowl10);
        booleanArrayList.add(bowl11);
        booleanArrayList.add(bowl12);

        for (int i = 0; i < booleanArrayList.size(); i++) {
            if (b) {
                count += 1;
                b = false;
            } else if (b2) {
                count += 1;
                b2 = false;
            } else if (b3) {
                count += 1;
                b3 = false;
            } else if (b4) {
                count += 1;
                b4 = false;
            } else if (b5) {
                count += 1;
                b5 = false;
            } else if (b6) {
                count += 1;
                b6 = false;
            } else if (bowl1) {
                count += 1;
                bowl1 = false;
            } else if (bowl2) {
                count += 1;
                bowl2 = false;
            } else if (bowl3) {
                count += 1;
                bowl3 = false;
            } else if (bowl4) {
                count += 1;
                bowl4 = false;
            } else if (bowl5) {
                count += 1;
                bowl5 = false;
            } else if (bowl6) {
                count += 1;
                bowl6 = false;
            } else if (bowl7) {
                count += 1;
                bowl7 = false;
            } else if (bowl8) {
                count += 1;
                bowl8 = false;
            } else if (bowl9) {
                count += 1;
                bowl9 = false;
            } else if (bowl10) {
                count += 1;
                bowl10 = false;
            } else if (bowl11) {
                count += 1;
                bowl11 = false;
            } else if (bowl12) {
                count += 1;
                bowl12 = false;
            }
            Log.d("豪豪", "計次: " + count);

        }


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
                    Toast.makeText(GoldenTriangleCameraActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            distance = getDistance(mLat, mLon, lat, lon);
            ans = Integer.valueOf(distance);

            btnCatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Dialog dialog = new Dialog(GoldenTriangleCameraActivity.this);
                            if (ans < 50) {
                                if (count == 3) {
                                    runOnUiThread(() -> {
                                        Dialog dialog1 = new Dialog(GoldenTriangleCameraActivity.this);
                                        dialog1.setContentView(R.layout.dialog_ar_success);
                                        dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                        dialog1.show();
                                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                        TextView txtContent = dialog1.findViewById(R.id.tv_content);
                                        txtContent.setText("魅力金三角商圈好禮兌換券\n請至我的優惠券查看\n(每帳號限領一份)");
                                        Button btnBack = dialog1.findViewById(R.id.btn_close);

                                        btnBack.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v1) {
                                                if (dialog1 != null) {
                                                    ApiConnection.getCoupon("TRIANGLE_ARVOUCHER", new ApiConnection.OnConnectResultListener() {
                                                        @Override
                                                        public void onSuccess(String jsonString) {
                                                            countNumber();
                                                            Intent intent = new Intent(GoldenTriangleCameraActivity.this, MyDiscountNew2Activity.class);
                                                            dialog1.dismiss();
                                                            startActivity(intent);
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onFailure(String message) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    countNumber();
                                                                    Toast.makeText(GoldenTriangleCameraActivity.this, message, Toast.LENGTH_SHORT).show();
                                                                    dialog1.dismiss();
                                                                    Intent i = new Intent(GoldenTriangleCameraActivity.this, GoldenTriangleActivity.class);
                                                                    startActivity(i);
                                                                    finish();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    });

                                } else if (count == 4 || count == 5) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Dialog dialog = new Dialog(GoldenTriangleCameraActivity.this);
                                            dialog.setContentView(R.layout.dialog_ar_success);
                                            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                            dialog.show();
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                            TextView txtContent = dialog.findViewById(R.id.tv_content);
                                            TextView txtTitle = dialog.findViewById(R.id.tv_title);
                                            txtTitle.setText("恭喜收集到1個景點！");
                                            txtContent.setText("您已領取過一份好禮兌換券了\n(每帳號限領一份)");
                                            Button btnBack = dialog.findViewById(R.id.btn_close);
                                            btnBack.setText("返回");

                                            btnBack.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (dialog != null) {
//                                                        ApiConnection.getCoupon("ARCOUPON2", new ApiConnection.OnConnectResultListener()
//                                                        {
//                                                            @Override
//                                                            public void onSuccess(String jsonString)
//                                                            {
//                                                                countNumber();
//                                                            }

//                                                            @Override
//                                                            public void onFailure(String message)
//                                                            {
//                                                                runOnUiThread(new Runnable()
//                                                                {
//                                                                    @Override
//                                                                    public void run()
//                                                                    {
//                                                                        Toast.makeText(GoldenTriangleCameraActivity.this, message, Toast.LENGTH_SHORT).show();
//                                                                    }
//                                                                });
//                                                            }
//                                                        });
                                                        countNumber();
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    runOnUiThread(() ->{
                                                loadInfo(aid);}
                                            );
                                }
//                                if (!b) //沒抓到過 要成功
//                                {
//                                    dialog.setContentView(R.layout.dialog_ar_success);
//                                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                                    dialog.show();
//                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
//                                    TextView txtContent = dialog.findViewById(R.id.tv_content);
//                                    txtContent.setText("魅力金三角商圈\n\n好禮兌換券\n\n請至我的優惠券查看");
//                                    Button btnBack = dialog.findViewById(R.id.btn_close);
//
//                                    btnBack.setOnClickListener(new View.OnClickListener()
//                                    {
//                                        @Override
//                                        public void onClick(View v)
//                                        {
//                                            if (dialog != null)
//                                            {
//                                                ApiConnection.getCoupon("TRIANGLE_ARVOUCHER", new ApiConnection.OnConnectResultListener()
//                                                {
//                                                    @Override
//                                                    public void onSuccess(String jsonString)
//                                                    {
////                                                        Calendar calendar = Calendar.getInstance();
////                                                        //初始化日曆時間，與手機當前同步
////                                                        calendar.setTimeInMillis(System.currentTimeMillis());
////                                                        //設置時區
////                                                        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
////                                                        //設置預定時間 凌晨24:00
////                                                        calendar.set(Calendar.HOUR_OF_DAY, 0);
////                                                        calendar.set(Calendar.MINUTE, 0);
////                                                        calendar.set(Calendar.SECOND, 0);
////                                                        calendar.set(Calendar.MILLISECOND, 0);
//                                                        //獲取上面設置的秒值
////                                                        long selectTime = calendar.getTimeInMillis();
//                                                        b = true;
//                                                        sharedPreferences.edit()
////                                                                .putLong("time", selectTime + 86400000)
//                                                                .putBoolean("isStatus", b)
//                                                                .commit();
//                                                        Intent intent = new Intent(GoldenTriangleCameraActivity.this, MyDiscountNewActivity.class);
//                                                        dialog.dismiss();
//                                                        startActivity(intent);
//                                                        finish();
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(String message)
//                                                    {
//                                                        runOnUiThread(new Runnable()
//                                                        {
//                                                            @Override
//                                                            public void run()
//                                                            {
//                                                                Toast.makeText(GoldenTriangleCameraActivity.this, message, Toast.LENGTH_SHORT).show();
//                                                                dialog.dismiss();
//                                                                Intent i = new Intent(GoldenTriangleCameraActivity.this, GoldenTriangleActivity.class);
//                                                                startActivity(i);
//                                                                finish();
//                                                            }
//                                                        });
//                                                    }
//
//                                                });
////                                                Calendar calendar = Calendar.getInstance();
////                                                //初始化日曆時間，與手機當前同步
////                                                calendar.setTimeInMillis(System.currentTimeMillis());
////                                                //設置時區
////                                                calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
////                                                //設置預定時間 凌晨24:00
////                                                calendar.set(Calendar.HOUR_OF_DAY, 0);
////                                                calendar.set(Calendar.MINUTE, 0);
////                                                calendar.set(Calendar.SECOND, 0);
////                                                calendar.set(Calendar.MILLISECOND, 0);
////                                                //獲取上面設置的秒值
////                                                long selectTime = calendar.getTimeInMillis();
////                                                b = true;
////                                                sharedPreferences.edit()
////                                                        .putLong("time", selectTime + 86400000)
////                                                        .putBoolean("isStatus", b)
////                                                        .commit();
////                                                dialog.dismiss();
////                                                Intent i = new Intent(GoldenTriangleCameraActivity.this, GoldenTriangleActivity.class);
////                                                startActivity(i);
////                                                finish();
//                                            }
//                                        }
//                                    });
//                                } else
//                                {
//                                    dialog.setContentView(R.layout.dialog_ar_collection);
//                                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                                    dialog.show();
//                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
//                                    Button btnBack = dialog.findViewById(R.id.btn_close);
//                                    TextView txtTitle = dialog.findViewById(R.id.tv_title);
//                                    txtTitle.setText("恭喜您已經收集到1個景點！");
//                                    TextView txtContent = dialog.findViewById(R.id.tv_content);
//                                    txtContent.setText("您已領取過\n(每人限領一份)");
//                                    btnBack.setOnClickListener(new View.OnClickListener()
//                                    {
//                                        @Override
//                                        public void onClick(View v)
//                                        {
//                                            if (dialog != null)
//                                            {
//                                                dialog.dismiss();
//                                                Intent i = new Intent(GoldenTriangleCameraActivity.this, GoldenTriangleActivity.class);
//                                                startActivity(i);
//                                                finish();
//                                            }
//                                        }
//                                    });
//                                }
                            } else {
                                dialog.setContentView(R.layout.dialog_ar_collection);
                                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                dialog.show();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                TextView txtTitle = dialog.findViewById(R.id.tv_title);
                                TextView txtContent = dialog.findViewById(R.id.tv_content);
                                Button btnBack = dialog.findViewById(R.id.btn_close);
                                txtTitle.setText("再接再厲");
                                txtContent.setText("唉呀，\n沒有捕捉到景點畫面...\n可能要再靠近一些喔！");

                                btnBack.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (dialog != null) {
                                            dialog.dismiss();
                                            Intent i = new Intent(GoldenTriangleCameraActivity.this, GoldenTriangleActivity.class);
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
                Toast.makeText(this, "未獲取您的定位資訊，請退出重新", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void countNumber() {

        if (storeBean.getAid().equals("9") && storeBean.getAid() != null && !b) {
            sharedPreferences.edit()
                    .putString("isNumber1", "9")
                    .putBoolean("isStatus1", true)
                    .commit();
        }
        if (storeBean.getAid().equals("10") && storeBean.getAid() != null && !b2) {
            sharedPreferences.edit()
                    .putString("isNumber2", "10")
                    .putBoolean("isStatus2", true)
                    .commit();
        }
        if (storeBean.getAid().equals("11") && storeBean.getAid() != null && !b3) {
            sharedPreferences.edit()
                    .putString("isNumber3", "11")
                    .putBoolean("isStatus3", true)
                    .commit();
        }
        if (storeBean.getAid().equals("12") && storeBean.getAid() != null && !b4) {
            sharedPreferences.edit()
                    .putString("isNumber4", "12")
                    .putBoolean("isStatus4", true)
                    .commit();
        }
        if (storeBean.getAid().equals("13") && storeBean.getAid() != null && !b5) {
            sharedPreferences.edit()
                    .putString("isNumber5", "13")
                    .putBoolean("isStatus5", true)
                    .commit();
        }
        if (storeBean.getAid().equals("14") && storeBean.getAid() != null && !b6) {
            sharedPreferences.edit()
                    .putString("isNumber6", "15")
                    .putBoolean("isStatus6", true)
                    .commit();
        }
        /*2022/03/21新增需求，延續原本rd logic*/
        if (storeBean.getAid().equals("38") && storeBean.getAid() != null && !bowl1) {
            sharedPreferences.edit()
                    .putString("isNumber38", "38")
                    .putBoolean("isStatus38", true)
                    .commit();
        }
        if (storeBean.getAid().equals("39") && storeBean.getAid() != null && !bowl2) {
            sharedPreferences.edit()
                    .putString("isNumber39", "39")
                    .putBoolean("isStatus39", true)
                    .commit();
        }
        if (storeBean.getAid().equals("41") && storeBean.getAid() != null && !bowl3) {
            sharedPreferences.edit()
                    .putString("isNumber40", "41")
                    .putBoolean("isStatus40", true)
                    .commit();
        }
        if (storeBean.getAid().equals("43") && storeBean.getAid() != null && !bowl4) {
            sharedPreferences.edit()
                    .putString("isNumber41", "43")
                    .putBoolean("isStatus41", true)
                    .commit();
        }
        if (storeBean.getAid().equals("42") && storeBean.getAid() != null && !bowl5) {
            sharedPreferences.edit()
                    .putString("isNumber42", "42")
                    .putBoolean("isStatus42", true)
                    .commit();
        }
        if (storeBean.getAid().equals("47") && storeBean.getAid() != null && !bowl6) {
            sharedPreferences.edit()
                    .putString("isNumber43", "47")
                    .putBoolean("isStatus43", true)
                    .commit();
        }
        if (storeBean.getAid().equals("44") && storeBean.getAid() != null && !bowl7) {
            sharedPreferences.edit()
                    .putString("isNumber44", "44")
                    .putBoolean("isStatus44", true)
                    .commit();
        }
        if (storeBean.getAid().equals("40") && storeBean.getAid() != null && !bowl8) {
            sharedPreferences.edit()
                    .putString("isNumber45", "40")
                    .putBoolean("isStatus45", true)
                    .commit();
        }
        if (storeBean.getAid().equals("45") && storeBean.getAid() != null && !bowl9) {
            sharedPreferences.edit()
                    .putString("isNumber46", "45")
                    .putBoolean("isStatus46", true)
                    .commit();
        }
        if (storeBean.getAid().equals("46") && storeBean.getAid() != null && !bowl10) {
            sharedPreferences.edit()
                    .putString("isNumber47", "46")
                    .putBoolean("isStatus47", true)
                    .commit();
        }
        if (storeBean.getAid().equals("48") && storeBean.getAid() != null && !bowl11) {
            sharedPreferences.edit()
                    .putString("isNumber48", "48")
                    .putBoolean("isStatus48", true)
                    .commit();
        }
        if (storeBean.getAid().equals("49") && storeBean.getAid() != null && !bowl12) {
            sharedPreferences.edit()
                    .putString("isNumber49", "49")
                    .putBoolean("isStatus49", true)
                    .commit();
        }
    }

    /**
     * 根據兩個位置的經緯度，來計算兩地的距離（單位為KM）
     * 引數為String型別
     * //     * @param lat1 使用者經度
     * //     * @param lng1 使用者緯度
     * //     * @param lat2 商家經度
     * //     * @param lng2 商家緯度
     *
     * @return
     **/

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public String getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {

        Double lat1 = Double.parseDouble(lat1Str);
        Double lng1 = Double.parseDouble(lng1Str);
        Double lat2 = Double.parseDouble(lat2Str);
        Double lng2 = Double.parseDouble(lng2Str);

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;
        String distanceStr = distance + "";
        distanceStr = distanceStr.
                substring(0, distanceStr.indexOf("."));

        return distanceStr;
    }

    private void collectDialog() {
        Dialog dialog = new Dialog(GoldenTriangleCameraActivity.this);
        dialog.setContentView(R.layout.dialog_ar_collection);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        TextView txtContent = dialog.findViewById(R.id.tv_content);
        TextView txtTitle = dialog.findViewById(R.id.tv_title);
        txtTitle.setText("恭喜收集到1個景點！");
        txtContent.setText("收集4個景點，\n即可獲得好禮兌換券\n(每帳號限領1份)");
        Button btnBack = dialog.findViewById(R.id.btn_close);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    countNumber();
                    dialog.dismiss();
                    Intent i = new Intent(GoldenTriangleCameraActivity.this, GoldenTriangleActivity.class);
                    startActivity(i);
                    finish();
                }
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
                runOnUiThread(() -> {
                    Dialog dialog = new Dialog(GoldenTriangleCameraActivity.this);
                    dialog.setContentView(R.layout.dialog_gold_recipe);
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                    TextView txtTitle = dialog.findViewById(R.id.gdT);
                    TextView txtContent = dialog.findViewById(R.id.gdTC);
                   ImageView img = dialog.findViewById(R.id.gdI);
                    txtTitle.setText(storeList.get(0).getAr_name2());
                    txtContent.setText(storeList.get(0).getArDescript2());
                    Picasso.with(GoldenTriangleCameraActivity.this).load(ApiConstant.API_IMAGE + storeList.get(0).getAr_picture2()).into(img);

                    Button btnFind = dialog.findViewById(R.id.gdC);
                    btnFind.setOnClickListener(v -> {

                        collectDialog();

                    });

                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoldenTriangleCameraActivity.this, "連線有誤，請重新操作", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}
