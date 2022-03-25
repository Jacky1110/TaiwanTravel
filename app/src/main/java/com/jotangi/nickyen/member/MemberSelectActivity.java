package com.jotangi.nickyen.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jotangi.nickyen.R;
import com.jotangi.nickyen.scanner.SurfaceViewActivity;

public class MemberSelectActivity extends AppCompatActivity implements View.OnClickListener
{

    LinearLayout btnShop, btnMember, btnTravel;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_select);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        initView();
    }

    private void initView()
    {
        btnMember = findViewById(R.id.btn_member); //一般會員
        btnShop = findViewById(R.id.btn_shop); //特約商家
//        btnTravel = findViewById(R.id.btn_travel); //特約旅行社

        btnMember.setOnClickListener(this);
        btnShop.setOnClickListener(this);
//        btnTravel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        final String general = "1"; //一般會員類別
        final String shop = "1"; //特約商家類別
//        final String travel = "3"; //特約旅行社類別

        switch (v.getId())
        {
            case R.id.btn_member:

                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("member_type", general);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_shop:
                Intent intent2 = new Intent(this, SurfaceViewActivity.class);
                intent2.putExtra("member_type", shop);
                startActivity(intent2);
                finish();
                break;
//            case R.id.btn_travel:
//                Intent intent3 = new Intent(this, RegisterActivity.class);
//                intent3.putExtra("member_type",travel);
//                startActivity(intent3);
//                finish();
//                break;
        }
    }
}