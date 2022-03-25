package com.jotangi.nickyen.beautySalons;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.beautySalons.model.DesignerBean;
import com.jotangi.nickyen.member.RegisterActivity;
import com.jotangi.nickyen.model.ShopBean;
import com.jotangi.nickyen.model.UserBean;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class CheckDesignerFragment extends BaseFragment
{
    public static ShopBean shopBean;

    private ImageButton btnBack;
    private RecyclerView recyclerView;
    private TextView txtNoData;
    private RecyclerAdapter adapter;
    private Button btnNotSpecify;
    private ProgressBar progressBar;

    private ArrayList<DesignerBean> designerArrayList = new ArrayList<>();


    public CheckDesignerFragment()
    {
        // Required empty public constructor
    }

    public static CheckDesignerFragment newInstance(ShopBean str)
    {
        CheckDesignerFragment fragment = new CheckDesignerFragment();
        Bundle args = new Bundle();
        shopBean = str;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_check_designer, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);
        btnNotSpecify = getView().findViewById(R.id.btn_not_specify);
        btnNotSpecify.setOnClickListener(this);
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
        ApiConnection.getHairstylist(sid, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<DesignerBean>>()
                {
                }.getType();
                designerArrayList = new Gson().fromJson(jsonString, type);
                if (getActivity() == null) {
                    return;
                }
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
                        btnNotSpecify.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void layoutViews()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(designerArrayList);
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
            case R.id.btn_not_specify:
//                getActivity().runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        Toast.makeText(getActivity(), "功能維護中，請指定設計師", Toast.LENGTH_SHORT).show();
//                    }
//                });
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.checkDesignerLayout, new CheckServiceFragment(), null).addToBackStack(null).commit();
                break;
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
    {
        private ArrayList<DesignerBean> dataList;

        public RecyclerAdapter(final ArrayList<DesignerBean> dataList)
        {
            this.dataList = dataList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            RoundedImageView riv;
            TextView txtName;
            Button btnReserve;

            public ViewHolder(@NonNull @NotNull View itemView)
            {
                super(itemView);
                txtName = itemView.findViewById(R.id.tv_name);
                riv = itemView.findViewById(R.id.riv);
                btnReserve = itemView.findViewById(R.id.btn_reserve);
            }
        }

        @NonNull
        @NotNull
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
        {
            return new RecyclerAdapter.ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_designer, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerAdapter.ViewHolder holder, int i)
        {
            holder.txtName.setText(dataList.get(i).getNickName());
            String imagerUrl = ApiConstant.API_IMAGE + dataList.get(i).getStylistPic();
            PicassoTrustAll.getInstance((holder.riv.getContext())).load(imagerUrl).into(holder.riv);
            holder.btnReserve.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CheckServiceFragment checkServiceFragment = CheckServiceFragment.newInstance(dataList.get(i));
                    sendIII(dataList.get(i).getNickName()); //送出設計師頁的功能點擊喜好
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.checkDesignerLayout, checkServiceFragment, null).addToBackStack(null).commit();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }

    private void sendIII(String nickName)
    {
        ApiConnection.getIIIAdd4("設計師", nickName, String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000), AppUtility.DecryptAES2(UserBean.member_id), AppUtility.getIPAddress(getActivity()), shopBean.getStoreName(), "設計師", new ApiConnection.OnConnectResultListener()
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