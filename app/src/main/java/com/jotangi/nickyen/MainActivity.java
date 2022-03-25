package com.jotangi.nickyen;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.discount.DiscountFragment;
import com.jotangi.nickyen.home.HomeFragment;
import com.jotangi.nickyen.home.PointDiscountFragment;
import com.jotangi.nickyen.member.LoginActivity;
import com.jotangi.nickyen.member.MemberFragment;
import com.jotangi.nickyen.merch.MerchMainActivity;
import com.jotangi.nickyen.model.UserBean;
import com.jotangi.nickyen.parking.ParkingFragment;
import com.jotangi.nickyen.shop.ShopNewActivity;
import com.jotangi.nickyen.utils.NavigationController;
import com.jotangi.nickyen.utils.PageNavigationView;
import com.jotangi.nickyen.utils.item.BaseTabItem;
import com.jotangi.nickyen.utils.item.SpecialTab;
import com.jotangi.nickyen.utils.item.SpecialTabRound;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.jotangi.nickyen.shop.ShopNewActivity.pages;

public class MainActivity extends AppCompatActivity
{
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 223;

    //TODO 客戶反應字體大小能不能固定，所以有些先改dp，大部分都還是是sp，有遇到在幫改或是新增項目在記得textSize要dp

    ImageView imageView;
    PageNavigationView pagerNavigationView;

    public static LocationManager mLocationManager;
    public static Location myLocation;
    String notifyChangePage =""; // 接收fcm推播換頁參數

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //狀態通知欄顏色
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        //這是地圖初始化為地圖的狀態
        ShopNewActivity.status = true;
        //這是地圖儲存的頁籤
        pages = 0;

        //TODO 之後要做fcm點選切換頁面，尚未做完
        processExtraData(); // 處理通知欄帶的參數

        SharedPreferences user = getSharedPreferences("loginInfo", MODE_PRIVATE);

        boolean signed = user.getBoolean("isLogin", false);
        checkPermission();
        requestPermission();

        if (signed == true)
        {
            UserBean.member_id = user.getString("account", "");
            UserBean.member_pwd = user.getString("password", "");

            //店長app自動登入
            try
            {
                if (AppUtility.DecryptAES2(UserBean.member_id) != null && AppUtility.DecryptAES2(UserBean.member_id).length() == 8)
                {
                    Intent intent = new Intent(this, MerchMainActivity.class);
//                    intent.putExtra("notification",notifyChangePage);
                    startActivity(intent);
                    finish();
//                    Log.e("豪豪", "跳: " + intent.getStringExtra("notification"));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            //開啟APP時若有註冊資料則直接跳到登入頁面，這一頁會finish掉
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        imageView = findViewById(R.id.image);
        pagerNavigationView = findViewById(R.id.pagerNavigationView);

        NavigationController navigationController = pagerNavigationView.custom()
                .addItem(newItem(R.drawable.ic_new_home_big, R.drawable.ic_new_home_red, "首頁"))
                .addItem(newItem(R.drawable.ic_new_discount, R.drawable.ic_new_discount_red, "好康推薦"))
                .addItem(newRoundItem(R.drawable.ic_tab2, R.drawable.ic_tab2, ""))
                .addItem(newItem(R.drawable.ic_parking_small, R.drawable.ic_new_parking_red, "停車資訊"))
                .addItem(newItem(R.drawable.ic_new_account, R.drawable.ic_new_account_red, "會員專區"))
                .build();


        ViewPager viewPager = findViewById(R.id.noTouchViewPager);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new HomeFragment());
        fragments.add(new DiscountFragment());
        fragments.add(new PointDiscountFragment());
        fragments.add(new ParkingFragment());
        fragments.add(new MemberFragment());
        viewPager.setAdapter(new BaseViewPagerAdapter(getSupportFragmentManager(), navigationController.getItemCount(), fragments));

        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(viewPager);
        String s = AppUtility.getIPAddress(this);
        Log.d("豪豪", "IP位址:" + s);

        //推播Token更新
        if (!AppUtility.DecryptAES2(UserBean.member_id).isEmpty() || !AppUtility.DecryptAES2(UserBean.member_pwd).isEmpty())
            login();

//        if (notifyChangePage!=null && notifyChangePage.equals("industryRecord")){
//            startActivity(new Intent(this, IndustryRecordActivity.class));
//        }
    }

    private void login()
    {
        ApiConnection.login(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(final String jsonString)
            {
                Log.d("ManActivity", "onSuccess: " + jsonString);
            }

            @Override
            public void onFailure(final String message)
            {
                Log.d("ManActivity", "onFailure: " + message);
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        AppUtility.showMyDialog(MainActivity.this, "登入會員有誤，請重新登入", "確定", null, new AppUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                logout();
                            }

                            @Override
                            public void onCancel()
                            {

                            }
                        });
                    }
                });
            }
        });
    }

    private void logout()
    {
        String account = null;
        String pwd = null;
        try
        {
            account = AppUtility.DecryptAES2(UserBean.member_id);
            pwd = AppUtility.DecryptAES2(UserBean.member_pwd);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        ApiConnection.logout(account, pwd, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                SharedPreferences preferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                //北埔 刪除尋寶紀錄
                SharedPreferences preferences2 = getSharedPreferences("doll", MODE_PRIVATE);
                preferences2.edit().clear().commit();
                //金三角 刪除尋寶紀錄
                SharedPreferences preferences3 = getSharedPreferences("triangle", MODE_PRIVATE);
                preferences3.edit().clear().commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message)
            {
                SharedPreferences preferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                //北埔 刪除尋寶紀錄
                SharedPreferences preferences2 = getSharedPreferences("doll", MODE_PRIVATE);
                preferences2.edit().clear().commit();
                //金三角 刪除尋寶紀錄
                SharedPreferences preferences3 = getSharedPreferences("triangle", MODE_PRIVATE);
                preferences3.edit().clear().commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkPermission()
    {
        checkCamera(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }

    private void checkCamera(String permission, int requestCode)
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

    private void requestPermission()
    {

        if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            String message = "『旅行點點 ddot』將使用您的位置資訊，僅用於定位您與店家的相對位置。";
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    AppUtility.showMyDialog(MainActivity.this, message, "了解", "", new AppUtility.OnBtnClickListener()
                    {
                        @Override
                        public void onCheck()
                        {


                            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    ACCESS_FINE_LOCATION))
                            {

                                Log.e("TAG", "這裡是說明");
                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.

                                //使用者拒絕允許位置取用
                                //TODO:再次請求允許

                            } else
                            {

                                //跳出請求彈窗
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        }

                        @Override
                        public void onCancel()
                        {

                        }
                    });
                }
            });

        } else
        {
            // Permission has already been granted
            setMyLocationManager();
        }
    }

    private void setMyLocationManager()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
//        mMap.getUiSettings().setMapToolbarEnabled(false);

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location bestLocation = null;
        for (String provider : providers)
        {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null)
            {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy())
            {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        myLocation = bestLocation;

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {

//                Log.d(TAG, "onLocationChanged");
                myLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

//                Log.d(TAG, "onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String provider)
            {

//                Log.d(TAG, "onProviderEnabled");
                Toast.makeText(MainActivity.this, "已開啟定位", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProviderDisabled(String provider)
            {

//                Log.d(TAG, "onProviderDisabled");
//                showLocationStatue("您的定位未啟用");

            }
        });
    }

    private void showLocationStatue(String status)
    {

        AppUtility.showMyDialog(this, status, "確定", "", new AppUtility.OnBtnClickListener()
        {
            @Override
            public void onCheck()
            {

            }

            @Override
            public void onCancel()
            {

            }
        });

    }

    /**
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text)
    {
        SpecialTab mainTab = new SpecialTab(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xFF888888);
        mainTab.setTextCheckedColor(0xFFE70012);
        return mainTab;
    }

    /**
     * 圆形tab
     */
    private BaseTabItem newRoundItem(int drawable, int checkedDrawable, String text)
    {
        SpecialTabRound mainTab = new SpecialTabRound(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xFF888888);
        mainTab.setTextCheckedColor(0xFF009688);
        return mainTab;
    }

    private static class BaseViewPagerAdapter extends FragmentPagerAdapter
    {
        private int size;
        private List<Fragment> fragments;

        public BaseViewPagerAdapter(@NonNull FragmentManager fm, int size, List<Fragment> fragments)
        {
            super(fm);
            this.size = size;
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position)
        {
            return fragments.get(position);
        }

        @Override
        public int getCount()
        {
            return size;
        }
    }

    public void setMenuHide(boolean setHide)
    {
        if (setHide) //隱藏導航欄
        {
            imageView.setVisibility(View.INVISIBLE);
            pagerNavigationView.setVisibility(View.INVISIBLE);
        } else
        {
            imageView.setVisibility(View.VISIBLE);
            pagerNavigationView.setVisibility(View.VISIBLE);
        }
    }

    // Step01-設定目前時間變數(使用long是因為System.currentTimeMillis()方法的型態是long):
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
                ShopNewActivity.status = true;
                //這是地圖儲存的頁籤
                pages = 0;
                finish();
                Log.d("退出", "onKeyDown: ");
//                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(AppUtility.appDialog != null)
        AppUtility.appDialog.dismiss();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processExtraData();
//        Log.e("豪豪", "onNewIntent: " + intent.getExtras().getString("notification"));
    }
    private void processExtraData()
    {
        Intent intent = getIntent();
//        if (intent != null)
//        {
//            notifyChangePage = intent.getStringExtra("notification");
//            Log.e("豪豪", "processExtraData: " + intent.getStringExtra("notification"));
//
//        }
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
