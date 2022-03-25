package com.jotangi.nickyen.storearea;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.base.BaseFragment;
import com.jotangi.nickyen.model.ShopBean;
import com.jotangi.nickyen.shop.ProductFragment;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StoreAreaFragment extends BaseFragment
{
    static String s;
    private String position2;

    private ImageButton btnBack;
    private ImageView imgContent, imgMap;
    private TextView txtContent, txtAddress;
    private WebView webView;
    private ViewPager shopViewPager;
    private ProgressBar progressBar;

    private List<ShopBean> shopList = new ArrayList<>();
    private ShopViewPagerAdapter shopViewPagerAdapter;

    public StoreAreaFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StoreAreaFragment newInstance(String param1)
    {
        StoreAreaFragment fragment = new StoreAreaFragment();
        Bundle args = new Bundle();
        s = param1;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            position2 = "";
            switch (s)
            {
                case "魅力金三角商圈":
                    position2 = "4";
                    break;
                case "北埔商圈":
                    position2 = "3";
                    break;
                case "桂花巷商圈":
                    position2 = "1";
                    break;
                case "楊梅四維商圈":
                case "尖石五峰商圈":
                    position2 = "";
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_store_aera, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);

        imgContent = getView().findViewById(R.id.iv_content);
//        imgMap = getView().findViewById(R.id.iv_map);
//        imgMap.setOnClickListener(this);
        txtContent = getView().findViewById(R.id.tv_content);
        txtAddress = getView().findViewById(R.id.tv_info_store_address);
        txtAddress.setOnClickListener(this);

        progressBar = getView().findViewById(R.id.progressBar);

//        webView = getView().findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setSupportZoom(true);
//        //允許android調用javascript
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setAllowFileAccess(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//        {
//            webView.getSettings().setAllowFileAccessFromFileURLs(true);
//            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        }
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webView.setWebViewClient(new WebViewClient()
//        {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(final WebView view, String url)
//            {
//                if (url.startsWith("http") || url.startsWith("https"))
//                { //http和https協議開頭的執行正常的流程
//                    return false;
//                } else
//                {  //其他的URL則會開啟一個Acitity然後去呼叫原生APP
//                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    if (in.resolveActivity(getActivity().getPackageManager()) == null)
//                    {
//                        //說明系統中不存在這個activity
//                        view.post(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                AppUtility.showMyDialog(getActivity(), "你即將前往外部連結", "確認", "取消", new AppUtility.OnBtnClickListener()
//                                {
//                                    @Override
//                                    public void onCheck()
//                                    {
//                                        Uri uri2;
//                                        uri2 = Uri.parse("https://www.google.com/search?q=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4&oq=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4#trex=m_r:1,m_t:gwp,rc_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4,rc_ui:3,ru_gwp:0%252C6,ru_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4");
//                                        startActivity(new Intent(Intent.ACTION_VIEW, uri2));
//                                    }
//
//                                    @Override
//                                    public void onCancel()
//                                    {
//
//                                    }
//                                });
//                            }
//                        });
//
//                    } else
//                    {
//                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                        startActivity(in);
//                        //如果想要載入成功跳轉可以 這樣
//                        view.post(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                view.loadUrl("https://www.google.com/search?q=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4&oq=%E9%99%84%E8%BF%91%E5%81%9C%E8%BB%8A%E5%A0%B4#trex=m_r:1,m_t:gwp,rc_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4,rc_ui:3,ru_gwp:0%252C6,ru_q:%25E9%2599%2584%25E8%25BF%2591%25E5%2581%259C%25E8%25BB%258A%25E5%25A0%25B4");
//                            }
//                        });
//                    }
//                    return true;
//                }
//            }
//        });

        shopViewPager = getView().findViewById(R.id.shop_viewPager);
        shopViewPager.setClipToPadding(false);
        shopViewPager.setClipChildren(false);
        shopViewPager.setOffscreenPageLimit(3);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        getShopList();
    }

    private void getShopList()
    {
        if (position2 != null)
        {
            progressBar.setVisibility(View.VISIBLE);
            ApiConnection.getShopList("", "", position2, "", new ApiConnection.OnConnectResultListener()
            {
                @Override
                public void onSuccess(String jsonString)
                {
                    Type type = new TypeToken<ArrayList<ShopBean>>()
                    {
                    }.getType();
                    shopList = new Gson().fromJson(jsonString, type);
                    if (getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            progressBar.setVisibility(View.GONE);
                            layoutView();
                        }
                    });
                }

                @Override
                public void onFailure(String message)
                {

                }
            });
        }
    }

    private void layoutView()
    {
        if (position2.equals("1")) //API的桂花巷商圈
        {
            imgContent.setImageResource(R.drawable.bg_guihua);
            txtContent.setText("南庄老街又稱桂花巷，桂花幾乎就是南庄的代表性植物，所製作的桂花釀則是南庄最具代表性的特產；來到南庄的街上，有不少店家都會販賣桂花釀，香甜的滋味，和果糖、蜂蜜類似的吃法，但更有獨特的香氣與天然的內涵。\n" +
                    "老街尾端的「洗衫坑」，也稱為「水汴頭」，這是當地居民利用灌溉水圳上鋪設了十餘塊石板，可以在此一邊洗滌衣物、蔬果，一邊聊天，互換生活訊息。老街的巷子很窄，長度也不長，但是小吃、美食都很精緻，像似客家大餅、肉餅、地瓜餅、芋頭餅、手工蛋捲、原住民的石板烤放山豬肉與山豬肉香腸、烤班甲、竹筒飯等，都普受歡迎。每到假日常見遊客摩肩接踵，場景熱鬧非凡！");
//            imgMap.setImageResource(R.drawable.bg_map_guihua);
            txtAddress.setText("苗栗縣南庄鄉中正路116號");
        }
        if (s.equals("楊梅四維商圈"))
        {
            imgContent.setImageResource(R.drawable.bg_yangmei);
            txtContent.setText("楊梅四維商圈主要範圍以四維路為主，鄰近埔心火車站，除了交通便捷外，商圈內充滿著各式各樣的特色店家，擁有著多元的美食文化，包辦了民眾五臟廟所需，商圈內還有健康醫療、美容美體、生活百貨等。搭配著寬敞、整潔的空間成了商圈主要的特色，街道上景觀樹木美化環境，提供了民眾寬敞的遊逛行走的空間。 \n" +
                    "楊梅區人口約17萬人，埔心地區近8萬人，約占楊梅人口數一半，是楊梅的重心之一。市府正逐步完成埔心地區的建設，除了四維親子館的設置，將於金門新村興建楊梅第一個社會住宅；同時埔心茶葉故事館，也可讓市民認識茶葉歷史文化。在軟體方面，市府期盼能持續增加埔心當地的體育、藝文與親子活動等，讓埔心活動能夠加乘。四維商圈硬體條件優異、潛力無窮，擁有不少特色店家，營造出整體商圈魅力，讓四維商圈成為埔心的亮點。");
            shopViewPager.setVisibility(View.INVISIBLE);
//            imgMap.setImageResource(R.drawable.bg_map_yangmei);
            txtAddress.setText("桃園市楊梅區四維路");
        }
        if (position2.equals("3")) //北埔商圈
        {
            imgContent.setImageResource(R.drawable.bg_baipu);
            txtContent.setText("北埔是清代道光年間廣東省客家移民開墾的基地，原名「竹北一堡南興庄」，同時亦為清代北台灣最大的隘墾區域，短短200公尺的老街上，就有就擁有金廣福公館、姜家天水堂到三級古蹟慈天宮等七座古蹟，是全台灣最密集的古蹟區。由於該地出於清朝係以武力拓墾，因此建築風格具有防禦特色。深具防衛功能的蜿蜒老街、精巧的古樸客家建築，再加上濃厚的客家風味，使此地風格更具獨樹一格。\n" +
                    "北埔老街商圈主要街區為中正路、北埔街、南興街，以及廟前路為主，但商圈範圍含蓋北埔鄉全區，屬於觀光型商圈。\n" +
                    "北埔的歷史背景孕育出濃厚的客家人文風情，不論擂茶文化，還是舉世聞名的東方美人茶、獨步全台的石柿餅都絕對不能錯過，還有全台唯二的冷泉也在北埔，商圈的魅力特色可在「古蹟歷史文化」、「客家傳統美食小吃」、「農特產加工伴手禮」等三部分充分顯現。\n" +
                    "北埔老街商圈的魅力不只僅於老街上的客家美食小吃、特色產品等，更展現於歷史古蹟與老街巷弄間的懷舊與時空的對話，透過這些歷史古蹟穿越時空，體驗當代的風俗習慣與歷史背景下的文化，更能深刻感受到這個客家小鎮當年的繁華以及前人的風光，小鎮巷弄慢漫遊就是最好的體驗模式，古蹟建築與老街巷弄間的每一磚瓦都藏著深厚的寓意，挖掘更多巷弄古蹟或故事，可以深刻打動遊客加深體驗，小鎮古風不變，卻加入更多元的業態發展與數位科技的便利，充分展現商圈永續的魅力。");
//            imgMap.setImageResource(R.drawable.bg_map_beipu);
            txtAddress.setText("新竹縣北埔鄉南興街");
        }
        if (position2.equals("4")) //金三角商圈
        {
            imgContent.setImageResource(R.drawable.bg_triangle1);
            txtContent.setText("桃園魅力金三角商圈，因位於桃園市中壢、平鎮、八德三區接壤的龍岡忠貞地區，核心點為桃園龍岡忠貞，包含中壢龍東路、龍南路與平鎮中山路、八德龍江路街道範圍，其中又以平鎮中山路為特色產業主要街區，涵蓋以忠貞市場等屬於行政區域上中壢、平鎮、八德三大區級市的交會點為核心，向外放射延伸至三鄉鎮區域，皆屬商圈範圍。\n" +
                    "商圈人文歷史豐富，主要肇始於40年代初期，流落在滇緬邊境的國民黨異域孤軍部隊，首批撤退來台時，被安置於此區域內的忠貞新村，引來大量雲南族裔為主的滇緬泰籍移民聚集，且包含少數民族以及伊斯蘭教徒；附近工業區引進大量外籍移工、加上因婚嫁來台的新住民，如越、菲、泰、印尼等新住民與移工等，使得此地形成一股以滇緬為主的生活與飲食文化特色之特殊跨族裔飲食產業特色。\n" +
                    "104年成立「桃園市魅力金三角地方特色產業展協會」，推展共同品牌，擦亮地區商圈特色，以異域孤軍故事發展出的五大文化力，包含孤軍故事、異國餐飲、多元種族服飾、舞蹈及傳統樂器等五大獨特的亮點DNA，來發展具有深度體驗的文化觀光型商圈，打造桃園獨有、台灣唯一的特色觀光型商圈。");
//            imgMap.setImageResource(R.drawable.bg_map_meili);
            txtAddress.setText("桃園市中壢區龍東路160號");
        }
        if (s.equals("尖石五峰商圈"))
        {
            imgContent.setImageResource(R.drawable.bg_jianshi);
            txtContent.setText("尖石鄉、五峰鄉為新竹縣的二個原民鄉，佔地面積為最遼闊的鄉鎮，面積廣西與橫山鄉為界，東至宜蘭縣鴛鴦湖保護區，北和桃園縣復興鄉為鄰，最南以中央山脈的品田山和臺中縣為界，境內又因為山脈聳立而分成前山、後山兩大區域，前山是頭前溪的源頭，後山則是大漢溪水系的發源地，二鄉境內全為山岳地帶，氣候平均溫涼，地廣人稀，轄區幅員遼闊，地形崎嶇陡峭，擁有豐富的天然景觀資源，大部份以農林為業，以果樹為主，佔40％，其次是特殊作物，佔10％栽種香菇為農林主要副業。普通作物以甘藷、實用玉蜀黍為主；蔬菜以蘿 蔔、蔥、甘藍、大芥菜、蕃茄、甜椒、苦瓜為主；水果方面，則有李、桃、柿、梨，堪稱新竹縣的綠色命脈。其自然資源如：鎮西堡神木群、霞喀羅古道、鴛鴦谷瀑布、錦屏大穚、馬里光瀑布群、軍艦岩、青蛙石、李棟山古堡等，且鄉內產業有甜柿、竹筍等及特有之溫泉及特色民宿等，其豐富的泉水帶動觀光的資源，為尖石鄉最大的發展特色。");
            shopViewPager.setVisibility(View.INVISIBLE);
//            imgMap.setImageResource(R.drawable.bg_map_wufeng);
            txtAddress.setText("新竹縣尖石鄉錦屏村");
        }
        if (shopList != null)
        {
            shopViewPagerAdapter = new ShopViewPagerAdapter(getContext(), shopList);
            shopViewPager.setAdapter(shopViewPagerAdapter);
        } else
        {
            shopViewPager.setAdapter(null);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
//            case R.id.iv_map:
            case R.id.tv_info_store_address:
                if (position2.equals("1")) //API的桂花巷商圈
                {
                    Uri uri = Uri.parse("https://goo.gl/maps/7RL1TaRjsQHfJtNW9");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
//                    uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=桃園市桃園區復興路98號七樓");
                }
                if (s.equals("楊梅四維商圈"))
                {
                    Uri uri = Uri.parse("https://goo.gl/maps/RnaY9v1FfhN1q3TN9");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                if (position2.equals("3")) //北埔商圈
                {
                    Uri uri = Uri.parse("https://goo.gl/maps/esx49PRTDXrK8Ujv5");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                if (position2.equals("4")) //金三角三圈
                {
                    Uri uri = Uri.parse("https://goo.gl/maps/RdCGcYBTaAoMNNu87");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));

                }
                if (s.equals("尖石五峰商圈"))
                {
                    Uri uri = Uri.parse("https://goo.gl/maps/fmC4LtFDP1jMPNYn7");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                break;
            case R.id.ib_go_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public class ShopViewPagerAdapter extends PagerAdapter
    {
        private Context context;
        private List<ShopBean> shopList;

        public ShopViewPagerAdapter(final Context context, final List<ShopBean> shopList)
        {
            this.context = context;
            this.shopList = shopList;
        }

        @Override
        public int getCount()
        {
            return 10;
        }

        // 來判斷顯示的是否是同一張圖片，這裡我们將兩個參數相比較返回即可
        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object)
        {
            return view.equals(object);
        }

        // 當要顯示的圖片可以進行緩存的時候，會調用這個方法進行顯示圖片的初始化，我们將要顯示的ImageView加入到ViewGroup中，然後作為返回值返回即可
        @NonNull
        @NotNull
        @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.slide_home_item, container, false);

            RoundedImageView imageView = view.findViewById(R.id.imageSlide);
            TextView txtTitle = view.findViewById(R.id.tv_title);
            TextView txtContent = view.findViewById(R.id.tv_content);
            TextView txtDistance = view.findViewById(R.id.tv_distance);

            PicassoTrustAll.getInstance(getContext()).load(ApiConstant.API_IMAGE + shopList.get(position).getStorePicture()).into(imageView);
            txtTitle.setText(shopList.get(position).getStoreName());
            txtContent.setText(shopList.get(position).getStoreNews());

            int AAA = Integer.parseInt(shopList.get(position).getDistance());

            if (AAA < 1000)
            {
                txtDistance.setText(shopList.get(position).getDistance() + "m");

            } else
            {
                double BBB = AAA / 100;
                BigDecimal bigDecimal = new BigDecimal(BBB);
                double f1 = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_DOWN).doubleValue() / 10;
                txtDistance.setText(f1 + "Km");
            }

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ProductFragment productFragment = ProductFragment.newInstance(shopList.get(position), "");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.storeAreaLayout2, productFragment, null).addToBackStack(null).commit();
                }
            });
//        container.addView(view,position);//要填0否則會報錯
            container.addView(view, 0);
            return view;
//        return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object)
        {
//        super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}