package com.jotangi.nickyen.discount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.model.UserBean;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import static com.jotangi.nickyen.api.ApiConstant.API_IMAGE;

public class DiscountFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private TabLayout tabLayout;
    private TextView noDataTV;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refresh_layout;
    private ArrayList<CouponListBean> couponArrayList = new ArrayList<>();
    private CouponAdapter adapter;

    static String sid = "";
    static String type = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discount, container, false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCouponList(sid, type);//第一格是sid商店類別，第二格是coupon_type優惠券類別 先空著
    }

    private void getCouponList(String s, String s1) {
        if (UserBean.member_id.equals("")) {
            noDataTV.setVisibility(View.VISIBLE);
            noDataTV.setText("請先登入帳號");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.getCouponList(s, s1, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<CouponListBean>>() {
                }.getType();
                couponArrayList = new Gson().fromJson(jsonString, type);
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        noDataTV.setVisibility(View.INVISIBLE);
                        layoutViews();
                    }
                });
            }

            @Override
            public void onFailure(final String message) {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        noDataTV.setVisibility(View.VISIBLE);
                        noDataTV.setText(R.string.non_ticket);
                        recyclerView.setAdapter(null);
                    }
                });
            }
        });
    }

    private void layoutViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CouponAdapter(couponArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getView().findViewById(R.id.recycler_view);
        progressBar = getView().findViewById(R.id.progressBar);
        refresh_layout = getView().findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(R.color.typeRed);
        refresh_layout.setOnRefreshListener(this);

        noDataTV = getView().findViewById(R.id.tv_noData);
        noDataTV.setVisibility(View.INVISIBLE);

        tabLayout = getView().findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("優惠券"));
        tabLayout.addTab(tabLayout.newTab().setText("獨家優惠"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (Objects.requireNonNull(tab.getText()).toString()) {
                    case "優惠券":
                        getCouponList("", "");
                        break;
                    case "獨家優惠":
                    default:
                        recyclerView.setAdapter(null);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onRefresh() {
        if (UserBean.member_id.equals("")) {
            refresh_layout.setRefreshing(false);
            return;
        }
        refresh_layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh_layout.setRefreshing(false);
                getCouponList(sid, type);
            }
        }, 1000);
    }

    private class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

        ArrayList<CouponListBean> dataList;

        public CouponAdapter(final ArrayList<CouponListBean> dataList) {
            this.dataList = dataList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView txtTitle, txtContent, txtStartDate, txtEndDate, txtRule;
            Button btnUse;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.img_coupon);
                txtTitle = itemView.findViewById(R.id.tv_title);
                txtContent = itemView.findViewById(R.id.tv_content);
                txtRule = itemView.findViewById(R.id.tv_rule);
                txtStartDate = itemView.findViewById(R.id.tv_star_date);
                txtEndDate = itemView.findViewById(R.id.tv_end_date);
                btnUse = itemView.findViewById(R.id.btn_use);
            }
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_couponlist, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
            holder.txtTitle.setText(dataList.get(position).getCouponName());
            holder.txtContent.setText(dataList.get(position).getCouponDescription());
            holder.txtStartDate.setText(dataList.get(position).getCouponStartdate());
            holder.txtEndDate.setText(dataList.get(position).getCouponEnddate());
            holder.btnUse.setText("領\n" + "取");
            PicassoTrustAll.getInstance(getContext())
                    .load(API_IMAGE + dataList.get(position).getCouponPicture())
                    .into(holder.imageView);
            if (dataList.get(position).getCouponType().equals("3") || dataList.get(position).getCouponType().equals("6")) {
                holder.txtRule.setText("");
            } else if (dataList.get(position).getCouponDiscount().equals("1")) {

                holder.txtRule.setText("滿" + dataList.get(position).getCouponRule() + "可折抵" + dataList.get(position).getDiscountAmount() + "元");

            } else if (dataList.get(position).getCouponDiscount().equals("2")) {

                holder.txtRule.setText("滿" + dataList.get(position).getCouponRule() + "可折抵" + dataList.get(position).getDiscountAmount() + "%");
            }
            holder.btnUse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiConnection.getCoupon(dataList.get(position).getCouponId(), new ApiConnection.OnConnectResultListener() {
                        @Override
                        public void onSuccess(String jsonString) {
                            if (getActivity() == null) {
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.btnUse.setText("領\n" + "取\n" + "成\n" + "功\n");
                                    holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
                                }
                            });
                        }

                        @Override
                        public void onFailure(String message) {
                            if (getActivity() == null) {
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.btnUse.setClickable(false);
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
}