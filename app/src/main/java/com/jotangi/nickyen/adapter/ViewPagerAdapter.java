package com.jotangi.nickyen.adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.model.BannerListBean;
import com.jotangi.nickyen.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    ArrayList<BannerListBean> bannerListBeanList ;

    public ViewPagerAdapter(List<BannerListBean> list) {
        bannerListBeanList = (ArrayList<BannerListBean>) list;
    }

    @Override
    public int getCount()
    {
        return bannerListBeanList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view =LayoutInflater.from(container.getContext()).inflate(R.layout.custom_layout,container,false);

        ViewHolder holder = new ViewHolder();
        holder.imageView = view.findViewById(R.id.iv_commodity);
        view.setTag(holder);

        BannerListBean bannerListBean = (BannerListBean) getItem(position);
        String imageUrl = ApiConstant.API_IMAGE + bannerListBean.getBanner_picture();
        PicassoTrustAll.getInstance(holder.imageView.getContext()).load(imageUrl).into(holder.imageView);
        container.addView(view);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(bannerListBean.getBanner_link()); //拿官網測試用
                intent.setData(uri);
                v.getContext().startActivity(intent); //getActivity()和startActivity 方法在中不可用PagerAdapter。因此v.getContext()用於準備Intent和調用startActivity方法：

            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }

    private class ViewHolder {
        ImageView imageView;
    }

    public Object getItem(int position) {
        return bannerListBeanList.get(position);
    }
}
