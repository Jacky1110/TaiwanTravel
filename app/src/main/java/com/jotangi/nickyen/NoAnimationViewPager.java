package com.jotangi.nickyen;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/9/28
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class NoAnimationViewPager extends ViewPager
{
    public NoAnimationViewPager(@NonNull @NotNull Context context)
    {
        super(context);
    }

    public NoAnimationViewPager(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item)
    {
        super.setCurrentItem(item,false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll)
    {
        super.setCurrentItem(item, smoothScroll);
    }
}
