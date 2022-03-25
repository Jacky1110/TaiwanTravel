package com.jotangi.nickyen.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.base.BaseActivity;
import com.jotangi.nickyen.home.viewModel.NewsFragmentViewModel;
import com.jotangi.nickyen.model.NewsBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/30
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class NewsActivity extends BaseActivity
{
    String nId;

    TextView txtSubject, txtDescript, txtDate;
    ImageView imgContent;
    private NewsFragmentViewModel viewModel;


//    public static NewsActivity newInstance(String str)
//    {
//        NewsActivity fragment = new NewsActivity();
//        Bundle args = new Bundle();
//        nid = str;
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getWindow().setStatusBarColor(ContextCompat.getColor(NewsActivity.this, R.color.typeRed));

        nId = getIntent().getStringExtra("nId");

        if (nId != null)
        {
            initView();
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//        View view = inflater.inflate(R.layout.fragment_news, container, false);
//
//        initView(view);
//
//        return view;
//    }

    private void initView()
    {
        txtSubject = findViewById(R.id.tv_subject);
        txtDate = findViewById(R.id.tv_date);
        txtDescript = findViewById(R.id.tv_descript);
        imgContent = findViewById(R.id.iv_content);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });


        viewModel = new ViewModelProvider(this).get(NewsFragmentViewModel.class);
        viewModel.getMlNewList().observe(this, new Observer<List<NewsBean>>()
        {
            @Override
            public void onChanged(List<NewsBean> data)
            {
                if (data != null)
                {
                    txtSubject.setText(data.get(0).getNewsSubject());
                    txtDate.setText(data.get(0).getNewsDate());
                    txtDescript.setText(data.get(0).getNewsDescript());
                    PicassoTrustAll.getInstance(NewsActivity.this).load(ApiConstant.API_IMAGE+data.get(0).getNewsPicture()).into(imgContent);
                    Log.d("志志", "onChanged: "+ApiConstant.API_IMAGE+data.get(0).getNewsPicture());

                } else
                {
                    Toast.makeText(NewsActivity.this, "網路連線異常，請檢查網路連線", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewModel.callApi(nId);

    }
}
