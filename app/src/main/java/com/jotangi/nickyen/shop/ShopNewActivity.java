package com.jotangi.nickyen.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.jotangi.nickyen.NoAnimationViewPager;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.shop.adapter.VPAdapter;

public class ShopNewActivity extends AppCompatActivity implements View.OnClickListener
{

    private TabLayout tabLayout;
    private NoAnimationViewPager viewPager;
    private VPAdapter adapter;

    private ImageView btnList;
    private ImageButton btnGoBack;

    //這是地圖初始化為地圖的狀態 true為地圖，false為列表
    public static boolean status;
    //這是地圖初始化為地圖的狀態 true為地圖，false為列表
    public static int pages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_new);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        btnList = findViewById(R.id.img_list);
        btnGoBack = findViewById(R.id.ib_go_back);
        btnList.setOnClickListener(this);
        btnGoBack.setOnClickListener(this);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        adapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new AllFragment(), "全部");
        adapter.addFragment(new FoodFragment(), "餐飲美食");
        adapter.addFragment(new GiftFragment(), "伴手好禮");
        adapter.addFragment(new LivelihoodFragment(), "民生服務");
        adapter.addFragment(new SalonsFragment(), "美容美髮");
        viewPager.setAdapter(adapter);

        if (pages == 0)
        {
            viewPager.setCurrentItem(pages);
            tabLayout.getTabAt(pages).select();
        } else
        {
            viewPager.setCurrentItem(pages - 1);
            tabLayout.getTabAt(pages - 1).select();
        }

        if (status)
        {
            //列表模式
            btnList.setImageResource(R.drawable.ic_list);

        } else
        {
            //成地圖模式
            btnList.setImageResource(R.drawable.ic_location_white);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.img_list:
                if (status)
                {
                    //切換成列表模式
                    status = false;
                    btnList.setImageResource(R.drawable.ic_location_white);

                } else
                {
                    //切換成地圖模式
                    status = true;
                    btnList.setImageResource(R.drawable.ic_list);
                }
                startActivity(new Intent(this, ShopNewActivity.class));
                finish();
                break;
            case R.id.ib_go_back:
                finish();
                break;
        }
    }
}