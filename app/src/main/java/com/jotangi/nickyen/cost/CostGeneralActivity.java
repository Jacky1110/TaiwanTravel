package com.jotangi.nickyen.cost;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.cost.adapter.MemberOrderFetchAdapter;
import com.jotangi.nickyen.home.SalonsCheckOutActivity;
import com.jotangi.nickyen.member.model.OrderListBean;
import com.jotangi.nickyen.model.ShopBean;
import com.jotangi.nickyen.model.UserBean;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.jotangi.nickyen.api.ApiConnection.mMyTrustManager;

public class CostGeneralActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = CostGeneralActivity.class.getSimpleName();
    static String startDate, endDate;
    //UI
    private ProgressBar progressBar;
    private ImageButton btnGoBack;
    private ConstraintLayout btnDate;
    private TextView txtStartDate, txtEndDate, txtAmount, txtCount, txt1, txtNoData;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    private MemberOrderFetchAdapter memberOrderFetchAdapter;

    private DatePickerDialog datePickerDialog;
    private Button btnStartDate, btnEndDate;
    private RadioButton btnThisMonth, btnAll;

    private String datePicker1, datePicker2;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private PopupWindow searchWindow;

    private ArrayList<OrderListBean> orderListBeanArrayList; //model跟info的一樣
    private ArrayList<ShopBean> shopBeansList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_cost_general);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.typeRed));

        initView();
    }

    private void initView()
    {
        progressBar = findViewById(R.id.progressBar);

        btnGoBack = findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);

        btnDate = findViewById(R.id.btn_date);
        btnDate.setOnClickListener(this);

        txtStartDate = findViewById(R.id.txt_start_date);
        txtEndDate = findViewById(R.id.txt_end_date);
        txtAmount = findViewById(R.id.tv_amount);
        txtCount = findViewById(R.id.tv_count);
        txt1 = findViewById(R.id.text);
        txtNoData = findViewById(R.id.tv_noData);
        recyclerView = findViewById(R.id.recycler);

        initThisMonth();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        getOrderList(startDate, endDate);
    }

    private void getOrderList(String startDate, String endDate)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.getMemberOrderList(startDate, endDate, new ApiConnection.OnConnectResultListener()
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

                        Type type = new TypeToken<ArrayList<OrderListBean>>()
                        {
                        }.getType();
                        orderListBeanArrayList = new ArrayList<>();
                        orderListBeanArrayList = new Gson().fromJson(jsonString, type);
                        txtNoData.setVisibility(View.GONE);

                        orderListBeanArrayList.stream().sorted(Comparator.comparing(OrderListBean::getOrderDate));
                        try
                        {
                            CompareTo(orderListBeanArrayList);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
//                        layoutViews(oderListBeanArrayList);
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
                        progressBar.setVisibility(View.INVISIBLE);
                        txtNoData.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(null);
                    }
                });
            }
        });
    }

    //比對storeInfo、orderList
    private void CompareTo(ArrayList<OrderListBean> orderListBeanArrayList)
    {
        for (int i = 0; i < orderListBeanArrayList.size(); i++)
        {
            String url = ApiConstant.API_URL + ApiConstant.StoreInfo;
            FormBody formBody = new FormBody
                    .Builder()
                    .add("member_id", AppUtility.DecryptAES2(UserBean.member_id))
                    .add("member_pwd", AppUtility.DecryptAES2(UserBean.member_pwd))
                    .add("sid", orderListBeanArrayList.get(i).getStoreId())
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(ApiConnection.createSSLSocketFactory(), mMyTrustManager).build();
            if (client == null)
                client = new OkHttpClient();

            client.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException
                {
                    String json = response.body().string();
                    try
                    {
                        JSONArray jsonArray = new JSONArray(json);
                        ArrayList<JSONObject> objectLists = new ArrayList<>();
                        ShopBean shopBean = new ShopBean();

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
//                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            objectLists.add((JSONObject) jsonArray.get(i));
                            shopBean.setSid(objectLists.get(i).getString("sid"));
                            shopBean.setStoreName(objectLists.get(i).getString("store_name"));
                            shopBeansList.add(shopBean);
                            Log.d(TAG, "onResponse: " + objectLists.get(i).getString("sid") + "/" + objectLists.get(i).getString("store_name"));
                        }

                        for (int j = 0; j < shopBeansList.size(); j++)
                        {
                            for (int k = 0; k < orderListBeanArrayList.size(); k++)
                            {
                                if (shopBeansList.get(j).getSid().equals(orderListBeanArrayList.get(k).getStoreId()))
                                {
                                    orderListBeanArrayList.get(k).setStoreName(shopBeansList.get(j).getStoreName());
                                }
                            }
                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            layoutViews(orderListBeanArrayList);
                        }
                    });
                }

                @Override
                public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e)
                {
                    txtNoData.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void layoutViews(ArrayList<OrderListBean> orderBeanArrayList)
    {
        mManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mManager);
        memberOrderFetchAdapter = new MemberOrderFetchAdapter(orderBeanArrayList, this);
        recyclerView.setAdapter(memberOrderFetchAdapter);
//        memberOrderFetchAdapter.setOnItemClickListener(new OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position)
//            {
//
//            }
//        });

        int total = 0;

        for (int i = 0; i < orderBeanArrayList.size(); i++)
        {
            total += Integer.parseInt(orderBeanArrayList.get(i).getOrderPay());
        }

        txtAmount.setText(AppUtility.strAddComma(String.valueOf(total)));
        txtCount.setText(String.valueOf(orderBeanArrayList.size()));
    }

    private void showSearchWindow()
    {
        if (searchWindow != null && searchWindow.isShowing())
        {
            return;
        }
        final View popupWindowView = View.inflate(this, R.layout.dialog_merch_serach, null);
        searchWindow = AppUtility.buildPopupWindow(popupWindowView, false);
        AppUtility.setWindowAlpha(getWindow(), 0.5f);
        View anchorView = findViewById(R.id.popup_show_as_view);
        searchWindow.showAsDropDown(anchorView, 0, 0);

        // 預約日期
        btnStartDate = popupWindowView.findViewById(R.id.btn_start_date);
        btnStartDate.setText("起始日期");
        btnStartDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDatePicker(view, String.valueOf(btnStartDate.getText()));
            }
        });
        btnEndDate = popupWindowView.findViewById(R.id.btn_end_date);
        btnEndDate.setText("結束日期");
        btnEndDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDatePicker2(view, String.valueOf(btnEndDate.getText()));
            }
        });

        Button btnConfirm = popupWindowView.findViewById(R.id.btn_confirm);
        Button btnCancel = popupWindowView.findViewById(R.id.btn_cancel);

        RadioGroup radioGroup = popupWindowView.findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.btn_this_month:
                        searchWindow.dismiss();
                        initThisMonth();
                        AppUtility.setWindowAlpha(getWindow(), 1);
                        txtStartDate.setText("");
                        txtEndDate.setText("");
                        txt1.setText("本月");
                        getOrderList(startDate, endDate);
                        break;
                    case R.id.rb_all:
                        searchWindow.dismiss();
                        AppUtility.setWindowAlpha(getWindow(), 1);
                        txtStartDate.setText("");
                        txtEndDate.setText("");
                        txt1.setText("全部");
                        startDate = "";
                        endDate = "";
                        getOrderList(startDate, endDate);
                        break;
                }
            }
        });

        btnThisMonth = popupWindowView.findViewById(R.id.btn_this_month);
        btnAll = popupWindowView.findViewById(R.id.rb_all);
        btnConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (datePicker1 == null && datePicker2 == null)
                {

                    txtStartDate.setText(startDate);
                    txtEndDate.setText(endDate);
                    btnStartDate.setText(startDate);
                    btnEndDate.setText(endDate);
                    getOrderList(startDate, endDate);

                } else if (datePicker1 == null)
                {

                    txtStartDate.setText(startDate);
                    txtEndDate.setText(datePicker2);
                    btnStartDate.setText(startDate);
                    btnEndDate.setText(datePicker2);
                    getOrderList(startDate, datePicker2);
                } else if (datePicker2 == null)
                {
                    txtStartDate.setText(datePicker1);
                    txtEndDate.setText(endDate);
                    btnStartDate.setText(datePicker1);
                    btnEndDate.setText(endDate);
                    getOrderList(datePicker1, endDate);
                } else
                {
                    txtStartDate.setText(datePicker1);
                    txtEndDate.setText(datePicker2);
                    btnStartDate.setText(datePicker1);
                    btnEndDate.setText(datePicker2);
                    getOrderList(datePicker1, datePicker2);
                }
                txt1.setText("～");
                searchWindow.dismiss();
                datePicker1 = null;
                datePicker2 = null;
                AppUtility.setWindowAlpha(getWindow(), 1);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchWindow.dismiss();
                datePicker1 = null;
                datePicker2 = null;
                AppUtility.setWindowAlpha(getWindow(), 1);
            }
        });
    }

    private void initThisMonth()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate = simpleDateFormat.format(calendar.getTime());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = simpleDateFormat.format(calendar2.getTime());
        txtStartDate.setText(startDate);
        txtEndDate.setText(endDate);
    }

    // 顯示起始日期選擇視窗
    private void showDatePicker(final View v, String txtBtnDate)
    {
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.getMinimum(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                datePicker1 = String.format("%d-%02d-%02d", year, (month + 1), dayOfMonth);
                btnStartDate.setText(datePicker1);
                btnThisMonth.setChecked(false);
                btnAll.setChecked(false);
            }
        }, year, month, day);
        DatePicker datePicker = null;
        if (datePicker2 != null)
        {
            datePicker = datePickerDialog.getDatePicker();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                datePicker.setMaxDate(sdf.parse(datePicker2).getTime());
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        datePickerDialog.show();
    }

    // 顯示起始日期選擇視窗
    private void showDatePicker2(final View v, String txtBtnDate)
    {
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.getMaximum(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                datePicker2 = String.format("%d-%02d-%02d", year, (month + 1), dayOfMonth);
                btnEndDate.setText(datePicker2);
                btnThisMonth.setChecked(false);
                btnAll.setChecked(false);
            }
        }, year, month, day);
        DatePicker datePicker = null;
        if (datePicker1 != null)
        {
            datePicker = datePickerDialog.getDatePicker();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                datePicker.setMinDate(sdf.parse(datePicker1).getTime());
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        datePickerDialog.show();
    }

    public Date stringToDate(String dateString)
    {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_go_back:
                Intent intent = new Intent(CostGeneralActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                finish();
                break;
            case R.id.btn_date:
                showSearchWindow();
                break;
        }
    }
}