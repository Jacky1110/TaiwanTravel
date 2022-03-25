package com.jotangi.nickyen.scanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.home.SalonsCheckOutActivity;
import com.jotangi.nickyen.home.SalonsCheckOutActivity2;
import com.jotangi.nickyen.member.RegisterActivity;

public class SurfaceViewActivity extends AppCompatActivity
{
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;

    boolean isRunning;
    private String barcodeScanned;
    Button btnClose;

    String memberType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        isRunning = false;

        memberType = getIntent().getStringExtra("member_type");
        Log.d("豪豪", "onCreate: " + memberType);

        surfaceView = findViewById(R.id.surfaceView);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true).setRequestedPreviewSize(640, 480).build();

        btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                finish();
//                Intent intent = new Intent(SurfaceViewActivity.this, MainActivity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

            }
        });

        //加入事件到surfaceView
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback()
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
                    cameraSource.start(surfaceView.getHolder());

                } catch (Exception e)
                {
                    Toast.makeText(SurfaceViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2)
            {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder)
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
            public void receiveDetections(Detector.Detections<Barcode> detections)
            {

                if (isRunning == false)
                {
                    isRunning = true;
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes.size() != 0)
                    {
                        isRunning = true;
                        //cameraSource.stop();
                        barcodeScanned = barcodes.valueAt(0).displayValue;
                        Log.d("豪豪", "Barcode = " + barcodeScanned);
                        //cameraSource.stop();
                        Intent intent;
                        //這是掃入會禮
                        if (barcodeScanned.contains("tid") && memberType == null)
                        {
                            String[] arrays = barcodeScanned.split("=");
                            String tId = arrays[1];
                            Log.d("豪豪", "onCreate: " + tId);
                            intent = new Intent();
                            intent.putExtra("tId", tId);
                            setResult(RESULT_OK, intent);
                        }
                        //這是走店家註冊
                        if (memberType != null)
                        {
                            String[] arrays = barcodeScanned.split("=");
                            String tId = arrays[1];
                            intent = new Intent(SurfaceViewActivity.this, RegisterActivity.class);
                            intent.putExtra("barcodeScanned", barcodeScanned);
                            intent.putExtra("member_type", "1");
                            intent.putExtra("tId", tId);
                            startActivity(intent);
                        }
                        //餐飲業
                        if (barcodeScanned.contains("s_id") && memberType == null)
                        {
                            String[] arrays = barcodeScanned.split("=|&");
                            for (String s : arrays)
                            {
                                Log.d("豪豪", "receiveDetections: " + s);
                            }
                            String sid = arrays[1];
                            String storeName = arrays[3];
                            String total = arrays[5];
//
                            intent = new Intent(SurfaceViewActivity.this, SalonsCheckOutActivity2.class);
                            intent.putExtra("sid", sid);
                            intent.putExtra("storeName", storeName);
                            intent.putExtra("total", total);
                            intent.putExtra("bookingNo", "");
                            intent.putExtra("designer", "");
                            intent.putExtra("service", "");
                            intent.putExtra("eat", "1");
                            startActivity(intent);
                        }
                        //美容美髮預約記錄
                        if (barcodeScanned.contains("booking_no") && !barcodeScanned.contains("eat") && memberType == null && !barcodeScanned.contains("program_name"))
                        {
                            String[] arrays = barcodeScanned.split("=|&");
                            String sid = arrays[1];
                            String storeName = arrays[3];
                            String total = arrays[5];
                            String bookingNo = arrays[7];
                            String designer = arrays[9];
                            String service = arrays[11];
                            String memberID = arrays[13];
                            intent = new Intent(SurfaceViewActivity.this, SalonsCheckOutActivity2.class);
                            intent.putExtra("sid", sid);
                            intent.putExtra("storeName", storeName);
                            intent.putExtra("total", total);
                            intent.putExtra("bookingNo", bookingNo);
                            intent.putExtra("designer", designer);
                            intent.putExtra("service", service);
                            intent.putExtra("memberID", memberID);
                            intent.putExtra("page", "salon");
                            startActivity(intent);
                        }
                        if (barcodeScanned.contains("program_name"))
                        {
                            String[] arrays = barcodeScanned.split("=|&");
                            String sid = arrays[1];
                            String storeName = arrays[3];
                            String total = arrays[5];
                            String bookingNo = arrays[7];
                            String memberID = arrays[9];
                            intent = new Intent(SurfaceViewActivity.this, SalonsCheckOutActivity2.class);
                            intent.putExtra("sid", sid);
                            intent.putExtra("storeName", storeName);
                            intent.putExtra("total", total);
                            intent.putExtra("bookingNo", bookingNo);
                            intent.putExtra("memberID", memberID);
                            intent.putExtra("page", "industry");
                            startActivity(intent);
                        }

                        finish();
                    } else
                    {
                        isRunning = false;
                    }
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        isRunning = false;
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        barcodeDetector.release();
        isRunning = false;
    }
}
