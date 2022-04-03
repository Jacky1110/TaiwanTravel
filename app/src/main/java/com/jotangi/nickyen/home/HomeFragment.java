package com.jotangi.nickyen.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.argame.ARGameActivity;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.beautySalons.BeautySalonsActivity;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.home.myquestionnaire.MyQuestionnaireActivity;
import com.jotangi.nickyen.home.notify.NotifyActivity;
import com.jotangi.nickyen.home.notify.NotifyModel;
import com.jotangi.nickyen.industry.IndustryActivity;
import com.jotangi.nickyen.model.BannerListBean;
import com.jotangi.nickyen.model.MemberBean;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.model.NewsBean;
import com.jotangi.nickyen.model.ShopBean;
import com.jotangi.nickyen.model.UserBean;
import com.jotangi.nickyen.pointshop.PointShopFragment;
import com.jotangi.nickyen.pointshop.renew.PointShopActivity;
import com.jotangi.nickyen.scanner.SurfaceViewActivity;
import com.jotangi.nickyen.shop.ProductFragment;
import com.jotangi.nickyen.shop.ShopNewActivity;
import com.jotangi.nickyen.storearea.StoreAreaActivity;
import com.jotangi.nickyen.utils.SharedPreferencesUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.stx.xhb.xbanner.XBanner;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends BaseFragment implements View.OnClickListener
{
    //banner
    private XBanner mXBanner;
    private ViewPager shopViewPager;
    private ArrayList<BannerListBean> bannerListBeans;
    private BannerListBean bannerData;
    //news
    private ArrayList<NewsBean> newsBeans;
    private NewsBean newsData;

    private ConstraintLayout btn_myPoint;
    private LinearLayout btnConstellation;
    private ImageView btnScan, btnNotify, imgRemind, btnPointMall, btn_myDiscount
            , btnMemberCard, btnStoreArea, btnNearbyStore, btnArGame, btnQuestionnaire
            , btnSalons, btnNewsAll, btnShopAll, btnIndustry, btnFestival;
    private TextView txtPoint;
    //nearbyStore
    private List<ShopBean> shopList = new ArrayList<>();
    private List<MemberBean> memberBeanList = new ArrayList<>();
    private ShopViewPagerAdapter shopViewPagerAdapter;

    //一般會員註冊平台禮判斷
    public static boolean isGift;
    public static boolean isScan;
    public static boolean isRecommend;
    private ArrayList<CouponListBean> couponList = new ArrayList<>();

    //掃碼加入會員禮判斷
//    public static boolean isMemberGift = false;

    private static final int EDIT = 1;

    private String isConstellation;

    OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d("豪豪", "時間: " + Calendar.getInstance().getTimeInMillis() / 1000);
        Intent giftIntent = getActivity().getIntent();
        if (giftIntent != null)
        {
            isGift = giftIntent.getBooleanExtra("gift", false);
            isScan = giftIntent.getBooleanExtra("scan", false);
            isRecommend = giftIntent.getBooleanExtra("recommend", false);
            String str = giftIntent.getStringExtra("tId");

            if (isGift && isScan)
            {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, new ProductFragment(), null).addToBackStack(null).commit();
                giftIntent.removeExtra("gift");
                giftIntent.removeExtra("scan");
                giftIntent.removeExtra("recommend");
            }
            if (isGift)
            {
                isMemberStatus2(str);
                giftIntent.removeExtra("gift");
                giftIntent.removeExtra("scan");
                giftIntent.removeExtra("recommend");
            }
            if (isRecommend)
            {
                //一般會員註冊沒有推薦碼拿取入會禮
                getGift2();
                giftIntent.removeExtra("gift");
                giftIntent.removeExtra("scan");
                giftIntent.removeExtra("recommend");
            }
        }
//        test(); // 拿來串接測試我寫的api
        mXBanner = v.findViewById(R.id.xBanner);
        txtPoint = v.findViewById(R.id.tv_point);
        btnScan = v.findViewById(R.id.iv_scan);
        btnScan.setOnClickListener(this);
        btnNotify = v.findViewById(R.id.iv_notify);
        btnNotify.setOnClickListener(this);
        imgRemind = v.findViewById(R.id.iv_remind);
        btn_myPoint = v.findViewById(R.id.cl_point);
        btn_myPoint.setOnClickListener(this);
        btnPointMall = v.findViewById(R.id.iv_point_mall);
        btnPointMall.setOnClickListener(this);
        btn_myDiscount = v.findViewById(R.id.iv_discount);
        btn_myDiscount.setOnClickListener(this);
        btnMemberCard = v.findViewById(R.id.iv_member_card);
        btnMemberCard.setOnClickListener(this);
        btnStoreArea = v.findViewById(R.id.iv_store_area);
        btnStoreArea.setOnClickListener(this);
        btnNearbyStore = v.findViewById(R.id.iv_nearby_store);
        btnNearbyStore.setOnClickListener(this);
        btnArGame = v.findViewById(R.id.iv_ar_game);
        btnArGame.setOnClickListener(this);
        btnQuestionnaire = v.findViewById(R.id.iv_questionnaire);
        btnQuestionnaire.setOnClickListener(this);
        btnSalons = v.findViewById(R.id.iv_salons);
        btnSalons.setOnClickListener(this);
        btnNewsAll = v.findViewById(R.id.iv_news_all);
        btnNewsAll.setOnClickListener(this);
        btnIndustry = v.findViewById(R.id.iv_industry);
        btnIndustry.setOnClickListener(this);
        btnFestival = v.findViewById(R.id.iv_festival);
        btnFestival.setOnClickListener(this);

        btnShopAll = v.findViewById(R.id.iv_shop_all);
        btnShopAll.setOnClickListener(this);
        shopViewPager = v.findViewById(R.id.shop_viewPager);
        shopViewPager.setClipToPadding(false);
        shopViewPager.setClipChildren(false);
        shopViewPager.setOffscreenPageLimit(3);

        getShopList();

        getMemberInfo();
        requestNews();
        requestBanner();
        getNotify();
        return v;
    }

    private void getNotify()
    {
        ApiConnection.getPushList(new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                new SharedPreferencesUtil(getActivity(), "notify");
                String info = (String) SharedPreferencesUtil.getData("info", "");
                if (info != null && !info.equals(""))
                {
                    Type type = new TypeToken<ArrayList<NotifyModel>>()
                    {
                    }.getType();
                    ArrayList<NotifyModel> old = new ArrayList<>();
                    old = new Gson().fromJson(info, type);
                    ArrayList<NotifyModel> newD = new ArrayList<>();
                    newD = new Gson().fromJson(jsonString, type);

                    if (newD.size() > old.size())
                    {
                        if (getActivity() == null)
                        {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                imgRemind.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
                SharedPreferencesUtil.putData("info", new Gson().toJson(jsonString));
            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

    private void getGift2()
    {
        String sid = "";
        String use = "0";
        ApiConnection.getMyCouponList(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), sid, use, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<CouponListBean>>()
                {
                }.getType();
                couponList = new ArrayList<>();
                couponList = new Gson().fromJson(jsonString, type);
                Collections.sort(couponList, new Comparator<CouponListBean>()
                {
                    @Override
                    public int compare(CouponListBean o1, CouponListBean o2)
                    {
                        return o1.getCouponType().compareTo(o2.getCouponType());
                    }
                });
                Log.d("豪豪", "onSuccess: " + couponList);
                if (getActivity() == null)
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
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

                                if (!couponList.get(i).getCouponType().equals("6"))
                                {

                                    txtTitle.setText("店家入會禮");

                                } else
                                {

                                    txtTitle.setText("會員註冊禮");
                                }
                                TextView txtContent = dialog.findViewById(R.id.tv_content);
                                txtContent.setText(couponList.get(i).getCouponName());
                                ImageView imgContent = dialog.findViewById(R.id.iv_content);
                                PicassoTrustAll.getInstance(getContext()).load(ApiConstant.API_IMAGE + couponList.get(i).getCouponPicture()).into(imgContent);
                                TextView txtDate = dialog.findViewById(R.id.tv_discount_date);
                                txtDate.setText("兌換期限：" + couponList.get(i).getCouponStartdate() + "～" + couponList.get(i).getCouponEnddate());

                                Button btnClose = dialog.findViewById(R.id.btn_close);
                                btnClose.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        dialog.dismiss();

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
                                            Intent myDiscountIntent = new Intent(getActivity(), MyDiscountNew2Activity.class);
                                            startActivity(myDiscountIntent);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

    //店家掃碼註冊獲取tid要到入會禮那關閉要拿取的sid
    private void isMemberStatus2(String str)
    {
        ApiConnection.isMemberStatus(str, new ApiConnection.OnConnectResultListener()
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
                        JSONArray jsonArray = new JSONArray(jsonString);
                        Type type = new TypeToken<ArrayList<MemberBean>>()
                        {
                        }.getType();
                        memberBeanList = new Gson().fromJson(String.valueOf(jsonArray), type);
                        if (getActivity() == null)
                        {
                            return;
                        }
                        getActivity().runOnUiThread(() -> getGift(memberBeanList.get(0)));

                    } else if (obj instanceof JSONObject)
                    {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String code = jsonObject.getString("code");
                        String storeInfo = jsonObject.getString("storeinfo");
                        ArrayList<MemberBean> memberBeans;
//                        JSONArray jsonArray = new JSONArray(jsonString);
                        Type type = new TypeToken<ArrayList<MemberBean>>()
                        {
                        }.getType();
                        memberBeans = new Gson().fromJson(storeInfo, type);

                        if (getActivity() == null)
                        {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                getGift(memberBeans.get(0));

                            }
                        });

                    }

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

    //取得入會禮
    private void getGift(MemberBean sid)
    {
        String type = "";
        String type2 = "0";

        ApiConnection.getMyCouponList(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), type, type2, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<CouponListBean>>()
                {
                }.getType();
                couponList = new ArrayList<>();
                couponList = new Gson().fromJson(jsonString, type);
                Collections.sort(couponList, new Comparator<CouponListBean>()
                {
                    @Override
                    public int compare(CouponListBean o1, CouponListBean o2)
                    {
                        return o1.getCouponType().compareTo(o2.getCouponType());
                    }
                });
                Log.d("豪豪", "onSuccess: " + couponList);
                if (getActivity() == null)
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (couponList.size() != 0)
                        {
                            ProductFragment productFragment = ProductFragment.newInstance2(sid, "1");
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, productFragment, null).addToBackStack(null).commit();
                            for (int i = 0; i < couponList.size(); i++)
                            {
                                Dialog dialog = new Dialog(getContext());
                                dialog.setContentView(R.layout.dialog_gift);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();
                                TextView txtTitle = dialog.findViewById(R.id.tv_title);

                                if (!couponList.get(i).getCouponType().equals("6"))
                                {

                                    txtTitle.setText("店家入會禮");

                                } else
                                {

                                    txtTitle.setText("會員註冊禮");
                                }
                                TextView txtContent = dialog.findViewById(R.id.tv_content);
                                txtContent.setText(couponList.get(i).getCouponName());
                                ImageView imgContent = dialog.findViewById(R.id.iv_content);
                                PicassoTrustAll.getInstance(getContext()).load(ApiConstant.API_IMAGE + couponList.get(i).getCouponPicture()).into(imgContent);
                                TextView txtDate = dialog.findViewById(R.id.tv_discount_date);
                                txtDate.setText("兌換期限：" + couponList.get(i).getCouponStartdate() + "～" + couponList.get(i).getCouponEnddate());

                                CouponListBean couponListBean = couponList.get(i);
                                Button btnClose = dialog.findViewById(R.id.btn_close);
                                btnClose.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {

                                        //                                        if (couponListBean == couponList.get(couponList.size() - 1))
                                        //                                        {
                                        //
                                        //                                            //本來想做最後一個dialog彈出按下關閉才會跳店家頁面，但改不太出來先讓按完第一次關閉直接自己跳
                                        //                                            isGood = true;
                                        //
                                        //                                            if (isGood)
                                        //                                            {
                                        ////                                                ProductFragment productFragment = ProductFragment.newInstance2(sid);
                                        ////                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, productFragment, null).addToBackStack(null).commit();
                                        //                                                isGood = false;
                                        //                                            }
                                        //                                        }
                                        dialog.dismiss();

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
                                            Intent myDiscountIntent = new Intent(getActivity(), MyDiscountNew2Activity.class);
                                            startActivity(myDiscountIntent);
                                        }
                                        if (dialog == null)
                                        {
                                            ProductFragment productFragment = ProductFragment.newInstance2(sid, "1");
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, productFragment, null).addToBackStack(null).commit();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

    private void getShopList()
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
        ApiConnection.getShopList(la, lo, "", "", new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                final Type type = new TypeToken<ArrayList<ShopBean>>()
                {
                }.getType();
                shopList = new Gson().fromJson(jsonString, type);
                if (getActivity() == null)
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        shopViewPagerAdapter = new ShopViewPagerAdapter(getContext(), shopList);
                        shopViewPager.setAdapter(shopViewPagerAdapter);
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

    private void requestBanner()
    {

        FormBody body = null;
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
                .url(ApiConstant.API_URL + "banner_list.php")
                .post(body)
                .build();

//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(ApiConnection.createSSLSocketFactory(), mMyTrustManager).build();
        if (client == null)
            client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException
            {

                String banner = response.body().string();
                bannerListBeans = new ArrayList<BannerListBean>();
                try
                {
                    JSONArray jsonArray = new JSONArray(banner);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
//                    JSONObject jsonObject = new JSONObject(banner);
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        bannerData = new BannerListBean();
                        bannerData.setBanner_picture(jsonObject.getString("banner_picture"));
                        bannerData.setBanner_link(jsonObject.getString("banner_link"));
                        bannerListBeans.add(bannerData);
                        Log.d("banner", "onResponse: " + bannerData.getBanner_picture());
                    }

                    if (getActivity() == null)
                    {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
//                            setBannerImage();
                            setCarouselList(bannerListBeans);

                        }

                        private void setCarouselList(ArrayList<BannerListBean> data)
                        {
                            mXBanner.setBannerData(R.layout.custom_layout, bannerListBeans);
                            mXBanner.loadImage(new XBanner.XBannerAdapter()
                            {
                                @Override
                                public void loadBanner(XBanner banner, Object model, View view, int position)
                                {
                                    ImageView img_carousel = view.findViewById(R.id.iv_commodity);

                                    String imageUrl = ApiConstant.API_IMAGE + data.get(position).getBanner_picture();
                                    PicassoTrustAll.getInstance(getActivity()).load(imageUrl).into(img_carousel);
                                }
                            });
                            mXBanner.setOnItemClickListener(new XBanner.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(XBanner banner, Object model, View view, int position)
                                {
                                    if (!data.get(position).getBanner_link().isEmpty())
                                    {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        Uri uri = Uri.parse(data.get(position).getBanner_link()); //拿官網測試用
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e)
            {
                Log.d("連線", e.getStackTrace().toString());
            }
        });

    }

    private void requestNews()
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
                .url(ApiConstant.API_URL + "news_list.php")
                .post(body)
                .build();

//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(ApiConnection.createSSLSocketFactory(), mMyTrustManager).build();
        if (client == null)
            client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                if (getActivity() == null)
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getActivity(), "網路出問題了!", Toast.LENGTH_SHORT).show();
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

    private void parseJson(String json)
    {
        newsBeans = new ArrayList<>();
        try
        {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                newsData = new NewsBean();
                newsData.setNewsDate(jsonObject.getString("news_date"));
                newsData.setNewsPicture(jsonObject.getString("news_picture"));
                newsData.setNewsSubject(jsonObject.getString("news_subject"));
                newsData.setNid(jsonObject.getString("nid"));
                newsBeans.add(newsData);
            }
            if (getActivity() == null)
            {
                return;
            }
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    setupRecyclerView(newsBeans);
                }
            });
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    //會員資料
    private void getMemberInfo()
    {
        String account = AppUtility.DecryptAES2(UserBean.member_id);
        String pwd = AppUtility.DecryptAES2(UserBean.member_pwd);
        ApiConnection.getMemberInfo(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                if (getActivity() == null)
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        txtPoint.setText(String.valueOf(MemberInfoBean.member_points));
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
                if (getActivity() == null)
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        txtPoint.setText("0");
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_scan:
                Intent scanIntent = new Intent(getActivity(), SurfaceViewActivity.class);
                startActivityForResult(scanIntent, EDIT);
                break;
            case R.id.iv_notify:
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, new NotifyFragment(), null).addToBackStack(null).commit();
                imgRemind.setVisibility(View.GONE);
                NotifyActivity.start(getActivity(), "user");
                break;
            case R.id.cl_point:
                Intent myPointIntent = new Intent(getActivity(), MyPointActivity.class);
                startActivity(myPointIntent);
                break;
            case R.id.iv_point_mall:
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, new PointShopFragment(), null).addToBackStack(null).commit();
//                Intent pointShopIntent = new Intent(getActivity(), PointShopActivity.class);
//                startActivity(pointShopIntent);
                Toast.makeText(getActivity(), "建置中\n敬請期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_discount:
                Intent myDiscountIntent = new Intent(getActivity(), MyDiscountNew2Activity.class);
                startActivity(myDiscountIntent);
                break;
            case R.id.iv_member_card:
                startActivity(new Intent(getActivity(), MemberCardActivity.class));
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, new MemberCardFragment(), null).addToBackStack(null).commit();
                break;
            case R.id.iv_store_area:
//                Toast.makeText(getActivity(), "商圈特區維護中，近期開放", Toast.LENGTH_SHORT).show();
                Intent storeAreaIntent = new Intent(getActivity(), StoreAreaActivity.class);
                startActivity(storeAreaIntent);
                break;
            case R.id.iv_nearby_store:
            case R.id.iv_shop_all:
                Intent shop = new Intent(getActivity(), ShopNewActivity.class);
                startActivity(shop);
                break;
            case R.id.iv_ar_game:
                Intent arGame = new Intent(getActivity(), ARGameActivity.class);
                startActivity(arGame);
//                Toast.makeText(getActivity(), "活動即將開始\n敬請期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_questionnaire:
//                QuestionnaireActivity.start(getActivity(),"1","波菲爾總店");
                startActivity(new Intent(getActivity(), MyQuestionnaireActivity.class));
//                Toast.makeText(getActivity(), "目前無待填寫問卷", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_salons:
                Intent beauty = new Intent(getActivity(), BeautySalonsActivity.class);
                startActivity(beauty);
                break;
            case R.id.iv_news_all:
                Intent newsIntent = new Intent(getActivity(), NewsAllActivity.class);
                startActivity(newsIntent);
                break;
            case R.id.iv_industry:
                Intent industryIntent = new Intent(getActivity(), IndustryActivity.class);
                startActivity(industryIntent);
//                Toast.makeText(getActivity(), "產業體驗維護中，近期開放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_festival:
//                startActivity(new Intent(getActivity(), ConstellationHomeActivity.class));
                Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case EDIT:
//                Toast.makeText(getActivity(), data.getExtras().getString("tId"), Toast.LENGTH_LONG).show();
                if (data != null)
                {
                    //確認是否為掃描的店家會員
                    isMemberStatus(data.getExtras().getString("tId"));
                }
        }
    }

    private void isMemberStatus(String tId)
    {
        ApiConnection.isMemberStatus(tId, new ApiConnection.OnConnectResultListener()
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
                        JSONArray jsonArray = new JSONArray(jsonString);
                        Type type = new TypeToken<ArrayList<MemberBean>>()
                        {
                        }.getType();
                        memberBeanList = new Gson().fromJson(String.valueOf(jsonArray), type);
                        if (getActivity() == null)
                        {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                AppUtility.showMyDialog(getActivity(), "您已是該店會員！！", "確認", null, new AppUtility.OnBtnClickListener()
                                {
                                    @Override
                                    public void onCheck()
                                    {
                                        //你已經是會員了，跳轉至店家頁面
                                        ProductFragment productFragment = ProductFragment.newInstance2(memberBeanList.get(0), "1");
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, productFragment, null).addToBackStack(null).commit();
                                    }

                                    @Override
                                    public void onCancel()
                                    {

                                    }
                                });
                            }
                        });

                    } else if (obj instanceof JSONObject)
                    {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String code = jsonObject.getString("code");
                        String storeInfo = jsonObject.getString("storeinfo");
                        ArrayList<MemberBean> memberBeans;
//                        JSONArray jsonArray = new JSONArray(jsonString);
                        Type type = new TypeToken<ArrayList<MemberBean>>()
                        {
                        }.getType();
                        memberBeans = new Gson().fromJson(storeInfo, type);

                        ArrayList<MemberBean> finalMemberBeans = memberBeans;
                        if (getActivity() == null)
                        {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Dialog dialog = new Dialog(getContext());
                                dialog.setContentView(R.layout.dialog_add_member);
                                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                dialog.show();
                                TextView txtContent = dialog.findViewById(R.id.tv_content);
                                ImageView imgContent = dialog.findViewById(R.id.iv_content);
                                txtContent.setText(finalMemberBeans.get(0).getStoreName());
                                PicassoTrustAll.getInstance((getContext())).load(ApiConstant.API_IMAGE + finalMemberBeans.get(0).getStorePicture()).into(imgContent);

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

                                        addMemberCard(finalMemberBeans);
                                        sendIII(finalMemberBeans.get(0).getStoreName());
                                        if (dialog != null)
                                        {
                                            dialog.dismiss();
                                        }

                                    }
                                });

                            }
                        });

                    }

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

    //加入會員
    private void addMemberCard(ArrayList<MemberBean> memberBeans)
    {
        ApiConnection.addMemberCard(memberBeans.get(0).getSid(), new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<CouponListBean>>()
                {
                }.getType();
                couponList = new ArrayList<>();
                couponList = new Gson().fromJson(jsonString, type);

                ProductFragment productFragment;
                if (couponList != null)
                {
                    productFragment = ProductFragment.newInstance3(memberBeans.get(0), true, couponList);
                } else
                {

                    productFragment = ProductFragment.newInstance2(memberBeans.get(0), "1");
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, productFragment, null).addToBackStack(null).commit();
                Log.d("豪豪", "onSuccess: " + couponList);

            }

            @Override
            public void onFailure(String message)
            {
                if (getActivity() == null)
                {
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
                                    //你已經是會員了，跳轉至店家頁面
                                    ProductFragment productFragment = ProductFragment.newInstance2(memberBeans.get(0), "1");
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, productFragment, null).addToBackStack(null).commit();
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

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NewsViewHolder>
    {

        private ArrayList<NewsBean> newss;

        public RecyclerAdapter(ArrayList<NewsBean> newss)
        {
            this.newss = newss;
        }

        class NewsViewHolder extends RecyclerView.ViewHolder
        {

            View view;
            ImageView picture;
            TextView date;
            TextView subject;

            public NewsViewHolder(@NonNull View itemView)
            {
                super(itemView);
                view = itemView;
                picture = itemView.findViewById(R.id.home_news_picture);
                date = itemView.findViewById(R.id.home_news_date);
                subject = itemView.findViewById(R.id.home_news_subject);

            }
        }

        @NonNull
        @Override
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {

            Context context = parent.getContext();
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_home_newlist_layout, parent, false);
            NewsViewHolder newsViewHolder = new NewsViewHolder(view);

            return newsViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull NewsViewHolder holder, int position)
        {

            String imagerUrl = ApiConstant.API_IMAGE + newss.get(position).getNewsPicture();
            Log.d("豪豪", imagerUrl);
            holder.date.setText(newss.get(position).getNewsDate());
            holder.subject.setText(newss.get(position).getNewsSubject());
//            解決Picasso能下載https的圖片問題
            PicassoTrustAll.getInstance(getContext()).load(imagerUrl).into(holder.picture);

            holder.view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent in = new Intent(getContext(), NewsActivity.class);
                    in.putExtra("nId", newss.get(position).getNid());
                    startActivity(in);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            int a;
            if (newss.size() == 1)
            {
                a = 1;
            } else if (newss.size() == 2)
            {
                a = 2;
            } else
            {
                a = 3;
            }
            return a;
        }

    }

    private void setupRecyclerView(ArrayList<NewsBean> newsBeans)
    {

        RecyclerView recyclerView = getActivity().findViewById(R.id.home_news_recycle);
        RecyclerAdapter adapter = new RecyclerAdapter(newsBeans);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity(), false));
    }

    //解決要使用scroll與recycle的兼容滑動問題 改成nestscroll
    public static class MyLinearLayoutManager extends LinearLayoutManager
    {
        private static final String TAG = MyLinearLayoutManager.class.getSimpleName();

        private boolean isScrollEnabled = true;

        public MyLinearLayoutManager(Context context, boolean isScrollEnabled)
        {
            super(context);
            this.isScrollEnabled = isScrollEnabled;
        }

        public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout)
        {
            super(context, orientation, reverseLayout);
        }
    }

    public class ShopViewPagerAdapter extends PagerAdapter
    {
        private Context context;
        private List<ShopBean> shopList;

        public ShopViewPagerAdapter(final Context context, final List<ShopBean> shopList)
        {
            this.context = context;
            this.shopList = shopList;
        }

        @Override
        public int getCount()
        {
            return 10;
        }

        // 來判斷顯示的是否是同一張圖片，這裡我们將兩個參數相比較返回即可
        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object)
        {
            return view.equals(object);
        }

        // 當要顯示的圖片可以進行緩存的時候，會調用這個方法進行顯示圖片的初始化，我们將要顯示的ImageView加入到ViewGroup中，然後作為返回值返回即可
        @NonNull
        @NotNull
        @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.slide_home_item, container, false);

            RoundedImageView imageView = view.findViewById(R.id.imageSlide);
            TextView txtTitle = view.findViewById(R.id.tv_title);
            TextView txtContent = view.findViewById(R.id.tv_content);
            TextView txtDistance = view.findViewById(R.id.tv_distance);

            PicassoTrustAll.getInstance(getContext()).load(ApiConstant.API_IMAGE + shopList.get(position).getStorePicture()).into(imageView);
            txtTitle.setText(shopList.get(position).getStoreName());
            txtContent.setText(shopList.get(position).getStoreNews());

            int AAA = Integer.parseInt(shopList.get(position).getDistance());

            if (AAA < 1000)
            {
                txtDistance.setText(shopList.get(position).getDistance() + "m");

            } else
            {
                double BBB = AAA / 100;
                BigDecimal bigDecimal = new BigDecimal(BBB);
                double f1 = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_DOWN).doubleValue() / 10;
                txtDistance.setText(f1 + "Km");
            }

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ProductFragment productFragment = ProductFragment.newInstance(shopList.get(position), "1");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeLayout, productFragment, null).addToBackStack(null).commit();
                }
            });
//        container.addView(view,position);//要填0否則會報錯
            container.addView(view, 0);
            return view;
//        return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object)
        {
//        super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
