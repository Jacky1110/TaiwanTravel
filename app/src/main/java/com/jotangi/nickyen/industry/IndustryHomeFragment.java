package com.jotangi.nickyen.industry;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.beautySalons.model.DesignerBean;
import com.jotangi.nickyen.industry.model.ClassBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IndustryHomeFragment extends Fragment implements View.OnClickListener
{
    private ImageButton btnBack;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ProgressBar progressBar;

    private NavController controller;

    private ArrayList<ClassBean> arrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */)
//        {
//            @Override
//            public void handleOnBackPressed()
//            {
//                // Handle the back button event
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_industry_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);

        recyclerView = getView().findViewById(R.id.recycleView);
        progressBar = getView().findViewById(R.id.progressBar);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadData();
    }

    private void loadData()
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.classList(new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<ClassBean>>()
                {
                }.getType();
                arrayList = new Gson().fromJson(jsonString, type);
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        layoutView();
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

    private void layoutView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(R.layout.item_industry, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position)
            {
                Bundle bundle = new Bundle();
                bundle.putString("data", new Gson().toJson(arrayList.get(position)));
                controller = Navigation.findNavController(view);
                controller.navigate(R.id.action_industryHomeFragment_to_industryIntroduceFragment, bundle);
            }
        });
    }

    private class RecyclerAdapter extends BaseQuickAdapter<ClassBean, BaseViewHolder>
    {
        private TextView txtName, txtDescript, txtPrice;
        private RoundedImageView riv;

        public RecyclerAdapter(int layoutResId, @org.jetbrains.annotations.Nullable List<ClassBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, ClassBean classBean)
        {
            txtName = baseViewHolder.getView(R.id.tv_name);
            txtName.setText(classBean.getClassName());
            txtDescript = baseViewHolder.getView(R.id.tv_descript);
            txtDescript.setText(classBean.getClassDescript());
            txtPrice = baseViewHolder.getView(R.id.tv_price);
            txtPrice.setText("NT$: " + classBean.getClassPrice());
            riv = baseViewHolder.getView(R.id.riv);
            Picasso.with(getContext()).load(ApiConstant.API_IMAGE + classBean.getClassPicture()).into(riv);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
                getActivity().finish();
                break;
        }
    }
}