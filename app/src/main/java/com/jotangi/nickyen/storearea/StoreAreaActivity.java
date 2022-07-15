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
        areaList.add(new AreaBean("3","角板山形象商圈","角板山形象商圈以中正路、忠孝路與中山路為主軸，特產為香菇、水蜜桃、桂竹筍、甜杮、水梨、野生靈芝、茶葉等，於民國八十三年十月獲經濟部商業司選定成為全台灣第一座形象商圈示範點，屬於觀光型態的示範性商圈。由於本地盛產品質優良的農特產品，特別是香菇，數十年前就成為重要的香菇批發集散地，各式品種的香菇齊聚街道兩側，成為名副其實的香菇大道。另一項重要的農特產－拉拉山水蜜桃，每年約5～8月為盛產期，近年來朝品種多樣化發展，高海拔的拉拉山水蜜桃因其果肉柔嫩，入口即化、甜蜜多汁的口感獨占鰲頭，在產季時吸引不少老饕準時上山品嚐。\n" + "周邊景點包含角板山行館、新溪口吊橋、戰備隧道、樟腦收納所、復興公園等，每逢花開季節或假日時吸引無數遊客前往。遊客可遊歷角板山行館和公園之後，品嘗酥炸溪蝦、馬告香腸、竹筒飯、烤山豬肉…等道地的原民佳餚，或快炒段木耳、白斬土雞、薑絲炒大腸等精緻的客家菜。離開時也別忘了把山間好味帶著走，如季節限定的水蜜桃、綠竹筍、桂竹筍、甜杮，以及香菇、金針、金線蓮、小米酒等農特產，都是本地的伴手好禮。來一趟角板山，就能輕鬆優閒享受放鬆抒壓的美食美景之旅。"));
        areaList.add(new AreaBean("4","關西魅力商圈","關西鎮舊名為「鹹菜甕」，直到日據時代，因客家人擅長製作「鹹菜」，與日語「關西」諧音，乃改名為關西。關西鎮內無污染之重型工業，復因早期交通不便，開發較晚，卻因此得天獨厚保留傳統農村樣貌；加上水質佳，居民生活仍需進行勞動，又因健康之生活型態及居住條件，鎮內居民多高壽者，爰有「長壽之鄉」美譽。\n" + "關西為以農業為主要生活型態的客家小鎮，氣候宜人、風光明媚、民風淳樸，觀光資源豐富。較有名氣的農產品有被稱為「關西三寶」的鹹菜、仙草、蕃茄及桔醬。\n" + "關西魅力商圈範圍以大同路、中山東路、正義路、中正路為主要商業街道，並以東安古橋為中心，方圓2公里內均屬商圈範圍。自日據時期以來，即設有茶廠、麵廠，屬於比較沒有過度商業開發的城市，規模是步行兩個小時可以走完的老城鎮，而且小鎮保存很多美麗與完整的歷史空間及古蹟，近年來則強化鎮內主要農產品之行銷，包括：仙草工廠、柿餅工廠、果園、農場都吸引外地遊客參訪。除此之外，鎮內有許多具有悠久歷史的建築物，例如歐式建築的關西天主堂、近200年歷史的太和宮、洋式風格的分駐所及所長宿舍、頗負建築之美的樹德醫院、紅茶公司，以及古樸的東安橋等，幽靜佇立關西街頭，各領風騷。"));
        areaList.add(new AreaBean("5","寶山特色商圈","寶山特色商圈以寶山糖廠為中心，山多丘陵起伏，早期是甘蔗的生產地，以學名紅糖(俗稱黑糖)聞名，寶山糖廠創立時間昭和3年(西元1928年)至今九十餘年，山上種蔗為多地形特殊，環山蜿蜒古路，湖光山色、埤塘美景，走到高處挑高遠望甚至可以眺望香山海邊美景美不勝收，猶如大型博物館，自實施OTOP後開發出糖的精美禮品並以客家美食聞名，商圈內經營型態、商品內容，都薈萃了本地的歷史變遷與人文特質，在在都蘊含深度的人文底蘊。\n" + "寶山素有長壽村及雙胞胎遺傳較多之鄉，歷史人文景觀及自然環境特色，包含柑橘、茶花與黑糖等特色產業、客家文化與景觀步道資源，以休閒農業、客家文化、景觀步道與周邊遊憩資源串聯各觀光元素，打造竹科後花園漫遊農村體驗模式，開創慢活的新生態環境成為都市居民假日休閒的度假勝地。"));
        areaList.add(new AreaBean("6","尖石五峰商圈","尖石鄉、五峰鄉為新竹縣的二個原民鄉，佔地面積為最遼闊的鄉鎮，面積廣西與橫山鄉為界，東至宜蘭縣鴛鴦湖保護區，北和桃園縣復興鄉為鄰，最南以中央山脈的品田山和臺中縣為界，境內又因為山脈聳立而分成前山、後山兩大區域，前山是頭前溪的源頭，後山則是大漢溪水系的發源地，二鄉境內全為山岳地帶，氣候平均溫涼，地廣人稀，轄區幅員遼闊，地形崎嶇陡峭，擁有豐富的天然"));
        areaList.add(new AreaBean("7","北埔商圈","北埔是清代道光年間廣東省客家移民開墾的基地，原名「竹北一堡南興庄」，同時亦為清代北台灣最大的隘墾區域，短短200公尺的老街上，就有就擁有金廣福公館、姜家天水堂到三級古蹟慈天宮等七座古蹟，是全台灣最密集的古蹟區。由於該地出於清朝係以武力拓墾，因此建築風格具有防禦特色。深具防衛功能的蜿蜒老街、精巧的古樸客家建築，再加上濃厚的客家風味，使此地風格更具獨樹一格。"));
        areaList.add(new AreaBean("8","桂花巷商圈","南庄老街又稱桂花巷，桂花幾乎就是南庄的代表性植物，所製作的桂花釀則是南庄最具代表性的特產；來到南庄的街上，有不少店家都會販賣桂花釀，香甜的滋味，和果糖、蜂蜜類似的吃法，但更有獨特的香氣與天然的內涵。\n" + "老街尾端的「洗衫坑」，也稱為「水汴頭」，這是當地居民利用灌溉水圳上鋪設了十餘塊石板，可以在此一邊洗滌衣物、蔬果，一邊聊天，互換生活訊息。"));

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
                riv.setImageResource(R.drawable.bg_jiaobanshan);
            }
            if (areaBean.getAreaId().equals("4")){
                riv.setImageResource(R.drawable.bg_kansai);
            }
            if (areaBean.getAreaId().equals("5")){
                riv.setImageResource(R.drawable.bg_baoshan);
            }
            if (areaBean.getAreaId().equals("6")){

                riv.setImageResource(R.drawable.bg_jianshi);
            }
            if (areaBean.getAreaId().equals("7")){
                riv.setImageResource(R.drawable.bg_baipu);
            }
            if (areaBean.getAreaId().equals("8")){
                riv.setImageResource(R.drawable.bg_guihua);
            }
        }
    }
}