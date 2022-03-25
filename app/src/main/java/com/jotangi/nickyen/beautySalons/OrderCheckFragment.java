package com.jotangi.nickyen.beautySalons;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.beautySalons.model.ServiceBean;
import com.jotangi.nickyen.home.SalonsCheckOutActivity;
import com.jotangi.nickyen.merch.reserve.OrderModel;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.model.UserBean;
import com.jotangi.nickyen.questionnaire.QuestionnaireActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import static com.jotangi.nickyen.beautySalons.CheckDesignerFragment.shopBean;
import static com.jotangi.nickyen.beautySalons.CheckServiceFragment.designerBean;

public class OrderCheckFragment extends Fragment implements View.OnClickListener
{
    static String workDate;
    static String service = "";
    static String reserveTimes;
    static String designerId;
    private String eat = "";
    static ArrayList<ServiceBean> serviceList1;
    ArrayList<OrderModel> orderModels = new ArrayList<>();

    private ImageButton btnBack;
    private Button btnNext;
    private TextView txtTitle, txtStoreName, txtDesigner, txtDate, txtName, txtTel, txtEmail, txtPleaseNote;

    private RecyclerView recyclerView;
    private OrderAdapter adapter;


    public OrderCheckFragment()
    {
        // Required empty public constructor
    }

    //    public static OrderCheckFragment newInstance(String workDay, CheckDateFragment.NewReserve newReserve, String finalServiceItem, ArrayList<ServiceBean> serviceList)
    public static OrderCheckFragment newInstance(String workDay, String reserveTime, ArrayList<ServiceBean> serviceList, String hid)
    {
        OrderCheckFragment fragment = new OrderCheckFragment();
        Bundle args = new Bundle();
        workDate = workDay;
        reserveTimes = reserveTime;
//        service = finalServiceItem;
        serviceList1 = serviceList;
        designerId = hid;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d("豪豪", "service: " + service);
        Log.d("豪豪", "serviceList1: " + serviceList1);
        for (int i = 0; i < serviceList1.size(); i++)
        {
            if (serviceList1.get(i).isCheck())
            {

                service += serviceList1.get(i).getServiceCode() + ",";
            }
        }
        service = Optional.ofNullable(service)
                .filter(s -> s.length() != 0)
                .map(s -> s.substring(0, s.length() - 1))
                .orElse(service);
        Log.d("豪豪", "service: " + service);
        Log.d("豪豪", "serviceList1: " + serviceList1);
        return inflater.inflate(R.layout.fragment_order_check, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);
        btnNext = getView().findViewById(R.id.btn_next);
        btnNext.setText("送出預約單");
        btnNext.setOnClickListener(this);
        txtTitle = getView().findViewById(R.id.textview);
        txtTitle.setText("預約明細");
        txtStoreName = getView().findViewById(R.id.tv_store_name);
        txtDesigner = getView().findViewById(R.id.text_designer);
        txtPleaseNote = getView().findViewById(R.id.tv_content);
        txtPleaseNote.setText(R.string.salons_please_note_order_check);
        txtPleaseNote.setVisibility(View.VISIBLE);
        txtDate = getView().findViewById(R.id.tv_order_date);
        txtName = getView().findViewById(R.id.tv_name);
        txtTel = getView().findViewById(R.id.tv_tel);
        txtEmail = getView().findViewById(R.id.tv_email);
        recyclerView = getView().findViewById(R.id.recycler);

        txtStoreName.setText(shopBean.getStoreName());
        if (designerBean != null)
        {
            txtDesigner.setText(designerBean.getNickName());
        } else
        {
            txtDesigner.setText("不指定設計師");
        }
        txtDate.setText(workDate + " " + AppUtility.getWeek(workDate) + reserveTimes);
        txtName.setText(MemberInfoBean.decryptName);
        txtTel.setText(MemberInfoBean.decryptId);
        if (MemberInfoBean.decryptEmail!=null){
            txtEmail.setText(MemberInfoBean.decryptEmail);
        }

        layoutView(serviceList1);
    }

    private void layoutView(ArrayList<ServiceBean> serviceList1)
    {
        Log.d("豪豪", "layoutView: " + service);
        String[] array = service.split(",");
        if (array != null)
        {
            for (int i = 0; i < array.length; i++)
            {
                for (int j = 0; j < serviceList1.size(); j++)
                {
                    if (array[i].equals(serviceList1.get(j).getServiceCode()))
                    {
                        orderModels.add(new OrderModel(serviceList1.get(j).getServiceCode(), serviceList1.get(j).getServiceName(), serviceList1.get(j).getServicePrice()));
                    }
                }
                Log.d("豪豪", "layoutView: " + array[i]);
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new OrderAdapter(orderModels);
            recyclerView.setAdapter(adapter);

            adapter.onItemClickListener(new OrderAdapter.OnItemClickListener()
            {
                @Override
                public void onDeleteClick(int position)
                {
                    removeItem(position);
                }
            });
        }
//        ArrayList<ServiceBean> filterList = new ArrayList<>();
//        for (int i = 0; i < serviceList1.size(); i++)
//        {
//            if (serviceList1.get(i).isCheck())
//            {
//                filterList.add(serviceList1.get(i));
//            }
//        }
    }

    public void removeItem(int position)
    {
        if (orderModels.size() > 1)
        {
            orderModels.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
                service = "";
                getActivity().onBackPressed();
                break;
            case R.id.btn_next:
                SharedPreferences sP = getActivity().getSharedPreferences("useTime", Context.MODE_PRIVATE);
                long firstTime = sP.getLong("useTime", 0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                long lastTime = System.currentTimeMillis();
                long finalTime = lastTime - firstTime / 1000;
                sendIII(finalTime);
                addBooking();
                break;
        }
    }

    private void addBooking()
    {
        String finalService = "";
        for (int i = 0; i < orderModels.size(); i++)
        {
            finalService += orderModels.get(i).getCode() + ",";
        }
        finalService = Optional.ofNullable(finalService)
                .filter(s -> s.length() != 0)
                .map(s -> s.substring(0, s.length() - 1))
                .orElse(finalService);

        ApiConnection.addBooking(shopBean.getSid(), designerId, workDate, reserveTimes, finalService, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        fcmToStore(shopBean.getSid(), getString(R.string.order_conform_notify), getString(R.string.fcm_to_store_reserve_content_confirm,  MemberInfoBean.decryptName));
                        TextView txtSName, txtDate;
                        Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog_salons_reserve);
                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                        dialog.show();
                        txtSName = dialog.findViewById(R.id.tv_content);
                        txtDate = dialog.findViewById(R.id.tv_date);
                        txtSName.setText(shopBean.getStoreName());
                        txtDate.setText(workDate + " " + AppUtility.getWeek(workDate) + " " + reserveTimes);
                        Button btnLook = dialog.findViewById(R.id.btn_enter);
                        btnLook.setOnClickListener(v ->
                        {
                            if (dialog != null)
                            {
                                dialog.dismiss();
                            }
                            Intent in = new Intent(getActivity(), SalonsRecordActivity.class);
                            startActivity(in);
                            getActivity().finish();
                        });
                        Button btnReturn = dialog.findViewById(R.id.btn_close);
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

    private void sendIII(long finalTime)
    {
        String s = service.replace(",", "///");
        ApiConnection.getIIIAdd2(String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000), AppUtility.DecryptAES2(UserBean.member_id)
                , AppUtility.getIPAddress(getActivity()), String.valueOf(finalTime), shopBean.getStoreName(), s
                , new ApiConnection.OnConnectResultListener()
                {
                    @Override
                    public void onSuccess(String jsonString)
                    {
                        Log.d("豪豪", "onSuccess: " + jsonString);
                    }

                    @Override
                    public void onFailure(String message)
                    {

                    }
                });
    }

    private static class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>
    {
        private ArrayList<OrderModel> dataList;
        private OnItemClickListener listener;

        public OrderAdapter(ArrayList<OrderModel> dataList)
        {
            this.dataList = dataList;
        }

        public void onItemClickListener(OnItemClickListener onItemClickListener)
        {
            listener = onItemClickListener;
        }

        public interface OnItemClickListener
        {
            void onDeleteClick(int position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView txtItem, txtFee;
            ImageView imgDelete;

            public ViewHolder(@NonNull @NotNull View itemView)
            {
                super(itemView);
                txtItem = itemView.findViewById(R.id.tv_store_name);
                txtFee = itemView.findViewById(R.id.tv_amount);
                imgDelete = itemView.findViewById(R.id.img_delete);

                imgDelete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (listener != null)
                        {
                            int position = getBindingAdapterPosition();
                            if (position != RecyclerView.NO_POSITION)
                            {
                                listener.onDeleteClick(position);
                            }
                        }
                    }
                });
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
//            holder.txtFee.setText("$ " + dataList.get(position).getFee());
            holder.imgDelete.setVisibility(View.VISIBLE);
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        service = "";
        designerId = "";
    }
}