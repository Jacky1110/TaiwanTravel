package com.jotangi.nickyen.industry;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.industry.model.IndustryOrderBean;
import com.jotangi.nickyen.merch.industryReserve.IndustryReserveFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class IndustryRecordActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private ImageButton btnBack;
    private TextView txtNoData;
    private ProgressBar progressBar;

    private HorizontalScrollView layoutTab;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private String getLab; //項目按鈕標籤

    private ArrayList<IndustryOrderBean> orderBeanList; // 全部的資料
    private ArrayList<IndustryOrderBean> filterList1 = new ArrayList<>(); // 已確認 (狀態為已回覆尚未完成type為1,2)
    private ArrayList<IndustryOrderBean> filterList2 = new ArrayList<>(); // 待確認 (狀態為尚未確認，type為0 預約尚未確認)
    private ArrayList<IndustryOrderBean> filterList3 = new ArrayList<>(); // 歷史紀錄 (type為3 已取消，已過期)

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

        layoutTab = findViewById(R.id.layout_tab);
        radioGroup = findViewById(R.id.radio_group);
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
        ApiConnection.getClassBookingList(new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<IndustryOrderBean>>()
                {
                }.getType();
                orderBeanList = new Gson().fromJson(jsonString, type);
                Collections.sort(orderBeanList, new Comparator<IndustryOrderBean>()
                {

                    @Override
                    public int compare(IndustryOrderBean o1, IndustryOrderBean o2)
                    {
                        return o2.getReserveDate().compareTo(o1.getReserveDate());
                    }
                });
                for (int i = 0; i < orderBeanList.size(); i++)
                {
//                    if ((orderBeanList.get(i).getReserveStatus().equals("0") && AppUtility.isExpire(orderBeanList.get(i).getReserveDate() + orderBeanList.get(i).getReserveTime(), true)) || orderBeanList.get(i).getReserveStatus().equals("2")) // 原本的邏輯 歷史紀錄只有已取消跟尚未回覆的過期
                    if ((!orderBeanList.get(i).getReserveStatus().equals("2") && AppUtility.isExpire(orderBeanList.get(i).getReserveDate() + orderBeanList.get(i).getReserveTime(), true)) || orderBeanList.get(i).getReserveStatus().equals("2")) // 新邏輯 同上加入"已完成"、"已回覆"的過期
                    {
                        filterList3.add(orderBeanList.get(i));
                    } else if ((orderBeanList.get(i).getReserveStatus().equals("1") || orderBeanList.get(i).getReserveStatus().equals("3")))
                    {
                        filterList1.add(orderBeanList.get(i));
                    } else if (orderBeanList.get(i).getReserveStatus().equals("0"))
                    {
                        filterList2.add(orderBeanList.get(i));
                    }
                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.GONE);
                        layoutViews();
                        layoutTab.setVisibility(View.VISIBLE);
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

    private void layoutViews()
    {
        layoutRadioButton();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new RecordAdapter(R.layout.item_salons_record, orderBeanList);
//        recyclerView.setAdapter(adapter);
    }

    private void layoutRadioButton()
    {
        String[] tabArray = {"已確認", "待確認", "歷史紀錄"};

        for (int i = 0; i < tabArray.length; i++)
        {
            radioButton = new RadioButton(this);
            int width = (int) (getResources().getDimension(R.dimen.dp_80));
            int height = (int) (getResources().getDimension(R.dimen.dp_40));
//            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
            radioButton.setWidth(width);
            radioButton.setHeight(height);
            int dimension = (int) (getResources().getDimension(R.dimen.dp_10));
            radioButton.setPadding(dimension, dimension, dimension, dimension);
//            radioButton.setPadding(AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10),AppUtility.dp 2px(getActivity(),10));
            radioButton.setBackgroundResource(R.drawable.radio_selector);
            radioButton.setText(tabArray[i]);
            radioButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            @SuppressLint("ResourceType")
            XmlResourceParser xrp = getResources().getXml(R.drawable.text_color);
            try
            {
                ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
                radioButton.setTextColor(csl);
            } catch (Exception e)
            {
            }
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setId(i);
            if (i == 0)
            {
                radioButton.setChecked(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                if (orderBeanList == null)
                {
                    recyclerView.setAdapter(null);
                } else
                {
                    adapter = new RecordAdapter(R.layout.item_salons_record, filterList1);
                    recyclerView.setAdapter(adapter);
                }
            }
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, AppUtility.dp2px(this, 10), 0);
            radioGroup.addView(radioButton, layoutParams);
            getLab = "已確認";
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    radioButton = findViewById(group.getCheckedRadioButtonId());
                    getLab = radioButton.getText().toString();
                    int getGroupId = group.indexOfChild(radioButton); //找出button位置

                    if (getLab.equals("已確認") && filterList1.size() > 0)
                    {
                        adapter = new RecordAdapter(R.layout.item_salons_record, filterList1);
                        recyclerView.setAdapter(adapter);

                    } else if (getLab.equals("待確認") && filterList2.size() > 0)
                    {
                        adapter = new RecordAdapter(R.layout.item_salons_record, filterList2);
                        recyclerView.setAdapter(adapter);
                    } else if (getLab.equals("歷史紀錄") && filterList3.size() > 0)
                    {
                        adapter = new RecordAdapter(R.layout.item_salons_record, filterList3);
                        recyclerView.setAdapter(adapter);
                    } else
                    {
                        recyclerView.setAdapter(null);
                    }
                }
            });
        }
    }

    private class RecordAdapter extends BaseQuickAdapter<IndustryOrderBean, BaseViewHolder>
    {
        private RoundedImageView img;
        private TextView txtPorgramName, txtStatus, txtDate;
        private ImageView imgArrow;

        public RecordAdapter(int layoutResId, ArrayList<IndustryOrderBean> orderBeanList)
        {
            super(layoutResId, orderBeanList);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, IndustryOrderBean orderBean)
        {
            img = baseViewHolder.getView(R.id.riv);
            txtPorgramName = baseViewHolder.getView(R.id.tv_name);
            txtStatus = baseViewHolder.getView(R.id.tv_descript);
            txtDate = baseViewHolder.getView(R.id.tv_date);
            imgArrow = baseViewHolder.getView(R.id.img_arrow);

            txtPorgramName.setText(orderBean.getProgramName());
            String s = AppUtility.reserveStatus(orderBean.getReserveStatus());

            if (AppUtility.isExpire(orderBean.getReserveDate() + orderBean.getReserveTime(), true) && orderBean.getReserveStatus().equals("0"))
            {
                txtStatus.setText("已過期");
            } else
            {
                txtStatus.setText(s);
            }
            txtDate.setText(orderBean.getReserveDate() + " " + AppUtility.getWeek(orderBean.getReserveDate() + " ") + orderBean.getReserveTime());
            Picasso.with(getContext()).load(ApiConstant.API_IMAGE + orderBean.getStorePicture()).into(img);

            imgArrow.setOnClickListener(v ->
            {
                IndustryOrderCheck2Fragment industryOrderCheck2Fragment = IndustryOrderCheck2Fragment.newInstance(orderBean);
                getSupportFragmentManager().beginTransaction().replace(R.id.salonsRecordLayout, industryOrderCheck2Fragment, null).addToBackStack(null).commit();
            });
        }
    }
}