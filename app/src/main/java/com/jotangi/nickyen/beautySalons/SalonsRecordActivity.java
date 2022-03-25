package com.jotangi.nickyen.beautySalons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.beautySalons.model.OrderBean;
import com.jotangi.nickyen.home.SalonsCheckOutActivity;
import com.jotangi.nickyen.questionnaire.QuestionnaireActivity;
import com.jotangi.nickyen.shop.ProductFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SalonsRecordActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private ImageButton btnBack;
    private TextView txtNoData;
    private ProgressBar progressBar;

    private ArrayList<OrderBean> orderBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salons_record);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        recyclerView = findViewById(R.id.recycleView);
        btnBack = findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        txtNoData = findViewById(R.id.tv_noData);
        progressBar = findViewById(R.id.progressBar);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadData();
    }

    private void loadData()
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.getBookingList(new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<OrderBean>>()
                {
                }.getType();
                orderBeanList = new Gson().fromJson(jsonString, type);
                Collections.sort(orderBeanList, new Comparator<OrderBean>()
                {

                    @Override
                    public int compare(OrderBean o1, OrderBean o2)
                    {
                        return o2.getReserveDate().compareTo(o1.getReserveDate());
                    }
                });
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.GONE);
                        layoutViews(orderBeanList);
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(null);
                    }
                });
            }
        });
    }

    private void layoutViews(ArrayList<OrderBean> orderBeanList)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecordAdapter(orderBeanList);
        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(new OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position)
//            {
//                OrderCheckFragment2 orderCheckFragment2 = OrderCheckFragment2.newInstance(orderBeanList.get(position));
//                getSupportFragmentManager().beginTransaction().replace(R.id.salonsRecordLayout, orderCheckFragment2, null).addToBackStack(null).commit();
//            }
//        });
    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>
    {

        private ArrayList<OrderBean> dataList;
        private String eat = "";

        public RecordAdapter(final ArrayList<OrderBean> dataList)
        {
            this.dataList = dataList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            RoundedImageView img;
            ImageView imgArrow;
            TextView txtStoreName, txtStatus, txtDate, txtFillOut;

            public ViewHolder(@NonNull @NotNull View itemView)
            {
                super(itemView);
                img = itemView.findViewById(R.id.riv);
                imgArrow = itemView.findViewById(R.id.img_arrow);
                txtStoreName = itemView.findViewById(R.id.tv_name);
                txtStatus = itemView.findViewById(R.id.tv_descript);
                txtDate = itemView.findViewById(R.id.tv_date);
                txtFillOut = itemView.findViewById(R.id.tv_fill_out);
            }
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
        {
            return new RecordAdapter.ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salons_record, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull SalonsRecordActivity.RecordAdapter.ViewHolder holder, int position)
        {
            holder.imgArrow.setOnClickListener(v ->
            {
                OrderCheckFragment2 orderCheckFragment2 = OrderCheckFragment2.newInstance(dataList.get(position));
                getSupportFragmentManager().beginTransaction().replace(R.id.salonsRecordLayout, orderCheckFragment2, null).addToBackStack(null).commit();
            });
            holder.txtStoreName.setText(dataList.get(position).getStoreName());
            String s = AppUtility.reserveStatus(dataList.get(position).getReserveStatus());
            if (dataList.get(position).getReserveStatus().equals("0") && AppUtility.isExpire(dataList.get(position).getReserveDate() + dataList.get(position).getReserveTime(),true))
            {
                holder.txtFillOut.setVisibility(View.GONE);
                s = "已過期";
                holder.txtStatus.setText(s);
            }
            else if (dataList.get(position).getReserveStatus().equals("1") ||dataList.get(position).getReserveStatus().equals("3"))
            {
                holder.txtFillOut.setVisibility(View.VISIBLE);
                holder.txtFillOut.setOnClickListener(v ->
                {
                    Intent intent = new Intent(SalonsRecordActivity.this, QuestionnaireActivity.class);
                    if (eat != null && eat.equals("1"))
                    {
                        intent.putExtra("sid", "2"); // 因為結帳說要互填問券，美容美髮填餐飲，餐飲反之，但目前只剩餐飲保留結帳，美容美髮不結帳所以就填自己的
                        intent.putExtra("storeName", orderBeanList.get(position).getStoreName());
                    } else
                    {
                        intent.putExtra("sid", "2"); //value 1.餐飲問題2.美容美髮問題
                        intent.putExtra("storeName", orderBeanList.get(position).getStoreName());
                    }
                    startActivity(intent);
                });
                holder.txtStatus.setText(s);
            }
            else
            {
                holder.txtFillOut.setVisibility(View.GONE);
                holder.txtStatus.setText(s);
            }
            holder.txtDate.setText(dataList.get(position).getReserveDate() + " " + AppUtility.getWeek(dataList.get(position).getReserveDate() + " ") + dataList.get(position).getReserveTime());
            Picasso.with(SalonsRecordActivity.this).load(ApiConstant.API_IMAGE + dataList.get(position).getStorePicture()).into(holder.img);
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }
}