package com.jotangi.nickyen.industry;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.industry.model.ClassBean;
import com.squareup.picasso.Picasso;

public class IndustryDetailFragment extends Fragment implements View.OnClickListener
{
    private ImageButton btnBack;
    private TextView txtProgramName, txtContent;
    private ImageView imgProduce;

    private ClassBean classBean;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            classBean = new Gson().fromJson(getArguments().getString("classBean"),ClassBean.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_industry_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable final Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);

        txtProgramName = getView().findViewById(R.id.tv_programName);
        txtProgramName.setText(classBean.getClassName());
        txtContent = getView().findViewById(R.id.tv_content);
        txtContent.setText(classBean.getClassDescript2());
        imgProduce = getView().findViewById(R.id.iv_content);
        Picasso.with(getActivity()).load(ApiConstant.API_IMAGE + classBean.getClassPicture2()).into(imgProduce);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.ib_go_back:
                Navigation.findNavController(getView()).popBackStack();
                break;
        }
    }
}