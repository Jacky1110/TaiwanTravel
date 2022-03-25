package com.jotangi.nickyen.api;

import com.jotangi.nickyen.home.myquestionnaire.MyQuestionnaireModel;
import com.jotangi.nickyen.merch.reconciliation.model.Reconciliation;
import com.jotangi.nickyen.model.NewsBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/6/8
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public interface RetrofitService
{
    // 最新消息全部
    @FormUrlEncoded
    @POST("news_list.php")
    Call<List<NewsBean>> getNewsList(@Field("member_id") String member_id, @Field("member_pwd") String member_pwd);
    // 最新消息內頁
    @FormUrlEncoded
    @POST("news_detail.php")
    Call<List<NewsBean>> getNewsDetail(@Field("member_id") String member_id, @Field("member_pwd") String member_pwd, @Field("news_id") String news_id);
    // 問券列表
    @FormUrlEncoded
    @POST("questionnaire_title.php")
    Call<List<MyQuestionnaireModel>> questionnaireTitle(@Field("member_id") String member_id, @Field("member_pwd") String member_pwd);
}
