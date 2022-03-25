package com.jotangi.nickyen.member;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.base.BaseActivity;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class LoginActivity extends BaseActivity implements View.OnClickListener
{
    //忘記密碼
    private TextView tv_forget;
    //使用者條款
    private TextView tv1;
    //隱私權條款
    private TextView tv2;
    //註冊帳密
    private Button btnRejister;
    //登入
    private Button btnLogin;

    private CheckBox cb1;//1.使用者條款2.隱私權同意
    private EditText mEtxtId;
    private EditText mEtxtPwd;
    ProgressBar progressBar;

    private static final int CAMERA_PERMISSION_CODE = 100;

    private ArrayList<MemberInfoBean> memberInfoBeanArrayList;
    private MemberInfoBean infoData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));
        checkPermission();
        initView();
    }

    private void initView()
    {

        progressBar = findViewById(R.id.progressBar);
        tv_forget = findViewById(R.id.tv_forget);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        tv_forget.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);

        tv_forget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        btnRejister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);

        btnRejister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        mEtxtId = findViewById(R.id.et_phone);
        mEtxtPwd = findViewById(R.id.et_password);

        cb1 = findViewById(R.id.cb_1);
        cb1.setChecked(true);

    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.btn_login:
                loginProcess();
                break;
            case R.id.btn_register:
                Intent intent = new Intent(this, MemberSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_forget:
                Intent forgetIntent = new Intent(this, ForgetPasswordActivity.class);
                startActivity(forgetIntent);
                break;
            case R.id.tv1:
                Intent termsIntent = new Intent(this, RegisterTermsActivity.class);
                startActivity(termsIntent);
                break;
            case R.id.tv2:
                Intent privacyIntent = new Intent(this, PrivacyActivity.class);
                startActivity(privacyIntent);
                break;
        }
    }

    private void loginProcess()
    {

        final String account = mEtxtId.getText().toString().trim();
        final String pwd = mEtxtPwd.getText().toString().trim();

        if (account.equals(""))
        {
            makeToastTextAndShow("登入手機號碼或密碼不得為空", 3500);
            return;
        }
        if (account.length() != 10 && account.length() != 8)
        {
            String message = "輸入帳戶格式錯誤  請重新輸入帳號。";
            makeToastTextAndShow(message, 3500);
            return;
        }

        if (pwd.equals(""))
        {
            makeToastTextAndShow("登入手機號碼或密碼不得為空", 3500);
            return;
        }
        if (pwd.length() > 30)
        {
            makeToastTextAndShow("登入密碼太長", 3500);
            return;
        }
        if (!cb1.isChecked())
        {
            makeToastTextAndShow("請勾選同意使用者及隱私權條款進行登入", 3500);
            return;
        }

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                progressBar.setVisibility(View.VISIBLE);
                ApiConnection.login(account, pwd, new ApiConnection.OnConnectResultListener()
                {
                    @Override
                    public void onSuccess(final String jsonString)
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressBar.setVisibility(View.GONE);

                                makeToastTextAndShow("登入成功", 3500);

                                try
                                {
                                    savaLoginStatus(true, account, pwd);
                                } catch (UnsupportedEncodingException e)
                                {
                                    e.printStackTrace();
                                }

                                getIntent().putExtra("ISLOGIN", true);
                                getIntent().putExtra("LOGIN_USERID", account);
                                getIntent().putExtra("LOGIN_PASSWORD", pwd);
                                setResult(RESULT_OK, getIntent());

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    //網路連線失敗出現彈跳視窗
                    @Override
                    public void onFailure(final String message)
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressBar.setVisibility(View.GONE);

                                if (message.contains("此號碼尚未通過驗證!"))
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
                                    try
                                    {
                                        savaLoginStatus(false, account, pwd);
                                    } catch (UnsupportedEncodingException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    Intent intent = new Intent(LoginActivity.this, PhoneVerifyActivity.class);
                                    intent.putExtra("account",account);
                                    startActivity(intent);
                                } else
                                {
                                    AppUtility.showMyDialog(LoginActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
                                    {
                                        @Override
                                        public void onCheck()
                                        {

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

    private void checkPermission()
    {
        checkCamera(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }

    private void checkCamera(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED)
        {
            // Requesting the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
        }
//        else
//        {
//            Toast.makeText(this,
//                    "Permission already granted",
//                    Toast.LENGTH_SHORT)
//                    .show();
//        }
    }

    // Step01-設定目前時間變數(使用long是因為System.currentTimeMillis()方法的型態是long):
    private long timeSave = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Step02-判斷是否按下按鍵，並且確認該按鍵是否為返回鍵:
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            // Step03-判斷目前時間與上次按下返回鍵時間是否間隔2000毫秒(2秒):
            if ((System.currentTimeMillis() - timeSave) > 2000)
            {
                Toast.makeText(this, "再按一次結束此應用!!", Toast.LENGTH_SHORT).show();
                // Step04-紀錄第一次案返回鍵的時間:
                timeSave = System.currentTimeMillis();
            } else
            {
                // Step05-結束Activity與關閉APP:
                finish();
                Log.d("退出", "onKeyDown: ");
//                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}