package com.jotangi.nickyen;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/12/6
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class LocationUtils
{
    private static LocationUtils instance;
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private Context mContext;

    private LocationUtils(Context context)
    {
        mContext = context;
        getLocation();

    }

    public static LocationUtils getInstance(Context context)
    {
        if (instance == null)
        {
            synchronized (LocationUtils.class)
            {
                if (instance == null)
                {
                    instance = new LocationUtils(context);
                }
            }
        }
        return instance;
    }

    private void getLocation()
    {
        //1.獲取位置管理器
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //2.獲取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER))
        {
            //如果是網路定位
            Log.d("kly", "如果是網路定位");
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER))
        {
            //如果是GPS定位
            Log.d("kly", "如果是GPS定位");
            locationProvider = LocationManager.GPS_PROVIDER;
        } else
        {
            Log.d("kly", "沒有可用的位置提供器");
            return;
        }
        //需要检查权限。
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null)

        {
            setLocation(location);
        } else

        {
            Log.d("kly", "location=null");
        }
        // 監聽地理位置變化，第二個和第三個參數分別為更新的最短時間minTime和最短距離minDistance
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }

    private void setLocation(Location location)
    {
        this.location = location;
        String address = "緯度：" + location.getLatitude() + "經度：" + location.getLongitude();
        Log.d("kly", "address" + address);
    }

    //獲取經緯度
    public Location showLocation()
    {
        return location;
    }

    // 移除定位監聽
    public void removeLocationUpdatesListener()
    {
        // 需要检查权限,否则编译不过
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        if (locationManager != null)
        {
            instance = null;
            locationManager.removeUpdates(locationListener);
        }
    }

    /**
     * LocationListener監聽器
     * 參數：地理位置提供器、監聽位置變化的時間間隔、位置變化的距離間隔、LocationListener監聽器
     */
    LocationListener locationListener = new LocationListener()
    {
        /**
         * 當某個位置提供者的狀態發生改變時
         /
         @Override public void onStatusChanged(String provider, int status, Bundle arg2) {
         }
         /*
          * 某個設備打開時
         /
         @Override public void onProviderEnabled(String provider) {}
         /*
          * 某個設備關閉時
         /
         @Override public void onProviderDisabled(String provider) {}
         /*
          * 手機位置發生變動
         */
        @Override
        public void onLocationChanged(Location location)
        {
            location.getAccuracy();//精确度
            setLocation(location);
        }
    };

    // 獲取地址信息
    public static List<Address> getAddress(Context context, Location location)
    {
        List<Address> result = null;
        try
        {
            if (location != null)
            {
                Geocoder gc = new Geocoder(context, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}


