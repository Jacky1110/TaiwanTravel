package com.jotangi.nickyen.argame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.nickyen.argame.model.StoreBean;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConstant;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<StoreBean> storeBean;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public StoreAdapter(ArrayList<StoreBean> storeBean)
    {
        this.storeBean = storeBean;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView picture;
        TextView title;
        TextView address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            picture = itemView.findViewById(R.id.ar_shop_picture);
            title = itemView.findViewById(R.id.tv_ar_name);
            address = itemView.findViewById(R.id.tv_ar_address);

        }
    }

    @NonNull
    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_ar_shop, parent, false);
        StoreAdapter.ViewHolder ViewHolder = new StoreAdapter.ViewHolder(view);

        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.ViewHolder hold, int position)
    {
        hold.title.setText(storeBean.get(position).getAr_name());
        hold.address.setText("地址: " + storeBean.get(position).getAr_address());
//            解決Picasso能下載https的圖片問題
        PicassoTrustAll.getInstance(mContext).load(ApiConstant.API_IMAGE + storeBean.get(position).getAr_picture()).into(hold.picture);

        hold.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mListener != null) {
                    if (position != RecyclerView.NO_POSITION){
                        mListener.onItemClick(position);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {

        return storeBean.size();
    }
}

