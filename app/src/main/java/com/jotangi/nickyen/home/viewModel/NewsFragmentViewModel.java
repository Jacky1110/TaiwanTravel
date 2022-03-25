package com.jotangi.nickyen.home.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.api.RetrofitRepository;
import com.jotangi.nickyen.api.RetrofitService;
import com.jotangi.nickyen.model.NewsBean;
import com.jotangi.nickyen.model.UserBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/7/1
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class NewsFragmentViewModel extends ViewModel
{
    private MutableLiveData<List<NewsBean>> mlNewList;

    public NewsFragmentViewModel()
    {
        mlNewList = new MutableLiveData<>();
    }

    public MutableLiveData<List<NewsBean>> getMlNewList()
    {
        return mlNewList;
    }

    public void callApi(String nid)
    {
        RetrofitService retrofitService = RetrofitRepository.getRetroInstance().create(RetrofitService.class);
        Call<List<NewsBean>> call = null;
        try
        {
            call = retrofitService.getNewsDetail(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), nid);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        call.enqueue(new Callback<List<NewsBean>>()
        {
            @Override
            public void onResponse(Call<List<NewsBean>> call, Response<List<NewsBean>> response)
            {
                if (response.isSuccessful())
                {
                    mlNewList.postValue(response.body());
                } else
                {
                    mlNewList.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<NewsBean>> call, Throwable t)
            {
                mlNewList.postValue(null);
            }
        });
    }
}
