package com.jotangi.nickyen.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/15
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class BaseFragment extends Fragment implements View.OnClickListener
{
    //此方法用來解決原生replace的舊Fragment會穿透控件問題
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {

    }
    //此方法用來解決原生replace的Fragment會穿透控件問題
}
