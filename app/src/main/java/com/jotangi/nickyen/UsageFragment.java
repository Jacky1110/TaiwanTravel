package com.jotangi.nickyen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jotangi.nickyen.api.ApiEnqueue;
import com.jotangi.nickyen.home.model.CouponListBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsageFragment extends Fragment implements View.OnClickListener {

    private String TAG = UsageFragment.class.getSimpleName() + "(TAG)";

    private ApiEnqueue apiEnqueue;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView txtNoData;
    private ImageButton btnGoBack;

    static ArrayList<CouponListBean> commodityList = new ArrayList<>();
    static ArrayList<CouponListBean> moneyList = new ArrayList<>();
    static ArrayList<CouponListBean> pointList = new ArrayList<>();


    private String radioButtonType = "1";

    ArrayList data = new ArrayList();
    UsageAdapter usageAdapter;

    public UsageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usage, container, false);
        initView(view);
        loadCouponData();
        return view;
    }

    private void initView(View v) {
        apiEnqueue = new ApiEnqueue();
        btnGoBack = v.findViewById(R.id.ib_go_back);
        btnGoBack.setOnClickListener(this);
        progressBar = v.findViewById(R.id.progressBar);
        txtNoData = v.findViewById(R.id.tv_noData);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));

    }

    private void loadCouponData() {
        apiEnqueue.storeCouponUsestate(new ApiEnqueue.resultListener() {
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
                                UsageModel model = new UsageModel();
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                model.title = jsonObject.getString("coupon_name");
                                model.receive = jsonObject.getString("total_coupon");
                                model.receiveUse = jsonObject.getString("use_coupon");
                                model.imgCoupon = jsonObject.getString("coupon_picture");
                                data.add(model);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    usageAdapter = new UsageAdapter();
                                    usageAdapter.setmData(data);
                                    recyclerView.setAdapter(usageAdapter);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    txtNoData.setVisibility(View.VISIBLE);
                                    recyclerView.setAdapter(null);
                                }
                            });

                        }
                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void layoutViews2() {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_go_back:
                Navigation.findNavController(v).popBackStack();
                break;
        }
    }
}