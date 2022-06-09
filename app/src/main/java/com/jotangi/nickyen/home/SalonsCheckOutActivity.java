package com.jotangi.nickyen.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.beautySalons.model.IsBonusStore;
import com.jotangi.nickyen.cost.CostGeneralActivity;
import com.jotangi.nickyen.home.model.CouponListBean;
import com.jotangi.nickyen.merch.model.MerchMemberInfoBean;
import com.jotangi.nickyen.model.MemberInfoBean;
import com.jotangi.nickyen.model.UserBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

public class SalonsCheckOutActivity extends AppCompatActivity implements View.OnClickListener
{
    private String TAG = getClass().getSimpleName() + "TAG";

    private static final int EDIT = 1;

    private ConstraintLayout checkPointLayout;
    private CheckBox cbCreditCard, cbLinePay, cbJKO, cbCash;
    private ImageButton btnBack;
    private Button btnEnter, btnChoice, btnNoChoice, btnCheck, btnNoCheck;
    private TextView txtCost, txtStoreName, txtTicket, txtUse, txtPoint;
    private TextView txtCredit, txtLinePay, txtJKO, txtCash;
    private EditText editAmount;
    private ProgressBar progressBar;

    private String sid;
    private String storeName;
    private String total; //總金額
    private String bookingNo;
    private String designer = "";
    private String service = "";
    private String eat = "";

    private String couponNo = "";//下一頁優惠券返回的
    private String type = "1"; //1.現金 2.刷卡 3.Line Pay
    private String action = "0"; //用來判斷否有選擇優惠券 0.沒有 1.有
    private String str = "0";//顯示點數
    private String ticketCount = "0";//顯示優惠券張數
    private int point = 0; // 要送出的點數
    private String sPoint; //顯示UI的
    private int last; //要送出的最後金額
    private int discountAmount = 0;

    private String page = "";
    private String memberID = ""; //判斷帳號跟店長的訂單是否無誤

    private CouponListBean coupon = new CouponListBean();

    private ArrayList<MerchMemberInfoBean> memberInfoArrayList;
    private ArrayList<CouponListBean> couponList;
    private ArrayList<CouponListBean> filterList;

    private boolean isStatus2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salons_check_out);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        sid = getIntent().getStringExtra("sid");
        storeName = getIntent().getStringExtra("storeName");
        total = getIntent().getStringExtra("total");
        bookingNo = getIntent().getStringExtra("bookingNo");
        designer = getIntent().getStringExtra("designer");
        service = getIntent().getStringExtra("service");
        eat = getIntent().getStringExtra("eat");
        memberID = getIntent().getStringExtra("memberID");
        page = getIntent().getStringExtra("page");

        checkPointLayout = findViewById(R.id.layout3);

        last = Integer.parseInt(total);
//        if (point != 0 && point > Integer.parseInt(total))
//        {
//            point = Integer.parseInt(total);
//        }
        isBonusStore(sid);
//        getPoint();
        getCouponList();
        initView();
    }

    private void isBonusStore(String sid)
    {
        ApiConnection.isBonusStore(sid, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                isStatus2 = true;
                IsBonusStore isBonusStore = new Gson().fromJson(jsonString, IsBonusStore.class);
                if (!AppUtility.isExpire(isBonusStore.getContractStartdate(), false) || AppUtility.isExpire(isBonusStore.getContractEnddate(), false))
                { // 如果未啟用+過期
                    isStatus2 = false;
                    runOnUiThread(() ->
                    {
                        checkPointLayout.setVisibility(View.GONE);
                    });
                } else
                {
                    getPoint();
                }
            }

            @Override
            public void onFailure(String message)
            {
                isStatus2 = false;
                runOnUiThread(() ->
                {
                    checkPointLayout.setVisibility(View.GONE);
                });
            }
        });
    }

    private void initView()
    {
        progressBar = findViewById(R.id.progressBar);

        btnBack = findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);

        btnEnter = findViewById(R.id.btn_enter);
        btnEnter.setOnClickListener(this);

        btnChoice = findViewById(R.id.btn_choice);
        btnChoice.setOnClickListener(this);
        btnNoChoice = findViewById(R.id.btn_no_choice);
        btnNoChoice.setOnClickListener(this);

        btnCheck = findViewById(R.id.btn_check);
        btnCheck.setOnClickListener(this);
        btnNoCheck = findViewById(R.id.btn_no_check);
        btnNoCheck.setOnClickListener(this);
//        isStatus2 = true; //一開始進來是有使用點數

        txtCost = findViewById(R.id.tv_cost);

        txtTicket = findViewById(R.id.tv_ticket);

        txtStoreName = findViewById(R.id.tv_store_name);
        txtStoreName.setText(storeName);

        txtPoint = findViewById(R.id.tv_point);
        txtUse = findViewById(R.id.tv_use);

        cbCreditCard = findViewById(R.id.check_credit_card);
        cbCreditCard.setClickable(false);

        txtCredit = findViewById(R.id.tv_credit);
        String strMsg1 = "<font color=\"#000000\">信用卡一次付清</font><br><font color=\"#7F7F7F\">VISA/JCB/MasterCard</font>";
        txtCredit.setText(Html.fromHtml(strMsg1));

        cbLinePay = findViewById(R.id.check_line_pay);
        cbLinePay.setClickable(false);

        txtLinePay = findViewById(R.id.tv_line);
        String strMsg2 = "<font color=\"#000000\">LINE Pay</font><br><font color=\"#7F7F7F\">可用LINE Points</font>";
        txtLinePay.setText(Html.fromHtml(strMsg2));

        cbJKO = findViewById(R.id.check_jko);
        cbJKO.setClickable(false);

        txtJKO = findViewById(R.id.tv_jko);
        String strMsg3 = "<font color=\"#000000\">街口支付</font><br><font color=\"#7F7F7F\">可用街口幣折抵</font>";
        txtJKO.setText(Html.fromHtml(strMsg3));

        cbCash = findViewById(R.id.check_cash);
        cbCash.setChecked(true);

        txtCash = findViewById(R.id.tv_cash);
        String strMsg4 = "<font color=\"#000000\">現金</font><br><font color=\"#7F7F7F\">請與店家當面點清</font>";
        txtCash.setText(Html.fromHtml(strMsg4));

//        editAmount = findViewById(R.id.editAmount);
//        editAmount.setText(String.valueOf(point));
//
//        TextWatcher textWatcher = new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after)
//            {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//                if (s.toString().isEmpty()){
//                    editAmount.setText("0");
//                    point=0;
//                }
//                else if (Integer.parseInt(s.toString()) > point )
//                {
//                    Toast.makeText(SalonsCheckOutActivity.this, "超出範圍", Toast.LENGTH_SHORT).show();
//                }
//                else if ( Integer.parseInt(s.toString()) > Integer.parseInt(total)){
//                    editAmount.setText("0");
//                    point=0;
//                }
//                Log.d("您好", "onTextChanged1: "+s);
//                Log.d("您好", "onTextChanged2: "+start);
//                Log.d("您好", "onTextChanged3: "+before);
//                Log.d("您好", "onTextChanged4: "+count);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//
//            }
//        };
//        editAmount.addTextChangedListener(textWatcher);

        //寫checkbox單選
//        View.OnClickListener checkBoxListener = null;
//        cbCreditCard.setOnClickListener(checkBoxListener);
//
//        checkBoxListener = new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//            }
//        };

        if (isStatus2)
        {
            String s = String.valueOf(point);
            txtUse.setText("可使用" + s + "點");
            btnCheck.setVisibility(View.VISIBLE);
            btnNoCheck.setVisibility(View.INVISIBLE);
            last = Integer.parseInt(total) - discountAmount - point;
            if (point != 0 && point > Integer.parseInt(total))
            {
                point = Integer.parseInt(total) - discountAmount;
                txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - point)));
            }
            txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - point)));
        } else
        {
            txtUse.setText("您未使用點數");
            point = 0;
            txtPoint.setText(String.valueOf(MemberInfoBean.member_points));
            Log.d(TAG, "txtPoint: " + txtPoint);
            btnCheck.setVisibility(View.INVISIBLE);
            btnNoCheck.setVisibility(View.VISIBLE);
            last = Integer.parseInt(total) - discountAmount - point;
            txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - 0)));
            Log.d("豪豪1", "總金額: " + total);
            Log.d("豪豪1", "最終金額: " + last);
            Log.d("豪豪1", "折扣金額: " + discountAmount);
            Log.d("豪豪1", "點數: " + point);
        }
    }

    private void getPoint()
    {
        ApiConnection.getMerchMemberInfo(new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<MerchMemberInfoBean>>()
                {
                }.getType();
                memberInfoArrayList = new ArrayList<>();
                memberInfoArrayList = new Gson().fromJson(jsonString, type);
                MemberInfoBean.member_points = Integer.parseInt(memberInfoArrayList.get(0).getMemberTotalpoints()) - Integer.parseInt(memberInfoArrayList.get(0).getMemberUsingpoints());
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        str = String.valueOf(MemberInfoBean.member_points);
                        sPoint = str;//顯示未使用點數的全部點數
                        point = Integer.valueOf(str);

                        if (point > Integer.parseInt(total))
                        {
                            point = Integer.parseInt(total);
                        }
                        String s = String.valueOf(point);

                        if (isStatus2)
                        {
                            txtUse.setText("使用" + s + "點");
                            txtPoint.setText(String.valueOf(point));
                            Log.d(TAG, "txtPoint: " + txtPoint);
//                            editAmount.setText(String.valueOf(point));
                            btnCheck.setVisibility(View.VISIBLE);
                            btnNoCheck.setVisibility(View.INVISIBLE);
                            txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - point)));
                        } else
                        {
                            txtUse.setText("您未使用點數");
                            txtPoint.setText(String.valueOf(MemberInfoBean.member_points));
                            point = 0;
//                            editAmount.setText(String.valueOf(point));
                            btnCheck.setVisibility(View.INVISIBLE);
                            btnNoCheck.setVisibility(View.VISIBLE);
                            last = Integer.parseInt(total) - discountAmount - point;
                            txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - 0)));
                        }
//                        txtCost.setText(String.valueOf(Integer.parseInt(total) - point));
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
                        AppUtility.showMyDialog(SalonsCheckOutActivity.this, "點數可能有點問題...，請檢查網路連線或洽服務人員", "確定", null, new AppUtility.OnBtnClickListener()
                        {
                            @Override
                            public void onCheck()
                            {
                                txtUse.setText("使用" + str + "點");
                                txtPoint.setText(str);
                                point = 0;
//                                txtCost.setText(String.valueOf(last));
                            }

                            @Override
                            public void onCancel()
                            {
                                txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - point)));
                            }
                        });
                    }
                });
            }
        });
    }

    private void getCouponList()
    {
        if (action.equals("0"))
        {
            ApiConnection.getMyCouponList(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), "", "0", new ApiConnection.OnConnectResultListener()
            {
                @Override
                public void onSuccess(String jsonString)
                {
                    Type type = new TypeToken<ArrayList<CouponListBean>>()
                    {
                    }.getType();
                    couponList = new ArrayList<>();
                    couponList = new Gson().fromJson(jsonString, type);
                    filterList = new ArrayList<>();
                    for (int i = 0; i < couponList.size(); i++)
                    {
                        CouponListBean coupon = couponList.get(i);
                        if (coupon.getCouponType().equals("1") || coupon.getCouponType().equals("2") || coupon.getCouponType().equals("4") || coupon.getCouponType().equals("5"))
                        {
                            filterList.add(coupon);
                        }
                    }
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            ticketCount = String.valueOf(filterList.size());
                            txtTicket.setText("已有" + ticketCount + "張優惠券");
                            txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - point)));
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
                            ticketCount = "0";
                            txtTicket.setText("已有" + ticketCount + "張優惠券");
                            txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - point)));
                        }
                    });

                }
            });
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
//                finish();
                Intent intent = new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btn_choice:
            case R.id.btn_no_choice:
                Intent myDiscountIntent = new Intent(this, MyDiscountNew2Activity.class);
                myDiscountIntent.putExtra("discount", "1");
                myDiscountIntent.putExtra("total", total);
                startActivityForResult(myDiscountIntent, EDIT);
                break;
            case R.id.btn_check: //按下check鈕表示使用點數
                txtUse.setText("您未使用點數");
                txtPoint.setText(String.valueOf(MemberInfoBean.member_points));
                btnCheck.setVisibility(View.INVISIBLE);
                btnNoCheck.setVisibility(View.VISIBLE);
                point = 0;
                last = Integer.parseInt(total) - discountAmount - point;
                txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - 0)));
                Log.d("豪豪1", "總金額: " + total);
                Log.d("豪豪1", "最終金額: " + last);
                Log.d("豪豪1", "折扣金額: " + discountAmount);
                Log.d("豪豪1", "點數: " + point);
                isStatus2 = false;
                break;
            case R.id.btn_no_check:
                btnCheck.setVisibility(View.VISIBLE);
                btnNoCheck.setVisibility(View.INVISIBLE);
                last = Integer.parseInt(total) - discountAmount - point;
                point = Integer.parseInt(sPoint);
                if (point != 0 && point > Integer.parseInt(total))
                {
                    point = Integer.parseInt(total) - discountAmount;
                    txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - point)));
                }
                String s = String.valueOf(point);
                txtUse.setText("使用" + s + "點");
                txtPoint.setText(String.valueOf(point));
                txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - point)));
                Log.d("豪豪2", "總金額: " + total);
                Log.d("豪豪2", "最終金額: " + last);
                Log.d("豪豪2", "折扣金額: " + discountAmount);
                Log.d("豪豪2", "點數: " + point);
                isStatus2 = true;
                break;
            case R.id.btn_enter:
                Log.d("豪豪", "sid: " + sid);
                Log.d("豪豪", "couponNo: " + couponNo);
                Log.d("豪豪", "discountAmount: " + String.valueOf(discountAmount));
                Log.d("豪豪", "total: " + total);
                Log.d("豪豪", "point: " + point);
                Log.d("豪豪", "type: " + type);
                Log.d("豪豪", "bookingNo: " + bookingNo);
                last = Integer.parseInt(total) - discountAmount - point;
                Log.d("豪豪", "last: " + last);
                if (cbCash.isChecked() == false)
                {
                    Toast.makeText(this, "請確認付款方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (memberID != null && !AppUtility.DecryptAES2(UserBean.member_id).equals(memberID))
                {
                    Toast.makeText(this, "請確認此筆訂單是否為您所預約", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (page != null && page.equals("salon"))
                {
                    memberAddOrder(sid, couponNo, String.valueOf(discountAmount), total, String.valueOf(point), type, bookingNo, String.valueOf(last));
                } else if (page != null && page.equals("industry"))
                {
                    memberAddClassOrder(sid, couponNo, String.valueOf(discountAmount), total, String.valueOf(point), type, bookingNo, String.valueOf(last));
                }
//                else if (page == null && eat != null)
                else
                {
                    memberAddOrder(sid, couponNo, String.valueOf(discountAmount), total, String.valueOf(point), type, bookingNo, String.valueOf(last));
                }
                break;
        }

    }

    // 美容美髮核銷
    private void memberAddOrder(String sid, String couponNo, String discountAmount, String total, String point, String type, String bookingNo, String last)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.memberAddOrder(sid, couponNo, discountAmount, point, total, type, last, bookingNo, new ApiConnection.OnConnectResultListener()
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
                        sendIII();
                        AppUtility.showMyDialog(SalonsCheckOutActivity.this, "謝謝惠顧！您可點選交易明細確認詳細內容。", "交易明細", "回到首頁", new AppUtility.OnBtnClickListener()
                                {
                                    @Override
                                    public void onCheck()
                                    {
                                        Intent intent = new Intent(SalonsCheckOutActivity.this, CostGeneralActivity.class);
//                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        if (eat != null && eat.equals("1"))
//                                        {
//                                            intent.putExtra("sid", "2");
//                                            intent.putExtra("storeName", storeName);
//                                        } else
//                                        {
//                                            intent.putExtra("sid", "1");
//                                            intent.putExtra("storeName", storeName);
//                                        }
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancel()
                                    {
                                        Intent intent = new Intent(SalonsCheckOutActivity.this, MainActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                        );
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
                        AppUtility.showMyDialog(SalonsCheckOutActivity.this, "選定之優惠券非本店家使用，請重新選擇。", "確認", null, new AppUtility.OnBtnClickListener()
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

    // 產業體驗核銷
    private void memberAddClassOrder(String sid, String couponNo, String discountAmount, String total, String point, String type, String bookingNo, String last)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.memberAddClassOrder(sid, couponNo, discountAmount, point, total, type, last, bookingNo, new ApiConnection.OnConnectResultListener()
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
//                        sendIII();
                        AppUtility.showMyDialog(SalonsCheckOutActivity.this, "謝謝惠顧！", "確定", null, new AppUtility.OnBtnClickListener()
                                {
                                    @Override
                                    public void onCheck()
                                    {
                                        Intent intent = new Intent(SalonsCheckOutActivity.this, MainActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancel()
                                    {
                                    }
                                }
                        );
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
                        AppUtility.showMyDialog(SalonsCheckOutActivity.this, "連線有誤，請重新！", "確認", null, new AppUtility.OnBtnClickListener()
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

    private void sendIII()
    {
        String payType = "";
        switch (type)
        {
            case "1":
                payType = "現金";
                break;
            case "2":
                payType = "刷卡";
                break;
            case "3":
                payType = "Line Pay";
                break;
        }
        String couponName = "無使用";

        if (coupon.getCouponName() != null)
        {
            couponName = coupon.getCouponName();
        }
        String[] array = service.split(",|///");
        String sService2 = "";
        for (int i = 0; i < array.length; i = i + 2)
        {
            sService2 += array[i] + "加";
        }
        sService2 = Optional.ofNullable(sService2)
                .filter(s -> s.length() != 0)
                .map(s -> s.substring(0, s.length() - 1))
                .orElse(sService2);

        ApiConnection.getIIIAdd9(couponName, service.replace(",", ""), payType, String.valueOf(point)
                , sService2, String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000), AppUtility.DecryptAES2(UserBean.member_id), AppUtility.getIPAddress(this), storeName, total, designer
                , new ApiConnection.OnConnectResultListener()
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case EDIT:
//                Toast.makeText(getActivity(), data.getExtras().getString("name"), Toast.LENGTH_LONG).show();
                if (data != null)
                {
                    couponNo = data.getExtras().getString("dis");
                    txtTicket.setText(data.getExtras().getString("name"));
                    coupon = new Gson().fromJson(data.getExtras().getString("coupon"), CouponListBean.class);
                    Log.d("豪豪", "onActivityResult: " + coupon);
                    action = "1";
                    //計算規則
                    //總數-折扣金額-點數
                    if (coupon.getCouponDiscount().equals("1"))
                    {
                        discountAmount = Integer.parseInt(coupon.getDiscountAmount());
                        if (point > Integer.parseInt(total)) //點數大於總額防呆
                        {
                            point = Integer.parseInt(total) - discountAmount;
                            last = 0;
                        } else
                        {
                            last = Integer.parseInt(total) - discountAmount - point;
                        }
                    }
                    //折扣%數
                    if (coupon.getCouponDiscount().equals("2"))
                    {
                        int c = Integer.parseInt(coupon.getDiscountAmount());
                        double d1 = (double) c / 100;
                        double d2 = (Integer.parseInt(total) * d1);
                        discountAmount = Math.toIntExact(Math.round(d2));//四捨五入最終折扣金額

                        if (point > Integer.parseInt(total)) //點數大於總額防呆
                        {
                            point = Integer.parseInt(total) - discountAmount;

                        }
                        last = Integer.parseInt(total) - discountAmount - point;
                    }
                    if (point == 0)
                    {
                        txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - 0)));
                    } else
                    {
                        point = Integer.parseInt(total) - discountAmount;
                        txtCost.setText(AppUtility.strAddComma(String.valueOf(Integer.parseInt(total) - discountAmount - point)));
                    }
                    Log.d("豪豪", "總金額: " + total);
                    Log.d("豪豪", "最終金額: " + last);
                    Log.d("豪豪", "折扣金額: " + discountAmount);
                    Log.d("豪豪", "點數: " + point);
                }
        }
    }
}