package com.jotangi.nickyen.merch.reserve;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.DialogIOSUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.beautySalons.SalonsRecordActivity;
import com.jotangi.nickyen.beautySalons.model.DesignerBean;
import com.jotangi.nickyen.beautySalons.model.OrderBean;
import com.jotangi.nickyen.questionnaire.QuestionnaireActivity;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SuppressWarnings("unchecked")
public class ReserveFragment extends Fragment
{
    private ImageButton btnBack;
    private TextView txtNoData, txtCount;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    static String startDate, endDate;
    private DatePickerDialog datePickerDialog;
    private ConstraintLayout btnDate;
    private PopupWindow searchWindow;
    private Button btnStartDate, btnEndDate;
    private TextView txtStartDate, txtEndDate, txt1;
    private RadioButton btnThisMonth, btnAll;
    private String datePicker1, datePicker2;

    private ArrayList<OrderBean> orderBeanList = new ArrayList<>();
    private ArrayList<DesignerBean> designerBeanList = new ArrayList<>();
    private ArrayList<DesignerBean> arrayList1 = new ArrayList(); //????????????????????????

    private RecyclerView recyclerView;
    private RecyclerAdapter rAdapter;

    private String getLab; //?????????????????????

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ReserveFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_reserve, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavController controller;
                controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_reserveFragment_to_merchHomeFragment);
            }
        });
        btnDate = getView().findViewById(R.id.btn_date);
        btnDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showSearchWindow();
            }
        });
        txtStartDate = getView().findViewById(R.id.txt_start_date);
        txtEndDate = getView().findViewById(R.id.txt_end_date);
        txt1 = getView().findViewById(R.id.text);
        progressBar = getView().findViewById(R.id.progressBar);
        txtNoData = getView().findViewById(R.id.tv_noData);
        txtCount = getView().findViewById(R.id.tv_count);

        recyclerView = getView().findViewById(R.id.recycler);

        radioGroup = getView().findViewById(R.id.radio_group);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate = simpleDateFormat.format(calendar.getTime());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = simpleDateFormat.format(calendar2.getTime());
        txtStartDate.setText(startDate);
        txtEndDate.setText(endDate);
        loadDesignerData();
        loadData(startDate, endDate);
    }

    private void showSearchWindow()
    {
        if (searchWindow != null && searchWindow.isShowing())
        {
            return;
        }
        final View popupWindowView = View.inflate(getContext(), R.layout.dialog_merch_serach, null);
        searchWindow = AppUtility.buildPopupWindow(popupWindowView, false);
        AppUtility.setWindowAlpha(getActivity().getWindow(), 0.5f);
        View anchorView = getActivity().findViewById(R.id.popup_show_as_view);
        searchWindow.showAsDropDown(anchorView, 0, 0);

        // ????????????
        btnStartDate = popupWindowView.findViewById(R.id.btn_start_date);
        btnStartDate.setText("????????????");
        btnStartDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDatePicker(view, String.valueOf(btnStartDate.getText()));
            }
        });
        btnEndDate = popupWindowView.findViewById(R.id.btn_end_date);
        btnEndDate.setText("????????????");
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
                        AppUtility.setWindowAlpha(getActivity().getWindow(), 1);
                        initThisMonth();
                        txtStartDate.setText("");
                        txtEndDate.setText("");
                        txt1.setText("??????");
                        loadData2(startDate, endDate);
                        searchWindow.dismiss();
                        break;
                    case R.id.rb_all:
                        AppUtility.setWindowAlpha(getActivity().getWindow(), 1);
                        txtStartDate.setText("");
                        txtEndDate.setText("");
                        txt1.setText("??????");
                        startDate = "";
                        endDate = "";
                        loadData2(startDate, endDate);
                        searchWindow.dismiss();
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
                    loadData2(startDate, endDate);
                } else if (datePicker1 == null)
                {
                    txtStartDate.setText(startDate);
                    txtEndDate.setText(datePicker2);
                    btnStartDate.setText(startDate);
                    btnEndDate.setText(datePicker2);
                    loadData2(startDate, datePicker2);
                } else if (datePicker2 == null)
                {
                    txtStartDate.setText(datePicker1);
                    txtEndDate.setText(endDate);
                    btnStartDate.setText(datePicker1);
                    btnEndDate.setText(endDate);
                    loadData2(datePicker1, endDate);
                } else
                {
                    txtStartDate.setText(datePicker1);
                    txtEndDate.setText(datePicker2);
                    btnStartDate.setText(datePicker1);
                    btnEndDate.setText(datePicker2);
                    loadData2(datePicker1, datePicker2);
                }
                txt1.setText("???");
                searchWindow.dismiss();
                datePicker1 = null;
                datePicker2 = null;
                AppUtility.setWindowAlpha(getActivity().getWindow(), 1);
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
                AppUtility.setWindowAlpha(getActivity().getWindow(), 1);
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

    // ??????????????????????????????
    private void showDatePicker(final View v, String txtBtnDate)
    {
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.getMinimum(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener()
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

    // ??????????????????????????????
    private void showDatePicker2(final View v, String txtBtnDate)
    {
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.getMaximum(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener()
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

    private void loadDesignerData()
    {
        ApiConnection.storeHairStylist(new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<DesignerBean>>()
                {
                }.getType();
                designerBeanList = new Gson().fromJson(jsonString, type);
            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

    //????????????????????????api
    private void loadData(String start, String end)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.storeBookingList(start, end, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<OrderBean>>()
                {
                }.getType();
                if (orderBeanList != null)
                {
                    orderBeanList.clear();
                }
                orderBeanList = new Gson().fromJson(jsonString, type);
                Collections.sort(orderBeanList, (o1, o2) -> o2.getReserveDate().compareTo(o1.getReserveDate()));
                getActivity().runOnUiThread(() ->
                {
                    progressBar.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.GONE);
                    layoutView(orderBeanList);
                });
            }

            @Override
            public void onFailure(String message)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.VISIBLE);
                        layoutView(orderBeanList);
                    }
                });
            }
        });
    }

    //?????????????????????Api
    private void loadData2(String start, String end)
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.storeBookingList(start, end, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<OrderBean>>()
                {
                }.getType();
                if (orderBeanList != null)
                {
                    orderBeanList.clear();
                }
                orderBeanList = new Gson().fromJson(jsonString, type);
                Collections.sort(orderBeanList, (o1, o2) -> o2.getReserveDate().compareTo(o1.getReserveDate()));
                getActivity().runOnUiThread(() ->
                {
                    progressBar.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.GONE);
                    layoutView2(orderBeanList);
                });
            }

            @Override
            public void onFailure(String message)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        txtNoData.setText("???????????????");
                        txtNoData.setVisibility(View.VISIBLE);
                        if (orderBeanList != null)
                        {
                            orderBeanList.clear();
                        }
                        layoutView2(orderBeanList);
                    }
                });
            }
        });
    }

    long selectTime;

    //????????????????????????
    private void layoutView(ArrayList<OrderBean> orderBeanList)
    {
        Calendar calendar = Calendar.getInstance();
        //?????????????????????????????????????????????
        calendar.setTimeInMillis(System.currentTimeMillis());
        //????????????
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //??????????????????
        long systemTime = System.currentTimeMillis();
        //?????????????????? ??????24:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //???????????????????????????
        selectTime = calendar.getTimeInMillis();

        arrayList1.add(new DesignerBean("0.1", "", "??????", "", "", ""));
        arrayList1.addAll(designerBeanList);
//        int count = 0;
//        for (int i = 0; i < orderBeanList.size(); i++)
//        {
//            arrayList.add(orderBeanList.get(i).getNickName());
//            Date date = new Date();
//            try
//            {
//                date = simpleDateFormat.parse(orderBeanList.get(i).getReserveDate());
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            if (selectTime == date.getTime())
//            {
//                count += 1;
//            }
//            Log.d("??????", ": " + count);
//        }
        //????????????
//        Iterator it = arrayList.iterator();
//        while (it.hasNext())
//        {
//            String s = (String) it.next();
//            if (!arrayList1.contains(s))
//            {
//                arrayList1.add(s);
//            }
//        }

        for (int i = 0; i < arrayList1.size(); i++)
        {
            radioButton = new RadioButton(getContext());
            int width = (int) (getResources().getDimension(R.dimen.dp_80));
            int height = (int) (getResources().getDimension(R.dimen.dp_40));
            radioButton.setWidth(width);
            radioButton.setHeight(height);
            int dimension = (int) (getResources().getDimension(R.dimen.dp_10));
            radioButton.setPadding(dimension, dimension, dimension, dimension);
//            radioButton.setPadding(AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10));
            radioButton.setBackgroundResource(R.drawable.radio_selector);
            radioButton.setText(arrayList1.get(i).getNickName());
            radioButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            @SuppressLint("ResourceType")
            XmlResourceParser xrp = getResources().getXml(R.drawable.text_color);
            try
            {
                ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
                radioButton.setTextColor(csl);
            } catch (Exception e)
            {
            }
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setId(i);
            if (i == 0)
            {
                int count = 0;
                for (int y = 0; y < orderBeanList.size(); y++)
                {
                    Date date = new Date();
                    try
                    {
                        date = simpleDateFormat.parse(orderBeanList.get(y).getReserveDate());
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    if (selectTime == date.getTime())
                    {
                        count += 1;
                    }
                    Log.d("??????", ": " + count);
                }
                txtCount.setText("   ???????????????????????? " + count + " ???");

                radioButton.setChecked(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                if (orderBeanList == null)
                {
                    recyclerView.setAdapter(null);
                } else
                {
                    rAdapter = new RecyclerAdapter(R.layout.item_merch_reserve, orderBeanList);
                    recyclerView.setAdapter(rAdapter);
                }
            }
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            if (i != 0)
//            {
            layoutParams.setMargins(0, 0, AppUtility.dp2px(getActivity(), 10), 0);

//            } else
//            {
//                layoutParams.setMargins(0, 0, 0, 0);
//            }
            radioGroup.addView(radioButton, layoutParams);
            getLab = "??????";
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    radioButton = getView().findViewById(group.getCheckedRadioButtonId());
                    getLab = radioButton.getText().toString();
                    int getGroupId = group.indexOfChild(radioButton); //??????button??????

                    for (int j = 0; j < arrayList1.size(); j++)
                    {
                        if (getLab.equals("??????"))
                        {
                            int count = 0;
                            for (int y = 0; y < orderBeanList.size(); y++)
                            {
                                Date date = new Date();
                                try
                                {
                                    date = simpleDateFormat.parse(orderBeanList.get(y).getReserveDate());
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                if (selectTime == date.getTime())
                                {
                                    count += 1;
                                }
                            }
                            txtCount.setText("   ???????????????????????? " + count + " ???");
                            rAdapter = new RecyclerAdapter(R.layout.item_merch_reserve, orderBeanList);
                            recyclerView.setAdapter(rAdapter);

                        } else if (getLab.equals(arrayList1.get(j).getNickName()) && !getLab.equals("??????"))
                        {
                            ArrayList<OrderBean> filter = new ArrayList<>();
                            for (int k = 0; k < orderBeanList.size(); k++)
                            {
                                if (getLab.equals(orderBeanList.get(k).getNickName()))
                                {
                                    filter.add(orderBeanList.get(k));
                                }
                                rAdapter = new RecyclerAdapter(R.layout.item_merch_reserve, filter);
                                recyclerView.setAdapter(rAdapter);
                            }
                            Log.d("??????", "onCheckedChanged: 2" + filter);
                            int count = 0;
                            for (int y = 0; y < filter.size(); y++)
                            {
                                Date date = new Date();
                                try
                                {
                                    date = simpleDateFormat.parse(filter.get(y).getReserveDate());
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                if (selectTime == date.getTime())
                                {
                                    count += 1;
                                }
                                Log.d("??????", ": " + count);
                            }
                            txtCount.setText("   ???????????????????????? " + count + " ???");
                        }
                    }
                }
            });
        }
    }

    //?????????????????????????????? ?????????????????????????????????
    private void layoutView2(ArrayList<OrderBean> orderBeanList)
    {
        Calendar calendar = Calendar.getInstance();
        //?????????????????????????????????????????????
        calendar.setTimeInMillis(System.currentTimeMillis());
        //????????????
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //?????????????????? ??????24:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //???????????????????????????
        selectTime = calendar.getTimeInMillis();

        ArrayList<DesignerBean> arrayList1 = new ArrayList();
        arrayList1.add(new DesignerBean("0.1", "", "??????", "", "", ""));
        arrayList1.addAll(designerBeanList);

        for (int i = 0; i < arrayList1.size(); i++)
        {
//            radioButton = new RadioButton(getContext());
//            int width = (int) (getResources().getDimension(R.dimen.dp_80));
//            int height = (int) (getResources().getDimension(R.dimen.dp_40));
//            radioButton.setWidth(width);
//            radioButton.setHeight(height);
//            int dimension = (int) (getResources().getDimension(R.dimen.dp_10));
//            radioButton.setPadding(dimension, dimension, dimension, dimension);
////            radioButton.setPadding(AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10));
//            radioButton.setBackgroundResource(R.drawable.radio_selector);
//            radioButton.setText(arrayList1.get(i).getNickName());
//            radioButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            @SuppressLint("ResourceType")
//            XmlResourceParser xrp = getResources().getXml(R.drawable.text_color);
//            try
//            {
//                ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
//                radioButton.setTextColor(csl);
//            } catch (Exception e)
//            {
//            }
//            radioButton.setButtonDrawable(android.R.color.transparent);
//            radioButton.setId(i);
            if (i == 0)
            {
                int count = 0;
                for (int y = 0; y < orderBeanList.size(); y++)
                {
                    Date date = new Date();
                    try
                    {
                        date = simpleDateFormat.parse(orderBeanList.get(y).getReserveDate());
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    if (selectTime == date.getTime())
                    {
                        count += 1;
                    }
                    Log.d("??????", ": " + count);
                }
                txtCount.setText("   ???????????????????????? " + count + " ???");

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                if (orderBeanList == null)
                {
                    recyclerView.setAdapter(null);
                    txtNoData.setText("???????????????");
                    txtNoData.setVisibility(View.VISIBLE);
                } else
                {
                    rAdapter = new RecyclerAdapter(R.layout.item_merch_reserve, orderBeanList);
                    recyclerView.setAdapter(rAdapter);
                    txtNoData.setVisibility(View.GONE);
                }
            }
//            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//            layoutParams.setMargins(0, 0, AppUtility.dp2px(getActivity(), 10), 0);
//            radioGroup.addView(radioButton, layoutParams);
            designerSort();
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    radioButton = getView().findViewById(group.getCheckedRadioButtonId());
                    getLab = radioButton.getText().toString();
                    int getGroupId = group.indexOfChild(radioButton); //??????button??????
                    designerSort();
//                    for (int j = 0; j < arrayList1.size(); j++)
//                    {
//                        if (getLab.equals("??????"))
//                        {
//                            int count = 0;
//                            for (int y = 0; y < orderBeanList.size(); y++)
//                            {
//                                Date date = new Date();
//                                try
//                                {
//                                    date = simpleDateFormat.parse(orderBeanList.get(y).getReserveDate());
//                                } catch (Exception e)
//                                {
//                                    e.printStackTrace();
//                                }
//                                if (selectTime == date.getTime())
//                                {
//                                    count += 1;
//                                }
//                            }
//                            txtCount.setText("   ???????????????????????? " + count + " ???");
//                            rAdapter = new RecyclerAdapter(R.layout.item_merch_reserve, orderBeanList);
//                            recyclerView.setAdapter(rAdapter);
//
//                        } else if (getLab.equals(arrayList1.get(j).getNickName()) && !getLab.equals("??????"))
//                        {
//                            ArrayList<OrderBean> filter = new ArrayList<>();
//                            for (int k = 0; k < orderBeanList.size(); k++)
//                            {
//                                if (getLab.equals(orderBeanList.get(k).getNickName()))
//                                {
//                                    filter.add(orderBeanList.get(k));
//                                }
//                                rAdapter = new RecyclerAdapter(R.layout.item_merch_reserve, filter);
//                                recyclerView.setAdapter(rAdapter);
//                            }
//                            Log.d("??????", "onCheckedChanged: 2" + filter);
//                            int count = 0;
//                            for (int y = 0; y < filter.size(); y++)
//                            {
//                                Date date = new Date();
//                                try
//                                {
//                                    date = simpleDateFormat.parse(orderBeanList.get(y).getReserveDate());
//                                } catch (Exception e)
//                                {
//                                    e.printStackTrace();
//                                }
//                                if (selectTime == date.getTime())
//                                {
//                                    count += 1;
//                                }
//                                Log.d("??????", ": " + count);
//                            }
//                            txtCount.setText("   ???????????????????????? " + count + " ???");
//                        }
//                    }
                }
            });

        }
    }

    private void designerSort()
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int j = 0; j < arrayList1.size(); j++)
                {
                    if (getLab.equals("??????"))
                    {
                        int count = 0;
                        for (int y = 0; y < orderBeanList.size(); y++)
                        {
                            Date date = new Date();
                            try
                            {
                                date = simpleDateFormat.parse(orderBeanList.get(y).getReserveDate());
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            if (selectTime == date.getTime())
                            {
                                count += 1;
                            }
                        }
                        txtCount.setText("   ???????????????????????? " + count + " ???");
                        rAdapter = new RecyclerAdapter(R.layout.item_merch_reserve, orderBeanList);
                        recyclerView.setAdapter(rAdapter);

                    } else if (getLab.equals(arrayList1.get(j).getNickName()) && !getLab.equals("??????"))
                    {
                        ArrayList<OrderBean> filter = new ArrayList<>();
                        for (int k = 0; k < orderBeanList.size(); k++)
                        {
                            if (getLab.equals(orderBeanList.get(k).getNickName()))
                            {
                                filter.add(orderBeanList.get(k));
                            }
                            rAdapter = new RecyclerAdapter(R.layout.item_merch_reserve, filter);
                            recyclerView.setAdapter(rAdapter);
                        }
                        Log.d("??????", "onCheckedChanged: 2" + filter);
                        int count = 0;
                        for (int y = 0; y < filter.size(); y++)
                        {
                            Date date = new Date();
                            try
                            {
                                date = simpleDateFormat.parse(filter.get(y).getReserveDate());
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            if (selectTime == date.getTime())
                            {
                                count += 1;
                            }
                            Log.d("??????", ": " + count);
                        }
                        txtCount.setText("   ???????????????????????? " + count + " ???");
                    }
                }
            }
        });
    }

    private class RecyclerAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder>
    {
        private TextView txtDate, txtDesigner, txtService, txtReserveStatus;
        private Button btnDetail, btnReserveConfirm, btnReserveModify;
        private LinearLayout selectLayout;

        public RecyclerAdapter(final int layoutResId, @org.jetbrains.annotations.Nullable final List<OrderBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, OrderBean orderBean)
        {
            txtDate = baseViewHolder.getView(R.id.tv_order_date);
            txtDate.setText(orderBean.getReserveDate() + " " + orderBean.getReserveTime());
            txtService = baseViewHolder.getView(R.id.tv_service);
            btnDetail = baseViewHolder.getView(R.id.btn_refund);
            selectLayout = baseViewHolder.getView(R.id.select_layout);
            btnReserveConfirm = baseViewHolder.getView(R.id.btn_reserve_confirm);
            btnReserveModify = baseViewHolder.getView(R.id.btn_reserve_modify);
            txtDesigner = baseViewHolder.getView(R.id.tv_designer);
            txtReserveStatus = baseViewHolder.getView(R.id.tv_reserve_status);

            Bundle bundle = new Bundle();
            bundle.putString("designer", orderBean.getNickName());
            bundle.putString("bookingNo", orderBean.getBookingNo());

            if (orderBean.getNickName() != null)
            {
                txtDesigner.setText("????????????" + orderBean.getNickName() + " ????????????" + orderBean.getMemberName());
            } else
            {
                txtDesigner.setText("??????????????????????????????" + " ????????????" + orderBean.getMemberName());
            }

            String s = orderBean.getServiceName().replace(",", "???");
            txtService.setText(s);
            String status = AppUtility.reserveStatus(orderBean.getReserveStatus());
            if (orderBean.getReserveStatus().equals("0") && AppUtility.isExpire(orderBean.getReserveDate() + orderBean.getReserveTime(),true))
            {
                btnDetail.setBackgroundResource(R.drawable.shape_round_light_af);
                btnDetail.setVisibility(View.VISIBLE);
                selectLayout.setVisibility(View.GONE);
                txtReserveStatus.setText("????????????????????????");
            } else if (orderBean.getReserveStatus().equals("0") && !AppUtility.isExpire(orderBean.getReserveDate() + orderBean.getReserveTime(),true))
            {
                btnDetail.setVisibility(View.GONE);
                selectLayout.setVisibility(View.VISIBLE);
                txtReserveStatus.setText("???????????????"+status);

                btnReserveConfirm.setOnClickListener(v ->
                {
                    bundle.putString("page", "reserveConfirm");
                    Navigation.findNavController(v).navigate(R.id.action_reserveFragment_to_reserveDetailFragment, bundle);
                });

                btnReserveModify.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Navigation.findNavController(v).navigate(R.id.action_reserveFragment_to_reserveModifyFragment, bundle);
                    }
                });
            } else
            {
                btnDetail.setBackgroundResource(R.drawable.shape_round_light_typered);
                btnDetail.setVisibility(View.VISIBLE);
                selectLayout.setVisibility(View.GONE);
                txtReserveStatus.setText("???????????????"+status);
            }

            btnDetail.setOnClickListener(v ->
            {
                bundle.putString("page", "normal");
                Navigation.findNavController(v).navigate(R.id.action_reserveFragment_to_reserveDetailFragment, bundle);
            });
        }
    }
}