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

public class SalonsCheckOutActivity2 extends AppCompatActivity implements View.OnClickListener {

    private static final int EDIT = 1;

    private ConstraintLayout checkPointLayout;
    private CheckBox cbCreditCard, cbLinePay, cbJKO, cbCash;
    private ImageButton btnBack;
    private Button btnEnter, btnChoice, btnNoChoice;
    private TextView txtCost, txtStoreName, txtTicket;
    //            , txtUse, txtPoint;
    private TextView txtCredit, txtLinePay, txtJKO, txtCash;
    private EditText editAmount;
    private ProgressBar progressBar;

    private String sid;
    private String storeName;
    private int total; // 訂單金額
    private String bookingNo;
    private String designer = "";
    private String service = "";
    private String eat = "";

    private String couponNo = ""; // 選擇優惠券返回的優惠券編號
    private String type = "1"; // 1.現金 2.刷卡 3.Line Pay
    private String isUsingDiscount = "0"; //用來判斷否有選擇優惠券 0.沒有 1.有

    private String ticketCount = "0";//顯示優惠券張數
    private boolean isStatus; // 初始化是否直接折扣點數狀態

    // 計算類變數也等於是要送到Api的
    private int point; // 點數
    private int discountAmount; //優惠券折價金額
    private int last; //要送出的最後金額

    private String page = ""; // 判斷是哪一頁進來，有不同的結帳Api
    private String memberID = ""; // 判斷會員帳號跟店長的訂單是否無誤

    private CouponListBean coupon = new CouponListBean();
    private ArrayList<MerchMemberInfoBean> memberInfoArrayList;
    private ArrayList<CouponListBean> couponList;
    private ArrayList<CouponListBean> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salons_check_out);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        getData();
        initView();
        isBonusStore(sid); // 判斷是否能使用紅利點數的店家 > 獲取點數
        getCouponList();
        editTxtChange();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);

        btnBack = findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);
        btnEnter = findViewById(R.id.btn_enter);
        btnEnter.setOnClickListener(this);
        btnChoice = findViewById(R.id.btn_choice);
        btnChoice.setOnClickListener(this);
        btnNoChoice = findViewById(R.id.btn_no_choice);
        btnNoChoice.setOnClickListener(this);

        txtCost = findViewById(R.id.tv_cost);
        txtTicket = findViewById(R.id.tv_ticket);
        txtStoreName = findViewById(R.id.tv_store_name);
        txtStoreName.setText(storeName);
//        txtPoint = findViewById(R.id.tv_point);
//        txtUse = findViewById(R.id.tv_use);

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

        checkPointLayout = findViewById(R.id.layout3);

        editAmount = findViewById(R.id.editAmount);

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
    }

    private void editTxtChange() {

        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Integer.parseInt(s.toString()) > MemberInfoBean.member_points) { // 捕捉輸入點數大於自身點數 || 輸入點數大於訂單金額
                        editAmount.setText(MemberInfoBean.member_points);
                    } else if (Integer.parseInt(s.toString()) > total - discountAmount) { // 捕捉輸入點數大於訂單金額
                        editAmount.setText(String.valueOf(total - discountAmount));
                    }
                } catch (Exception e) {
                    editAmount.setText("0");
                }

                //判定首字為0消去
                if (s.toString().length() >= 2 && s.toString().indexOf("0") == 0) {
                    editAmount.setText(s.toString().substring(1, s.toString().length()));
                }

                if (editAmount.getText().length() == 0) {
                    editAmount.setText("0");
                }
                editAmount.setSelection(editAmount.getText().length());
                txtCost.setText(AppUtility.strAddComma(String.valueOf(total - discountAmount - Integer.parseInt(editAmount.getText().toString().trim()))));
                Log.d("嘿嘿", "afterTextChanged: " + total + "\n" + editAmount.getText().toString().trim());
            }
        });
    }

    private void getData() {
        sid = getIntent().getStringExtra("sid");
        storeName = getIntent().getStringExtra("storeName");
        total = Integer.parseInt(getIntent().getStringExtra("total"));
        bookingNo = getIntent().getStringExtra("bookingNo");
        designer = getIntent().getStringExtra("designer");
        service = getIntent().getStringExtra("service");
        eat = getIntent().getStringExtra("eat");
        memberID = getIntent().getStringExtra("memberID");
        page = getIntent().getStringExtra("page");

        isStatus = true;
    }


    private void isBonusStore(String sid) {
        ApiConnection.isBonusStore(sid, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                IsBonusStore isBonusStore = new Gson().fromJson(jsonString, IsBonusStore.class);
                if (!AppUtility.isExpire(isBonusStore.getContractStartdate(), false)
                        || AppUtility.isExpire(isBonusStore.getContractEnddate(), false)) { // 如果未啟用+過期
                    runOnUiThread(() ->
                    {
                        checkPointLayout.setVisibility(View.GONE);
                    });
                } else {
                    getPoint();
                }
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() ->
                {
                    checkPointLayout.setVisibility(View.GONE);
                });
            }
        });
    }

    private void getPoint() {
        ApiConnection.getMerchMemberInfo(new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                Type type = new TypeToken<ArrayList<MerchMemberInfoBean>>() {
                }.getType();
                memberInfoArrayList = new ArrayList<>();
                memberInfoArrayList = new Gson().fromJson(jsonString, type);
                MemberInfoBean.member_points = Integer.parseInt(memberInfoArrayList.get(0).getMemberTotalpoints()) - Integer.parseInt(memberInfoArrayList.get(0).getMemberUsingpoints());

                runOnUiThread(() ->
                {
                    if (isStatus) {
                        point = MemberInfoBean.member_points;
                        Log.d("嘿嘿", "監測1: " + point);
                        txtCost.setText(AppUtility.strAddComma(calculationAmount()));
                        Log.d("嘿嘿", "監測2: " + point);
                        editAmount.setText(String.valueOf(point));
                        Log.d("嘿嘿", "onSuccess: " + point);
                        Log.d("嘿嘿", "onSuccess: " + editAmount.getText());
                    } else {
                        txtCost.setText(AppUtility.strAddComma(calculationAmount()));
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> AppUtility.showMyDialog(SalonsCheckOutActivity2.this, "點數可能有點問題...，請檢查網路連線或洽服務人員", "確定", null, new AppUtility.OnBtnClickListener() {
                    @Override
                    public void onCheck() {
                        point = 0;

                        txtCost.setText(AppUtility.strAddComma(calculationAmount()));
                    }

                    @Override
                    public void onCancel() {
                    }
                }));
            }
        });
    }

    private void getCouponList() {
        if (isUsingDiscount.equals("0")) {
            ApiConnection.getMyCouponList(AppUtility.DecryptAES2(UserBean.member_id), AppUtility.DecryptAES2(UserBean.member_pwd), "", "0", new ApiConnection.OnConnectResultListener() {
                @Override
                public void onSuccess(String jsonString) {
                    Type type = new TypeToken<ArrayList<CouponListBean>>() {
                    }.getType();
                    couponList = new ArrayList<>();
                    couponList = new Gson().fromJson(jsonString, type);
                    filterList = new ArrayList<>();
                    for (int i = 0; i < couponList.size(); i++) {
                        CouponListBean coupon = couponList.get(i);
                        if (coupon.getCouponType().equals("1") || coupon.getCouponType().equals("2") || coupon.getCouponType().equals("4") || coupon.getCouponType().equals("5")) {
                            filterList.add(coupon);
                        }
                    }
                    runOnUiThread(() -> {
                        ticketCount = String.valueOf(filterList.size());
                        txtTicket.setText("已有" + ticketCount + "張優惠券");
                    });
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> {
                        ticketCount = "0";
                        txtTicket.setText("已有" + ticketCount + "張優惠券");
                    });
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_go_back:
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
            case R.id.btn_enter:

                Log.d("嘿嘿", "sid: " + sid);
                Log.d("嘿嘿", "couponNo: " + couponNo);
                Log.d("嘿嘿", "discountAmount: " + String.valueOf(discountAmount));
                Log.d("嘿嘿", "total: " + total);
                Log.d("嘿嘿", "point: " + point);
                Log.d("嘿嘿", "type: " + type);
                Log.d("嘿嘿", "bookingNo: " + bookingNo);
                Log.d("嘿嘿", "last: " + last);

                // 最終在複查計算
                if (editAmount.getText().toString().trim().isEmpty() || editAmount.getText().toString().trim().equals("0")) {
                    point = 0;
                } else {
                    point = Integer.parseInt(editAmount.getText().toString().trim());
                }
                calculationAmount();
                Toast.makeText(this, "訂單金額: " + total + " 優惠券: " + discountAmount + " 點數: " + point + " 最終金額: " + last, Toast.LENGTH_SHORT).show();

                if (cbCash.isChecked() == false) {
                    Toast.makeText(this, "請確認付款方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (memberID != null && !AppUtility.DecryptAES2(UserBean.member_id).equals(memberID)) {
                    Toast.makeText(this, "請確認此筆訂單是否為您所預約", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (page != null && page.equals("salon")) {
                    memberAddOrder(sid, couponNo, String.valueOf(discountAmount), String.valueOf(total), String.valueOf(point), type, bookingNo, String.valueOf(last));
                } else if (page != null && page.equals("industry")) {
                    memberAddClassOrder(sid, couponNo, String.valueOf(discountAmount), String.valueOf(total), String.valueOf(point), type, bookingNo, String.valueOf(last));
                }
//                else if (page == null && eat != null)
                else {
                    memberAddOrder(sid, couponNo, String.valueOf(discountAmount), String.valueOf(total), String.valueOf(point), type, bookingNo, String.valueOf(last));
                }
                break;
        }
    }

    // 美容美髮核銷
    private void memberAddOrder(String sid, String couponNo, String discountAmount, String total, String point, String type, String bookingNo, String last) {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.memberAddOrder(sid, couponNo, discountAmount, point, total, type, last, bookingNo, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(final String jsonString) {
                runOnUiThread(() ->
                {
                    progressBar.setVisibility(View.GONE);
                    sendIII();
                    AppUtility.showMyDialog(SalonsCheckOutActivity2.this, "謝謝惠顧！您可點選交易明細確認詳細內容。", "交易明細", "回到首頁", new AppUtility.OnBtnClickListener() {
                                @Override
                                public void onCheck() {
                                    Intent intent = new Intent(SalonsCheckOutActivity2.this, CostGeneralActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancel() {
                                    Intent intent = new Intent(SalonsCheckOutActivity2.this, MainActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                    );
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() ->
                {
                    progressBar.setVisibility(View.GONE);
                    AppUtility.showMyDialog(SalonsCheckOutActivity2.this, "選定之優惠券非本店家使用，請重新選擇。", "確認", null, new AppUtility.OnBtnClickListener() {
                        @Override
                        public void onCheck() {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                });
            }
        });
    }

    // 產業體驗核銷
    private void memberAddClassOrder(String sid, String couponNo, String discountAmount, String total, String point, String type, String bookingNo, String last) {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.memberAddClassOrder(sid, couponNo, discountAmount, point, total, type, last, bookingNo, new ApiConnection.OnConnectResultListener() {
            @Override
            public void onSuccess(String jsonString) {
                runOnUiThread(() ->
                {
                    progressBar.setVisibility(View.GONE);
                    AppUtility.showMyDialog(SalonsCheckOutActivity2.this, "謝謝惠顧！", "確定", null, new AppUtility.OnBtnClickListener() {
                                @Override
                                public void onCheck() {
                                    Intent intent = new Intent(SalonsCheckOutActivity2.this, MainActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancel() {
                                }
                            }
                    );
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() ->
                {
                    progressBar.setVisibility(View.GONE);
                    AppUtility.showMyDialog(SalonsCheckOutActivity2.this, "連線有誤，請重新！", "確認", null, new AppUtility.OnBtnClickListener() {
                        @Override
                        public void onCheck() {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT:
                if (data != null) {
                    couponNo = data.getExtras().getString("dis");
                    txtTicket.setText(data.getExtras().getString("name"));
                    coupon = new Gson().fromJson(data.getExtras().getString("coupon"), CouponListBean.class);
                    Log.d("嘿嘿", "onActivityResult: " + coupon);
                    isUsingDiscount = "1";

                    if (coupon.getCouponDiscount().equals("1")) { // 直接折扣金額
                        discountAmount = Integer.parseInt(coupon.getDiscountAmount());
                    }
                    if (coupon.getCouponDiscount().equals("2")) { // 折扣%數
                        int c = Integer.parseInt(coupon.getDiscountAmount());
                        double d1 = (double) c / 100;
                        double d2 = (total * d1);
                        discountAmount = Math.toIntExact(Math.round(d2));//四捨五入最終折扣金額
                    }
                    point = MemberInfoBean.member_points;
                }
                Log.d("嘿嘿", "總$: " + total + " 折券: " + discountAmount + " 點: " + point + " 最終金額: " + last);
                txtCost.setText(AppUtility.strAddComma(calculationAmount()));
        }
    }

    /**
     * 計算規則 總金額-優惠券-點數
     *
     * @return
     */

    private String calculationAmount() {
        if (point > total - discountAmount) { // 點數大於總金額-優惠券
            point = total - discountAmount;
        }
        last = total - discountAmount - point;
        editAmount.setText(String.valueOf(point));
        Log.d("嘿嘿", "calculationAmount 總$: " + total + " 折券: " + discountAmount + " 點: " + point + " 最終金額: " + last);
        return String.valueOf(last);
    }

    private void sendIII() {
        String payType = "";
        switch (type) {
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

        if (coupon.getCouponName() != null) {
            couponName = coupon.getCouponName();
        }
        String[] array = service.split(",|///");
        String sService2 = "";
        for (int i = 0; i < array.length; i = i + 2) {
            sService2 += array[i] + "加";
        }
        sService2 = Optional.ofNullable(sService2)
                .filter(s -> s.length() != 0)
                .map(s -> s.substring(0, s.length() - 1))
                .orElse(sService2);

        ApiConnection.getIIIAdd9(couponName, service.replace(",", ""), payType, String.valueOf(point)
                , sService2, String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000), AppUtility.DecryptAES2(UserBean.member_id), AppUtility.getIPAddress(this), storeName, String.valueOf(total), designer
                , new ApiConnection.OnConnectResultListener() {
                    @Override
                    public void onSuccess(String jsonString) {
                    }

                    @Override
                    public void onFailure(String message) {
                    }
                });
    }
}