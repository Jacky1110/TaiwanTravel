package com.jotangi.nickyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.nickyen.api.ApiConstant;

import java.util.ArrayList;
import java.util.List;

public class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.ViewHolder>{

    private String TAG = MemberAdapter.class.getSimpleName() + "(TAG)";
    private List<UsageModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTitle, txtReceive, txtReceiveUse;
        ImageView imgCoupon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tv_title);
            txtReceive = itemView.findViewById(R.id.tv_receive);
            txtReceiveUse = itemView.findViewById(R.id.tv_receiveUse);
            imgCoupon = itemView.findViewById(R.id.img_coupon);
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(mData.get(position).title);
        holder.txtReceive.setText("已領取人數:" + mData.get(position).receive);
        holder.txtReceiveUse.setText("已兌換人數:" + mData.get(position).receiveUse);

        String imagerUrl = ApiConstant.API_IMAGE + mData.get(position).imgCoupon;
        PicassoTrustAll.getInstance((holder.imgCoupon.getContext())).load(imagerUrl).into(holder.imgCoupon);
    }

    public void setmData(List<UsageModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class UsageModel {
    String title = "";
    String receive = "";
    String receiveUse = "";
    String imgCoupon = "";
}
