package com.jotangi.nickyen.merch.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.member.LoginActivity;
import com.jotangi.nickyen.model.UserBean;

import java.util.ArrayList;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/23
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class MerchSettingFragment extends Fragment implements View.OnClickListener
{

    //UI
    ImageButton btnGoBack;
    ImageView productView;
    private SwitchCompat switchButton;
    private String notifyStatus;
    private Button btnLogOut;

    private NavController controller;

    //店家資訊區塊 點數區塊 優惠券區塊
    ConstraintLayout InfoView, pointView, ticketView;
    //資訊區塊內的控件
    TextView txtStoreName, txtStoreAddress, txtStoreTel, txtStoreBusinessTime, txtStoreDescript;

    public MerchSettingFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_merch_setting, container, false);

//        getNotifyStatus();
        initView(view);

        return view;
    }

//    private void getNotifyStatus()
//    {
//        ApiConnection.getMerchNotifyStatus(new ApiConnection.OnConnectResultListener()
//        {
//            @Override
//            public void onSuccess(final String jsonString)
//            {
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        try
//                        {
//                            JSONArray jsonArray = new JSONArray(jsonString);
//                            notifyStatus = jsonArray.getJSONObject(0).getString("notify");
//
//                        } catch (JSONException e)
//                        {
//                            e.printStackTrace();
//                        }
//                        switchButtonStatus();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(final String message)
//            {
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        AppUtility.showMyDialog(MerchSettingActivity.this, message, "", "確定", new AppUtility.OnBtnClickListener()
//                        {
//                            @Override
//                            public void onCheck()
//                            {
//                            }
//
//                            @Override
//                            public void onCancel()
//                            {
//                            }
//                        });
//                    }
//                });
//            }
//        });
//    }

    private void initView(View v)
    {
        switchButton = v.findViewById(R.id.switch1);

        btnLogOut = v.findViewById(R.id.btn_logOut);
        btnLogOut.setOnClickListener(this);
        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
                Navigation.findNavController(getView()).popBackStack();
                break;
            case R.id.btn_logOut:
                logout();
                break;
        }
    }

    private void logout()
    {

        String account = AppUtility.DecryptAES2(UserBean.member_id);
        String pwd = AppUtility.DecryptAES2(UserBean.member_pwd);

        ApiConnection.logout(account, pwd, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                SharedPreferences preferences = getContext().getSharedPreferences("loginInfo", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onFailure(String message)
            {
                SharedPreferences preferences = getContext().getSharedPreferences("loginInfo", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("QUESTIONNAIRE_COUPON7");
        arrayList.add("QUESTIONNAIRE_COUPON8");
        arrayList.add("QUESTIONNAIRE_COUPON9");
        arrayList.add("QUESTIONNAIRE_COUPON10");
    }

//    private void switchButtonStatus()
//    {
//        switchButton.setChecked(notifyStatus.equals("ON"));
//        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
//            {
//                if (compoundButton.isChecked())
//                {
//                    String notify = "on";
//                    changeNotify(notify);
//                } else
//                {
//                    String notify = "off";
//                    changeNotify(notify);
//                }
//            }
//        });
//    }
//
//    private void changeNotify(String notify)
//    {
//        ApiConnection.setNotify(notify, new ApiConnection.OnConnectResultListener()
//        {
//            @Override
//            public void onSuccess(String jsonString)
//            {
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        getNotifyStatus();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(final String message)
//            {
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        Toast.makeText(MerchSettingActivity.this, message, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        });
//    }
}
