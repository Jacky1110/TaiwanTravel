package com.jotangi.nickyen.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.home.adapter.MemberCardRecycleAdapter;
import com.jotangi.nickyen.model.MemberBean;
import com.jotangi.nickyen.model.UserBean;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/7
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class MemberCardFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
{
    private ImageButton btnGoBack;
    private TextView txtNumber, noDataTV;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<MemberBean> memberList = new ArrayList<>();
    private MemberCardRecycleAdapter adapter;

    public MemberCardFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_member_card, container, false);

        ((MainActivity) getActivity()).setMenuHide(true);

        initView(view);

        return view;
    }

    private void initView(View v)
    {
        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity) getActivity()).setMenuHide(false);
                getActivity().onBackPressed();
            }
        });
        txtNumber = v.findViewById(R.id.tv_number);
        progressBar = v.findViewById(R.id.progressBar);
        noDataTV = v.findViewById(R.id.tv_noData);
        recyclerView = v.findViewById(R.id.recycleView);
        refreshLayout = v.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.typeRed);
        refreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        getMemberCardList();
    }

    private void getMemberCardList()
    {
        if (UserBean.member_id.equals(""))
        {
            noDataTV.setVisibility(View.VISIBLE);
            noDataTV.setText("請先登入帳號");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.getMemberCardList(new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<MemberBean>>()
                {
                }.getType();
                memberList = new Gson().fromJson(jsonString, type);
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        noDataTV.setVisibility(View.GONE);
                        layoutViews(memberList);
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
                        progressBar.setVisibility(View.INVISIBLE);
                        noDataTV.setVisibility(View.INVISIBLE);
//                        noDataTV.setText("網路出了點問題，請檢查網路連線...。");
                    }
                });
            }
        });
    }

    private void layoutViews(ArrayList<MemberBean> memberList)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MemberCardRecycleAdapter(memberList, getContext());
        recyclerView.setAdapter(adapter);
        String str = String.valueOf(memberList.size());
        if (str != null)
        {
            txtNumber.setText(str);
        }
    }

    @Override
    public void onRefresh()
    {
        if (UserBean.member_id.equals(""))
        {
            refreshLayout.setRefreshing(false);
            return;
        }
        refreshLayout.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                refreshLayout.setRefreshing(false);
                getMemberCardList();
            }
        }, 1000);
    }
}
