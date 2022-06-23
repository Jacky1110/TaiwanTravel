package com.jotangi.nickyen;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.home.model.CouponListBean;

import java.util.ArrayList;

public class CouponViewHolder extends RecyclerView.ViewHolder{

    private ArrayList<CouponListBean> couponList;

    TextView txtTitle, txtContent, txtStartDate, txtEndDate, txtRule;
    ImageView imgCoupon;
    Button btnUse;

    public CouponViewHolder(@NonNull View itemView) {
        super(itemView);

        txtTitle = itemView.findViewById(R.id.tv_title);
        txtContent = itemView.findViewById(R.id.tv_content);
        txtRule = itemView.findViewById(R.id.tv_rule);
        txtStartDate = itemView.findViewById(R.id.tv_star_date);
        txtEndDate = itemView.findViewById(R.id.tv_end_date);
        imgCoupon = itemView.findViewById(R.id.img_coupon);
        btnUse = itemView.findViewById(R.id.btn_use);
    }

    public void bind(CouponListBean data) {
        txtTitle.setText(data.getCouponName());
        txtContent.setText(data.getCouponDescription());
        txtStartDate.setText(data.getCouponStartdate());
        txtEndDate.setText(data.getCouponEnddate());

        if (data.getCouponType().equals("7") || data.getCouponType().equals("3") || data.getCouponType().equals("6")) {

            txtRule.setText("");

        } else if (data.getCouponDiscount().equals("1")) {

            txtRule.setText("滿" + data.getCouponRule() + "可折抵" + data.getDiscountAmount() + "元");

        } else if (data.getCouponDiscount().equals("2")) {

            txtRule.setText("滿" + data.getCouponRule() + "可折抵" + data.getDiscountAmount() + "%");
        }
        String imagerUrl = ApiConstant.API_IMAGE + data.getCouponPicture();
        PicassoTrustAll.getInstance((imgCoupon.getContext())).load(imagerUrl).into(imgCoupon);
    }




}
