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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.beautySalons.model.WorkingDayBean;
import com.jotangi.nickyen.home.MyDiscountNew2Activity;
import com.jotangi.nickyen.home.model.CouponListBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SalonsCameraActivity extends AppCompatActivity
{
    private SurfaceView ARSurfaceView;
    private CameraSource ARCamera;
    BarcodeDetector barcodeDetector;
    private FrameLayout btnCatch;
    private String distance;
    private int ans;//經緯度距離

    private SharedPreferences sharedPreferences;

    private boolean b, b2, b3, b4, b5, b6;
    int count = 0;

    private StoreBean storeBean;
    private String coupon = "";

    private String mLon = "";
    private String mLat = "";

    private String lat = "";
    private String lon = "";

    private static final double EARTH_RADIUS = 6378137.0;

//    private boolean b; //判斷刷新狀態規則為：一天只能領取一個，不管哪個景點只能領一次。凌晨24:00刷新

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_camera);

        String s = getIntent().getStringExtra("salons");
        storeBean = new Gson().fromJson(s, StoreBean.class);

//        sharedPreferences = getSharedPreferences("triangle", MODE_PRIVATE);
//        b = sharedPreferences.getBoolean("isStatus1", false);
//        b2 = sharedPreferences.getBoolean("isStatus2", false);
//        b3 = sharedPreferences.getBoolean("isStatus3", false);
//        b4 = sharedPreferences.getBoolean("isStatus4", false);
//        b5 = sharedPreferences.getBoolean("isStatus5", false);
//        b6 = sharedPreferences.getBoolean("isStatus6", false);

        //=============這是目前位置的經緯度 計算結果跟公式在最下方(也有API可以CALL)========================
        if (MainActivity.myLocation != null)
        {
            mLon = String.valueOf(MainActivity.myLocation.getLongitude());
            mLat = String.valueOf(MainActivity.myLocation.getLatitude());
        }

        if (storeBean.getAr_latitude() != null || storeBean.getAr_longitude() != null)
        {
            lat = storeBean.getAr_latitude();
            lon = storeBean.getAr_longitude();
        }

        ARSurfaceView = findViewById(R.id.ARSurfaceView);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        if (storeBean.getAid().equals("15"))
            coupon = "AR_BEAUTY6";
//        if (storeBean.getAid().equals("16"))
//            coupon = "AR_BEAUTY2";
        if (storeBean.getAid().equals("17"))
            coupon = "AR_BEAUTY5";
        if (storeBean.getAid().equals("18"))
            coupon = "AR_BEAUTY4";
        if (storeBean.getAid().equals("19"))
            coupon = "AR_BEAUTY1";
        if (storeBean.getAid().equals("31"))
            coupon = "AR_BEAUTY2";
        if (storeBean.getAid().equals("32"))
            coupon = "AR_BEAUTY3";

        btnCatch = findViewById(R.id.btnARFind);

        //先抓儲存資料的做初始狀態
//        ArrayList<Boolean> booleanArrayList = new ArrayList<>();

//        b = sharedPreferences.getBoolean("isStatus", false);
//        booleanArrayList.add(b);
//        booleanArrayList.add(b2);
//        booleanArrayList.add(b3);
//        booleanArrayList.add(b4);
//        booleanArrayList.add(b5);
//        booleanArrayList.add(b6);

//        for (int i = 0; i < booleanArrayList.size(); i++)
//        {
//            if (b)
//            {
//                count += 1;
//                b = false;
//            } else if (b2)
//            {
//                count += 1;
//                b2 = false;
//            } else if (b3)
//            {
//                count += 1;
//                b3 = false;
//            } else if (b4)
//            {
//                count += 1;
//                b4 = false;
//            } else if (b5)
//            {
//                count += 1;
//                b5 = false;
//            } else if (b6)
//            {
//                count += 1;
//                b6 = false;
//            }
//            Log.d("豪豪", "計次: " + count);
//        }

//        long selectTime = sharedPreferences.getLong("time", 1);

        //這是每天領一次的規則
//        if (b) //布林變數是從抓的那邊獲取 來判斷有沒有抓過，有就進來判斷時間
//        {
//            //創建日曆
//            Calendar calendar = Calendar.getInstance();
//            //設置與當前手機時間同步
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            //獲取當前秒值
//            long systemTime = System.currentTimeMillis();
//            //初始化日曆時間，與手機當前同步
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            //設置時區
//            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//            //設置預定時間 凌晨24:00
////            calendar.set(Calendar.HOUR_OF_DAY, 16);
////            calendar.set(Calendar.MINUTE, 15);
////            calendar.set(Calendar.SECOND, 0);
//            //獲取上面設置的秒值
////            long selectTime = calendar.getTimeInMillis()/10;
//
//            Log.d("豪豪", "systemTime: " + systemTime + "selectTime: " + selectTime);
//            //當前時間若大於設置時間，那就從第二天設定時間開始
//            if (systemTime > selectTime) //系統時間大於選定時間 表示可以抓false
//            {
//                //超過當日凌晨24:00 要刷新抓取的布林變數
//                b = false;
//                Log.d("豪豪", "有進入: ");
//            } else
//            {
//                b = true;
//            }
//        }


        ARCamera = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true).setRequestedPreviewSize(720, 480).build();

        //加入事件到surfaceView
        ARSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                try
                {
                    //檢查授權
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    //如果授權許可，啟動相機
                    ARCamera.start(ARSurfaceView.getHolder());

                } catch (Exception e)
                {
                    Toast.makeText(SalonsCameraActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2)
            {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder)
            {
                ARCamera.stop();
            }
        });

        //=============目前位置跟人偶位置的"距離"===========
        //單位是公尺(?沒確認) 不顯示 可以用下面log看 定位到那邊距離會正常
        if (!mLat.isEmpty() && !mLon.isEmpty() && !lat.isEmpty() && !lon.isEmpty())
        {
            distance = getDistance(mLat, mLon, lat, lon);
            ans = Integer.valueOf(distance);

            btnCatch.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ARCamera.stop();
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Dialog dialog = new Dialog(SalonsCameraActivity.this);
                            if (ans < 50)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        dialog.setContentView(R.layout.dialog_ar_success);
                                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                        dialog.show();
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                        TextView txtTitle = dialog.findViewById(R.id.tv_title);
                                        String s = "";
                                        switch (storeBean.getAid())
                                        {
                                            case "15":
                                                s = "伊登";
                                                break;
//                                            case "16":
//                                                s="維斯塔";
//                                                break;
                                            case "17":
                                                s = "洛基";
                                                break;
                                            case "18":
                                                s = "希拉";
                                                break;
                                            case "19":
                                                s = "維納斯";
                                                break;
                                        }
                                        txtTitle.setText("取得\n" + s + "水晶！");
                                        TextView txtContent = dialog.findViewById(R.id.tv_content);
                                        txtContent.setVisibility(View.GONE);
                                        ImageView img = dialog.findViewById(R.id.iv_content);
                                        img.setVisibility(View.VISIBLE);
                                        Button btnBack = dialog.findViewById(R.id.btn_close);
                                        btnBack.setText("提煉水晶");

                                        btnBack.setOnClickListener(new View.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View v)
                                            {
                                                if (dialog != null)
                                                {
                                                    dialog.dismiss();
                                                }
                                                ApiConnection.getCoupon(coupon, new ApiConnection.OnConnectResultListener()
                                                {
                                                    @Override
                                                    public void onSuccess(String jsonString)
                                                    {
                                                        ArrayList<CouponListBean> couponListBeans = new ArrayList<>();
                                                        try
                                                        {
                                                            JSONObject jsonObject = new JSONObject(jsonString);
                                                            String couponInfo = jsonObject.getString("coupon_info");
                                                            Type type = new TypeToken<ArrayList<CouponListBean>>()
                                                            {
                                                            }.getType();
                                                            couponListBeans = new Gson().fromJson(couponInfo, type);
                                                        } catch (JSONException e)
                                                        {
                                                            e.printStackTrace();
                                                        }

                                                        ArrayList<CouponListBean> finalCouponListBeans = couponListBeans;
                                                        runOnUiThread(new Runnable()
                                                        {
                                                            @Override
                                                            public void run()
                                                            {
                                                                Dialog dialog2 = new Dialog(SalonsCameraActivity.this);
                                                                dialog2.setContentView(R.layout.dialog_ar_success);
                                                                dialog2.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                                                dialog2.show();
                                                                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                                                TextView txtContent = dialog2.findViewById(R.id.tv_content);
                                                                txtContent.setText(finalCouponListBeans.get(0).getCouponName() + "好禮兌換券\n請至我的優惠券查看\n(每帳號限領一份)");
                                                                Button btnBack = dialog2.findViewById(R.id.btn_close);
                                                                btnBack.setOnClickListener(new View.OnClickListener()
                                                                {
                                                                    @Override
                                                                    public void onClick(View v)
                                                                    {
                                                                        if (dialog2 != null)
                                                                        {
                                                                            Intent intent = new Intent(SalonsCameraActivity.this, MyDiscountNew2Activity.class);
                                                                            dialog2.dismiss();
                                                                            startActivity(intent);
                                                                            finish();
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
                                                                Toast.makeText(SalonsCameraActivity.this, message, Toast.LENGTH_SHORT).show();
                                                                Intent i = new Intent(SalonsCameraActivity.this, SalonsARActivity.class);
                                                                startActivity(i);
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            } else
                            {
                                dialog.setContentView(R.layout.dialog_ar_collection);
                                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                dialog.show();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                TextView txtTitle = dialog.findViewById(R.id.tv_title);
                                TextView txtContent = dialog.findViewById(R.id.tv_content);
                                Button btnBack = dialog.findViewById(R.id.btn_close);
                                txtTitle.setText("再接再厲");
                                txtContent.setText("唉呀，\n沒有拿到水晶...\n可能要再靠近提示的地點喔！");

                                btnBack.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        if (dialog != null)
                                        {
                                            dialog.dismiss();
                                            Intent i = new Intent(SalonsCameraActivity.this, SalonsARActivity.class);
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
        } else
        {
            btnCatch.setOnClickListener(v ->
            {
                Toast.makeText(this, "請划掉點點app並操作重新啟動", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void countNumber()
    {

        if (storeBean.getAid().equals("9") && storeBean.getAid() != null && !b)
        {
            sharedPreferences.edit()
                    .putString("isNumber1", "9")
                    .putBoolean("isStatus1", true)
                    .commit();
        }
        if (storeBean.getAid().equals("10") && storeBean.getAid() != null && !b2)
        {
            sharedPreferences.edit()
                    .putString("isNumber2", "10")
                    .putBoolean("isStatus2", true)
                    .commit();
        }
        if (storeBean.getAid().equals("11") && storeBean.getAid() != null && !b3)
        {
            sharedPreferences.edit()
                    .putString("isNumber3", "11")
                    .putBoolean("isStatus3", true)
                    .commit();
        }
        if (storeBean.getAid().equals("12") && storeBean.getAid() != null && !b4)
        {
            sharedPreferences.edit()
                    .putString("isNumber4", "12")
                    .putBoolean("isStatus4", true)
                    .commit();
        }
        if (storeBean.getAid().equals("13") && storeBean.getAid() != null && !b5)
        {
            sharedPreferences.edit()
                    .putString("isNumber5", "13")
                    .putBoolean("isStatus5", true)
                    .commit();
        }
        if (storeBean.getAid().equals("14") && storeBean.getAid() != null && !b6)
        {
            sharedPreferences.edit()
                    .putString("isNumber6", "15")
                    .putBoolean("isStatus6", true)
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

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public String getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str)
    {

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

}
