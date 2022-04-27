package com.jotangi.nickyen.argame;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.base.BaseActivity;

import java.util.ArrayList;

public class ARCameraActivity extends BaseActivity
{
    private SurfaceView ARSurfaceView;
    private CameraSource ARCamera;
    BarcodeDetector barcodeDetector;
    private FrameLayout btnCatch;
    private String distance;
    private int ans;//經緯度距離

    private SharedPreferences doll;

    ArrayList<StoreBean> storeBean2 = new ArrayList<>();
    private static final double EARTH_RADIUS = 6378137.0;

    private boolean b, b2, b3, b4, b5, b6, b7, b8;
    int count = 0;
    private String aid;

    private String mLon = "";
    private String mLat = "";

    private String lat = "";
    private String lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_camera);

        storeBean2 = ARStoreActivity.storeBean;

        //=============這是目前位置的經緯度 計算結果跟公式在最下方(也有API可以CALL)========================
        if (MainActivity.myLocation != null)
        {
            mLon = String.valueOf(MainActivity.myLocation.getLongitude());
            mLat = String.valueOf(MainActivity.myLocation.getLatitude());
        }


        ARSurfaceView = findViewById(R.id.ARSurfaceView);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        Intent intent = getIntent();
        String lat = intent.getStringExtra("lat");
        String lon = intent.getStringExtra("lon");
        //========接收上一頁的AID=======
        aid = intent.getStringExtra("aid");

        btnCatch = findViewById(R.id.btnARFind);

        //先抓儲存資料的做初始狀態
        ArrayList<Boolean> booleanArrayList = new ArrayList<>();

        doll = getSharedPreferences("doll", MODE_PRIVATE);
        b = doll.getBoolean("isStatus1", false);
        b2 = doll.getBoolean("isStatus2", false);
        b3 = doll.getBoolean("isStatus3", false);
        b4 = doll.getBoolean("isStatus4", false);
        b5 = doll.getBoolean("isStatus5", false);
        b6 = doll.getBoolean("isStatus6", false);
        b7 = doll.getBoolean("isStatus7", false);
        b8 = doll.getBoolean("isStatus8", false);

        booleanArrayList.add(b);
        booleanArrayList.add(b2);
        booleanArrayList.add(b3);
        booleanArrayList.add(b4);
        booleanArrayList.add(b5);
        booleanArrayList.add(b6);
        booleanArrayList.add(b7);
        booleanArrayList.add(b8);

        for (int i = 0; i < booleanArrayList.size(); i++)
        {
            if (b)
            {
                count += 1;
                b = false;
            } else if (b2)
            {
                count += 1;
                b2 = false;
            } else if (b3)
            {
                count += 1;
                b3 = false;
            } else if (b4)
            {
                count += 1;
                b4 = false;
            } else if (b5)
            {
                count += 1;
                b5 = false;
            } else if (b6)
            {
                count += 1;
                b6 = false;
            } else if (b7)
            {
                count += 1;
                b7 = false;
            } else if (b8)
            {
                count += 1;
                b8 = false;
            }
            Log.d("豪豪", "計次: " + count);
        }

        if (aid.equals("1") && b)
        {
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (aid.equals("2") && b2)
        {
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (aid.equals("3") && b3)
        {
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (aid.equals("4") && b4)
        {
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (aid.equals("5") && b5)
        {
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }

        if (aid.equals("6") && b6)
        {
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (aid.equals("7") && b7)
        {
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }
        if (aid.equals("8") && b8)
        {
            Toast.makeText(this, "您已領取過", Toast.LENGTH_SHORT).show();
            return;
        }

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
                    Toast.makeText(ARCameraActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    if (ans < 50)
                    {
                        if (count == 3)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Dialog dialog = new Dialog(ARCameraActivity.this);
                                    dialog.setContentView(R.layout.dialog_ar_success);
                                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                    dialog.show();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                    TextView txtContent = dialog.findViewById(R.id.tv_content);
                                    txtContent.setText("商圈特色客製圖樣醫療口罩兌換券\n請至我的優惠券查看\n(每帳號限領1份)");
                                    Button btnBack = dialog.findViewById(R.id.btn_close);

                                    btnBack.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            if (dialog != null)
                                            {
                                                ApiConnection.getCoupon("ARCOUPON1", new ApiConnection.OnConnectResultListener()
                                                {
                                                    @Override
                                                    public void onSuccess(String jsonString)
                                                    {

                                                        countNumber();
                                                    }

                                                    @Override
                                                    public void onFailure(String message)
                                                    {
                                                        runOnUiThread(new Runnable()
                                                        {
                                                            @Override
                                                            public void run()
                                                            {
                                                                Toast.makeText(ARCameraActivity.this, message, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                                dialog.dismiss();
                                                Intent i = new Intent(ARCameraActivity.this,ARStoreActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    });
                                }
                            });

                        } else if (count == 7)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Dialog dialog = new Dialog(ARCameraActivity.this);
                                    dialog.setContentView(R.layout.dialog_ar_success);
                                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                    dialog.show();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                    TextView txtContent = dialog.findViewById(R.id.tv_content);
                                    txtContent.setText("商圈特色客製圖樣醫療口罩兌換券\n請至我的優惠券查看\n(每帳號限領1份)");
                                    Button btnBack = dialog.findViewById(R.id.btn_close);

                                    btnBack.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            if (dialog != null)
                                            {
                                                ApiConnection.getCoupon("ARCOUPON2", new ApiConnection.OnConnectResultListener()
                                                {
                                                    @Override
                                                    public void onSuccess(String jsonString)
                                                    {
                                                        countNumber();
                                                    }

                                                    @Override
                                                    public void onFailure(String message)
                                                    {
                                                        runOnUiThread(new Runnable()
                                                        {
                                                            @Override
                                                            public void run()
                                                            {
                                                                Toast.makeText(ARCameraActivity.this, message, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                                dialog.dismiss();
                                                finish();
                                            }
                                        }
                                    });
                                }
                            });
                        } else
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Dialog dialog = new Dialog(ARCameraActivity.this);
                                    dialog.setContentView(R.layout.dialog_ar_collection);
                                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                    dialog.show();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                    TextView txtContent = dialog.findViewById(R.id.tv_content);
                                    txtContent.setText("繼續尋找，\n找到4個小人偶，\n獲得好禮優惠券！");
                                    Button btnBack = dialog.findViewById(R.id.btn_close);

                                    btnBack.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            if (dialog != null)
                                            {
                                                countNumber();
                                                dialog.dismiss();
                                                Intent i = new Intent(ARCameraActivity.this,ARStoreActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    } else
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Dialog dialog = new Dialog(ARCameraActivity.this);
                                dialog.setContentView(R.layout.dialog_ar_collection);
                                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                dialog.show();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                                TextView txtTitle = dialog.findViewById(R.id.tv_title);
                                TextView txtContent = dialog.findViewById(R.id.tv_content);
                                Button btnBack = dialog.findViewById(R.id.btn_close);
                                txtTitle.setText("再接再厲");
                                txtContent.setText("唉呀，被逃走了...\n可能要再靠近一些喔！");

                                btnBack.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        if (dialog != null)
                                        {
                                            dialog.dismiss();
                                            Intent i = new Intent(ARCameraActivity.this,ARStoreActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }else {
            btnCatch.setOnClickListener(v ->
            {
                Toast.makeText(this, "請划掉點點app並操作重新啟動", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void countNumber()
    {

        if (aid.equals("1") && aid != null && !b)
        {
            doll.edit()
                    .putString("isNumber1", "1")
                    .putBoolean("isStatus1", true)
                    .commit();
        }
        if (aid.equals("2") && aid != null && !b2)
        {
            doll.edit()
                    .putString("isNumber2", "2")
                    .putBoolean("isStatus2", true)
                    .commit();
        }
        if (aid.equals("3") && aid != null && !b3)
        {
            doll.edit()
                    .putString("isNumber3", "3")
                    .putBoolean("isStatus3", true)
                    .commit();
        }
        if (aid.equals("4") && aid != null && !b4)
        {
            doll.edit()
                    .putString("isNumber4", "4")
                    .putBoolean("isStatus4", true)
                    .commit();
        }
        if (aid.equals("5") && aid != null && !b5)
        {
            doll.edit()
                    .putString("isNumber5", "5")
                    .putBoolean("isStatus5", true)
                    .commit();
        }
        if (aid.equals("6") && aid != null && !b6)
        {
            doll.edit()
                    .putString("isNumber6", "6")
                    .putBoolean("isStatus6", true)
                    .commit();
        }
        if (aid.equals("7") && aid != null && !b7)
        {
            doll.edit()
                    .putString("isNumber7", "7")
                    .putBoolean("isStatus7", true)
                    .commit();
        }
        if (aid.equals("8") && aid != null && !b8)
        {
            doll.edit()
                    .putString("isNumber8", "8")
                    .putBoolean("isStatus8", true)
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
