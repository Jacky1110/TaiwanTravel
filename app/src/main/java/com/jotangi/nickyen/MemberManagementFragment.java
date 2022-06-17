package com.jotangi.nickyen;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jotangi.nickyen.api.ApiEnqueue;
import com.jotangi.nickyen.merch.OrderFetchAdapter;
import com.jotangi.nickyen.merch.model.MerchOrderBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.jar.JarException;

public class MemberManagementFragment extends Fragment implements View.OnClickListener {

    private String TAG = MemberManagementFragment.class.getSimpleName() + "(TAG)";

    static String startDate, endDate;

    private ApiEnqueue apiEnqueue;
    private ProgressBar progressBar;
    private ImageButton btnGoBack;
    private ConstraintLayout btnDate;
    private TextView txtStartDate, txtEndDate, txt1, txtPeople, txtNoData;
    private RecyclerView recyclerView;


    private DatePickerDialog datePickerDialog;
    private Button btnStartDate, btnEndDate;
    private RadioButton btnThisMonth, btnAll;

    private String datePicker1, datePicker2;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private PopupWindow searchWindow;

    ArrayList data = new ArrayList();
    MemberAdapter adapter;

    public MemberManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_management, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {
        apiEnqueue = new ApiEnqueue();
        progressBar = v.findViewById(R.id.progressBar);

        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);

        btnDate = v.findViewById(R.id.btn_date);
        btnDate.setOnClickListener(this);

        txtStartDate = v.findViewById(R.id.txt_start_date);
        txtEndDate = v.findViewById(R.id.txt_end_date);
        txt1 = v.findViewById(R.id.text);
        txtNoData = v.findViewById(R.id.tv_noData);
        txtPeople = v.findViewById(R.id.tv_people);
        recyclerView = v.findViewById(R.id.rec_manage);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));

        initThisMonth();
    }

    private void initThisMonth() {
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

    @Override
    public void onResume() {
        super.onResume();
        getOrderList(startDate, endDate);
    }

    private void getOrderList(String startDate, String endDate) {
        apiEnqueue.MemberList(startDate, endDate, new ApiEnqueue.resultListener() {
            @Override
            public void onSuccess(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.GONE);

                        data = new ArrayList();

                        try {
                            JSONArray jsonArray = new JSONArray(message);
                            Log.d(TAG, "jsonArray: " + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MemberModel model = new MemberModel();
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                model.name = jsonObject.getString("member_name");
                                model.day = jsonObject.getString("member_date");
                                model.mid = jsonObject.getString("mid");
                                data.add(model);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new MemberAdapter();
                                    adapter.setmData(data);
                                    recyclerView.setAdapter(adapter);
                                    txtPeople.setText("總計" + data.size() + "人");
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    txtNoData.setVisibility(View.VISIBLE);
                                    txtPeople.setText("總計" + String.valueOf(data.size()) + "人");
                                    recyclerView.setAdapter(null);
                                }
                            });

                        }
                    }
                });

            }

            @Override
            public void onFailure(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        txtNoData.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(null);
                    }
                });
            }
        });

    }


    private void showSearchWindow() {
        if (searchWindow != null && searchWindow.isShowing()) {
            return;
        }
        final View popupWindowView = View.inflate(getContext(), R.layout.dialog_merch_serach, null);
        searchWindow = AppUtility.buildPopupWindow(popupWindowView, false);
        AppUtility.setWindowAlpha(getActivity().getWindow(), 0.5f);
        View anchorView = getActivity().findViewById(R.id.popup_show_as_view);
        searchWindow.showAsDropDown(anchorView, 0, 0);

        // 預約日期
        btnStartDate = popupWindowView.findViewById(R.id.btn_start_date);
        btnStartDate.setText("起始日期");
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view, String.valueOf(btnStartDate.getText()));
            }
        });

        btnEndDate = popupWindowView.findViewById(R.id.btn_end_date);
        btnEndDate.setText("結束日期");
        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker2(view, String.valueOf(btnEndDate.getText()));
            }
        });

        Button btnConfirm = popupWindowView.findViewById(R.id.btn_confirm);
        Button btnCancel = popupWindowView.findViewById(R.id.btn_cancel);

        RadioGroup radioGroup = popupWindowView.findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_this_month:
                        searchWindow.dismiss();
                        initThisMonth();
                        AppUtility.setWindowAlpha(getActivity().getWindow(), 1);
                        txtStartDate.setText("");
                        txtEndDate.setText("");
                        txt1.setText("本月");
                        getOrderList(startDate, endDate);
                        break;
                    case R.id.rb_all:
                        searchWindow.dismiss();
                        AppUtility.setWindowAlpha(getActivity().getWindow(), 1);
                        txtStartDate.setText("");
                        txtEndDate.setText("");
                        txt1.setText("全部");
                        startDate = "";
                        endDate = "";
                        getOrderList(startDate, endDate);
//                      recyclerView.setAdapter(null);
                        break;
                }
            }
        });

        btnThisMonth = popupWindowView.findViewById(R.id.btn_this_month);
        btnAll = popupWindowView.findViewById(R.id.rb_all);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (btnThisMonth.isChecked())
//                {
//                    getOrderList(startDate, endDate);
//                }
//                else
//                {
                if (datePicker1 == null && datePicker2 == null) {

                    txtStartDate.setText(startDate);
                    txtEndDate.setText(endDate);
                    btnStartDate.setText(startDate);
                    btnEndDate.setText(endDate);
                    getOrderList(startDate, endDate);

                } else if (datePicker1 == null) {

                    txtStartDate.setText(startDate);
                    txtEndDate.setText(datePicker2);
                    btnStartDate.setText(startDate);
                    btnEndDate.setText(datePicker2);
                    getOrderList(startDate, datePicker2);
                } else if (datePicker2 == null) {
                    txtStartDate.setText(datePicker1);
                    txtEndDate.setText(endDate);
                    btnStartDate.setText(datePicker1);
                    btnEndDate.setText(endDate);
                    getOrderList(datePicker1, endDate);
                } else {
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
                AppUtility.setWindowAlpha(getActivity().getWindow(), 1);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchWindow.dismiss();
                datePicker1 = null;
                datePicker2 = null;
                AppUtility.setWindowAlpha(getActivity().getWindow(), 1);
            }
        });
    }

    // 顯示起始日期選擇視窗
    private void showDatePicker(View view, String valueOf) {
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.getMinimum(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datePicker1 = String.format("%d-%02d-%02d", year, (month + 1), dayOfMonth);
                btnStartDate.setText(datePicker1);
                btnThisMonth.setChecked(false);
                btnAll.setChecked(false);
            }
        }, year, month, day);
        DatePicker datePicker = null;
        if (datePicker2 != null) {
            datePicker = datePickerDialog.getDatePicker();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                datePicker.setMaxDate(sdf.parse(datePicker2).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        datePickerDialog.show();
    }

    // 顯示起始日期選擇視窗
    private void showDatePicker2(final View v, String txtBtnDate) {
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.getMaximum(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                datePicker2 = String.format("%d-%02d-%02d", year, (month + 1), dayOfMonth);
                btnEndDate.setText(datePicker2);
                btnThisMonth.setChecked(false);
                btnAll.setChecked(false);
            }
        }, year, month, day);
        DatePicker datePicker = null;
        if (datePicker1 != null) {
            datePicker = datePickerDialog.getDatePicker();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                datePicker.setMinDate(sdf.parse(datePicker1).getTime());
            } catch (ParseException e) {
                datePicker.setMinDate(new Date().getTime());
            }
        }
        datePickerDialog.show();
    }

    public Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_go_back:
                Navigation.findNavController(getView()).popBackStack();
                break;
            case R.id.btn_date:
                showSearchWindow();
                break;
        }
    }

}