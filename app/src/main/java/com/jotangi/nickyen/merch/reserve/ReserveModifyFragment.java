package com.jotangi.nickyen.merch.reserve;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.List;
import java.util.stream.Collectors;

public class ReserveModifyFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
    private ImageButton btnBack;
    private Button btnReserveConfirm, btnReserveCancel;
    private TextView txtStoreName, txtDesigner, txtDate, txtName, txtTel, txtEmail, txtCostEstimate, txtNoData;
    private ProgressBar progressBar;
    private Spinner spinnerTime;
    private EditText edtRemark;

    private RecyclerView recyclerView;
    private OrderAdapter adapter;

    private ArrayList<OrderBean> orderBeanList = new ArrayList<>();
    private ArrayList<OrderModel> orderModelList = new ArrayList<>();
    private ArrayList<ShopBean> shopBean2 = new ArrayList<>();

    private String bookingNo, designer, strTime;
    private StringBuilder s = new StringBuilder();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            bookingNo = getArguments().getString("bookingNo");
            designer = getArguments().getString("designer");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserve_modify, container, false);
//        String strMsg1 = "<font color=\"#000000\">信用卡一次付清</font><br><font color=\"#7F7F7F\">VISA/JCB/MasterCard</font>";
//        txtCredit.setText(Html.fromHtml(strMsg1));
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);
        btnReserveConfirm = getView().findViewById(R.id.btn_reserve_confirm);
        btnReserveConfirm.setOnClickListener(this);
        btnReserveCancel = getView().findViewById(R.id.btn_reserve_cancel);
        btnReserveCancel.setOnClickListener(this);

        txtStoreName = getView().findViewById(R.id.tv_store_name);
        txtDesigner = getView().findViewById(R.id.text_designer);
        txtDate = getView().findViewById(R.id.tv_order_date);
        txtName = getView().findViewById(R.id.tv_name);
        txtTel = getView().findViewById(R.id.tv_tel);
        txtNoData = getView().findViewById(R.id.tv_noData);
        txtEmail = getView().findViewById(R.id.tv_email);
        recyclerView = getView().findViewById(R.id.recycler);
        spinnerTime = getView().findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.reserve_ime, R.layout.spinner_item);
        spinnerTime.setAdapter(spinnerAdapter);
        spinnerTime.setOnItemSelectedListener(this);
        edtRemark = getView().findViewById(R.id.edt_remark);
        progressBar = getView().findViewById(R.id.progressBar);
        txtCostEstimate = getView().findViewById(R.id.tv_cost_estimate);

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
                int count = 0;
                String[] splitArrays = null;
                for (int i = 0; i < array2.length; i++)
                {
                    Log.d("豪豪1", "onSuccess: " + array2[i]);
                    List<String> l = new ArrayList<>();
                    l.add(array2[i]);
                    Log.d("豪豪2", "onSuccess: " + l);
                    for (int j = 0; j < l.size(); j++)
                    {
                        if (l.get(j).contains("-"))
                        {
                            ArrayList<String> oldData = new ArrayList<>();
                            oldData.add(l.get(j));
                            ArrayList<String> ccc = (ArrayList<String>) oldData.stream().distinct().collect(Collectors.toList()); //剔除重複

                            for (int k = 0; k < ccc.size(); k++)
                            {
                                Log.d("豪豪3", "onSuccess: " + ccc.get(k));
                                s.append(ccc.get(k) + "-");
                            }
                        }
//                        else if (l.get(j).contains("起"))
                        else
                        {
                            count += Integer.parseInt(l.get(j));
                        }
                    }
                }
                splitArrays = s.toString().split("-");
                int min = 0;
                int max = 0;

                for (int i = 0; i < splitArrays.length; i++)
                {
                    if (i % 2 == 1 && !splitArrays[i].isEmpty())
                    {
                        max += Integer.parseInt(splitArrays[i].trim());
                    }
                    else if (!splitArrays[i].isEmpty())
                    {
                        min += Integer.parseInt(splitArrays[i].trim());
                    }
                }

                String finalMin = String.valueOf(min + count);
                String finalMax = String.valueOf(max + count);
                int finalCount = count;
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
                        if (!finalMin.equals(String.valueOf(finalCount)))
                        {
                            txtCostEstimate.setText("$" + finalMin + "-" + finalMax +"起");
                        } else
                        {
                            txtCostEstimate.setText("$" + finalMin + "起");
                        }
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
        if (designer != null)
        {
            txtDesigner.setText(designer);
        } else
        {
            txtDesigner.setText("不指定設計師");
        }
    }

    @Override
    public void onClick(View v)
    {
        final String strRemark = edtRemark.getText().toString().trim();
        switch (v.getId())
        {
            case R.id.ib_go_back:
                Navigation.findNavController(getView()).popBackStack();
                break;
            case R.id.btn_reserve_confirm:
                DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.confirm_to_modify_the_appointment), getString(R.string.after_confirmation), getString(R.string.text_confirm), getString(R.string.text_cancel), new DialogIOSUtility.OnBtnClickListener()
                {
                    @Override
                    public void onCheck()
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        ApiConnection.storeBookingUpdate(orderBeanList.get(0).getBookingNo(), orderBeanList.get(0).getReserveDate(), strTime, strRemark, "1", new ApiConnection.OnConnectResultListener()
                        {
                            @Override
                            public void onSuccess(String jsonString)
                            {
                                fcmToMember(orderBeanList.get(0).getMemberId(),getString(R.string.order_has_been_modify),getString(R.string.fcm_to_member_reserve_content, UserBean.MerchName), "salonsRecord");

                                if (getActivity() == null) {
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
            case R.id.btn_reserve_cancel:
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_ios_2);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                TextView txtTitle = dialog.findViewById(R.id.message_title);
                TextView txtContent = dialog.findViewById(R.id.message_content);
                TextView txtMessage = dialog.findViewById(R.id.tv_message);
                Button btnConfirm = dialog.findViewById(R.id.btnCheck);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                txtTitle.setText(R.string.reserve_confirm_cancel);
                txtContent.setText(R.string.fcm_merch_refuse);
                txtMessage.setText(getString(R.string.reserve_answer, UserBean.MerchName));
                btnConfirm.setOnClickListener(v1 ->
                {
                    if (dialog != null)
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        ApiConnection.storeBookingUpdate(orderBeanList.get(0).getBookingNo(), orderBeanList.get(0).getReserveDate(), orderBeanList.get(0).getReserveTime(), strRemark, "2", new ApiConnection.OnConnectResultListener()
                        {
                            @Override
                            public void onSuccess(String jsonString)
                            {
                                fcmToMember(orderBeanList.get(0).getMemberId(),getString(R.string.order_has_been_cancel),getString(R.string.fcm_to_member_reserve_content,  UserBean.MerchName), "salonsRecord");

                                if (getActivity() == null) {
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
                break;
        }
    }

    private void fcmToMember(String mobileNo, String title, String content, String extra)
    {
        ApiConnection.fcmToMember(mobileNo, title, content, extra, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
//                getActivity().runOnUiThread(() -> DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.fcm_success), "", getString(R.string.text_confirm), null, new DialogIOSUtility.OnBtnClickListener()
//                {
//                    @Override
//                    public void onCheck()
//                    {
////                        Navigation.findNavController(getView()).popBackStack();
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
//                getActivity().runOnUiThread(() -> DialogIOSUtility.showMyDialog(getActivity(), getString(R.string.fcm_failure), getString(R.string.please_check_the_connection_or_try_again), getString(R.string.text_confirm), null, new DialogIOSUtility.OnBtnClickListener()
//                {
//                    @Override
//                    public void onCheck()
//                    {
//
//                    }
//
//                    @Override
//                    public void onCancel()
//                    {
//
//                    }
//                }));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        strTime = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

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
                holder.txtFee.setText("價位：$" + dataList.get(position).getFee()+"起");
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