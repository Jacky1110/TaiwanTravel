package com.jotangi.nickyen.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.model.MemberBean;
import com.jotangi.nickyen.shop.ProductFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/29
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class MemberCardRecycleAdapter extends RecyclerView.Adapter<MemberCardRecycleAdapter.ViewHolder>
{
    private ArrayList<MemberBean> list;
    private Context context;

    public MemberCardRecycleAdapter(final ArrayList<MemberBean> list, final Context context)
    {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        View v;
        RoundedImageView imgMember;
        TextView txtStoreName;

        public ViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            v = itemView;
            txtStoreName = v.findViewById(R.id.tv_store_name);
            imgMember = v.findViewById(R.id.img_content);
        }

    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        return new MemberCardRecycleAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_card, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position)
    {
        holder.txtStoreName.setText(list.get(position).getStoreName());
        String imagerUrl = ApiConstant.API_IMAGE + list.get(position).getStorePicture();
        PicassoTrustAll.getInstance((holder.imgMember.getContext())).load(imagerUrl).into(holder.imgMember);
        holder.v.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ProductFragment productFragment = ProductFragment.newInstance2(list.get(position),"");
                ((MainActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.memberCardLayout, productFragment, null).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
