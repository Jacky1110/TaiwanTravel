package com.jotangi.nickyen.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

public class PhoneVerifyActivity extends AppCompatActivity implements View.OnClickListener
{

    private TextView txtPhone, txtHint, txtTime;
    private ImageButton btnGoBack;
    private EditText etCode1, etCode2, etCode3, etCode4;
    private ConstraintLayout btnResend;
    private ProgressBar progressBar;
    private String phoneNumber;
    private String pwd;

    private static String CountDown_OK = "1";
    private String code = "";

    private String storeId;
    private boolean recommend;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        phoneNumber = AppUtility.DecryptAES2(sp.getString("account", ""));
        pwd = AppUtility.DecryptAES2(sp.getString("password", ""));


        storeId = getIntent().getStringExtra("tId");
        recommend = getIntent().getBooleanExtra("recommend", false);

        initView();

    }

    private void initView()
    {
        btnGoBack = findViewById(R.id.ib_go_back);
        btnResend = findViewById(R.id.layout2);
        btnGoBack.setOnClickListener(this);
        btnResend.setOnClickListener(this);

        txtPhone = findViewById(R.id.tv_phone);
        txtHint = findViewById(R.id.tv_hint);
        txtTime = findViewById(R.id.tv_time);
        etCode1 = findViewById(R.id.et_code1);
        etCode2 = findViewById(R.id.et_code2);
        etCode3 = findViewById(R.id.et_code3);
        etCode4 = findViewById(R.id.et_code4);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        layoutView();
    }

    private void layoutView()
    {
//        if (phoneNumber.length() > 10)
//        {
//            txtPhone.setText("驗證碼已發送至 " + AppUtility.DecryptAES2(phoneNumber));
//        } else
//        {
        txtPhone.setText("驗證碼已發送至 " + phoneNumber);
//        }

        //建立文字監聽
        TextWatcher mTextWatcher = new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //如果字數達到4，取消自己焦點，下一個EditText取得焦點
                if (etCode1.getText().toString().length() == 1)
                {
                    etCode1.clearFocus();
                    etCode2.requestFocus();
                }

                if (etCode2.getText().toString().length() == 1)
                {
                    etCode2.clearFocus();
                    etCode3.requestFocus();
                }

                if (etCode3.getText().toString().length() == 1)
                {
                    etCode3.clearFocus();
                    etCode4.requestFocus();
                }

                //如果字數達到4，取消自己焦點，隱藏虛擬鍵盤
                if (etCode4.getText().toString().length() == 1)
                {
                    IBinder mIBinder = getCurrentFocus().getWindowToken();
                    InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(mIBinder, InputMethodManager.HIDE_NOT_ALWAYS);

                    if (etCode1.getText().toString().trim().length() == 1
                            && etCode2.getText().toString().trim().length() == 1
                            && etCode3.getText().toString().trim().length() == 1
                            && etCode4.getText().toString().trim().length() == 1)
                    {

                        code = etCode1.getText().toString() +
                                etCode2.getText().toString() +
                                etCode3.getText().toString() +
                                etCode4.getText().toString();
                        Log.d("安安", "layoutView: " + code);
                        if (code != null && code.length() == 4)
                        {

//                            checkVerify(code);
                            progressBar.setVisibility(View.VISIBLE);
                            ApiConnection.checkVerify(phoneNumber, code, new ApiConnection.OnConnectResultListener()
                            {
                                @Override
                                public void onSuccess(String jsonString)
                                {
                                    runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            progressBar.setVisibility(View.GONE);
                                            Intent intent = new Intent(PhoneVerifyActivity.this, SuccessActivity.class);

                                            try
                                            {
                                                savaLoginStatus(true, phoneNumber, pwd);
                                            } catch (UnsupportedEncodingException e)
                                            {
                                                e.printStackTrace();
                                            }
                                            getIntent().putExtra("ISLOGIN", true);
                                            getIntent().putExtra("LOGIN_USERID", phoneNumber);
                                            getIntent().putExtra("LOGIN_PASSWORD", pwd);

                                            setResult(RESULT_OK, getIntent());
                                            if (storeId != null)
                                            {
                                                intent.putExtra("tId", storeId);
                                            }
                                            if (recommend)
                                            {
                                                intent.putExtra("recommend", true);
                                            }
                                            startActivity(intent);
                                            finish();
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
                                            txtHint.setText("驗證碼錯誤");
                                            etCode1.setText(null);
                                            etCode2.setText(null);
                                            etCode3.setText(null);
                                            etCode4.setText(null);
                                            etCode4.clearFocus();
                                            etCode1.requestFocus();
                                        }
                                    });
                                }
                            });
                        }
                    }
                    //下方為顯示虛擬鍵盤
//                    mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        };

        //加入文字監聽
        etCode1.addTextChangedListener(mTextWatcher);
        etCode2.addTextChangedListener(mTextWatcher);
        etCode3.addTextChangedListener(mTextWatcher);
        etCode4.addTextChangedListener(mTextWatcher);

    }

    private void checkVerify(String code)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.checkVerify(phoneNumber, code, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(PhoneVerifyActivity.this, SuccessActivity.class);

                        try
                        {
                            savaLoginStatus(true, phoneNumber, pwd);
                        } catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }
                        getIntent().putExtra("ISLOGIN", true);
                        getIntent().putExtra("LOGIN_USERID", phoneNumber);
                        getIntent().putExtra("LOGIN_PASSWORD", pwd);

                        setResult(RESULT_OK, getIntent());
                        if (storeId != null)
                        {
                            intent.putExtra("tId", storeId);
                        }
                        if (recommend)
                        {
                            intent.putExtra("recommend", true);
                        }
                        startActivity(intent);
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
                        txtHint.setText("驗證碼錯誤");
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

    private void getCode()
    {
//        String message = "驗證碼已發送";
//        Toast.makeText(PhoneVerifyActivity.this, message, Toast.LENGTH_SHORT).show();
//
//        CountDown_OK = "2";
//        txtTime.setVisibility(View.VISIBLE);
//
//        new CountDownTimer(60000 + 100, 1000)
//        {
//
//            @Override
//            public void onTick(long l)
//            {
//                String s = String.valueOf(l / 1000);
//                txtTime.setText("(" + s + ")");
//            }
//
//            @Override
//            public void onFinish()
//            {
//                CountDown_OK = "1";
//                txtTime.setVisibility(View.INVISIBLE);
//            }
//        }.start();
        ApiConnection.userCode(phoneNumber, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (jsonString.equals("0x0200"))
                        {
                            String message = "驗證碼已發送";
                            Toast.makeText(PhoneVerifyActivity.this, message, Toast.LENGTH_SHORT).show();

                            CountDown_OK = "2";
                            txtTime.setVisibility(View.VISIBLE);

                            new CountDownTimer(60000 + 100, 1000)
                            {

                                @Override
                                public void onTick(long l)
                                {
                                    String s = String.valueOf(l / 1000);
                                    txtTime.setText("(" + s + ")");
                                }

                                @Override
                                public void onFinish()
                                {
                                    CountDown_OK = "1";
                                    txtTime.setVisibility(View.INVISIBLE);
                                }
                            }.start();
                        } else if (jsonString.equals("0x0201"))
                        {
                            String message = "帳號錯誤";
                            AppUtility.showMyDialog(PhoneVerifyActivity.this, message, "返回", null, new AppUtility.OnBtnClickListener()
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
                        } else if (jsonString.equals("0x0202"))
                        {
                            String message = "異常錯誤";
                            AppUtility.showMyDialog(PhoneVerifyActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
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

            @Override
            public void onFailure(String message)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        AppUtility.showMyDialog(PhoneVerifyActivity.this, message, "返回", null, new AppUtility.OnBtnClickListener()
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
                });
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
                finish();
                break;
            case R.id.layout2:
                if (CountDown_OK.equals("2"))
                {
                    Toast.makeText(this, "請等待60秒後重發驗證簡訊", Toast.LENGTH_SHORT).show();
                    return;
                }
                getCode();
                break;
        }
    }
}