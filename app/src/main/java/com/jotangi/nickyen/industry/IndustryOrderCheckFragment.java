package com.jotangi.nickyen.industry;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.industry.model.ClassBean;
import com.jotangi.nickyen.industry.model.ProgramBean;
import com.jotangi.nickyen.model.MemberInfoBean;

public class IndustryOrderCheckFragment extends Fragment implements View.OnClickListener
{
    private ImageButton btnBack;
    private Button btnNext;
    private ProgressBar progressBar;

    private TextView txtStoreNameTitle, txtStoreName, txtProgramName, txtDate, txtProgramAddress, txtProgramTel, txtName, txtTel, txtMail, txtOrderItem, txtAmount, txtRemark;

    private LinearLayout layoutRemark;

    private ClassBean classBean;
    private ProgramBean programBean;
    private String person, workDate, reserveTimes, price, note;
    private String fee = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            classBean = new Gson().fromJson(getArguments().getString("classBean"), ClassBean.class);
            programBean = new Gson().fromJson(getArguments().getString("plan"), ProgramBean.class);
            person = getArguments().getString("count");
            workDate = getArguments().getString("date");
            reserveTimes = getArguments().getString("time");
            note = getArguments().getString("remark"); // 消費者零的必填備註
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_industry_order_check, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable final Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);

        btnNext = getView().findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        progressBar = getView().findViewById(R.id.progressBar);
        txtStoreNameTitle = getView().findViewById(R.id.text7);
        txtStoreName = getView().findViewById(R.id.tv_p_store_name);
        txtProgramName = getView().findViewById(R.id.tv_store_name);
        txtDate = getView().findViewById(R.id.tv_order_date);
        txtProgramAddress = getView().findViewById(R.id.tv_order_address);
        txtProgramTel = getView().findViewById(R.id.tv_order_tel);
        txtName = getView().findViewById(R.id.tv_name);
        txtTel = getView().findViewById(R.id.tv_tel);
        txtMail = getView().findViewById(R.id.tv_mail);
        txtOrderItem = getView().findViewById(R.id.tv_order_item);
        txtAmount = getView().findViewById(R.id.tv_item_amount);
//        recyclerView = getView().findViewById(R.id.recycler);

        txtStoreName.setText(programBean.getStoreName());
        txtStoreNameTitle.setVisibility(View.VISIBLE);
        txtStoreName.setVisibility(View.VISIBLE);
        txtProgramName.setText(programBean.getClassName());
        txtDate.setText(workDate + " " + reserveTimes);
        txtProgramAddress.setText(programBean.getStoreAddress());
        txtProgramTel.setText(programBean.getStorePhone());
        txtName.setText(MemberInfoBean.decryptName);
        txtTel.setText(AppUtility.DecryptAES2(MemberInfoBean.member_id));
        if (!MemberInfoBean.member_email.equals(""))
            txtMail.setText(AppUtility.DecryptAES2(MemberInfoBean.member_email));

        txtOrderItem.setText(programBean.getProgramName() + " X" + person);
        if (!programBean.getProgramPrice().contains("~"))
        {
            price = AppUtility.substringFunction(programBean.getProgramPrice(), "元", "", 1);
            fee = String.valueOf(Integer.parseInt(price) * Integer.parseInt(person));
        } else
        {
            int min = Integer.parseInt(AppUtility.substringFunction(programBean.getProgramPrice(),"~","",1));
            int max = Integer.parseInt(AppUtility.substringFunction(programBean.getProgramPrice(),"~","元",3));
            String priceMin = String.valueOf(min * Integer.parseInt(person));
            String priceMax = String.valueOf(max * Integer.parseInt(person));
            fee = priceMin +"~" +priceMax;
        }

        txtAmount.setText("$ " + fee);

        layoutRemark = getView().findViewById(R.id.layout_remark);
        layoutRemark.setVisibility(View.VISIBLE);
        txtRemark = getView().findViewById(R.id.tv_remark);
        txtRemark.setText(note);
//        layoutView(serviceList1);
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.ib_go_back:
                Navigation.findNavController(getView()).popBackStack(); //不會重走上一頁的onCreate方法其他都會重走 actionAtoB則是全部重走
                break;
            case R.id.btn_next:
                addBooking();
                break;
        }
    }

    private void addBooking()
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.addClassBooking(programBean.getStoreId(), programBean.getPid(), workDate, reserveTimes, person, "", note, new ApiConnection.OnConnectResultListener()
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
                        fcmToStore(programBean.getStoreId(), getString(R.string.order_conform_notify), getString(R.string.fcm_to_store_reserve_content_confirm, MemberInfoBean.decryptName));
                        TextView txtSName, txtDate;
                        Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog_industry_reserve);
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                        dialog.show();
                        txtSName = dialog.findViewById(R.id.tv_content);
                        txtDate = dialog.findViewById(R.id.tv_date);
                        txtSName.setText(programBean.getStoreName() + "-" + programBean.getProgramName());
                        txtDate.setText(workDate + " " + reserveTimes);
                        Button btnLook = dialog.findViewById(R.id.btn_enter);
                        btnLook.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (dialog != null)
                                {
                                    dialog.dismiss();
                                }
                                Intent in = new Intent(getActivity(), IndustryRecordActivity.class);
                                startActivity(in);
                                getActivity().finish();
                            }
                        });
                        TextView btnReturn = dialog.findViewById(R.id.btn_close);
                        btnReturn.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (dialog != null)
                                {
                                    dialog.dismiss();
                                }
                                getActivity().finish();
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
                        AppUtility.showMyDialog(getActivity(), "連線失敗，請重新預約", "確認", null, new AppUtility.OnBtnClickListener()
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
                });
            }
        });
    }

    private void fcmToStore(String mobileNo, String title, String content)
    {
        ApiConnection.fcmToStore(mobileNo, title, content, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
            }

            @Override
            public void onFailure(String message)
            {
            }
        });
    }
}