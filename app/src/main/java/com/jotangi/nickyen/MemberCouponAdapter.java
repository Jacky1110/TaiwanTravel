package com.jotangi.nickyen;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.home.MyDiscountNew2Activity;
import com.jotangi.nickyen.home.adapter.CouponRecyclerAdapter;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.shop.MemberQRActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MemberCouponAdapter extends RecyclerView.Adapter<MemberCouponAdapter.ViewHolder> {

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtContent, txtStartDate, txtEndDate, txtRule;
        ImageView imgCoupon;
        Button btnUse;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.tv_title);
            txtContent = itemView.findViewById(R.id.tv_content);
            txtRule = itemView.findViewById(R.id.tv_rule);
            txtStartDate = itemView.findViewById(R.id.tv_star_date);
            txtEndDate = itemView.findViewById(R.id.tv_end_date);
            imgCoupon = itemView.findViewById(R.id.img_coupon);
            btnUse = itemView.findViewById(R.id.btn_use);

        }
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_membercoupon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtTitle.setText(couponList.get(position).getCouponName());
        holder.txtContent.setText(couponList.get(position).getCouponDescription());
        holder.txtStartDate.setText(couponList.get(position).getCouponStartdate());
        holder.txtEndDate.setText(couponList.get(position).getCouponEnddate());

        if (couponList.get(position).getCouponType().equals("7") || couponList.get(position).getCouponType().equals("3") || couponList.get(position).getCouponType().equals("6")) {

            holder.txtRule.setText("");

        } else if (couponList.get(position).getCouponDiscount().equals("1")) {

            holder.txtRule.setText("滿" + couponList.get(position).getCouponRule() + "可折抵" + couponList.get(position).getDiscountAmount() + "元");

        } else if (couponList.get(position).getCouponDiscount().equals("2")) {

            holder.txtRule.setText("滿" + couponList.get(position).getCouponRule() + "可折抵" + couponList.get(position).getDiscountAmount() + "%");
        }
        String imagerUrl = ApiConstant.API_IMAGE + couponList.get(position).getCouponPicture();
        PicassoTrustAll.getInstance((holder.imgCoupon.getContext())).load(imagerUrl).into(holder.imgCoupon);
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }
}
