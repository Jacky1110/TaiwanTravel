package com.jotangi.nickyen.merch.reserve;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.DialogIOSUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.beautySalons.model.OrderBean;
import com.jotangi.nickyen.model.ShopBean;
import com.jotangi.nickyen.model.UserBean;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReserveDetailFragment extends Fragment implements View.OnClickListener
{
    private ImageButton btnBack;
    private Button btnNext;
    private TextView txtStoreName, txtDesigner, txtDate, txtName, txtTel, txtEmail, txtRemark, txtNoData;
    private ProgressBar progressBar;
    private LinearLayout layoutRemark;

    private RecyclerView recyclerView;
    private OrderAdapter adapter;

    private ArrayList<OrderBean> orderBeanList = new ArrayList<>();
    private ArrayList<OrderModel> orderModelList = new ArrayList<>();
    private ArrayList<ShopBean> shopBean2 = new ArrayList<>();

    private String bookingNo;
    private String designer;
    private String page;

    public ReserveDetailFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (getArguments() != null)
        {
            bookingNo = getArguments().getString("bookingNo");
            designer = getArguments().getString("designer");
            page = getArguments().getString("page");
        }
        return inflater.inflate(R.layout.fragment_order_check, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);
        btnNext = getView().findViewById(R.id.btn_next);
        btnNext.setText(R.string.reserve_confirm);
        if (page != null && page.equals("reserveConfirm"))
        {
            btnNext.setVisibility(View.VISIBLE);
        } else
        {
            btnNext.setVisibility(View.GONE);
        }
        btnNext.setOnClickListener(this);

        txtStoreName = getView().findViewById(R.id.tv_store_name);
        txtDesigner = getView().findViewById(R.id.text_designer);
        txtDate = getView().findViewById(R.id.tv_order_date);
        txtName = getView().findViewById(R.id.tv_name);
        txtTel = getView().findViewById(R.id.tv_tel);
        txtEmail = getView().findViewById(R.id.tv_email);
        txtNoData = getView().findViewById(R.id.tv_noData);
        recyclerView = getView().findViewById(R.id.recycler);
        progressBar = getView().findViewById(R.id.progressBar);

        layoutRemark = getView().findViewById(R.id.layout_remark);
        layoutRemark.setVisibility(View.VISIBLE);
        txtRemark = getView().findViewById(R.id.tv_remark);

        loadInfo(bookingNo);
    }

    private void loadInfo(String bookingNo)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.storeBookingInfo(bookingNo, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<OrderBean>>()
                {
                }.getType();
                orderBeanList = new Gson().fromJson(jsonString, type);
                String[] array = null;
                String[] array2 = null;
                for (int i = 0; i < orderBeanList.size(); i++)
                {
                    array = orderBeanList.get(0).getServiceName().split(",");
                    array2 = orderBeanList.get(0).getServicePrice().split(",");
                }
                for (int i = 0; i < array.length; i++)
                {
                    for (int j = 0; j < array2.length; j++)
                    {
                        if (i == j)

                            orderModelList.add(new OrderModel("", array[i], array2[j]));
                    }
                }
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        layoutView();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new OrderAdapter(orderModelList);
                        recyclerView.setAdapter(adapter);
                        getStoreDescript(orderBeanList.get(0).getStoreId());
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
                        txtNoData.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(null);
                    }
                });
            }
        });
    }

    private void getStoreDescript(String sid)
    {
        ApiConnection.getStoreDescript(sid, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<ShopBean>>()
                {
                }.getType();
                shopBean2 = new Gson().fromJson(jsonString, type);
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        txtStoreName.setText(shopBean2.get(0).getStoreName());
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
            }
        });
    }

    private void layoutView()
    {
        txtDate.setText(orderBeanList.get(0).getReserveDate() + " " + orderBeanList.get(0).getReserveTime());
        txtName.setText(orderBeanList.get(0).getMemberName());
        txtTel.setText(orderBeanList.get(0).getMemberId());
        txtEmail.setText(orderBeanList.get(0).getMemberEmail());
        txtRemark.setText(orderBeanList.get(0).getReserveRemark());
        if (designer != null)
        {
            txtDesigner.setText(designer);
        } else
        {
            txtDesigner.setText("不指定設計師");
        }
        if (AppUtility.isExpire(orderBeanList.get(0).getReserveDate() + orderBeanList.get(0).getReserveTime(), true) && orderBeanList.get(0).getReserveStatus().equals("0"))
        {
            btnNext.setText("已過期");
            btnNext.setClickable(false);
        }
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.ib_go_back:
                Bundle bundle = new Bundle();
                NavController controller;
                controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_reserveDetailFragment_to_reserveFragment, bundle);
                break;
            case R.id.btn_next:
                DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.reserve_send_the_appointment), getString(R.string.after_confirmation), getString(R.string.text_confirm), getString(R.string.text_cancel), new DialogIOSUtility.OnBtnClickListener()
                {
                    @Override
                    public void onCheck()
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        ApiConnection.storeBookingUpdate(orderBeanList.get(0).getBookingNo(), orderBeanList.get(0).getReserveDate(), orderBeanList.get(0).getReserveTime(), "", "3", new ApiConnection.OnConnectResultListener()
                        {
                            @Override
                            public void onSuccess(String jsonString)
                            {
                                fcmToMember(orderBeanList.get(0).getMemberId(), getString(R.string.order_has_been_successful), getString(R.string.fcm_to_member_reserve_content, UserBean.MerchName), "salonsRecord");

                                if (getActivity() == null) {
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
                                if (getActivity() == null) {
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

                    @Override
                    public void onCancel()
                    {

                    }
                });
                break;
        }
    }

    public void fcmToMember(String mobileNo, String title, String content, String extra)
    {
        ApiConnection.fcmToMember(mobileNo, title, content, extra, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
//                if (getActivity() == null) {
//                    return;
//                }
//                getActivity().runOnUiThread(() -> DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.fcm_success), "", getString(R.string.text_confirm), null, new DialogIOSUtility.OnBtnClickListener()
//                {
//                    @Override
//                    public void onCheck()
//                    {
//                        Navigation.findNavController(getView()).popBackStack();
//                    }
//
//                    @Override
//                    public void onCancel()
//                    {
//
//                    }
//                }));
            }

            @Override
            public void onFailure(String message)
            {
//                Navigation.findNavController(getView()).popBackStack();
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
        public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
        {
            return new OrderAdapter.ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salon_order, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull OrderAdapter.ViewHolder holder, int position)
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