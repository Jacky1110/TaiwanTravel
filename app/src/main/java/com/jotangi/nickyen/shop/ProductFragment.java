package com.jotangi.nickyen.shop;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.beautySalons.CheckDesignerFragment;
import com.jotangi.nickyen.home.MyDiscountNew2Activity;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.model.MemberBean;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.model.ShopBean;
import com.jotangi.nickyen.model.UserBean;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.jotangi.nickyen.api.ApiConstant.API_IMAGE;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/10
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class ProductFragment extends BaseFragment implements View.OnClickListener
{
    static ShopBean shopData;
    static MemberBean memberBeanData;
    static boolean isAddMemberGift;
    static String s = ""; //判斷是否從home進來隱藏底部導航欄 1是會隱藏
    //UI
    private ImageButton btnGoBack;
    private ImageView productView, imgGlobe, imgFb, imgClock, imgPoint;
    private TabLayout tabLayout;

    private FirebaseAnalytics mFirebaseAnalytics; //firebaseGA分析

    //店家資訊區塊 點數區塊 優惠券區塊
    private ConstraintLayout infoView, pointView, ticketView;
    //資訊區塊內的控件
    private TextView txtStoreName, txtStoreAddress, txtStoreTel, txtStoreBusinessTime, txtStoreDescript, txtGlobe, txtFb;
    private Button btnReserve, btnAddMember;
    //優惠圈區塊控件
    private RecyclerView ticketRecycler;
    private TicketAdapter ticketAdapter;
    private TextView txtNoData;

    static ArrayList<CouponListBean> couponList;
    static ArrayList<ShopBean> shopBean2 = new ArrayList<>();

    private ArrayList<MemberBean> memberBeans = new ArrayList<>();

    public ProductFragment()
    {
        // Required empty public constructor
    }

    //創建Fragment實例，接收參數
    public static ProductFragment newInstance(ShopBean data, String str)
    {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        shopData = data;
        s = str;
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductFragment newInstance2(MemberBean memberBean, String str)
    {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        memberBeanData = memberBean;
        s = str;
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductFragment newInstance3(MemberBean memberBean, boolean b, ArrayList<CouponListBean> coupon)
    {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        memberBeanData = memberBean;
        isAddMemberGift = b;
        couponList = coupon;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_product_new, container, false);
//        viewModel = new ViewModelProvider(this).get(MemberCardFragmentViewModel.class);
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_card,container,false);
//        binding.setData(viewModel);
//        binding.setLifecycleOwner(this);
        if (s.equals("1") && s != null)
            ((MainActivity) getActivity()).setMenuHide(true);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
//        ((MainActivity) getActivity()).setMenuHide(true);
        //若是一般會員沒掃碼好像沒sid當主鍵拿到商店資料  掃碼完拿取tid會得到sid再丟到storeInfo去拿描述
        //會有兩種狀況發生一種是從shopActivity進來，那這樣資料是用傳遞的shopData
        //另一種是藉由掃碼完確認是會員直接從tid得到sid進來用的是shopBean2
        //遇到爛團隊就是喜歡突然在一個時間內大改專案跟很晚才出Api，PM又亂搞規格沒規劃...我褲子都脫一半了
        if (shopData == null)
        {
            if (null != memberBeanData.getSid())
                getStoreNewDescript(memberBeanData.getSid(), view);
        } else
        {
            try
            {
                initView(view);
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        return view;
    }

    private void isMemberStatus(String storeID)
    {
        ApiConnection.isMemberStatus(storeID, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                if (getActivity() != null)
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                Object obj = new JSONTokener(jsonString).nextValue();
                                if (obj instanceof JSONArray)
                                {
                                    btnAddMember.setClickable(false);
                                    btnAddMember.setBackgroundResource(R.color.black);
                                    btnAddMember.setText("您是該店家的會員呦");

                                } else if (obj instanceof JSONObject)
                                {
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    String code = jsonObject.getString("code");
                                    String storeInfo = jsonObject.getString("storeinfo");
                                    Type type = new TypeToken<ArrayList<MemberBean>>()
                                    {
                                    }.getType();
                                    memberBeans = new Gson().fromJson(storeInfo, type);
                                    if (code.equals("0x0201"))
                                    {
                                        btnAddMember.setBackgroundResource(R.color.typeRed);
                                        btnAddMember.setText("加入會員");
                                    }
                                }
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

    private void initView(View v) throws UnsupportedEncodingException
    {
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

        if (shopData.getStoreWebsite() != null && shopData.getStoreWebsite().length() != 0)
        {
            txtGlobe.setText(shopData.getStoreWebsite());
            txtGlobe.setVisibility(View.VISIBLE);
            imgGlobe.setVisibility(View.VISIBLE);
            txtGlobe.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            txtGlobe.getPaint().setAntiAlias(true);
            txtGlobe.setOnClickListener(this);
        } else
        {
            txtGlobe.setVisibility(View.GONE);
            imgGlobe.setVisibility(View.GONE);
        }

        if (shopData.getStoreFacebook() != null && shopData.getStoreFacebook().length() != 0)
        {
            txtFb.setText(shopData.getStoreFacebook());
            txtFb.setVisibility(View.VISIBLE);
            imgFb.setVisibility(View.VISIBLE);
            txtFb.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            txtFb.getPaint().setAntiAlias(true);
            txtFb.setOnClickListener(this);
        } else
        {
            txtFb.setVisibility(View.GONE);
            imgFb.setVisibility(View.GONE);
        }
        infoView = v.findViewById(R.id.layout_info);
        pointView = v.findViewById(R.id.layout_point);
        imgPoint = v.findViewById(R.id.iv_point);

        if (shopData.getStoreName() != null && shopData.getStoreName().length() != 0)
        {
            //可能會改名用sid
            switch (shopData.getSid())
            {
                case "122": //七彩雲南（龍潭店）
                case "123": //七彩雲南（金陵店）
                case "124": //七彩雲南（桃園店）
                case "17": //阿美米干
                case "21": //版納傣味
                case "18": //小云滇
                case "22": //阿美金三角點心店
                case "19": //七彩雲南 (忠貞店)
                case "20": //七彩雲南 (八德店)
                case "24": //癮食聖堂
                case "31": //冰獨
                case "34": //阿美米干1981
                case "23": //八妹婆婆民族創藝過橋米線
//                    imgPoint.setBackgroundResource(R.drawable.bg_point2);
//                    break;
                default:
                    imgPoint.setImageResource(R.drawable.bg_point1);
                    break;
            }
        }

        ticketView = v.findViewById(R.id.layout_ticket);

        tabLayout = v.findViewById(R.id.tab_layout);
        ticketRecycler = v.findViewById(R.id.recycle_discount);
        txtNoData = v.findViewById(R.id.tv_no_data);

        btnReserve = v.findViewById(R.id.btn_reserve);
        btnAddMember = v.findViewById(R.id.btn_add_member);
        btnAddMember.setOnClickListener(this);

        if (shopData.getStoreType().equals("1"))
        {
            isStoreBlackList(shopData.getSid());
            btnReserve.setOnClickListener(this);
        }
        getStoreDescript(shopData.getSid());
        getCouponList(shopData.getSid());
        isMemberStatus(shopData.getStoreId());
    }

    private void isStoreBlackList(String sid)
    {
//        ApiConnection.isStoreBlackList(sid, new ApiConnection.OnConnectResultListener()
//        {
//            @Override
//            public void onSuccess(String jsonString)
//            {
//                getActivity().runOnUiThread(() ->
//                {
//                    btnReserve.setVisibility(View.VISIBLE);
//                    btnReserve.setClickable(true);
//                });
//            }
//
//            @Override
//            public void onFailure(String message)
//            {
//                getActivity().runOnUiThread(() ->
//                {
//                    btnReserve.setVisibility(View.VISIBLE);
//                    btnReserve.setClickable(false);
//                });
//            }
//        });
        if (null != shopData &&(shopData.getSid().equals("81") || shopData.getSid().equals("83") || shopData.getSid().equals("85") || shopData.getSid().equals("126")))
        {
            btnReserve.setVisibility(View.GONE);
        } else
        {
            btnReserve.setVisibility(View.VISIBLE);
            btnReserve.setClickable(true);
        }
    }

    private void getCouponList(String sid)
    {
        String use = "";
        ApiConnection.getMyCouponList2(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), sid, use, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                try
                {
                    Object obj = new JSONTokener(jsonString).nextValue();
                    Log.e("豪豪", "這是" + obj);
                    if (obj instanceof JSONArray)
                    {
                        Type type = new TypeToken<ArrayList<CouponListBean>>()
                        {
                        }.getType();

                        ArrayList<CouponListBean> couponList2;
                        couponList2 = new Gson().fromJson(jsonString, type);
                        ArrayList<CouponListBean> finalCouponList = couponList2;
                        if (getActivity() == null) {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                layoutViews(finalCouponList);
                            }
                        });

                    }
//                    else if (obj instanceof JSONObject)
//                    {
//
//
//
//                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message)
            {

            }

        });
    }

    private void layoutViews(ArrayList<CouponListBean> couponList)
    {
        ticketRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ticketAdapter = new TicketAdapter(R.layout.item_member_qr, couponList);
        ticketRecycler.setAdapter(ticketAdapter);
//        ticketAdapter.setOnItemClickListener(new OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position)
//            {
//                CouponListBean data;
//                data = couponList.get(position);
//                Intent intent = new Intent(getContext(), MemberQRActivity.class);
//                intent.putExtra("ticket", new Gson().toJson(data));
//                startActivity(intent);
//            }
//        });
    }

    //當從shopFragment進來的時候取得店家描述
    private void getStoreDescript(String sid) throws UnsupportedEncodingException
    {
        ApiConnection.getStoreDescript(sid, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                final Type type = new TypeToken<ArrayList<ShopBean>>()
                {
                }.getType();
                shopBean2 = new Gson().fromJson(jsonString, type);
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        txtStoreDescript.setText(shopBean2.get(0).getStoreDescript());
                        if (shopBean2.get(0).getStoreOpenTime() != null && shopBean2.get(0).getStoreOpenTime().length() != 0)
                        {
                            txtStoreBusinessTime.setText(shopBean2.get(0).getStoreOpenTime());
                            txtStoreBusinessTime.setVisibility(View.VISIBLE);
                            imgClock.setVisibility(View.VISIBLE);
                        } else
                        {
                            txtStoreBusinessTime.setVisibility(View.GONE);
                            imgClock.setVisibility(View.GONE);
                        }

                        Bundle params = new Bundle();
                        params.putString("store_name", shopBean2.get(0).getStoreName());
                        params.putString("shopping_area", AppUtility.getShoppingAreaChinese(shopBean2.get(0).getShoppingArea()));
                        mFirebaseAnalytics.logEvent("Click_Store", params);
                        tabSelect();
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void tabSelect()
    {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                switch (Objects.requireNonNull(tab.getText()).toString())
                {
                    case "店家資訊":
                        infoView.setVisibility(View.VISIBLE);
                        pointView.setVisibility(View.GONE);
                        ticketView.setVisibility(View.GONE);
                        break;
                    case "點數":
                        infoView.setVisibility(View.GONE);
                        pointView.setVisibility(View.VISIBLE);
                        ticketView.setVisibility(View.GONE);
                        break;
                    case "優惠券":
                        getCouponList(shopData.getSid());
                        infoView.setVisibility(View.GONE);
                        pointView.setVisibility(View.GONE);
                        ticketView.setVisibility(View.VISIBLE);
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
    }

    private void tabSelect2()
    {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                switch (Objects.requireNonNull(tab.getText()).toString())
                {
                    case "店家資訊":
                        infoView.setVisibility(View.VISIBLE);
                        pointView.setVisibility(View.GONE);
                        ticketView.setVisibility(View.GONE);
                        break;
                    case "點數":
                        infoView.setVisibility(View.GONE);
                        pointView.setVisibility(View.VISIBLE);
                        ticketView.setVisibility(View.GONE);
                        break;
                    case "優惠券":
                        getCouponList(shopBean2.get(0).getSid());
                        infoView.setVisibility(View.GONE);
                        pointView.setVisibility(View.GONE);
                        ticketView.setVisibility(View.VISIBLE);
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
    }

    //當從掃碼會員進入拿入會禮取得店家
    private void getStoreNewDescript(String sid, View v)
    {
        ApiConnection.getStoreDescript(sid, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                final Type type = new TypeToken<ArrayList<ShopBean>>()
                {
                }.getType();
                shopBean2 = new Gson().fromJson(jsonString, type);
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        initView2(v, shopBean2);
                        Log.d("豪豪2", "run: " + couponList);

                        if (isAddMemberGift)
                        {
                            if (couponList.size() != 0)
                            {
                                for (int i = 0; i < couponList.size(); i++)
                                {
                                    Dialog dialog = new Dialog(getContext());
                                    dialog.setContentView(R.layout.dialog_gift);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.show();
                                    TextView txtTitle = dialog.findViewById(R.id.tv_title);
                                    txtTitle.setText("店家入會禮");
                                    TextView txtContent = dialog.findViewById(R.id.tv_content);
                                    txtContent.setText(couponList.get(i).getCouponName());
                                    ImageView imgContent = dialog.findViewById(R.id.iv_content);
                                    PicassoTrustAll.getInstance(getContext()).load(API_IMAGE + couponList.get(i).getCouponPicture()).into(imgContent);
                                    TextView txtDate = dialog.findViewById(R.id.tv_discount_date);
                                    txtDate.setText("兌換期限：" + couponList.get(i).getCouponStartdate() + "～" + couponList.get(i).getCouponEnddate());

                                    Button btnClose = dialog.findViewById(R.id.btn_close);
                                    btnClose.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            if (dialog != null)
                                            {
                                                dialog.dismiss();
                                                isAddMemberGift = false;
                                            }
                                        }

                                    });
                                    Button btnEnter = dialog.findViewById(R.id.btn_enter);
                                    btnEnter.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            if (dialog != null)
                                            {
                                                dialog.dismiss();
                                                isAddMemberGift = false;
                                                Intent myDiscountIntent = new Intent(getActivity(), MyDiscountNew2Activity.class);
                                                startActivity(myDiscountIntent);
                                            }
                                        }
                                    });
                                }
                            }

                        } else
                        {
                            initView2(v, shopBean2);
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void initView2(View v, ArrayList<ShopBean> shopBean2)
    {
        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);
        productView = v.findViewById(R.id.iv_product);
        String imageUrl = API_IMAGE + shopBean2.get(0).getStorePicture();
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
        imgPoint = v.findViewById(R.id.iv_point);

        txtStoreName.setText(shopBean2.get(0).getStoreName());

        txtStoreAddress.setText(shopBean2.get(0).getStoreAddress());
        txtStoreAddress.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下劃線
        txtStoreAddress.getPaint().setAntiAlias(true);//抗鋸齒
        txtStoreAddress.setOnClickListener(this);

        txtStoreTel.setText(shopBean2.get(0).getStorePhone());
        txtStoreTel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下劃線
        txtStoreTel.getPaint().setAntiAlias(true);//抗鋸齒
        txtStoreTel.setOnClickListener(this);

        if (shopBean2.get(0).getStoreWebsite() != null && shopBean2.get(0).getStoreWebsite().length() != 0)
        {
            txtGlobe.setText(shopBean2.get(0).getStoreWebsite());
            txtGlobe.setVisibility(View.VISIBLE);
            imgGlobe.setVisibility(View.VISIBLE);
            txtGlobe.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            txtGlobe.getPaint().setAntiAlias(true);
            txtGlobe.setOnClickListener(this);
        } else
        {
            txtGlobe.setVisibility(View.GONE);
            imgGlobe.setVisibility(View.GONE);
        }

        if (shopBean2.get(0).getStoreFacebook() != null && shopBean2.get(0).getStoreFacebook().length() != 0)
        {
            txtFb.setText(shopBean2.get(0).getStoreFacebook());
            txtFb.setVisibility(View.VISIBLE);
            imgFb.setVisibility(View.VISIBLE);
            txtFb.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            txtFb.getPaint().setAntiAlias(true);
            txtFb.setOnClickListener(this);

        } else
        {
            txtFb.setVisibility(View.GONE);
            imgFb.setVisibility(View.GONE);

        }

        if (shopBean2.get(0).getStoreOpenTime() != null && shopBean2.get(0).getStoreOpenTime().length() != 0)
        {
            txtStoreBusinessTime.setText(shopBean2.get(0).getStoreOpenTime());
            txtStoreBusinessTime.setVisibility(View.VISIBLE);
            imgClock.setVisibility(View.VISIBLE);
        } else
        {
            txtStoreBusinessTime.setVisibility(View.GONE);
            imgClock.setVisibility(View.GONE);
        }

        if (shopBean2.get(0).getStoreName() != null && shopBean2.get(0).getStoreName().length() != 0)
        {
            switch (shopBean2.get(0).getSid())
            {
                case "122": //七彩雲南（龍潭店）
                case "123": //七彩雲南（金陵店）
                case "124": //七彩雲南（桃園店）
                case "17": //阿美米干
                case "21": //版納傣味
                case "18": //小云滇
                case "22": //阿美金三角點心店
                case "19": //七彩雲南 (忠貞店)
                case "20": //七彩雲南 (八德店)
                case "24": //癮食聖堂
                case "31": //冰獨
                case "34": //阿美米干1981
                case "23": //八妹婆婆民族創藝過橋米線
//                    imgPoint.setBackgroundResource(R.drawable.bg_point2);
//                    break;
                default:
                    imgPoint.setImageResource(R.drawable.bg_point1);
                    break;
            }
        }
        txtStoreDescript.setText(shopBean2.get(0).getStoreDescript());
        txtStoreBusinessTime.setText(shopBean2.get(0).getStoreOpenTime());

        infoView = v.findViewById(R.id.layout_info);
        pointView = v.findViewById(R.id.layout_point);
        ticketView = v.findViewById(R.id.layout_ticket);

        tabLayout = v.findViewById(R.id.tab_layout);
        ticketRecycler = v.findViewById(R.id.recycle_discount);
        txtNoData = v.findViewById(R.id.tv_no_data);

        btnReserve = v.findViewById(R.id.btn_reserve);
        btnReserve.setOnClickListener(this);

        btnAddMember = v.findViewById(R.id.btn_add_member);
        btnAddMember.setOnClickListener(this);

        if (shopBean2.get(0).getStoreType().equals("1"))
        {
            isStoreBlackList(shopBean2.get(0).getSid());
        }

        getCouponList(shopBean2.get(0).getSid());
        isMemberStatus(shopBean2.get(0).getStoreId());

        tabSelect2();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
                if (s != null && s.equals("1"))
                {
                    ((MainActivity) getActivity()).setMenuHide(false);
                    s = "";
                }
                shopData = null;
                getActivity().onBackPressed();
                break;
            case R.id.tv_info_store_address:
                Uri uri;
                if (shopData != null)
                {
                    uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + shopData.getStoreAddress());
                } else
                {
                    uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + shopBean2.get(0).getStoreAddress());
                }
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                break;
            case R.id.tv_info_store_tel:
                Intent intent;
                if (shopData != null)
                {
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopData.getStorePhone()));
                } else
                {
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopBean2.get(0).getStorePhone()));
                }
                startActivity(intent);
                break;
            case R.id.tv_globe:
                Uri uri1;
                if (shopData != null)
                {
                    uri1 = Uri.parse(shopData.getStoreWebsite());
                } else
                {
                    uri1 = Uri.parse(shopBean2.get(0).getStoreWebsite());
                }
                startActivity(new Intent(Intent.ACTION_VIEW, uri1));
                break;
            case R.id.tv_fb:
                Uri uri2;
                if (shopData != null)
                {
                    uri2 = Uri.parse(shopData.getStoreFacebook());
                } else
                {
                    uri2 = Uri.parse(shopBean2.get(0).getStoreFacebook());
                }
                startActivity(new Intent(Intent.ACTION_VIEW, uri2));
                break;
            case R.id.btn_reserve:
                if (shopData != null)
                {
                    isMemberStatus2(shopData.getStoreId());
                } else
                {
                    isMemberStatus2(shopBean2.get(0).getStoreId());
                }
                break;
            case R.id.btn_add_member:
                dialogAddMember();
//                Toast.makeText(getActivity(), "新功能維護中！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void isMemberStatus2(String storeId)
    {
        ApiConnection.isMemberStatus(storeId, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(() ->
                {
                    try
                    {
                        Object obj = new JSONTokener(jsonString).nextValue();
                        if (obj instanceof JSONArray)
                        {
                            CheckDesignerFragment checkDesignerFragment;
                            if (shopData != null)
                            {
                                checkDesignerFragment = CheckDesignerFragment.newInstance(shopData);
                            } else
                            {
                                checkDesignerFragment = CheckDesignerFragment.newInstance(shopBean2.get(0));
                            }
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            long firstTime = System.currentTimeMillis();

                            SharedPreferences sP = getActivity().getSharedPreferences("useTime", Context.MODE_PRIVATE);
                            sP.edit().putLong("useTime", firstTime).commit();

                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.productLayout, checkDesignerFragment, null).addToBackStack(null).commit();
                        } else if (obj instanceof JSONObject)
                        {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            String code = jsonObject.getString("code");
                            String storeInfo = jsonObject.getString("storeinfo");
                            Type type = new TypeToken<ArrayList<MemberBean>>()
                            {
                            }.getType();
                            memberBeans = new Gson().fromJson(storeInfo, type);
                            if (code.equals("0x0201"))
                            {
                                Toast.makeText(getActivity(), "請先加入該店會員！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

    private void dialogAddMember()
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_member);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        TextView txtContent = dialog.findViewById(R.id.tv_content);
        ImageView imgContent = dialog.findViewById(R.id.iv_content);
        txtContent.setText(memberBeans.get(0).getStoreName());
        PicassoTrustAll.getInstance((getContext())).load(API_IMAGE + memberBeans.get(0).getStorePicture()).into(imgContent);

        Button btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (dialog != null)
                {
                    dialog.dismiss();
                }
            }
        });
        Button btnAdd = dialog.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //加入會員並顯示在會員卡

                addMemberCard(memberBeans.get(0));
                sendIII(memberBeans.get(0).getStoreName());
                if (dialog != null)
                {
                    dialog.dismiss();
                }
                btnAddMember.setClickable(false);
                btnAddMember.setBackgroundResource(R.color.black);
                btnAddMember.setText("您是該店家的會員呦");
            }
        });
    }

    private void addMemberCard(MemberBean memberBean)
    {
        ApiConnection.addMemberCard(memberBean.getSid(), new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<CouponListBean>>()
                {
                }.getType();
                couponList = new ArrayList<>();
                couponList = new Gson().fromJson(jsonString, type);

                if (couponList != null)
                {
                    if (getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (int i = 0; i < couponList.size(); i++)
                            {
                                Dialog dialog = new Dialog(getContext());
                                dialog.setContentView(R.layout.dialog_gift);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();
                                TextView txtTitle = dialog.findViewById(R.id.tv_title);
                                txtTitle.setText("店家入會禮");
                                TextView txtContent = dialog.findViewById(R.id.tv_content);
                                txtContent.setText(couponList.get(i).getCouponName());
                                ImageView imgContent = dialog.findViewById(R.id.iv_content);
                                PicassoTrustAll.getInstance(getContext()).load(API_IMAGE + couponList.get(i).getCouponPicture()).into(imgContent);
                                TextView txtDate = dialog.findViewById(R.id.tv_discount_date);
                                txtDate.setText("兌換期限：" + couponList.get(i).getCouponStartdate() + "～" + couponList.get(i).getCouponEnddate());

                                Button btnClose = dialog.findViewById(R.id.btn_close);
                                btnClose.setOnClickListener(v ->
                                {
                                    if (dialog != null)
                                    {
                                        dialog.dismiss();
                                    }
                                });
                                Button btnEnter = dialog.findViewById(R.id.btn_enter);
                                btnEnter.setOnClickListener(v ->
                                {
                                    if (dialog != null)
                                    {
                                        dialog.dismiss();
                                        Intent myDiscountIntent = new Intent(getActivity(), MyDiscountNew2Activity.class);
                                        startActivity(myDiscountIntent);
                                    }
                                });
                            }
                        }
                    });
                }

            }

            @Override
            public void onFailure(String message)
            {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (message.equals("0x0206"))
                        {
                            AppUtility.showMyDialog(getActivity(), "您已註冊過此店家會員", "確認", null, new AppUtility.OnBtnClickListener()
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
                        } else
                        {
                            AppUtility.showMyDialog(getActivity(), "連線異常，或輸入參數不正確", "確認", null, new AppUtility.OnBtnClickListener()
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
                    }
                });

            }
        });
    }

    //資策會大數據 使用者樣貌
    private void sendIII(String storeName)
    {
        ApiConnection.getIIIAdd8(String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000), AppUtility.DecryptAES2(UserBean.member_id), AppUtility.getIPAddress(getActivity()), "", MemberInfoBean.decryptAddress, "", storeName, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Log.d("豪豪", "onSuccess: " + jsonString);
            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

    private class TicketAdapter extends BaseQuickAdapter<CouponListBean, BaseViewHolder>
    {

        public TicketAdapter(int layoutResId, @Nullable List<CouponListBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, CouponListBean couponListBean)
        {
            RoundedImageView imageView = baseViewHolder.getView(R.id.iv_content);
            RoundedImageView imageView2 = baseViewHolder.getView(R.id.iv_used);
            RoundedImageView imageView3 = baseViewHolder.getView(R.id.iv_expired);
            ConstraintLayout btnTicket = baseViewHolder.getView(R.id.constraintLayout);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            long systemTime = System.currentTimeMillis();
            //已過期
            if (couponListBean.getUsingFlag().equals("1"))
            {
                imageView2.setVisibility(View.VISIBLE);
                imageView2.setBackgroundColor(Color.parseColor("#000000"));
                imageView2.setAlpha(0.7f);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try
            {
                date = simpleDateFormat.parse(couponListBean.getCouponEnddate());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            if (couponListBean.getUsingFlag().equals("0") && systemTime > date.getTime() + 86400000)
            {
                imageView3.setVisibility(View.VISIBLE);
                imageView3.setBackgroundColor(Color.parseColor("#000000"));
                imageView3.setAlpha(0.7f);
            }
            Date finalDate = date;
            btnTicket.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (couponListBean.getUsingFlag().equals("0") && systemTime < finalDate.getTime())
                    {
                        Intent intent = new Intent(getContext(), MemberQRActivity.class);
                        intent.putExtra("ticket", new Gson().toJson(couponListBean));
                        startActivity(intent);
                    }
                }
            });
            if (couponListBean.getCouponPicture() != null)
            {
                PicassoTrustAll.getInstance(getContext())
                        .load(API_IMAGE + couponListBean.getCouponPicture())
                        .into(imageView);
            } else
            {
                imageView.setImageResource(R.color.ARAlphabet);
            }
        }
    }
}
