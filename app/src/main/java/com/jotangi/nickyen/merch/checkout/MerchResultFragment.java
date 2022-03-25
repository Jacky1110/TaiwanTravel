package com.jotangi.nickyen.merch.checkout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.DialogIOSUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.merch.model.CheckBonus;
import com.jotangi.nickyen.merch.model.MerchCoupon;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MerchResultFragment extends Fragment implements View.OnClickListener
{
    private ImageButton btnGoBack;
    private ProgressBar progressBar;
    private Button btnEnter;
    private TextView txtTotal, txtStoreName, txtTicket, txtPoint, txtOrder, txtDiscount, txtPoint2, txtTotal2;

    private NavController controller;

    private ArrayList<CheckBonus> checkBonus;
    MerchCoupon merchCoupon;

    private String sid;
    private String total;
    private String bonus;
    private String mid;
    private String str;
    private String name;
    private String discount;//折扣金額
    private String last;//最後金額

    private int rule;//滿額規則
    private int a;//店長輸入金額
    private int b;//點數(一點折一元)

    public MerchResultFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_merch_result, container, false);
        sid = getArguments().getString("sid");
        total = getArguments().getString("total");
        bonus = getArguments().getString("bonus");
        mid = getArguments().getString("mid");
        str = getArguments().getString("coupon");
//        Log.d("店長", "onCreateView: "+str+"?"+mid);
        name = getArguments().getString("name");
        if (str.equals("0")){
            merchCoupon = new MerchCoupon();
            merchCoupon.setCouponName("您未使用優惠券");
            merchCoupon.setCouponNo("");
            merchCoupon.setCouponDiscount("");
        }else
        {
            merchCoupon = new Gson().fromJson(str, MerchCoupon.class);
            rule = Integer.parseInt(merchCoupon.getCouponRule());
        }
        a = Integer.parseInt(total);
        b = Integer.parseInt(bonus);

        if (rule > a)
        {
            DialogIOSUtility.showMyDialog(getActivity(), "核銷失敗", "此券適用滿額" + rule + "以上", "確認", "", new DialogIOSUtility.OnBtnClickListener()
            {
                @Override
                public void onCheck()
                {
                    controller = Navigation.findNavController(view);
                    controller.navigate(R.id.action_merchResultFragment_to_merchHomeFragment);
                }

                @Override
                public void onCancel()
                {

                }
            });
        }
        initView(view);

        return view;
    }

    private void initView(View view)
    {
        btnGoBack = view.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);
        btnEnter = view.findViewById(R.id.btn_enter);
        btnEnter.setOnClickListener(this);

        txtTotal = view.findViewById(R.id.tv_cost);
        txtStoreName = view.findViewById(R.id.tv_store_name);
        txtTicket = view.findViewById(R.id.tv_ticket);
        txtPoint = view.findViewById(R.id.tv_use);
        txtOrder = view.findViewById(R.id.tv_order);
        txtDiscount = view.findViewById(R.id.tv_discount);
        txtPoint2 = view.findViewById(R.id.tv_point);
        txtTotal2 = view.findViewById(R.id.tv_total);

        progressBar = view.findViewById(R.id.progressBar);


        if (bonus != null&&!bonus.equals("0"))
        {
            checkPoint(mid);
        } else
        {
            loadView();
        }
    }

    private void checkPoint(String mid)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.checkBonus(mid, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        Type type = new TypeToken<ArrayList<CheckBonus>>()
                        {
                        }.getType();
                        checkBonus = new Gson().fromJson(jsonString, type);
                        int a = Integer.parseInt(checkBonus.get(0).getMemberTotalpoints());
                        int b = Integer.parseInt(checkBonus.get(0).getMemberUsingpoints());
                        if (a > b)
                        {
                            loadView();

                        } else
                        {
                            DialogIOSUtility.showMyDialog(getActivity(), "點數異常", "會員點數有誤，請退出重新", "確認", "", new DialogIOSUtility.OnBtnClickListener()
                            {
                                @Override
                                public void onCheck()
                                {
                                    controller = Navigation.findNavController(getView());
                                    controller.navigate(R.id.action_merchResultFragment_to_merchHomeFragment);
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

            @Override
            public void onFailure(String message)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        DialogIOSUtility.showMyDialog(getActivity(), "條碼參數異常", "請退出重新", "確認", "", new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                controller = Navigation.findNavController(getView());
                                controller.navigate(R.id.action_merchResultFragment_to_merchHomeFragment);
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

    private void loadView()
    {
        txtTotal.setText(total);
        txtStoreName.setText(name);
        txtTicket.setText(merchCoupon.getCouponName());
        txtOrder.setText("NT$" + total);
        //讓點數不得大於輸入金額以防止負數
//        if (b>a){
//            b = a;
//            bonus = total;
//        }
//        txtPoint.setText(bonus);
        if (merchCoupon.getCouponDiscount().equals("1"))
        {
            int c = Integer.parseInt(merchCoupon.getDiscountAmount()); //優惠券折扣
            discount = merchCoupon.getDiscountAmount();
            if (b>a){
                b = a-c;
                bonus = String.valueOf(b);
            }
            txtPoint.setText(bonus);
            last = String.valueOf(a - b - c);

            txtDiscount.setText("-" + merchCoupon.getDiscountAmount());

            txtTotal2.setText(last);
        }
        else if (merchCoupon.getCouponDiscount().equals("2"))
        {
            int c = Integer.parseInt(merchCoupon.getDiscountAmount());
            double s = (double)c/100;
            double d = (a*s);
            int e = Math.toIntExact(Math.round(d));//最終折扣金額
            txtDiscount.setText("-" + e);
            discount = String.valueOf(e);
            if (b>a){
                b = a-e;
                bonus = String.valueOf(b);
            }
            txtPoint.setText(bonus);
            last = String.valueOf(a-e -b);
            txtTotal2.setText(last);
        }else if (merchCoupon.getCouponDiscount().isEmpty()){

            if (b>a){
                b = a;
                bonus = total;
            }
            discount = "0";
            txtDiscount.setText("-0");
            last = String.valueOf(a-b);
            txtTotal2.setText(last);
        }
        txtPoint2.setText("-" + bonus);
    }

    @Override
    public void onClick(View v)
    {
        controller = Navigation.findNavController(v);
        switch (v.getId())
        {
            case R.id.ib_go_back:
                controller.navigate(R.id.action_merchResultFragment_to_merchHomeFragment);
                break;
            case R.id.btn_enter:
                //確認核銷 接api跟彈跳視窗 按確認跳到消費記錄
                addOrder();
                break;
        }
    }

    private void addOrder()
    {
        String type = "1";
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.addOrder(sid,mid, merchCoupon.getCouponNo(), discount, bonus, total, type, last,new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        DialogIOSUtility.showMyDialog(getActivity(), "核銷成功", "已核銷！！", "確認", "", new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                controller = Navigation.findNavController(getView());
                                controller.navigate(R.id.action_merchResultFragment_to_merchRecordFragment);
                            }

                            @Override
                            public void onCancel()
                            {

                            }
                        });
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
                        progressBar.setVisibility(View.GONE);
                        DialogIOSUtility.showMyDialog(getActivity(), "核銷失敗", "請重新核銷", "確認", "", new DialogIOSUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                controller = Navigation.findNavController(getView());
                                controller.navigate(R.id.action_merchResultFragment_to_merchHomeFragment);

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
}