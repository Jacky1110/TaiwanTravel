package com.jotangi.nickyen.pointshop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.nickyen.R;

public class PointChangeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private int[] images = {R.drawable.sample3,R.drawable.sample3,R.drawable.sample3,R.drawable.sample3,
            R.drawable.sample3,R.drawable.sample3};
    private RecycleAdapter recycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_change);

        getWindow().setStatusBarColor(ContextCompat.getColor(PointChangeActivity.this, R.color.typeRed));

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyle_pt_cg);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recycleAdapter = new RecycleAdapter();
        recyclerView.setAdapter(recycleAdapter);

    }

    private class RecycleAdapter extends  RecyclerView.Adapter<RecycleAdapter.MyViewHolder>{


        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            ImageView commodity,point;
            TextView title,count;
            Button change;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                commodity = itemView.findViewById(R.id.iv_commodity);
                point = itemView.findViewById(R.id.iv_p1);
                title = itemView.findViewById(R.id.tv_title);
                count = itemView.findViewById(R.id.tv_count);
                change = itemView.findViewById(R.id.btn_change);

                change.setOnClickListener(this);
                commodity.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("AAA","兌換點擊");

                    }
                });

//                commodity.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(PointChangeActivity.this,PointCommodityActivity.class);
//                        startActivity(intent);
//                        Log.d("BBB","切換頁面");
//                    }
//                });
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_point_recycle,parent,false);

            MyViewHolder viewHolder = new MyViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            int image_id = images[position];

            holder.commodity.setImageResource(image_id);

        }

        @Override
        public int getItemCount() {
            return images.length;
        }

    }

}