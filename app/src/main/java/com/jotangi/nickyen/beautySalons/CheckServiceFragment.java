package com.jotangi.nickyen.beautySalons;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.beautySalons.calendar.CheckCalendarFragment;
import com.jotangi.nickyen.beautySalons.model.DesignerBean;
import com.jotangi.nickyen.beautySalons.model.ServiceBean;
import com.jotangi.nickyen.model.UserBean;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import static com.jotangi.nickyen.beautySalons.CheckDesignerFragment.shopBean;

public class CheckServiceFragment extends BaseFragment
{
    public static DesignerBean designerBean;

    private ImageButton btnBack;
    private RecyclerView recyclerView;
    private TextView txtNoData;
    private RecyclerAdapter adapter;
    Button btnNext;
    private ProgressBar progressBar;

    private ArrayList<ServiceBean> serviceArrayList = new ArrayList<>();

    public CheckServiceFragment()
    {
        // Required empty public constructor
    }

    public static CheckServiceFragment newInstance(DesignerBean data)
    {
        CheckServiceFragment fragment = new CheckServiceFragment();
        Bundle args = new Bundle();
        designerBean = data;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d("豪豪22", "onCreateView: "+designerBean);
        return inflater.inflate(R.layout.fragment_check_service, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);
        btnNext = getView().findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        recyclerView = getView().findViewById(R.id.recycleView);
        txtNoData = getView().findViewById(R.id.tv_noData);
        progressBar = getView().findViewById(R.id.progressBar);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadData(shopBean.getSid());
    }

    private void loadData(String sid)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.getHairService(sid, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<ServiceBean>>()
                {
                }.getType();
                serviceArrayList = new Gson().fromJson(jsonString, type);

                Log.d("豪豪", "onSuccess: " + serviceArrayList);
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        layoutViews();
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
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressBar.setVisibility(View.GONE);
                                txtNoData.setVisibility(View.VISIBLE);
                                btnNext.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });
    }

    private void layoutViews()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(serviceArrayList, btnNext, getActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        designerBean = null;
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
    {
        ArrayList<ServiceBean> dataList;
        Button btn;
        Context context;


        public RecyclerAdapter(final ArrayList<ServiceBean> dataList, final Button btn, final Context context)
        {
            this.dataList = dataList;
            this.btn = btn;
            this.context = context;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView txtName, txtFee;
            CheckBox checkBox;

            public ViewHolder(@NonNull @NotNull View itemView)
            {
                super(itemView);
                txtName = itemView.findViewById(R.id.tv_name);
                txtFee = itemView.findViewById(R.id.tv_fee);
                checkBox = itemView.findViewById(R.id.checkBox);
            }

//            public void setOnClickListener(View.OnClickListener onClickListener)
//            {
//                itemView.setOnClickListener(onClickListener);
//            }
        }

        @NonNull
        @NotNull
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
        {
            return new RecyclerAdapter.ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerAdapter.ViewHolder holder, int i)
        {
            holder.txtName.setText(dataList.get(i).getServiceName());
            holder.checkBox.setChecked(dataList.get(i).isCheck());

            if (AppUtility.isStr2Int(dataList.get(i).getServicePrice()))
            {
                holder.txtFee.setText("價位：$" + dataList.get(i).getServicePrice()+" 起");
            }else {
                holder.txtFee.setText("價位：$" + dataList.get(i).getServicePrice());
            }

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {

                    if (b)
                    {
                        dataList.get(holder.getAdapterPosition()).setCheck(true);
//                        dataList.get(i).setCheck(true);
                    } else
                    {
                        dataList.get(holder.getAdapterPosition()).setCheck(false);
//                        dataList.get(i).setCheck(false);
                    }
                }
            });

            btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    String serviceItem = "";
                    for (int j = 0; j < dataList.size(); j++)
                    {
                        if (dataList.get(j).isCheck())
                        {
                            serviceItem += dataList.get(j).getServiceName() + dataList.get(j).getServicePrice() + "///";
                        }
                    }
                    serviceItem = Optional.ofNullable(serviceItem)
                            .filter(s -> s.length() != 0)
                            .map(s -> s.substring(0, s.length() - 3))
                            .orElse(serviceItem);
                    sendIII(serviceItem);

                    for (int j = 0; j < dataList.size(); j++)
                    {
                        if (dataList.get(j).isCheck() == true)
                        {
                            CheckCalendarFragment checkCalendarFragment;
                            if (designerBean != null)
                            {
                                checkCalendarFragment = CheckCalendarFragment.newInstance(designerBean.getHid(), dataList);
                            }else {
                                checkCalendarFragment = CheckCalendarFragment.newInstance("0", dataList);
                            }
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.checkServiceLayout, checkCalendarFragment, null).addToBackStack(null).commit();
                            break;
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }

        private void sendIII(String serviceItem)
        {
            ApiConnection.getIIIAdd4("選擇服務", serviceItem, String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000), AppUtility.DecryptAES2(UserBean.member_id), AppUtility.getIPAddress(getActivity()), shopBean.getStoreName(), "選擇服務", new ApiConnection.OnConnectResultListener()
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
    }
}