package com.jotangi.nickyen.member;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.base.BaseActivity;
import com.jotangi.nickyen.R;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class RegisterActivity extends BaseActivity implements View.OnClickListener
{
    EditText etName, etPhone, etPassword, etPasswordCheck, etReferrer;
    //使用者條款/隱私權條款
    TextView tv1, tv2;
    Button btnSend;
    ProgressBar progressBar;
    Toolbar toolbar;

    String type; //會員類別 "1",一般會員 "2",特約商家 "3",特約旅行社
    String str;
    String storeId;
    private CheckBox cb1;//1.使用者條款2.隱私權同意

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_registered);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        Intent intent = getIntent();

        if (intent != null)
        {
            type = intent.getStringExtra("member_type");
            str = intent.getStringExtra("barcodeScanned");
            if (!(str == null))
            {
                String[] arrays = str.split("=");
                storeId = arrays[1];
                Log.d("豪豪", "onCreate: " + storeId);
            }
        }
        initView();
    }

    private void initView()
    {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etPasswordCheck = findViewById(R.id.et_password2);
        etReferrer = findViewById(R.id.et_referrer);
        etReferrer.setHint("  推薦人代碼(可空白)");

        if (storeId != null)
        {
            etReferrer.setText(storeId);
        }

//        if (type.equals("1"))
//        {
//            etReferrer.setHint("  推薦人代碼(可空白)");
//
//        } else if (type.equals("2"))
//        {
//            etReferrer.setHint("  特約商家代碼");
//        }
//        else if (type.equals("3"))
//        {
//            etReferrer.setHint("  特約旅行社代碼");
//        }

        progressBar = findViewById(R.id.progressBar);

        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        cb1 = findViewById(R.id.cb_1);
        cb1.setChecked(true);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_send:
                registered();//改跳驗證碼
                break;
            case R.id.tv1:
                Intent intent2 = new Intent(this, RegisterTermsActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv2:
                Intent intent3 = new Intent(this, PrivacyActivity.class);
                startActivity(intent3);
                break;
        }
    }

    private void registered()
    {
        String name = etName.getText().toString().trim();
        String account = etPhone.getText().toString().trim();
        String pw = etPassword.getText().toString().trim();
        String pwd = etPasswordCheck.getText().toString().trim();
//        final String type = "1"; //一般班會員類別1 請於前端檢查格式
        String recommend = etReferrer.getText().toString().trim();

        if (name.equals(""))
        {
            makeToastTextAndShow("名字不得為空", 3500);
            return;
        }
        if (!AppUtility.isNameRegex(name))
        {
            makeToastTextAndShow("名字不得超過30個字元或含有空白", 3500);
            return;
        }
        if (account.equals("") || account.length() != 10 && account.length() != 8)
        {
            makeToastTextAndShow("手機號碼錯誤", 3500);
            return;
        }

        if (pw.equals("") || pwd.equals(""))
        {
            makeToastTextAndShow("密碼不得為空", 3500);
            return;
        }
        if (!AppUtility.isPasswordRegex(pw) || !AppUtility.isPasswordRegex(pwd))
        {
            makeToastTextAndShow("密碼或新密碼必須含有英文及數字", 3500);
            return;
        }
        if (!pw.equals(pwd))
        {
            makeToastTextAndShow("確認密碼是否相符", 3500);
            return;
        }
        if (!cb1.isChecked())
        {
            makeToastTextAndShow("請勾選同意使用者及隱私權條款進行登入", 3500);
            return;
        }
//        if (recommend.equals(""))
//        {
//            makeToastTextAndShow("推薦碼不得為空", 3500);
//            return;
//        }

        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.registered(account, pwd, name, type, recommend, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                ApiConnection.getIIIAdd8(String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000), account, AppUtility.getIPAddress(RegisterActivity.this), "", "", "", "", new ApiConnection.OnConnectResultListener()
                {
                    @Override
                    public void onSuccess(String jsonString)
                    {
                        Log.d("豪豪", "onSuccess: " + jsonString);
                    }

                    @Override
                    public void onFailure(String message)
                    {

                    }
                });
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        getVerify();
                        Intent intent = new Intent(RegisterActivity.this, PhoneVerifyActivity.class);
                        try
                        {
                            savaLoginStatus(false, account, pwd);
                        } catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }

                        getIntent().putExtra("ISLOGIN", false);
                        getIntent().putExtra("LOGIN_USERID", account);
                        getIntent().putExtra("LOGIN_PASSWORD", pwd);
                        setResult(RESULT_OK, getIntent());
                        if (storeId != null)
                        {
                            intent.putExtra("tId", storeId);
                        }
                        if (recommend.equals(""))
                        {
                            intent.putExtra("recommend", true);
                        }
                        startActivity(intent);
                        finish();
                    }
                });
            }

            private void getVerify()
            {
                ApiConnection.verifyCode(account, new ApiConnection.OnConnectResultListener()
                {
                    @Override
                    public void onSuccess(String jsonString)
                    {

                    }

                    @Override
                    public void onFailure(String message)
                    {

                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        if (message.contains("此號碼尚未通過驗證!"))
                        {
                            String s = "此號碼尚未通過驗證，請至登入頁面登入進行驗證";
                            AppUtility.showMyDialog(RegisterActivity.this, s, "確認", null, new AppUtility.OnBtnClickListener()
                            {
                                @Override
                                public void onCheck()
                                {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancel()
                                {

                                }
                            });
                        } else
                        {
                            AppUtility.showMyDialog(RegisterActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
                            {
                                @Override
                                public void onCheck()
                                {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancel()
                                {

                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void savaLoginStatus(boolean status, String account, String pwd) throws UnsupportedEncodingException
    {

        byte[] ac = AppUtility.EncryptAES(ApiConnection.IvAES.getBytes("UTF-8"), ApiConnection.KeyAES.getBytes("UTF-8"), account.getBytes("UTF-8"));
        String encAccount = Base64.encodeToString(ac, Base64.DEFAULT);
        byte[] pw = AppUtility.EncryptAES(ApiConnection.IvAES.getBytes("UTF-8"), ApiConnection.KeyAES.getBytes("UTF-8"), pwd.getBytes("UTF-8"));
        String encPassword = Base64.encodeToString(pw, Base64.DEFAULT);
        SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);
        pref.edit()
                .putBoolean("isLogin", status)
                .putString("account", encAccount)
                .putString("password", encPassword)
                .commit();
        Log.d("儲存", "成功");
    }
}