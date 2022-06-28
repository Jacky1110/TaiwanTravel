package com.jotangi.nickyen;

import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.jotangi.nickyen.api.ApiEnqueue;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.member.model.OrderListBean;
import com.jotangi.nickyen.model.MemberInfoBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MemberDataFragment extends Fragment implements View.OnClickListener {

    private String TAG = MemberDataFragment.class.getSimpleName() + "(TAG)";

    private Button btnConsumption, btnVoucher;
    private ImageButton btnGoBack;
    private ApiEnqueue apiEnqueue;

    // value
    private String mid;

    private TextView txtName, txtNumber, txtBirthday, txtSex, txtEmail, txtAddress;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mid = getArguments().getString("mid");

            DataMemberBeen.mid = mid;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_member_data, container, false);
        initView(v);
        getOrderInfo(mid);
        return v;
    }

    private void initView(View v) {

        apiEnqueue = new ApiEnqueue();
        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);
        btnConsumption = v.findViewById(R.id.btn_consumption);
        btnConsumption.setOnClickListener(this);
        btnVoucher = v.findViewById(R.id.btn_voucher);
        btnVoucher.setOnClickListener(this);

        //會員性名
        txtName = v.findViewById(R.id.tv_memberName);
        txtNumber = v.findViewById(R.id.tv_memberNumber);
        txtBirthday = v.findViewById(R.id.tv_memberBirthday);
        txtSex = v.findViewById(R.id.tv_memberSex);
        txtEmail = v.findViewById(R.id.tv_memberEmail);
        txtAddress = v.findViewById(R.id.tv_memberAddress);


    }

    private void getOrderInfo(String mid) {

        apiEnqueue.StoreMemberInfo(mid, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            Log.d(TAG, "jsonArray: " + jsonArray);
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                            Log.d(TAG, "jsonObject: " + jsonObject);
                            MemberInfoBean.memberName = jsonObject.getString("member_name");
                            MemberInfoBean.memberNumber = jsonObject.getString("member_phone");
                            Log.d(TAG, "MemberInfoBean.memberNumber: " + MemberInfoBean.memberNumber);
                            MemberInfoBean.memberBirthday = jsonObject.getString("member_birthday");
                            MemberInfoBean.memberSex = jsonObject.getString("member_gender");
                            MemberInfoBean.memberEmail = jsonObject.getString("member_email");
                            MemberInfoBean.memberAddress = jsonObject.getString("member_address");


                            txtName.setText(MemberInfoBean.memberName);
                            if (MemberInfoBean.memberNumber.equals("null")) {
                                txtNumber.setText("");
                            } else {
                                txtNumber.setText(MemberInfoBean.memberNumber);
                            }
                            if (MemberInfoBean.memberBirthday.equals("null")) {
                                txtBirthday.setText("");
                            } else {
                                txtBirthday.setText(MemberInfoBean.memberBirthday);
                            }
                            if (MemberInfoBean.memberEmail.equals("null")) {
                                txtEmail.setText("");
                            } else {
                                txtEmail.setText(MemberInfoBean.memberEmail);
                            }
                            if (MemberInfoBean.memberAddress.equals("null")) {
                                txtAddress.setText("");
                            } else {
                                txtAddress.setText(MemberInfoBean.memberAddress);
                            }
                            if (MemberInfoBean.memberSex.equals("1")) {
                                txtSex.setText("男");
                            } else if (MemberInfoBean.memberSex.equals("0")) {
                                txtSex.setText("女");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_go_back:
                Navigation.findNavController(v).popBackStack();
                break;
            case R.id.btn_consumption:
                Bundle bundle = new Bundle();
                bundle.putString("mid", mid);
                Log.d(TAG, "mid: " + mid);
                Navigation.findNavController(v).navigate(R.id.action_memberDataFragment_to_memberCostGeneralFragment, bundle);
                break;
            case R.id.btn_voucher:
                Bundle mode = new Bundle();
                mode.putString("mid", mid);
                Navigation.findNavController(v).navigate(R.id.action_memberDataFragment_to_discountMemberFragment, mode);
                break;
        }

    }
}