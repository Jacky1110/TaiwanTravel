package com.jotangi.nickyen.merch.industryReserve;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.DialogIOSUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.industry.model.IndustryOrderBean;
import com.jotangi.nickyen.model.UserBean;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class IndustryReserveDetailFragment extends Fragment implements View.OnClickListener
{

    private ImageButton btnBack;
    private Button btnNext, btnReNotify;
    private TextView txtProgramName, txtDate, txtProgramAddress, txtProgramTel, txtName, txtTel, txtMail, txtOrderItem, txtAmount, txtRemark, txtNoData;
    private ProgressBar progressBar;
    private LinearLayout layoutRemark;

    private IndustryOrderBean industryOrderBean;
    private ArrayList<IndustryOrderBean> newBeanList = new ArrayList<>();

    private String page;
    private String price;
    private String fee = "";

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            industryOrderBean = new Gson().fromJson(getArguments().getString("order"), IndustryOrderBean.class);
            page = getArguments().getString("page");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_industry_order_check, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);

        layoutRemark = getView().findViewById(R.id.layout_remark);
        layoutRemark.setVisibility(View.VISIBLE);
        txtRemark = getView().findViewById(R.id.tv_remark);

        btnNext = getView().findViewById(R.id.btn_next);
        if (page != null && page.equals("reserveConfirm"))
        {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setText(R.string.reserve_confirm);
            if (industryOrderBean.getReserveNote() != null)
            {
                txtRemark.setText(industryOrderBean.getReserveNote());
            }

        } else if ((industryOrderBean.getReserveStatus().equals("1") || industryOrderBean.getReserveStatus().equals("3")) && !AppUtility.isExpire(industryOrderBean.getReserveDate() + industryOrderBean.getReserveTime(), true))
        {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setText("預約提醒");

            btnReNotify = getView().findViewById(R.id.btn_cancel);
            btnReNotify.setText(R.string.cancel_booking);
            btnReNotify.setVisibility(View.VISIBLE);
            btnReNotify.setOnClickListener(this);

            if (industryOrderBean.getReserveNote() == null || industryOrderBean.getReserveNote().equals(""))
            {
                txtRemark.setText(industryOrderBean.getReserveRemark());
            } else
            {
                txtRemark.setText(industryOrderBean.getReserveNote() + "\n" + industryOrderBean.getReserveRemark());
            }

        } else
        {
            btnNext.setVisibility(View.GONE);

            if (industryOrderBean.getReserveNote() == null || industryOrderBean.getReserveNote().equals(""))
            {
                txtRemark.setText(industryOrderBean.getReserveRemark());
            } else
            {
                txtRemark.setText(industryOrderBean.getReserveNote() + "\n" + industryOrderBean.getReserveRemark());
            }
        }
        btnNext.setOnClickListener(this);


//        if (AppUtility.isExpire(industryOrderBean.getReserveDate() + industryOrderBean.getReserveTime(),true) && industryOrderBean.getReserveStatus().equals("0"))
//        {
//            btnNext.setText("已過期");
//            btnNext.setClickable(false);
//        } else
//        {
//            if (industryOrderBean.getReserveStatus().equals("0"))
//            {
//
//                btnNext.setText("結帳");
//            } else
//            {
//                btnNext.setText("已付款");
//                btnNext.setBackgroundResource(R.drawable.shape_type_gray);
//                btnNext.setClickable(false);
//            }
//        }

        txtProgramName = getView().findViewById(R.id.tv_store_name);
        txtDate = getView().findViewById(R.id.tv_order_date);
        txtProgramAddress = getView().findViewById(R.id.tv_order_address);
        txtProgramTel = getView().findViewById(R.id.tv_order_tel);
        txtName = getView().findViewById(R.id.tv_name);
        txtTel = getView().findViewById(R.id.tv_tel);
        txtMail = getView().findViewById(R.id.tv_mail);
        txtNoData = getView().findViewById(R.id.tv_noData);
        txtOrderItem = getView().findViewById(R.id.tv_order_item);
        txtAmount = getView().findViewById(R.id.tv_item_amount);
        progressBar = getView().findViewById(R.id.progressBar);

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

        loadData(industryOrderBean.getBookingNo());
    }

    private void loadData(String bookingNo)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.storeClassBookingInfo(bookingNo, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<IndustryOrderBean>>()
                {
                }.getType();
                newBeanList = new Gson().fromJson(jsonString, type);
                if (getActivity() == null)
                {
                    return;
                }
                getActivity().runOnUiThread(() ->
                {
                    progressBar.setVisibility(View.GONE);
                    txtProgramAddress.setText(newBeanList.get(0).getStoreAddress());
                    txtProgramTel.setText(newBeanList.get(0).getStorePhone());
                });
            }

            @Override
            public void onFailure(String message)
            {
                progressBar.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
                Navigation.findNavController(getView()).popBackStack();
                break;
            case R.id.btn_next:
//                Bundle bundle = new Bundle();
//                bundle.putString("order",new Gson().toJson(newBeanList.get(0)));
//                Navigation.findNavController(v).navigate(R.id.action_industryReserveDetailFragment_to_industryReserveInputFragment, bundle);
//                break;
                DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.reserve_send_the_appointment), getString(R.string.after_confirmation), getString(R.string.text_confirm), getString(R.string.text_cancel), new DialogIOSUtility.OnBtnClickListener()
                {
                    @Override
                    public void onCheck()
                    {
                        if (btnNext.getText().equals("預約提醒"))
                        {
                            fcmToMember(industryOrderBean.getMemberId(), "預約提醒通知", getString(R.string.fcm_to_member_remind,  "【"+UserBean.MerchName+industryOrderBean.getProgramName()+"】", "【"+industryOrderBean.getReserveDate()+" "+industryOrderBean.getReserveTime()+"】"), "industryRecord");
                            Navigation.findNavController(getView()).popBackStack();

                        } else
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            ApiConnection.classBookingUpdate(industryOrderBean.getBookingNo(), industryOrderBean.getReserveDate(), industryOrderBean.getReserveTime(), industryOrderBean.getProgramPerson(), "3", "", new ApiConnection.OnConnectResultListener()
                            {
                                @Override
                                public void onSuccess(String jsonString)
                                {
                                    fcmToMember(industryOrderBean.getMemberId(), getString(R.string.order_has_been_successful), getString(R.string.fcm_to_member_reserve_content, UserBean.MerchName), "industryRecord");

                                    if (getActivity() == null)
                                    {
                                        return;
                                    }
                                    getActivity().runOnUiThread(() ->
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        btnNext.setVisibility(View.GONE);
                                        Navigation.findNavController(getView()).popBackStack();
                                    });
                                }

                                @Override
                                public void onFailure(String message)
                                {
                                    if (getActivity() == null)
                                    {
                                        return;
                                    }
                                    getActivity().runOnUiThread(() ->
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.order_has_not_been_successful), getString(R.string.please_check_the_connection_or_try_again), getString(R.string.text_confirm), null, new DialogIOSUtility.OnBtnClickListener()
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
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancel()
                    {

                    }
                });
                break;
            case R.id.btn_cancel:
                if (btnReNotify.getText().equals(getString(R.string.cancel_booking)))
                {
                    cancelBooking();
                }
                break;
        }
    }

    private void cancelBooking()
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_ios_2);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        TextView txtTitle = dialog.findViewById(R.id.message_title);
        TextView txtContent = dialog.findViewById(R.id.message_content);
        EditText editMessage = dialog.findViewById(R.id.tv_message);
        Button btnConfirm = dialog.findViewById(R.id.btnCheck);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        txtTitle.setText(R.string.reserve_confirm_cancel);
        txtContent.setText(R.string.fcm_merch_refuse);
        String preset = getString(R.string.reserve_answer, UserBean.MerchName);
        editMessage.setHint(preset);
        btnConfirm.setOnClickListener(v1 ->
        {
            String custom = editMessage.getText().toString().trim();
            if (dialog != null)
            {
                progressBar.setVisibility(View.VISIBLE);
                ApiConnection.classBookingUpdate(industryOrderBean.getBookingNo(), industryOrderBean.getReserveDate(), industryOrderBean.getReserveTime(), "", "2", industryOrderBean.getProgramPerson(), new ApiConnection.OnConnectResultListener()
                {
                    @Override
                    public void onSuccess(String jsonString)
                    {

                        if (!custom.equals(""))
                        {
                            fcmToMember(industryOrderBean.getMemberId(), getString(R.string.order_has_been_cancel), custom, "industryRecord");
                        } else
                        {
                            fcmToMember(industryOrderBean.getMemberId(), getString(R.string.order_has_been_cancel), preset, "industryRecord");
                        }

                        if (getActivity() == null)
                        {
                            return;
                        }
                        getActivity().runOnUiThread(() ->
                        {
                            progressBar.setVisibility(View.GONE);
                            Navigation.findNavController(getView()).popBackStack();
                        });
                    }

                    @Override
                    public void onFailure(String message)
                    {
                        if (getActivity() == null)
                        {
                            return;
                        }
                        getActivity().runOnUiThread(() ->
                        {
                            progressBar.setVisibility(View.GONE);
                            DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.order_has_not_been_successful), getString(R.string.please_check_the_connection_or_try_again), getString(R.string.text_confirm), null, new DialogIOSUtility.OnBtnClickListener()
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
                        });
                    }
                });
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(v2 ->
        {
            if (dialog != null)
            {
                dialog.dismiss();
            }
        });
    }

    public void fcmToMember(String mobileNo, String title, String content, String extra)
    {
        ApiConnection.fcmToMember(mobileNo, title, content, extra, new ApiConnection.OnConnectResultListener()
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