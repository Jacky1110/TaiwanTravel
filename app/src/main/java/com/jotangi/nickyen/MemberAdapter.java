package com.jotangi.nickyen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{

    private String TAG = MemberAdapter.class.getSimpleName() + "(TAG)";
    private List<MemberModel> mData = new ArrayList<>();
    private ItemClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtname, txtday, txtdata;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.tv_name);
            txtday = itemView.findViewById(R.id.tv_day);
            txtdata = itemView.findViewById(R.id.tv_member_data);
            itemView.setOnClickListener(this);
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
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mamber_manage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtname.setText(mData.get(position).name);
        holder.txtday.setText(mData.get(position).day);
        holder.txtdata.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("mid",mData.get(position).mid);
            Navigation.findNavController(v).navigate(R.id.action_memberManagementFragment_to_memberDataFragment, bundle);
        });
    }


    public void setmData(List<MemberModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}



class MemberModel{
    String name = "";
    String day = "";
    String mid = "";

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getMid() {
        return this.mid;
    }

    public void setMid(final String mid) {
        this.mid = mid;
    }
}