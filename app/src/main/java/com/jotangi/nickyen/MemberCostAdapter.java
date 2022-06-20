package com.jotangi.nickyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MemberCostAdapter extends RecyclerView.Adapter<MemberCostAdapter.ViewHolder>{

    private String TAG = MemberCostAdapter.class.getSimpleName() + "(TAG)";
    private List<MemberCostModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtStoreName, txtOrderDate, txtAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStoreName = itemView.findViewById(R.id.tv_store_name);
            txtOrderDate = itemView.findViewById(R.id.tv_order_date);
            txtAmount = itemView.findViewById(R.id.tv_amount);
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
                .inflate(R.layout.item_member_record,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberCostAdapter.ViewHolder holder, int position) {

        holder.txtStoreName.setText(mData.get(position).storeName);
        holder.txtOrderDate.setText(mData.get(position).orderDate);
        holder.txtAmount.setText("消費金額：NT$" +AppUtility.strAddComma(mData.get(position).amount));
    }

    public void setmData(List<MemberCostModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class MemberCostModel{
    String storeName = "";
    String orderDate = "";
    String amount = "";

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(final String amount) {
        this.amount = amount;
    }
}