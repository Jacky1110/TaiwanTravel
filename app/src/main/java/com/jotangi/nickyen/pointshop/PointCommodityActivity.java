//package com.jotangi.nickyen.PointShop;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.content.ContextCompat;
//import androidx.viewpager.widget.ViewPager;
//
//import com.jotangi.nickyen.Adapter.ViewPagerAdapter;
//import com.jotangi.nickyen.R;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import me.relex.circleindicator.CircleIndicator;
//
//public class PointCommodityActivity extends AppCompatActivity {
//
//    ViewPager viewPager;
//    CircleIndicator circleIndicator;
//    Handler handler;
//    Timer timer;
//
//    private ListView listView;
//    private String[] from = {"title","context"};
//    private int[] to = {R.id.item_title,R.id.item_comtext};
//    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
//
//    private SimpleAdapter adapter;
//
////    LinearLayout sliderDotspanel;
////    private int dotscount;
////    private ImageView[] dots;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_point_commodity);
//
//        getWindow().setStatusBarColor(ContextCompat.getColor(PointCommodityActivity.this, R.color.typeRed));
//        Toolbar mToolbar = findViewById(R.id.toolbar);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        viewPager = findViewById(R.id.viewPager);
//        circleIndicator = findViewById(R.id.circleIndicator);
//        listView = findViewById(R.id.list_commodity);
//
//        initLisView(); //初始化
////        sliderDotspanel = findViewById(R.id.slideDots);
//
//        List<Integer> imageList = new ArrayList<>();
//        imageList.add(R.drawable.test_coffee);
//        imageList.add(R.drawable.test_coffee);
//        imageList.add(R.drawable.test_coffee);
//        imageList.add(R.drawable.test_coffee);
//
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(imageList);
//        viewPager.setAdapter(viewPagerAdapter);
////        viewPager.setPageTransformer(true,new Transforms());
//
//        //點點適配調用viewpager方法
//        circleIndicator.setViewPager(viewPager);
//
////        dotscount = viewPagerAdapter.getCount();
////        dots = new ImageView[dotscount;]
//
//        handler = new Handler();
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int i = viewPager.getCurrentItem();
//
//                        if (i == imageList.size()-1) {
//
//                            i = 0;
//                            viewPager.setCurrentItem(i, true);
//
//                        } else {
//                        i++;
//                        viewPager.setCurrentItem(i, true);
//                        }
//                    }
//                });
//            }
//        },3000,3000);
//    }
//
//    private void initLisView() {
//
//        HashMap<String,String> d0 = new HashMap<>();
//        d0.put(from[0],"兌換說明");
//        d0.put(from[1],"1. 活動時間：2021/01/01~2021/12/31" + "\n" + "2. 咖啡兌換券兌換時間：2021-01-01 ~ 2022- 01-07)");
//        data.add(d0);
//
//        HashMap<String,String> d1 = new HashMap<>();
//        d1.put(from[0],"注意項目");
//        d1.put(from[1],"1.本優惠不與其他優惠活動併行。\n"
//                        + "(包含咖啡買一送一、第二杯五折、會員日咖啡折扣…等優惠折扣)\n"+
//                        "2. 需於兌換期限前完成兌換，逾期則無效作廢；且不得以任何理由要求補發商品兌換券或等值商品。\n"+
//                        "3. 結帳時請出示本兌換券，且兌換後門市人員將會回收此券，並不得重複使用；本券一經使用，一律不接受退貨。\n"+
//                        "4. 萊爾富保有修改、變更、暫停或終止本活動之權利，如有未盡事宜，悉依本公司相關規定或解釋辦理，並得隨時補充公告之。");
//        data.add(d1);
//
//
//        adapter = new  SimpleAdapter(this,data,R.layout.item_commodity,from,to);
//        listView.setAdapter(adapter);
//
//    }
//
////    //縮放過度
////    public class Transforms implements  ViewPager.PageTransformer{
////        private static  final  float MAX_ALPHA=0.5f;
////        private static  final  float MAX_SCALE=0.9f;
////
////        @Override
////        public void transformPage(@NonNull View page, float position) {
////            if (position<-1||position>1){
////                //不可見區域
////                page.setAlpha(MAX_ALPHA);
////                page.setScaleX(MAX_SCALE);
////                page.setScaleY(MAX_SCALE);
////            }else {
////                //可見區域透明效果
////                if (position<=0){
////                    //pos區域[-1,0]
////                    page.setAlpha(MAX_ALPHA+MAX_ALPHA*(1-position));
////                }else {
////                    //pos區域[0,1]
////                    page.setAlpha(MAX_ALPHA+MAX_ALPHA*(1-position));
////                }
////                //可見區域，縮放效果
////                float scale = Math.max(MAX_SCALE,1-Math.abs(position));
////                page.setScaleX(scale);
////                page.setScaleY(scale);
////            }
////        }
////    }
//}