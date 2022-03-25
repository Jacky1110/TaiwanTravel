package com.jotangi.nickyen.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;

public class SuccessActivity extends AppCompatActivity
{

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succee);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        String str = getIntent().getStringExtra("tId");

        boolean isRecommend = getIntent().getBooleanExtra("recommend", false);

        button = findViewById(R.id.btn_right);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SuccessActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//清空所有Activity保留新的Activity
                intent.putExtra("gift", true);
                intent.putExtra("scan", false);
                if (str != null)
                {
                    intent.putExtra("tId", str);
                } else
                {
                    intent.putExtra("tId", "");
                }
                if (isRecommend)
                {
                    intent.putExtra("recommend", true);
                }
                startActivity(intent);
            }
        });
    }
}