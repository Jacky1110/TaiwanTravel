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
import androidx.fragment.app.Fragment;
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
public class MerchScanCheckFragment extends Fragment
{
    //UI
    private Button btnClose;

    View view;
    SurfaceView cameraView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    private String barcodeScanned;
    private String sid;
    private String total;
    private String name;

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
            if (path != null && path.contains("bonus_point"))
            {

                String[] arrays = path.split("=|&");
                String mid = arrays[1];
                String coupon = arrays[3];
                String bonus = arrays[5];

                if (coupon.equals("0"))
                {
                    coupon = "";
                    Message message2 = new Message();
                    message2.what = 6;
                    message2.obj = mid + "=" + bonus;
                    Log.d("店長", "run: ");
                    MyHandler.sendMessage(message2);
                } else
                {

                    checkCoupon(mid, coupon, bonus);
                    isApi = true;
                }

            } else if (path != null && path.contains("spot://"))
            {
                //可能會多次連線會有多次要規避多次拿取api
                String[] arrays = path.split("=|&");
                String mid = arrays[1];
                String coupon = arrays[3];
                if (coupon.equals("0"))
                {
                    mid = "";
                    Message message2 = new Message();
                    message2.what = 1;
                    message2.obj = mid + "=" + "0";
                }
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

    public MerchScanCheckFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_merch_scan_check, container, false);

        if (getArguments() != null)
        {
            sid = getArguments().getString("sid", "");
            total = getArguments().getString("total", "");
            name = getArguments().getString("name", "");
        }
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
        btnClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_merchScanCheckFragment_to_merchHomeFragment);
            }
        });

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
                    isApi = false;
                    barcodeScanned = barcodes.valueAt(0).displayValue;
                    ApiCatch qrcode_apiCatch = new ApiCatch(barcodeScanned);
                    qrcode_apiCatch.start();
                    Log.d("Barcode", barcodeScanned);
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
                    String mid = separated[0];
                    String bonus = separated[1];
                    Bundle bundle = new Bundle();
                    if (!isDialog)
                    {
                        isDialog = true;
                        DialogIOSUtility.showMyDialog(getActivity(), "是否核銷此優惠券", "", "確認核銷", "取消", new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                bundle.putString("sid", sid);
                                bundle.putString("mid", mid);
                                bundle.putString("total", total);
                                bundle.putString("bonus", bonus);
                                bundle.putString("name", name);
                                bundle.putString("coupon", new Gson().toJson(merchCoupons.get(0)));
                                controller = Navigation.findNavController(getView());
                                controller.navigate(R.id.action_merchScanCheckFragment_to_merchResultFragment, bundle);
                                isDialog = false;
                            }

                            @Override
                            public void onCancel()
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
                                        controller = Navigation.findNavController(getView());
                                        controller.navigate(R.id.action_merchScanCheckFragment_to_merchHomeFragment);
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
                                        controller = Navigation.findNavController(getView());
                                        controller.navigate(R.id.action_merchScanCheckFragment_to_merchHomeFragment);
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
                                        controller = Navigation.findNavController(getView());
                                        controller.navigate(R.id.action_merchScanCheckFragment_to_merchHomeFragment);
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
                        DialogIOSUtility.showMyDialog(getActivity(), "核銷失敗", "優惠券類型必須為點數折抵類型", "確認", null, new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
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
                                        controller = Navigation.findNavController(getView());
                                        controller.navigate(R.id.action_merchScanCheckFragment_to_merchHomeFragment);
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
                case 6:
                    String[] separated2 = msg.obj.toString().split("=");
                    String mid2 = separated2[0];
                    String bonus2 = separated2[1];
                    Bundle bundle2 = new Bundle();
                    if (!isDialog)
                    {
//                        cameraSource.stop();
                        isDialog = true;
                        DialogIOSUtility.showMyDialog(getActivity(), "是否核銷此優惠券", "", "確認核銷", "取消", new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                isDialog = false;
                                bundle2.putString("sid", sid);
                                bundle2.putString("mid", mid2);
                                bundle2.putString("total", total);
                                bundle2.putString("bonus", bonus2);
                                bundle2.putString("name", name);
                                bundle2.putString("coupon", "0");

                                controller = Navigation.findNavController(getView());
                                controller.navigate(R.id.action_merchScanCheckFragment_to_merchResultFragment, bundle2);
                            }

                            @Override
                            public void onCancel()
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
                                        controller = Navigation.findNavController(getView());
                                        controller.navigate(R.id.action_merchScanCheckFragment_to_merchHomeFragment);
                                        isDialog = false;
                                    }
                                }.start();
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
                    message2.what = 1;
                    message2.obj = mid + "=" + bonus;

                } else if (merchCoupons.get(0).getUsingFlag().equals("0") && (merchCoupons.get(0).getCouponType().equals("3") || merchCoupons.get(0).getCouponType().equals("6")))
                {
                    message2.what = 5;
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

}
