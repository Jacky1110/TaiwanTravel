package com.jotangi.nickyen.storearea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.storearea.model.AreaBean;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StoreAreaActivity extends AppCompatActivity
{
    private ImageButton btnBack;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ProgressBar progressBar;

    private ArrayList<AreaBean> areaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_area);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        btnBack = findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycleView);

        areaList.add(new AreaBean("1","魅力金三角商圈","桃園魅力金三角商圈，因位於桃園市中壢、平鎮、八德三區接壤的龍岡忠貞地區，核心點為桃園龍岡忠貞，包含中壢龍東路、龍南路與平鎮中山路、八德龍江路街道範圍，其中又以平鎮中山路為特色產業主要街區，涵蓋以忠貞市場等屬於行政區域上中壢、平鎮、八德三大區級市的交會點為核心，向外放射延伸至三鄉鎮區域，皆屬商圈範圍。"));
        areaList.add(new AreaBean("2","楊梅四維商圈","楊梅四維商圈主要範圍以四維路為主，鄰近埔心火車站，除了交通便捷外，商圈內充滿著各式各樣的特色店家，擁有著多元的美食文化，包辦了民眾五臟廟所需，商圈內還有健康醫療、美容美體、生活百貨等。搭配著寬敞、整潔的空間成了商圈主要的特色，街道上景觀樹木美化環境，提供了民眾寬敞的遊逛行走的空間。"));
        areaList.add(new AreaBean("3","尖石五峰商圈","尖石鄉、五峰鄉為新竹縣的二個原民鄉，佔地面積為最遼闊的鄉鎮，面積廣西與橫山鄉為界，東至宜蘭縣鴛鴦湖保護區，北和桃園縣復興鄉為鄰，最南以中央山脈的品田山和臺中縣為界，境內又因為山脈聳立而分成前山、後山兩大區域，前山是頭前溪的源頭，後山則是大漢溪水系的發源地，二鄉境內全為山岳地帶，氣候平均溫涼，地廣人稀，轄區幅員遼闊，地形崎嶇陡峭，擁有豐富的天然"));
        areaList.add(new AreaBean("4","北埔商圈","北埔是清代道光年間廣東省客家移民開墾的基地，原名「竹北一堡南興庄」，同時亦為清代北台灣最大的隘墾區域，短短200公尺的老街上，就有就擁有金廣福公館、姜家天水堂到三級古蹟慈天宮等七座古蹟，是全台灣最密集的古蹟區。由於該地出於清朝係以武力拓墾，因此建築風格具有防禦特色。深具防衛功能的蜿蜒老街、精巧的古樸客家建築，再加上濃厚的客家風味，使此地風格更具獨樹一格。"));
        areaList.add(new AreaBean("5","桂花巷商圈","南庄老街又稱桂花巷，桂花幾乎就是南庄的代表性植物，所製作的桂花釀則是南庄最具代表性的特產；來到南庄的街上，有不少店家都會販賣桂花釀，香甜的滋味，和果糖、蜂蜜類似的吃法，但更有獨特的香氣與天然的內涵。\n" + "老街尾端的「洗衫坑」，也稱為「水汴頭」，這是當地居民利用灌溉水圳上鋪設了十餘塊石板，可以在此一邊洗滌衣物、蔬果，一邊聊天，互換生活訊息。"));

        layoutView();
    }

    private void layoutView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(R.layout.item_store_area, areaList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position)
            {

                StoreAreaFragment storeAreaFragment = StoreAreaFragment.newInstance(areaList.get(position).getAreaName());
                getSupportFragmentManager().beginTransaction().replace(R.id.storeAreaLayout, storeAreaFragment, null).addToBackStack(null).commit();
            }
        });
    }

    private class RecyclerAdapter extends BaseQuickAdapter<AreaBean, BaseViewHolder>
    {
        private TextView txtName, txtDescript;
        private RoundedImageView riv;

        public RecyclerAdapter(int layoutResId, @Nullable List<AreaBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, AreaBean areaBean)
        {
            txtName = baseViewHolder.getView(R.id.tv_name);
            txtName.setText(areaBean.getAreaName());
            txtDescript = baseViewHolder.getView(R.id.tv_descript);
            txtDescript.setText(areaBean.getAreaDescript());
            riv = baseViewHolder.getView(R.id.riv);
            if (areaBean.getAreaId().equals("1")){
                riv.setImageResource(R.drawable.bg_triangle1);
            }
            if (areaBean.getAreaId().equals("2")){
                riv.setImageResource(R.drawable.bg_yangmei);
            }
            if (areaBean.getAreaId().equals("3")){

                riv.setImageResource(R.drawable.bg_jianshi);
            }
            if (areaBean.getAreaId().equals("4")){
                riv.setImageResource(R.drawable.bg_baipu);
            }
            if (areaBean.getAreaId().equals("5")){
                riv.setImageResource(R.drawable.bg_guihua);
            }
        }
    }
}