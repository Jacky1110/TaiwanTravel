package com.jotangi.nickyen.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.merch.model.MerchMemberInfoBean;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.model.UserBean;
import com.jotangi.nickyen.scanner.SurfaceViewActivity;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/11
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class PointDiscountFragment extends BaseFragment implements View.OnClickListener
{
    private ImageView QRImage;
    private TextView txtTicket, txtPoint, txtTips, txtUse;
    private Button btnChoice, btnNoChoice, btnCheck, btnNoCheck, btnConfirm;
    //有選取優惠券的狀態，一開始無
    private Boolean isStatus = false;
    //有選取使用點數狀態，一開始有
    private Boolean isStatus2 = true;

    private String couponNo;//下一頁優惠券返回的
    private ArrayList<MerchMemberInfoBean> memberInfoArrayList;
    private ArrayList<CouponListBean> couponList;
    private ArrayList<CouponListBean> filterList;

    String str = "0";//點數
    String ticketCount="0";//優惠券張數
    String action = "0"; //一開始進來叫api拿張數，1的話表示從優惠券回來要秀優惠券名稱

    private static final int EDIT = 1;

    public PointDiscountFragment()
    {
        // Required empty public constructor
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_point_discount, container, false);
        initView(view);
        return view;
    }

    private void change()
    {
        Intent scanIntent = new Intent(getActivity(), SurfaceViewActivity.class);
        startActivityForResult(scanIntent, EDIT);
        getActivity().finish();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPoint();
        getCouponList();
//        change(); //積點折點頁面換別的頁面
    }

    private void getCouponList()
    {
        if (action.equals("0"))
        {
            ApiConnection.getMyCouponList(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), "", "0", new ApiConnection.OnConnectResultListener()
            {
                @Override
                public void onSuccess(String jsonString)
                {
                    Type type = new TypeToken<ArrayList<CouponListBean>>()
                    {
                    }.getType();
                    couponList = new ArrayList<>();
                    couponList = new Gson().fromJson(jsonString, type);
                    filterList = new ArrayList<>();
                    for (int i = 0; i < couponList.size(); i++)
                    {
                        CouponListBean coupon = couponList.get(i);
                        if (coupon.getCouponType().equals("1") || coupon.getCouponType().equals("2") || coupon.getCouponType().equals("4") || coupon.getCouponType().equals("5"))
                        {
                            filterList.add(coupon);
                        }
                    }
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            ticketCount = String.valueOf(filterList.size());
                            txtTicket.setText("已有" + ticketCount + "張優惠券");
                        }
                    });
                }

                @Override
                public void onFailure(String message)
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ticketCount = "0";
                            txtTicket.setText("已有" + ticketCount + "張優惠券");
                        }
                    });

                }
            });
        }
    }

    private void getPoint()
    {
        ApiConnection.getMerchMemberInfo(new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<MerchMemberInfoBean>>()
                {
                }.getType();
                memberInfoArrayList = new ArrayList<>();
                memberInfoArrayList = new Gson().fromJson(jsonString, type);
                MemberInfoBean.member_points = Integer.parseInt(memberInfoArrayList.get(0).getMemberTotalpoints()) - Integer.parseInt(memberInfoArrayList.get(0).getMemberUsingpoints());
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        str = String.valueOf(MemberInfoBean.member_points);
                        txtUse.setText("使用" + str + "點");
                        txtPoint.setText(str);
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        AppUtility.showMyDialog(getActivity(), "點數可能有點問題...，請檢查網路連線或洽服務人員", "確定", "", new AppUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                str = String.valueOf(MemberInfoBean.member_points);
                                txtUse.setText("使用" + str + "點");
                                txtPoint.setText(str);
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

    private void initView(View view)
    {
        QRImage = view.findViewById(R.id.image);
        txtTicket = view.findViewById(R.id.tv_ticket);
        txtPoint = view.findViewById(R.id.tv_point);
        txtUse = view.findViewById(R.id.tv_use);
        txtTips = view.findViewById(R.id.tv_tips);
        btnChoice = view.findViewById(R.id.btn_choice);
        btnChoice.setOnClickListener(this);
        btnNoChoice = view.findViewById(R.id.btn_no_choice);
        btnNoChoice.setOnClickListener(this);
        btnCheck = view.findViewById(R.id.btn_check);
        btnCheck.setOnClickListener(this);
        btnNoCheck = view.findViewById(R.id.btn_no_check);
        btnNoCheck.setOnClickListener(this);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_choice:
            case R.id.btn_no_choice:
                Intent myDiscountIntent = new Intent(getActivity(), MyDiscountNew2Activity.class);
                myDiscountIntent.putExtra("discount", "1");
                startActivityForResult(myDiscountIntent, EDIT);
                break;
            case R.id.btn_check: //按下check鈕表示使用點數
                txtUse.setText("您未使用點數");
                btnCheck.setVisibility(View.INVISIBLE);
                btnNoCheck.setVisibility(View.VISIBLE);
                isStatus2 = false;
                break;
            case R.id.btn_no_check:
                txtUse.setText("使用" + str + "點");
                btnCheck.setVisibility(View.VISIBLE);
                btnNoCheck.setVisibility(View.INVISIBLE);
                isStatus2 = true;
                break;
            case R.id.btn_confirm:

                if (btnConfirm.getText().equals("重新選擇"))
                {
                    QRImage.setVisibility(View.INVISIBLE);
                    txtTips.setVisibility(View.VISIBLE);
                    btnChoice.setVisibility(View.VISIBLE);
                    btnNoChoice.setVisibility(View.INVISIBLE);
                    btnCheck.setVisibility(View.INVISIBLE);
                    btnNoCheck.setVisibility(View.VISIBLE);
                    btnChoice.setClickable(true);
                    btnNoChoice.setClickable(true);
                    btnCheck.setClickable(true);
                    btnNoCheck.setClickable(true);
                    btnConfirm.setText(R.string.text_confirm);
                    txtUse.setText("您未使用點數");
                    isStatus = false;
                    isStatus2 = false;
                    txtTicket.setText("已有" + ticketCount + "張優惠券");
                    Log.d("安安", "onClick: "+ticketCount);

                } else
                {
                    if (!isStatus && !isStatus2)
                    {

                        txtUse.setText("您未使用點數");
                        Toast.makeText(getActivity(), "提醒：您未使用優惠券及點數折抵", Toast.LENGTH_SHORT).show();
//                        return;
                        genCode("0", "0");
                    } else if (!isStatus && isStatus2)
                    {

                        genCode("0", str);
                    } else if (isStatus && !isStatus2)
                    {
                        txtUse.setText("您未使用點數");
                        genCode(couponNo, "0");
                    } else
                    {
                        genCode(couponNo, str);
                    }
                    txtTips.setVisibility(View.INVISIBLE);
                    if (!isStatus)
                    {
                        txtTicket.setText("您未使用任何優惠券");
                    }
                    if (isStatus2)
                    {
                        txtUse.setText("使用" + str + "點");
                    }
                    btnChoice.setVisibility(View.INVISIBLE);
                    btnNoChoice.setVisibility(View.VISIBLE);
                    btnChoice.setClickable(false);
                    btnNoChoice.setClickable(false);
                    btnNoChoice.setVisibility(View.VISIBLE);
                    btnCheck.setVisibility(View.INVISIBLE);
                    btnNoCheck.setVisibility(View.VISIBLE);
                    btnCheck.setClickable(false);
                    btnNoCheck.setClickable(false);
                    btnConfirm.setText("重新選擇");
                }
                break;
        }
    }

    private void genCode(String couponNo, String bonusPoint)
    {
        if (couponNo == null)
        {
            couponNo = "0";
        }
        String str = null;
        try
        {
            str = "spot://payment?m_id=" + AppUtility.DecryptAES2(UserBean.member_id) + "&coupon_no=" + couponNo + "&bonus_point=" + bonusPoint;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d("豪豪", "genCode: " + str);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try
        {
            Bitmap bit = encoder.encodeBitmap(str, BarcodeFormat.QR_CODE,
                    250, 250);
            QRImage.setImageBitmap(bit);
            QRImage.setVisibility(View.VISIBLE);
        } catch (WriterException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case EDIT:
//                Toast.makeText(getActivity(), data.getExtras().getString("name"), Toast.LENGTH_LONG).show();
                if (data != null)
                {
                    couponNo = data.getExtras().getString("dis");
                    txtTicket.setText(data.getExtras().getString("name"));
                    isStatus = true;
                    action = "1";
                }
        }
    }
}
