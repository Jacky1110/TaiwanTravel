package com.jotangi.nickyen.argame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.base.BaseActivity;
import com.jotangi.nickyen.model.UserBean;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ARStoreActivity extends BaseActivity
{
    private FrameLayout btnBack;
    private List<StoreBean> storeList = new ArrayList<>();

    public static JSONArray jsonData;

    public static ArrayList<StoreBean> storeBean;
    private StoreBean storeData;
    private ImageView btnImg1, btnImg2, btnImg3, btnImg4, btnImg5, btnImg6, btnImg7, btnImg8;
    private SharedPreferences doll;
    private boolean b, b2, b3, b4, b5, b6, b7, b8;

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_store);

        btnBack = findViewById(R.id.btnARBack);
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        btnImg1 = findViewById(R.id.btnImg1);
        btnImg2 = findViewById(R.id.btnImg2);
        btnImg3 = findViewById(R.id.btnImg3);
        btnImg4 = findViewById(R.id.btnImg4);
        btnImg5 = findViewById(R.id.btnImg5);
        btnImg6 = findViewById(R.id.btnImg6);
        btnImg7 = findViewById(R.id.btnImg7);
        btnImg8 = findViewById(R.id.btnImg8);
        btnImg1.setOnClickListener(this::onClick);
        btnImg2.setOnClickListener(this::onClick);
        btnImg3.setOnClickListener(this::onClick);
        btnImg4.setOnClickListener(this::onClick);
        btnImg5.setOnClickListener(this::onClick);
        btnImg6.setOnClickListener(this::onClick);
        btnImg7.setOnClickListener(this::onClick);
        btnImg8.setOnClickListener(this::onClick);

        doll = getSharedPreferences("doll", MODE_PRIVATE);
        b = doll.getBoolean("isStatus1", false);
        b2 = doll.getBoolean("isStatus2", false);
        b3 = doll.getBoolean("isStatus3", false);
        b4 = doll.getBoolean("isStatus4", false);
        b5 = doll.getBoolean("isStatus5", false);
        b6 = doll.getBoolean("isStatus6", false);
        b7 = doll.getBoolean("isStatus7", false);
        b8 = doll.getBoolean("isStatus8", false);

        requestStore();
//        getARShopList();
    }

    private void parseJson(String json)
    {

        storeBean = new ArrayList<>();

        try
        {
            jsonData = new JSONArray(json);
            for (int i = 0; i < jsonData.length(); i++)
            {
                JSONObject jsonObject = (JSONObject) jsonData.get(i);
                storeData = new StoreBean();
                storeData.setAid(jsonObject.getString("aid"));
                storeData.setAr_name(jsonObject.getString("ar_name"));
                storeData.setAr_address(jsonObject.getString("ar_address"));
                storeData.setAr_picture(jsonObject.getString("ar_picture"));
                storeData.setAr_latitude(jsonObject.getString("ar_latitude"));
                storeData.setAr_longitude(jsonObject.getString("ar_longitude"));
                storeData.setAr_descript(jsonObject.getString("ar_descript"));
                storeData.setArDescript2(jsonObject.getString("ar_descript2"));
                storeBean.add(storeData);

            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private void requestStore()
    {

        RequestBody body = null;
        try
        {
            body = new FormBody.Builder()
                    .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                    .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                    .build();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(ApiConstant.API_URL + "ar_info.php")
                .post(body)
                .build();

//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(ApiConnection.createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();
        if (client == null)
            client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(ARStoreActivity.this, "網路出問題了!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {

                String json = response.body().string();
                Log.d("OKHttp", "json=" + json);
                parseJson(json);

            }
        });

    }

    private void getARShopList()
    {

        String la, lo;
        if (MainActivity.myLocation == null)
        {
            la = "";
            lo = "";
        } else
        {
            la = String.valueOf(MainActivity.myLocation.getLatitude());
            lo = String.valueOf(MainActivity.myLocation.getLongitude());
        }
        ApiConnection.getARShopInfo("", new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                final Type type = new TypeToken<ArrayList<StoreBean>>()
                {
                }.getType();
                storeList = new Gson().fromJson(jsonString, type);

            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

    private void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnImg1:
//                if (b)
//                {
//                    makeToastTextAndShow("您已領取過", 3500);
//                    return;
//                } else
//                {
                    Intent shop1 = new Intent(this, ARFindActivity.class);
                    shop1.putExtra("pass", "1");
                    startActivity(shop1);
                    finish();
//                }
                break;
            case R.id.btnImg2:
//                if (b2)
//                {
//                    makeToastTextAndShow("您已領取過", 3500);
//                    return;
//                } else
//                {
                    Intent shop2 = new Intent(this, ARFindActivity.class);
                    shop2.putExtra("pass", "2");
                    startActivity(shop2);
                    finish();
//                }
                break;
            case R.id.btnImg3:
//                if (b3)
//                {
//                    makeToastTextAndShow("您已領取過", 3500);
//                    return;
//                } else
//                {
                    Intent shop3 = new Intent(this, ARFindActivity.class);
                    shop3.putExtra("pass", "3");
                    startActivity(shop3);
                    finish();
//                }
                break;
            case R.id.btnImg4:
//                if (b4)
//                {
//                    makeToastTextAndShow("您已領取過", 3500);
//                    return;
//                } else
//                {
                    Intent shop4 = new Intent(this, ARFindActivity.class);
                    shop4.putExtra("pass", "4");
                    startActivity(shop4);
                    finish();
//                }
                break;
            case R.id.btnImg5:
//                if (b5)
//                {
//                    makeToastTextAndShow("您已領取過", 3500);
//                    return;
//                } else
//                {
                    Intent shop5 = new Intent(this, ARFindActivity.class);
                    shop5.putExtra("pass", "5");
                    startActivity(shop5);
                    finish();
//                }
                break;
            case R.id.btnImg6:
//                if (b6)
//                {
//                    makeToastTextAndShow("您已領取過", 3500);
//                    return;
//                } else
//                {
                    Intent shop6 = new Intent(this, ARFindActivity.class);
                    shop6.putExtra("pass", "6");
                    startActivity(shop6);
                    finish();
//                }
                break;
            case R.id.btnImg7:
//                if (b7)
//                {
//                    makeToastTextAndShow("您已領取過", 3500);
//                    return;
//                } else
//                {
                    Intent shop7 = new Intent(this, ARFindActivity.class);
                    shop7.putExtra("pass", "7");
                    startActivity(shop7);
                    finish();
//                }
                break;
            case R.id.btnImg8:
//                if (b8)
//                {
//                    makeToastTextAndShow("您已領取過", 3500);
//                    return;
//                } else
//                {
                    Intent shop8 = new Intent(this, ARFindActivity.class);
                    shop8.putExtra("pass", "8");
                    startActivity(shop8);
                    finish();
//                }
                break;
        }
    }
}