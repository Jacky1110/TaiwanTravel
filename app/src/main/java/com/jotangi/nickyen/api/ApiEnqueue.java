package com.jotangi.nickyen.api;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.model.UserBean;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

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

    private static OkHttpClient client;
    private static Handler handler;

    public static String runTask = "";

    public final String TASK_BONUS_DEADLINE = "TASK_BONUS_DEADLINE";
    public final String TASK_STORE_MEMBER_COUNT = "TASK_STORE_MEMBER_COUNT";
    public final String TASK_MEMBER_LIST = "TASK_MEMBER_LIST";
    public final String TASK_STORE_MEMBER_INFO = "TASK_STORE_MEMBER_INFO";
    public final String TASK_STORE_MEMBER_PAYMENTINDEX = "TASK_STORE_MEMBER_PAYMENTINDEX";
    public final String TASK_STORE_MEMBER_COUPON = "TASK_STORE_MEMBER_COUPON";
    public final String TASK_STORE_SETTING = "TASK_STORE_SETTING";
    public final String TASK_STORE_COUPON_USESTATE = "TASK_STORE_COUPON_USESTATE";


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
    public void StoreMemberCount(resultListener listen) {

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

    // (1)查看會員名單
    public void MemberList(String startDate, String endDate, resultListener listen) {

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

    // (2)會員詳細資料
    public void StoreMemberInfo(String mid, resultListener listen) {

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

    // (3)會員消費紀錄
    public void StoreMemberPaymentindex(String mid, String startDate, String endDate, resultListener listen) {

        runTask = TASK_STORE_MEMBER_PAYMENTINDEX;

        listener = listen;

        String url = ApiConstant.API_URL + ApiConstant.Store_member_paymentindex;
        Log.d(TAG, "URL: " + url);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .addFormDataPart("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .addFormDataPart("mid", mid)
                .addFormDataPart("order_startdate", startDate)
                .addFormDataPart("order_enddate", endDate)
                .build();
        Log.d(TAG, "member_id: " + AppUtility.DecryptAES2(UserBean.member_id));
        Log.d(TAG, "member_pwd: " + AppUtility.DecryptAES2(UserBean.member_pwd));
        Log.d(TAG, "mid: " + mid);
        Log.d(TAG, "order_startdate: " + startDate);
        Log.d(TAG, "order_enddate: " + endDate);

        runOkHttp(url, requestBody);

    }

    //取得優惠券列表
    public void storeMemberCcoupon(String member_id, String member_pwd, String sid, String use, String mid, ApiConnection.OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.store_member_coupon;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", member_id)
                .add("member_pwd", member_pwd)
                .add("sid", sid)
                .add("using_flag", use)
                .add("mid", mid)
                .build();
        Log.d(TAG, "member_id: " + member_id);
        Log.d(TAG, "member_pwd: " + member_pwd);
        Log.d(TAG, "sid: " + sid);
        Log.d(TAG, "using_flag: " + use);
        Log.d(TAG, "mid: " + mid);


        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    private void createSuccessCall(Request request, ApiConnection.OnConnectResultListener listener) {
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();
        if (client == null)
            client = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.d(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                Log.d(TAG, "body: " + body);
                try {
                    JSONArray jsonArray = new JSONArray(body);
                    Log.d(TAG, "jsonArray" + jsonArray);

                    listener.onSuccess(jsonArray.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    // 店家設定
    public void storeSetting(resultListener listen) {

        runTask = TASK_STORE_SETTING;

        listener = listen;

        String url = ApiConstant.API_URL + ApiConstant.StoreSetting;
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

    // (6)店家優惠券使用狀況
    public void storeCouponUsestate(resultListener listen) {

        runTask = TASK_STORE_COUPON_USESTATE;

        listener = listen;

        String url = ApiConstant.API_URL + ApiConstant.Store_coupon_usestate;
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


    private void runOkHttp(String url, RequestBody requestBody) {

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

            // 消費紀錄
            case TASK_STORE_MEMBER_PAYMENTINDEX:
                taskStoreMemberPaymenindex(body);
                break;

            case TASK_STORE_MEMBER_COUPON:
                taskStoreMemberCoupon(body);
                break;

            case TASK_STORE_SETTING:
                taskStoreSetting(body);
                break;

            case TASK_STORE_COUPON_USESTATE:
                taskStoreCouponUsestate(body);
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

    private void taskStoreMemberPaymenindex(String body) {
        listener.onSuccess(body);
    }

    private void taskStoreMemberCoupon(String body) {
        listener.onSuccess(body);
    }

    private void taskStoreSetting(String body) {
        listener.onSuccess(body);
    }

    private void taskStoreCouponUsestate(String body) {
        listener.onSuccess(body);
    }
}
