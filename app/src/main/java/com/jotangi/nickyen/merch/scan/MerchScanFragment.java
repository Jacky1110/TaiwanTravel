package com.jotangi.nickyen.merch.scan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.DialogIOSUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.merch.model.MerchCoupon;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/7/1
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class MerchScanFragment extends BaseFragment
{
    //UI
    private Button btnClose;

    SurfaceView cameraView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    private String barcodeScanned;
    String sid;

    boolean isRunning;
    boolean isDialog;
    boolean isApi;

    private NavController controller;

    private ArrayList<MerchCoupon> merchCoupons = new ArrayList<>();

    class ApiCatch extends Thread
    {
        private String path;

        public ApiCatch(String path)
        {
            this.path = path;
        }

        @Override
        public void run()
        {
//            if (path.contains("bonus_point") && path!=null){
//
//                String[] arrays = path.split("=|&");
//                String mid = arrays[1];
//                String coupon = arrays[3];
//                String bonus = arrays[5];
//
//                isApi = true;
//                checkCoupon(mid, coupon, bonus);
//
//            }
//            else
            if (path != null && path.contains("spot://"))
            {
                //可能會多次連線會有多次要規避多次拿取api
                String[] arrays = path.split("=|&");
                String mid = arrays[1];
                String coupon = arrays[3];
                if (!isApi && coupon != null)
                {
                    isApi = true;
                    checkCoupon(mid, coupon, "0");
                }

            } else
            {
                Message message2 = new Message();
                message2.what = 3;
                MyHandler.sendMessage(message2);
            }
        }
    }

    public MerchScanFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_merch_scan, container, false);

        sid = getArguments().getString("sid", "");
        isRunning = true;
        initView(view);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isRunning = true;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        barcodeDetector.release();
        isRunning = false;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        barcodeDetector.release();
        isRunning = false;
    }

    private void initView(View view)
    {
        btnClose = view.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> Navigation.findNavController(getView()).popBackStack());

        cameraView = view.findViewById(R.id.surfaceView);

        barcodeDetector =
                new BarcodeDetector.Builder(getContext())
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        cameraSource = new CameraSource
                .Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder)
            {
                try
                {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie)
                {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height)
            {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder)
            {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>()
        {
            @Override
            public void release()
            {

            }

            @Override
            public void receiveDetections(@NonNull @NotNull Detector.Detections<Barcode> detections)
            {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0)
                {
                    barcodeScanned = barcodes.valueAt(0).displayValue;
                    final ApiCatch qrcode_apiCatch = new ApiCatch(barcodeScanned);
                    qrcode_apiCatch.start();
//                    Log.d("Barcode", barcodeScanned);
                }
            }
        });
    }

    public Handler MyHandler = new Handler(Looper.myLooper())
    {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg)
        {
            cameraSource.stop();
            switch (msg.what)
            {
                case 1:
                    String[] separated = msg.obj.toString().split("=");
                    String title = separated[0];
                    String mid = separated[1];
                    String couponNo = separated[2];
                    if (!isDialog)
                    {
                        isDialog = true;
                        DialogIOSUtility.showMyDialog(getActivity(), title, "是否核銷此優惠券", "確認核銷", "取消", new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                isDialog = false;
                                applyCoupon(mid, couponNo);
                            }

                            @Override
                            public void onCancel()
                            {
                                isDialog = false;
                                new Thread()
                                {
                                    @Override
                                    public void run()
                                    {
                                        super.run();
                                        try
                                        {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        Navigation.findNavController(getView()).popBackStack();
                                        isDialog = false;
                                    }
                                }.start();
                            }
                        });
                    }
                    break;
                case 2:
                    if (!isDialog)
                    {
                        isDialog = true;
                        DialogIOSUtility.showMyDialog(getActivity(), "核銷失敗", "掃碼失敗，核銷代碼已被使用。", "確認", null, new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                new Thread()
                                {
                                    @Override
                                    public void run()
                                    {
                                        super.run();
                                        try
                                        {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        Navigation.findNavController(getView()).popBackStack();
                                        isDialog = false;
                                    }
                                }.start();
                            }

                            @Override
                            public void onCancel()
                            {

                            }
                        });
                    }
                    break;
                case 3:
                    if (!isDialog)
                    {
                        isDialog = true;
                        DialogIOSUtility.showMyDialog(getActivity(), "核銷失敗", "掃碼失敗，核銷代碼有誤。", "確認", null, new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                new Thread()
                                {
                                    @Override
                                    public void run()
                                    {
                                        super.run();
                                        try
                                        {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        Navigation.findNavController(getView()).popBackStack();
                                        isDialog = false;
                                    }
                                }.start();
                            }

                            @Override
                            public void onCancel()
                            {

                            }
                        });
                    }
                    break;
                case 4:
                    if (!isDialog)
                    {
                        isDialog = true;
                        DialogIOSUtility.showMyDialog(getActivity(), "", "已核銷！", "確認", null, new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                new Thread()
                                {
                                    @Override
                                    public void run()
                                    {
                                        super.run();
                                        try
                                        {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        Navigation.findNavController(getView()).popBackStack();
                                        isDialog = false;
                                    }
                                }.start();
                            }

                            @Override
                            public void onCancel()
                            {

                            }
                        });
                    }
                    break;
                case 5:
                    if (!isDialog)
                    {
                        isDialog = true;
                        DialogIOSUtility.showMyDialog(getActivity(), "核銷失敗", "優惠券類型必須為平台註冊禮或是入會禮類型", "確認", null, new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                new Thread()
                                {
                                    @Override
                                    public void run()
                                    {
                                        super.run();
                                        try
                                        {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        Navigation.findNavController(getView()).popBackStack();
                                        isDialog = false;
                                    }
                                }.start();

                            }

                            @Override
                            public void onCancel()
                            {

                            }
                        });
                    }
                    break;
            }
        }
    };

    private void checkCoupon(String mid, String couponNo, String bonus)
    {
        ApiConnection.checkCoupon(couponNo, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<MerchCoupon>>()
                {
                }.getType();
                merchCoupons = new Gson().fromJson(jsonString, type);
                Message message2 = new Message();
                if (merchCoupons.get(0).getUsingFlag().equals("0") && (merchCoupons.get(0).getCouponType().equals("1") || merchCoupons.get(0).getCouponType().equals("2") || merchCoupons.get(0).getCouponType().equals("4")) || merchCoupons.get(0).getCouponType().equals("5"))
                {
                    message2.what = 5;
                    message2.obj = merchCoupons.get(0).getCouponName() + "=" + mid + "=" + couponNo + "=" + bonus;

                } else if (merchCoupons.get(0).getUsingFlag().equals("0") && (merchCoupons.get(0).getCouponType().equals("3") || merchCoupons.get(0).getCouponType().equals("6"))|| merchCoupons.get(0).getCouponType().equals("7"))
                {
                    message2.what = 1;
                    message2.obj = merchCoupons.get(0).getCouponName() + "=" + mid + "=" + couponNo;

                } else
                {
                    message2.what = 2;
                }
                MyHandler.sendMessage(message2);
                isApi = false;
            }

            @Override
            public void onFailure(String message)
            {
                Message message2 = new Message();
                message2.what = 3;
                MyHandler.sendMessage(message2);
                isApi = false;
            }
        });
    }

    private void applyCoupon(String mid, String couponNo)
    {
        ApiConnection.applyCoupon(mid, couponNo, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Message message2 = new Message();
                message2.what = 4;
                MyHandler.sendMessage(message2);
            }

            @Override
            public void onFailure(String message)
            {
                Message message2 = new Message();
                message2.what = 3;
                MyHandler.sendMessage(message2);
            }
        });
    }
}
