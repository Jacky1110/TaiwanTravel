package com.jotangi.nickyen.beautySalons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.DialogIOSUtility;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.beautySalons.model.DesignerBean;
import com.jotangi.nickyen.beautySalons.model.OrderBean;
import com.jotangi.nickyen.beautySalons.model.ServiceBean;
import com.jotangi.nickyen.home.SalonsCheckOutActivity;
import com.jotangi.nickyen.industry.IndustryActivity;
import com.jotangi.nickyen.merch.reserve.OrderModel;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderCheckFragment2 extends Fragment implements View.OnClickListener
{
    static OrderBean orderBean;

    private ImageButton btnBack;
    private Button btnNext, btnCancel;
    private TextView txtTitle, txtStoreName, txtDesigner, txtDate, txtName, txtTel, txtEmail, txtPleaseNote, txtRemark;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private OrderAdapter adapter;

    private LinearLayout layoutRemark;

    private ArrayList<ServiceBean> serviceArrayList = new ArrayList<>();

    //==此為會員專區進來的預約記錄不是從美容美髮頁面進來的
    public OrderCheckFragment2()
    {
        // Required empty public constructor
    }

    public static OrderCheckFragment2 newInstance(OrderBean data)
    {
        OrderCheckFragment2 fragment = new OrderCheckFragment2();
        Bundle args = new Bundle();
        orderBean = data;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_order_check, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);
        btnNext = getView().findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        btnCancel = getView().findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        txtPleaseNote = getView().findViewById(R.id.tv_content);

        if (AppUtility.isExpire(orderBean.getReserveDate() + orderBean.getReserveTime(), true))
        {
            btnNext.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        } else
        {
            if (orderBean.getReserveStatus().equals("0")) // 預約尚未確認
            {
                btnNext.setText(R.string.booking_yet);
                btnNext.setBackgroundResource(R.drawable.shape_type_gray);
            } else if (orderBean.getReserveStatus().equals("1")) // 預約已回覆
            {
                btnNext.setText(R.string.cancel_booking);
                btnCancel.setText(R.string.rebooking);
                btnCancel.setVisibility(View.VISIBLE);
                txtPleaseNote.setText(R.string.salons_please_note_order_check2);
                txtPleaseNote.setVisibility(View.VISIBLE);
            } else if (orderBean.getReserveStatus().equals("2"))  // 預約已取消
            {
                btnNext.setVisibility(View.GONE);
                btnCancel.setText(R.string.rebooking);
                btnCancel.setVisibility(View.VISIBLE);
                txtPleaseNote.setText(R.string.salons_please_note_order_check3);
                txtPleaseNote.setVisibility(View.VISIBLE);
            } else // 預約已完成
            {
                btnNext.setVisibility(View.GONE);
                btnCancel.setText(R.string.cancel_booking);
                btnCancel.setVisibility(View.VISIBLE);
            }
        }
        txtTitle = getView().findViewById(R.id.textview);
        txtTitle.setText("預約明細");
        txtStoreName = getView().findViewById(R.id.tv_store_name);
        txtDesigner = getView().findViewById(R.id.text_designer);
        if (orderBean.getNickName() != null)
        {
            txtDesigner.setText(orderBean.getNickName());
        } else
        {
            txtDesigner.setText("不指定設計師");
        }
        txtDate = getView().findViewById(R.id.tv_order_date);
        txtName = getView().findViewById(R.id.tv_name);
        txtTel = getView().findViewById(R.id.tv_tel);
        txtEmail = getView().findViewById(R.id.tv_email);
        recyclerView = getView().findViewById(R.id.recycler);
        progressBar = getView().findViewById(R.id.progressBar);

        txtStoreName.setText(orderBean.getStoreName());
        txtDate.setText(orderBean.getReserveDate() + " " + AppUtility.getWeek(orderBean.getReserveDate()) + orderBean.getReserveTime());
        txtName.setText(orderBean.getMemberName());
        txtTel.setText(orderBean.getMemberId());
        txtEmail.setText(orderBean.getMemberEmail());

        layoutRemark = getView().findViewById(R.id.layout_remark);
        layoutRemark.setVisibility(View.VISIBLE);
        txtRemark = getView().findViewById(R.id.tv_remark);
        txtRemark.setText(orderBean.getReserveRemark());

        loadServiceData(orderBean.getStoreId());
    }


    private void loadServiceData(String storeId)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.getHairService(storeId, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<ServiceBean>>()
                {
                }.getType();
                serviceArrayList = new Gson().fromJson(jsonString, type);

                Log.d("豪豪", "onSuccess: " + serviceArrayList);
                if (getActivity() == null)
                {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        layoutView(serviceArrayList);
                        progressBar.setVisibility(View.GONE);
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
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void layoutView(ArrayList<ServiceBean> serviceList1)
    {
        ArrayList<OrderModel> orderModels = new ArrayList<>();

        String[] array = orderBean.getServiceItem().split(",");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(array));

        if (array != null)
        {
            for (int i = 0; i < list.size(); i++)
            {
                for (int j = 0; j < serviceList1.size(); j++)
                {
                    if (list.get(i).equals(serviceList1.get(j).getServiceCode()))
                    {
                        orderModels.add(new OrderModel(serviceList1.get(j).getServiceCode(), serviceList1.get(j).getServiceName(), serviceList1.get(j).getServicePrice()));
                    }
                }
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new OrderAdapter(orderModels);
            recyclerView.setAdapter(adapter);

        } else
        {
            recyclerView.setAdapter(null);
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
                if (btnNext.getText().equals(getString(R.string.cancel_booking)))
                {
                    AppUtility.showMyDialog(getActivity(), "本次預約是否取消？\n提醒：若取消預約三次，會被系統加入黑名單，且無法使用線上預約，需要店家解除黑名單才可以再次預約。", "確認取消", "取消", new AppUtility.OnBtnClickListener()
                    {
                        @Override
                        public void onCheck()
                        {
                            cancelBooking(orderBean.getBookingNo(), false);
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
                            cancelBooking(orderBean.getBookingNo(), false);
                        }

                        @Override
                        public void onCancel()
                        {

                        }
                    });
                } else if (btnCancel.getText().equals(getString(R.string.rebooking)))
                {
                    if (orderBean.getReserveStatus().equals("2"))
                    {
                        startActivity(new Intent(getActivity(), BeautySalonsActivity.class));
                        getActivity().finish();
                    } else
                    {
                        cancelBooking(orderBean.getBookingNo(), true);
                    }
                }
                break;
        }
    }

    private void cancelBooking(String bookingNo, boolean isRebooking)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.cancelBooking(bookingNo, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "您已取消成功！", Toast.LENGTH_SHORT).show();
                        fcmToStore(orderBean.getStoreId(), getString(R.string.order_cancel_notify), getString(R.string.fcm_to_store_reserve_content_cancel, orderBean.getMemberName()));
                        if (isRebooking)
                        {
                            startActivity(new Intent(getActivity(), BeautySalonsActivity.class));
                        }
                        getActivity().finish();
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
                        progressBar.setVisibility(View.GONE);
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

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>
    {
        private ArrayList<OrderModel> dataList;

        public OrderAdapter(ArrayList<OrderModel> dataList)
        {
            this.dataList = dataList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView txtItem, txtFee;

            public ViewHolder(@NonNull @NotNull View itemView)
            {
                super(itemView);
                txtItem = itemView.findViewById(R.id.tv_store_name);
                txtFee = itemView.findViewById(R.id.tv_amount);
            }
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
        {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salon_order, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position)
        {
            holder.txtItem.setText(dataList.get(position).getItem());
            if (AppUtility.isStr2Int(dataList.get(position).getFee()))
            {
                holder.txtFee.setText("價位：$" + dataList.get(position).getFee()+" 起");
            }else {
                holder.txtFee.setText("價位：$" + dataList.get(position).getFee());
            }
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }
}