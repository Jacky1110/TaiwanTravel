package com.jotangi.nickyen.scanner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;

public class PayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        getWindow().setStatusBarColor(ContextCompat.getColor(PayActivity.this, R.color.typeRed));
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        Button btnCash = findViewById(R.id.btn_cash);
        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(PayActivity.this);
                dialog.setContentView(R.layout.dialog_confirm_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button btnClose = dialog.findViewById(R.id.btn_close);
                Button btnPayOk = dialog.findViewById(R.id.btn_payOK);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnPayOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog1 = new Dialog(PayActivity.this);
                        dialog1.setContentView(R.layout.dialog_confirm_succee_layout);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.show();
                        Button btnSucceeClose = dialog1.findViewById(R.id.btn_succee_close);
                        Button btnSucceeEnter = dialog1.findViewById(R.id.btn_succee_enter);

                        btnSucceeClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });

                        btnSucceeEnter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                                startActivity(intent);
                                dialog1.dismiss();
                            }
                        });
                        dialog.dismiss();

                    }
                });
            }
        });

    }
}