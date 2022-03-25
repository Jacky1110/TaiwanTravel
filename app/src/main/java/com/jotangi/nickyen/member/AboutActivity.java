package com.jotangi.nickyen.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.jotangi.nickyen.R;

public class AboutActivity extends AppCompatActivity
{

    private ImageButton btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));
        btnGoBack = findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}