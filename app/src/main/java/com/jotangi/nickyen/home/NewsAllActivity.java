package com.jotangi.nickyen.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.home.viewModel.NewsAllActivityViewModel;
import com.jotangi.nickyen.model.NewsBean;

import java.util.List;

public class NewsAllActivity extends AppCompatActivity
{
    private NewsAllActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private List<NewsBean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_all);

        getWindow().setStatusBarColor(ContextCompat.getColor(NewsAllActivity.this, R.color.typeRed));

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycleView);
        setViewModelCallback();
    }

    private void setViewModelCallback()
    {
        viewModel = new ViewModelProvider(this).get(NewsAllActivityViewModel.class);
        viewModel.getMlNewList().observe(this, new Observer<List<NewsBean>>()
        {
            @Override
            public void onChanged(List<NewsBean> data)
            {
                if (data != null)
                {
                    dataList = data;
                    RecyclerAdapter adapter = new RecyclerAdapter(data,NewsAllActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(NewsAllActivity.this));

                } else
                {
                    Toast.makeText(NewsAllActivity.this, "網路連線異常，請檢查網路連線", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewModel.callApi();
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NewsViewHolder>
    {

        private List<NewsBean> news;
        private Context context;

        public RecyclerAdapter(final List<NewsBean> news, final Context context)
        {
            this.news = news;
            this.context = context;
        }

        class NewsViewHolder extends RecyclerView.ViewHolder
        {

            View view;
            ImageView picture;
            TextView date;
            TextView subject;

            public NewsViewHolder(@NonNull View itemView)
            {
                super(itemView);
                view=itemView;
                picture = itemView.findViewById(R.id.home_news_picture);
                date = itemView.findViewById(R.id.home_news_date);
                subject = itemView.findViewById(R.id.home_news_subject);

            }
        }

        @NonNull
        @Override
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {

            return new RecyclerAdapter.NewsViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_newlist_layout, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull NewsViewHolder holder, int position)
        {

            NewsBean newsData = news.get(position);
            String imagerUrl = ApiConstant.API_IMAGE + newsData.getNewsPicture();
            holder.date.setText(newsData.getNewsDate());
            holder.subject.setText(newsData.getNewsSubject());
            PicassoTrustAll.getInstance(NewsAllActivity.this).load(imagerUrl).into(holder.picture);
            holder.view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent in = new Intent(NewsAllActivity.this, NewsActivity.class);
                    in.putExtra("nId",newsData.getNid());
                    startActivity(in);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return news.size();
        }

    }
}