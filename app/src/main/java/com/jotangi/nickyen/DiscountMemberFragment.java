package com.jotangi.nickyen;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiEnqueue;
import com.jotangi.nickyen.home.adapter.CouponRecyclerAdapter;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.model.UserBean;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DiscountMemberFragment extends Fragment implements View.OnClickListener {


    private ApiEnqueue apiEnqueue;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noDataTV;
    private ImageButton btnGoBack;
    private RadioGroup radioGroup;
    private RadioButton btnAll, btnCommodity, btnMoney, btnPoint;
    private MemberCouponAdapter memberCouponAdapter;
    static ArrayList<CouponListBean> couponList = new ArrayList<>(); //可以使用
    static ArrayList<CouponListBean> couponList2 = new ArrayList<>(); //已過期或已使用
    static ArrayList<CouponListBean> couponList3 = new ArrayList<>(); //已過濾的可以使用的
    static ArrayList<CouponListBean> commodityList = new ArrayList<>();
    static ArrayList<CouponListBean> moneyList = new ArrayList<>();
    static ArrayList<CouponListBean> pointList = new ArrayList<>();

    //如果是積點折抵的進來的參數藉此辨識
    private String discount = "";
    //如果是美髮美髮結帳的確認訂單進來參數藉此辨識
    private String total = "";

    private String radioButtonType = "1";

    private String status = "1"; //tab標籤蘭

    private long systemTime;

    // value
    private String mid;

    public DiscountMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mid = getArguments().getString("mid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discount_member, container, false);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.typeRed));

        discount = getActivity().getIntent().getStringExtra("discount");
        total = getActivity().getIntent().getStringExtra("total");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        systemTime = System.currentTimeMillis();
        initView(view);
        return view;
    }

    private void initView(View v) {
        apiEnqueue = new ApiEnqueue();
        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);
        progressBar = v.findViewById(R.id.progressBar);
        noDataTV = v.findViewById(R.id.tv_noData);
        recyclerView = v.findViewById(R.id.recycler_view);
        tabLayout = v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("可使用"));
        tabLayout.addTab(tabLayout.newTab().setText("使用完畢"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (Objects.requireNonNull(tab.getText()).toString()) {
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
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        btnAll = v.findViewById(R.id.rb_all);
        btnAll.setChecked(true);
        btnCommodity = v.findViewById(R.id.rb_commodity);
        btnMoney = v.findViewById(R.id.rb_money);
        btnPoint = v.findViewById(R.id.rb_point);

        radioGroup = v.findViewById(R.id.btn_layout);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (status.equals("1")) {
                    switch (checkedId) {
                        case R.id.rb_all:
                            radioButtonType = "1";
                            layoutViews2(couponList3, discount);
                            break;
                        case R.id.rb_commodity:
                            radioButtonType = "2";
                            if (commodityList != null) {
                                layoutViews2(commodityList, discount);
                            } else {
                                recyclerView.setAdapter(null);
                            }
                            break;
                        case R.id.rb_money:
                            radioButtonType = "3";
                            if (moneyList != null) {
                                layoutViews2(moneyList, discount);
                            } else {
                                recyclerView.setAdapter(null);
                            }
                            break;
                        case R.id.rb_point:
                            radioButtonType = "4";
                            if (pointList != null) {
                                layoutViews2(pointList, discount);
                            } else {
                                recyclerView.setAdapter(null);
                            }
                            break;
                    }
                } else if (status.equals("2")) {
                    switch (checkedId) {
                        case R.id.rb_all:
                            radioButtonType = "1";
                            layoutViews2(couponList2, discount);
                            break;
                        case R.id.rb_commodity:
                            radioButtonType = "2";
                            if (commodityList != null) {
                                layoutViews2(commodityList, discount);
                            } else {
                                recyclerView.setAdapter(null);
                            }
                            break;
                        case R.id.rb_money:
                            radioButtonType = "3";
                            if (moneyList != null) {
                                layoutViews2(moneyList, discount);
                            } else {
                                recyclerView.setAdapter(null);
                            }
                            break;
                        case R.id.rb_point:
                            radioButtonType = "4";
                            if (pointList != null) {
                                layoutViews2(pointList, discount);
                            } else {
                                recyclerView.setAdapter(null);
                            }
                            break;
                    }

                }
            }
        });
        loadCouponData2("1"); //已使用
    }

    private void loadCouponData2(String s) {
        progressBar.setVisibility(View.VISIBLE);
        String sid = "";

        apiEnqueue.storeMemberCcoupon(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), sid, s, mid, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<CouponListBean>>() {
                }.getType();

                couponList2 = new Gson().fromJson(jsonString, type);
                loadCouponData("0");
            }

            @Override
            public void onFailure(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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

    private void loadCouponData(String type) {
        progressBar.setVisibility(View.VISIBLE);
        String sid = "";

        apiEnqueue.storeMemberCcoupon(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), sid, type, mid, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<CouponListBean>>() {
                }.getType();

                couponList = new Gson().fromJson(jsonString, type);
//                }
                if (couponList3 != null) {
                    couponList3.clear();
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < couponList.size(); i++) {
                    CouponListBean coupon = couponList.get(i);
                    Date date = new Date();
                    try {
                        date = simpleDateFormat.parse(coupon.getCouponEnddate());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (systemTime > date.getTime() + 86400000) {
                        couponList2.add(coupon);
                    } else {
                        couponList3.add(coupon);
                    }
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        noDataTV.setVisibility(View.INVISIBLE);
                        layoutViews(couponList3, discount);
//
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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


    private void layoutViews(ArrayList<CouponListBean> couponList2, String str) {
        if (commodityList != null) {
            commodityList.clear();
        }
        if (moneyList != null) {
            moneyList.clear();
        }
        if (pointList != null) {
            pointList.clear();
        }

        for (int i = 0; i < couponList2.size(); i++) {
            if (couponList2.get(i).getCouponType().equals("3") || couponList2.get(i).getCouponType().equals("6") || couponList2.get(i).getCouponType().equals("7")) {
                commodityList.add(couponList2.get(i));
            }
            if (couponList2.get(i).getCouponType().equals("1") || couponList2.get(i).getCouponType().equals("4")) {
                moneyList.add(couponList2.get(i));
            }
            if (couponList2.get(i).getCouponType().equals("2") || couponList2.get(i).getCouponType().equals("5")) {
                pointList.add(couponList2.get(i));
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        if (radioButtonType.equals("1")) {
            memberCouponAdapter = new MemberCouponAdapter(couponList2, requireActivity(), str, total);
        } else if (radioButtonType.equals("2")) {
            memberCouponAdapter = new MemberCouponAdapter(commodityList, requireActivity(), str, total);
        } else if (radioButtonType.equals("3")) {
            memberCouponAdapter = new MemberCouponAdapter(moneyList, requireActivity(), str, total);
        } else {
            memberCouponAdapter = new MemberCouponAdapter(pointList, requireActivity(), str, total);
        }
        recyclerView.setAdapter(memberCouponAdapter);
    }

    private void layoutViews2(ArrayList<CouponListBean> couponList2, String str) {

        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        memberCouponAdapter = new MemberCouponAdapter(couponList2, requireActivity(), str, total);
        recyclerView.setAdapter(memberCouponAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_go_back:
                Navigation.findNavController(v).popBackStack();
                break;
        }
    }
}