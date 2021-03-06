package com.jotangi.nickyen.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.home.MyDiscountNew2Activity;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.shop.MemberQRActivity;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/17
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class CouponRecyclerAdapter extends RecyclerView.Adapter<CouponRecyclerAdapter.ViewHolder>
{
    private ArrayList<CouponListBean> couponList;
    private Context context;
    private String discount;
    private String total;

    public CouponRecyclerAdapter(final ArrayList<CouponListBean> couponList, final Context context, final String discount, final String total)
    {
        this.couponList = couponList;
        this.context = context;
        this.discount = discount;
        this.total = total;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitle, txtContent, txtStartDate, txtEndDate, txtRule;
        ImageView imgCoupon;
        Button btnUse;

        public ViewHolder(@NonNull @NotNull View itemView)
        {
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
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_couponlist, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position)
    {
        holder.txtTitle.setText(couponList.get(position).getCouponName());
        holder.txtContent.setText(couponList.get(position).getCouponDescription());
        holder.txtStartDate.setText(couponList.get(position).getCouponStartdate());
        holder.txtEndDate.setText(couponList.get(position).getCouponEnddate());

        if (couponList.get(position).getCouponType().equals("7") || couponList.get(position).getCouponType().equals("3") || couponList.get(position).getCouponType().equals("6"))
        {
            holder.txtRule.setText("");
        } else if (couponList.get(position).getCouponDiscount().equals("1"))
        {

            holder.txtRule.setText("???" + couponList.get(position).getCouponRule() + "?????????" + couponList.get(position).getDiscountAmount() + "???");

        } else if (couponList.get(position).getCouponDiscount().equals("2"))
        {

            holder.txtRule.setText("???" + couponList.get(position).getCouponRule() + "?????????" + couponList.get(position).getDiscountAmount() + "%");
        }
        String imagerUrl = ApiConstant.API_IMAGE + couponList.get(position).getCouponPicture();
        PicassoTrustAll.getInstance((holder.imgCoupon.getContext())).load(imagerUrl).into(holder.imgCoupon);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try
        {
            date = simpleDateFormat.parse(couponList.get(position).getCouponEnddate());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Date dateFuture = new Date();
        try
        {
            dateFuture = simpleDateFormat.parse(couponList.get(position).getCouponStartdate());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (couponList.get(position).getUsingFlag().equals("1"))
        {
            holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
            holder.btnUse.setText("???\n" + "???\n" + "???");
            holder.btnUse.setClickable(false);

        } else if (couponList.get(position).getUsingFlag().equals("0") && systemTime > date.getTime()+86400000)
        {
            holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
            holder.btnUse.setText("???\n" + "???\n" + "???");
            holder.btnUse.setClickable(false);
        }
        else if (couponList.get(position).getUsingFlag().equals("0") && systemTime < dateFuture.getTime())
        {
            holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
            holder.btnUse.setText("???\n" + "???\n");
            holder.btnUse.setClickable(false);
        }
        else
        {
            //?????????????????????????????????????????????????????????
            if (discount != null && discount.equals("1") && (couponList.get(position).getCouponType().equals("3") || couponList.get(position).getCouponType().equals("6") || couponList.get(position).getCouponType().equals("7")))
            {
                holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
                holder.btnUse.setText("???\n" + "???\n" + "???\n" + "???");
                holder.btnUse.setClickable(false);
            } else if (total != null && !total.equals("") && discount.equals("1") && Integer.parseInt(total) < Integer.parseInt(couponList.get(position).getCouponRule()))
            { //??????????????????????????????????????????????????????????????????user???
                holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
                holder.btnUse.setText("???\n" + "???\n" + "???\n" + "???");
                holder.btnUse.setClickable(false);
            } else
            {
                holder.btnUse.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        if (MyDiscountNew2Activity.class.isInstance(context))
                        {
                            // ?????????activity?????????finish?????????
                            MyDiscountNew2Activity activity = (MyDiscountNew2Activity) context;
                            Intent i;
                            if (discount != null && discount.equals("1"))
                            { //????????????????????????????????????
                                i = new Intent();
                                i.putExtra("dis", couponList.get(position).getCouponNo());
                                i.putExtra("name", couponList.get(position).getCouponName());
                                i.putExtra("coupon", new Gson().toJson(couponList.get(position))); //????????????????????????
                                activity.setResult(RESULT_OK, i);
                            } else
                            { //???????????????????????????
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
    public int getItemCount()
    {
        return couponList.size();
    }
}

//public class CouponRecyclerAdapter extends RecyclerView.Adapter<CouponRecyclerAdapter.ViewHolder>
//{
//    private ArrayList<CouponListBean> couponList;
//    private Context context;
//    private String string;
//
//    public CouponRecyclerAdapter(final ArrayList<CouponListBean> couponList, final Context context, final String string)
//    {
//        this.couponList = couponList;
//        this.context = context;
//        this.string = string;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder
//    {
//        TextView txtTitle, txtContent, txtStartDate, txtEndDate, txtRule;
//        ImageView imgCoupon;
//        Button btnUse;
//
//        public ViewHolder(@NonNull @NotNull View itemView)
//        {
//            super(itemView);
//            txtTitle = itemView.findViewById(R.id.tv_title);
//            txtContent = itemView.findViewById(R.id.tv_content);
//            txtRule = itemView.findViewById(R.id.tv_rule);
//            txtStartDate = itemView.findViewById(R.id.tv_star_date);
//            txtEndDate = itemView.findViewById(R.id.tv_end_date);
//            imgCoupon = itemView.findViewById(R.id.img_coupon);
//            btnUse = itemView.findViewById(R.id.btn_use);
//        }
//    }
//
//    @NonNull
//    @NotNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
//    {
//        return new ViewHolder(
//                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_couponlist, parent, false)
//        );
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position)
//    {
//        holder.txtTitle.setText(couponList.get(position).getCouponName());
//        holder.txtContent.setText(couponList.get(position).getCouponDescription());
//        holder.txtStartDate.setText(couponList.get(position).getCouponStartdate());
//        holder.txtEndDate.setText(couponList.get(position).getCouponEnddate());
//
//        if (couponList.get(position).getCouponType().equals("7")||couponList.get(position).getCouponType().equals("3") || couponList.get(position).getCouponType().equals("6"))
//        {
//            holder.txtRule.setText("");
//        } else if (couponList.get(position).getCouponDiscount().equals("1"))
//        {
//
//            holder.txtRule.setText("???" + couponList.get(position).getCouponRule() + "?????????" + couponList.get(position).getDiscountAmount() + "???");
//
//        } else if (couponList.get(position).getCouponDiscount().equals("2"))
//        {
//
//            holder.txtRule.setText("???" + couponList.get(position).getCouponRule() + "?????????" + couponList.get(position).getDiscountAmount() + "%");
//        }
//        String imagerUrl = ApiConstant.API_IMAGE + couponList.get(position).getCouponPicture();
//        PicassoTrustAll.getInstance((holder.imgCoupon.getContext())).load(imagerUrl).into(holder.imgCoupon);
//        if (couponList.get(position).getUsingFlag().equals("1"))
//        {
//
//            holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
//            holder.btnUse.setText("???\n" + "???\n" + "???");
//            holder.btnUse.setClickable(false);
//
//        } else
//        {
//
//            if (string != null && string.equals("1") && (couponList.get(position).getCouponType().equals("3") || couponList.get(position).getCouponType().equals("6")))
//            {
//                holder.btnUse.setBackgroundResource(R.drawable.bg_dcdcdc);
//                holder.btnUse.setText("???\n" + "???\n" + "???\n" + "???");
//                holder.btnUse.setClickable(false);
//            } else
//            {
//                holder.btnUse.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//
//                        if (MyDiscountNewActivity.class.isInstance(context))
//                        {
//                            if (string != null && string.equals("1"))
//                            {
//                                // ?????????activity?????????finish?????????
//                                MyDiscountNewActivity activity = (MyDiscountNewActivity) context;
//                                Intent i = new Intent();
//                                i.putExtra("dis", couponList.get(position).getCouponNo());
//                                i.putExtra("name", couponList.get(position).getCouponName());
//                                activity.setResult(RESULT_OK, i);
//                                activity.finish();
//                            } else
//                            {
//                                Intent i = new Intent(context, MemberQRActivity.class);
//                                i.putExtra("ticket", new Gson().toJson(couponList.get(position)));
//                                context.startActivity(i);
//
//                            }
//                        }
//                    }
//                });
//            }
//        }
//    }
//
//    @Override
//    public int getItemCount()
//    {
//        return couponList.size();
//    }
//}