package com.jotangi.nickyen;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DiscountMemberFragment extends Fragment implements View.OnClickListener {

    private String TAG = DiscountMemberFragment.class.getSimpleName() + "(TAG)";

    private ApiEnqueue apiEnqueue;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noDataTV;
    private ImageButton btnGoBack;
    private MemberCouponAdapter memberCouponAdapter;
    ArrayList data = new ArrayList();
    static ArrayList<MemberCouponMode> couponList = new ArrayList<>(); //可以使用
    static ArrayList<MemberCouponMode> couponList2 = new ArrayList<>(); //已過期或已使用
    static ArrayList<MemberCouponMode> couponList3 = new ArrayList<>(); //已過濾的可以使用的

    //如果是積點折抵的進來的參數藉此辨識
    private String discount = "";
    //如果是美髮美髮結帳的確認訂單進來參數藉此辨識
    private String total = "";

    private String radioButtonType = "1";

    private String status = "1"; //tab標籤蘭

    private long systemTime = System.currentTimeMillis();

    // value
    private String mid;
    private String use;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));

        tabLayout = v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("可使用"));
        tabLayout.addTab(tabLayout.newTab().setText("使用完畢"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (Objects.requireNonNull(tab.getText()).toString()) {
                    case "可使用":
                        loadCouponData();
//                        layoutViews(couponList3, discount);
                        status = "1";
                        break;
                    case "使用完畢":
                        loadCouponData2();
//                        layoutViews(couponList2, discount);
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
    }

    @Override
    public void onStart() {
        super.onStart();
        loadCouponData();
    }

    //可使用
    private void loadCouponData() {
        progressBar.setVisibility(View.VISIBLE);

        apiEnqueue.storeMemberCcoupon(mid, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        noDataTV.setVisibility(View.GONE);

                        couponList = new ArrayList();

                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            Log.d(TAG, "jsonArray: " + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MemberCouponMode model = new MemberCouponMode();
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                model.title = jsonObject.getString("coupon_name");
                                model.content = jsonObject.getString("coupon_description");
                                model.startDate = jsonObject.getString("coupon_startdate");
                                model.endDate = jsonObject.getString("coupon_enddate");
                                model.pic = jsonObject.getString("coupon_picture");
                                model.rule = jsonObject.getString("coupon_rule");
                                model.type = jsonObject.getString("coupon_type");
                                model.discount = jsonObject.getString("coupon_discount");
                                model.amount = jsonObject.getString("discount_amount");
                                model.using = jsonObject.getString("using_flag");
                                couponList.add(model);
                            }

                            if (couponList3 != null) {

                                couponList3.clear();
                            }

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            for (int i = 0; i < couponList.size(); i++) {
                                MemberCouponMode coupon = couponList.get(i);
                                Date date = new Date();
                                try {
                                    date = simpleDateFormat.parse(coupon.endDate);
                                    Log.d(TAG, "date: " + date);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (coupon.using.equals("0") && systemTime < date.getTime() + 86400000) {
                                    couponList3.add(coupon);
                                }
//
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    memberCouponAdapter = new MemberCouponAdapter();
                                    memberCouponAdapter.setmData(couponList3);
                                    recyclerView.setAdapter(memberCouponAdapter);
                                }
                            });
                            if (couponList3.isEmpty()){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        noDataTV.setVisibility(View.VISIBLE);
                                        recyclerView.setAdapter(null);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    noDataTV.setVisibility(View.VISIBLE);
                                    recyclerView.setAdapter(null);
                                }
                            });

                        }
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
                        recyclerView.setAdapter(null);
                    }
                });
            }
        });
    }

    //已使用
    private void loadCouponData2() {
        progressBar.setVisibility(View.VISIBLE);

        apiEnqueue.storeMemberCcoupon(mid, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        noDataTV.setVisibility(View.GONE);

                        couponList = new ArrayList();

                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            Log.d(TAG, "jsonArray: " + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MemberCouponMode model = new MemberCouponMode();
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                model.title = jsonObject.getString("coupon_name");
                                model.content = jsonObject.getString("coupon_description");
                                model.startDate = jsonObject.getString("coupon_startdate");
                                model.endDate = jsonObject.getString("coupon_enddate");
                                model.pic = jsonObject.getString("coupon_picture");
                                model.rule = jsonObject.getString("coupon_rule");
                                model.type = jsonObject.getString("coupon_type");
                                model.discount = jsonObject.getString("coupon_discount");
                                model.amount = jsonObject.getString("discount_amount");
                                model.using = jsonObject.getString("using_flag");
                                couponList.add(model);
                            }

                            if (couponList2 != null){
                                couponList2.clear();
                            }

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            for (int i = 0; i < couponList.size(); i++) {
                                MemberCouponMode coupon = couponList.get(i);
                                Date date = new Date();
                                try {
                                    date = simpleDateFormat.parse(coupon.endDate);
                                    Log.d(TAG, "date: " + date);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (coupon.using.equals("1") || systemTime > date.getTime() + 86400000) {
                                    couponList2.add(coupon);
                                }
//
                            }


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    memberCouponAdapter = new MemberCouponAdapter();
                                    memberCouponAdapter.setmData(couponList2);
                                    recyclerView.setAdapter(memberCouponAdapter);
                                }
                            });
                            if (couponList2.isEmpty()){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        noDataTV.setVisibility(View.VISIBLE);
                                        recyclerView.setAdapter(null);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    noDataTV.setVisibility(View.VISIBLE);
                                    recyclerView.setAdapter(null);
                                }
                            });

                        }
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
                        recyclerView.setAdapter(null);
                    }
                });
            }
        });
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