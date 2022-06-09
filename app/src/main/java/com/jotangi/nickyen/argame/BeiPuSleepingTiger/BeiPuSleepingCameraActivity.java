package com.jotangi.nickyen.argame.BeiPuSleepingTiger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.argame.GuiHuaActivity;
import com.jotangi.nickyen.argame.GuiHuaCameraActivity;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.databinding.ActivityBeiPuSleepingCameraBinding;
import com.jotangi.nickyen.home.SalonsCheckOutActivity2;
import com.jotangi.nickyen.member.RegisterActivity;
import com.jotangi.nickyen.scanner.SurfaceViewActivity;

public class BeiPuSleepingCameraActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = getClass().getSimpleName() + "TAG";

    ActivityBeiPuSleepingCameraBinding binding;

    private BarcodeDetector barcodeDetector;
    private CameraSource ARCamera;
    boolean isRunning;
    private String barcodeScanned;
    StoreBean storeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBeiPuSleepingCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isRunning = false;

        if (getIntent() != null) {
            String s = getIntent().getStringExtra("sleeping");
            storeBean = new Gson().fromJson(s, StoreBean.class);
        }

        binding.btnBack.setOnClickListener(this);

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        ARCamera = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true).setRequestedPreviewSize(720, 480).build();

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
                    Toast.makeText(BeiPuSleepingCameraActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                if (isRunning == false) {
                    isRunning = true;
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes.size() != 0) {
                        isRunning = true;
                        barcodeScanned = barcodes.valueAt(0).displayValue;
                        Log.d(TAG, "Barcode = " + barcodeScanned);
                        if (barcodeScanned.contains("tiger")) {
                            String[] arrays = barcodeScanned.split("=");
                            String qid = arrays[1];
                            Intent intent;
                            intent = new Intent(BeiPuSleepingCameraActivity.this, ResultActivity.class);
                            intent.putExtra("qid", qid);
                            intent.putExtra("sleeping", new Gson().toJson(storeBean));
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        isRunning = false;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnBack:
                startActivity(new Intent(this,BeiPuSleepingTigerActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        isRunning = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barcodeDetector.release();
        isRunning = false;
    }
}