package com.jotangi.nickyen.merch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.jotangi.nickyen.R;

public class MerchMainActivity extends AppCompatActivity
{
    private static final int CAMERA_PERMISSION_CODE = 100;
//    public static String notifyChangePage ="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merch_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        checkPermission();
//        processExtraData();
    }

    private void checkPermission()
    {
        checkCamera(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }

    public void checkCamera(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED)
        {

            // Requesting the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
        }
//        else
//        {
//            Toast.makeText(this,
//                    "Permission already granted",
//                    Toast.LENGTH_SHORT)
//                    .show();
//        }
    }

//    private void processExtraData()
//    {
//        Intent intent = getIntent();
//        if (intent != null)
//        {
//            notifyChangePage = intent.getStringExtra("notification");
//            Log.e("豪豪", "mProcessExtraData: " + intent.getStringExtra("notification"));
//        }
//    }
//
//    @Override
//    protected void onDestroy()
//    {
//        super.onDestroy();
//        notifyChangePage="";
//    }
//
    private long timeSave = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Step02-判斷是否按下按鍵，並且確認該按鍵是否為返回鍵:
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            // Step03-判斷目前時間與上次按下返回鍵時間是否間隔2000毫秒(2秒):
            if ((System.currentTimeMillis() - timeSave) > 2000)
            {
                Toast.makeText(this, "再按一次結束此應用!!", Toast.LENGTH_SHORT).show();
                // Step04-紀錄第一次案返回鍵的時間:
                timeSave = System.currentTimeMillis();
            } else
            {
                // Step05-結束Activity與關閉APP:
                finish();
                Log.d("退出", "onKeyDown: ");
//                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 重寫getResources()方法，讓APP的字體不受系統設置字體大小影響
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}