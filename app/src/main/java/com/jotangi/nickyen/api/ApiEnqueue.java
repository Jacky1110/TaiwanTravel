package com.jotangi.nickyen.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.model.UserBean;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiEnqueue {
    private String TAG = ApiEnqueue.class.getSimpleName() + "(TAG)";

    public interface resultListener {
        void onSuccess(final String message);

        void onFailure(final String message);

    }

    private ApiEnqueue.resultListener listener;

    public static String runTask = "";

    public final String TASK_BONUS_DEADLINE = "TASK_BONUS_DEADLINE";
    public final String TASK_STORE_MEMBER_COUNT = "TASK_STORE_MEMBER_COUNT";
    public final String TASK_MEMBER_LIST = "TASK_MEMBER_LIST";
    public final String TASK_STORE_MEMBER_INFO = "TASK_STORE_MEMBER_INFO";


    // 紅利到期日
    public void bonus_deadline(resultListener listen) {

        runTask = TASK_BONUS_DEADLINE;

        listener = listen;

        String url = ApiConstant.API_URL + ApiConstant.bonus_deadline;
        Log.d(TAG, "URL: " + url);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .addFormDataPart("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Log.d(TAG, "member_id: " + AppUtility.DecryptAES2(UserBean.member_id));
        Log.d(TAG, "member_pwd: " + AppUtility.DecryptAES2(UserBean.member_pwd));

        runOkHttp(url, requestBody);

    }

    //店家會員的人數統計資料
    public void StoreMemberCount(resultListener listen){

        runTask = TASK_STORE_MEMBER_COUNT;

        listener = listen;

        String url = ApiConstant.API_URL + ApiConstant.StoreMemberCount;
        Log.d(TAG, "URL: " + url);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .addFormDataPart("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Log.d(TAG, "member_id: " + AppUtility.DecryptAES2(UserBean.member_id));
        Log.d(TAG, "member_pwd: " + AppUtility.DecryptAES2(UserBean.member_pwd));


        runOkHttp(url, requestBody);
    }

    // 會員管理
    public void MemberList(String startDate, String endDate, resultListener listen){

        runTask = TASK_MEMBER_LIST;

        listener = listen;

        String url = ApiConstant.API_URL + ApiConstant.Member_list;
        Log.d(TAG, "URL: " + url);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .addFormDataPart("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .addFormDataPart("order_startdate", startDate)
                .addFormDataPart("order_enddate", endDate)
                .build();
        Log.d(TAG, "member_id: " + AppUtility.DecryptAES2(UserBean.member_id));
        Log.d(TAG, "member_pwd: " + AppUtility.DecryptAES2(UserBean.member_pwd));

        runOkHttp(url, requestBody);
    }

    // 會員管理資訊
    public void StoreMemberInfo(String mid, resultListener listen){

        runTask = TASK_STORE_MEMBER_INFO;

        listener = listen;

        String url = ApiConstant.API_URL + ApiConstant.Store_member_info;
        Log.d(TAG, "URL: " + url);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .addFormDataPart("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .addFormDataPart("mid", mid)
                .build();
        Log.d(TAG, "member_id: " + AppUtility.DecryptAES2(UserBean.member_id));
        Log.d(TAG, "member_pwd: " + AppUtility.DecryptAES2(UserBean.member_pwd));
        Log.d(TAG, "mid: " + mid);

        runOkHttp(url, requestBody);

    }




    private void runOkHttp(String url,  RequestBody requestBody) {

        Request request = new Request.Builder().url(url).post(requestBody).build();


        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                listener.onFailure("連線失敗");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                ResponseBody responseBody = response.body();

                if (responseBody != null) {
                    String body = responseBody.string();
                    Log.d(TAG, "body: " + body);

                    processBody(body);
                } else {
                    listener.onFailure("無回傳資料");
                }
            }

        });
    }

    private void processBody(String body) {

        switch (runTask) {

            // 紅利到期日
            case TASK_BONUS_DEADLINE:
                taskBonusDeadline(body);
                break;

            //店家會員的人數統計資料
            case TASK_STORE_MEMBER_COUNT:
                taskStoreMemberCount(body);
                break;

            // 會員管理
            case TASK_MEMBER_LIST:
                taskMemberList(body);
                break;

            // 會員管理資訊
            case TASK_STORE_MEMBER_INFO:
                taskStoreMemberInfo(body);
                break;
        }
    }

    private void taskBonusDeadline(String body) {
        listener.onSuccess(body);
    }
    private void taskStoreMemberCount(String body) {
        listener.onSuccess(body);
    }
    private void taskMemberList(String body) {
        listener.onSuccess(body);
    }
    private void taskStoreMemberInfo(String body) {
        listener.onSuccess(body);
    }
}
