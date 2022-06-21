package com.jotangi.nickyen.merch.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.api.ApiEnqueue;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.home.notify.NotifyActivity;
import com.jotangi.nickyen.home.notify.NotifyModel;
import com.jotangi.nickyen.merch.model.MerchMemberInfoBean;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.model.ShopBean;
import com.jotangi.nickyen.model.UserBean;
import com.jotangi.nickyen.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.jar.JarException;

//import static com.jotangi.nickyen.merch.MerchMainActivity.notifyChangePage;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/23
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class MerchHomeFragment extends BaseFragment {
    private String TAG = getClass().getSimpleName() + "TAG";

    //UI
    TextView txtStoreName, tvDay, tvWeek, tvMonth;
    ImageView imgContent, btnNotify, imgRemind;
    ApiEnqueue apiEnqueue;

    ConstraintLayout btnCheck, btnExchange, btnSetting, btnRecord, btnReserve, btnIndustryReserve, btnReconciliation, btnManagement, btnMemberSetting;

    private NavController controller;
    private ArrayList<MerchMemberInfoBean> memberInfoArrayList = new ArrayList<>();

    public MerchHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merch_home, container, false);

        try {
            getMemberInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView(view);
        getNotify();
        getMemberCount();
        return view;
    }

    private void initView(View v) {
        apiEnqueue = new ApiEnqueue();
        btnNotify = v.findViewById(R.id.iv_notify);
        btnNotify.setOnClickListener(this);
        imgRemind = v.findViewById(R.id.iv_remind);
        txtStoreName = v.findViewById(R.id.tv_store_name);
        imgContent = v.findViewById(R.id.iv_store);
        tvDay = v.findViewById(R.id.tv_day_member);
        tvWeek = v.findViewById(R.id.tv_week_member);
        tvMonth = v.findViewById(R.id.tv_month_member);


        //結帳核銷
        btnCheck = v.findViewById(R.id.layout_check);
        btnCheck.setOnClickListener(this);
        //商品兌換
        btnExchange = v.findViewById(R.id.layout_exchange);
        btnExchange.setOnClickListener(this);
        //會員管理
        btnManagement = v.findViewById(R.id.layout_manage);
        btnManagement.setOnClickListener(this);
        //交易紀錄
        btnRecord = v.findViewById(R.id.layout_record);
        btnRecord.setOnClickListener(this);
        //預約記錄
        btnReserve = v.findViewById(R.id.layoutReserve);
        btnReserve.setOnClickListener(this);
        // 體驗記錄
        btnIndustryReserve = v.findViewById(R.id.layoutIndustryReserve);
        btnIndustryReserve.setOnClickListener(this);
        // 對帳系統
        btnReconciliation = v.findViewById(R.id.layoutReconciliation);
        btnReconciliation.setOnClickListener(this);
        // 店家設定
        btnMemberSetting = v.findViewById(R.id.layout_MemberSetting);
        btnMemberSetting.setOnClickListener(this);
        //系統設定
        btnSetting = v.findViewById(R.id.layout_setting);
        btnSetting.setOnClickListener(this);
    }

    private void getNotify() {
        ApiConnection.getPushList(new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                new SharedPreferencesUtil(getActivity(), "notify");
                String info = (String) SharedPreferencesUtil.getData("info", "");
                if (info != null && !info.equals("")) {
                    Type type = new TypeToken<ArrayList<NotifyModel>>() {
                    }.getType();
                    ArrayList<NotifyModel> old = new ArrayList<>();
                    old = new Gson().fromJson(info, type);
                    ArrayList<NotifyModel> newD = new ArrayList<>();
                    newD = new Gson().fromJson(jsonString, type);

                    if (newD.size() > old.size()) {
                        if (getActivity() == null) {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imgRemind.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
                SharedPreferencesUtil.putData("info", new Gson().toJson(jsonString));
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void getMemberInfo() {
        ApiConnection.getMerchMemberInfo(new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<MerchMemberInfoBean>>() {
                }.getType();
                memberInfoArrayList = new Gson().fromJson(jsonString, type);
                if (memberInfoArrayList.get(0) != null) {
                    getStoreDescript(memberInfoArrayList.get(0).getMemberSid());
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    //取得店家描述
    private void getStoreDescript(String sid) {
        ApiConnection.getStoreDescript(sid, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<ShopBean>>() {
                }.getType();
                ArrayList<ShopBean> shopBean2 = new Gson().fromJson(jsonString, type);
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtStoreName.setText(shopBean2.get(0).getStoreName());
                        UserBean.MerchName = shopBean2.get(0).getStoreName();
                        PicassoTrustAll.getInstance(getContext()).load(ApiConstant.API_IMAGE + shopBean2.get(0).getStorePicture()).into(imgContent);
//                        changePage();
                    }
                });
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        notifyChangePage ="";
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.iv_notify:
                imgRemind.setVisibility(View.GONE);
                NotifyActivity.start(getActivity(), "merch");
                break;
            case R.id.layout_check:
                if (!memberInfoArrayList.isEmpty()) {
                    bundle.putString("sid", memberInfoArrayList.get(0).getMemberSid());
                    bundle.putString("name", AppUtility.DecryptAES2(memberInfoArrayList.get(0).getMemberName()));
                    controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_merchHomeFragment_to_merchInputFragment, bundle);
                }
                break;
            case R.id.layout_exchange:
                bundle.putString("sid", memberInfoArrayList.get(0).getMemberSid());
                controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_merchHomeFragment_to_merchScanFragment, bundle);
                break;
            case R.id.layout_manage:
                controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_merchHomeFragment_to_memberManagementFragment);
                break;

            case R.id.layout_record:
                controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_merchHomeFragment_to_merchRecordFragment);
                break;
            case R.id.layoutReserve:
                if (memberInfoArrayList.get(0).getMemberSid().equals("81") || memberInfoArrayList.get(0).getMemberSid().equals("82")
                        || memberInfoArrayList.get(0).getMemberSid().equals("83") || memberInfoArrayList.get(0).getMemberSid().equals("84")
                        || memberInfoArrayList.get(0).getMemberSid().equals("85") || memberInfoArrayList.get(0).getMemberSid().equals("86")
                        || memberInfoArrayList.get(0).getMemberSid().equals("87") || memberInfoArrayList.get(0).getMemberSid().equals("126")) {
                    controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_merchHomeFragment_to_reserveFragment);
                } else {
                    Toast.makeText(getActivity(), "該店家目前無此權限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layoutIndustryReserve:
//                if (memberInfoArrayList.get(0).getMemberSid().equals("28") || memberInfoArrayList.get(0).getMemberSid().equals("32"))
                if (memberInfoArrayList.get(0).getMemberSid().equals("34") || memberInfoArrayList.get(0).getMemberSid().equals("35") || memberInfoArrayList.get(0).getMemberSid().equals("127")) {
                    controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_merchHomeFragment_to_industryReserveFragment);
                } else {
                    Toast.makeText(getActivity(), "該店家目前無此權限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layoutReconciliation:
                controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_merchHomeFragment_to_reconciliationFragment);
                break;
            case R.id.layout_MemberSetting:
                controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_merchHomeFragment_to_memberProductFragment);
                break;
            case R.id.layout_setting:
                controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_merchHomeFragment_to_merchSettingFragment);
                break;
        }
    }

    private void getMemberCount() {
        apiEnqueue.StoreMemberCount(new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                requireActivity().runOnUiThread(()->{
                    try {
                        JSONArray jsonArray = new JSONArray(message);
                        Log.d(TAG, "jsonArray: " + jsonArray);
                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                        Log.d(TAG, "jsonObject: " + jsonObject);
                        MemberInfoBean.day = jsonObject.getString("day");
                        MemberInfoBean.week = jsonObject.getString("week");
                        MemberInfoBean.month = jsonObject.getString("month");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    tvDay.setText(MemberInfoBean.day);
                    tvWeek.setText(MemberInfoBean.week);
                    tvMonth.setText(MemberInfoBean.month);


                });


            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

//    private void changePage()
//    {
//        if (notifyChangePage != null && notifyChangePage.equals("industryReserve"))
//        {
//            btnIndustryReserve.performClick();
//        }
//    }
}
