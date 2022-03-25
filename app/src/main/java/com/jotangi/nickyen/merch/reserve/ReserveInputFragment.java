package com.jotangi.nickyen.merch.reserve;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.google.gson.Gson;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.model.ShopBean;

import java.util.ArrayList;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/7/8
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class ReserveInputFragment extends Fragment implements View.OnClickListener
{
    static ShopBean shopBean;

    private ImageButton btnGoBack;
    private ImageView imgView;
    private Button btn9, btn8, btn7, btn6, btn5, btn4, btn3, btn2, btn1, btn00, btn0, btnCheck;
    private ConstraintLayout btnClear, btnEnter, btnLayoutCheck, btnLayoutEnter;
    private TextView txtNumber;

    StringBuilder builder = new StringBuilder();

    static String result;
    static String bookingNo;
    static String sDesigner ="";
    static ArrayList<OrderModel> orderModels;

    public ReserveInputFragment()
    {
        // Required empty public constructor
    }

    public static ReserveInputFragment newInstance(ShopBean dataList, String booking, String designer, ArrayList<OrderModel> orderModelList)
    {
        Bundle args = new Bundle();
        ReserveInputFragment fragment = new ReserveInputFragment();
        shopBean = dataList;
        bookingNo = booking;
        sDesigner = designer;
        orderModels = orderModelList;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_merch_input, container, false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        txtNumber = view.findViewById(R.id.tv_number);
        txtNumber.setText("0");

        imgView = view.findViewById(R.id.image);
        imgView.setVisibility(View.GONE);
        btnGoBack = view.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);
        btnEnter = view.findViewById(R.id.layout_enter);
        btnEnter.setVisibility(View.GONE);
        btnCheck = view.findViewById(R.id.btn_check);
        btnCheck.setOnClickListener(this);
        btnCheck.setVisibility(View.VISIBLE);

        btnLayoutCheck = view.findViewById(R.id.layout_check);
        btnLayoutEnter = view.findViewById(R.id.layout_enter);
        btnLayoutCheck.setVisibility(View.GONE);
        btnLayoutEnter.setVisibility(View.GONE);

        btnClear = view.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);

        btn1 = view.findViewById(R.id.btn_1);
        btn2 = view.findViewById(R.id.btn_2);
        btn3 = view.findViewById(R.id.btn_3);
        btn4 = view.findViewById(R.id.btn_4);
        btn5 = view.findViewById(R.id.btn_5);
        btn6 = view.findViewById(R.id.btn_6);
        btn7 = view.findViewById(R.id.btn_7);
        btn8 = view.findViewById(R.id.btn_8);
        btn9 = view.findViewById(R.id.btn_9);
        btn0 = view.findViewById(R.id.btn_0);
        btn00 = view.findViewById(R.id.btn_00);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btn00.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
//                shopBean = new ShopBean();
//                bookingNo="";
//                orderModels.clear();
//                sDesigner="";
                getActivity().onBackPressed();
                break;
            case R.id.btn_check:
                if (txtNumber.getText().equals("0"))
                {
                    Toast.makeText(getActivity(), "輸入金額請大於零", Toast.LENGTH_SHORT).show();
                    return;
                } else if (txtNumber.getText().length() > 10)
                {
                    Toast.makeText(getActivity(), "請勿輸入過大金額", Toast.LENGTH_SHORT).show();
                    return;
                } else
                {
                    if (result != null)
                    {
                        ReserveQRFragment reserveQRFragment = ReserveQRFragment.newInstance(new Gson().toJson(shopBean), result, bookingNo, sDesigner, orderModels);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.orderCheckLayout, reserveQRFragment, null).addToBackStack(null).commit();
                    }
                }
                break;
            case R.id.btn_clear:
                builder.setLength(0);
                break;
            case R.id.btn_9:
                builder.append("9");
                break;
            case R.id.btn_8:
                builder.append("8");
                break;
            case R.id.btn_7:
                builder.append("7");
                break;
            case R.id.btn_6:
                builder.append("6");
                break;
            case R.id.btn_5:
                builder.append("5");
                break;
            case R.id.btn_4:
                builder.append("4");
                break;
            case R.id.btn_3:
                builder.append("3");
                break;
            case R.id.btn_2:
                builder.append("2");
                break;
            case R.id.btn_1:
                builder.append("1");
                break;
            case R.id.btn_0:
                if (!txtNumber.getText().equals("0"))
                {
                    builder.append("0");
                }
                break;
            case R.id.btn_00:
                if (!txtNumber.getText().equals("0"))
                {
                    builder.append("00");
                }
                break;
        }
        if (builder.length() == 0)
        {
            txtNumber.setText("0");
        } else
        {
            result = builder.toString();
            txtNumber.setText(result);
        }
    }
}
