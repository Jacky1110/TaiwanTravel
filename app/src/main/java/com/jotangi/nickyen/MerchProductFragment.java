package com.jotangi.nickyen;

import static com.jotangi.nickyen.api.ApiConstant.API_IMAGE;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.model.MemberBean;
import com.jotangi.nickyen.model.ShopBean;
import com.jotangi.nickyen.shop.ProductFragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MerchProductFragment extends BaseFragment implements View.OnClickListener {

    static ShopBean shopData;
    static MemberBean memberBeanData;
    static boolean isAddMemberGift;
    static String s = ""; //判斷是否從home進來隱藏底部導航欄 1是會隱藏
    //UI
    private ImageButton btnGoBack;

    //資訊區塊內的控件
    private TextView txtStoreName, txtStoreAddress, txtStoreTel, txtStoreBusinessTime, txtStoreDescript, txtGlobe, txtFb;
    private Button btnReserve, btnAddMember;

    private ImageView productView, imgGlobe, imgFb, imgClock, imgPoint;
    private FirebaseAnalytics mFirebaseAnalytics; //firebaseGA分析


    static ArrayList<CouponListBean> couponList;
    static ArrayList<ShopBean> shopBean2 = new ArrayList<>();

    private ArrayList<MemberBean> memberBeans = new ArrayList<>();

    public MerchProductFragment() {
        // Required empty public constructor
    }

    //創建Fragment實例，接收參數
    public static MerchProductFragment newInstance(ShopBean data, String str) {
        MerchProductFragment fragment = new MerchProductFragment();
        Bundle args = new Bundle();
        shopData = data;
        s = str;
        fragment.setArguments(args);
        return fragment;
    }

    public static MerchProductFragment newInstance2(MemberBean memberBean, String str) {
        MerchProductFragment fragment = new MerchProductFragment();
        Bundle args = new Bundle();
        memberBeanData = memberBean;
        s = str;
        fragment.setArguments(args);
        return fragment;
    }

    public static MerchProductFragment newInstance3(MemberBean memberBean, boolean b, ArrayList<CouponListBean> coupon) {
        MerchProductFragment fragment = new MerchProductFragment();
        Bundle args = new Bundle();
        memberBeanData = memberBean;
        isAddMemberGift = b;
        couponList = coupon;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merch_product, container, false);
        if (s.equals("1") && s != null)
            ((MainActivity) getActivity()).setMenuHide(true);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        initView(view);
        return view;
    }

    private void initView(View v) {
        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);
        productView = v.findViewById(R.id.iv_product);
        String imageUrl = API_IMAGE + shopData.getStorePicture();
        PicassoTrustAll.getInstance(getContext()).load(imageUrl).into(productView);

        //店家資訊區塊控件
        txtStoreName = v.findViewById(R.id.tv_info_store_name);
        txtStoreAddress = v.findViewById(R.id.tv_info_store_address);
        txtStoreTel = v.findViewById(R.id.tv_info_store_tel);
        txtStoreBusinessTime = v.findViewById(R.id.tv_info_store_business_time);
        txtStoreDescript = v.findViewById(R.id.tv_info_store_descript);
        txtGlobe = v.findViewById(R.id.tv_globe);
        txtFb = v.findViewById(R.id.tv_fb);
        imgGlobe = v.findViewById(R.id.iv_info_globe);
        imgFb = v.findViewById(R.id.iv_info_fb);
        imgClock = v.findViewById(R.id.iv_info_clock);

        txtStoreName.setText(shopData.getStoreName());
        txtStoreAddress.setText(shopData.getStoreAddress());
        txtStoreAddress.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下劃線
        txtStoreAddress.getPaint().setAntiAlias(true);//抗鋸齒
        txtStoreAddress.setOnClickListener(this);

        txtStoreTel.setText(shopData.getStorePhone());
        txtStoreTel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下劃線
        txtStoreTel.getPaint().setAntiAlias(true);//抗鋸齒
        txtStoreTel.setOnClickListener(this);
    }
}