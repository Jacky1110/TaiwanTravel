package com.jotangi.nickyen.industry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.jotangi.nickyen.R;

public class IndustryActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));
    }
}