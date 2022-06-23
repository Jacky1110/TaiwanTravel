package com.jotangi.nickyen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.nickyen.home.model.CouponListBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MemberCouponAdapter extends RecyclerView.Adapter {

    private String TAG = MemberAdapter.class.getSimpleName() + "(TAG)";
    private ArrayList<CouponListBean> couponList;
    private Context context;
    private String discount;
    private String total;

    public MemberCouponAdapter(final ArrayList<CouponListBean> couponList, final Context context, final String discount, final String total) {
        this.couponList = couponList;
        this.context = context;
        this.discount = discount;
        this.total = total;
    }

//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView txtTitle, txtContent, txtStartDate, txtEndDate, txtRule;
//        ImageView imgCoupon;
//        Button btnUse;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            txtTitle = itemView.findViewById(R.id.tv_title);
//            txtContent = itemView.findViewById(R.id.tv_content);
//            txtRule = itemView.findViewById(R.id.tv_rule);
//            txtStartDate = itemView.findViewById(R.id.tv_star_date);
//            txtEndDate = itemView.findViewById(R.id.tv_end_date);
//            imgCoupon = itemView.findViewById(R.id.img_coupon);
//            btnUse = itemView.findViewById(R.id.btn_use);
//
//        }
//    }


    @NotNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_membercoupon, parent, false);
        return new CouponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CouponListBean data = couponList.get(position);
        ((CouponViewHolder) holder).bind(data);

    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }
}
