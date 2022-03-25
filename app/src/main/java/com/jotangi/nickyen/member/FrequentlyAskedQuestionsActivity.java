package com.jotangi.nickyen.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.industry.model.ClassBean;
import com.jotangi.nickyen.member.adapter.ExpandAdapter;
import com.jotangi.nickyen.member.model.FAQBean;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FrequentlyAskedQuestionsActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageButton btnGoBack;

    private ArrayList<FAQBean> faqList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequently_asked_questions);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        recyclerView = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);
        btnGoBack = findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        loadData();
    }

    private void loadData()
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.questionList(new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {

                Type type = new TypeToken<ArrayList<FAQBean>>()
                {
                }.getType();
                faqList = new Gson().fromJson(jsonString, type);
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        initRecyclerView();
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
            }
        });
    }

    private void initRecyclerView() {
        ExpandAdapter adapter = new ExpandAdapter(faqList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}