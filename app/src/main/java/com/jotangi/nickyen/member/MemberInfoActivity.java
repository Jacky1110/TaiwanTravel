package com.jotangi.nickyen.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jotangi.nickyen.base.BaseActivity;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.R;

import java.util.HashMap;
import java.util.LinkedList;

public class MemberInfoActivity extends BaseActivity
{
    private Button btnEdit;
    private ListView listView;
    private String[] from = {"title", "id"};
    private int[] to = {R.id.member_title,R.id.member_id};
    LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_member_info);

        getWindow().setStatusBarColor(ContextCompat.getColor(MemberInfoActivity.this, R.color.typeRed));

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MemberInfoActivity.this,MemberModifyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        listView = findViewById(R.id.member_listview);
        initListView();
    }


    private void initListView() {

        MemberInfoBean memberInfoBean = new MemberInfoBean();

        HashMap<String,String> d0 = new HashMap<>();
        d0.put(from[0],"密碼變更");
        d0.put(from[1]," ");
        data.add(d0);

        HashMap<String,String> d1 = new HashMap<>();
        d1.put(from[0],"姓名");
        d1.put(from[1],MemberInfoBean.decryptName);
        data.add(d1);

        HashMap<String,String> d2 = new HashMap<>();
        d2.put(from[0],"手機號碼");
        d2.put(from[1],MemberInfoBean.decryptId);
        data.add(d2);

        HashMap<String,String> d3 = new HashMap<>();
        d3.put(from[0],"出生日期");
        d3.put(from[1],MemberInfoBean.decryptBirthday);
        data.add(d3);

        HashMap<String,String> d4 = new HashMap<>();
        d4.put(from[0],"性別");
//        d4.put(from[1],"預設");
//        data.add(d4);
        if (MemberInfoBean.member_gender.isEmpty()){
            d4.put(from[1],"預設");
        } else if (MemberInfoBean.member_gender.equals("1"))
        {
            d4.put(from[1],"男");
        } else
        {
            d4.put(from[1],"女");
        }
        data.add(d4);

        HashMap<String,String> d5 = new HashMap<>();
        d5.put(from[0],"電子信箱");
        d5.put(from[1],MemberInfoBean.decryptEmail);
        data.add(d5);

        HashMap<String,String> d6 = new HashMap<>();
        d6.put(from[0],"聯絡地址");
        d6.put(from[1],MemberInfoBean.decryptAddress);
        data.add(d6);

        HashMap<String,String> d7 = new HashMap<>();
        d7.put(from[0],"推薦人");
        d7.put(from[1],MemberInfoBean.decryptRecommendCode);
        data.add(d7);

        adapter = new SimpleAdapter(this,data,R.layout.item_memberinfo,from,to);
        listView.setAdapter(adapter);
    }
}