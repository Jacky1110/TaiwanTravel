package com.jotangi.nickyen.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.base.BaseActivity;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.R;

public class MemberModifyActivity extends BaseActivity implements View.OnClickListener
{

    EditText etName, etPhone, etEmail, etAdress, etBirthday, etOldPassword, etPassword, etPassword2;
    Button btnSend, btnSend2;//資料按鈕 密碼更改
    AppCompatButton btnMale, btnFemale;
    ProgressBar progressBar;

    private String account = MemberInfoBean.decryptId;
    private String pwd = MemberInfoBean.decryptPwd;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_member_modify);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MemberModifyActivity.this,MemberInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        initView();
    }

    private void initView()
    {
        etName = findViewById(R.id.et_modifyName);
//        etPhone = findViewById(R.id.et_modifyPhone);
        etEmail = findViewById(R.id.et_modifyEmail);
        etAdress = findViewById(R.id.et_modifyAddress);
        etBirthday = findViewById(R.id.et_ModifyBirthday);
        etOldPassword = findViewById(R.id.et_ModifyPassword); //舊密碼
        etPassword = findViewById(R.id.et_ModifyPassword2); //第一次輸入密碼
        etPassword2 = findViewById(R.id.et_ModifyPassword3); //第二次輸入密碼

        if (MemberInfoBean.decryptName != "null")
            etName.setText(MemberInfoBean.decryptName);

        if (MemberInfoBean.decryptEmail != "null")
            etEmail.setText(MemberInfoBean.decryptEmail);

        if (MemberInfoBean.decryptAddress != "null")
            etAdress.setText(MemberInfoBean.decryptAddress);

        if (MemberInfoBean.decryptBirthday != "null")
            etBirthday.setText(MemberInfoBean.decryptBirthday);

        btnSend = findViewById(R.id.btn_send); //會員資料修改提交
        btnSend2 = findViewById(R.id.btn_send2); //密碼確認修改提交
        btnMale = findViewById(R.id.btn_male);
        btnFemale = findViewById(R.id.btn_female);

        if (MemberInfoBean.member_gender.length() == 0 || MemberInfoBean.member_gender.equals("1"))
        {
            btnMale.setSelected(true);
            btnFemale.setSelected(false);
        } else if (MemberInfoBean.member_gender.equals("2"))
        {
            btnFemale.setSelected(true);
            btnMale.setSelected(false);
        }
        btnMale.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnMale.setSelected(true);
                btnFemale.setSelected(false);
            }
        });
        btnFemale.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnFemale.setSelected(true);
                btnMale.setSelected(false);
            }
        });

        btnSend.setOnClickListener(this);
        btnSend2.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_send:
                sendMemberInfo();
                break;
            case R.id.btn_send2:
                sendChangePwd();
        }
    }

    private void sendMemberInfo()
    {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String birthday = etBirthday.getText().toString().trim();
        String address = etAdress.getText().toString().trim();
//        String phone = etPhone.getText().toString().trim();

        if (etName.length() != 0)
        {
            MemberInfoBean.decryptName = name;
        } else
        {
            name = MemberInfoBean.decryptName;
        }
        if (!AppUtility.isNameRegex(name))
        {
            makeToastTextAndShow("名字不得超過30個字元或含有空白", 3500);
            return;
        }
        if (etEmail.length() != 0)
        {
            MemberInfoBean.decryptEmail = email;
        } else
        {
            email = MemberInfoBean.decryptEmail;
        }

        if (!AppUtility.isMailRegex(email))
        {
            makeToastTextAndShow("信箱格式錯誤", 3500);
            return;
        }

        if (etBirthday.length() != 0)
        {
            MemberInfoBean.decryptBirthday = birthday;
        } else
        {
            birthday = MemberInfoBean.decryptBirthday;
        }

        if (etAdress.length() != 0)
        {
            MemberInfoBean.decryptAddress = address;
        } else
        {
            address = MemberInfoBean.decryptAddress;
        }

//        if (etPhone.length() != 0)
//        {
//            MemberInfoBean.member_phone = phone;
//        } else
//        {
//            phone = MemberInfoBean.member_phone;
//        }

        if (btnMale.isSelected())
        {
            gender = "1";
            MemberInfoBean.member_gender = gender;
        } else if (btnFemale.isSelected())
        {
            gender = "2";
            MemberInfoBean.member_gender = gender;
        }

        if (name.equals(""))
        {
            makeToastTextAndShow("名字不得為空", 3500);
            return;
        }

        if (gender == null)
        {
            gender = "1";
        }

        if (email == null)
        {
            email = "";
        }

        if (birthday == null)
        {
            birthday = "";
        }
        if (address == null)
        {
            address = "";
        }
        if (email.length() > 30)
        {
            makeToastTextAndShow("信箱格式錯誤", 3500);
            return;
        }
        if (address.length() > 30)
        {
            makeToastTextAndShow("地址格式錯誤", 3500);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.userEdit(account, pwd, name, gender, email, birthday, address, new ApiConnection.OnConnectResultListener()
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
                        if (jsonString.equals("0x0200"))
                        {
                            String message = "會員資料已成功更新";
                            AppUtility.showMyDialog(MemberModifyActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
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
                        } else if (jsonString.equals("0x0201"))
                        {
                            String message = "帳號或密碼錯誤";
                            AppUtility.showMyDialog(MemberModifyActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
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
                            AppUtility.showMyDialog(MemberModifyActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
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
                        progressBar.setVisibility(View.GONE);
                        AppUtility.showMyDialog(MemberModifyActivity.this, message, "返回", null, new AppUtility.OnBtnClickListener()
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

    private void sendChangePwd()
    {
        final String pwdOld = etOldPassword.getText().toString().trim();
        final String pw = etPassword.getText().toString().trim();
        final String pwdNew = etPassword2.getText().toString().trim();

        if (pwdOld.length() == 0)
        {
            makeToastTextAndShow("請輸入舊密碼", 3500);
            return;
        }

        if (pw.equals("") || pwdNew.equals(""))
        {
            makeToastTextAndShow("新密碼不得為空", 3500);
            return;
        }
        if (!pwdNew.equals(pw))
        {
            makeToastTextAndShow("確認新密碼是否相符", 3500);
            return;
        }

        if (!AppUtility.isPasswordRegex(pwdOld))
        {
            makeToastTextAndShow("密碼或新密碼必須含有英文及數字", 3500);
            return;
        }

        if (!AppUtility.isPasswordRegex(pw))
        {
            makeToastTextAndShow("密碼或新密碼必須含有英文及數字", 3500);
            return;
        }

        if (!AppUtility.isPasswordRegex(pwdNew))
        {
            makeToastTextAndShow("密碼或新密碼必須含有英文及數字", 3500);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.userChangePwd(account, pwdOld, pwdNew, new ApiConnection.OnConnectResultListener()
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
                        if (jsonString.equals("0x0200"))
                        {
                            String message = "密碼已成功變更";
                            AppUtility.showMyDialog(MemberModifyActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
                            {
                                @Override
                                public void onCheck()
                                {

                                    Intent intent = new Intent(MemberModifyActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancel()
                                {

                                }
                            });
                        } else if (jsonString.equals("0x0201"))
                        {
                            String message = "帳號錯誤";
                            AppUtility.showMyDialog(MemberModifyActivity.this, message, "返回", null, new AppUtility.OnBtnClickListener()
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
                            AppUtility.showMyDialog(MemberModifyActivity.this, message, "確認", null, new AppUtility.OnBtnClickListener()
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
                        progressBar.setVisibility(View.GONE);
                        AppUtility.showMyDialog(MemberModifyActivity.this, message, "返回", null, new AppUtility.OnBtnClickListener()
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
}