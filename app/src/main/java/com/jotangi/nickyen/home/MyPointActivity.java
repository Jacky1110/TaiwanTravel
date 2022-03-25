package com.jotangi.nickyen.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.home.model.MyBonusBean;
import com.jotangi.nickyen.member.model.OrderListBean;
import com.jotangi.nickyen.model.MemberInfoBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MyPointActivity extends AppCompatActivity implements View.OnClickListener
{
    static String startDate, endDate;
    //UI
    private ProgressBar progressBar;
    private ImageButton btnGoBack;
    private ConstraintLayout btnDate;
    private TextView txtStartDate, txtEndDate, txtPoint, tvPointYet, txtCount, txt1, txtNoData, txtPointsExpiryTime;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    private FetchAdapter adapter;

    private DatePickerDialog datePickerDialog;
    private Button btnStartDate, btnEndDate;
    private RadioButton btnThisMonth, btnAll;
    private DatePickerDialog datePickerDialog1;

    private String datePicker1, datePicker2;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private PopupWindow searchWindow;

    private ArrayList<MyBonusBean> myBonusBeanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_point);

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
        txtPoint = findViewById(R.id.tv_point);
        tvPointYet = findViewById(R.id.tv_point_yet);
        txtCount = findViewById(R.id.tv_count);
        txt1 = findViewById(R.id.text);
        txtNoData = findViewById(R.id.tv_noData);
        txtPointsExpiryTime = findViewById(R.id.txtPointsExpiryTime);
        recyclerView = findViewById(R.id.recycler);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate = simpleDateFormat.format(calendar.getTime());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = simpleDateFormat.format(calendar2.getTime());
        txtStartDate.setText(startDate);
        txtEndDate.setText(endDate);
        if ((Integer) MemberInfoBean.member_points != null)
        {
            txtPoint.setText(String.valueOf(MemberInfoBean.member_points));
        } else
        {
            txtPoint.setText("0");
        }
        tvPointYet.setText(MemberInfoBean.bonus_will_get);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getMyBonus(startDate, endDate);
        getMyBonus2("",""); // 計算過期點數日期 消費最新一筆+一年
    }

    private void getMyBonus2(String s, String s1) {
        ApiConnection.getMyBonus(startDate, endDate, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Type type = new TypeToken<ArrayList<MyBonusBean>>()
                        {
                        }.getType();

                        ArrayList<MyBonusBean> data;
                        data = new Gson().fromJson(jsonString, type);

                        Collections.sort(data, new Comparator<MyBonusBean>()
                        {

                            @Override
                            public int compare(MyBonusBean o1, MyBonusBean o2)
                            {
                                return o2.getBonusDate().compareTo(o1.getBonusDate());
                            }
                        });
                        if (data.get(0).getBonusDate() != null)
                        {
                            Date date;
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try
                            {
//                                date = simpleDateFormat.parse(data.get(0).getBonusDate());
                                date = simpleDateFormat.parse(data.get(0).getBonusDate());
                                cal.setTime(date);
                                cal.add(Calendar.YEAR, 1);
                                date = cal.getTime();
                                String expireDate = sdf.format(date);
                                txtPointsExpiryTime.setText(expireDate);

                            } catch (ParseException e)
                            {
                                e.printStackTrace();
                                txtPointsExpiryTime.setText("-");
                            }
                        }else {
                            txtPointsExpiryTime.setText("-");
                        }
                        layoutViews(data);
                    }
                });
            }

            @Override
            public void onFailure(String message)
            {
                runOnUiThread(() -> txtPointsExpiryTime.setText("-"));
            }
        });
    }

    private void getMyBonus(String startDate, String endDate)
    {
        ApiConnection.getMyBonus(startDate, endDate, new ApiConnection.OnConnectResultListener()
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
                        txtNoData.setVisibility(View.GONE);

                        Type type = new TypeToken<ArrayList<MyBonusBean>>()
                        {
                        }.getType();
                        myBonusBeanArrayList = new ArrayList<>();
                        myBonusBeanArrayList = new Gson().fromJson(jsonString, type);

                        ArrayList<MyBonusBean> data = myBonusBeanArrayList;

                        Collections.sort(myBonusBeanArrayList, new Comparator<MyBonusBean>()
                        {

                            @Override
                            public int compare(MyBonusBean o1, MyBonusBean o2)
                            {
                                return o2.getBonusDate().compareTo(o1.getBonusDate());
                            }
                        });
                        layoutViews(data);
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
                        recyclerView.setAdapter(null);
                    }
                });
            }
        });
    }

    private void layoutViews(ArrayList<MyBonusBean> list)
    {

        mManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mManager);
        adapter = new FetchAdapter(R.layout.item_point_record, list);
        recyclerView.setAdapter(adapter);

//        int total = 0;
//
//        for (int i = 0; i < list.size(); i++)
//        {
//            total += Integer.parseInt(list.get(i).getBonus());
//        }

        if ((Integer) MemberInfoBean.member_points != null)
        {
            txtPoint.setText(String.valueOf(MemberInfoBean.member_points));
        } else
        {
            txtPoint.setText("0");
        }
//        txtAmount.setText(String.valueOf(total));
//        txtCount.setText("＊");
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
                        initThisMonth();
                        searchWindow.dismiss();
                        AppUtility.setWindowAlpha(getWindow(), 1);
                        txtStartDate.setText("");
                        txtEndDate.setText("");
                        txt1.setText("本月");
                        getMyBonus(startDate, endDate);
                        break;
                    case R.id.rb_all:
                        searchWindow.dismiss();
                        AppUtility.setWindowAlpha(getWindow(), 1);
                        txtStartDate.setText("");
                        txtEndDate.setText("");
                        txt1.setText("全部");
                        startDate = "";
                        endDate = "";
                        getMyBonus(startDate, endDate);
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
                    getMyBonus(startDate, endDate);

                } else if (datePicker1 == null)
                {

                    txtStartDate.setText(startDate);
                    txtEndDate.setText(datePicker2);
                    btnStartDate.setText(startDate);
                    btnEndDate.setText(datePicker2);
                    getMyBonus(startDate, datePicker2);
                } else if (datePicker2 == null)
                {
                    txtStartDate.setText(datePicker1);
                    txtEndDate.setText(endDate);
                    btnStartDate.setText(datePicker1);
                    btnEndDate.setText(endDate);
                    getMyBonus(datePicker1, endDate);
                } else
                {
                    txtStartDate.setText(datePicker1);
                    txtEndDate.setText(datePicker2);
                    btnStartDate.setText(datePicker1);
                    btnEndDate.setText(datePicker2);
                    getMyBonus(datePicker1, datePicker2);
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

        datePickerDialog1 = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener()
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
        datePickerDialog1.show();
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
                datePicker.setMinDate(new Date().getTime());
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
                finish();
                break;
            case R.id.btn_date:
                showSearchWindow();
                break;
        }
    }

    private class FetchAdapter extends BaseQuickAdapter<MyBonusBean, BaseViewHolder>
    {
        TextView txtDate, txtType, txtBonus, txtStoreName, btnDetail;

        public FetchAdapter(int layoutResId, @Nullable List<MyBonusBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, MyBonusBean myBonusBean)
        {
            txtStoreName = baseViewHolder.getView(R.id.tv_store_name);
            txtDate = baseViewHolder.getView(R.id.tv_order_date);
            txtType = baseViewHolder.getView(R.id.tv_pay_type);
            txtBonus = baseViewHolder.getView(R.id.tv_refund);
            btnDetail = baseViewHolder.getView(R.id.btn_order);
            btnDetail.setText("紅利明細");

            txtStoreName.setText(myBonusBean.getStoreName());
            txtDate.setText(myBonusBean.getBonusDate());
            String str;
            switch (myBonusBean.getBonusType())
            {
                case "1":
                    str = "消費累點";
                    txtBonus.setText("+" + myBonusBean.getBonus());
                    break;
                case "2":
                    str = "使用點數";
                    txtBonus.setText("-" + myBonusBean.getBonus());
                    break;
                case "3":
                    str = "平台贈點";
                    txtBonus.setText("+" + myBonusBean.getBonus());
                    break;
                default:
                    str = "";
                    break;
            }
            txtType.setText(str);

            btnDetail.setOnClickListener(v ->
            {
                MyPointDetailFragment myPointDetailFragment = MyPointDetailFragment.newInstance(myBonusBean);
                ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.myPointLayout, new MyPointDetailFragment(), null).addToBackStack(null).commit();
            });
        }
    }
}