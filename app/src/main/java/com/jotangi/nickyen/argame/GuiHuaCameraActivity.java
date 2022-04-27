package com.jotangi.nickyen.argame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.LocationUtils;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.databinding.ActivityGuiHuaCameraBinding;
import com.jotangi.nickyen.home.MyDiscountNew2Activity;
import com.jotangi.nickyen.home.model.CouponListBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GuiHuaCameraActivity extends AppCompatActivity
{
    private ActivityGuiHuaCameraBinding binding;
    private BarcodeDetector barcodeDetector;
    private CameraSource ARCamera;
    private StoreBean storeBean;
    private int ans; // 經緯度距離
    private String mLon = "";
    private String mLat = "";
    private String lat = "";
    private String lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityGuiHuaCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String s = getIntent().getStringExtra("guiHua");
        storeBean = new Gson().fromJson(s, StoreBean.class);
        Log.d("豪豪", "onCreate: " + storeBean);

        // == 測試工具類 ==
        Location location = LocationUtils.getInstance(this).showLocation();
        if (location != null)

        {
            Log.d("location", "高度" + location.getAltitude() + "緯度" + location.getLatitude() + "經度" + location.getLongitude() + "城市" + LocationUtils.getAddress(this,location));
        }
        
        // == 測試工具類 ==

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
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        ARCamera = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true).setRequestedPreviewSize(720, 480).build();

        binding.ARSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback()
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
                    ARCamera.start(binding.ARSurfaceView.getHolder());

                } catch (Exception e)
                {
                    Toast.makeText(GuiHuaCameraActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

        if (!mLat.isEmpty() && !mLon.isEmpty() && !lat.isEmpty() && !lon.isEmpty())
        {
            ans = Integer.valueOf(AppUtility.getDistance(mLat, mLon, lat, lon));

            binding.btnCatch.setOnClickListener(v ->
            {
                Dialog dialog = new Dialog(GuiHuaCameraActivity.this);
                if (ans < 50)
                {
                    fetchCoupon();
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
                    txtContent.setText("唉呀，\n沒有捕捉到景點畫面...\n可能要再靠近一些喔！");
                    btnBack.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (dialog != null)
                            {
                                dialog.dismiss();
                                Intent i = new Intent(GuiHuaCameraActivity.this, GuiHuaActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                }
            });
        } else
        {
            binding.btnCatch.setOnClickListener(v ->
            {
                Toast.makeText(this, "請划掉點點app並操作重新啟動", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private int couponCount = 0;
    private String[] arGuiHua = {
            "G1001", "G1002", "G1003", "G1004", "G1005", "G1006", "G1007"
            , "G1008", "G1009", "G1010", "G1011", "G1012", "G1013", "G1014", "G1015", "G1016"
            , "G1017", "G1018", "G1019", "G1020", "G1021", "G1022", "G1023", "G1024", "G1025"
            , "G1026", "G1027", "G1028", "G1029", "G1030", "G1031", "G1032", "G1033"};

//    private String[] arGuiHua = {
//            "G1001", "G1002", "G1003", "G1004"};

    private void fetchCoupon()
    {
        if (arGuiHua.length > couponCount)
        {
            getCoupon(arGuiHua[couponCount]);
            couponCount++;
        } else
        {
            dialogGetCouponFinish("", false);
        }
    }

    private void getCoupon(String couponID)
    {
        ApiConnection.getCoupon(couponID, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                ArrayList<CouponListBean> couponListBeans = new ArrayList<>();
                try
                {
                    String couponInfo = new JSONObject(jsonString).getString("coupon_info");
                    couponListBeans = new Gson().fromJson(couponInfo, new TypeToken<ArrayList<CouponListBean>>()
                    {
                    }.getType());
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                ArrayList<CouponListBean> finalCouponListBeans = couponListBeans;
                runOnUiThread(() ->
                {
                    dialogGetCouponFinish(finalCouponListBeans.get(0).getCouponName(), true);
                });
            }

            @Override
            public void onFailure(String message)
            {
                runOnUiThread(() ->
                {
                    fetchCoupon();
                });
            }
        });
    }

    private void dialogGetCouponFinish(String couponName, boolean isCoupon)
    {
        Dialog dialog2 = new Dialog(GuiHuaCameraActivity.this);
        dialog2.setContentView(R.layout.dialog_ar_success);
        dialog2.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog2.show();
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        TextView txtTitle = dialog2.findViewById(R.id.tv_title);
        TextView txtContent = dialog2.findViewById(R.id.tv_content);
        Button btnBack = dialog2.findViewById(R.id.btn_close);
        if (isCoupon)
        {
            txtContent.setText(couponName + "好禮兌換券\n請至我的優惠券查看\n(每帳號限領一份)");
            btnBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (dialog2 != null)
                    {
                        dialog2.dismiss();
                        startActivity(new Intent(GuiHuaCameraActivity.this, MyDiscountNew2Activity.class));
                        finish();
                    }
                }
            });
        } else
        {
            txtTitle.setText("Oops!");
            txtContent.setText("票券都領過啦，\n快去\"\"優惠券\"\"查看吧");
            btnBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (dialog2 != null)
                    {
                        dialog2.dismiss();

                        finish();
                    }
                }
            });
        }
    }
}