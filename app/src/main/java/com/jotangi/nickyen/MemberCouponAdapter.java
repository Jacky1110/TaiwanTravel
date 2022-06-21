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

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_membercoupon, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

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

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(couponList.get(position).getCouponEnddate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date dateFuture = new Date();
        try {
            dateFuture = simpleDateFormat.parse(couponList.get(position).getCouponStartdate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (couponList.get(position).getUsingFlag().equals("1")) {
            holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
            holder.btnUse.setText("已\n" + "使\n" + "用");
            holder.btnUse.setClickable(false);

        } else if (couponList.get(position).getUsingFlag().equals("0") && systemTime > date.getTime() + 86400000) {
            holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
            holder.btnUse.setText("已\n" + "過\n" + "期");
            holder.btnUse.setClickable(false);
        } else if (couponList.get(position).getUsingFlag().equals("0") && systemTime < dateFuture.getTime()) {
            holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
            holder.btnUse.setText("使\n" + "用\n");
            holder.btnUse.setClickable(false);
        } else {
            //針對積點折抵進來商品券無法使用由店長端
            if (discount != null && discount.equals("1") && (couponList.get(position).getCouponType().equals("3") || couponList.get(position).getCouponType().equals("6") || couponList.get(position).getCouponType().equals("7"))) {
                holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
                holder.btnUse.setText("無\n" + "法\n" + "使\n" + "用");
                holder.btnUse.setClickable(false);
            } else if (total != null && !total.equals("") && discount.equals("1") && Integer.parseInt(total) < Integer.parseInt(couponList.get(position).getCouponRule())) { //這是對美髮結帳處理的金額不足無法使用優惠券由user端
                holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
                holder.btnUse.setText("消\n" + "費\n" + "不\n" + "足");
                holder.btnUse.setClickable(false);
            } else {
                holder.btnUse.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        if (MyDiscountNew2Activity.class.isInstance(context))
                        {
                            // 轉化為activity，然后finish就行了
                            MyDiscountNew2Activity activity = (MyDiscountNew2Activity) context;
                            Intent i;
                            if (discount != null && discount.equals("1"))
                            { //從有關結帳頁面進來選取的
                                i = new Intent();
                                i.putExtra("dis", couponList.get(position).getCouponNo());
                                i.putExtra("name", couponList.get(position).getCouponName());
                                i.putExtra("coupon", new Gson().toJson(couponList.get(position))); //美容美髮要用到的
                                activity.setResult(RESULT_OK, i);
                            } else
                            { //普通優惠券頁面進入
                                i = new Intent(context, MemberQRActivity.class);
                                i.putExtra("ticket", new Gson().toJson(couponList.get(position)));
                                activity.startActivity(i);

                            }
                            activity.finish();
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }
}
