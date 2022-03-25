package com.jotangi.nickyen.industry;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.industry.model.ClassBean;
import com.jotangi.nickyen.industry.model.ProgramBean;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class IndustryIntroduceFragment extends Fragment implements View.OnClickListener
{
    private ImageButton btnBack, btnSub, btnAdd;
    private Button btnEnter;
    private TextView txtCount, txtDescript, txtNoData, btnDetail, txtProgramName;
    private ImageView imgContent, imgProduce;;
    private EditText editRemark;
    //    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private String getLab; //專案按鈕標籤

    private NavController controller;
    private ClassBean classBean = new ClassBean();
    private ArrayList<ProgramBean> programList = new ArrayList();
    private ProgramBean programBean;

    private int count = 1;

    public IndustryIntroduceFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            classBean = new Gson().fromJson(getArguments().getString("data"), ClassBean.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_industry_introduce, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable final Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        btnBack = getView().findViewById(R.id.ib_go_back);
        btnBack.setOnClickListener(this);
        btnEnter = getView().findViewById(R.id.btn_enter);
        btnEnter.setOnClickListener(this);
        btnSub = getView().findViewById(R.id.btn_sub);
        btnSub.setOnClickListener(this);
        btnAdd = getView().findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnDetail = getView().findViewById(R.id.btn_detail);
        btnDetail.setOnClickListener(this);

        txtProgramName = getView().findViewById(R.id.tv_programName);
        txtProgramName.setText(classBean.getClassName());
        txtNoData = getView().findViewById(R.id.tv_noData);
        txtCount = getView().findViewById(R.id.tv_count);
        txtCount.setText(String.valueOf(count));
        txtDescript = getView().findViewById(R.id.tv_descript);
        txtDescript.setText(classBean.getClassDescript());
        if (classBean.getClassPicture2() != null)
            btnDetail.setVisibility(View.VISIBLE);

        imgContent = getView().findViewById(R.id.img_content);
        Picasso.with(getActivity()).load(ApiConstant.API_IMAGE + classBean.getClassPicture()).into(imgContent);
        imgProduce = getView().findViewById(R.id.iv_content);
        if (classBean.getClassPicture2() != null){
        Picasso.with(getActivity()).load(ApiConstant.API_IMAGE + classBean.getClassPicture2()).into(imgProduce);
        imgProduce.setVisibility(View.VISIBLE);
        }

//        recyclerView = getView().findViewById(R.id.recycler);
        radioGroup = getView().findViewById(R.id.radio_group);
        editRemark = getView().findViewById(R.id.edit_remark);
        progressBar = getView().findViewById(R.id.progressBar);
        loadData();
    }


    private void loadData()
    {
        progressBar.setVisibility(View.VISIBLE);
        ApiConnection.programList(classBean.getCid(), new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<ProgramBean>>()
                {
                }.getType();
                programList = new Gson().fromJson(jsonString, type);

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        layoutView(programList);

                    }
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
                        txtNoData.setText(R.string.please_check_the_connection_or_try_again);
                        btnEnter.setClickable(false);
                    }
                });
            }
        });
    }

    private void layoutView(ArrayList<ProgramBean> programList)
    {
        for (int i = 0; i < programList.size(); i++)
        {
            radioButton = new RadioButton(getContext());
            int width = (int) (getResources().getDimension(R.dimen.dp_80));
            int height = (int) (getResources().getDimension(R.dimen.dp_70));
            radioButton.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            radioButton.setHeight(height);
            int dimension = (int) (getResources().getDimension(R.dimen.dp_10));
            radioButton.setPadding(dimension, dimension, dimension, dimension);
//            radioButton.setPadding(AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10),AppUtility.dp2px(getActivity(),10));
            radioButton.setBackgroundResource(R.drawable.selector_industry);
            radioButton.setText(programList.get(i).getProgramName() + "\n" + "價位：$" + programList.get(i).getProgramPrice());
            radioButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            radioButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setId(i);

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, AppUtility.dp2px(getActivity(), 10), 0, 0);
            radioGroup.addView(radioButton, layoutParams);
            getLab = radioButton.getText().toString();
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    radioButton = getView().findViewById(group.getCheckedRadioButtonId());
                    getLab = radioButton.getText().toString();
                    int getGroupId = group.indexOfChild(radioButton); //找出button位置

                    for (int j = 0; j < programList.size(); j++)
                    {
                        //出問題
                        if (getLab.equals(programList.get(j).getProgramName() + "\n" + "價位：$" + programList.get(j).getProgramPrice()))
                        {
                            programBean = programList.get(j);
                        }
                    }

                    Log.d("豪豪", "onCheckedChanged: " + programBean);
//                    for (int j = 0; j < arrayList1.size(); j++)
//                    {
//                        if (getLab.equals("全部"))
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
//                            txtCount.setText("   本日預約人數總計 " + count + " 人");
//                            rAdapter = new ReserveFragment.RecyclerAdapter(R.layout.item_merch_reserve, orderBeanList);
//                            recyclerView.setAdapter(rAdapter);
//
//                        } else if (getLab.equals(arrayList1.get(j).getNickName()) && !getLab.equals("全部"))
//                        {
//                            ArrayList<OrderBean> filter = new ArrayList<>();
//                            for (int k = 0; k < orderBeanList.size(); k++)
//                            {
//                                if (getLab.equals(orderBeanList.get(k).getNickName()))
//                                {
//                                    filter.add(orderBeanList.get(k));
//                                }
//                                rAdapter = new ReserveFragment.RecyclerAdapter(R.layout.item_merch_reserve, filter);
//                                recyclerView.setAdapter(rAdapter);
//                            }
//                            Log.d("安安", "onCheckedChanged: 2" + filter);
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
//                                Log.d("安安", ": " + count);
//                            }
//                            txtCount.setText("   本日預約人數總計 " + count + " 人");
//                        }
//                    }
                }
            });
        }
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        RecyclerAdapter adapter = new RecyclerAdapter(programList, btnEnter, getActivity());
//        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v)
    {
        Bundle bundle = new Bundle();
        switch (v.getId())
        {
            case R.id.ib_go_back:
                Navigation.findNavController(getView()).popBackStack();
                break;
            case R.id.btn_sub:
                count -= 1;
                if (count < 1)
                {
                    count = 1;
                    return;
                }
                txtCount.setText(String.valueOf(count));
                break;
            case R.id.btn_add:
                count += 1;
                for (int i = 0; i < programList.size(); i++)
                {
//                    if (radioButton.isChecked() && radioButton.getText().toString().equals(programList.get(i).getProgramName() + "\n" + "價位：$" + programList.get(i).getProgramPrice()) && count > Integer.parseInt(programList.get(i).getProgramLimit()))// 打勾 然後根據專案position 去確認限制人數
                    if (count > Integer.parseInt(programList.get(0).getProgramLimit()))
                    {
                        count = Integer.parseInt(programList.get(0).getProgramLimit());
                        Toast.makeText(getActivity(), "人數無法大於所選方案限制人數", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                txtCount.setText(String.valueOf(count));
                break;
            case R.id.btn_enter:
                if (!radioButton.isChecked())
                {
                    Toast.makeText(getActivity(), "請選擇方案", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (editRemark.getText().toString().trim().equals("")){
//                    Toast.makeText(getActivity(), "請填備註", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                bundle.putString("classBean", new Gson().toJson(classBean));
                bundle.putString("plan", new Gson().toJson(programBean));
                bundle.putString("people", String.valueOf(count));
                bundle.putString("remark", editRemark.getText().toString().trim());
                Navigation.findNavController(v).navigate(R.id.action_industryIntroduceFragment_to_industryCalendarNewFragment, bundle);
                break;
            case R.id.btn_detail:
                bundle.putString("classBean", new Gson().toJson(classBean));
//                bundle.putString("plan", new Gson().toJson(programBean));
//                bundle.putString("people", String.valueOf(count));
                Navigation.findNavController(v).navigate(R.id.action_industryIntroduceFragment_to_industryDetailFragment, bundle);
//                Toast.makeText(getActivity(),"目前無詳細說明內容",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        count = 1;
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
    {
        ArrayList<ProgramBean> dataList;
        Button btn;
        Context context;


        public RecyclerAdapter(final ArrayList<ProgramBean> dataList, final Button btn, final Context context)
        {
            this.dataList = dataList;
            this.btn = btn;
            this.context = context;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView txtName;
            CheckBox checkBox;

            public ViewHolder(@NonNull @NotNull View itemView)
            {
                super(itemView);
                txtName = itemView.findViewById(R.id.tv_name);
                checkBox = itemView.findViewById(R.id.checkBox);
            }
        }

        @NonNull
        @NotNull
        @Override
        public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
        {
            return new RecyclerAdapter.ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_industry_select, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerAdapter.ViewHolder holder, int i)
        {
            holder.txtName.setText(dataList.get(i).getProgramName() + "\n" + "價位：$" + dataList.get(i).getProgramPrice());
            holder.checkBox.setChecked(dataList.get(i).isCheck());

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {

                    if (b)
                    {
                        dataList.get(holder.getAdapterPosition()).setCheck(true);
                    } else
                    {
                        dataList.get(holder.getAdapterPosition()).setCheck(false);
                    }
                }
            });

            btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    String serviceItem ="";
//                    for (int j = 0; j < dataList.size(); j++)
//                    {
//                        if (dataList.get(j).isCheck()){
//                            serviceItem += dataList.get(j).getServiceName()+dataList.get(j).getServicePrice()+"///";
//                        }
//                    }
//                    serviceItem = Optional.ofNullable(serviceItem)
//                            .filter(s -> s.length() != 0)
//                            .map(s -> s.substring(0, s.length() - 3))
//                            .orElse(serviceItem);
                    Bundle bundle = new Bundle();
                    for (int j = 0; j < dataList.size(); j++)
                    {
                        if (dataList.get(j).isCheck() == true)
                        {
                            bundle.putString("classBean", new Gson().toJson(classBean));
                            bundle.putString("plan", new Gson().toJson(dataList));
                            controller = Navigation.findNavController(v);
                            controller.navigate(R.id.action_industryIntroduceFragment_to_industryCalendarNewFragment, bundle);
                            break;
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return dataList.size();
        }
    }
}