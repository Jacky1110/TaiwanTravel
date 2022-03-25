package com.jotangi.nickyen.member;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.base.BaseActivity;
import com.jotangi.nickyen.R;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener
{

    private EditText etResetPhoneNumber, etResetPassword, etResetPassword2, etResetCode;
    private Button btnUserCode, btnReset;
    private String account;

    private static String CountDown_OK = "1";
    private static String sDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_forget_passord);

        getWindow().setStatusBarColor(ContextCompat.getColor(ForgetPasswordActivity.this, R.color.typeRed));

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        initView();
    }

    private void initView()
    {
        etResetPhoneNumber = findViewById(R.id.et_phone);
        etResetCode = findViewById(R.id.et_resetCode);
        etResetPassword = findViewById(R.id.et_password);
        etResetPassword2 = findViewById(R.id.et_password2);

        btnUserCode = findViewById(R.id.btn_userCode);
        btnReset = findViewById(R.id.btn_reset);

        btnUserCode.setOnClickListener(this);
        btnReset.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_userCode:
//                做一個60秒防連按發送簡訊
                if (CountDown_OK.equals("2"))
                {
                    Toast.makeText(this, "請於"+sDuration+"秒後重發驗證簡訊", Toast.LENGTH_SHORT).show();
                    return;
                }
                getCode();
                break;
            case R.id.btn_reset:
                codeVerify();
                break;
        }
    }

    private void getCode()
    {
        String account2 = etResetPhoneNumber.getText().toString().trim();
        account = account2;

        if (account2.equals(""))
        {
            String message = "請先輸入10碼手機號碼。";
            makeToastTextAndShow(message, 3500);
            return;
        } else if (account2.length() != 10)
        {
            String message = "輸入格式錯誤,請輸入10碼手機號碼。";
            makeToastTextAndShow(message, 3500);
            return;
        }
        ApiConnection.userCode(account, new ApiConnection.OnConnectResultListener()
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
                            String message = "帳號已存在\n驗證碼已發送";
                            AppUtility.showMyDialog(ForgetPasswordActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
                            {
                                @Override
                                public void onCheck()
                                {
                                    CountDown_OK = "2";
                                    long duration = TimeUnit.MINUTES.toMillis(1);

                                    new CountDownTimer(duration, 1000)
                                    {

                                        @Override
                                        public void onTick(long l)
                                        {
                                            sDuration = String.valueOf(l/1000);
//                                                    = String.format(Locale.ENGLISH,"%02d:%02d"
//                                        , Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(1))
//                                                    ,TimeUnit.MILLISECONDS.toSeconds(1)-
//                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMillis(1)));
                                        }

                                        @Override
                                        public void onFinish()
                                        {
                                            CountDown_OK = "1";
                                        }
                                    }.start();
                                }

                                @Override
                                public void onCancel()
                                {

                                }
                            });
                        } else if (jsonString.equals("0x0201"))
                        {
                            String message = "帳號錯誤";
                            AppUtility.showMyDialog(ForgetPasswordActivity.this, message, "返回", null, new AppUtility.OnBtnClickListener()
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
                            AppUtility.showMyDialog(ForgetPasswordActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
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
                        AppUtility.showMyDialog(ForgetPasswordActivity.this, message, "返回", null, new AppUtility.OnBtnClickListener()
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

    private void codeVerify()
    {
        String code = etResetCode.getText().toString().trim();
        String pw = etResetPassword.getText().toString().trim();
        String pwd = etResetPassword2.getText().toString().trim();

        if (pw.equals("") || pwd.equals(""))
        {
            makeToastTextAndShow("新密碼不得為空", 3500);
            return;
        }
        if (!AppUtility.isPasswordRegex(pw)||!AppUtility.isPasswordRegex(pwd))
        {
            makeToastTextAndShow("密碼或新密碼必須含有英文及數字", 3500);
            return;
        }
        if (!pw.equals(pwd))
        {
            makeToastTextAndShow("確認新密碼是否相符", 3500);
            return;
        }

        if (code.length() == 0)
        {
            String message = "請輸入簡訊驗證碼。";
            makeToastTextAndShow(message, 3500);
        }

        ApiConnection.resetCode(account, pwd, code, new ApiConnection.OnConnectResultListener()
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
                            String message = "密碼重置成功\n請重新登入";
                            AppUtility.showMyDialog(ForgetPasswordActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
                            {
                                @Override
                                public void onCheck()
                                {
                                    Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancel()
                                {

                                }
                            });
                        } else if (jsonString.equals(("0x0201")))
                        {
                            String message = "帳號或驗證碼錯誤";
                            AppUtility.showMyDialog(ForgetPasswordActivity.this, message, "返回", null, new AppUtility.OnBtnClickListener()
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
                        } else
                        {
                            String message = "異常錯誤";
                            AppUtility.showMyDialog(ForgetPasswordActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
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
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                AppUtility.showMyDialog(ForgetPasswordActivity.this, message, "返回", null, new AppUtility.OnBtnClickListener()
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
        });
    }
}