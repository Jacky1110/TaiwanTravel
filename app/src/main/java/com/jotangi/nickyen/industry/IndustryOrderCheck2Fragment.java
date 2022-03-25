package com.jotangi.nickyen.industry;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.beautySalons.model.ServiceBean;
import com.jotangi.nickyen.industry.model.ClassBean;
import com.jotangi.nickyen.industry.model.IndustryOrderBean;
import com.jotangi.nickyen.industry.model.ProgramBean;
import com.jotangi.nickyen.model.MemberInfoBean;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class IndustryOrderCheck2Fragment extends Fragment implements View.OnClickListener
{
    private ImageButton btnBack;
    private Button btnNext, btnCancel;
    //    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    //    private OrderAdapter adapter;
    private TextView txtStoreNameTitle, txtStoreName, txtProgramName, txtDate, txtProgramAddress, txtProgramTel, txtName, txtTel, txtMail, txtOrderItem, txtAmount, txtPleaseNote, txtRemark;
    private String price;
    private String fee = "";
    private LinearLayout layoutRemark;

//    private ClassBean classBean;
//    private ProgramBean programBean;
//    private String person;
//    private String workDate;
//    private String reserveTimes;

    static IndustryOrderBean industryOrderBean;

    //==此為會員專區進來的預約記錄不是從美容美髮頁面進來的
    public static IndustryOrderCheck2Fragment newInstance(IndustryOrderBean data)
    {
        Bundle args = new Bundle();
        IndustryOrderCheck2Fragment fragment = new IndustryOrderCheck2Fragment();
        industryOrderBean = data;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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

        btnCancel = getView().findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        txtPleaseNote = getView().findViewById(R.id.tv_content);

        if (AppUtility.isExpire(industryOrderBean.getReserveDate() + industryOrderBean.getReserveTime(), true))
        {
            btnNext.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        } else
        {
            if (industryOrderBean.getReserveStatus().equals("0")) //預約尚未確認
            {
                btnNext.setText(R.string.booking_yet);
                btnNext.setBackgroundResource(R.drawable.shape_type_gray);
            } else if (industryOrderBean.getReserveStatus().equals("1")) // 預約已回覆
            {
                btnNext.setText(R.string.cancel_booking);
                btnCancel.setText(R.string.rebooking);
                btnCancel.setVisibility(View.VISIBLE);
                txtPleaseNote.setText(R.string.industry_please_note_order_check2);
                txtPleaseNote.setVisibility(View.VISIBLE);
            } else if (industryOrderBean.getReserveStatus().equals("2")) // 預約已取消
            {
                btnNext.setVisibility(View.GONE);
                btnCancel.setText(R.string.rebooking);
                btnCancel.setVisibility(View.VISIBLE);
                txtPleaseNote.setText(R.string.industry_please_note_order_check3);
                txtPleaseNote.setVisibility(View.VISIBLE);
            } else // 預約已完成
            {
                btnNext.setVisibility(View.GONE);
                btnCancel.setText(R.string.cancel_booking);
                btnCancel.setVisibility(View.VISIBLE);
            }
        }

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

        txtStoreName.setText(industryOrderBean.getStoreName());
        txtStoreNameTitle.setVisibility(View.VISIBLE);
        txtStoreName.setVisibility(View.VISIBLE);
        txtProgramName.setText(industryOrderBean.getProgramName());
        txtDate.setText(industryOrderBean.getReserveDate() + " " + industryOrderBean.getReserveTime());
        txtProgramAddress.setText(industryOrderBean.getStoreAddress());
        txtProgramTel.setText(industryOrderBean.getStorePhone());
        txtName.setText(industryOrderBean.getMemberName());
        txtTel.setText(industryOrderBean.getMemberId());
        if (!industryOrderBean.getMemberEmail().equals(""))
            txtMail.setText(industryOrderBean.getMemberEmail());

        txtOrderItem.setText(industryOrderBean.getProgramName() + " X" + industryOrderBean.getProgramPerson());
        if (!industryOrderBean.getProgramPrice().contains("~"))
        {
            price = AppUtility.substringFunction(industryOrderBean.getProgramPrice(), "元", "", 1);
            fee = String.valueOf(Integer.parseInt(price) * Integer.parseInt(industryOrderBean.getProgramPerson()));
        } else
        {
            int min = Integer.parseInt(AppUtility.substringFunction(industryOrderBean.getProgramPrice(),"~","",1));
            int max = Integer.parseInt(AppUtility.substringFunction(industryOrderBean.getProgramPrice(),"~","元",3));
            String priceMin = String.valueOf(min * Integer.parseInt(industryOrderBean.getProgramPerson()));
            String priceMax = String.valueOf(max * Integer.parseInt(industryOrderBean.getProgramPerson()));
            fee = priceMin +"~" +priceMax;
        }
        txtAmount.setText("$ " + fee);

        layoutRemark = getView().findViewById(R.id.layout_remark);
        layoutRemark.setVisibility(View.VISIBLE);
        txtRemark = getView().findViewById(R.id.tv_remark);
        if (industryOrderBean.getReserveNote() == null|| industryOrderBean.getReserveNote().equals(""))
        {
            txtRemark.setText(industryOrderBean.getReserveRemark());
        }else {
            txtRemark.setText(industryOrderBean.getReserveNote()+"\n"+industryOrderBean.getReserveRemark());
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_next:
//                if (btnNext.getText().equals(getString(R.string.cancel_booking)))
//                {
//                    AppUtility.showMyDialog(getActivity(), "本次預約是否取消？\n提醒：若取消預約三次，會被系統加入黑名單，且無法使用線上預約，需要店家解除黑名單才可以再次預約。", "確認取消", "取消", new AppUtility.OnBtnClickListener()
//                    {
//                        @Override
//                        public void onCheck()
//                        {
//                            cancelBooking(orderBean.getBookingNo(), false);
//                        }
//
//                        @Override
//                        public void onCancel()
//                        {
//
//                        }
//                    });
//                }
//                break;
//            case R.id.btn_cancel:
//                if (btnCancel.getText().equals(getString(R.string.cancel_booking)))
//                {
//                    AppUtility.showMyDialog(getActivity(), "本次預約是否取消？\n提醒：若取消預約三次，會被系統加入黑名單，且無法使用線上預約，需要店家解除黑名單才可以再次預約。", "確認取消", "取消", new AppUtility.OnBtnClickListener()
//                    {
//                        @Override
//                        public void onCheck()
//                        {
//                            cancelBooking(orderBean.getBookingNo(), false);
//                        }
//
//                        @Override
//                        public void onCancel()
//                        {
//
//                        }
//                    });
//                } else if (btnCancel.getText().equals(getString(R.string.rebooking)))
//                {
//                    cancelBooking(orderBean.getBookingNo(), true);
//                }
//                break;
                if (btnNext.getText().equals(getString(R.string.cancel_booking)))
                {
                    AppUtility.showMyDialog(getActivity(), "本次預約是否取消？\n提醒：若取消預約三次，會被系統加入黑名單，且無法使用線上預約，需要店家解除黑名單才可以再次預約。", "確認取消", "取消", new AppUtility.OnBtnClickListener()
                    {
                        @Override
                        public void onCheck()
                        {
                            cancelBooking(industryOrderBean.getBookingNo(), false);
                        }

                        @Override
                        public void onCancel()
                        {

                        }
                    });
                }
                break;
            case R.id.btn_cancel:
                if (btnCancel.getText().equals(getString(R.string.cancel_booking)))
                {
                    AppUtility.showMyDialog(getActivity(), "本次預約是否取消？\n提醒：若取消預約三次，會被系統加入黑名單，且無法使用線上預約，需要店家解除黑名單才可以再次預約。", "確認取消", "取消", new AppUtility.OnBtnClickListener()
                    {
                        @Override
                        public void onCheck()
                        {
                            cancelBooking(industryOrderBean.getBookingNo(), false);
                        }

                        @Override
                        public void onCancel()
                        {

                        }
                    });
                } else if (btnCancel.getText().equals(getString(R.string.rebooking)))
                {
                    if (industryOrderBean.getReserveStatus().equals("2"))
                    {
                        startActivity(new Intent(getActivity(), IndustryActivity.class));
                        getActivity().finish();
                    } else
                    {
                        cancelBooking(industryOrderBean.getBookingNo(), true);
                    }
                }
                break;
//                if (industryOrderBean.getReserveStatus().equals("1"))
//                {
//                    getActivity().onBackPressed();
//
//                } else
//                {
//                    getActivity().runOnUiThread(new Runnable()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            AppUtility.showMyDialog(getActivity(), "本次預約是否取消？", "確認取消", "取消", new AppUtility.OnBtnClickListener()
//                            {
//                                @Override
//                                public void onCheck()
//                                {
//                                    cancelBooking(industryOrderBean.getBookingNo());
//
//                                }
//
//                                @Override
//                                public void onCancel()
//                                {
//
//                                }
//                            });
//                        }
//                    });
//                }
//                break;
        }
    }

    private void cancelBooking(String bookingNo, boolean isRebooking)
    {
        ApiConnection.classBookingCancel(bookingNo, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getActivity(), "您已取消成功！", Toast.LENGTH_SHORT).show();
                        fcmToStore(industryOrderBean.getStoreId(), getString(R.string.order_cancel_notify), getString(R.string.fcm_to_store_reserve_content_cancel, industryOrderBean.getMemberName()));
                        if (isRebooking)
                        {
                            startActivity(new Intent(getActivity(), IndustryActivity.class));
                        }
                        getActivity().finish();
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
                        Toast.makeText(getActivity(), "連線失敗，請重新操作", Toast.LENGTH_SHORT).show();
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