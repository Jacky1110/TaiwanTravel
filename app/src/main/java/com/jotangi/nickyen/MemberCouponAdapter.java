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
    private List<MemberCouponMode> mData = new ArrayList<>();
    private ItemClickListener clickListener;
    private Context context;
    private String discount;
    private String total;

//    public MemberCouponAdapter(final List<MemberCouponMode> mData, final Context context, final String discount, final String total) {
//        this.mData = mData;
//        this.context = context;
//        this.discount = discount;
//        this.total = total;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setClickListener(ItemClickListener listener) {
        clickListener = listener;
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

        holder.txtTitle.setText(mData.get(position).title);
        holder.txtContent.setText(mData.get(position).content);
        holder.txtStartDate.setText(mData.get(position).startDate);
        holder.txtEndDate.setText(mData.get(position).endDate);

        if (mData.get(position).type.equals("7") || mData.get(position).type.equals("3") || mData.get(position).type.equals("6")) {

            holder.txtRule.setText("");

        } else if (mData.get(position).discount.equals("1")) {

            holder.txtRule.setText("滿" + mData.get(position).rule + "可折抵" + mData.get(position).amount + "元");

        } else if (mData.get(position).discount.equals("2")) {

            holder.txtRule.setText("滿" + mData.get(position).rule + "可折抵" + mData.get(position).amount + "%");
        }
        String imagerUrl = ApiConstant.API_IMAGE + mData.get(position).pic;
        PicassoTrustAll.getInstance((holder.imgCoupon.getContext())).load(imagerUrl).into(holder.imgCoupon);
    }

    public void setmData(List<MemberCouponMode> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class MemberCouponMode{
    String title = "";
    String content = "";
    String startDate = "";
    String endDate = "";
    String pic = "";
    String rule = "";
    String type = "";
    String discount = "";
    String amount = "";
    String using = "";



}
