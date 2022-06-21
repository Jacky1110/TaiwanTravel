package com.jotangi.nickyen.api;

import android.content.Context;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.model.UserBean;
import com.jotangi.nickyen.pointshop.EcorderInfoRequest;
import com.jotangi.nickyen.pointshop.EcorderListRequest;
import com.jotangi.nickyen.pointshop.OrderRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiConnection {
    private static final String TAG = ApiConnection.class.getSimpleName();

    private static Handler handler;
    private static OkHttpClient client;

    //CBC解密規則的參數
    public final static String IvAES = "77215989@jotangi";
    public final static String KeyAES = "AwBHMEUCIQCi7omUvYLm0b2LobtEeRAY";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void initParam(Context context) {
        //要在Main Thread初始化
        if (handler == null)
            handler = new Handler(context.getMainLooper());

        if (client == null)
            client = new OkHttpClient();
    }

    public interface OnConnectJsonResultListerner {
        void onSuccess(final JSONObject jsonObject);

        void onFailure(final String message);
    }

    public interface OnConnectResultListener {
        void onSuccess(final String jsonString);

        void onFailure(final String message);
    }

    public interface OnConnectStringArrayResultListener {
        void onSuccess(final String[] stringArray);

        void onFailure(final String message);
    }

    //解決okhttp https 忽略憑證問題
    public static MyTrustManager mMyTrustManager;

    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            // ssl certification
            mMyTrustManager = new MyTrustManager();
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{mMyTrustManager}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return ssfFactory;
    }

    //實現X509TrustManager接口
    public static class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    //登入
    public static void login(String account, String pwd, @NonNull final OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.LOGIN;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("FCM_Token", UserBean.NOTIFICATION_TOKEN)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String status = jsonObject.getString("status");
                    String code = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("responseMessage");

                    if (code.equals("0x0200")) {
                        UserBean.member_id = AppUtility.EncryptAES2(account);
                        UserBean.member_pwd = AppUtility.EncryptAES2(pwd);
                        listener.onSuccess(jsonObject.getString("responseMessage"));
                    } else {
                        listener.onFailure(jsonObject.getString("responseMessage"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });

    }

    // 推播列表
    public static void getPushList(OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.Push_List;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 發送給會員推播
    public static void fcmToMember(String mobileNo, String title, String content, String extra, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.FCM_TO_MEMBER;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("mobile_no", mobileNo)
                .add("FCM_title", title)
                .add("FCM_content", content)
                .add("FCM_extra", extra)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 發送給店長推播
    public static void fcmToStore(String sid, String title, String content, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.FCM_TO_STORE;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .add("FCM_title", title)
                .add("FCM_content", content)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //會員資料
    public static void getMemberInfo(String account, String pwd, final OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.MemberInfo;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e.getLocalizedMessage() != null) {
                    if (e.getLocalizedMessage() != null) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                    listener.onFailure("與伺服器連線失敗");
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();

                try {
                    JSONArray jsonArray = new JSONArray(body);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Log.e(TAG, jsonObject.toString());
                        MemberInfoBean.member_id = jsonObject.getString("member_id");
                        Log.d(TAG, "MemberInfoBean.member_id: " + AppUtility.DecryptAES2(MemberInfoBean.member_id));
                        MemberInfoBean.member_pwd = jsonObject.getString("member_pwd");
                        MemberInfoBean.member_name = jsonObject.getString("member_name");
                        MemberInfoBean.member_type = jsonObject.getString("member_type");
                        MemberInfoBean.member_gender = jsonObject.getString("member_gender");
                        MemberInfoBean.member_email = jsonObject.getString("member_email");
                        MemberInfoBean.member_birthday = jsonObject.getString("member_birthday");
                        MemberInfoBean.member_address = jsonObject.getString("member_address");
                        MemberInfoBean.member_phone = jsonObject.getString("member_phone");
                        MemberInfoBean.member_picture = jsonObject.getString("member_picture");
                        MemberInfoBean.member_totalpoints = jsonObject.getString("member_totalpoints");
                        MemberInfoBean.member_usingpoints = jsonObject.getString("member_usingpoints");
                        MemberInfoBean.member_status = jsonObject.getString("member_status");
                        MemberInfoBean.recommend_code = jsonObject.getString("recommend_code");
                        MemberInfoBean.bonus_will_get = jsonObject.getString("bonuswillget");

                        MemberInfoBean.member_points = Integer.parseInt(MemberInfoBean.member_totalpoints) - Integer.parseInt(MemberInfoBean.member_usingpoints);
                    }
                    byte[] ID = AppUtility.DecryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), Base64.decode(MemberInfoBean.member_id.getBytes("UTF-8"), Base64.DEFAULT));
                    MemberInfoBean.decryptId = new String(ID, "UTF-8");
                    byte[] PWD = AppUtility.DecryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), Base64.decode(MemberInfoBean.member_pwd.getBytes("UTF-8"), Base64.DEFAULT));
                    MemberInfoBean.decryptPwd = new String(PWD, "UTF-8");
                    byte[] NAME = AppUtility.DecryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), Base64.decode(MemberInfoBean.member_name.getBytes("UTF-8"), Base64.DEFAULT));
                    MemberInfoBean.decryptName = new String(NAME, "UTF-8");
                    byte[] EMAIL = AppUtility.DecryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), Base64.decode(MemberInfoBean.member_email.getBytes("UTF-8"), Base64.DEFAULT));
                    MemberInfoBean.decryptEmail = new String(EMAIL, "UTF-8");
                    byte[] BIRTHDAY = AppUtility.DecryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), Base64.decode(MemberInfoBean.member_birthday.getBytes("UTF-8"), Base64.DEFAULT));
                    MemberInfoBean.decryptBirthday = new String(BIRTHDAY, "UTF-8");
                    byte[] ADDRESS = AppUtility.DecryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), Base64.decode(MemberInfoBean.member_address.getBytes("UTF-8"), Base64.DEFAULT));
                    MemberInfoBean.decryptAddress = new String(ADDRESS, "UTF-8");
                    byte[] PHONE = AppUtility.DecryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), Base64.decode(MemberInfoBean.member_phone.getBytes("UTF-8"), Base64.DEFAULT));
                    MemberInfoBean.decryptPhone = new String(PHONE, "UTF-8");
                    byte[] RECOMMEND_CODE = AppUtility.DecryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), Base64.decode(MemberInfoBean.recommend_code.getBytes("UTF-8"), Base64.DEFAULT));
                    MemberInfoBean.decryptRecommendCode = new String(RECOMMEND_CODE, "UTF-8");

                    listener.onSuccess(String.valueOf(jsonArray));

                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //會員註冊
    public static void registered(String account, String pwd, String name, String type, String recommend, final OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.Register;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("member_name", name)
                .add("member_type", type)
                .add("recommend_code", recommend)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createNewCall(request, listener);
    }

    //發送簡訊驗證碼
    public static void userCode(String account, final OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.UserCode;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", account)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String state = jsonObject.getString("code");
                    String[] successArr = {"0x0200", "0x0201", "0x0202"};
                    if (Arrays.asList(successArr).contains(state)) {
                        listener.onSuccess(state);
                    } else {
                        listener.onFailure(jsonObject.getString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //發送驗證碼
    public static void verifyCode(String account, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.VerifyCode;
        FormBody formBody = new FormBody
                .Builder()
                .add("mobile_no", account)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //手機註冊檢查驗證碼狀態
    public static void checkVerify(String phoneNumber, String code, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.CheckVerify;
        FormBody formBody = new FormBody
                .Builder()
                .add("mobile_no", phoneNumber)
                .add("vcode", code)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //忘記密碼
    public static void resetCode(String account, String pwd, String code, final OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.UserResetpwd;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("code", code)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String state = jsonObject.getString("code");
                    String[] successArr = {"0x0200", "0x0201", "0x0202"};
                    if (Arrays.asList(successArr).contains(state)) {
                        listener.onSuccess(state);
                    } else {
                        listener.onFailure(jsonObject.getString("errorMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //登出
    public static void logout(String account, String pwd, @NonNull final OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.LogOut;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", account)
                .add("pwd", pwd)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null)
                    body = responseBody.string();
                Log.d(TAG, "onResponse: " + body);
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //更改密碼
    public static void userChangePwd(String account, String pwdOld, String pwdNew, final OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.UserChangepwd;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", account)
                .add("old_password", pwdOld)
                .add("new_password", pwdNew)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createNewCall(request, listener);
    }

    //修改資料
    public static void userEdit(String account, String pwd, String name, String gender, String email, String birthday, String address, final OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.UserEdit;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", account)
                .add("member_pwd", pwd)
                .add("member_name", name)
                .add("member_gender", gender)
                .add("member_email", email)
                .add("member_birthday", birthday)
                .add("member_address", address)
//                .add("member_phone", phone)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        createNewCall(request, listener);
    }

    //修改會員大頭照
    public static void uploadFile(File imageFile, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.UserUploadPic;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .addFormDataPart("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .addFormDataPart("upload_filename", imageFile.getName(), RequestBody.create(MediaType.parse("image/jpg"), imageFile))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        createApiCall(request, onConnectResultListener);
    }

    //常見問題
    public static void questionList(OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.QUESTION_LIST;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //取得商店列表
    public static void getShopList(String latitude, String longitude, String area, String type, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.StoreList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("shopping_area", area)
                .add("store_type", type)
                .add("loc_lng", String.valueOf(longitude))
                .add("loc_lat", String.valueOf(latitude))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONArray jsonArray = new JSONArray(body);
                    Log.e(TAG, jsonArray.toString());

                    listener.onSuccess(jsonArray.toString());
//                    String state = jsonObject.getString("returnCode");
//                    if (state.equals(ApiConstant.RETURN_SUCCESS))
//                    {
//                        listener.onSuccess(jsonObject.getJSONArray("returnData").toString());
//                    } else
//                    {
//                        listener.onFailure(jsonObject.getString("errorMsg"));
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //取得店家描述
    public static void getStoreDescript(String sid, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.StoreInfo;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    //取得優惠券列表
    public static void getMyCouponList(String member_id, String member_pwd, String sid, String use, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.MyCouponList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", member_id)
                .add("member_pwd", member_pwd)
//                .add("member_id", "0918565779")
//                .add("member_pwd", "peter0224")
                .add("sid", sid)
                .add("using_flag", use)
                .build();
        Log.d(TAG, "member_id: " + member_id);
        Log.d(TAG, "member_pwd: " + member_pwd);
        Log.d(TAG, "sid: " + sid);
        Log.d(TAG, "using_flag: " + use);

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    //取得優惠券列表2
    public static void getMyCouponList2(String member_id, String member_pwd, String sid, String use, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.MyCouponList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", member_id)
                .add("member_pwd", member_pwd)
                .add("sid", sid)
                .add("using_flag", use)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();
        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    body = responseBody.string();

                    listener.onSuccess(body);
                }

            }
        });
    }

    //取得好康推薦列表
    public static void getCouponList(String sid, String type, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.CouponList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .add("coupon_type", type)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    //領取好康推薦優惠券
    public static void getCoupon(String couponId, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.GetCoupon;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("coupon_id", couponId)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

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
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String code = jsonObject.getString("code");
                    if (code.equals("0x0200")) {
                        listener.onSuccess(body);
                    } else if (code.equals("0x0206")) {
                        listener.onFailure("您已經擁有此優惠券！");
                    } else {
                        listener.onFailure("領取有誤，請聯繫客服人員！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //確認是否為掃描該店家之會員
    public static void isMemberStatus(String tId, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.IsMemberStatus;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("store_id", tId)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    body = responseBody.string();

                    listener.onSuccess(body);
                }

            }
        });
    }

    //加入會員
    public static void addMemberCard(String sid, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.AddMemberCard;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String code = jsonObject.getString("code");
                    String couponInfo = jsonObject.getString("couponinfo");
                    if (code.equals("0x0200")) {
                        listener.onSuccess(couponInfo);
                    } else {
                        listener.onFailure(code);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });

    }

    //取得會員卡清單
    public static void getMemberCardList(OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.GetMemberCard;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    //會員獲取點數清單
    public static void getMyBonus(String startDate, String endDate, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.BonusList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("bonus_startdate", startDate)
                .add("bonus_enddate", endDate)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    // 紅利到期日
    public static void bonus_deadline(OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.bonus_deadline;
        Log.d(TAG, "URL: " + url);

        FormBody formBody = new FormBody.Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Log.d(TAG, "member_id: " + AppUtility.DecryptAES2(UserBean.member_id));
        Log.d(TAG, "member_pwd: " + AppUtility.DecryptAES2(UserBean.member_pwd));

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    //會員獲取消費記錄
    public static void getMemberOrderList(String startDate, String endDate, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.MemberOrderList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("order_startdate", startDate)
                .add("order_enddate", endDate)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    // 獲取消費記錄內頁
    public static void getOrderInfo(String order_id, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.MemberOrderInfo;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("order_no", order_id)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //會員美容美髮預約記錄結帳
    public static void memberAddOrder(String sid, String couponNo, String couponDiscount, String bonus, String total, String type, String last, String bookingNo, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.MemberAddOrder;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .add("coupon_no", couponNo)
                .add("discount_amount", couponDiscount)
                .add("bonus_point", bonus)
                .add("order_amount", total)
                .add("pay_type", type)
                .add("order_pay", last)
                .add("booking_no", bookingNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 判斷是否為紅利點數店家
    public static void isBonusStore(String sid, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.IS_BONUS_STORE;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
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
                onConnectResultListener.onFailure("與伺服器連線失敗");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    body = responseBody.string();
                }
                try {
                    Object obj = new JSONTokener(body).nextValue();
                    if (obj instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(body);
                        Log.e("ApiConnection", jsonObject.toString());
                        if (jsonObject.toString().contains("status")) {
                            onConnectResultListener.onFailure(jsonObject.toString());
                        } else {
                            onConnectResultListener.onSuccess(jsonObject.toString());
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 會員產業體驗預約結帳
    public static void memberAddClassOrder(String sid, String couponNo, String discountAmount, String point, String total, String type, String last, String bookingNo, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.MemberAddClassOrder;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .add("coupon_no", couponNo)
                .add("discount_amount", discountAmount)
                .add("bonus_point", point)
                .add("order_amount", total)
                .add("pay_type", type)
                .add("order_pay", last)
                .add("booking_no", bookingNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    /**
     * 店長app相關
     */
    //取得會員資訊是店長的帳號密碼將會取得sid
    public static void getMerchMemberInfo(OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.MemberInfo;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    //檢查票券狀態
    public static void checkCoupon(String couponNo, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.CheckCoupon;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("coupon_no", couponNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    //核銷票券
    public static void applyCoupon(String mid, String couponNo, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.ApplyCoupon;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("m_id", mid)
                .add("coupon_no", couponNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();

        if (handler == null)
            handler = new Handler();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String code = jsonObject.getString("code");
                    if (code.equals("0x0200")) {
                        listener.onSuccess(code);
                    } else {
                        listener.onFailure(code);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //檢查會員點數
    public static void checkBonus(String mid, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.CheckBonus;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("m_id", mid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    //結帳核銷成功
    public static void addOrder(String sid, String mid, String couponNo, String couponDiscount, String bonus, String total, String type, String last, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.AddOrder;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("m_id", mid)
                .add("store_id", sid)
                .add("coupon_no", couponNo)
                .add("discount_amount", couponDiscount)
                .add("bonus_point", bonus)
                .add("order_amount", total)
                .add("pay_type", type)
                .add("order_pay", last)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createNew2Call(request, listener);
    }

    //獲取店長訂單記錄
    public static void getMerchOrderList(String startDate, String endDate, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.MerchOrderList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("order_startdate", startDate)
                .add("order_enddate", endDate)
                .build();
        Log.d(TAG, "order_startdate: " + startDate);
        Log.d(TAG, "order_enddate: " + endDate);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }

    //獲取店長訂單記錄
    public static void getMerchOrderInfo(String order_no, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.MerchOrderInfo;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("order_no", order_no)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createSuccessCall(request, listener);
    }


    // 店長對帳
    public static void getProfitInfo(String month, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.PROFIT_INFO;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("profit_month", month)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //店家查詢設計師
    public static void storeHairStylist(OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.StoreHairStyList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //店家獲取美容美髮預約記錄
    public static void storeBookingList(String start, String end, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.StoreBookingList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("booking_startdate", start)
                .add("booking_enddate", end)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //店家獲取預約紀錄明細
    public static void storeBookingInfo(String bookingNo, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.StoreBookingInfo;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("booking_no", bookingNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 美容美髮店家獲取預約紀錄更新狀態
    public static void storeBookingUpdate(String bookingNo, String reserveDate, String reserveTime, String reserveRemark, String reserveStatus, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.STORE_BOOKING_UPDATE;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("booking_no", bookingNo)
                .add("reserve_date", reserveDate)
                .add("reserve_time", reserveTime)
                .add("reserve_remark", reserveRemark)
                .add("reserve_status", reserveStatus) // 1.預約尚未確認2.預約已回覆3.預約已取消4.預約已完成
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 產業體驗店家獲取預約紀錄更新狀態
    public static void classBookingUpdate(String bookingNo, String reserveDate, String reserveTime, String reserveRemark, String reserveStatus, String person, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.STORE_CLASS_BOOKING_UPDATE;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("booking_no", bookingNo)
                .add("reserve_date", reserveDate)
                .add("reserve_time", reserveTime)
                .add("reserve_remark", reserveRemark)
                .add("program_person", person)
                .add("reserve_status", reserveStatus) // 1.預約尚未確認2.預約已回覆3.預約已取消4.預約已完成
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 店長產業體驗預約記錄
    public static void storeClassBookingList(String start, String end, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.StoreClassBookingList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("booking_startdate", start)
                .add("booking_enddate", end)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 產業體驗記錄明細
    public static void storeClassBookingInfo(String bookingNo, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.StoreClassBookingInfo;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("booking_no", bookingNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    /**
     * AR相關
     */
    //取得商店資訊
    public static void getARShopInfo(String aid, OnConnectResultListener listener) {
        String url = ApiConstant.API_URL + ApiConstant.arInfo;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("aid", aid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

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
            public void onFailure(Call call, IOException e) {
                if (e.getLocalizedMessage() != null) {
                    Log.e("getARShopInfo", e.getLocalizedMessage());
                }
                listener.onFailure("與伺服器連線失敗。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String body = null;
                ResponseBody responseBody = response.body();

                if (responseBody != null)
                    body = responseBody.string();
                try {
                    JSONArray jsonArray = new JSONArray(body);
                    Log.e("getARShopInfo", jsonArray.toString());

                    listener.onSuccess(jsonArray.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //獲取商店列表
    public static void getARList(String shoppingArea, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.arList;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("shopping_area", shoppingArea)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 領取優惠券
    public static void getCoupon2(String couponID, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.getCoupon2;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("coupon_id", couponID)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 優惠券說明
    public static void myCouponList2(String sid, String usingFlag, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.myCouponList2;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .add("using_flag", usingFlag)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 針對AR的核銷優惠券
    public static void applyCoupon2(String couponNo, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.applyCoupon2;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("coupon_no", couponNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 針對AR的核銷優惠券
    public static void arStore(String aid, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.arStore;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("qid", "")
                .add("aid", aid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    public static void getDistance(String lat1, String lng1, String lat2, String lng2, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.getDistance;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("loc_lat1", lat1)
                .add("loc_lng1", lng1)
                .add("loc_lat2", lat2)
                .add("loc_lng2", lng2)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    /**
     * 美容美髮相關
     */
    //判斷是否為該店家黑名單
    public static void isStoreBlackList(String sid, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.IsStoreBlackList;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //查詢設計師
    public static void getHairstylist(String sid, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.GetHairstylist;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //查詢服務項目
    public static void getHairService(String sid, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.GetHairService;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    public static void getWorkingDay(String hid, String sid, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.GetWorkingDay;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("hid", hid)
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //查詢設計師預約訂單
    public static void getBookingDay(String hid, String workingDate, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.IsBookingDay;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("hid", hid)
                .add("reserve_date", workingDate)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

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
                onConnectResultListener.onFailure("與伺服器連線失敗");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    body = responseBody.string();
                }
                try {
                    Object obj = new JSONTokener(body).nextValue();
                    Log.e(TAG, "這是" + obj);
                    if (obj instanceof JSONArray) {
                        onConnectResultListener.onSuccess(body);

                    } else if (obj instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(body);
                        String status = jsonObject.getString("status");
                        String code = jsonObject.getString("code");
                        String responseMessage = jsonObject.getString("responseMessage");

                        if (code.equals("0x0200") || code.equals("0x0201")) {
                            onConnectResultListener.onSuccess(responseMessage);
                        } else {
                            onConnectResultListener.onFailure(responseMessage);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //新增訂單
    public static void addBooking(String sid, String hid, String date, String time, String service, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.AddBooking;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .add("hid", hid)
                .add("reserve_date", date)
                .add("reserve_time", time)
                .add("service_item", service)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //獲取訂單列表
    public static void getBookingList(OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.BookingList;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);

    }

    //取消預約
    public static void cancelBooking(String bookingNo, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.BookingCancel;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("booking_no", bookingNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    /**
     * 產業體驗相關
     */
    // 產業體驗課程
    public static void classList(OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.CLASS_LIST;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 產業體驗課程
    public static void programList(String cID, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.PROGRAM_LIST;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("cid", cID)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 產業體驗預約日期
    public static void getClassWorkingDay(@NotNull String pid, @NotNull ApiConnection.OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.GET_CLASS_WORKING_DAY;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("pid", pid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //查詢產業體驗指定預約日期
    public static void isClassBookingDay(@NotNull String pid, @NotNull String workingDate, @NotNull ApiConnection.OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.IS_CLASS_BOOKING_DAY;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("pid", pid)
                .add("reserve_date", workingDate)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

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
                onConnectResultListener.onFailure("與伺服器連線失敗");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    body = responseBody.string();
                }
                try {
                    Object obj = new JSONTokener(body).nextValue();
                    Log.e(TAG, "這是" + obj);
                    if (obj instanceof JSONArray) {
                        onConnectResultListener.onSuccess(body);

                    } else if (obj instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(body);
                        String status = jsonObject.getString("status");
                        String code = jsonObject.getString("code");
                        String responseMessage = jsonObject.getString("responseMessage");

                        if (code.equals("0x0200") || code.equals("0x0201")) {
                            onConnectResultListener.onSuccess(responseMessage);
                        } else {
                            onConnectResultListener.onFailure(responseMessage);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //新增訂單
    public static void addClassBooking(String sid, String hid, String date, String time, String service, String remark, String note, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.ADD_CLASS_BOOKING_DAY;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .add("pid", hid)
                .add("reserve_date", date)
                .add("reserve_time", time)
                .add("program_person", service)
                .add("reserve_remark", remark)
                .add("reserve_note", note)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //會員中心預約記錄
    public static void getClassBookingList(OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.CLASS_BOOKING_LIST;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 會員中心取消預約
    public static void classBookingCancel(String bookingNo, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.CLASS_BOOKING_CANCEL;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("booking_no", bookingNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    /**
     * 星座測驗相關
     */
    // 產業體驗課程
    public static void saveConstellation(String gender, String birthday, String drink, String housework, String emotion, String hobby, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.SAVE_CONSTELLATION;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("gender", gender)
                .add("birthday", birthday)
                .add("drink", drink)
                .add("housework", housework)
                .add("emotion", emotion)
                .add("hobby", hobby)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

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
                onConnectResultListener.onFailure("與伺服器連線失敗");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    body = responseBody.string();
                }
                try {
                    Object obj = new JSONTokener(body).nextValue();
                    if (obj instanceof JSONArray) {
                        onConnectResultListener.onSuccess(body);
                        Log.e(TAG, body);

                    } else if (obj instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(body);
                        Log.e(TAG, jsonObject.toString());
                        String status = jsonObject.getString("status");
                        String code = jsonObject.getString("code");
                        String responseMessage = jsonObject.getString("responseMessage");

                        if (status.equals("false")) {
                            onConnectResultListener.onFailure(responseMessage);
                        } else if (status.equals("true")) {
                            String constellation = jsonObject.getString("constellation");
                            onConnectResultListener.onSuccess(constellation);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 商城相關
     */
    // 獲取商城Tab
    public static void getProductType(OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_MALL_URL + ApiConstant.PRODUCT_TYPE;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 獲取商品List
    public static void getProductList(String type, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_MALL_URL + ApiConstant.PRODUCT_LIST;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("product_type", type)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 獲取商品個別Info
    public static void getProductInfo(String productNo, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_MALL_URL + ApiConstant.PRODUCT_INFO;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("product_no", productNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 加入購物車
    public static void addShoppingCart(String productNo, String productPrice
            , String orderQty, String totalAmount, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_MALL_URL + ApiConstant.ADD_SHOPPING_CART;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("product_no", productNo)
                .add("product_price", productPrice)
                .add("order_qty", orderQty)
                .add("total_amount", totalAmount)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 購物車內商品列表
    public static void getShoppingCartList(OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_MALL_URL + ApiConstant.SHOPPING_CART_LIST;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 修改購物車內商品數量
    public static void editShoppingCart(String productNo, String orderQty
            , OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_MALL_URL + ApiConstant.EDIT_SHOPPING_CART;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("product_no", productNo)
                .add("order_qty", orderQty)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 修改購物車內商品數量
    public static void delShoppingCart(String productNo, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_MALL_URL + ApiConstant.DEL_SHOPPING_CART;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("product_no", productNo)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 購物車數量
    public static void getNumberShoppingCart(OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_MALL_URL + ApiConstant.SHOPPING_CART_COUNT;

        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 商城訂單明細
    public static void getEcorderInfo(EcorderInfoRequest e_request,OnConnectResultListener onConnectResultListener){
        String url = ApiConstant.API_MALL_URL + ApiConstant.ECORDER_INFO;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id",e_request.getMember_id())
                .add("member_pwd",e_request.getMember_pwd())
                .add("order_no",e_request.getOrder_no())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 商城訂單紀錄
    public static void getEcorderList(EcorderListRequest e_request,OnConnectResultListener onConnectResultListener){
        String url = ApiConstant.API_MALL_URL + ApiConstant.ECORDER_LIST;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id",e_request.getMember_id())
                .add("member_pwd",e_request.getMember_pwd())
                .add("order_startdate",e_request.getOrder_startdate())
                .add("order_enddate",e_request.getOrder_enddate())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    // 新增商城訂單
    public static void addEcorder(OrderRequest o_request,OnConnectResultListener onConnectResultListener){
        String url = ApiConstant.API_MALL_URL + ApiConstant.ADD_ECORDER;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", o_request.getMember_id())
                .add("member_pwd", o_request.getMember_pwd())
                .add("order_amount", o_request.getOrder_amount().toString())
                .add("coupon_no", o_request.getCoupon_no())
                .add("discount_amount",o_request.getDiscount_amount().toString())
                .add("order_pay",o_request.getOrder_pay().toString())
                .add("bonus_point",o_request.getBonus_point().toString())
                .add("delivery_type",o_request.getDelivery_type().toString())
                .add("recipient_name",o_request.getRecipient_name())
                .add("recipient_addr",o_request.getRecipient_addr())
                .add("recipient_phone",o_request.getRecipient_phone())
                .add("recipient_mail",o_request.getRecipient_mail())
                .add("invoice_type",o_request.getInvoice_type().toString())
                .add("invoice_phone",o_request.getInvoice_phone())
                .add("company_title",o_request.getCompany_title())
                .add("uniform_no",o_request.getUniform_no())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    private static void createApiCall(Request request, OnConnectResultListener onConnectResultListener) {
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
                onConnectResultListener.onFailure("與伺服器連線失敗");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = null;
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    body = responseBody.string();
                }
                try {
                    Object obj = new JSONTokener(body).nextValue();
                    if (obj instanceof JSONArray) {
                        onConnectResultListener.onSuccess(body);
                        Log.e(TAG, body);

                    } else if (obj instanceof JSONObject) {
                        JSONObject jsonObject = new JSONObject(body);
                        Log.e("ApiConnection", jsonObject.toString());
                        String status = jsonObject.getString("status");
                        String code = jsonObject.getString("code");
                        String responseMessage = jsonObject.getString("responseMessage");

                        if (status.equals("false")) {
                            onConnectResultListener.onFailure(responseMessage);
                        } else if (status.equals("true")) {
                            onConnectResultListener.onSuccess(responseMessage);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Api回傳結果的解析
    private static void createNewCall(Request request, OnConnectResultListener listener) {
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
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String code = jsonObject.getString("code");
                    String mes = jsonObject.getString("responseMessage");
//                    String couponInfo = jsonObject.getString("couponinfo");
                    if (code.equals("0x0200")) {
                        listener.onSuccess(code);
                    } else {
                        listener.onFailure(mes);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //只要0x0200回傳成功其他都是失敗
    private static void createNew2Call(Request request, OnConnectResultListener listener) {
        if (client == null)
            client = new OkHttpClient();
//        OkHttpClient client1 = new OkHttpClient.Builder()
//                .sslSocketFactory(createSSLSocketFactory(), mMyTrustManager).build();
//        if (client1 == null)
//            client1 = new OkHttpClient();

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
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e(TAG, jsonObject.toString());

                    String code = jsonObject.getString("code");
                    if (code.equals("0x0200")) {
                        listener.onSuccess(code);
                    } else {
                        listener.onFailure(code);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(e.getMessage());
                }
            }
        });
    }

    //Api回傳結果的解析(jsonArray成功直接返回data)
    private static void createSuccessCall(Request request, OnConnectResultListener listener) {
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

    // region ---- 資策會大數據Api回傳失敗不理會

    /**
     * mode:8
     * app_id:110_依德科技
     * time:202100817
     * device_id:0918565779
     * ip:127.0.0.1
     * gender:男
     * location:
     * age:
     * view:波菲爾南亞店
     **/
    //使用者樣貌(註冊時個人資料、加入店家會員)
    public static void getIIIAdd8(String time, String account
            , String ipAddress, String gender
            , String location, String age
            , String view, OnConnectResultListener listener) {
        String url = "https://apptracker.tk/app_restful/public/index.php/api/customers/add/8";
        FormBody formBody = new FormBody
                .Builder()
                .add("mode", "8")
                .add("app_id", "110_依德科技")
                .add("time", time)
                .add("device_id", account)
                .add("ip", ipAddress)
                .add("gender", gender)
                .add("location", location)
                .add("age", age)
                .add("view", view)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createIIICall(request, listener);
    }

    //功能點擊喜好(註冊時個人資料、加入店家會員)
    public static void getIIIAdd4(String functionId, String functionName
            , String time, String account
            , String ipAddress, String view, String nowPage, OnConnectResultListener listener) {
        String url = "https://apptracker.tk/app_restful/public/index.php/api/customers/add/4";
        FormBody formBody = new FormBody
                .Builder()
                .add("mode", "4")
                .add("app_id", "110_依德科技")
                .add("function_id", functionId)
                .add("function_name", functionName)
                .add("time", time)
                .add("device_id", account)
                .add("ip", ipAddress)
                .add("view", view)
                .add("now_page", nowPage)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createIIICall(request, listener);
    }

    /**
     * mode:2
     * app_id:110_依德科技
     * action_id:美容美髮
     * action_name:預約
     * use_time:17
     * time:1629365946
     * device_id:0918565779
     * ip:127.0.0.1
     * view:波菲爾企業管理顧問(股)公司
     * now_page:洗髮250
     */
    //應用情境停留
    public static void getIIIAdd2(String time, String account, String ipAddress, String finalTime, String storeName, String s, OnConnectResultListener listener) {
        String url = "https://apptracker.tk/app_restful/public/index.php/api/customers/add/2";
        FormBody formBody = new FormBody
                .Builder()
                .add("mode", "2")
                .add("app_id", "110_依德科技")
                .add("action_id", "美容美髮")
                .add("action_name", "預約")
                .add("use_time", finalTime)
                .add("time", time)
                .add("device_id", account)
                .add("ip", ipAddress)
                .add("view", storeName)
                .add("now_page", s)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createIIICall(request, listener);
    }

    /**
     * mode:9
     * app_id:110_依德科技
     * store_name:打卡折扣100 (不可為空)
     * product_name:洗髮250///染髮450
     * action:現金
     * price:點數折抵
     * address:洗髮加染髮
     * time:1631587652
     * device_id:0918565779
     * ip:fe80::234:75c1:68cf:24b%ccmni0
     * view:波菲爾
     * note1:3000
     * note2:Belle
     */
    //消費商品資訊
    public static void getIIIAdd9(String couponName, String servicePrice, String payType, String point, String service, String time, String account, String ipAddress, String storeName, String total, String designer, OnConnectResultListener listener) {
        String url = "https://apptracker.tk/app_restful/public/index.php/api/customers/add/9";
        FormBody formBody = new FormBody
                .Builder()
                .add("mode", "9")
                .add("app_id", "110_依德科技")
                .add("store_name", couponName)
                .add("product_name", servicePrice)
                .add("action", payType)
                .add("price", point)
                .add("address", service)
                .add("time", time)
                .add("device_id", account)
                .add("ip", ipAddress)
                .add("view", storeName)
                .add("note1", total)
                .add("note2", designer)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createIIICall(request, listener);
    }

    private static void createIIICall(Request request, OnConnectResultListener listener) {
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

                listener.onSuccess(body);
                Log.e(TAG, "onResponse: " + body);

            }
        });
    }

    // endregion

    //取得店家問券資料
    public static void getQuestionnaireList(String sid, OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.QUESTIONNAIRE_LIST;
        FormBody formBody = new FormBody
                .Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }

    //送出店家問券答案
    public static void sendQuestionnaire(
            String qid,
            String answer,
            Boolean hasOther,
            String others_remark,
            OnConnectResultListener onConnectResultListener) {
        String url = ApiConstant.API_URL + ApiConstant.SEND_QUESTIONNAIRE;
        FormBody formBody = new FormBody.Builder()
                .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                .add("qid", qid)
                .add("answer", answer)
                .add("others", hasOther ? "1" : "0")
                .add("others_remark", others_remark)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        createApiCall(request, onConnectResultListener);
    }
}
