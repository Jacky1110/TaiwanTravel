package com.jotangi.nickyen.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.home.adapter.CouponRecyclerAdapter;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.model.UserBean;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MyDiscountNew2Activity extends AppCompatActivity
{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noDataTV;
    static ArrayList<CouponListBean> couponList = new ArrayList<>(); //可以使用
    static ArrayList<CouponListBean> couponList2 = new ArrayList<>(); //已過期或已使用
    static ArrayList<CouponListBean> couponList3 = new ArrayList<>(); //已過濾的可以使用的
    static ArrayList<CouponListBean> commodityList = new ArrayList<>();
    static ArrayList<CouponListBean> moneyList = new ArrayList<>();
    static ArrayList<CouponListBean> pointList = new ArrayList<>();
    private CouponRecyclerAdapter couponAdapter;
    private RadioGroup radioGroup;
    private RadioButton btnAll, btnCommodity, btnMoney, btnPoint;

    //如果是積點折抵的進來的參數藉此辨識
    private String discount = "";
    //如果是美髮美髮結帳的確認訂單進來參數藉此辨識
    private String total = "";

    private String radioButtonType = "1";

    private String status = "1"; //tab標籤蘭

    private long systemTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_discount_new);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        discount = getIntent().getStringExtra("discount");
        total = getIntent().getStringExtra("total");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        systemTime = System.currentTimeMillis();

        initView();
    }

    private void initView()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        progressBar = findViewById(R.id.progressBar);
        noDataTV = findViewById(R.id.tv_noData);
        recyclerView = findViewById(R.id.recycler_view);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("可使用"));
        tabLayout.addTab(tabLayout.newTab().setText("使用完畢"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                switch (Objects.requireNonNull(tab.getText()).toString())
                {
                    case "可使用":
//                        loadCouponData("0");
                        layoutViews(couponList3, discount);
                        status = "1";
                        break;
                    case "使用完畢":
//                        loadCouponData("1");
                        layoutViews(couponList2, discount);
                        status = "2";
                        break;
                    default:
                        recyclerView.setAdapter(null);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
        btnAll = findViewById(R.id.rb_all);
        btnAll.setChecked(true);
        btnCommodity = findViewById(R.id.rb_commodity);
        btnMoney = findViewById(R.id.rb_money);
        btnPoint = findViewById(R.id.rb_point);

        radioGroup = findViewById(R.id.btn_layout);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (status.equals("1"))
                {
                    switch (checkedId)
                    {
                        case R.id.rb_all:
                            radioButtonType = "1";
                            layoutViews2(couponList3, discount);
                            break;
                        case R.id.rb_commodity:
                            radioButtonType = "2";
                            if (commodityList != null)
                            {
                                layoutViews2(commodityList, discount);
                            } else
                            {
                                recyclerView.setAdapter(null);
                            }
                            break;
                        case R.id.rb_money:
                            radioButtonType = "3";
                            if (moneyList != null)
                            {
                                layoutViews2(moneyList, discount);
                            } else
                            {
                                recyclerView.setAdapter(null);
                            }
                            break;
                        case R.id.rb_point:
                            radioButtonType = "4";
                            if (pointList != null)
                            {
                                layoutViews2(pointList, discount);
                            } else
                            {
                                recyclerView.setAdapter(null);
                            }
                            break;
                    }
                } else if (status.equals("2"))
                {
                    switch (checkedId)
                    {
                        case R.id.rb_all:
                            radioButtonType = "1";
                            layoutViews2(couponList2, discount);
                            break;
                        case R.id.rb_commodity:
                            radioButtonType = "2";
                            if (commodityList != null)
                            {
                                layoutViews2(commodityList, discount);
                            } else
                            {
                                recyclerView.setAdapter(null);
                            }
                            break;
                        case R.id.rb_money:
                            radioButtonType = "3";
                            if (moneyList != null)
                            {
                                layoutViews2(moneyList, discount);
                            } else
                            {
                                recyclerView.setAdapter(null);
                            }
                            break;
                        case R.id.rb_point:
                            radioButtonType = "4";
                            if (pointList != null)
                            {
                                layoutViews2(pointList, discount);
                            } else
                            {
                                recyclerView.setAdapter(null);
                            }
                            break;
                    }

                }
            }
        });
        loadCouponData2("1"); //已使用
    }

    private void loadCouponData(String type)
    {
        if (UserBean.member_id.equals(""))
        {
            noDataTV.setVisibility(View.VISIBLE);
            noDataTV.setText("請先登入帳號");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        String sid = "";

        ApiConnection.getMyCouponList(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), sid, type, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<CouponListBean>>()
                {
                }.getType();

//                couponList = new ArrayList<>();
                couponList = new Gson().fromJson(jsonString, type);
//                commodityList = new ArrayList<>();
//                moneyList = new ArrayList<>();
//                pointList = new ArrayList<>();
//                if (couponList2 !=null){
//                    couponList2.clear();
//                }
                if (couponList3 != null)
                {
                    couponList3.clear();
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < couponList.size(); i++)
                {
                    CouponListBean coupon = couponList.get(i);
                    Date date = new Date();
                    try
                    {
                        date = simpleDateFormat.parse(coupon.getCouponEnddate());
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    if (systemTime > date.getTime()+86400000)
                    {
                        couponList2.add(coupon);
                    }else
                    {
                        couponList3.add(coupon);
                    }
                }
                Log.d("安安", "群1: " + couponList.size());
                Log.d("安安", "群1-: " + couponList2.size());
                Log.d("安安", "群1=: " + couponList3.size());
//                Log.d("安安", "c:" + commodityList.size() + "\n" + "m:" + moneyList.size() + "\n" + "p:" + pointList.size());
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        noDataTV.setVisibility(View.INVISIBLE);
                        layoutViews(couponList3, discount);
//                        if (radioButtonType.equals("1"))
//                        {
//                            layoutViews(couponList, discount);
//                        } else if (radioButtonType.equals("2"))
//                        {
//                            if (commodityList != null)
//                            {
//                                layoutViews(commodityList, discount);
//                            } else
//                            {
//
//                                recyclerView.setAdapter(null);
//                            }
//                        } else if (radioButtonType.equals("3"))
//                        {
//                            if (moneyList != null)
//                            {
//                                layoutViews(moneyList, discount);
//                            } else
//                            {
//
//                                recyclerView.setAdapter(null);
//                            }
//                        } else if (radioButtonType.equals("4"))
//                        {
//                            if (pointList != null)
//                            {
//                                layoutViews(pointList, discount);
//                            } else
//                            {
//
//                                recyclerView.setAdapter(null);
//                            }
//                        }
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        noDataTV.setVisibility(View.VISIBLE);
                        noDataTV.setText(R.string.non_ticket);
                        if (commodityList != null)
                            couponList.clear();
                        if (commodityList != null)
                            commodityList.clear();
                        if (moneyList != null)
                            moneyList.clear();
                        if (pointList != null)
                            pointList.clear();
                        recyclerView.setAdapter(null);
                    }
                });
            }
        });
    }

    private void loadCouponData2(String s)
    {
        if (UserBean.member_id.equals(""))
        {
            noDataTV.setVisibility(View.VISIBLE);
            noDataTV.setText("請先登入帳號");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        String sid = "";

        ApiConnection.getMyCouponList(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), sid, s, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<CouponListBean>>()
                {
                }.getType();

                couponList2 = new Gson().fromJson(jsonString, type);
                loadCouponData("0");
//                commodityList = new ArrayList<>();
//                moneyList = new ArrayList<>();
//                pointList = new ArrayList<>();
//                for (int i = 0; i < couponList.size(); i++)
//                {
//                    CouponListBean coupon = couponList.get(i);
//                    if (coupon.getCouponType().equals("3") || coupon.getCouponType().equals("6") || coupon.getCouponType().equals("7"))
//                    {
//                        commodityList.add(coupon);
//                    }
//                    if (coupon.getCouponType().equals("2") || coupon.getCouponType().equals("5"))
//                    {
//                        moneyList.add(coupon);
//                    }
//                    if (coupon.getCouponType().equals("1") || coupon.getCouponType().equals("4"))
//                    {
//                        pointList.add(coupon);
//                    }
//                }
//                Log.d("安安", "c:" + commodityList.size() + "\n" + "m:" + moneyList.size() + "\n" + "p:" + pointList.size());
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        progressBar.setVisibility(View.INVISIBLE);
//                        noDataTV.setVisibility(View.INVISIBLE);
//                        if (radioButtonType.equals("1"))
//                        {
//                            layoutViews(couponList, discount);
//                        } else if (radioButtonType.equals("2"))
//                        {
//                            if (commodityList != null)
//                            {
//                                layoutViews(commodityList, discount);
//                            } else
//                            {
//
//                                recyclerView.setAdapter(null);
//                            }
//                        } else if (radioButtonType.equals("3"))
//                        {
//                            if (moneyList != null)
//                            {
//                                layoutViews(moneyList, discount);
//                            } else
//                            {
//
//                                recyclerView.setAdapter(null);
//                            }
//                        } else if (radioButtonType.equals("4"))
//                        {
//                            if (pointList != null)
//                            {
//                                layoutViews(pointList, discount);
//                            } else
//                            {
//
//                                recyclerView.setAdapter(null);
//                            }
//                        }
//                    }
//                });
            }

            @Override
            public void onFailure(String message)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        noDataTV.setVisibility(View.VISIBLE);
                        noDataTV.setText(R.string.non_ticket);
                        if (commodityList != null)
                            couponList.clear();
                        if (commodityList != null)
                            commodityList.clear();
                        if (moneyList != null)
                            moneyList.clear();
                        if (pointList != null)
                            pointList.clear();
                        recyclerView.setAdapter(null);
                    }
                });
                loadCouponData("0");
            }
        });
    }

    private void layoutViews(ArrayList<CouponListBean> couponList2, String str)
    {
//        commodityList = new ArrayList<>();
//        moneyList = new ArrayList<>();
//        pointList = new ArrayList<>();
        if (commodityList != null)
        {
            commodityList.clear();
        }
        if (moneyList != null)
        {
            moneyList.clear();
        }
        if (pointList != null)
        {
            pointList.clear();
        }

        for (int i = 0; i < couponList2.size(); i++)
        {
            if (couponList2.get(i).getCouponType().equals("3") || couponList2.get(i).getCouponType().equals("6") || couponList2.get(i).getCouponType().equals("7"))
            {
                commodityList.add(couponList2.get(i));
            }
            if (couponList2.get(i).getCouponType().equals("1") || couponList2.get(i).getCouponType().equals("4"))
            {
                moneyList.add(couponList2.get(i));
            }
            if (couponList2.get(i).getCouponType().equals("2") || couponList2.get(i).getCouponType().equals("5"))
            {
                pointList.add(couponList2.get(i));
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (radioButtonType.equals("1"))
        {
            couponAdapter = new CouponRecyclerAdapter(couponList2, this, str, total);
        } else if (radioButtonType.equals("2"))
        {
            couponAdapter = new CouponRecyclerAdapter(commodityList, this, str, total);
        } else if (radioButtonType.equals("3"))
        {
            couponAdapter = new CouponRecyclerAdapter(moneyList, this, str, total);
        } else
        {
            couponAdapter = new CouponRecyclerAdapter(pointList, this, str, total);
        }
        recyclerView.setAdapter(couponAdapter);
    }

    private void layoutViews2(ArrayList<CouponListBean> couponList2, String str)
    {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        couponAdapter = new CouponRecyclerAdapter(couponList2, this, str, total);
        recyclerView.setAdapter(couponAdapter);
    }
}