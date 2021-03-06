package com.jotangi.nickyen.argame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.nickyen.R;
import com.jotangi.nickyen.argame.BeiPuSleepingTiger.BeiPuSleepingTigerActivity;
import com.jotangi.nickyen.argame.JiaoBanShan.JiaoBanShanActivity;
import com.jotangi.nickyen.argame.LoyaltyCulturalPark.LoyaltyCulturalParkActivity;

public class ARGameActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button btnBack;
    ConstraintLayout layout1, layout2, layout3, layout4;
    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.richmobile.thar");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_game);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);

    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_back:
                finish();
                break;
            case R.id.layout1:
                startActivity(new Intent(this, BeiPuSleepingTigerActivity.class));
//                Toast.makeText(this,"活動即將於9/18開始，敬請期待",Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout2:
                startActivity(new Intent(this, GoldenTriangleActivity.class));
//                Toast.makeText(this,"活動即將於9/18開始，敬請期待",Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout3:
                startActivity(new Intent(this, LoyaltyCulturalParkActivity.class));
//                Toast.makeText(this,"活動將於近期開始，敬請期待！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout4:
                startActivity(new Intent(this, JiaoBanShanActivity.class));
//                Toast.makeText(this,"活動將於近期開始，敬請期待！",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                break;
        }
    }

}
